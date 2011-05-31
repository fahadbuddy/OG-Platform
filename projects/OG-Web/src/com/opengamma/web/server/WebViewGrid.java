/**
 * Copyright (C) 2009 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.web.server;

import it.unimi.dsi.fastutil.longs.LongArraySet;
import it.unimi.dsi.fastutil.longs.LongSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicReference;

import org.cometd.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opengamma.engine.ComputationTargetSpecification;
import com.opengamma.engine.ComputationTargetType;
import com.opengamma.engine.depgraph.DependencyGraph;
import com.opengamma.engine.depgraph.DependencyGraphExplorer;
import com.opengamma.engine.depgraph.DependencyNode;
import com.opengamma.engine.value.ComputedValue;
import com.opengamma.engine.value.ValueProperties;
import com.opengamma.engine.value.ValueRequirement;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.engine.view.ViewCalculationConfiguration;
import com.opengamma.engine.view.ViewDefinition;
import com.opengamma.engine.view.ViewTargetResultModel;
import com.opengamma.engine.view.calc.ComputationCacheQuery;
import com.opengamma.engine.view.calc.ComputationCacheResponse;
import com.opengamma.engine.view.calc.EngineResourceReference;
import com.opengamma.engine.view.calc.ViewCycle;
import com.opengamma.engine.view.client.ViewClient;
import com.opengamma.engine.view.compilation.CompiledViewDefinition;
import com.opengamma.engine.view.compilation.CompiledViewDefinitionWithGraphs;
import com.opengamma.id.UniqueIdentifier;
import com.opengamma.util.ArgumentChecker;
import com.opengamma.util.tuple.Pair;
import com.opengamma.web.server.conversion.ConversionMode;
import com.opengamma.web.server.conversion.ResultConverter;
import com.opengamma.web.server.conversion.ResultConverterCache;

/**
 * Stores state relating to an individual grid in a web client instance.
 */
public abstract class WebViewGrid {
  private static final Logger s_logger = LoggerFactory.getLogger(WebViewGrid.class);
  private static final String GRID_STRUCTURE_ROOT_CHANNEL = "/gridStructure";
  private static final String UPDATES_ROOT_CHANNEL = "/updates";
  private static final int HISTORY_SIZE = 20;
  
  private final String _name;
  
  private final String _updateChannel;
  private final String _columnStructureChannel;
  
  private final WebViewGridStructure _gridStructure;
  private final ResultConverterCache _resultConverterCache;
  private final ViewClient _viewClient;
  private final Client _local;
  private final Client _remote;
  private final String _nullCellValue;
  
  // Row-based state
  private final AtomicReference<SortedMap<Long, Long>> _viewportMap = new AtomicReference<SortedMap<Long, Long>>();
  
  // Column-based state: few entries expected so using an array set 
  private final LongSet _historyOutputs = new LongArraySet();
  
  // Cell-based state
  private final Set<WebGridCell> _fullConversionModeCells = new CopyOnWriteArraySet<WebGridCell>();
  private final Set<WebGridCell> _explainCells = new CopyOnWriteArraySet<WebGridCell>();
  private final Map<WebGridCell, SortedMap<Long, Object>> _cellValueHistory = new HashMap<WebGridCell, SortedMap<Long, Object>>();
  
  protected WebViewGrid(String name, ViewClient viewClient, CompiledViewDefinition compiledViewDefinition, List<UniqueIdentifier> targets,
      EnumSet<ComputationTargetType> targetTypes, ResultConverterCache resultConverterCache, Client local,
      Client remote, String nullCellValue) {
    ArgumentChecker.notNull(name, "name");
    ArgumentChecker.notNull(viewClient, "viewClient");
    ArgumentChecker.notNull(compiledViewDefinition, "compiledViewDefinition");
    ArgumentChecker.notNull(targetTypes, "targetTypes");
    ArgumentChecker.notNull(resultConverterCache, "resultConverterCache");
    ArgumentChecker.notNull(local, "local");
    ArgumentChecker.notNull(remote, "remote");
    
    _name = name;
    _viewClient = viewClient;
    _updateChannel = UPDATES_ROOT_CHANNEL + "/" + name;
    _columnStructureChannel = GRID_STRUCTURE_ROOT_CHANNEL + "/" + name + "/columns";
    
    List<WebViewGridColumnKey> requirements = getRequirements(compiledViewDefinition.getViewDefinition(), targetTypes);    
    _gridStructure = new WebViewGridStructure(compiledViewDefinition, targetTypes, requirements, targets);
    
    _resultConverterCache = resultConverterCache;
    _local = local;
    _remote = remote;
    _nullCellValue = nullCellValue;
  }
  
  public String getName() {
    return _name;
  }
  
  //-------------------------------------------------------------------------

  public void processTargetResult(ComputationTargetSpecification target, ViewTargetResultModel resultModel, Long resultTimestamp) {
    Long rowId = getGridStructure().getRowId(target.getUniqueId());
    if (rowId == null) {
      // Result not in the grid
      return;
    }
    boolean rowInViewport = getViewport().containsKey(rowId);
    Long lastHistoryTime = getViewport().get(rowId);
    Map<String, Object> valuesToSend = null;
    if (rowInViewport) {
      valuesToSend = new HashMap<String, Object>();
      valuesToSend.put("rowId", rowId);
    }
    
    for (String configName : resultModel.getCalculationConfigurationNames()) {
      for (ComputedValue value : resultModel.getAllValues(configName)) {
        ValueSpecification specification = value.getSpecification();
        WebViewGridColumn column = getGridStructure().getColumn(configName, specification);
        if (column == null) {
          s_logger.warn("Could not find column for calculation configuration {} with value specification {}", configName, specification);
          continue;
        }
        
        long colId = column.getId();
        
        // s_logger.debug("{} {} = {} {}", new Object[] {target.getUniqueId(), columnName, value.getValue().getValue(), value.getValue().getSpecification().getProperties()});

        WebGridCell cell = WebGridCell.of(rowId, colId);
        
        ConversionMode mode = getConversionMode(cell);
        Object originalValue = value.getValue();
        ResultConverter<Object> converter = originalValue != null ? getConverter(column, value.getSpecification().getValueName(), originalValue.getClass()) : null;

        Object displayValue;
        if (originalValue != null) {
          try {
            displayValue = converter.convertForDisplay(_resultConverterCache, value.getSpecification(), originalValue, mode);
          } catch (Exception e) {
            s_logger.error("Exception when converting: ", e);
            displayValue = "Conversion Error";
          }
        } else {
          displayValue = null;
        }
        
        boolean isHistoryOutput = isHistoryOutput(colId);
        if (isHistoryOutput) {
          Object historyValue;
          if (originalValue != null) {
            historyValue = converter.convertForHistory(_resultConverterCache, value.getSpecification(), originalValue);
          } else {
            historyValue = null;
          }
          addCellHistory(cell, resultTimestamp, historyValue);
        }
        
        boolean isExplain = isExplain(cell);
        Object explainRows = null;
        if (isExplain) {
          explainRows = getJsonExplainRows(configName, value.getSpecification());
        }
        
        Object cellValue;
        if (rowInViewport) {
          // Client requires this row
          if (isHistoryOutput || isExplain) {
            Map<String, Object> cellData = new HashMap<String, Object>();
            cellData.put("display", displayValue);
            SortedMap<Long, Object> history = getCellHistory(cell, lastHistoryTime);
            if (history != null) {
              cellData.put("history", history.values());
            }
            if (explainRows != null) {
              cellData.put("explain", explainRows);
            }
            cellValue = cellData;
          } else {
            cellValue = displayValue;
          }

          if (cellValue != null) {
            valuesToSend.put(Long.toString(colId), cellValue);
          }
        }
      }
    }
    if (rowInViewport) {
      _remote.deliver(_local, _updateChannel, valuesToSend, null);
    }
  }
  
  private Object getJsonExplainRows(String calcConfigName, ValueSpecification valueSpecification) {
    // TODO: this may not be the cycle corresponding to the result - some tracking of cycle IDs required
    EngineResourceReference<? extends ViewCycle> cycleReference = getViewClient().createLatestCycleReference();
    if (cycleReference == null) {
      return null;
    }
    ViewCycle viewCycle = cycleReference.get();
    CompiledViewDefinitionWithGraphs compiledViewDefinition = viewCycle.getCompiledViewDefinition();
    DependencyGraphExplorer explorer = compiledViewDefinition.getDependencyGraphExplorer(calcConfigName);
    DependencyGraph subgraph = explorer.getSubgraphProducing(valueSpecification);
    if (subgraph == null) {
      s_logger.warn("No subgraph producing value specification {}", valueSpecification);
      return null;
    }
    DependencyNode outputNode = subgraph.getNodeProducing(valueSpecification);
    if (outputNode == null) {
      s_logger.warn("Subgraph does not contain a node producing specification {}", valueSpecification);
    }
    ComputationCacheQuery valueQuery = new ComputationCacheQuery();
    valueQuery.setCalculationConfigurationName(calcConfigName);
    valueQuery.setValueSpecifications(subgraph.getOutputSpecifications());
    ComputationCacheResponse valueResponse = viewCycle.queryComputationCaches(valueQuery);
    cycleReference.release();
    
    Map<ValueSpecification, Object> valueMap = new HashMap<ValueSpecification, Object>();
    for (Pair<ValueSpecification, Object> valuePair : valueResponse.getResults()) {
      valueMap.put(valuePair.getFirst(), valuePair.getSecond());
    }
    List<Object> rows = new ArrayList<Object>();
    rows.add(getJsonExplainRow(0, -1, outputNode, valueSpecification, valueMap.get(valueSpecification), 0));
    addInputJsonExplainRows(subgraph, outputNode, 0, 1, rows, valueMap);
    return rows;
  }
  
  private void addInputJsonExplainRows(DependencyGraph graph, DependencyNode node, int parentRowId, int indent, List<Object> rows, Map<ValueSpecification, Object> valueMap) {
    for (ValueSpecification inputSpec : node.getInputValues()) {
      DependencyNode inputNode = graph.getNodeProducing(inputSpec);
      if (inputNode == null) {
        s_logger.warn("Subgraph does not contain input node producing {}", inputSpec);
      }
      int rowId = rows.size();
      rows.add(getJsonExplainRow(rowId, parentRowId, inputNode, inputSpec, valueMap.get(inputSpec), indent));
      addInputJsonExplainRows(graph, inputNode, rowId, indent + 1, rows, valueMap);
    }
  }
  
  private Object getJsonExplainRow(int id, int parentRowId, DependencyNode node, ValueSpecification valueSpecification, Object value, int indent) {
    Map<String, Object> columns = new HashMap<String, Object>();
    String targetName = node.getComputationTarget().getName();
    if (targetName == null) {
      targetName = node.getComputationTarget().getUniqueId().toString();
    }
    String targetType = getTargetTypeName(node.getComputationTarget().getType());
    String functionName = node.getFunction().getFunction().getFunctionDefinition().getShortName();
    columns.put("rowId", id);
    if (parentRowId > -1) {
      columns.put("parentRowId", parentRowId);
    }
    columns.put("indent", indent);
    columns.put("targetType", targetType);
    columns.put("target", targetName);
    columns.put("function", functionName);
    columns.put("valueName", valueSpecification.getValueName().toString());
    columns.put("properties", valueSpecification.getProperties().toString());
    columns.put("value", value);
    return columns;
  }
  
  private String getTargetTypeName(ComputationTargetType targetType) {
    switch (targetType) {
      case PORTFOLIO_NODE:
        return "Agg";
      case POSITION:
        return "Pos";
      case SECURITY:
        return "Sec";
      case PRIMITIVE:
        return "Prim";
      default:
        return null;
    }
  }
  
  @SuppressWarnings("unchecked")
  private ResultConverter<Object> getConverter(WebViewGridColumn column, String valueName, Class<?> valueType) {
    // Ensure the converter is cached against the value name before sending the column details 
    ResultConverter<Object> converter = (ResultConverter<Object>) _resultConverterCache.getAndCacheConverter(valueName, valueType);
    if (!column.isTypeKnown()) {
      sendColumnDetails(Collections.singleton(column));
    }
    return converter;
  }
  
  public ConversionMode getConversionMode(WebGridCell cell) {
    return _fullConversionModeCells.contains(cell)
        ? ConversionMode.FULL
        : ConversionMode.SUMMARY;
  }
  
  public void setConversionMode(WebGridCell cell, ConversionMode mode) {
    if (mode == ConversionMode.SUMMARY) {
      _fullConversionModeCells.remove(cell);
    } else {
      _fullConversionModeCells.add(cell);
    }
  }
  
  public boolean isExplain(WebGridCell cell) {
    return _explainCells.contains(cell);
  }
  
  public void setExplain(WebGridCell cell, boolean isEnabled) {
    // Ensure view cycle access is supported before the new entry is inserted, to avoid a race condition
    if (_explainCells.size() == 0 && isEnabled) {
      getViewClient().setViewCycleAccessSupported(true);
    }
    if (isEnabled) {
      _explainCells.add(cell);
    } else {
      _explainCells.remove(cell);
    }
    if (_explainCells.size() == 0) {
      getViewClient().setViewCycleAccessSupported(false);
    }
  }
  
  //-------------------------------------------------------------------------
  
  public Object getJsonGridStructure() {
    Map<String, Object> gridStructure = new HashMap<String, Object>();
    gridStructure.put("name", getName());
    gridStructure.put("rows", getJsonRowStructures());
    gridStructure.put("columns", getJsonColumnStructures(getGridStructure().getColumns()));
    return gridStructure;
  }

  private void sendColumnDetails(Collection<WebViewGridColumn> columnDetails) {
    _remote.deliver(_local, _columnStructureChannel, getJsonColumnStructures(columnDetails), null);
  }
  
  private Map<String, Object> getJsonColumnStructures(Collection<WebViewGridColumn> columns) {
    Map<String, Object> columnStructures = new HashMap<String, Object>();
    for (WebViewGridColumn columnDetails : columns) {
      columnStructures.put(Long.toString(columnDetails.getId()), getJsonColumnStructure(columnDetails));
    }
    return columnStructures;
  }
  
  private Map<String, Object> getJsonColumnStructure(WebViewGridColumn column) {
    Map<String, Object> detailsToSend = new HashMap<String, Object>();
    long colId = column.getId();
    detailsToSend.put("colId", colId);
    detailsToSend.put("header", column.getHeader());
    detailsToSend.put("description", column.getDescription());
    detailsToSend.put("nullValue", _nullCellValue);
    
    String resultType = _resultConverterCache.getKnownResultTypeName(column.getValueName());
    if (resultType != null) {
      column.setTypeKnown(true);
      detailsToSend.put("dataType", resultType);
      
      // Hack - the client should decide which columns it requires history for, taking into account the capabilities of
      // the renderer.
      if (resultType.equals("DOUBLE")) {
        addHistoryOutput(column.getId());
      }
    }
    return detailsToSend;
  }

  private List<Object> getJsonRowStructures() {
    List<Object> rowStructures = new ArrayList<Object>();
    for (Map.Entry<UniqueIdentifier, Long> targetEntry : getGridStructure().getTargets().entrySet()) {
      Map<String, Object> rowDetails = new HashMap<String, Object>();
      UniqueIdentifier target = targetEntry.getKey();
      long rowId = targetEntry.getValue();
      rowDetails.put("rowId", rowId);
      addRowDetails(target, rowId, rowDetails);
      rowStructures.add(rowDetails);
    }
    return rowStructures;
  }
  
  protected abstract void addRowDetails(UniqueIdentifier target, long rowId, Map<String, Object> details);
  
  //-------------------------------------------------------------------------
  
  public SortedMap<Long, Long> getViewport() {
    return _viewportMap.get();
  }
  
  public void setViewport(SortedMap<Long, Long> viewportMap) {
    _viewportMap.set(viewportMap);
  }
  
  protected WebViewGridStructure getGridStructure() {
    return _gridStructure;
  }
  
  //-------------------------------------------------------------------------
  
  private void addHistoryOutput(long colId) {
    _historyOutputs.add(colId);
  }
  
  private boolean isHistoryOutput(long colId) {
    return _historyOutputs.contains(colId);
  }
  
  private void addCellHistory(WebGridCell cell, Long timestamp, Object value) {
    SortedMap<Long, Object> history = _cellValueHistory.get(cell);
    if (history == null) {
      history = new TreeMap<Long, Object>();
      _cellValueHistory.put(cell, history);
    }
    if (history.size() > HISTORY_SIZE) {
      history.remove(history.entrySet().iterator().next().getKey());
    }
    history.put(timestamp, value);
  }
  
  private SortedMap<Long, Object> getCellHistory(WebGridCell cell, Long lastTimestamp) {
    SortedMap<Long, Object> history = _cellValueHistory.get(cell);
    if (history == null) {
      return null;
    }
    if (lastTimestamp == null) {
      return history;
    }
    return history.tailMap(lastTimestamp + 1);
  }
  
  private ViewClient getViewClient() {
    return _viewClient;
  }

  //-------------------------------------------------------------------------

  private static List<WebViewGridColumnKey> getRequirements(ViewDefinition viewDefinition, EnumSet<ComputationTargetType> targetTypes) {
    List<WebViewGridColumnKey> result = new ArrayList<WebViewGridColumnKey>();
    for (ViewCalculationConfiguration calcConfig : viewDefinition.getAllCalculationConfigurations()) {
      String calcConfigName = calcConfig.getName();
      if (targetTypes.contains(ComputationTargetType.POSITION) || targetTypes.contains(ComputationTargetType.PORTFOLIO_NODE)) {
        for (Pair<String, ValueProperties> portfolioOutput : calcConfig.getAllPortfolioRequirements()) {
          String valueName = portfolioOutput.getFirst();
          ValueProperties constraints = portfolioOutput.getSecond();
          WebViewGridColumnKey columnKey = new WebViewGridColumnKey(calcConfigName, valueName, constraints);
          result.add(columnKey);
        }
      }
      
      for (ValueRequirement specificRequirement : calcConfig.getSpecificRequirements()) {
        if (!targetTypes.contains(specificRequirement.getTargetSpecification().getType())) {
          continue;
        }
        String valueName = specificRequirement.getValueName();
        ValueProperties constraints = specificRequirement.getConstraints();
        WebViewGridColumnKey columnKey = new WebViewGridColumnKey(calcConfigName, valueName, constraints);
        result.add(columnKey);
      }
    }
    return result;
  }
    
}

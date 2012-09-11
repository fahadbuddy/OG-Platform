/**
 * Copyright (C) 2012 - present by OpenGamma Inc. and the OpenGamma group of companies
 *
 * Please see distribution for license.
 */
package com.opengamma.integration.copiernew.tool;

import com.opengamma.component.tool.AbstractTool;
import com.opengamma.financial.tool.ToolContext;
import com.opengamma.integration.copiernew.Writeable;
import com.opengamma.integration.copiernew.portfolio.PortfolioMasterReader;
import com.opengamma.integration.copiernew.portfoliocontent.PortfolioContent;
import com.opengamma.integration.copiernew.portfoliocontent.PortfolioContentMasterReader;
import com.opengamma.integration.copiernew.portfoliocontent.PortfolioContentMasterWriter;
import com.opengamma.integration.copiernew.security.SecurityMasterWriter;
import com.opengamma.master.portfolio.PortfolioSearchRequest;

public class TestCopierTool extends AbstractTool<ToolContext> {

  /**
   * Main method to run the tool.
   *
   * @param args  the arguments, not null
   */
  public static void main(String[] args) { //CSIGNORE
    new TestCopierTool().initAndRun(args, ToolContext.class);
    System.exit(0);
  }

  @Override
  protected void doRun() throws Exception {

    ToolContext sourceContext = getToolContext(0);
    ToolContext destContext = getToolContext(1);

    PortfolioSearchRequest portfolioSearchRequest = new PortfolioSearchRequest();
    portfolioSearchRequest.setName("A KV Test");
    Iterable<PortfolioContent> portfolioContentReader = new PortfolioContentMasterReader(
        new PortfolioMasterReader(sourceContext.getPortfolioMaster(), portfolioSearchRequest),
        sourceContext.getPositionMaster(),
        sourceContext.getSecuritySource()
    );
    Writeable<PortfolioContent> portfolioContentWriter = new PortfolioContentMasterWriter(
        destContext.getPortfolioMaster(),
        destContext.getPositionMaster(),
        new SecurityMasterWriter(destContext.getSecurityMaster()),
        "Ex-"
    );
    portfolioContentWriter.addOrUpdate(portfolioContentReader);
    portfolioContentWriter.flush();


    // Configuration copier
//    ConfigSearchRequest<Object> searchRequest = new ConfigSearchRequest<Object>();
//    searchRequest.setType(Object.class);
//    searchRequest.setName("*DJX*");
//    Iterable<ObjectsPair<String, Object>> reader =
//        new ConfigMasterReader<Object>(getToolContext().getConfigMaster(), searchRequest);
//    Writeable<ObjectsPair<String, Object>> writer =
//        new ConfigMasterWriter<Object>(getToolContext().getConfigMaster(), "KV <name>");
//    writer.addOrUpdate(reader);


    // Multi time-series copier
//    HistoricalTimeSeriesInfoSearchRequest searchRequest = new HistoricalTimeSeriesInfoSearchRequest();
//    searchRequest.setName("KV*");
//    Iterable<ObjectsPair<ManageableHistoricalTimeSeriesInfo, Iterable<LocalDateDoubleTimeSeries>>>
//        reader = new HistoricalTimeSeriesMasterReader(
//            getToolContext().getHistoricalTimeSeriesMaster(),
//            searchRequest
//        );
//    Writeable<ObjectsPair<ManageableHistoricalTimeSeriesInfo, Iterable<LocalDateDoubleTimeSeries>>>
//        writer = new HistoricalTimeSeriesMasterWriter(getToolContext().getHistoricalTimeSeriesMaster(), "KV<name>");
//    writer.addOrUpdate(reader);

//    // Time-series data point copier
//    Iterable<LocalDateDoubleTimeSeries> reader = new HistoricalTimeSeriesDataPointMasterReader(
//        getToolContext().getHistoricalTimeSeriesMaster(), UniqueId.of("DbHts", "1000")
//    );
//    ManageableHistoricalTimeSeriesInfo info = new ManageableHistoricalTimeSeriesInfo();
//    info.setName("KV Test");
//    info.setDataField("KVDATA");
//    info.setDataSource("KVSOURCE");
//    info.setDataProvider("KVPROVIDER");
//    info.setObservationTime("KVTIME");
//    info.setExternalIdBundle(ExternalIdBundleWithDates.of(ExternalIdWithDates.parse("KV~666")));
//    HistoricalTimeSeriesInfoDocument doc = getToolContext().getHistoricalTimeSeriesMaster().add(
//        new HistoricalTimeSeriesInfoDocument(info)
//    );
//    Writeable<LocalDateDoubleTimeSeries> writer = new HistoricalTimeSeriesDataPointMasterWriter(
//        getToolContext().getHistoricalTimeSeriesMaster(),
//        //UniqueId.parse("DbHts~11360")
//        doc.getInfo().getUniqueId()
//    );
//    writer.addOrUpdate(reader);

    // Holiday copier
//    HolidaySearchRequest searchRequest = new HolidaySearchRequest();
//    searchRequest.setName("ADALV");
//    Iterable<ManageableHoliday> reader =
//        new HolidayMasterReader(getToolContext().getHolidayMaster(), searchRequest);
//    Writeable<ManageableHoliday> writer =
//        new HolidayMasterWriter(getToolContext().getHolidayMaster());
//    writer.addOrUpdate(reader);

    // Portfolio copier (no contents)
//    PortfolioSearchRequest searchRequest = new PortfolioSearchRequest();
//    searchRequest.setName("Example Equity Portfolio*");
//    Iterable<ManageablePortfolio> reader =
//        new PortfolioMasterReader(getToolContext().getPortfolioMaster(), searchRequest);
//    Writeable<ManageablePortfolio> writer =
//        new PortfolioMasterWriter(getToolContext().getPortfolioMaster(), "<name> KV");
//    writer.addOrUpdate(reader);

    // Portfolio content copier (multiple portfolios)
//    PortfolioSearchRequest portfolioSearchRequest = new PortfolioSearchRequest();
//    portfolioSearchRequest.setName("Example Equity Portfolio*");
//    Iterable<PortfolioContent> portfolioContentReader = new PortfolioContentMasterReader(
//        new PortfolioMasterReader(getToolContext().getPortfolioMaster(), portfolioSearchRequest),
//        getToolContext().getPositionMaster(),
//        getToolContext().getSecuritySource()
//    );
//    Writeable<PortfolioContent> portfolioContentWriter = new PortfolioContentMasterWriter(
//        getToolContext().getPortfolioMaster(),
//        getToolContext().getPositionMaster(),
//        new SecurityMasterWriter(getToolContext().getSecurityMaster()),
//        "Ex-"
//    );
//    portfolioContentWriter.addOrUpdate(portfolioContentReader);
//    portfolioContentWriter.flush();


    // Portfolio nodepositionsecurity copier (single portfolio)
//    PortfolioDocument sourcePortfolioDocument = getToolContext().getPortfolioMaster().get(UniqueId.of("DbPrt", "122711"));
//    ManageablePortfolio destPortfolio;
//      destPortfolio = getToolContext().getPortfolioMaster().get(UniqueId.of("DbPrt", "123001")).getPortfolio();
////      destPortfolio = new ManageablePortfolio("KVKVKVKVKV Test");
//    Iterable<NodePositionSecurity> nodePositionSecurityReader = new NodePositionSecurityMasterReader(
//        getToolContext().getPositionMaster(),
//        getToolContext().getSecuritySource(),
//        sourcePortfolioDocument.getPortfolio().getRootNode()
//    );
//    ManageablePortfolioNode root = new ManageablePortfolioNode();
//    Writeable<NodePositionSecurity> nodePositionSecurityWriter = new NodePositionSecurityMasterWriter(
//        getToolContext().getPositionMaster(),
//        new SecurityMasterWriter(getToolContext().getSecurityMaster()),
//        root, destPortfolio.getRootNode()
//    );
//    nodePositionSecurityWriter.addOrUpdate(nodePositionSecurityReader);
//    new PortfolioMasterWriter(getToolContext().getPortfolioMaster()).addOrUpdate(
//        new ManageablePortfolio("KVKVKVKVKV Test", root)
//    );


    // Security copier
//    SecuritySearchRequest searchRequest = new SecuritySearchRequest();
//    //searchRequest.setSecurityType("EQUITY");
//    //searchRequest.setExternalIdScheme("GLOBEOP_SECID");
//    searchRequest.addObjectId(ObjectId.parse("DbSec~23239"));
//    searchRequest.addObjectId(ObjectId.parse("DbSec~1406"));
//    Iterable<ManageableSecurity> reader =
//        new SecurityMasterReader(getToolContext().getSecurityMaster(), searchRequest);
//    Writeable<ManageableSecurity> writer =
//        new SecurityMasterWriter(getToolContext().getSecurityMaster());
//    writer.addOrUpdate(reader);


    // Position copier
//    PositionSearchRequest searchRequest = new PositionSearchRequest();
//    //searchRequest.setSecurityType("EQUITY");
//    //searchRequest.setExternalIdScheme("GLOBEOP_SECID");
//    searchRequest.addPositionObjectId(ObjectId.parse("DbPos~1100"));
//    searchRequest.addPositionObjectId(ObjectId.parse("DbPos~1087"));
//    Iterable<ManageablePosition> reader =
//        new PositionMasterReader(getToolContext().getPositionMaster(), searchRequest);
//    Writeable<ManageablePosition> writer =
//        new PositionMasterWriter(getToolContext().getPositionMaster(), true);
//    writer.addOrUpdate(reader);


    // Export securities
//    SecuritySearchRequest searchRequest = new SecuritySearchRequest();
//    //searchRequest.setSecurityType("EQUITY");
//    //searchRequest.setExternalIdScheme("GLOBEOP_SECID");
//    searchRequest.addObjectId(ObjectId.parse("DbSec~23239"));
//    searchRequest.addObjectId(ObjectId.parse("DbSec~1406"));
//    Iterable<ManageableSecurity> reader =
//        new SecurityMasterReader(getToolContext().getSecurityMaster(), searchRequest);
//
//    RowWriter<ManageableSecurity> securityRowWriter =
//        new SecurityRowWriter(EquitySecurity.class, SwapSecurity.class);
//    Writeable<ManageableSecurity> writer =
//        new SheetWriter<ManageableSecurity>(new CsvRawSheetWriter("test-mixed 3.csv", securityRowWriter.getColumns()),
//            securityRowWriter);


    // Import securities
//    Iterable<ManageableSecurity> reader =
//        new SheetReader<ManageableSecurity>(new CsvRawSheetReader("test-mixed 3.csv"),
//            new SecurityRowReader(EquitySecurity.class, SwapSecurity.class));
//    Writeable<ManageableSecurity> writer =
//        new SecurityMasterWriter(getToolContext().getSecurityMaster());
//
//    new Copier<ManageableSecurity>().copy(reader, writer);


    // Copy exchanges
//    ExchangeSearchRequest searchRequest = new ExchangeSearchRequest();
//    searchRequest.setName("Abu Dhabi Securities Exchange TEST");
//    Iterable<ManageableExchange> masterReader =
//        new ExchangeMasterReader(getToolContext().getExchangeMaster(), searchRequest);
//    Writeable<ManageableExchange> masterWriter =
//        new ExchangeMasterWriter(getToolContext().getExchangeMaster(), "<name> 2");
//    masterWriter.addOrUpdate(masterReader);

//    RowWriter<ManageableExchange> rowWriter =
//        new ExchangeRowWriter();
//    Writeable<ManageableExchange> sheetWriter =
//        new SheetWriter<ManageableExchange>(new CsvRawSheetWriter("test.csv", rowWriter.getColumns()), rowWriter);
//    new Copier<ManageableExchange>().copy(masterReader, sheetWriter);


    // Does not function correctly since details are omitted and beancompare detects details in existing exchanges
//    Iterable<ManageableExchange> reader =
//        new SheetReader<ManageableExchange>(new CsvRawSheetReader("test.csv"), new ExchangeRowReader());
//    Writeable<ManageableExchange> writer =
//        new ExchangeMasterWriter(getToolContext().getExchangeMaster());
//    new Copier<ManageableExchange>().copy(reader, writer);


//    writer.flush();
  }

}
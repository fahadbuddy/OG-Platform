/**
 * Copyright (C) 2011 - present by OpenGamma Inc. and the OpenGamma group of companies
 * 
 * Please see distribution for license.
 */
package com.opengamma.financial.view.rest;

import java.net.URI;

import com.opengamma.engine.depgraph.DependencyGraph;
import com.opengamma.engine.depgraph.DependencyGraphExplorer;
import com.opengamma.engine.value.ValueSpecification;
import com.opengamma.util.rest.FudgeRestClient;

/**
 * Remote implementation of {@link DependencyGraphExplorer}.
 */
public class RemoteDependencyGraphExplorer implements DependencyGraphExplorer {

  private final URI _baseUri;
  private final FudgeRestClient _client;
  
  public RemoteDependencyGraphExplorer(URI baseUri) {
    _baseUri = baseUri;
    _client = FudgeRestClient.create();
  }
  
  @Override
  public DependencyGraph getSubgraphProducing(ValueSpecification output) {
    String encodedValueSpec = _client.encode(output);
    URI uri = DataDependencyGraphExplorerResource.uriSubgraph(_baseUri, encodedValueSpec);
    return _client.access(uri).get(DependencyGraph.class);
  }

}

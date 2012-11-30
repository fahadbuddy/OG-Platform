package com.opengamma.util.csv;

import static org.testng.AssertJUnit.assertEquals;

import java.util.List;

import org.fudgemsg.FudgeContext;
import org.fudgemsg.FudgeMsg;
import org.fudgemsg.MutableFudgeMsg;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.google.common.collect.Lists;
import com.opengamma.util.fudgemsg.OpenGammaFudgeContext;

@Test
public class CSVDocumentReaderTest {
  
  private static final FudgeContext s_fudgeContext = OpenGammaFudgeContext.getInstance();
  private List<FudgeMsg> _expectedRows;
  
  @BeforeMethod
  public void setUp() {
    
    _expectedRows = Lists.newArrayList();
    
    MutableFudgeMsg row = s_fudgeContext.newMessage();
    row.add("Name", "Kirk");
    row.add("JobTitle", "CEO");
    _expectedRows.add(row);
    
    row = s_fudgeContext.newMessage();
    row.add("Name", "Jim");
    row.add("JobTitle", "CTO");
    _expectedRows.add(row);
    
    row = s_fudgeContext.newMessage();
    row.add("Name", "Elaine");
    row.add("JobTitle", "CQO");
    _expectedRows.add(row);
    
  }
  
  public void csvReader() {
    List<FudgeMsg> actualRows = Lists.newArrayList();
    
    CSVDocumentReader reader = new CSVDocumentReader(CSVDocumentReaderTest.class.getResource("TestCSV.txt"));
    for (FudgeMsg row : reader) {
      actualRows.add(row);
    }
    assertEquals(_expectedRows, actualRows);        
  }

}
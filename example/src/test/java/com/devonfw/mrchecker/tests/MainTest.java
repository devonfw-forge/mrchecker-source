package com.devonfw.mrchecker.tests;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.capgemini.mrchecker.test.core.BaseTest;
import com.devonfw.mrchecker.common.data.SampleObject;
import com.devonfw.mrchecker.common.mapper.CsvMapper;

import junitparams.FileParameters;
import junitparams.JUnitParamsRunner;

@RunWith(JUnitParamsRunner.class)
public class MainTest extends BaseTest {

  /**
   * Method executed before the tests
   * Useful to do initial configurations
   * */
  @Override
  public void setUp() {
    // TODO: Initial configurations

  }

  /**
   * Method executed after the tests
   * Useful to do cleaning operations
   * */
  @Override
  public void tearDown() {
    // TODO: Cleaning operations

  }

  /**
   * Simple test to check if the constructor works correctly
   * */
  @Test
  public void Test_withoutMapper() {
    String attribute1 = "value1";
    String attribute2 = "value2";
    SampleObject sobject = new SampleObject(attribute1, attribute2);
    Assert.assertTrue(sobject.getAttribute1().equals(attribute1));
    Assert.assertTrue(sobject.getAttribute2().equals(attribute2));
  }

  /**
   * Test to check the attributes of and object written
   * as a CSV file
   *
   * This test is executed once for each line in the CSV (excepting the header).
   * */
  @Test
  @FileParameters(value = "src/test/resources/datadriven/test_sample.csv", mapper = CsvMapper.class)
  public void Test_withMapper(SampleObject sobject) {
    String attribute1 = "value1";
    String attribute2 = "value2";
    Assert.assertTrue(sobject.getAttribute1().equals(attribute1));
    Assert.assertTrue(sobject.getAttribute2().equals(attribute2));
  }

}

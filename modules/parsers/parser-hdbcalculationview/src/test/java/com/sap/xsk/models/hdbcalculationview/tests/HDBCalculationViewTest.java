/*
 * Copyright (c) 2019-2021 SAP SE or an SAP affiliate company and XSK contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, v2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-FileCopyrightText: 2019-2021 SAP SE or an SAP affiliate company and XSK contributors
 * SPDX-License-Identifier: Apache-2.0
 */
package com.sap.xsk.models.hdbcalculationview.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import com.sap.ndb.bimodel.calculation.CalculationScenario;
import com.sap.ndb.bimodel.calculation.CalculationViewType;
import com.sap.ndb.bimodel.calculation.DataSource;
import com.sap.ndb.bimodel.calculation.DataSources;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import org.junit.Test;

public class HDBCalculationViewTest {

  @Test
  public void serialize() throws JAXBException {
    CalculationScenario calculationScenario = new CalculationScenario();
    calculationScenario.setId("com.sap.xsk.samples::XSK_HDI_SIMPLE_CALC_VIEW");
    calculationScenario.setOutputViewType(CalculationViewType.PROJECTION);
    DataSource dataSource = new DataSource();
    dataSource.setId("XSK_HDI_SIMPLE_TABLE");
    dataSource.setResourceUri("XSK_HDI_SIMPLE_TABLE");
    DataSources dataSources = new DataSources();
    dataSources.getDataSource().add(dataSource);
    calculationScenario.setDataSources(dataSources);

    StringWriter writer = new StringWriter();
    JAXBContext context = JAXBContext.newInstance(CalculationScenario.class);
    Marshaller m = context.createMarshaller();
    m.marshal(calculationScenario, writer);

    System.out.println(writer.toString());

  }

  @Test
  public void deserialize() throws JAXBException {
//		String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?><ns2:calculationScenario outputViewType=\"Projection\" id=\"com.sap.xsk.samples::XSK_HDI_SIMPLE_CALC_VIEW\" xmlns:ns2=\"http://www.sap.com/ndb/BiModelCalculation.ecore\"><dataSources><DataSource id=\"XSK_HDI_SIMPLE_TABLE\"><resourceUri>XSK_HDI_SIMPLE_TABLE</resourceUri></DataSource></dataSources></ns2:calculationScenario>";

    String xml = "";
    try {
      xml = org.apache.commons.io.IOUtils.toString(HDBCalculationViewTest.class.getResourceAsStream("/test.hdbcalculationview"));
    } catch (IOException e) {
      fail("Parsing of calculation view failed.");
      e.printStackTrace();
    }

    xml = xml.replace("<Calculation:scenario", "<Calculation:calculationScenario");
    xml = xml.replace("</Calculation:scenario>", "</Calculation:calculationScenario>");

    JAXBContext context = JAXBContext.newInstance(CalculationScenario.class);
    Unmarshaller um = context.createUnmarshaller();
    CalculationScenario calculationScenario = (CalculationScenario) um.unmarshal(new StringReader(xml));

    assertEquals(calculationScenario.getId(), "com.sap.hana.example::projection");
    assertEquals(calculationScenario.getDataSources().getDataSource().get(0).getResourceUri(), "com.sap.hana.example::TAB1");
  }

}

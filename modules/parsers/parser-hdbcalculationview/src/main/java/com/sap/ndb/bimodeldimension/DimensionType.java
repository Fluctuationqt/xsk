/*
 * Copyright (c) 2021 SAP SE or an SAP affiliate company and XSK contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, v2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * SPDX-FileCopyrightText: 2021 SAP SE or an SAP affiliate company and XSK contributors
 * SPDX-License-Identifier: Apache-2.0
 */
//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.3.0 
// See <a href="https://javaee.github.io/jaxb-v2/">https://javaee.github.io/jaxb-v2/</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2020.11.26 at 10:54:28 AM EET 
//


package com.sap.ndb.bimodeldimension;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlEnumValue;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for DimensionType.
 *
 * <p>The following schema fragment specifies the expected content contained within this class.
 * <p>
 * <pre>
 * &lt;simpleType name="DimensionType"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}NMTOKEN"&gt;
 *     &lt;enumeration value="Unknown"/&gt;
 *     &lt;enumeration value="Time"/&gt;
 *     &lt;enumeration value="Measure"/&gt;
 *     &lt;enumeration value="Standard"/&gt;
 *     &lt;enumeration value="Geography"/&gt;
 *     &lt;enumeration value="Customer"/&gt;
 *     &lt;enumeration value="Product"/&gt;
 *     &lt;enumeration value="Organization"/&gt;
 *     &lt;enumeration value="Employee"/&gt;
 *     &lt;enumeration value="Currency"/&gt;
 *     &lt;enumeration value="Channel"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 */
@XmlType(name = "DimensionType")
@XmlEnum
public enum DimensionType {

  @XmlEnumValue("Unknown")
  UNKNOWN("Unknown"),
  @XmlEnumValue("Time")
  TIME("Time"),
  @XmlEnumValue("Measure")
  MEASURE("Measure"),
  @XmlEnumValue("Standard")
  STANDARD("Standard"),
  @XmlEnumValue("Geography")
  GEOGRAPHY("Geography"),
  @XmlEnumValue("Customer")
  CUSTOMER("Customer"),
  @XmlEnumValue("Product")
  PRODUCT("Product"),
  @XmlEnumValue("Organization")
  ORGANIZATION("Organization"),
  @XmlEnumValue("Employee")
  EMPLOYEE("Employee"),
  @XmlEnumValue("Currency")
  CURRENCY("Currency"),
  @XmlEnumValue("Channel")
  CHANNEL("Channel");
  private final String value;

  DimensionType(String v) {
    value = v;
  }

  public static DimensionType fromValue(String v) {
    for (DimensionType c : DimensionType.values()) {
      if (c.value.equals(v)) {
        return c;
      }
    }
    throw new IllegalArgumentException(v);
  }

  public String value() {
    return value;
  }

}

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
package com.sap.xsk.hdb.ds.transformer.hdbdd;

public enum CdsTypeEnum {
  String("NVARCHAR"),
  Binary("VARBINARY"),
  LargeBinary("BLOB"),
  Integer("INTEGER"),
  Integer64("BIGINT"),
  Decimal("DECIMAL"),
  DecimalFloat("DECIMAL"),
  LocalDate("DATE"),
  LocalTime("TIME"),
  UTCDateTime("SECONDDATE"),
  UTCTimestamp("TIMESTAMP"),
  Boolean("BOOLEAN");

  CdsTypeEnum(java.lang.String sqlType) {
    this.sqlType = sqlType;
  }

  private String sqlType;

  public java.lang.String getSqlType() {
    return sqlType;
  }
}

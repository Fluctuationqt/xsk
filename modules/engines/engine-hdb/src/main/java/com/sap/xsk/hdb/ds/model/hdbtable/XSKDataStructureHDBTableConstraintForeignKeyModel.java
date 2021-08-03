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
package com.sap.xsk.hdb.ds.model.hdbtable;

/**
 * The Data Structure Table Constraint Foreign Key Model.
 */
public class XSKDataStructureHDBTableConstraintForeignKeyModel extends XSKDataStructureHDBTableConstraintModel {

  private String referencedTable;

  private String[] referencedColumns;

  /**
   * Gets the referenced table.
   *
   * @return the referenced table
   */
  public String getReferencedTable() {
    return referencedTable;
  }

  /**
   * Sets the referenced table.
   *
   * @param referencedTable the new referenced table
   */
  public void setReferencedTable(String referencedTable) {
    this.referencedTable = referencedTable;
  }

  /**
   * Gets the referenced columns.
   *
   * @return the referenced columns
   */
  public String[] getReferencedColumns() {
    return (referencedColumns != null) ? referencedColumns.clone() : null;
  }

  /**
   * Sets the referenced columns.
   *
   * @param referencedColumns the new referenced columns
   */
  public void setReferencedColumns(String[] referencedColumns) {
    this.referencedColumns = referencedColumns;
  }

}

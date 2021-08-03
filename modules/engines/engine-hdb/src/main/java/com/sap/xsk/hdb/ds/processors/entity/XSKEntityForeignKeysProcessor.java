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
package com.sap.xsk.hdb.ds.processors.entity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.eclipse.dirigible.database.sql.SqlFactory;
import org.eclipse.dirigible.database.sql.builders.table.AlterTableBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sap.xsk.hdb.ds.model.hdbdd.XSKDataStructureEntityModel;
import com.sap.xsk.hdb.ds.model.hdbtable.XSKDataStructureHDBTableConstraintForeignKeyModel;
import com.sap.xsk.hdb.ds.processors.AbstractXSKProcessor;
import com.sap.xsk.utils.XSKHDBUtils;

/**
 * The Entity Create Processor.
 */
public class XSKEntityForeignKeysProcessor extends AbstractXSKProcessor<XSKDataStructureEntityModel> {

  private static final Logger logger = LoggerFactory.getLogger(XSKEntityForeignKeysProcessor.class);

  private XSKEntityForeignKeysProcessor() {
  }

  /**
   * Execute the corresponding statement.
   *
   * @param connection  the connection
   * @param entityModel the entity model
   * @throws SQLException the SQL exception
   */
  public void execute(Connection connection, XSKDataStructureEntityModel entityModel) throws SQLException {
    String tableName = XSKHDBUtils.getTableName(entityModel);
    logger.info("Processing Foreign Keys to the Table: {}", tableName);
//		CreateTableBuilder createTableBuilder = SqlFactory.getNative(connection).create().table(tableName);

    List<XSKDataStructureHDBTableConstraintForeignKeyModel> foreignKeys = entityModel.getConstraints().getForeignKeys();
    for (XSKDataStructureHDBTableConstraintForeignKeyModel foreignKeyModel : foreignKeys) {

      String sourceTable = XSKHDBUtils.getTableName(entityModel);
      String name = "FK_" + sourceTable + "_" + tableName;
      sourceTable = XSKHDBUtils.escapeArtifactName(connection, sourceTable);

      boolean existing = SqlFactory.getNative(connection).exists(connection, sourceTable);
      if (existing) {
        AlterTableBuilder alterTableBuilder = SqlFactory.getNative(connection).alter().table(sourceTable);
        alterTableBuilder.add().foreignKey(name, foreignKeyModel.getColumns(), tableName, foreignKeyModel.getReferencedColumns());

        String sql = alterTableBuilder.build();
        executeSql(sql, connection);
      } else {
        String reason = "Table does not exist - " + sourceTable;
        logger.error(reason);
        throw new SQLException(reason);
      }
    }
  }

}

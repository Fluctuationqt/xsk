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
package com.sap.xsk.hdb.ds.parser.hdbtable;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.sap.xsk.hdb.ds.model.XSKDBContentType;
import com.sap.xsk.hdb.ds.model.hdbtable.*;
import com.sap.xsk.parser.hdbtable.core.HdbtableLexer;
import com.sap.xsk.parser.hdbtable.core.HdbtableParser;
import com.sap.xsk.parser.hdbtable.custom.XSKHDBTABLECoreVisitor;
import com.sap.xsk.parser.hdbtable.custom.XSKHDBTABLESyntaxErrorListener;
import com.sap.xsk.parser.hdbtable.exceptions.XSKHDBTableMissingPropertyException;
import com.sap.xsk.parser.hdbtable.model.XSKHDBTABLEColumnsModel;
import com.sap.xsk.parser.hdbtable.model.XSKHDBTABLEIndexesModel;
import com.sap.xsk.utils.XSKConstants;
import com.sap.xsk.utils.XSKHDBUtils;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.sap.xsk.parser.hdbtable.model.XSKHDBTABLEDefinitionModel;
import com.sap.xsk.hdb.ds.api.IXSKDataStructureModel;
import com.sap.xsk.hdb.ds.api.XSKDataStructuresException;
import com.sap.xsk.hdb.ds.parser.XSKDataStructureParser;


public class XSKTableParser implements XSKDataStructureParser {

  private static final Logger logger = LoggerFactory.getLogger(XSKTableParser.class);

  @Override
  public String getType() {
    return IXSKDataStructureModel.TYPE_HDB_TABLE;
  }

  @Override
  public Class<XSKDataStructureHDBTableModel> getDataStructureClass() {
    return XSKDataStructureHDBTableModel.class;
  }

  @Override
  public XSKDataStructureHDBTableModel parse(String location, String content) throws XSKDataStructuresException, IOException {
    String expectedHanaXSAdvancedSyntax =
        XSKConstants.XSK_HDBTABLE_SYNTAX + "\"" + XSKHDBUtils.getRepositoryBaseObjectName(location) + "\"";
    String receivedSyntax = XSKHDBUtils.extractRepositoryBaseObjectNameFromContent(XSKConstants.XSK_HDBTABLE_SYNTAX, content);
    logger.debug("Determine if the hdbtable is Hana XS Classic or Hana XS Advanced by Comparing '" + receivedSyntax + "' with '"
        + expectedHanaXSAdvancedSyntax + "'");

    return (receivedSyntax.equals(expectedHanaXSAdvancedSyntax))
        ? parseHanaXSAdvancedContent(location, content)
        : parseHanaXSClassicContent(location, content);
  }

  private XSKDataStructureHDBTableModel parseHanaXSClassicContent(String location, String content)
      throws IOException, XSKDataStructuresException {
    ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
    ANTLRInputStream inputStream = new ANTLRInputStream(is);
    HdbtableLexer hdbtableLexer = new HdbtableLexer(inputStream);
    CommonTokenStream tokenStream = new CommonTokenStream(hdbtableLexer);

    HdbtableParser hdbtableParser = new HdbtableParser(tokenStream);
    hdbtableParser.setBuildParseTree(true);
    hdbtableParser.removeErrorListeners();

    XSKHDBTABLESyntaxErrorListener xskhdbtableSyntaxErrorListener = new XSKHDBTABLESyntaxErrorListener();
    hdbtableParser.addErrorListener(xskhdbtableSyntaxErrorListener);
    ParseTree parseTree = hdbtableParser.hdbtableDefinition();

    if (hdbtableParser.getNumberOfSyntaxErrors() > 0) {
      String errorMessage = xskhdbtableSyntaxErrorListener.getErrorMessage();
      throw new XSKDataStructuresException(errorMessage);
    }

    XSKHDBTABLECoreVisitor xskhdbtableCoreVisitor = new XSKHDBTABLECoreVisitor();

    JsonElement parsedResult = xskhdbtableCoreVisitor.visit(parseTree);

    Gson gson = new Gson();

    XSKHDBTABLEDefinitionModel hdbtableDefinitionModel = gson.fromJson(parsedResult, XSKHDBTABLEDefinitionModel.class);
    try {
      hdbtableDefinitionModel.checkForAllMandatoryFieldsPresence();
    } catch (Exception e) {
      throw new XSKHDBTableMissingPropertyException(String.format("Wrong format of table definition: [%s]. [%s]", location, e.getMessage()));
    }

    XSKDataStructureHDBTableModel dataStructureHDBTableModel = new XSKDataStructureHDBTableModel();

    List<XSKDataStructureHDBTableColumnModel> columns = new ArrayList<>();
    for( XSKHDBTABLEColumnsModel column : hdbtableDefinitionModel.getColumns()) {
      try {
        column.checkForAllMandatoryColumnFieldsPresence();
      } catch (Exception e) {
        throw new XSKHDBTableMissingPropertyException(String.format("Wrong format of table definition: [%s]. [%s]", location, e.getMessage()));
      }
      XSKDataStructureHDBTableColumnModel dataStructureHDBTableColumnModel = new XSKDataStructureHDBTableColumnModel();
      dataStructureHDBTableColumnModel.setLength(column.getLength());
      dataStructureHDBTableColumnModel.setName(column.getName());
      dataStructureHDBTableColumnModel.setType(column.getSqlType());
      dataStructureHDBTableColumnModel.setComment(column.getComment());
      dataStructureHDBTableColumnModel.setNullable(column.isNullable());
      dataStructureHDBTableColumnModel.setDefaultValue(column.getDefaultValue());
      dataStructureHDBTableColumnModel.setPrecision(column.getPrecision());
      dataStructureHDBTableColumnModel.setScale(column.getScale());
      dataStructureHDBTableColumnModel.setUnique(column.isUnique());
      columns.add(dataStructureHDBTableColumnModel);
    }

    dataStructureHDBTableModel.setSchema(hdbtableDefinitionModel.getSchemaName());
    dataStructureHDBTableModel.setDescription(hdbtableDefinitionModel.getDescription());
    dataStructureHDBTableModel.setLoggingType(hdbtableDefinitionModel.getLoggingType());
    dataStructureHDBTableModel.setPublicProp(hdbtableDefinitionModel.getPublicProp());
    dataStructureHDBTableModel.setTemporary(hdbtableDefinitionModel.getTemporary());
    dataStructureHDBTableModel.setTableType(hdbtableDefinitionModel.getTableType());

    dataStructureHDBTableModel.setColumns(columns);
    dataStructureHDBTableModel.setConstraints(new XSKDataStructureHDBTableConstraintsModel());

    XSKDataStructureHDBTableConstraintPrimaryKeyModel primaryKey = new XSKDataStructureHDBTableConstraintPrimaryKeyModel();
    primaryKey.setColumns(hdbtableDefinitionModel.getPkcolumns().toArray(String[]::new));
    primaryKey.setName("PK_"+ dataStructureHDBTableModel.getName());
    dataStructureHDBTableModel.getConstraints().setPrimaryKey(primaryKey);


    List<XSKDataStructureHDBTableConstraintUniqueModel> uniqueIndices = new ArrayList<>();

    if(hdbtableDefinitionModel.getIndexes()!=null) {
      for (XSKHDBTABLEIndexesModel index : hdbtableDefinitionModel.getIndexes()) {
        try {
          index.checkForAllIndexMandatoryFieldsPresence();
        } catch (Exception e) {
          throw new XSKHDBTableMissingPropertyException(String.format("Wrong format of table definition: [%s]. [%s]", location, e.getMessage()));
        }
        XSKDataStructureHDBTableConstraintUniqueModel uniqueIndex = new XSKDataStructureHDBTableConstraintUniqueModel();
        uniqueIndex.setName(index.getIndexName());
        uniqueIndex.setColumns(index.getIndexColumns().toArray(String[]::new));
        uniqueIndices.add(uniqueIndex);
      }
      dataStructureHDBTableModel.getConstraints().setUniqueIndices(uniqueIndices);
    }
    setXSKDataStructureHDBTableModelDetails(location, content, XSKDBContentType.XS_CLASSIC, dataStructureHDBTableModel);

    return dataStructureHDBTableModel;
  }

  private XSKDataStructureHDBTableModel parseHanaXSAdvancedContent(String location, String content) {
    XSKDataStructureHDBTableModel dataStructureHDBTableModel = new XSKDataStructureHDBTableModel();
    setXSKDataStructureHDBTableModelDetails(location, content, XSKDBContentType.OTHERS, dataStructureHDBTableModel);
    dataStructureHDBTableModel.setRawContent(content);
    return dataStructureHDBTableModel;
  }

  private void setXSKDataStructureHDBTableModelDetails(String location, String content, XSKDBContentType dbContentType, XSKDataStructureHDBTableModel dataStructureHDBTableModel) {
    dataStructureHDBTableModel.setName(XSKHDBUtils.getRepositoryBaseObjectName(location));
    dataStructureHDBTableModel.setLocation(location);
    dataStructureHDBTableModel.setType(IXSKDataStructureModel.TYPE_HDB_TABLE);
    dataStructureHDBTableModel.setHash(DigestUtils.md5Hex(content));
    dataStructureHDBTableModel.setCreatedBy(UserFacade.getName());
    dataStructureHDBTableModel.setCreatedAt(new Timestamp(new java.util.Date().getTime()));
    dataStructureHDBTableModel.setDbContentType(dbContentType);
  }
}


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
package com.sap.xsk.hdb.ds.parser.hdbview;

import com.sap.xsk.hdb.ds.api.IXSKDataStructureModel;
import com.sap.xsk.hdb.ds.api.XSKDataStructuresException;
import com.sap.xsk.hdb.ds.model.XSKDBContentType;
import com.sap.xsk.hdb.ds.model.hdbview.XSKDataStructureHDBViewModel;
import com.sap.xsk.hdb.ds.parser.XSKDataStructureParser;
import com.sap.xsk.parser.hdbview.core.HdbviewLexer;
import com.sap.xsk.parser.hdbview.core.HdbviewParser;
import com.sap.xsk.parser.hdbview.custom.XSKHDBVIEWCoreListener;
import com.sap.xsk.parser.hdbview.custom.XSKHDBVIEWSyntaxErrorListener;
import com.sap.xsk.parser.hdbview.models.XSKHDBVIEWDefinitionModel;
import com.sap.xsk.utils.XSKConstants;
import com.sap.xsk.utils.XSKHDBUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.apache.commons.codec.digest.DigestUtils;
import org.eclipse.dirigible.api.v3.security.UserFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XSKViewParser implements XSKDataStructureParser {

  private static final Logger logger = LoggerFactory.getLogger(XSKViewParser.class);

  @Override
  public XSKDataStructureHDBViewModel parse(String location, String content) throws XSKDataStructuresException, IOException {
    String expectedHana2Syntax = XSKConstants.XSK_HDBVIEW_SYNTAX + "\"" + XSKHDBUtils.getRepositoryBaseObjectName(location) + "\"";
    String receivedHana2Syntax = XSKHDBUtils.extractRepositoryBaseObjectNameFromContent(XSKConstants.XSK_HDBVIEW_SYNTAX, content);
    logger.debug("Determine if the hdbview is Hana v1 or v2 by Comparing '" + receivedHana2Syntax + "' with '" + expectedHana2Syntax + "'");
    if (receivedHana2Syntax.equals(expectedHana2Syntax)) {
      return parseHANAv2Content(location, content);
    } else {
      return parseHANAv1Content(location, content);
    }
  }

  private XSKDataStructureHDBViewModel parseHANAv2Content(String location, String content) {
    XSKDataStructureHDBViewModel hdbViewModel = new XSKDataStructureHDBViewModel();
    populateXSKDataStructureHDBViewModel(location, content, hdbViewModel);
    hdbViewModel.setDbContentType(XSKDBContentType.OTHERS);
    hdbViewModel.setRawContent(content);
    return hdbViewModel;
  }


  private XSKDataStructureHDBViewModel parseHANAv1Content(String location, String content) throws XSKDataStructuresException, IOException {
    XSKDataStructureHDBViewModel hdbViewModel = new XSKDataStructureHDBViewModel();
    populateXSKDataStructureHDBViewModel(location, content, hdbViewModel);
    hdbViewModel.setDbContentType(XSKDBContentType.XS_CLASSIC);

    ByteArrayInputStream is = new ByteArrayInputStream(content.getBytes());
    ANTLRInputStream inputStream = new ANTLRInputStream(is);
    HdbviewLexer hdbviewLexer = new HdbviewLexer(inputStream);
    CommonTokenStream tokenStream = new CommonTokenStream(hdbviewLexer);

    HdbviewParser hdbviewParser = new HdbviewParser(tokenStream);
    hdbviewParser.setBuildParseTree(true);
    hdbviewParser.removeErrorListeners();

    XSKHDBVIEWSyntaxErrorListener xskhdbviewSyntaxErrorListener = new XSKHDBVIEWSyntaxErrorListener();
    hdbviewParser.addErrorListener(xskhdbviewSyntaxErrorListener);
    ParseTree parseTree = hdbviewParser.hdbviewDefinition();

    if (hdbviewParser.getNumberOfSyntaxErrors() > 0) {
      String syntaxError = xskhdbviewSyntaxErrorListener.getErrorMessage();
      throw new XSKDataStructuresException(String.format(
          "Wrong format of HDB View: [%s] during parsing. Ensure you are using the correct format for the correct compatibility version. [%s]",
          location, syntaxError));
    }

    XSKHDBVIEWCoreListener XSKHDBVIEWCoreListener = new XSKHDBVIEWCoreListener();
    ParseTreeWalker parseTreeWalker = new ParseTreeWalker();
    parseTreeWalker.walk(XSKHDBVIEWCoreListener, parseTree);

    XSKHDBVIEWDefinitionModel antlr4Model = XSKHDBVIEWCoreListener.getModel();
    try {
      antlr4Model.checkForAllMandatoryFieldsPresence();
    } catch (Exception e) {
      throw new XSKDataStructuresException(String.format("Wrong format of HDB View: [%s] during parsing. [%s]", location, e.getMessage()));
    }
    hdbViewModel.setQuery(antlr4Model.getQuery());
    hdbViewModel.setSchema(antlr4Model.getSchema());
    hdbViewModel.setPublic(antlr4Model.isPublic());
    hdbViewModel.setDependsOn(antlr4Model.getDependsOn());
    hdbViewModel.setDependsOnTable(antlr4Model.getDependsOnTable());
    hdbViewModel.setDependsOnView(antlr4Model.getDependsOnView());

    return hdbViewModel;
  }

  private void populateXSKDataStructureHDBViewModel(String location, String content, XSKDataStructureHDBViewModel hdbViewModel) {
    hdbViewModel.setName(XSKHDBUtils.getRepositoryBaseObjectName(location));
    hdbViewModel.setLocation(location);
    hdbViewModel.setType(IXSKDataStructureModel.TYPE_HDB_VIEW);
    hdbViewModel.setHash(DigestUtils.md5Hex(content));
    hdbViewModel.setCreatedBy(UserFacade.getName());
    hdbViewModel.setCreatedAt(new Timestamp(new java.util.Date().getTime()));
  }

  @Override
  public String getType() {
    return IXSKDataStructureModel.TYPE_HDB_VIEW;
  }

  @Override
  public Class getDataStructureClass() {
    return XSKDataStructureHDBViewModel.class;
  }
}
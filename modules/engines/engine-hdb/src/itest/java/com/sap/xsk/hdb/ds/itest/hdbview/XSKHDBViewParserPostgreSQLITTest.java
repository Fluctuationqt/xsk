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
package com.sap.xsk.hdb.ds.itest.hdbview;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.name.Names;
import com.sap.xsk.hdb.ds.api.XSKDataStructuresException;
import com.sap.xsk.hdb.ds.facade.IXSKHDBCoreFacade;
import com.sap.xsk.hdb.ds.itest.module.XSKHDBTestModule;
import com.sap.xsk.hdb.ds.itest.model.JDBCModel;
import com.sap.xsk.utils.XSKHDBUtils;
import org.eclipse.dirigible.core.scheduler.api.SynchronizationException;
import org.eclipse.dirigible.repository.local.LocalResource;
import org.junit.BeforeClass;
import org.junit.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import javax.sql.DataSource;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XSKHDBViewParserPostgreSQLITTest {

  private static PostgreSQLContainer jdbcContainer;
  private static Connection connection;
  private static IXSKHDBCoreFacade facade;


  @BeforeClass
  public static void setUp() throws SQLException {
    jdbcContainer =
        new PostgreSQLContainer<>("postgres:alpine");
    jdbcContainer.start();
    JDBCModel model = new JDBCModel(jdbcContainer.getDriverClassName(), jdbcContainer.getJdbcUrl(), jdbcContainer.getUsername(),
        jdbcContainer.getPassword());
    Injector injector = Guice.createInjector(new XSKHDBTestModule(model));
    connection = injector.getInstance(DataSource.class).getConnection();
    facade = injector.getInstance(Key.get(IXSKHDBCoreFacade.class, Names.named("xskHDBCoreFacade")));
  }


  @Test
  public void testHDBViewCreate() throws XSKDataStructuresException, SynchronizationException, IOException, SQLException {
    Statement stmt = connection.createStatement();
    stmt.executeUpdate("create table \"public\".\"acme.com.test.tables::MY_TABLE1\"(Column1 integer,Column2 integer)");
    stmt.executeUpdate("create table \"public\".\"acme.com.test.views::MY_VIEW1\"(Column1 integer,Column2 integer)");
    LocalResource resource = XSKHDBTestModule.getResources("/usr/local/target/dirigible/repository/root",
        "/registry/public/hdbview-itest/SamplePostgreXSClassicView.hdbview",
        "/hdbview-itest/SamplePostgreXSClassicView.hdbview");

    this.facade.handleResourceSynchronization(resource);
    this.facade.updateEntities();

    ResultSet rs = stmt
        .executeQuery(String.format("SELECT COUNT(*) as rawsCount FROM \"%s\"", "hdbview-itest::SamplePostgreXSClassicView"));
    assertTrue(rs.next());
    assertEquals(0, rs.getInt("rawsCount"));
    stmt.executeUpdate(
        String.format("DROP VIEW %s", XSKHDBUtils.escapeArtifactName(connection, "hdbview-itest::SamplePostgreXSClassicView")));
    stmt.executeUpdate("drop table \"public\".\"acme.com.test.tables::MY_TABLE1\"");
    stmt.executeUpdate("drop table \"public\".\"acme.com.test.views::MY_VIEW1\"");
  }
}

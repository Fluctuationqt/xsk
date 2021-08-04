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
package com.sap.xsk.migration.api.dto;

import javax.validation.constraints.NotNull;

public class MigrationRequestBody {

  private final NeoData neo;
  private final HanaData hana;

  public MigrationRequestBody(NeoData neo, HanaData hana) {
    this.neo = neo;
    this.hana = hana;
  }

  @NotNull
  public NeoData getNeo() {
    return neo;
  }

  @NotNull
  public HanaData getHana() {
    return hana;
  }
}

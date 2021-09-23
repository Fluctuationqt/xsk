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

var registry = require("platform/v4/registry");

exports.Job = function Job(path) {
  this.path = path;
  var job = JSON.parse(registry.getText(path));

//  com.sap.xsk.xsjob.ds.facade.XSKJobFacade.newJob(path, );

  this.activate = function(){
    com.sap.xsk.xsjob.ds.facade.XSKJobFacade.activate(this.path);
  }

  this.deactivate = function(){
    com.sap.xsk.xsjob.ds.facade.XSKJobFacade.deactivate(this.path);
  }
}
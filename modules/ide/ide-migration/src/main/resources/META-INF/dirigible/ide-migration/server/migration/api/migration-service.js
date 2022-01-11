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
const HanaRepository = require('ide-migration/server/migration/repository/hana-repository');
const workspaceManager = require("platform/v4/workspace");
const repositoryManager = require("platform/v4/repository");
const bytes = require("io/v4/bytes");
const database = require("db/v4/database");
const config = require("core/v4/configurations");
const HANA_USERNAME = "HANA_USERNAME";
const XSKHDBCoreFacade = Java.type("com.sap.xsk.hdb.ds.facade.XSKHDBCoreFacade");

class MigrationService {

    connection = null;
    repo = null;

    setupConnection(databaseName, databaseUser, databaseUserPassword, connectionUrl) {
        database.createDataSource(databaseName, "com.sap.db.jdbc.Driver", connectionUrl, databaseUser, databaseUserPassword, null);

        this.connection = database.getConnection('dynamic', databaseName);
        this.repo = new HanaRepository(this.connection);
    }

    getAllDeliveryUnits() {
        if (!this.repo) {
            throw new Error("Repository not initialized");
        }

        return this.repo.getAllDeliveryUnits();
    }

    copyAllFilesForDu(du, workspaceName) {
        if (!this.repo) {
            throw new Error("Repository not initialized");
        }

        let context = {};
        const filesAndPackagesObject = this.repo.getAllFilesForDu(context, du)
        this.dumpSourceFiles(workspaceName, filesAndPackagesObject.files, du)
    }

    dumpSourceFiles(workspaceName, lists, du) {
        let workspace;
        if (!workspaceName) {
            workspace = workspaceManager.getWorkspace(du.name)
            if (!workspace) {
                workspaceManager.createWorkspace(du.name)
                workspace = workspaceManager.getWorkspace(du.name)
            }
        }
        workspace = workspaceManager.getWorkspace(workspaceName);

        const deployables = [];
        const facade = new XSKHDBCoreFacade();
        for (let i = 0; i < lists.length; i++) {
            const file = lists[i];
            // each file's package id is based on its directory
            // if we do not get only the first part of the package id, we would have several XSK projects created for directories in the same XS app
            const projectName = file.packageId.split('.')[0];

            let project = workspace.getProject(projectName)
            if (!project) {
                workspace.createProject(projectName)
                project = workspace.getProject(projectName)
            }

            // Call parsers for each file
            const fileName = file._name + "." + file._suffix;
            const filePath = file._packageName.replaceAll('.', "/") + "/" + fileName;
            const fileContent = String.fromCharCode.apply(String, file._content).replace(/\0/g,'');

            if(fileName.endsWith(".hdbdd")) {
              console.log("IVO: Creating hdbdd duplicate in registry so that hdbdd parser can find them");
              console.log("IVO:" + "/registry/public/" + filePath + "  file_content: " + fileContent);
              // IVO: TODO: ADD HDBTI FILES HERE TOO FOR THE HDBDD PARSER IT NEEDS THEM?
              // IVO: TODO: ALSO DELETE FILES LIKE THIS AFTER USE !!
              repositoryManager.createResource("/registry/public/" + filePath, fileContent, 'text/plain');
            }
            const parsedData = facade.parseDataStructureModel(fileName, filePath, fileContent);
            const generatedSynonymFile = this.handleParsedData(parsedData, project);

            if (!deployables.find(x => x.projectName === projectName)) {
                deployables.push({
                    project: project,
                    projectName: projectName,
                    artifacts: []
                });
            }

            let fileRunLocation = file.RunLocation;

            if (fileRunLocation.startsWith("/" + projectName)) {
                // remove package id from file location in order to remove XSK project and folder nesting
                fileRunLocation = fileRunLocation.slice(projectName.length + 1);
            }

            let projectFile = project.createFile(fileRunLocation);
            projectFile.setContent(file._content);

            if (fileRunLocation.endsWith('hdbcalculationview')
                || fileRunLocation.endsWith('calculationview')) {
                deployables.find(x => x.projectName === projectName).artifacts.push(file.RunLocation);
            }

            if(generatedSynonymFile !== null && generatedSynonymFile !== undefined) {
                deployables.find(x => x.projectName === projectName).artifacts.push("/" + projectName + "/" + generatedSynonymFile);
            }
        }

        this.handlePossibleDeployableArtifacts(deployables);
    }

    handleParsedData(parsedData, project) {
        if(parsedData === null) {
            console.log("File could not be parsed, no synonym generated.");
            return;
        }

        const dataModelType = parsedData.getClass().getName();
        const hdbTableModel = "com.sap.xsk.hdb.ds.model.hdbtable.XSKDataStructureHDBTableModel";
        const hdbViewModel = "com.sap.xsk.hdb.ds.model.hdbview.XSKDataStructureHDBViewModel";
        const hdbTableTypeModel = "com.sap.xsk.hdb.ds.model.hdbtabletype.XSKDataStructureHDBTableTypeModel";
        const hdbDDModel = "com.sap.xsk.hdb.ds.model.hdbdd.XSKDataStructureCdsModel";

        console.log("IVO: Attempting to create synonym for Model: " + dataModelType);

        if(dataModelType == hdbTableModel) {
            return this.createHdbSynonymFile(project, parsedData.getName(), parsedData.getSchema());
        }
        else if(dataModelType == hdbViewModel) {
              return this.createHdbSynonymFile(project, parsedData.getName(), parsedData.getSchema());
        }
        else if(dataModelType == hdbTableTypeModel) {
            return this.createHdbSynonymFile(project, parsedData.getName(), parsedData.getSchema());
        }
        else if(dataModelType == hdbDDModel) {
            // TODO: foreach item in parsedData.tableModels and parsedData.tableTypeModles
            // TODO: generate a synonym file with the item's schema and name
            return;
        }

        console.log("File parsed, but no synonym generation required!");
        return;
    }

    handlePossibleDeployableArtifacts(deployables) {
        for (const deployable of deployables) {
            if (deployable.artifacts && deployable.artifacts.length > 0) {
                const hdiConfigPath = this.createHdiConfigFile(deployable.project);
                this.createHdiFile(deployable.project, hdiConfigPath, deployable.artifacts);
            }
        }
    }

    createHdiConfigFile(project) {
        const hdiConfig = {
            file_suffixes: {
                hdbcalculationview: {
                    plugin_name: "com.sap.hana.di.calculationview",
                    plugin_version: "12.1.0"
                },
                calculationview: {
                    plugin_name: "com.sap.hana.di.calculationview",
                    plugin_version: "12.1.0"
                },
                hdbsynonym: {
                    plugin_name: "com.sap.hana.di.synonym",
                    plugin_version: "12.1.0"
                }
            }
        };

        const projectName = project.getName();
        const hdiConfigPath = `${projectName}.hdiconfig`;
        const hdiConfigFile = project.createFile(hdiConfigPath);
        const hdiConfigJson = JSON.stringify(hdiConfig, null, 4);
        const hdiConfigJsonBytes = bytes.textToByteArray(hdiConfigJson);
        hdiConfigFile.setContent(hdiConfigJsonBytes);

        return hdiConfigPath;
    }

    createHdiFile(project, hdiConfigPath, deployables) {
        const projectName = project.getName();
        const defaultHanaUser = this.getDefaultHanaUser();

        const hdi = {
            configuration: `/${projectName}/${hdiConfigPath}`,
            users: [defaultHanaUser],
            group: projectName,
            container: projectName,
            deploy: deployables,
            undeploy: []
        };

        const hdiPath = `${projectName}.hdi`;
        const hdiFile = project.createFile(`${projectName}.hdi`);
        const hdiJson = JSON.stringify(hdi, null, 4);
        const hdiJsonBytes = bytes.textToByteArray(hdiJson);
        hdiFile.setContent(hdiJsonBytes);

        return hdiPath;
    }

    createHdbSynonymFile(project, name, schemaName) {
        // input name should be like: xsk-test-app::SamplePostgreXSClassicTable
        const viewName = name.split(':').pop();
        var hdbSynonym = {};
        hdbSynonym[name] = {
            "target" : {
                "object" : viewName,
                "schema" : schemaName
            }
        };

        const hdbSynonymPath = `${viewName}.hdbsynonym`;
        const hdbSynonymFile = project.createFile(`${viewName}.hdbsynonym`);
        const hdbSynonymJson = JSON.stringify(hdbSynonym, null, 4);
        const hdbSynonymJsonBytes = bytes.textToByteArray(hdbSynonymJson);
        hdbSynonymFile.setContent(hdbSynonymJsonBytes);

        return hdbSynonymPath;
    }

    getDefaultHanaUser() {
        return config.get(HANA_USERNAME, "DBADMIN");
    }

}

module.exports = MigrationService;



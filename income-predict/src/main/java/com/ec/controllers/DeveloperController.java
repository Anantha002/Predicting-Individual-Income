/*

 * JBoss, Home of Professional Open Source
 * Copyright 2015, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.ec.controllers;

import javax.enterprise.context.RequestScoped;
import org.apache.log4j.Logger;

import com.ec.bussiness.logic.EJBModelDao;
import com.ec.bussiness.logic.ModelDao;
import com.ec.bussiness.logic.User;
import com.ec.bussiness.logic.UserDao;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.annotation.ManagedBean;
import javax.ejb.EJB;

@Named
@RequestScoped
public class DeveloperController {
	/*
	 * @EJB EJBModelDao ejb_model;
	 */

	@Inject
	private ModelDao modelDao;

	private String inputModelPath;

	private String modelData;

	private String errorLog;

	final static Logger logger = Logger.getLogger(DeveloperController.class);

	/**
	 * 
	 * @brief model object is created and saved in Db.
	 */
	public void trainDataModel() throws FileNotFoundException, IOException {
		logger.info("Developer is requesting to retrieve trained model from HDFS and save as model object in Db");
		String result = modelDao.trainModelData(getinputModelPath(), getModelData());
		errorLog = result;
		logger.info(errorLog);
	}

	/**
	 * 
	 * @brief The dataset is trained and model is created.
	 */
	public void generateModel() {
		logger.info("The developer is requesting to train the dataset and create model ");
		String result = modelDao.invokeHadoop();
		errorLog = result;
		logger.info(errorLog);
	}

	public String getinputModelPath() {
		return inputModelPath;
	}

	public void setinputModelPath(String inputModelPath) {
		this.inputModelPath = inputModelPath;
	}

	public String getModelData() {
		return modelData;
	}

	public void setModelData(String modelData) {
		this.modelData = modelData;
	}

	public String getErrorLog() {
		return errorLog;
	}

}

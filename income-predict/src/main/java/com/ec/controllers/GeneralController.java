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
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.ec.bussiness.logic.ModelDao;
import com.ec.bussiness.logic.User;
import com.ec.bussiness.logic.UserDao;

@Named
@RequestScoped
public class GeneralController {

	@Inject
	private ModelDao modelDao;

	private String dataInput1;
	private String dataInput2;
	private String dataInput3;
	private String dataInput4;
	private String dataInput5;
	private String dataInput6;
	private String dataInput7;

	private String modelOutput;

	private String output;

	final static Logger logger = Logger.getLogger(GeneralController.class);

	/**
	 * 
	 * @brief predicts the income range based on input factors
	 */
	public void modelDataPredict() {
		logger.info("The user is requesting to predict income");
		String result = modelDao.predictModel(getDataInput1(), getDataInput2(), getDataInput3(), getDataInput4(),
				getDataInput5(), getDataInput6(), getDataInput7(), getModelOutput());
		output = result;
		logger.info(output);
	}

	public String getDataInput1() {
		return dataInput1;
	}

	public void setDataInput1(String dataInput1) {
		this.dataInput1 = dataInput1;
	}

	public String getDataInput2() {
		return dataInput2;
	}

	public void setDataInput2(String dataInput2) {
		this.dataInput2 = dataInput2;
	}

	public String getDataInput3() {
		return dataInput3;
	}

	public void setDataInput3(String dataInput3) {
		this.dataInput3 = dataInput3;
	}

	public String getDataInput4() {
		return dataInput4;
	}

	public void setDataInput4(String dataInput4) {
		this.dataInput4 = dataInput4;
	}

	public String getDataInput5() {
		return dataInput5;
	}

	public void setDataInput5(String dataInput5) {
		this.dataInput5 = dataInput5;
	}

	public String getDataInput6() {
		return dataInput6;
	}

	public void setDataInput6(String dataInput6) {
		this.dataInput6 = dataInput6;
	}

	public String getDataInput7() {
		return dataInput7;
	}

	public void setDataInput7(String dataInput7) {
		this.dataInput7 = dataInput7;
	}

	public String getModelOutput() {
		return modelOutput;
	}

	public void setModelOutput(String modelOutput) {
		this.modelOutput = modelOutput;
	}

	public String getOutput() {
		return output;
	}

}

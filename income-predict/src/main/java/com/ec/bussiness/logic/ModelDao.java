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
package com.ec.bussiness.logic;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * @class ModelDao
 * @brief This is the interface for ModelDao class.
 * @file ModelDoa.java
 */
public interface ModelDao {

	String trainModelData(String input_data, String Output_data) throws FileNotFoundException, IOException;

	String predictModel(String in_1, String in_2, String in_3, String in_4, String in_5, String in_6, String in_7,
			String Model_name);

	String invokeHadoop();
}

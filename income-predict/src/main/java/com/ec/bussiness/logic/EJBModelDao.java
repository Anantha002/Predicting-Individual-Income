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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.inject.Alternative;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.NotSupportedException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;

@Stateful
@Alternative
public class EJBModelDao implements ModelDao {

	@Inject
	private EntityManager obj_em;

	@Inject
	private UserTransaction obj_utx;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "";

	public String invokeHadoop() {
		try {
			ProcessBuilder process_builder = new ProcessBuilder("C:/enterprise/hadoop-2.7.1/bin/hadoop.cmd", "jar",
					"C:/tmp/enterprise/hadoop/kmeans-hd.jar", "com.ec.lab.KmeansMR", "/ec", "/result2/");
			process_builder.start();
			String out = "Model is being trained. Please wait for 5 minutues and check the output in Hadoop filesytem";
			return out;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "failed";
		}

	}

	public String trainModelData(String model_path, String model_name) throws FileNotFoundException, IOException {
		String hdfsPath = "hdfs://localhost:9000";
		String result = "";
		Configuration conf = new Configuration();
		conf.set("fs.defaultFS", hdfsPath);

		// get FileSystem
		FileSystem fs = FileSystem.get(conf);

		// read a file
		Path path = new Path(model_path);
		if (!fs.exists(path)) {
			System.out.println("File" + " does not exists");

		} else {
			FSDataInputStream in = fs.open(path);
			byte[] b = new byte[1024];
			int numBytes = 0;
			String s = new String();
			while ((numBytes = in.read(b)) > 0) {
				s += new String(b);

			}

			String[] data = s.split("\t");

			List<String> objList = new ArrayList<String>();
			for (String value : data) {
				if (value.contains("\n")) {
					String[] res = value.split("\n");
					if (!res[1].isEmpty())
						objList.add(res[1]);
				} else {
					objList.add(value);
				}

			}

			Model objCluster = new Model();
			List<NestedModel> list_Model = new ArrayList<NestedModel>();

			for (String value : objList) {
				if (!value.isEmpty() && !value.equals("") && !value.equals(null) && value.contains(",")) {
					String[] res = value.split(",");
					NestedModel objModel = new NestedModel();
					String[] data_list = new String[8];
					data_list[0] = res[0];
					data_list[1] = res[1];
					data_list[2] = res[2];
					data_list[3] = res[3];

					data_list[4] = res[4];
					data_list[5] = res[5];
					data_list[6] = res[6];
					data_list[7] = res[7];

					objModel.setData(data_list);
					list_Model.add(objModel);
				}
			}
			objCluster.setData(list_Model);

			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ObjectOutput out = null;
			try {
				out = new ObjectOutputStream(bos);
				out.writeObject(objCluster);
				out.flush();
			} finally {
				try {
					bos.close();
				} catch (IOException ex) {
					// ignore close exception
				}
			}

			ModelSet objModel = new ModelSet();
			objModel.setName("test");
			objModel.setClassname(model_name);
			objModel.setObject(bos.toByteArray());
			objModel.setDate(new Timestamp(System.currentTimeMillis()));

			try {
				obj_utx.begin();
				obj_em.persist(objModel);
			} catch (NotSupportedException | SystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					obj_utx.commit();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			result = "Data is trained Successfully and is stored in the database";
		}
		return result + model_path + model_name;

	}

	public String predictModel(String in_1, String in_2, String in_3, String in_4, String in_5, String in_6,
			String in_7, String Model_name) {

		System.out.println("\n" + in_3 + "," + in_4 + "," + in_5);
		String final_result = "";
		try {
			ModelSet ms = new ModelSet();

			Query obq = obj_em.createQuery("select u from ModelSet u where u.classname = :classname");
			obq.setParameter("classname", Model_name);
			ms = (ModelSet) obq.getSingleResult();

			Model obj_model = new Model();

			ByteArrayInputStream bais = new ByteArrayInputStream(ms.getObject());
			ObjectInputStream ois = new ObjectInputStream(bais);
			obj_model = (Model) ois.readObject();

			int input_1 = 0;
			int input_2 = 0;
			int input_3 = 0;
			int input_4 = 0;
			int input_5 = 0;
			int input_6 = 0;
			int input_7 = 0;

			double[] newValue = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue[input_1] = Math.abs(Double.parseDouble(in_1) - Double.parseDouble(objModel.getData()[0]));
				input_1++;
			}

			double[] newValue2 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue2[input_2] = Math.abs(Double.parseDouble(in_2) - Double.parseDouble(objModel.getData()[1]));
				input_2++;
			}

			double[] newValue3 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue3[input_3] = Math.abs(Double.parseDouble(in_3) - Double.parseDouble(objModel.getData()[2]));
				input_3++;
			}

			double[] newValue4 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue4[input_4] = Math.abs(Double.parseDouble(in_4) - Double.parseDouble(objModel.getData()[3]));
				input_4++;
			}

			double[] newValue5 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue5[input_5] = Math.abs(Double.parseDouble(in_5) - Double.parseDouble(objModel.getData()[4]));
				input_5++;
			}
			double[] newValue6 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue6[input_6] = Math.abs(Double.parseDouble(in_6) - Double.parseDouble(objModel.getData()[5]));
				input_6++;
			}
			double[] newValue7 = new double[obj_model.getData().size()];
			for (NestedModel objModel : obj_model.getData()) {
				newValue7[input_7] = Math.abs(Double.parseDouble(in_7) - Double.parseDouble(objModel.getData()[6]));
				input_7++;
			}

			int len[] = new int[obj_model.getData().size()];

			for (int i : len) {
				i = 0;
			}

			for (int val = 0; val < newValue.length - 1; val++) {
				if (newValue[val] > 0 && newValue[val + 1] > 0) {
					if (newValue[val] < newValue[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue2.length - 1; val++) {
				if (newValue2[val] > 0 && newValue2[val + 1] > 0) {
					if (newValue2[val] < newValue2[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue3.length - 1; val++) {
				if (newValue3[val] > 0 && newValue3[val + 1] > 0) {
					if (newValue3[val] < newValue3[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue4.length - 1; val++) {
				if (newValue4[val] > 0 && newValue4[val + 1] > 0) {
					if (newValue4[val] < newValue4[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue5.length - 1; val++) {
				if (newValue5[val] > 0 && newValue5[val + 1] > 0) {
					if (newValue5[val] < newValue5[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue6.length - 1; val++) {
				if (newValue6[val] > 0 && newValue6[val + 1] > 0) {
					if (newValue6[val] < newValue6[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			for (int val = 0; val < newValue7.length - 1; val++) {
				if (newValue7[val] > 0 && newValue7[val + 1] > 0) {
					if (newValue7[val] < newValue7[val + 1]) {
						len[val]++;
					} else {
						len[val + 1]++;
					}
				}
			}

			int m = len[0];
			int n = 0;

			for (int j = 1; j < newValue.length; j++) {
				if (m < len[j]) {
					m = len[j];
					n = j;
				}
			}
			long l = (new Double(obj_model.getData().get(n).getData()[7])).longValue();
			String res;

			if (l <= 35000) {

				res = "\n--> Below 35000 CAD ";
			} else if (l > 35000 && l < 70000) {
				res = "\n -->  35000 to 70000 CAD";

			} else {
				res = "\n -->Above 70000 CAD";

			}

			final_result = res;

		} catch (Exception ex) {
			final_result = ex.toString();
		}

		return final_result;
	}

}

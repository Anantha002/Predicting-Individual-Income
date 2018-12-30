
package com.ec.bussiness.logic;

import java.io.BufferedInputStream;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.transaction.HeuristicMixedException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.log4j.Logger;

import com.ec.bussiness.logic.Model;
import com.ec.bussiness.logic.NestedModel;
import com.mysql.jdbc.PreparedStatement;

import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ConverterUtils.DataSource;
import org.apache.log4j.Logger;

/**
 * @class ManagedBeanModelDao
 * @brief Model training,saving model object and prediction logic are
 *        implemented here
 * @file ManagedBeanModelDao.java
 */

@ManagedBean
public class ManagedBeanModelDao implements ModelDao {

	@Inject
	private EntityManager obj_em;

	@Inject
	private UserTransaction obj_utx;

	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost:3306/test";
	static final String USER = "root";
	static final String PASS = "";

	/**
	 * 
	 * @brief invooke_hadoop function generates the trained model in HDFS.
	 */
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

	/**
	 * 
	 * @brief Model object is saved in Db
	 */
	public String trainModelData(String model_path, String model_name) throws FileNotFoundException, IOException {

		String path_hdfs = "hdfs://localhost:9000";
		String output = "";
		Configuration c = new Configuration();
		c.set("fs.defaultFS", path_hdfs);

		// get FileSystem
		FileSystem sys = FileSystem.get(c);

		// read a file
		Path path = new Path(model_path);
		if (!sys.exists(path)) {
			System.out.println("File" + " does not exists");
			output = "No such file in given location-->";

			return output + model_path;
		} else {
			FSDataInputStream in = sys.open(path);
			byte[] obj_bytes = new byte[1024];
			int numBytes = 0;
			String datastring = new String();
			while ((numBytes = in.read(obj_bytes)) > 0) {
				datastring += new String(obj_bytes);

			}

			String[] in_data = datastring.split("\t");

			List<String> l = new ArrayList<String>();
			for (String entries : in_data) {
				if (entries.contains("\n")) {
					String[] remaining = entries.split("\n");
					if (!remaining[1].isEmpty())
						l.add(remaining[1]);
				} else {
					l.add(entries);
				}

			}

			Model model_obj = new Model();
			List<NestedModel> input_list = new ArrayList<NestedModel>();

			for (String a : l) {
				if (!a.isEmpty() && !a.equals("") && !a.equals(null) && a.contains(",")) {
					String[] b = a.split(",");
					NestedModel nested = new NestedModel();
					String[] dl = new String[8];
					dl[0] = b[0];
					dl[1] = b[1];
					dl[2] = b[2];
					dl[3] = b[3];

					dl[4] = b[4];
					dl[5] = b[5];
					dl[6] = b[6];
					dl[7] = b[7];

					nested.setData(dl);
					input_list.add(nested);
				}
			}
			model_obj.setData(input_list);

			ByteArrayOutputStream outstream = new ByteArrayOutputStream();
			ObjectOutput obj_out = null;
			try {
				obj_out = new ObjectOutputStream(outstream);
				obj_out.writeObject(model_obj);
				obj_out.flush();
			} finally {
				try {
					outstream.close();
				} catch (IOException exp) {
					// ignore close exception
				}
			}

			ModelSet oms = new ModelSet();
			oms.setName("test");
			oms.setClassname(model_name);
			oms.setObject(outstream.toByteArray());
			oms.setDate(new Timestamp(System.currentTimeMillis()));

			try {
				obj_utx.begin();
				obj_em.persist(oms);
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
			output = "Data is retrived from HDFS file system and stored in database with model name as " + model_name;

			return output;
		}

	}

	/**
	 * 
	 * @brief PredictModel function predicts the income range.
	 */
	public String predictModel(String in_1, String in_2, String in_3, String in_4, String in_5, String in_6,
			String in_7, String Model_name) {
		

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

			if (newValue[0] < newValue[1]) {
				if (newValue[0] < newValue[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue[1] < newValue[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue2[0] < newValue2[1]) {
				if (newValue2[0] < newValue2[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue2[1] < newValue2[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue3[0] < newValue3[1]) {
				if (newValue3[0] < newValue3[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue3[1] < newValue3[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue4[0] < newValue4[1]) {
				if (newValue4[0] < newValue4[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue4[1] < newValue4[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue5[0] < newValue5[1]) {
				if (newValue5[0] < newValue5[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue5[1] < newValue5[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue6[0] < newValue6[1]) {
				if (newValue6[0] < newValue6[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue6[1] < newValue6[2]) {
				len[1]++;
			} else {
				len[2]++;
			}

			if (newValue7[0] < newValue7[1]) {
				if (newValue7[0] < newValue7[2]) {
					len[0]++;
				} else {
					len[2]++;
				}
			} else if (newValue7[1] < newValue7[2]) {
				len[1]++;
			} else {
				len[2]++;
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

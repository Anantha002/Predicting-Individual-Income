package com.ec.junit;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.persistence.EntityManager;
import javax.transaction.UserTransaction;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import com.ec.bussiness.logic.ManagedBeanModelDao;

/**
 * @class TrainModelTest
 * @brief The service is tested here. \n the training model feature is tested
 *        and validated using Junit
 * @file TrainModelTest.java
 */

public class TrainModelTest {

	private static ManagedBeanModelDao obj;
	String result;

	@BeforeClass
	public static void initialCall() {
		System.out.println("This is executed once before all Test");
		obj = new ManagedBeanModelDao();

	}

	@Before
	public void beforeEachTest() {
		System.out.println("This is executed before each Test");
	}

	@After
	public void afterEachTest() {
		System.out.println("This is exceuted after each Test");
	}

	/**
	 * 
	 * @brief Make sure the hadoop is started and delete the directory named
	 *        "result2" in the HDFS. \n After running the test program the
	 *        cluster will be created in location "result2"
	 */
	@Test
	public void predict_Test1() {
		System.out.println("Test case 1 to invoke hadoop for training dataset");
		result = obj.invokeHadoop();
		System.out.println(result);
		assertEquals("Model is being trained. Please wait for 5 minutues and check the output in Hadoop filesytem",
				result);

	}

	@Test
	public void predict_Test2() {
		System.out.println(" Dummy Test case 2 ");
	}

}


/** 
* @mainpage Doxygen example 
* 
* How to document program for Doxygen retrieval<br> 
* 
* Keywords are identified with an @ or backslash \ to signify attributes of the code or to 
* call special format options. The most common keywords are:<br> 
* \@brief - a short code description<br>
* \@param abc - describes the parameter abc of an function<br>
* \@return - describes the return value of an function<br>
* \@class abc - describes the class abc<br>
* \@file abc.java - describes the file abc.java<br>
*  
*  
*  @brief Project Information
* \n We are providing service for predicting individual's income range based on certain factors. Here we are applying Kmeans clustering on Hadoop MapReduce software framework to predict the income. 
*  The motive of the service is to predict the income range of an individual and to provide a market value of a person with respect to various factors such as education, skill set and so on. 
*  This is unique service which can be given to the people for knowing their actual market worth and expected salary range.
* 
* 
* @author ANANTHA KRISHNAN HARI 
*/
package com.ec.controllers;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.ec.bussiness.logic.User;
import com.ec.bussiness.logic.UserDao;

@Named
@RequestScoped
public class GreetController {

	final static Logger logger = Logger.getLogger(GreetController.class);

	@Inject
	private UserDao userDao;

	private String clientUserName;

	private String name;

	private int role;

	private String clientPassword;

	private String errorLog;

	/**
	 * 
	 * @brief validates the user name and password and redirects to other page
	 *        accorrding to role.
	 */
	public String greetLogin() {
		logger.info("login feature is invoked");
		User user = userDao.validateUsername(clientUserName);
		{

		}
		if (user != null) {
			if (user.getPassword().equals(clientPassword)) {
				logger.info("user name and password is valid");
				if (user.getRole() == 1) {
					return "developer_page?faces-redirect=true";
				} else if (user.getRole() == 2) {
					return "admin_page?faces-redirect=true";
				} else if (user.getRole() == 3) {
					return "general_page?faces-redirect=true";
				} else {
					logger.error("invalid role is assigned to " + clientUserName + " user");
					errorLog = "invalid role is assigned to user.Reassign the role for user!";
					return "greet";
				}
			} else {
				logger.error("Password is invalid for " + clientUserName + " user");
				errorLog = "Password is invalid.Try again!!";

				return "greet";
			}
		} else {
			logger.error("No such user exist in the given name-->" + clientUserName);
			errorLog = "No such user exist in the given name";
			return "greet";
		}
	}

	public String getClientUserName() {
		return clientUserName;
	}

	public void setClientUserName(String clientUserName) {
		this.clientUserName = clientUserName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRole() {
		return role;
	}

	public void setRole(int role) {
		this.role = role;
	}

	public String getClientPassword() {
		return clientPassword;
	}

	public void setClientPassword(String clientPassword) {
		this.clientPassword = clientPassword;
	}

	public String getErrorLog() {
		return errorLog;
	}

}

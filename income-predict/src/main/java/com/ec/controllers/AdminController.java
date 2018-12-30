package com.ec.controllers;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.log4j.Logger;

import com.ec.bussiness.logic.User;
import com.ec.bussiness.logic.UserDao;

@Named
@RequestScoped
public class AdminController {
	@Named
	@Produces
	@RequestScoped
	private User newUser = new User();

	@Inject
	private UserDao userDao;

	private String adminName;

	private int userRole;

	private String errorLog;

	final static Logger logger = Logger.getLogger(AdminController.class);

	/**
	 * 
	 * @brief The role for existing user is changed.
	 */
	public void changeRole() {
		logger.info("admin tries to change role for user->" + adminName);
		User user = userDao.validateAdmin(adminName);
		if (user != null) {
			user.setRole(getUserRole());
			userDao.roleChange(user);
			logger.info(" New Role is " + user.getRole());
			errorLog = " New Role is " + user.getRole();
		} else {
			logger.error("No such user exists in the given name");
			errorLog = "No such user exists in the given name.Try again!";
		}
	}

	public String getAdminName() {
		return adminName;
	}

	public void setAdminName(String adminName) {
		this.adminName = adminName;
	}

	public int getUserRole() {
		return userRole;
	}

	public void setUserRole(int userRole) {
		this.userRole = userRole;
	}

	public String getErrorLog() {
		return errorLog;
	}
}

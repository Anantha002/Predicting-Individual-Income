package com.ec.lab;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

@Path("/")
@RequestScoped
public class StatsRS {

	@GET
	@Path("/json")
	@Produces({ "application/json" })
	public String json() {
		return "{\"result\":\"Enter the input and based on those factors, the income range will be predicted. For Model Name ->Give the  name which is stored in Db \"}";
	}

	@GET
	@Path("/admin")
	@Produces({ "application/json" })
	public String admin() {
		return "{\"result\":\"Enter the existing username and thier new roles. The roles will be updated for the specified user in the database. \"}";
	}

	@GET
	@Path("/dev")
	@Produces({ "application/json" })
	public String dev() {
		return "{\"result\":\"Train Model-->This will invoke hadoop and create cluster which is used as trained models "
				+ "|| Save Model-->Enter the HDFS location of trained model and give name for model object which will be stored in Db.  \"}";
	}

}
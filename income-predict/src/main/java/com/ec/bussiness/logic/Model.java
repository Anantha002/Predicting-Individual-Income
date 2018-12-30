package com.ec.bussiness.logic;

import java.io.Serializable;
import java.util.List;

/**
 * @class Model
 * @brief Model for cluster datasets
 * @file Model.java
 */
public class Model implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<NestedModel> obj_NestedModel;

	public Model() {
		super();
	}

	public final List<NestedModel> getData() {
		return obj_NestedModel;
	}

	public final void setData(List<NestedModel> obj_NestedModel) {
		this.obj_NestedModel = obj_NestedModel;
	}
}

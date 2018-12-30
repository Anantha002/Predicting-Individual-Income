package com.ec.bussiness.logic;

import java.io.Serializable;

/**
 * @class NestedModel
 * @brief Model for cluster datasets
 * @file NestedModel.java
 */
public class NestedModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private String[] nestedModelData;

	public NestedModel() {
		super();
	}

	public final String[] getData() {
		return nestedModelData;
	}

	public final void setData(String[] nestedModelData) {
		this.nestedModelData = nestedModelData;
	}
}

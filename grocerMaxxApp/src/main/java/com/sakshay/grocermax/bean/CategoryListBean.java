package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class CategoryListBean extends BaseResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private List<com.sakshay.grocermax.bean.Category> Category = new ArrayList<com.sakshay.grocermax.bean.Category>();

	/**
	 * 
	 * @return The Category
	 */
	public List<com.sakshay.grocermax.bean.Category> getCategory() {
		return Category;
	}

	/**
	 * 
	 * @param Category
	 *            The Category
	 */
	public void setCategory(List<com.sakshay.grocermax.bean.Category> Category) {
		this.Category = Category;
	}

}
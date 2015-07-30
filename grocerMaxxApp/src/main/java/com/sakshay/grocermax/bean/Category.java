package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class Category implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Expose
	private String categoryId;
	@Expose
	private String name;

	/**
	* 
	* @return
	* The categoryId
	*/
	public String getCategoryId() {
	return categoryId;
	}

	/**
	* 
	* @param categoryId
	* The categoryId
	*/
	public void setCategoryId(String categoryId) {
	this.categoryId = categoryId;
	}

	/**
	* 
	* @return
	* The name
	*/
	public String getName() {
	return name;
	}

	/**
	* 
	* @param name
	* The name
	*/
	public void setName(String name) {
	this.name = name;
	}

	}
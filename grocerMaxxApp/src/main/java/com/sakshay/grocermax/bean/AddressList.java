package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;

public class AddressList implements Serializable{

	@Expose
	private ArrayList<com.sakshay.grocermax.bean.Address> Address = new ArrayList<com.sakshay.grocermax.bean.Address>();
	@Expose
	private Integer flag;

	/**
	 * 
	 * @return
	 * The Address
	 */
	public ArrayList<com.sakshay.grocermax.bean.Address> getAddress() {
		return Address;
	}

	/**
	 * 
	 * @param Address
	 * The Address
	 */
	public void setAddress(ArrayList<com.sakshay.grocermax.bean.Address> Address) {
		this.Address = Address;
	}

	/**
	 * 
	 * @return
	 * The flag
	 */
	public Integer getFlag() {
		return flag;
	}

	/**
	 * 
	 * @param flag
	 * The flag
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}
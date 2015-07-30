package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import com.google.gson.annotations.Expose;

public class CheckoutAddressBean implements Serializable {

	@Expose
	private ArrayList<Address> Address = new ArrayList<Address>();
	@Expose
	private Integer flag;

	HashMap<String,ArrayList<String>> date_timeSlot=new HashMap<String, ArrayList<String>>();

	/**
	 * 
	 * @return The Address
	 */
	public ArrayList<Address> getAddress() {
		return Address;
	}

	/**
	 * 
	 * @param Address
	 *            The Address
	 */
	public void setAddress(ArrayList<Address> Address) {
		this.Address = Address;
	}

	
	

	public HashMap<String, ArrayList<String>> getDate_timeSlot() {
		return date_timeSlot;
	}

	public void setDate_timeSlot(HashMap<String, ArrayList<String>> date_timeSlot) {
		this.date_timeSlot = date_timeSlot;
	}

	/**
	 * 
	 * @return The flag
	 */
	public Integer getFlag() {
		return flag;
	}

	/**
	 * 
	 * @param flag
	 *            The flag
	 */
	public void setFlag(Integer flag) {
		this.flag = flag;
	}

}

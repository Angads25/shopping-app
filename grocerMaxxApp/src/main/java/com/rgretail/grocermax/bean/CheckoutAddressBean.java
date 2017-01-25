package com.rgretail.grocermax.bean;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class CheckoutAddressBean implements Serializable {

	@Expose
	private ArrayList<Address> Address = new ArrayList<Address>();
	@Expose
	private Integer flag;
//	@Expose
//	private boolean Available;

	HashMap<String,ArrayList<String>> date_timeAvailableSlot=new HashMap<String,ArrayList<String>>();

	public HashMap<String, ArrayList<String>> getDate_timeAvailableSlot() {
		return date_timeAvailableSlot;
	}
	public void setDate_timeAvailableSlot(HashMap<String, ArrayList<String>> date_timeSlot) {
		this.date_timeAvailableSlot = date_timeSlot;
	}


//	HashMap<String,Integer> date_timeAvailableSlot
//	public void setAvailable(boolean available){
//		this.Available = available;
//	}
//
//	public boolean getAvailable(){
//		return Available;
//	}


	HashMap<String,ArrayList<String>> date_timeSlot=new HashMap<String, ArrayList<String>>();

	HashMap<String,ArrayList<String>> date_timeSlot_new=new HashMap<String, ArrayList<String>>();

	public HashMap<String, ArrayList<String>> getDate_timeSlot_new() {
		return date_timeSlot_new;
	}

	public void setDate_timeSlot_new(HashMap<String, ArrayList<String>> date_timeSlot_new) {
		this.date_timeSlot_new = date_timeSlot_new;
	}

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

package com.rgretail.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserDetailBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("Personal Info")
	@Expose
	private com.rgretail.grocermax.bean.PersonalInfo PersonalInfo;
	@Expose
	private Address ShippingAddress;
	@Expose
	private Address BillingAddress;

	/**
	 * 
	 * @return The PersonalInfo
	 */
	public com.rgretail.grocermax.bean.PersonalInfo getPersonalInfo() {
		return PersonalInfo;
	}

	/**
	 * 
	 * @param PersonalInfo
	 *            The Personal Info
	 */
	public void setPersonalInfo(com.rgretail.grocermax.bean.PersonalInfo PersonalInfo) {
		this.PersonalInfo = PersonalInfo;
	}

	/**
	 * 
	 * @return The ShippingAddress
	 */
	public Address getShippingAddress() {
		return ShippingAddress;
	}

	/**
	 * 
	 * @param ShippingAddress
	 *            The ShippingAddress
	 */
	public void setShippingAddress(Address ShippingAddress) {
		this.ShippingAddress = ShippingAddress;
	}

	/**
	 * 
	 * @return The BillingAddress
	 */
	public Address getBillingAddress() {
		return BillingAddress;
	}

	/**
	 * 
	 * @param BillingAddress
	 *            The BillingAddress
	 */
	public void setBillingAddress(Address BillingAddress) {
		this.BillingAddress = BillingAddress;
	}

}
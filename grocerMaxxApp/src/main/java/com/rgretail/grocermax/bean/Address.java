package com.rgretail.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Address implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String firstname;
	@Expose
	private String lastname;
	@Expose
	private String city;
	@SerializedName("country_id")
	@Expose
	private String country_id;
	@Expose
	private String postcode;
	@Expose
	private String telephone;
	@Expose
	private String StreetAddress;
	@SerializedName("country_name")
	@Expose
	private String countryName;
	@Expose
	private String id;
//	@Expose
//	private String State;
	@Expose
	private String customer_address_id;
	@Expose
	private String region;
	@Expose
	private String region_id;
	@Expose
	private String street;
	@Expose
	private String is_default_billing;
	@Expose
	private String is_default_shipping;

	public String getDefaultBilling() {return is_default_billing;  }

	public void setDefaultBilling(String is_default_billing) {	this.is_default_billing = is_default_billing; }

	public String getDefaultShipping() {return is_default_shipping;    }

	public void setDefaultShipping(String is_default_shipping) {	this.is_default_shipping = is_default_shipping; }

	public String getStreetAddress() {
		return StreetAddress;
	}

	public void setStreetAddress(String streetAddress) {
		StreetAddress = streetAddress;
	}

	/**
	 * 
	 * @return The firstname
	 */

	public String getFirstname() {
		return firstname;
	}

	/**
	 * 
	 * @param firstname
	 *            The firstname
	 */
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	
	/**
	 * 
	 * @return The lastname
	 */
	public String getLastname() {
		return lastname;
	}

	/**
	 * 
	 * @param firstname
	 *            The firstname
	 */
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	/**
	 * 
	 * @return The city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * 
	 * @param city
	 *            The city
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * 
	 * @return The countryId
	 */
	public String getCountryId() {
		return country_id;
	}

	/**
	 * 
	 * @param countryId
	 *            The country_id
	 */
	public void setCountryId(String countryId) {
		this.country_id = countryId;
	}

	/**
	 * 
	 * @return The postcode
	 */
	public String getPostcode() {
		return postcode;
	}

	/**
	 * 
	 * @param postcode
	 *            The postcode
	 */
	public void setPostcode(String postcode) {
		this.postcode = postcode;
	}

	/**
	 * 
	 * @return The telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * 
	 * @param telephone
	 *            The telephone
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * 
	 * @return The street
	 */
	public String getStreet() {
		return street;
	}

	/**
	 * 
	 * @param street
	 *            The street
	 */
	public void setStreet(String street) {
		this.street = street;
	}

	/**
	 * 
	 * @return The countryName
	 */
	public String getCountryName() {
		return countryName;
	}

	/**
	 * 
	 * @param countryName
	 *            The country_name
	 */
	public void setCountryName(String countryName) {
		this.countryName = countryName;
	}

	/**
	 * 
	 * @return The id
	 */
	public String getid() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            The address_entity_id
	 */
	public void setid(String id) {
		this.id = id;
	}

	/**
	 * 
	 * @return The state
	 */
//	public String getState() {
//		return State;
//	}

	/**
	 * 
	 * @param state
	 *            The state
	 */
//	public void setState(String state) {
//		this.State = state;
//	}

	public String getCustomer_address_id() {
		return customer_address_id;
	}

	public void setCustomer_address_id(String customer_address_id) {
		this.customer_address_id = customer_address_id;
	}

	public String getRegionId(){
		return region_id;
	}

	public void setRegionId(String regionid){
		this.region_id = regionid;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

}
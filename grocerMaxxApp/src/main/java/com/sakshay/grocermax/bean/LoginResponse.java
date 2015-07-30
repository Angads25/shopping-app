package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class LoginResponse extends BaseResponseBean implements Serializable {

	@Expose
	private String UserID;
	@Expose
	private String Firstname;
	@Expose
	private String Lastname;
	
	/**
	 * 
	 * @return The UserID
	 */
	public String getUserID() {
		return UserID;
	}

	/**
	 * 
	 * @param UserID
	 *            The UserID
	 */
	public void setUserID(String UserID) {
		this.UserID = UserID;
	}

	
	public String getFirstName(){
		return Firstname;
	}
	
	public void setFirstName(String Firstname){
		this.Firstname = Firstname;
	}
	
	public String getLastName(){
		return Lastname;
	}
	
	public void setLastName(String Lastname){
		this.Lastname = Lastname;
	}

}

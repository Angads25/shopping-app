package com.sakshay.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class BaseResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String flag;
	@Expose
	private String Result;
	@Expose
	private String QuoteId;
	@Expose
	private int TotalItem;
	/**
	 *
	 * @return The flag
	 */
	@Expose
	private String Mobile;
	
	public String getMobile(){
		return Mobile;
	}
	
	public void setMobile(String Mobile){	this.Mobile = Mobile;}

	public String getFlag() {return flag;}

	public int getTotalItem() {
		return TotalItem;
	}

	public void setTotalItem(int totalItem)
	{

		TotalItem = totalItem;
	}

	public String getQuoteId() {return QuoteId;}

	public void setQuoteId(String quoteId) {QuoteId = quoteId;}

	/**
	 * 
	 * @param flag
	 *            The flag
	 */
	public void setFlag(String flag) {
		this.flag = flag;
	}

	/**
	 * 
	 * @return The Result
	 */
	public String getResult() {
		return Result;
	}

	/**
	 * 
	 * @param Result
	 *            The Result
	 */
	public void setResult(String Result) {
		this.Result = Result;
	}

}
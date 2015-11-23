package com.rgretail.grocermax.bean;

import java.io.Serializable;

import com.google.gson.annotations.Expose;

public class PaymentOption implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String GatewayId;
	@Expose
	private String GatewayTitle;
	@Expose
	private String PaymentType;
	@Expose
	private String Paymentoption;
	@Expose
	private String SupplierId;

	/**
	 * 
	 * @return The GatewayId
	 */
	public String getGatewayId() {
		return GatewayId;
	}

	/**
	 * 
	 * @param GatewayId
	 *            The GatewayId
	 */
	public void setGatewayId(String GatewayId) {
		this.GatewayId = GatewayId;
	}

	/**
	 * 
	 * @return The GatewayTitle
	 */
	public String getGatewayTitle() {
		return GatewayTitle;
	}

	/**
	 * 
	 * @param GatewayTitle
	 *            The GatewayTitle
	 */
	public void setGatewayTitle(String GatewayTitle) {
		this.GatewayTitle = GatewayTitle;
	}

	/**
	 * 
	 * @return The PaymentType
	 */
	public String getPaymentType() {
		return PaymentType;
	}

	/**
	 * 
	 * @param PaymentType
	 *            The PaymentType
	 */
	public void setPaymentType(String PaymentType) {
		this.PaymentType = PaymentType;
	}

	/**
	 * 
	 * @return The Paymentoption
	 */
	public String getPaymentoption() {
		return Paymentoption;
	}

	/**
	 * 
	 * @param Paymentoption
	 *            The Paymentoption
	 */
	public void setPaymentoption(String Paymentoption) {
		this.Paymentoption = Paymentoption;
	}

	/**
	 * 
	 * @return The SupplierId
	 */
	public String getSupplierId() {
		return SupplierId;
	}

	/**
	 * 
	 * @param SupplierId
	 *            The SupplierId
	 */
	public void setSupplierId(String SupplierId) {
		this.SupplierId = SupplierId;
	}

}
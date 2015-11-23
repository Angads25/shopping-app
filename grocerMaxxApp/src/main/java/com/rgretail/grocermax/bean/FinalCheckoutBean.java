package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class FinalCheckoutBean extends BaseResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private List<CartDetail> Product = new ArrayList<CartDetail>();
	@Expose
	private String SubTotal;
	@Expose
	private String GrandTotal;
	/*@Expose
	private int TotalItem;*/
	@Expose
	private String TransactionID;
	@Expose
	private String SuccessURL;
	@Expose
	private String OrderID;
	@Expose
	private String OrderDBID;
	
	@Expose
	private List<String> PaymentMode = new ArrayList<String>();

	/**
	 * 
	 * @return The Product
	 */
	
	
	
	public List<CartDetail> getProduct() {
		return Product;
	}

	public String getOrderDBID() {
		return OrderDBID;
	}

	public void setOrderDBID(String orderDBID) {
		OrderDBID = orderDBID;
	}

	/**
	 * 
	 * @param Product
	 *            The Product
	 */
	public void setProduct(List<CartDetail> Product) {
		this.Product = Product;
	}

	/**
	 * 
	 * @return The SubTotal
	 */
	public String getSubTotal() {
		return SubTotal;
	}

	/**
	 * 
	 * @param SubTotal
	 *            The SubTotal
	 */
	public void setSubTotal(String SubTotal) {
		this.SubTotal = SubTotal;
	}

	/**
	 * 
	 * @return The GrandTotal
	 */
	public String getGrandTotal() {
		return GrandTotal;
	}

	/**
	 * 
	 * @param GrandTotal
	 *            The GrandTotal
	 */
	public void setGrandTotal(String GrandTotal) {
		this.GrandTotal = GrandTotal;
	}

	/**
	 * 
	 * @return The TotalItem
	 */
	/*public int getTotalItem() {
		return TotalItem;
	}

	*//**
	 * 
	 * @param TotalItem
	 *            The TotalItem
	 *//*
	public void setTotalItem(int TotalItem) {
		this.TotalItem = TotalItem;
	}*/

	/**
	 * 
	 * @return The TransactionID
	 */
	public String getTransactionID() {
		return TransactionID;
	}

	/**
	 * 
	 * @param TransactionID
	 *            The TransactionID
	 */
	public void setTransactionID(String TransactionID) {
		this.TransactionID = TransactionID;
	}

	/**
	 * 
	 * @return The OrderId
	 */
	public String getOrderId() {
		return OrderID;
	}

	/**
	 * 
	 * @param OrderID
	 *            The OrderID
	 */
	public void setOrderId(String OrderID) {
		this.OrderID = OrderID;
	}

	/**
	 * 
	 * @return The SuccessURL
	 */
	public String getSuccessURL() {
		return SuccessURL;
	}

	/**
	 * 
	 * @param SuccessURL
	 *            The SuccessURL
	 */
	public void setSuccessURL(String SuccessURL) {
		this.SuccessURL = SuccessURL;
	}

	/**
	 * 
	 * @return The PaymentMode
	 */
	public List<String> getPaymentMode() {
		return PaymentMode;
	}

	/**
	 * 
	 * @param PaymentMode
	 *            The PaymentMode
	 */
	public void setPaymentMode(List<String> PaymentMode) {
		this.PaymentMode = PaymentMode;
	}

}
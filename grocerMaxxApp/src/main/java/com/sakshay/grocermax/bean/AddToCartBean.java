package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class AddToCartBean extends BaseResponseBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private List<com.sakshay.grocermax.bean.CartDetail> CartDetail = new ArrayList<com.sakshay.grocermax.bean.CartDetail>();
	@Expose
	private String SubTotal;
	@Expose
	private String GrandTotal;
	@Expose
	private int TotalItem;

	/**
	 * 
	 * @return The CartDetail
	 */
	public List<com.sakshay.grocermax.bean.CartDetail> getCartDetail() {
		return CartDetail;
	}

	/**
	 * 
	 * @param CartDetail
	 *            The CartDetail
	 */
	public void setCartDetail(List<com.sakshay.grocermax.bean.CartDetail> CartDetail) {
		this.CartDetail = CartDetail;
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
	public int getTotalItem() {
		return TotalItem;
	}

	/**
	 * 
	 * @param TotalItem
	 *            The TotalItem
	 */
	public void setTotalItem(int TotalItem) {
		this.TotalItem = TotalItem;
	}

}
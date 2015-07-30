package com.sakshay.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductDetailsListBean extends BaseResponseBean implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@SerializedName("Product_Detail")
	@Expose
	private List<com.sakshay.grocermax.bean.ProductDetail> ProductDetail = new ArrayList<com.sakshay.grocermax.bean.ProductDetail>();

	/**
	 * 
	 * @return The ProductDetail
	 */
	public List<com.sakshay.grocermax.bean.ProductDetail> getProductDetail() {
		return ProductDetail;
	}

	/**
	 * 
	 * @param ProductDetail
	 *            The Product_Detail
	 */
	public void setProductDetail(List<com.sakshay.grocermax.bean.ProductDetail> ProductDetail) {
		this.ProductDetail = ProductDetail;
	}
}
package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;

public class ProductListBean extends BaseResponseBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private List<com.rgretail.grocermax.bean.Product> Product = new ArrayList<com.rgretail.grocermax.bean.Product>();

	/**
	 * 
	 * @return The Product
	 */
	public List<com.rgretail.grocermax.bean.Product> getProduct() {
		return Product;
	}

	/**
	 * 
	 * @param Product
	 *            The Product
	 */
	public void setProduct(List<com.rgretail.grocermax.bean.Product> Product) {
		this.Product = Product;
	}

}
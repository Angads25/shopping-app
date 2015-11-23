package com.rgretail.grocermax.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

import com.google.gson.annotations.Expose;

public class Orderhistory implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Expose
	private String increment_id;
	
	@Expose
	private String order_id;
	
	@Expose
	private String created_at;
	
	@Expose
	private String grand_total;
	
	@Expose
	private String status;
	
	@Expose 
	private String total_item_count;
	
	static SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	
	
	
	
	

	public String getOrder_id() {
		return order_id;
	}

	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}

	public String getIncrement_id() {
		return increment_id;
	}

	public void setIncrement_id(String increment_id) {
		this.increment_id = increment_id;
	}

	public String getCreated_at() {
		return created_at;
	}

	public void setCreated_at(String created_at) {
		this.created_at = created_at;
	}

	public String getGrand_total() {
		return grand_total;
	}

	public void setGrand_total(String grand_total) {
		this.grand_total = grand_total;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTotal_item_count() {
		return total_item_count;
	}

	public void setTotal_item_count(String total_item_count) {
		this.total_item_count = total_item_count;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	public static Comparator<Orderhistory> getDistanceinkm() {
		return dateTime;
	}
	public static void setDistanceinkm(Comparator<Orderhistory> dateTime) {
		Orderhistory.dateTime = dateTime;
	}
	
	public static Comparator<Orderhistory> dateTime = new Comparator<Orderhistory>() {

		@SuppressWarnings("deprecation")
		public int compare(Orderhistory productData1, Orderhistory productData2) {

			Date date1=null;;
			Date date2=null;;
			try {
				date1 = formatter.parse(productData1.getCreated_at());
				date2 = formatter.parse(productData2.getCreated_at());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 return date2.compareTo(date1);
		}};

	

}
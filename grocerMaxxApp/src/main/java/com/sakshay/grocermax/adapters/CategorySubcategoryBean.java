package com.sakshay.grocermax.adapters;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.util.ArrayList;

public class CategorySubcategoryBean implements Serializable {

	String Category, CategoryId, breadcrumb, isActive;

	ArrayList<CategorySubcategoryBean> category = new ArrayList<CategorySubcategoryBean>();

	public void addCategory(CategorySubcategoryBean categoryItem) {
		category.add(categoryItem);
	}

	public ArrayList<CategorySubcategoryBean> getChildren() {
		return category;
	}

	public String getCategoryId() {
		return CategoryId;
	}

	public void setCategoryId(String categoryId) {
		CategoryId = categoryId;
	}

	public String getCategory() {
		return Category;
	}

	public void setCategory(String category) {
		Category = category;
	}
	
	public String getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(String breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}

}

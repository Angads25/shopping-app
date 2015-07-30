package com.sakshay.grocermax.adapters;

import java.io.Serializable;
import java.util.ArrayList;

public class CategorySubcategoryBean implements Serializable {

	String Category, CategoryId, breadcrumb;

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

}

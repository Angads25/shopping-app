package com.rgretail.grocermax.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Category;

import com.rgretail.grocermax.exception.GrocermaxBaseException;

public class CategoryListAdapter extends BaseAdapter{
	
	List<Category> categoryList = null;
	Context mContext;
	private LayoutInflater inflater = null;
	
	public CategoryListAdapter(Context mContext, List<Category> list) {
		this.mContext = mContext;
		this.categoryList = list;
		this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (categoryList != null){
			return categoryList.size();
		} else {
			return 0;
		}
	}

	@Override
	public Category getItem(int position) {
		return categoryList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		try{
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.browse_activity_row, parent, false);
				holder = new ViewHolder();
				holder.cat_name = (TextView) convertView.findViewById(R.id.item_name);
				holder.cat_image = (ImageView) convertView.findViewById(R.id.image);

				holder.cat_image.setVisibility(View.GONE);
				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}

			Category obj = getItem(position);
			holder.cat_name.setText(obj.getName());
		}catch(Exception e){
			new GrocermaxBaseException("CategoryListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return convertView;
	}
	
	private class ViewHolder {

		TextView cat_name;
		ImageView cat_image;
	}
}


package com.sakshay.grocermax.adapters;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.exception.GrocermaxBaseException;

public class HomeListAdapter extends BaseAdapter{
	
	List<CategorySubcategoryBean> categoryList = null;
	Context mContext;
	private LayoutInflater inflater = null;
	
	public HomeListAdapter(Context mContext, List<CategorySubcategoryBean> list) {
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
	public CategorySubcategoryBean getItem(int position) {
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
		
		CategorySubcategoryBean obj = getItem(position);

		holder.cat_name.setText(obj.getCategory());
		}catch(Exception e){
			new GrocermaxBaseException("HomeListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return convertView;
	}
	
	private class ViewHolder {
		TextView cat_name;
		ImageView cat_image;
	}
	
	public void refreshList(List<CategorySubcategoryBean> categoryList){
		try{
			this.categoryList = categoryList;
			notifyDataSetChanged();
		}catch(Exception e){
			new GrocermaxBaseException("HomeListAdapter","refreshList",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
}


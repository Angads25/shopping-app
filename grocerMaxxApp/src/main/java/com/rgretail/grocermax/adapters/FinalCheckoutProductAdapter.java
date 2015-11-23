package com.rgretail.grocermax.adapters;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.exception.GrocermaxBaseException;

public class FinalCheckoutProductAdapter extends BaseAdapter{
	
	List<CartDetail> products = null;
	Activity activity;
	private LayoutInflater inflater = null;
	
	public FinalCheckoutProductAdapter(Activity activity, List<CartDetail> list) {
		((BaseActivity)activity).initImageLoaderM();
		this.activity = activity;
		this.products = list;
		this.inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (products != null){
			return products.size();
		} else {
			return 0;
		}
	}

	@Override
	public CartDetail getItem(int position) {
		return products.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		try{
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.final_checkout_product_row, parent, false);
				holder = new ViewHolder();
				holder.prod_name = (TextView) convertView.findViewById(R.id.product_name);
				holder.amount = (TextView) convertView.findViewById(R.id.amount);
				holder.quantity = (TextView) convertView.findViewById(R.id.quantity);
				holder.prod_image = (ImageView) convertView.findViewById(R.id.product_image);

				convertView.setTag(holder);
			}else {
				holder = (ViewHolder) convertView.getTag();
			}

			final CartDetail obj = getItem(position);
			holder.prod_name.setText(obj.getName());

			holder.amount.setText("Rs. "+String.format("%.2f",Float.parseFloat(obj.getPrice().toString())));
			holder.quantity.setText("Quantity: "+obj.getQty());

			ImageLoader.getInstance().displayImage(obj.getProduct_thumbnail(),holder.prod_image, ((BaseActivity)activity).baseImageoptions);
		}catch(Exception e){
			new GrocermaxBaseException("AddressListAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return convertView;
	}
	
	private class ViewHolder {

		TextView prod_name, quantity, amount;
		ImageView prod_image;
	}
}


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

import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.OrderHistory;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Orderhistory;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.exception.GrocermaxBaseException;

public class OrderHistoryAdapter extends BaseAdapter {

	List<Orderhistory> orders = null;
	Activity activity;
	private LayoutInflater inflater = null;

	public OrderHistoryAdapter(Activity activity, List<Orderhistory> list) {
//		 ((BaseActivity)activity).initImageLoaderM();
		this.activity = activity;
		this.orders = list;
		this.inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount() {
		if (orders != null) {
			return orders.size();
		} else {
			return 0;
		}
	}

	@Override
	public Orderhistory getItem(int position) {
		return orders.get(position);
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
			convertView = inflater.inflate(R.layout.order_history_adapter,
					parent, false);
			holder = new ViewHolder();
			holder.order_date = (TextView) convertView
					.findViewById(R.id.order_date);
			holder.order_no = (TextView) convertView
					.findViewById(R.id.order_no);
			holder.amnt_paid = (TextView) convertView
					.findViewById(R.id.amnt_paid);
			holder.status = (TextView) convertView.findViewById(R.id.status);
//			holder.name = (TextView) convertView.findViewById(R.id.name);
			holder.no_of_items = (TextView) convertView
					.findViewById(R.id.no_of_items);

			holder.arrowImage = (ImageView) convertView
					.findViewById(R.id.arrowImage);

			holder.product_image = (ImageView) convertView
					.findViewById(R.id.product_image);
			
			holder.order_date.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
			holder.order_no.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
			holder.amnt_paid.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
			holder.status.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
//			holder.name.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
			holder.no_of_items.setTypeface(CustomFonts.getInstance().getRobotoMedium(HomeScreen.mContext));
			
			holder.tv1 = (TextView) convertView.findViewById(R.id.tv1);
			holder.tv2 = (TextView) convertView.findViewById(R.id.tv2);
			holder.tv3 = (TextView) convertView.findViewById(R.id.tv3);
			holder.tv5 = (TextView) convertView.findViewById(R.id.tv5);
			holder.tv6 = (TextView) convertView.findViewById(R.id.tv6);
			
			holder.tv1.setTypeface(CustomFonts.getInstance().getRobotoBold(HomeScreen.mContext));
			holder.tv2.setTypeface(CustomFonts.getInstance().getRobotoBold(HomeScreen.mContext));
			holder.tv3.setTypeface(CustomFonts.getInstance().getRobotoBold(HomeScreen.mContext));
			holder.tv5.setTypeface(CustomFonts.getInstance().getRobotoBold(HomeScreen.mContext));
			holder.tv6.setTypeface(CustomFonts.getInstance().getRobotoBold(HomeScreen.mContext));

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		final Orderhistory obj = getItem(position);
		/*Order order_obj = obj.getOrder();

		holder.order_date.setText(order_obj.getOrderDate());
		holder.order_no.setText(order_obj.getOrderId());
		holder.amnt_paid.setText(order_obj.getTotal());
		holder.status.setText(order_obj.getStatus());
		if (order_obj.getItems().size() > 1) {
			String header = (order_obj.getItems().get(0).getProductName())
					+ " + " + (order_obj.getItems().size() - 1) + " more items";
			holder.name.setText(header);
		} else
			holder.name.setText(order_obj.getItems().get(0).getProductName()
					.toString());

		holder.no_of_items.setText("" + order_obj.getItems().size());*/

		/*ImageLoader.getInstance().displayImage(
				order_obj.getItems().get(0).getProductThumbnail(),
				holder.product_image,
				((BaseActivity) activity).baseImageoptions);*/
		
		
		//-----------------New CODE --------Response changed-------------------//
		
		holder.order_date.setText(obj.getCreated_at().split(" ")[0]);
		holder.order_no.setText(obj.getIncrement_id());
		holder.amnt_paid.setText("Rs. "+String.format("%.2f",Float.parseFloat(obj.getGrand_total())));
		holder.status.setText(obj.getStatus().substring(0, 1).toUpperCase() + obj.getStatus().substring(1));
		holder.no_of_items.setText("" + obj.getTotal_item_count());

		/// code added by Ishan///////////////////
		holder.tv_reorder=(TextView)convertView.findViewById(R.id.tv_reorder);
		holder.tv_noLocation=(TextView)convertView.findViewById(R.id.tv_noLocation);
		if(MySharedPrefs.INSTANCE.getSelectedStoreId().equals(obj.getStore_id()))
		{
			holder.tv_reorder.setVisibility(View.VISIBLE);
			holder.tv_noLocation.setVisibility(View.GONE);
			holder.tv_reorder.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					((OrderHistory)activity).reOrderItems(obj.getIncrement_id().trim());
				}
			});
		}else{
			holder.tv_reorder.setVisibility(View.GONE);
			holder.tv_noLocation.setVisibility(View.VISIBLE);
		}
		}catch(Exception e){
			new GrocermaxBaseException("OrderHistorytAdapter","getView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return convertView;
	}

	private class ViewHolder {

		TextView order_date, order_no, amnt_paid, status, no_of_items,tv_reorder,tv_noLocation;
//		TextView name;
		ImageView arrowImage, product_image;
		TextView tv1,tv2,tv3,tv5,tv6;
		
		
		
	}
}

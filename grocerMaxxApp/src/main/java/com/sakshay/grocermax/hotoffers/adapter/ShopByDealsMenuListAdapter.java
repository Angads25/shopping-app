package com.sakshay.grocermax.hotoffers.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.bean.ShopByDealModel;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;

import java.util.List;


public class ShopByDealsMenuListAdapter extends BaseAdapter {

	private Context mContext;
	private List<ShopByDealModel> offerList;
	private LayoutInflater mInflator;
	private Fragment frag;
	public ShopByDealsMenuListAdapter(Context pContext, Fragment fragment) {
		mContext = pContext;
		mInflator = (LayoutInflater) pContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		frag = fragment;
	}

	public void setListData(List<ShopByDealModel> listData) {
		offerList = listData;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getCount()
	 */
	@Override
	public int getCount() {
		return offerList == null ? 0 : offerList.size();
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public Object getItem(int position) {
		return offerList.get(position);
	}

	/* (non-Javadoc)
	 * @see android.widget.Adapter#getItemId(int)
	 */
	@Override
	public long getItemId(int position) {
		return position;
	}
	/* (non-Javadoc)
	 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(final int position, View view, ViewGroup parent) {

		ViewHolder holder;
		if (view == null) {
			// view = mInflator.inflate(R.layout.list_membership_item, null);
			view = mInflator.inflate(R.layout.list_item, null);
			holder = new ViewHolder();
			holder.txvMenuItem = (TextView) view
					.findViewById(R.id.textMenuItem);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
			
			
		}
		holder.txvMenuItem.setText(offerList.get(position).getDealType());
		holder.txvMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				((HotOffersActivity)mContext).hitForShopByDeals(offerList.get(position).getId());
				((HotOffersActivity) mContext).getDrawerLayout().closeDrawers();
//				ShopByDealDetailListAdapter.strDealListDeatilHeading = offerList.get(position).getDealType();
			}
		});
		
		return view;
	}
	class ViewHolder {
		// TextView txvProductName;
		TextView txvMenuItem;
	}
}
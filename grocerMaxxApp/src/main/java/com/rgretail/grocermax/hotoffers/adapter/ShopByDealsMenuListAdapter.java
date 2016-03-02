package com.rgretail.grocermax.hotoffers.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.rgretail.grocermax.MyApplication;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.ShopByDealModel;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

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

//		System.out.println("========deal type============="+offerList.get(position).getDealType());

		holder.txvMenuItem.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

                /*tracking GA event on Shop By Deal Item click from drawer*/
                try{
                    MyApplication.isFromDrawer=true;
                    UtilityMethods.clickCapture(mContext, "Drawer - Deal Category L1", "", offerList.get(position).getDealType(), "", MySharedPrefs.INSTANCE.getSelectedCity());
					RocqAnalytics.trackEvent("Drawer - Deal Category L1", new ActionProperties("Category", "Drawer - Deal Category L1", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",offerList.get(position).getDealType()));
                }catch(Exception e){
                    e.printStackTrace();
                }
                /*--------------------------------------------------------*/

				AppConstants.strTitleHotDeal = "";
				AppConstants.strTitleHotDeal = offerList.get(position).getDealType();
				((HomeScreen)mContext).hitForShopByDeals(offerList.get(position).getId());
				((HomeScreen) mContext).getDrawerLayout().closeDrawers();
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
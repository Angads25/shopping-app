package com.rgretail.grocermax.hotoffers.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;

import java.util.List;


public class MenuListAdapter extends BaseAdapter {

	private Context mContext;
	private List<CategorySubcategoryBean> offerList;
	private LayoutInflater mInflator;
	private Fragment frag;
	public MenuListAdapter(Context pContext,Fragment fragment) {
		mContext = pContext;
		mInflator = (LayoutInflater) pContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		frag = fragment;
	}

	public void setListData(List<CategorySubcategoryBean> listData) {
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
			holder.llMenuList = (LinearLayout) view
					.findViewById(R.id.ll_menulist);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		holder.txvMenuItem.setText(offerList.get(position).getCategory());
//		holder.txvMenuItem.setOnClickListener(new OnClickListener() {
		holder.llMenuList.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(offerList.get(position).getChildren().size()>0) {
//					setListData(offerList.get(position).getChildren());
//					((MenuFragment)frag).setData(offerList.get(position).getChildren());
					((HomeScreen)mContext).setMenu(offerList.get(position).getChildren(),offerList.get(position).getCategory());
				}else{

//					Toast.makeText(mContext,"Last Level",Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		return view;
	}
	class ViewHolder {
		// TextView txvProductName;
		TextView txvMenuItem;
		LinearLayout llMenuList;

	}
}
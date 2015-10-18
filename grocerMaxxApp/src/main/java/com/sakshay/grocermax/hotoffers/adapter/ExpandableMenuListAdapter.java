package com.sakshay.grocermax.hotoffers.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakshay.grocermax.R;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.hotoffers.fragment.ExpandableMenuFragment;
import com.sakshay.grocermax.hotoffers.fragment.MenuFragment;

public class ExpandableMenuListAdapter extends BaseExpandableListAdapter {

	private Context _context;
	private List<String> _listDataHeader; // header titles
	// child data in format of header title, child title
	private HashMap<String, ArrayList<CategorySubcategoryBean>> _listDataChild;
	private Fragment fragment;

	public ExpandableMenuListAdapter(Context context,Fragment frag, List<String> listDataHeader,
								 HashMap<String, ArrayList<CategorySubcategoryBean>> listChildData) {
		this._context = context;
		this._listDataHeader = listDataHeader;
		this._listDataChild = listChildData;
		fragment = frag;
	}

	@Override
	public Object getChild(int groupPosition, int childPosititon) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.get(childPosititon).getCategory();
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	@Override
	public View getChildView(int groupPosition, final int childPosition,
							 boolean isLastChild, View convertView, ViewGroup parent) {

		final String childText = (String) getChild(groupPosition, childPosition);

		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.browse_activity_row_child, null);
		}

		TextView txtListChild = (TextView) convertView
				.findViewById(R.id.item_name_childs);

		txtListChild.setText(childText);
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		return this._listDataChild.get(this._listDataHeader.get(groupPosition))
				.size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return this._listDataHeader.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return this._listDataHeader.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
							 View convertView, ViewGroup parent) {
		String headerTitle = (String) getGroup(groupPosition);
		if (convertView == null) {
			LayoutInflater infalInflater = (LayoutInflater) this._context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infalInflater.inflate(R.layout.browse_activity_row_parent, null);
		}

		TextView lblListHeader = (TextView) convertView
				.findViewById(R.id.item_name_parent);
		ImageView imgArrow = (ImageView)convertView.findViewById(R.id.img_arrow_parent);
		ImageView img_arrow_parent_minus = (ImageView)convertView.findViewById(R.id.img_arrow_parent_minus);
		if(!((ExpandableMenuFragment)fragment).isExpandable(groupPosition)){

			imgArrow.setVisibility(View.GONE);
			img_arrow_parent_minus.setVisibility(View.GONE);
		}else{
			imgArrow.setVisibility(View.VISIBLE);
			if(((ExpandableMenuFragment)fragment).isExpanded(groupPosition)){
				img_arrow_parent_minus.setVisibility(View.VISIBLE);
				imgArrow.setVisibility(View.GONE);
			}else{
				imgArrow.setVisibility(View.VISIBLE);
				img_arrow_parent_minus.setVisibility(View.GONE);
			}
		}
		lblListHeader.setText(headerTitle);

		return convertView;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
}
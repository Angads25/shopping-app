package com.sakshay.grocermax.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sakshay.grocermax.AnimatedExpandableListView.AnimatedExpandableListAdapter;
import com.sakshay.grocermax.R;
import com.sakshay.grocermax.utils.CustomFonts;
//import com.payu.sdk.GetResponseTask;

public class ExpandableListAdapter extends AnimatedExpandableListAdapter{
	
	ArrayList<CategorySubcategoryBean> catObj;
	Context con;
	public ExpandableListAdapter(Context con,ArrayList<CategorySubcategoryBean> catObj) {
		this.con=con;
		this.catObj=catObj;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return catObj.get(groupPosition).getChildren().get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	/*@Override
	public View getChildView(int groupPosition, int childPosition,boolean isLastChild, View convertView, ViewGroup parent) {
		
		View v=convertView;
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.browse_activity_row, null);
		}
		TextView cat_name = (TextView) v.findViewById(R.id.item_name);
		ImageView cat_image = (ImageView) v.findViewById(R.id.image);
		cat_image.setVisibility(View.GONE);
		cat_name.setText(catObj.get(groupPosition).getChildren().get(childPosition).getCategory());
		
		v.setBackgroundColor(Color.BLUE);
		
		return v;
	}*/

	/*@Override
	public int getChildrenCount(int groupPosition) {
		return catObj.get(groupPosition).getChildren().size();
	}*/

	@Override
	public Object getGroup(int groupPosition) {
		return catObj.get(groupPosition);
	}

	@Override
	public int getGroupCount() {
		return catObj.size();
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,View convertView, ViewGroup parent) {
		View v=convertView;
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.browse_activity_row_parent, null);
		}
		TextView cat_name = (TextView) v.findViewById(R.id.item_name_parent);
		ImageView cat_image = (ImageView) v.findViewById(R.id.image_parent);
		ImageView img_arrow = (ImageView) v.findViewById(R.id.img_arrow_parent);
		View view = (View) v.findViewById(R.id.line_parent);
//		view.setVisibility(View.VISIBLE);
		cat_image.setVisibility(View.GONE);
		img_arrow.setVisibility(View.GONE);
		
//		convertView.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				LinearLayout ll = (LinearLayout)v;
//				RelativeLayout rl = (RelativeLayout) ll.getChildAt(0);
//				TextView tv = (TextView) rl.getChildAt(1);
//				tv.setTextColor(MyApplication.getInstance().getResources().getColor(R.color.main_cat_text_unselected));
//				
//			}
//		});
		
//		if(groupPosition == catObj.size()-1){
////			view.setPadding(0, -5, 0, -5);
//			
////			view.setVisibility(View.GONE);
//		}else{
//			view.setLayoutParams(new LinearLayout.LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
////			view.setPadding(0, 3, 0, 3);
//			view.setVisibility(View.VISIBLE);
//		}
		
		cat_name.setPadding(0, 10, 0, 10);
		
//		cat_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(con),R.style.CategoryParentView);
		cat_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(con));

		cat_name.setText(catObj.get(groupPosition).getCategory());
		
		return v;
	}

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	public void refreshList(ArrayList<CategorySubcategoryBean> categoryList){
		this.catObj = categoryList;
		notifyDataSetChanged();
	}

	@Override
	public View getRealChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		View v=convertView;
		if(convertView==null)
		{
			LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v=inflater.inflate(R.layout.browse_activity_row_child, null);
		}
		TextView cat_name = (TextView) v.findViewById(R.id.item_name_childs);
		ImageView cat_image = (ImageView) v.findViewById(R.id.image_childs);
		View viewLine = (View) v.findViewById(R.id.line_childs);
		cat_image.setVisibility(View.VISIBLE);
		
		cat_name.setPadding(0, 5, 0, 5);
		
//		if(isLastChild){
//			viewLine.setVisibility(View.VISIBLE);
//		}else{
//			viewLine.setVisibility(View.GONE);
//		}
		cat_name.setText(catObj.get(groupPosition).getChildren().get(childPosition).getCategory());
		
//		cat_name.setTypeface(CustomFonts.getInstance().getRobotoBold(con),R.style.CategoryChildView);
		cat_name.setTypeface(CustomFonts.getInstance().getRobotoBold(con));
		
		return v;
	}

	@Override
	public int getRealChildrenCount(int groupPosition) {
		return catObj.get(groupPosition).getChildren().size();
	}

}

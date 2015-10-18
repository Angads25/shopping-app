package com.sakshay.grocermax;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ExpandableListView.OnGroupExpandListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.adapters.ExpandableListAdapter;
import com.sakshay.grocermax.adapters.HomeListAdapter;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CategoriesProducts;
import com.sakshay.grocermax.bean.Simple;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public class HomeScreen extends BaseActivity implements OnItemClickListener{

	private ArrayList<CategorySubcategoryBean> catObj;
	private HomeListAdapter mAdapter;
	private int position = 0;
	
	private LinearLayout cat_main_layout;
	private ScrollView scroll_view;
//	private ListView sub_cat_listView;
//	private ImageView []arrowImageArray;              //show selected right side arrow of main category         
	private ImageView []catImageArray;                    //main category images
	private LinearLayout []linearMainCat;
//	EasyTracker tracker;
	int backImage[] = new int[10];
	
//	AnimatedExpandableListView expandableListView;          
	
	ExpandableListView expandableListView;
	
	ExpandableListAdapter exAdapter;                       //expand list adapter 
	String first_level="";
	String second_level="";
	String third_level="";
	int child_click=0;                       //child under group
	int group_click=0;                       //top level group , set 1 when move on to next page
	
	TextView tvSelctionCat;           //previously selected color change to bluish
	View viewtemp = null;
	TextView tvtemp = null;
	private LinearLayout[] catSelectedLL;
	private TextView categoryTV;

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.s_home_screen);
		try{

		addActionsInFilter(MyReceiverActions.PRODUCT_LIST_FROM_HOME);
		addActionsInFilter(MyReceiverActions.ALL_PRODUCTS_CATEGORY);
		 
		tvSelctionCat = new TextView(this);
		 
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
		}
		else{
			String response = UtilityMethods.readCategoryResponse(this, AppConstants.categoriesFile);
			catObj = UtilityMethods.getCategorySubCategory(response);
		}
		/*
		backImage[0] = R.drawable.beverages_large;
		backImage[1] = R.drawable.diary_large;
		backImage[2] = R.drawable.frozen_large;
		backImage[3] = R.drawable.fruits_large;
		backImage[4] = R.drawable.non_veg_large;
		backImage[5] = R.drawable.beverages_large;
		backImage[6] = R.drawable.diary_large;
		backImage[7] = R.drawable.frozen_large;
		backImage[8] = R.drawable.fruits_large;
		backImage[9] = R.drawable.non_veg_large;
		*/
		//TODO, abhishek 4 images being repeated here...what goes where is accoring to your logic please replace

		backImage[0] = R.drawable.beverages_large;
		backImage[1] = R.drawable.dairy_large;
		backImage[2] = R.drawable.frozen_large;
		backImage[3] = R.drawable.fruits_large;
		backImage[4] = R.drawable.non_veg_large;
		backImage[5] = R.drawable.beverages_large;
		backImage[6] = R.drawable.family_care_large;
		backImage[7] = R.drawable.home_care_large;
		backImage[8] = R.drawable.home_needs_large;
		backImage[9] = R.drawable.staples_large;

		categoryTV = (TextView) findViewById(R.id.tv_homescreen_category_heading);
		cat_main_layout = (LinearLayout) findViewById(R.id.cat_main_layout);
		scroll_view = (ScrollView) findViewById(R.id.scroll_view);
//		sub_cat_listView = (ListView) findViewById(R.id.cat_list);
		
//		expandableListView=(AnimatedExpandableListView)findViewById(R.id.lvExp);
		expandableListView=(ExpandableListView)findViewById(R.id.lvExp);

		if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
			expandableListView.getBackground().mutate().setAlpha(20);
		}


		expandableListView.setGroupIndicator(null);
		
		expandableListView.setOnGroupExpandListener(new OnGroupExpandListener() {
			@Override
			public void onGroupExpand(int groupPosition) {
				for (int i = 0; i < expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
					if (i != groupPosition)
						expandableListView.collapseGroup(i);
				}
			}
		});
		
		expandableListView.setOnGroupClickListener(new OnGroupClickListener() {
			@Override
			public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
				// TODO Auto-generated method stub

//				if(viewtemp != null){                          //from blue
//					viewtemp.setVisibility(View.VISIBLE);
//				}
//				if(tvtemp != null){
////					tvtemp.setTextColor(getResources().getColor(R.color.main_child_cat_text_unselected));
//					tvtemp.setTextColor(getResources().getColor(R.color.white));
//				}                                           
//				
//				View viewe = (View) v.findViewById(R.id.line_parent);
//				viewe.setVisibility(View.GONE);
//				
//				TextView cat_names = (TextView) v.findViewById(R.id.item_name_parent);
//
////				cat_names.setTextColor(getResources().getColor(R.color.main_child_cat_text_selected));
//				cat_names.setTextColor(getResources().getColor(R.color.subsubcategorycolor));
//				
//				viewtemp = viewe;        
//				tvtemp = cat_names;       //to blue


				if (group_click == 0) {

					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
						if (!keyboardVisibility)
							imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

					} else {
						if (keyboardVisibility)
							imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
					}

					boolean expandStatus = false;
					second_level = catObj.get(position).getChildren().get(groupPosition).getCategory();

					MySharedPrefs.INSTANCE.putBradecrum(first_level + ">>" + second_level);
					for (int i = 0; i < catObj.get(position).getChildren().get(groupPosition).getChildren().size(); i++) {
						if (catObj.get(position).getChildren().get(groupPosition).getChildren().get(i).getChildren().size() > 0) {
							expandStatus = true;                               //means any one has more child after expandable,so will not move on to next screen and expand itself.
							break;
						}
					}
					if (expandStatus == false) {                              //call dryfruits category on next page directly b/c dry fruits has no further categories -> categories.
						group_click = 1;                                      //when move to next page

						for (int i = 0; i < expandableListView.getExpandableListAdapter().getGroupCount(); i++) {
							expandableListView.collapseGroup(i);
						}

						Intent call = new Intent(HomeScreen.this, CategoryTabs.class);
						Bundle call_bundle = new Bundle();
						ArrayList<CategorySubcategoryBean> list = catObj.get(position).getChildren().get(groupPosition).getChildren();

						if (list.size() > 0) {
							if (!list.get(0).getCategory().equals("All")) {
								CategorySubcategoryBean carBean = new CategorySubcategoryBean();
								carBean.setCategory("All");
								carBean.setCategoryId(catObj.get(position).getChildren().get(groupPosition).getCategoryId());
								list.add(0, carBean);
							}
						} else {
							CategorySubcategoryBean carBean = new CategorySubcategoryBean();
							carBean.setCategory("All");
							carBean.setCategoryId(catObj.get(position).getChildren().get(groupPosition).getCategoryId());
							list.add(carBean);
						}
						call_bundle.putSerializable("Categories", list);
						call_bundle.putSerializable("Header", catObj.get(position).getChildren().get(groupPosition).getBreadcrumb());
						call_bundle.putSerializable("selectedcatid", catObj.get(position).getChildren().get(groupPosition).getCategoryId());
						call.putExtras(call_bundle);
//						startActivity(call);

						group_click = 0;
						child_click = 0;

						showDialog();

//						ArrayList<CategorySubcategoryBean> al1 = catObj.get(position).getChildren();                                              //5 right side sub category
//						String catid = catObj.get(position).getChildren().get(position).getCategoryId();                                      //selected catid
//						ArrayList<CategorySubcategoryBean> al = catObj.get(position).getChildren().get(groupPosition).getChildren();              //size 13 sub category
						System.out.println("======checking 11=========" + catObj.get(position).getChildren().get(groupPosition).getCategoryId());
						String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(groupPosition).getCategoryId();
						myApi.reqAllProductsCategory(url);

						return true;
					} else {
					/* if (expandableListView.isGroupExpanded(groupPosition)) {
						 expandableListView.collapseGroupWithAnimation(groupPosition);
		                } else {
		                	expandableListView.expandGroupWithAnimation(groupPosition);
		                }*/
						//expandableListView.expandGroupWithAnimation(groupPosition);
						return false;
					}
					//expandableListView.expandGroupWithAnimation(groupPosition);
				}
				return true;
			}
		});
		
		expandableListView.setOnChildClickListener(new OnChildClickListener() {
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				if(child_click==0)
				{
				child_click=1;
				third_level=catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategory();

//				System.out.println(third_level + "====checking 22==" + catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId());

//				System.out.println(third_level + "====checking 22==" + catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId());
				group_click = 0;
				child_click = 0;

				MySharedPrefs.INSTANCE.putBradecrum(first_level+">>"+catObj.get(position).getChildren().get(groupPosition).getCategory()+">>"+third_level);
				if(catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getChildren().size()>0)
				{
					Intent call = new Intent(HomeScreen.this, CategoryTabs.class);
					Bundle call_bundle = new Bundle();
					
					ArrayList<CategorySubcategoryBean> list=catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getChildren();
					if(list.size() > 0){
						if(!list.get(0).getCategory().equals("All"))
						{
							CategorySubcategoryBean carBean=new CategorySubcategoryBean();
							carBean.setCategory("All");
							carBean.setCategoryId(catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId());
							list.add(0,carBean);
						}
					}else{
						CategorySubcategoryBean carBean=new CategorySubcategoryBean();
						carBean.setCategory("All");
						carBean.setCategoryId(catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId());
						list.add(carBean);
					}
					call_bundle.putSerializable("Categories", list);
					call_bundle.putSerializable("Header", catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getBreadcrumb());
					call.putExtras(call_bundle);
//					startActivity(call);
					showDialog();
					String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId();
					myApi.reqAllProductsCategory(url);
				}
				else
				{
					showDialog();
					String url = UrlsConstants.PRODUCT_LIST_URL + catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId() + "&page=1";
					MySharedPrefs.INSTANCE.putCatId(catObj.get(position).getChildren().get(groupPosition).getChildren().get(childPosition).getCategoryId());
					myApi.reqProductListFromHomeScreen(url);
				}
				}
				return true;
			}
		});
		
//		sub_cat_listView.setOnItemClickListener(this);
		
		mAdapter = new HomeListAdapter(mContext, catObj.get(0).getChildren());
		exAdapter = new ExpandableListAdapter(mContext, catObj.get(0).getChildren());

		LayoutInflater inflater = this.getLayoutInflater();
//		arrowImageArray = new ImageView[catObj.size()];
		catImageArray = new ImageView[catObj.size()];
		linearMainCat = new LinearLayout[catObj.size()];
		catSelectedLL = new LinearLayout[catObj.size()];             //handle selection and unselection of selected main category.

		for (int i = 0; i < catObj.size(); i++) {                                 //handling main category on left side of screen in view
			View view = inflater.inflate(R.layout.item_cat_main, null);

			catImageArray[i] = (ImageView) view.findViewById(R.id.cat_icon);                //main category image like staples
			TextView cat_name = (TextView) view.findViewById(R.id.cat_name);                //main category name
			TextView catNamePlaceHolderTV = (TextView)view .findViewById(R.id.cat_name_placeholder);
			ImageView indicator = (ImageView) view.findViewById(R.id.indicator);            //main category indicator like right side arrow
			linearMainCat[i] = (LinearLayout) view.findViewById(R.id.ll_main_cat);
			catSelectedLL[i] = (LinearLayout) view.findViewById(R.id.ll_cat_main_selected);
			catImageArray[i].setImageResource(getImageResource(catObj.get(i).getCategory()));

			cat_name.setText(catObj.get(i).getCategory());
			catNamePlaceHolderTV.setText(catObj.get(i).getCategory());
			cat_name.setTypeface(CustomFonts.getInstance().getRobotoRegular(this));

//			arrowImageArray[i] = indicator;
			if (i != 0) {
//				arrowImageArray[i].setVisibility(View.INVISIBLE);                           //main category right indicator hide
				catImageArray[i].setSelected(false);
				cat_name.setTextColor(getResources().getColor(R.color.white));
				catSelectedLL[i].setVisibility(View.INVISIBLE);
			} else {
//				linearMainCat[i].setBackgroundColor(getResources().getColor(R.color.main_cat_selected));
				categoryTV.setText(catObj.get(i).getCategory());
				tvSelctionCat = cat_name;
				cat_name.setTextColor(getResources().getColor(R.color.main_cat_text_selected));
//				arrowImageArray[i].setVisibility(View.VISIBLE);                            //main category right indicator visible
//				catImageArray[i].setSelected(true);
//				sub_cat_listView.setAdapter(mAdapter);                //sub category adapter(on right side top)
				expandableListView.setAdapter(exAdapter);             //under sub category adapter
				first_level = catObj.get(i).getCategory();
				catSelectedLL[i].setVisibility(View.VISIBLE);

			}
			view.setTag(i);
			view.setOnClickListener(listener);
			catImageArray[i].setMinimumHeight((linearMainCat[i].getMeasuredHeight())); // trying to make it a square

			cat_main_layout.addView(view);                       //main category(left side) adding view under it
		}
		
		initHeader(findViewById(R.id.header), true, null);
		
//		icon_header_back.setVisibility(View.GONE);
		icon_header_logo_with_search.setClickable(false);
		icon_header_logo_with_search.setEnabled(false);
		icon_header_logo_without_search.setClickable(false);
		icon_header_logo_without_search.setEnabled(false);
		}catch(Exception e){
			new GrocermaxBaseException("HomeScreen","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
	 @Override
	 public void onWindowFocusChanged(boolean hasFocus) {
	  super.onWindowFocusChanged(hasFocus);
		try{
	  Drawable drawable_groupIndicator = 
//	   getResources().getDrawable(R.drawable.arrow_cb);            //temp commented
			  getResources().getDrawable(R.drawable.close_icon);  //temp done
	  int drawable_width = drawable_groupIndicator.getMinimumWidth();
	  
	  if(android.os.Build.VERSION.SDK_INT < 
	   android.os.Build.VERSION_CODES.JELLY_BEAN_MR2){
	   expandableListView.setIndicatorBounds(
	    expandableListView.getWidth()-drawable_width, 
	    expandableListView.getWidth());
	  }else{
	   expandableListView.setIndicatorBoundsRelative(
	    expandableListView.getWidth()-drawable_width, 
	    expandableListView.getWidth());
	  }
		}catch(Exception e){
			new GrocermaxBaseException("HomeScreen","onWindowFocusChanged",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	 }
	
	private int getImageResource(String name){
		int resId = 0;
		try {
			name = name.replaceAll(" ", "_");
			name = name.replaceAll("-", "_");
			name = name.toLowerCase();
			if (name.contains("fruits")) {
				name = "fruits";
			} else if (name.contains("dairy")) {
				name = "dairy";
			}

			resId = getResources().getIdentifier("cat_" + name, "drawable", HomeScreen.this.getApplicationInfo().packageName);
			if (resId == 0) {
				resId = getResources().getIdentifier("cat_staples", "drawable", HomeScreen.this.getApplicationInfo().packageName);
			}
		}
		catch(Exception e){
			new GrocermaxBaseException("HomeScreen","getImageResource",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return resId;
	}

	OnClickListener listener = new OnClickListener() {
		@Override
		public void onClick(View view) {
			try{
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
				if(!keyboardVisibility)
				imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
			}else{
				if(keyboardVisibility)
					imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
			}

			for(int i=0;i<expandableListView.getExpandableListAdapter().getGroupCount();i++)
			{
				if(expandableListView.isGroupExpanded(i)){
//					expandableListView.collapseGroupWithAnimation(i);
					expandableListView.collapseGroup(i);
				}
			}
			tvSelctionCat.setTextColor(getResources().getColor(R.color.white));         //unselected text color bluish for previously selected in main category
			catSelectedLL[position].setVisibility(View.INVISIBLE);                     //unselected previous main category selected
//			linearMainCat[position].setBackgroundColor(getResources().getColor(R.color.main_cat_unselected));    //selected background set blue in main category
			position = (Integer) view.getTag();
			
			TextView cat_name = (TextView) view.findViewById(R.id.cat_name);                           //get view of currently selected main category
			cat_name.setTextColor(getResources().getColor(R.color.main_cat_text_selected));              //selected text color white of main category
			catSelectedLL[position] = (LinearLayout) view.findViewById(R.id.ll_cat_main_selected);
			catSelectedLL[position].setVisibility(View.VISIBLE);
			//TODO, unselect others now

//			LinearLayout catSelectedLL = (LinearLayout) view.findViewById(R.id.ll_cat_main_selected);
//			catSelectedLL.setVisibility(View.VISIBLE);
			catSelectedLL[position].setVisibility(View.VISIBLE);                     //select current selected main category

			categoryTV.setText(catObj.get(position).getCategory());
			
//			linearMainCat[position].setBackgroundColor(getResources().getColor(R.color.main_cat_selected));
			
			first_level=catObj.get(position).getCategory();
			mAdapter.refreshList(catObj.get(position).getChildren());
			exAdapter.refreshList(catObj.get(position).getChildren());
			hideAllImage();
//			arrowImageArray[position].setVisibility(View.VISIBLE);
			//catImageArray[position].setSelected(true);
			//scroll_view.scrollTo(0, 0);
			
			tvSelctionCat = cat_name;                         //assign currently selected view to previously selected holder           
			
			//expandableListView.setBackgroundResource(backImage[position]);
			expandableListView.setCacheColorHint(android.R.color.transparent);
			}catch(Exception e){
				new GrocermaxBaseException("HomeScreen","listener",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};

	private void hideAllImage() {
		try{
		for (int i = 0; i < catImageArray.length; i++) {
//			arrowImageArray[i].setVisibility(View.INVISIBLE);
			catImageArray[i].setSelected(false);
		  }
		}catch(Exception e){
			new GrocermaxBaseException("HomeScreen","hideAllImage",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	@Override
	public void OnResponse(Bundle bundle) {
		// TODO Auto-generated method stub
		String action = bundle.getString("ACTION");
		if (action.equals(MyReceiverActions.ALL_PRODUCTS_CATEGORY)) {
//			group_click = 0;
			Simple responseBean = (Simple) bundle.getSerializable(ConnectionService.RESPONSE);
			if (responseBean.getFlag().equalsIgnoreCase("1")) {
				Intent call = new Intent(HomeScreen.this, CategoryTabs.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("PRODUCTDATA", responseBean);
				call.putExtras(call_bundle);
				startActivity(call);
			}else{
				UtilityMethods.customToast(AppConstants.ToastConstant.NO_RESULT_FOUND,mContext);
			}
//			Simple simple1 = (Simple) bundle.getSerializable(ConnectionService.RESPONSE);
//			if (responseBean.getResult().equalsIgnoreCase("1")) {
//				ArrayList<CategoriesProducts> hotproduct = responseBean.getHotproduct();
//				ArrayList<CategoriesProducts> productList = responseBean.getProductList();
//				if(hotproduct.size() > 0 && productList.size() > 0){
//
//				}
//			}
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		try{
//			Intent call = new Intent(HomeScreen.this, CategoryTabs.class);
//			Bundle call_bundle = new Bundle();
//			call_bundle.putSerializable("Categories", catObj.get(position).getChildren().get(arg2).getChildren());
//			call_bundle.putSerializable("Header", catObj.get(position).getChildren().get(arg2).getBreadcrumb());
//			call.putExtras(call_bundle);
//			startActivity(call);

			showDialog();
//			String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(arg2).getCategoryId();
			String url = UrlsConstants.GET_ALL_PRODUCTS_OF_CATEGORY + catObj.get(position).getChildren().get(arg2).getChildren().get(position).getCategoryId();
			myApi.reqAllProductsCategory(url);

		}catch(Exception e){
			new GrocermaxBaseException("HomeScreen","onItemClick",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*for(int i=0;i<expandableListView.getExpandableListAdapter().getGroupCount();i++)
		{
				expandableListView.collapseGroup(i);
		}*/
		try{
			child_click=0;
			group_click=0;
			initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("HomeScreen","onResume",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try{
			EasyTracker.getInstance(this).activityStart(this);
			FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
			FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
			EasyTracker.getInstance(this).activityStop(this);
			FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }





	/* (non-Javadoc)
 * @see android.support.v4.app.FragmentActivity#onBackPressed()
 */

}

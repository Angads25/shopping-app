package com.rgretail.grocermax;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.appsflyer.AppsFlyerLib;
import com.flurry.android.FlurryAgent;
//import com.google.analytics.tracking.android.EasyTracker;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.Category;
import com.rgretail.grocermax.bean.CategoryListBean;
import com.rgretail.grocermax.bean.ProductListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

public class CategoryList extends BaseActivity {
	private String header;
	// private ListView mList;
	// private CategoryListBean catObj;
	private ArrayList<CategorySubcategoryBean> catObj;
	private Category category;
	LinearLayout llList;
//	EasyTracker tracker;

	@SuppressWarnings("unchecked")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		try{
			AppsFlyerLib.setCurrencyCode("INR");
			AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
			AppsFlyerLib.sendTracking(getApplicationContext());
		}catch(Exception e){}

	try{
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			catObj = (ArrayList<CategorySubcategoryBean>) bundle
					.getSerializable("Categories");
			header = bundle.getString("Header");
		}
		setContentView(R.layout.activity_categoty_subcategory_list);

		addActionsInFilter(MyReceiverActions.CATEGORY_LIST);
		addActionsInFilter(MyReceiverActions.PRODUCT_LIST);

		llList = (LinearLayout) findViewById(R.id.llList);

		// mList = (ListView) findViewById(R.id.category_list);
		// CategoryListAdapter mAdapter = new CategoryListAdapter(mContext,
		// catObj.getCategory());
		// mList.setAdapter(mAdapter);
		// mList.setOnItemClickListener(new OnItemClickListener() {
		// @Override
		// public void onItemClick(AdapterView<?> arg0, View arg1, int position,
		// long arg3) {
		// showDialog();
		// category = catObj.getCategory().get(position);
		// String url =
		// UrlsConstants.CATEGORY_LISTING_URL+category.getCategoryId();
		// myApi.reqCategoryList(url);
		// }
		// });

		getCategoryView(CategoryList.this, catObj, llList, false, 0);

		initHeader(findViewById(R.id.header), true, header);
		initFooter(findViewById(R.id.footer), 0, 3);
	}catch(Exception e){
		new GrocermaxBaseException("CategoryList","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
	}

	}

	@Override
	public void OnResponse(Bundle bundle) {
		String action = bundle.getString("ACTION");
		try{
		if (action.equals(MyReceiverActions.CATEGORY_LIST)) {
			CategoryListBean bean = (CategoryListBean) bundle
					.getSerializable(ConnectionService.RESPONSE);
			if (bean.getFlag().equalsIgnoreCase("1")) {
				String catId = bean.getCategory().get(0).getCategoryId();
				if (catId == null || catId.trim().length() == 0) {
					showDialog();
					// header = category.getName();
					String url = UrlsConstants.PRODUCT_LIST_URL
							+ category.getCategoryId();
					myApi.reqProductList(url);
				} else {
					Intent call = new Intent(mContext, CategoryList.class);
					Bundle call_bundle = new Bundle();
					call_bundle.putSerializable("Categories", bean);
					call_bundle.putSerializable("Header", header);
					call.putExtras(call_bundle);
					startActivity(call);
				}
			} else {
				UtilityMethods.customToast(bean.getResult(), mContext);
			}
		} else if (action.equals(MyReceiverActions.PRODUCT_LIST)) {
			ProductListBean productListBean = (ProductListBean) bundle
					.getSerializable(ConnectionService.RESPONSE);
			if (productListBean.getFlag().equalsIgnoreCase("1")) {
				MySharedPrefs.INSTANCE.putIsSearched(false);
				Intent call = new Intent(mContext, ProductListScreen.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("ProductList", productListBean);
				call_bundle.putSerializable("Header", header);
				call.putExtras(call_bundle);
				startActivity(call);
			} else {
				UtilityMethods.customToast(productListBean.getResult(), mContext);
			}
		}
		}catch(Exception e){
			new GrocermaxBaseException("CategoryList","onResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public void getCategoryView(final Activity activity,
			ArrayList<CategorySubcategoryBean> category,
			final LinearLayout mLayout, final boolean isExpanded,
			final int level) {
		// int categoryLevelCount = 0;
	try{
		View categoryView = null;
		if (category != null) {
			for (int i = 0; i < category.size(); i++) {
				final CategorySubcategoryBean object = category.get(i);
				categoryView = activity.getLayoutInflater().inflate(
						R.layout.listview_element, null);
				final TextView name = (TextView) categoryView
						.findViewById(R.id.name);
				name.setText(object.getCategory());
				final LinearLayout childView = (LinearLayout) categoryView
						.findViewById(R.id.childlayout);

				LinearLayout llExpendCollepse = (LinearLayout) categoryView
						.findViewById(R.id.llExpendCollepse);

				View divider = categoryView.findViewById(R.id.divider);
				divider.setBackgroundColor(Color.parseColor("#E8E8E8"));

				ImageView countimage = (ImageView) categoryView
						.findViewById(R.id.countimage);
				final ImageView catimage = (ImageView) categoryView
						.findViewById(R.id.catimage);

				if (isExpanded) {
					if (object.getChildren().size() > 0) {
						catimage.setImageResource(R.drawable.ic_action_new);
					} else {
						catimage.setImageResource(R.drawable.list_right_arrow);
					}

					countimage.setVisibility(View.GONE);
					Resources rImg = activity.getResources();
					float px10 = TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 10,
							rImg.getDisplayMetrics());
					catimage.setPadding(categoryView.getPaddingLeft(),
							(int) px10 / 4, categoryView.getPaddingRight()
									+ (int) px10, (int) px10 / 4);
					float px40 = TypedValue.applyDimension(
							TypedValue.COMPLEX_UNIT_DIP, 10,
							rImg.getDisplayMetrics());
					name.setPadding(categoryView.getPaddingLeft() + (int) px40,
							(int) px40, categoryView.getPaddingRight(),
							(int) px40);
					if (level == 1)
						categoryView.setBackgroundColor(Color
								.parseColor("#DFDFDF"));
					else if (level == 2)
						categoryView.setBackgroundColor(Color
								.parseColor("#C1C1C1"));
				} else {

					int resourceId;
					if (object.getChildren().size() > 0) {
						resourceId = R.drawable.ic_action_new;
					} else {
						resourceId = R.drawable.list_right_arrow;
					}

					if (resourceId == 0) {
						catimage.setVisibility(View.GONE);
					} else {
						catimage.setVisibility(View.VISIBLE);
						catimage.setImageResource(resourceId);
					}
				}

				name.setTextColor(activity.getResources().getColor(
						R.color.black));
				if (object.getChildren().size() > 0) {
					countimage.setImageResource(R.drawable.ic_action_new);
				} else {
					countimage.setImageResource(R.drawable.list_right_arrow);
				}
				llExpendCollepse.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (object.getChildren() == null
								|| object.getChildren().size() <= 0) {

							if (AppConstants.DEBUG) {
								Log.e("",
										"EXECUTE ID::" + object.getCategoryId());
							}
							showDialog();
							// String url = UrlsConstants.CATEGORY_LISTING_URL
							// + object.getCategoryId();
							// myApi.reqCategoryList(url);
							String url = UrlsConstants.PRODUCT_LIST_URL
									+ object.getCategoryId();
							myApi.reqProductList(url);

						} else {
							if (childView.getVisibility() == View.GONE) {
								if (isExpanded)
									catimage.setImageResource(R.drawable.list_down_arrow);
								else {
									// int resourceId = activity
									// .getResources()
									// .getIdentifier(
									// "cat_selected_"
									// + object.getCategoryId(),
									// "drawable",
									// activity.getPackageName());

									int resourceId;
									if (object.getChildren().size() > 0) {
										resourceId = R.drawable.list_down_arrow;
									} else {
										resourceId = R.drawable.list_right_arrow;
									}
									if (resourceId == 0) {
										catimage.setVisibility(View.GONE);
									} else {
										catimage.setVisibility(View.VISIBLE);
										catimage.setImageResource(resourceId);
									}
									name.setTextColor(activity.getResources()
											.getColor(R.color.black));
								}

								childView.setVisibility(View.VISIBLE);

								ArrayList<CategorySubcategoryBean> childList = object
										.getChildren();
								getCategoryView(activity, childList, childView,
										true, level + 1);
							} else {
								if (isExpanded)
									catimage.setImageResource(R.drawable.ic_action_new);
								else {

									int resourceId = R.drawable.ic_action_new;
									if (resourceId == 0) {
										catimage.setVisibility(View.GONE);
									} else {
										catimage.setVisibility(View.VISIBLE);
										catimage.setImageResource(resourceId);
									}

									name.setTextColor(activity.getResources()
											.getColor(R.color.black));
								}
								childView.removeAllViews();
								childView.setVisibility(View.GONE);
								// childView.setTag(1);
							}
						}

					}
				});
				categoryView.setOnClickListener(new OnClickListener() {
					public void onClick(View v) {
						if (object.getCategoryId() != null) {

							if (AppConstants.DEBUG) {
								Log.e("",
										"EXECUTE ID::" + object.getCategoryId());
							}
							showDialog();
							// String url = UrlsConstants.CATEGORY_LISTING_URL
							// + object.getCategoryId();
							// myApi.reqCategoryList(url);
							String url = UrlsConstants.PRODUCT_LIST_URL
									+ object.getCategoryId();
							myApi.reqProductList(url);

						}

					}
				});
				if (i == 0) {
					categoryView.findViewById(R.id.topShadow).setVisibility(
							View.VISIBLE);
					categoryView.findViewById(R.id.divider).setVisibility(
							View.VISIBLE);
				} else if (i == category.size() - 1) {
					categoryView.findViewById(R.id.bottomShadow).setVisibility(
							View.VISIBLE);
					categoryView.findViewById(R.id.divider).setVisibility(
							View.GONE);
				} else {
					categoryView.findViewById(R.id.bottomShadow).setVisibility(
							View.GONE);
					categoryView.findViewById(R.id.topShadow).setVisibility(
							View.GONE);
					categoryView.findViewById(R.id.divider).setVisibility(
							View.VISIBLE);
				}

				// if(categoryLevelCount == 2){
				// categoryView.setBackgroundColor(Color.parseColor("#DFDFDF"));
				// }
				// else if(categoryLevelCount == 3)
				// categoryView.setBackgroundColor(Color.parseColor("#C1C1C1"));

				mLayout.addView(categoryView);
			}
		}
	  }catch(Exception e){
		new GrocermaxBaseException("CategoryList","getCategoryView",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
	  }
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
		try{
			initHeader(findViewById(R.id.header), true, null);
		}catch(Exception e){
			new GrocermaxBaseException("CategoryList","onResume()",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
	}

	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
		try{
			AppsFlyerLib.onActivityResume(this);
		}catch(Exception e){}
    	try{
//			EasyTracker.getInstance(this).activityStart(this);
//	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){}
    }

    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
		try{
			AppsFlyerLib.onActivityPause(this);
		}catch(Exception e){}
    	try{
//	    	tracker.activityStop(this);
	    	FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }

}

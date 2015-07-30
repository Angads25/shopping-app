package com.sakshay.grocermax;

import java.util.ArrayList;

import junit.framework.Assert;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.ListSublistConstants.ListConstant;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public class BrowseActivity extends BaseActivity implements OnClickListener {

	private String header;
	LinearLayout main_lay;
	EasyTracker tracker;

	String[] browse_cat = { "Browse by Categories", "Browse by Offers",
			"Shopping Lists" };
	String[] browse_cat_images = { "icon_checkout", "icon_offers",
			"icon_shoppinglist", "icon_smart_cart", "icon_quickshop" };

	ArrayList<CategorySubcategoryBean> category;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.browse_activity);

		addActionsInFilter(MyReceiverActions.CATEGORY_LIST);

		// addActionsInFilter(MyReceiverActions.CATEGORY_SUB_CATEGORY_LIST);

		main_lay = (LinearLayout) findViewById(R.id.main_lay);
		for (int i = 0; i < browse_cat.length; i++) {
			LinearLayout sub_lay = (LinearLayout) ((LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE))
					.inflate(R.layout.browse_activity_row, null, false);

			sub_lay.setTag(browse_cat[i]);
			ImageView image = (ImageView) sub_lay.findViewById(R.id.image);
			TextView name = (TextView) sub_lay.findViewById(R.id.item_name);

			image.setBackgroundDrawable(getResources().getDrawable(
					getDrawableID(mContext, browse_cat_images[i])));
			name.setText(browse_cat[i]);

			sub_lay.setOnClickListener(this);
			main_lay.addView(sub_lay);
		}

		initHeader(findViewById(R.id.header), true, "Browse by");
		initFooter(findViewById(R.id.footer), 0, -1);
	}

	public int getDrawableID(Context context, String name) {
		Assert.assertNotNull(context);
		Assert.assertNotNull(name);

		return context.getResources().getIdentifier(name, "drawable",
				context.getPackageName());
	}

	@Override
	public void onClick(View v) {
		header = (String) v.getTag();

		if (header.equals(browse_cat[0])) {
			showDialog();

			// String url = UrlsConstants.CATEGORY_LISTING_URL + "0";// for
			// parent
			String url = UrlsConstants.CATEGORY_COLLECTION_LISTING_URL;// +
																		// "0";//
																		// for
																		// parent

			myApi.reqCategorySubCategoryList(url);
		}
	}

	@Override
	void OnResponse(Bundle bundle) {

		String jsonResponse = (String) bundle
				.getSerializable(ConnectionService.RESPONSE);

		if (!jsonResponse.trim().equals("")
				&& getCategorySubCategory(jsonResponse).size() > 0) {
			Intent call = new Intent(BrowseActivity.this, CategoryList.class);
			Bundle call_bundle = new Bundle();
			call_bundle.putSerializable("Categories", category);
			call_bundle.putSerializable("Header", header);
			call.putExtras(call_bundle);
			// call.putExtra("Categories", category);
			// call.putExtra("Header", header);
			startActivity(call);
		} else {
			UtilityMethods.customToast(ToastConstant.DATA_NOT_FOUND, mContext);
		}

		// CategoryListBean userDataBean = (CategoryListBean) bundle
		// .getSerializable(ConnectionService.RESPONSE);
		// if (userDataBean.getFlag().equalsIgnoreCase("1")) {
		// Intent call = new Intent(BrowseActivity.this, CategoryList.class);
		// Bundle call_bundle = new Bundle();
		// call_bundle.putSerializable("Categories", userDataBean);
		// call_bundle.putSerializable("Header", header);
		// call.putExtras(call_bundle);
		// startActivity(call);
		// } else {
		// Toast.makeText(mContext, userDataBean.getResult(),
		// Toast.LENGTH_LONG).show();
		// }
	}

	ArrayList<CategorySubcategoryBean> getCategorySubCategory(String content) {
		try {
			category = new ArrayList<CategorySubcategoryBean>();
			CategorySubcategoryBean categoryOb;
			JSONObject jsonObject = new JSONObject(content.trim());

			JSONArray jsonMainArray = jsonObject
					.getJSONArray(ListConstant.TAG_CATEGORY);
			for (int i = 0; i < jsonMainArray.length(); i++) {
				JSONObject jsonName = jsonMainArray.getJSONObject(i);
				categoryOb = new CategorySubcategoryBean();

				categoryOb.setCategory(""
						+ jsonName.getString(ListConstant.TAG_NAME));
				categoryOb.setCategoryId(""
						+ jsonName.getString(ListConstant.TAG_CATEGORYID));

				JSONArray jsonChild;
				try {
					jsonChild = jsonName
							.getJSONArray(ListConstant.TAG_CHILDREN);
				} catch (Exception e) {
					jsonChild = new JSONArray();
				}

				for (int j = 0; j < jsonChild.length(); j++) {
					jsonName = jsonChild.getJSONObject(j);
					CategorySubcategoryBean categorySubOb = new CategorySubcategoryBean();
					categorySubOb.setCategory(""
							+ jsonName.getString(ListConstant.TAG_NAME));
					categorySubOb.setCategoryId(""
							+ jsonName.getString(ListConstant.TAG_CATEGORYID));

					JSONArray jsonChildCategory;
					try {
						jsonChildCategory = jsonName
								.getJSONArray(ListConstant.TAG_CHILDREN);
					} catch (Exception e) {
						jsonChildCategory = new JSONArray();
					}
					for (int k = 0; k < jsonChildCategory.length(); k++) {
						jsonName = jsonChildCategory.getJSONObject(k);
						CategorySubcategoryBean categorySubSubOb = new CategorySubcategoryBean();
						categorySubSubOb.setCategory(""
								+ jsonName.getString(ListConstant.TAG_NAME));
						categorySubSubOb
								.setCategoryId(""
										+ jsonName
												.getString(ListConstant.TAG_CATEGORYID));

						JSONArray jsonChildSubCategory;
						try {
							jsonChildSubCategory = jsonName
									.getJSONArray(ListConstant.TAG_CHILDREN);
						} catch (Exception e) {
							jsonChildSubCategory = new JSONArray();
						}
						for (int l = 0; l < jsonChildSubCategory.length(); l++) {
							jsonName = jsonChildSubCategory.getJSONObject(l);
							CategorySubcategoryBean categorySubSubSubOb = new CategorySubcategoryBean();
							categorySubSubSubOb
									.setCategory(""
											+ jsonName
													.getString(ListConstant.TAG_NAME));
							categorySubSubSubOb
									.setCategoryId(""
											+ jsonName
													.getString(ListConstant.TAG_CATEGORYID));
							JSONArray jsonChildSubSubCategory;
							try {
								jsonChildSubSubCategory = jsonName
										.getJSONArray(ListConstant.TAG_CHILDREN);
							} catch (Exception e) {
								jsonChildSubSubCategory = new JSONArray();
							}
							for (int m = 0; m < jsonChildSubSubCategory
									.length(); m++) {
								jsonName = jsonChildSubSubCategory
										.getJSONObject(m);
								CategorySubcategoryBean categorySubSubSubSubOb = new CategorySubcategoryBean();
								categorySubSubSubSubOb
										.setCategory(""
												+ jsonName
														.getString(ListConstant.TAG_NAME));
								categorySubSubSubSubOb
										.setCategoryId(""
												+ jsonName
														.getString(ListConstant.TAG_CATEGORYID));
								categorySubSubSubOb
										.addCategory(categorySubSubSubSubOb);
							}

							categorySubSubOb.addCategory(categorySubSubSubOb);
						}
						categorySubOb.addCategory(categorySubSubOb);
					}
					categoryOb.addCategory(categorySubOb);

				}
				category.add(categoryOb);
			}
		} catch (JSONException e) {
		}
		return category;
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		initHeader(findViewById(R.id.header), true, null);
	}
	
	@Override
    protected void onStart() {
    	// TODO Auto-generated method stub
    	super.onStart();
    	try{
	    	tracker.activityStart(this);
	    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
	    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
    	}catch(Exception e){}
    }
    
    @Override
    protected void onStop() {
    	// TODO Auto-generated method stub
    	super.onStop();
    	try{
	    	tracker.activityStop(this);
	    	FlurryAgent.onEndSession(this);
    	}catch(Exception e){}
    }

}

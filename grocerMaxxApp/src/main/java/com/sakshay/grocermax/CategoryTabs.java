package com.sakshay.grocermax;

import java.net.URLEncoder;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.Product;
import com.sakshay.grocermax.bean.ProductDetailsListBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.Constants.ToastConstant;
import com.sakshay.grocermax.utils.CustomFonts;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;
import com.viewpagerindicator.TabPageIndicator;

public class CategoryTabs extends BaseActivity
{


	private String header;
	private ArrayList<CategorySubcategoryBean> catObj;
	public Product product;
	TextView tv_bradcrum;
	LinearLayout ll_brad_crum;
	HorizontalScrollView hscrollview;
	ImageView iconHeaderHome;
	Context mContext = this;
	public static int clickStatus=0;
	public static ArrayList<ProductListFragments.CallAPI> asyncTasks=new ArrayList<ProductListFragments.CallAPI>();
	EasyTracker tracker;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.s_category_tabs);
		try {

			addActionsInFilter(MyReceiverActions.PRODUCT_CONTENT_LIST);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);
			ProductListFragments p = new ProductListFragments();

			Bundle bundle = getIntent().getExtras();
			if (bundle != null) {
				catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
				header = bundle.getString("Header");
			/*for(int i=0;i<catObj.size();i++)
			{
				ProductListFragments.CallAPI callapi=new ProductListFragments().new CallAPI();
				asyncTasks.add(callapi);
			}*/
			}


		LinearLayout llBreadcrumb = (LinearLayout)findViewById(R.id.llbreadcrum);
//		llBreadcrumb.setVisibility(View.VISIBLE);
		llBreadcrumb.setVisibility(View.GONE);

		tv_bradcrum=(TextView)findViewById(R.id.tv_Bradcrum);

		ll_brad_crum=(LinearLayout)findViewById(R.id.ll_Bradcrum);
		ll_brad_crum.setBackgroundColor(getResources().getColor(R.color.breadcrum_color));
		hscrollview=(HorizontalScrollView)findViewById(R.id.hscrollview);

//		hscrollview.setVisibility(View.VISIBLE);
			if (MySharedPrefs.INSTANCE.getBradecrum() != null) {
				String brade_crum[] = MySharedPrefs.INSTANCE.getBradecrum().split(">>");

				for (int i = 0; i < brade_crum.length; i++) {
					addImageView(ll_brad_crum);
					addTextView(ll_brad_crum, brade_crum[i]);
				}
			}
			hscrollview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
				@Override
				public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
					hscrollview.removeOnLayoutChangeListener(this);
					hscrollview.fullScroll(View.FOCUS_RIGHT);
				}
			});

			ll_brad_crum.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					finish();
				}
			});

			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			pager.setAdapter(adapter);
			pager.setOffscreenPageLimit(catObj.size());

			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
			indicator.setViewPager(pager);

			View headerView = findViewById(R.id.header);
			initHeader(headerView, true, header.replaceAll("/", " >> "));

			TextView textView = (TextView) headerView.findViewById(R.id.screenName);

			textView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View arg0) {
					finish();
				}
			});



//        ViewPager pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//        pager.setOffscreenPageLimit(catObj.size());
//
//        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(pager);
//
//        View headerView = findViewById(R.id.header);
//        initHeader(headerView, true, header.replaceAll("/", " >> "));
//
//        TextView textView = (TextView)headerView.findViewById(R.id.screenName);
//
//        textView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

    }


	class GoogleMusicAdapter extends FragmentPagerAdapter {
        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return ProductListFragments.newInstance(catObj.get(position % catObj.size()));
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return catObj.get(position % catObj.size()).getCategory().toUpperCase();
        }

        @Override
        public int getCount() {
          return catObj.size();

        }
    }

	public void addToCart(String product_id, String quantity) {
    	showDialog();
		try {
			JSONArray products = new JSONArray();
			JSONObject prod_obj = new JSONObject();
			prod_obj.put("productid", product_id);
			prod_obj.put("quantity", quantity);
			products.put(prod_obj);
			String url;
			if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
			{
				url = UrlsConstants.ADD_TO_CART_URL
						+ MySharedPrefs.INSTANCE.getUserId() +"&products="
						+ URLEncoder.encode(products.toString(), "UTF-8");
			}
			else
			{
				url = UrlsConstants.ADD_TO_CART_URL
						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
						+ URLEncoder.encode(products.toString(), "UTF-8");
			}

			myApi.reqAddToCart(url);
		}catch(NullPointerException e){
			new GrocermaxBaseException("CategoryTabs", "addToCart", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "addToCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}

	}

	public void addToCartGuest(String product_id, String quantity) {
    	showDialog();
		try {
			JSONArray products = new JSONArray();
			JSONObject prod_obj = new JSONObject();
			prod_obj.put("productid", product_id);
			prod_obj.put("quantity", quantity);
			products.put(prod_obj);
			String url;

			url = UrlsConstants.ADD_TO_CART_GUEST_URL+"quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
					+ URLEncoder.encode(products.toString(), "UTF-8");
			myApi.reqAddToCart(url);

		}catch(NullPointerException e){
			new GrocermaxBaseException("CategoryTabs", "addToCartGuest", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "product_id"+product_id+"quantity"+quantity);
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "addToCartGuest", e.getMessage(), GrocermaxBaseException.EXCEPTION, "product_id"+product_id+"quantity"+quantity);
		}
	}


	
	@Override
	void OnResponse(Bundle bundle) {
		try{
		String action = bundle.getString("ACTION");
		if (action.equals(MyReceiverActions.PRODUCT_CONTENT_LIST)) {
			ProductDetailsListBean contentListBean = (ProductDetailsListBean) bundle
					.getSerializable(ConnectionService.RESPONSE);
			if (contentListBean.getFlag().equalsIgnoreCase("1")) {
				Intent call = new Intent(mContext, ProductDetailScreen.class);
				Bundle call_bundle = new Bundle();
				call_bundle.putSerializable("ProductContent", contentListBean.getProductDetail().get(0));
				call_bundle.putSerializable("Product", product);
				call_bundle.putString("BRAND", product.getBrand());
				call_bundle.putString("NAME", product.getProductName());
				call_bundle.putString("GRAMSORML", product.getGramsORml());
				call_bundle.putString("PROMOTION", product.getPromotionLevel());
				call.putExtras(call_bundle);
				startActivity(call);
			} else {
				UtilityMethods.customToast(contentListBean.getResult(), mContext);
			}
		} else if (action.equals(MyReceiverActions.ADD_TO_CART)) {
			if (action.equalsIgnoreCase(MyReceiverActions.ADD_TO_CART)) {
				BaseResponseBean bean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
				if (bean.getFlag().equalsIgnoreCase("1")) {
					cart_count_txt.setText(String.valueOf(bean.getTotalItem()));
					UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, mContext);
					MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
				} else if(bean.getFlag().equalsIgnoreCase("0")){
					UtilityMethods.customToast(bean.getResult(), mContext);
				}else {
					UtilityMethods.customToast(ToastConstant.ERROR_MSG, mContext);
				}
			}
		}
		}catch(NullPointerException e){
			new GrocermaxBaseException("CategoryTabs", "OnResponse", e.getMessage(), GrocermaxBaseException.NULL_POINTER, String.valueOf(bundle));
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "OnResponse", e.getMessage(), GrocermaxBaseException.EXCEPTION, String.valueOf(bundle));
		}
	}
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, null);
			if (martHeader != null) {
				martHeader.setVisibility(View.GONE);
			}
			clickStatus = 0;
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "onResume", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		try {
			for (int i = 0; i < asyncTasks.size(); i++)
				if (!asyncTasks.get(i).isCancelled()) {
					asyncTasks.get(i).cancel(true);
					System.out.println(asyncTasks.get(i) + "----" + asyncTasks.get(i).isCancelled());
				}
		}catch(Exception e){
			new GrocermaxBaseException("CategoryTabs", "onResume", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	
public void addTextView(LinearLayout ll,String value)
{
	    TextView tv1=new TextView(this);
	    LinearLayout .LayoutParams layoutParams1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
	    tv1.setLayoutParams(layoutParams1);
	    tv1.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
	    tv1.setPadding(30,0,30,0);
	    tv1.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
//	    tv1.setTextColor(Color.GRAY);
	    tv1.setTextColor(getResources().getColor(R.color.breadcrum_text_color));
	    tv1.setTextSize(13);
//	    tv1.setTypeface(null, Typeface.BOLD);
	    tv1.setText(value);
	    ll.addView(tv1);
}
public void addImageView(LinearLayout ll)
{
	ImageView img=new ImageView(this);
	LinearLayout .LayoutParams layoutParams1= new LinearLayout.LayoutParams(20,20);
	layoutParams1.gravity=Gravity.CENTER_VERTICAL;
	
	img.setLayoutParams(layoutParams1);
//	img.setPadding(10, 0, 10, 0);
    img.setImageResource(R.drawable.forward_icon);
    ll.addView(img);
}
public int getdp(int a)
{
	int paddingPixel = a;
	float density = this.getResources().getDisplayMetrics().density;
	int paddingDp = (int)(paddingPixel * density);
	return paddingDp;
}

@Override
protected void onStart() {
	// TODO Auto-generated method stub
	super.onStart();
	try{
    	tracker.activityStart(this);
    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
	}catch(Exception e){
		new GrocermaxBaseException("CategoryTabs", "onStart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
	}
}

@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	try{
    	tracker.activityStop(this);
    	FlurryAgent.onEndSession(this);
	}catch(Exception e){
		new GrocermaxBaseException("CategoryTabs", "onStop", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
	}
}
	
}

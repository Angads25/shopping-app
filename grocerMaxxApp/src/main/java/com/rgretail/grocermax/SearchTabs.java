package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.Window;

import com.dq.rocq.RocqAnalytics;
import com.flurry.android.FlurryAgent;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.api.SearchLoader;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductDetailsListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;

public class SearchTabs extends BaseActivity{
	private String searchString;
//	private static SearchTabs instance = null;
//	public static SearchTabs getInstance(){
//		if(instance == null){
//			instance = new SearchTabs();
//		}
//		return instance;
//	}
//	public SearchTabs(){
//		instance = this;
//	}
	
	private String header;
//	private ArrayList<CategorySubcategoryBean> catObj;
	public Product product;
	public static Context context;
//	TextView tv_bradcrum;
//	LinearLayout ll_brad_crum;
//	HorizontalScrollView hscrollview;
//	ImageView iconHeaderHome;
//	public static int clickStatus=0;
//	public static ArrayList<ProductListFragments.CallAPI> asyncTasks=new ArrayList<ProductListFragments.CallAPI>();
//	HorizontalScrollView horizontalScrollView;
//	EasyTracker tracker;
	public JSONObject json[];
	public List<HashMap<String, String>> listCategoryHashMap;
	int size;

	public static String sort_condition;

	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		context = this;
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.s_category_tabs);
        try {

			MyApplication.GTM_FROM="search result";

			Intent intent = getIntent();
			searchString = intent.getStringExtra("SEARCHSTRING");
//			Bundle bundle = intent.getExtras();
//			JSONObject[] strw = (JSONObject[])bundle.getSerializable("searchdata");

			//try{UtilityMethods.clickCapture(context,"","","","", AppConstants.GA_EVENT_OPEN_SEARCH);}catch(Exception e){}

			sort_condition="popularity";

			size = SearchLoader.jsonObjectTop.length;

			addActionsInFilter(MyReceiverActions.PRODUCT_CONTENT_LIST);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART);

			json = SearchLoader.jsonObjectTop;
			listCategoryHashMap = SearchLoader.listCategory;
//			ProductListFragments p = new ProductListFragments();

//		horizontalScrollView = (HorizontalScrollView) findViewById(R.id.hscrollview);
//		horizontalScrollView.setVisibility(View.GONE);


//			Bundle bundle = getIntent().getExtras();
			header = "";

			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
			ViewPager pager = (ViewPager) findViewById(R.id.pager);
			pager.setAdapter(adapter);
//        pager.setOffscreenPageLimit(catObj.size());

			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
			if(pager!=null)
				indicator.setViewPager(pager);
				System.out.println("idicator count ="+pager.getChildCount());
			  // indicator.setCurrentItem(3);

			try {
				if(MySharedPrefs.INSTANCE.getTabIndex()!=null){
                    indicator.setCurrentItem(Integer.parseInt(MySharedPrefs.INSTANCE.getTabIndex()));
					MySharedPrefs.INSTANCE.putTabIndex("0");
				}
                else
                    indicator.setCurrentItem(0);
			} catch (Exception e) {
				indicator.setCurrentItem(0);
			}















//			if (size > 2) {
//				pager.setOffscreenPageLimit(2);
//			} else {
//				pager.setOffscreenPageLimit(size);
//			}


//			Handler handler;
//			handler = new Handler();
//			handler.postDelayed(runningThread, 0000);

//			Message msg = handler.obtainMessage();
//			handler.sendMessage(msg);


//			MakeFragments makeFragments = new MakeFragments();
//			makeFragments.execute();


//			UtilityMethods.getInstance().dismissDialog();


//		if (bundle != null) {
//			catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
//			header = bundle.getString("Header");
//			/*for(int i=0;i<catObj.size();i++)
//			{
//				ProductListFragments.CallAPI callapi=new ProductListFragments().new CallAPI();
//				asyncTasks.add(callapi);
//			}*/
//		}

//		iconHeaderHome = (ImageView)findViewById(R.id.icon_header_home);
//		iconHeaderHome.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				// TODO Auto-generated method stub
//				Intent intent = new Intent(mContext, HomeScreen.class);
//				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//				startActivity(intent);
//				finish();
//			}
//		});

//		tv_bradcrum=(TextView)findViewById(R.id.tv_Bradcrum);

//		ll_brad_crum=(LinearLayout)findViewById(R.id.ll_Bradcrum);
//		ll_brad_crum.setBackgroundColor(getResources().getColor(R.color.breadcrum_color));
//		hscrollview=(HorizontalScrollView)findViewById(R.id.hscrollview);
//		String brade_crum[]=MySharedPrefs.INSTANCE.getBradecrum().split(">>");
//		for(int i=0;i<brade_crum.length;i++)
//		{
//				 addImageView(ll_brad_crum);
//				 addTextView(ll_brad_crum, brade_crum[i]);
//		}

//		hscrollview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//		    @Override
//		    public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//		    	hscrollview.removeOnLayoutChangeListener(this);
//		    	hscrollview.fullScroll(View.FOCUS_RIGHT);
//		    }
//		});

//		ll_brad_crum.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				finish();
//			}
//		});


			///////////////////start/////////////////////
//			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//			ViewPager pager = (ViewPager) findViewById(R.id.pager);
//			pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//			pager.setOffscreenPageLimit(size);
//			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//			indicator.setViewPager(pager);
//			View headerView = findViewById(R.id.header);
//			initHeader(headerView, true, searchString);
//			UtilityMethods.getInstance().dismissDialog();
			/////////////////////end////////////////////////

//			dismissDialog();
//			showSearchView(true);
//			edtSearch.setText(searchString);

//			View headerView = findViewById(R.id.header);
//			initHeader(headerView, true, header.replaceAll("/", " >> "));
//			TextView textView = (TextView) headerView.findViewById(R.id.screenName);
//			textView.setOnClickListener(new View.OnClickListener() {
//
//				@Override
//				public void onClick(View v) {
//					// TODO Auto-generated method stub
//					finish();
//				}
//			});

		}catch(Exception e){
			dismissDialog();
//			UtilityMethods.getInstance().dismissDialog();
			new GrocermaxBaseException("SearchTabs","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

    }


//	final Handler handler = new Handler(){
//		@Override
//		public void handleMessage(Message msg) {
//
//			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//			ViewPager pager = (ViewPager) findViewById(R.id.pager);
//			pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//			pager.setOffscreenPageLimit(size);
//			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//			indicator.setViewPager(pager);
//
//
//			super.handleMessage(msg);
//		}
//	};


//	class MakeFragments extends AsyncTask{
//		@Override
//		protected void onPreExecute() {
//			super.onPreExecute();
//			showDialog();
//		}
//
//		@Override
//		protected Object doInBackground(Object[] params) {
////			FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//			ViewPager pager = (ViewPager) findViewById(R.id.pager);
//			pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//			pager.setOffscreenPageLimit(size);
//			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//			indicator.setViewPager(pager);
//			return null;
//		}
//
//		@Override
//		protected void onPostExecute(Object o) {
//			super.onPostExecute(o);
//			dismissDialog();
//		}
//	}



		class GoogleMusicAdapter extends FragmentPagerAdapter {
//	class GoogleMusicAdapter extends FragmentStatePagerAdapter{

			public GoogleMusicAdapter(FragmentManager fm) {
				super(fm);
			}

			@Override
			public Fragment getItem ( int position){
				try{
//					return SearchProductFragments.newInstance(json[position % json.length]);
					return SearchProductFragments.newInstance(String.valueOf(json[position % json.length]));
				}catch(IllegalStateException e){
					new GrocermaxBaseException("SearchTabs","getItem",e.getMessage(), GrocermaxBaseException.ILLEGALSTATEEXCEPTION,"nodetail");
				}catch(Exception e){
					new GrocermaxBaseException("SearchTabs","getItem",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
				}
				return new Fragment();       //default if error occured
			}

			@Override
			public CharSequence getPageTitle ( int position){
				try {
					if (listCategoryHashMap.get(position % json.length).get(SearchLoader.TAG_CAT_NAME) != null) {
						return listCategoryHashMap.get(position % json.length).get(SearchLoader.TAG_CAT_NAME).toUpperCase();
					} else {
						return listCategoryHashMap.get(position % json.length).get(SearchLoader.TAG_CAT_NAME);
					}
				}catch(IllegalStateException e){
					new GrocermaxBaseException("SearchTabs","getItem",e.getMessage(), GrocermaxBaseException.ILLEGALSTATEEXCEPTION,"nodetail");
				}catch(Exception e){
					new GrocermaxBaseException("SearchTabs","getPageTitle",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
				}
				return "";                      //default if error occured
			}

			@Override
			public int getCount () {
				try {
					return json.length;
				}catch(IllegalStateException e){
					new GrocermaxBaseException("SearchTabs","getItem",e.getMessage(), GrocermaxBaseException.ILLEGALSTATEEXCEPTION,"nodetail");
				}catch(Exception e){
					new GrocermaxBaseException("SearchTabs","getCount",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
				}
				return 0;                                                 //default if error occured
			}

        
    }

//	public void addToCart(String product_id, String quantity) {
//    	showDialog();
//		try {
//			JSONArray products = new JSONArray();
//			JSONObject prod_obj = new JSONObject();
//			prod_obj.put("productid", product_id);
//			prod_obj.put("quantity", quantity);
//			products.put(prod_obj);
//			String url;
//			if(MySharedPrefs.INSTANCE.getQuoteId()==null||MySharedPrefs.INSTANCE.getQuoteId().equals(""))
//			{
//				url = UrlsConstants.ADD_TO_CART_URL
//						+ MySharedPrefs.INSTANCE.getUserId() +"&products="
//						+ URLEncoder.encode(products.toString(), "UTF-8");
//			}
//			else
//			{
//				url = UrlsConstants.ADD_TO_CART_URL
//						+ MySharedPrefs.INSTANCE.getUserId() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//						+ URLEncoder.encode(products.toString(), "UTF-8");
//			}
//
//			myApi.reqAddToCart(url);
//		}catch(NullPointerException e){
//			new GrocermaxBaseException("SearchTabs","addToCart",e.getMessage(), GrocermaxBaseException.NULL_POINTER,"nodetail");
//		}
//		catch (Exception e) {
//			new GrocermaxBaseException("SearchTabs","addToCart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
//		}
//
//	}
	
//	public void addToCartGuest(String product_id, String quantity) {
//    	showDialog();
//		try {
//			JSONArray products = new JSONArray();
//			JSONObject prod_obj = new JSONObject();
//			prod_obj.put("productid", product_id);
//			prod_obj.put("quantity", quantity);
//			products.put(prod_obj);
//			String url;
//
//			url = UrlsConstants.ADD_TO_CART_GUEST_URL+"quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+"&products="
//					+ URLEncoder.encode(products.toString(), "UTF-8");
//			myApi.reqAddToCart(url);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//	}
	
	@Override
	public void OnResponse(Bundle bundle) {
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
				call_bundle.putString("BRAND", contentListBean.getProductDetail().get(0).getProductBrand());
				call_bundle.putString("NAME", contentListBean.getProductDetail().get(0).getProductSingleName());
				call_bundle.putString("GRAMSORML", contentListBean.getProductDetail().get(0).getProductPack());
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
					UtilityMethods.customToast(Constants.ToastConstant.PRODUCT_ADDED_CART, mContext);
					MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
					MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
				} else if(bean.getFlag().equalsIgnoreCase("0")){
					UtilityMethods.customToast(bean.getResult(), mContext);
				}else {
					UtilityMethods.customToast(Constants.ToastConstant.ERROR_MSG, mContext);
				}
			}
		}
		} catch (Exception e) {
			new GrocermaxBaseException("SearchTabs","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	Runnable runningThread = new Runnable() {
		public void run() {
//			showDialog();
			try {
				// change UI elements here
//			FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());


//				FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//				ViewPager pager = (ViewPager) findViewById(R.id.pager);
//				pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//
//				TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//				if(pager!=null)
//				indicator.setViewPager(pager);
//
//				if (size > 2) {
//					pager.setOffscreenPageLimit(2);
//				} else {
//					pager.setOffscreenPageLimit(size);
//				}

//				dismissDialog();
			}catch(IllegalStateException e){
				new GrocermaxBaseException("SearchTabs","runningThread",e.getMessage(), GrocermaxBaseException.ILLEGALSTATEEXCEPTION,"nodetail");
			}catch(Exception e){
				new GrocermaxBaseException("SearchTabs","runningThread",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
		}
	};
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		try {
			initHeader(findViewById(R.id.header), true, searchString);
			initBottom(findViewById(R.id.footer));
			showSubscriptionPopup();
//			initHeader(findViewById(R.id.header), true, null);
//			showSearchView(true);
//			edtSearch.setText(searchString);
			if (martHeader != null) {
				martHeader.setVisibility(View.GONE);
			}

//			FragmentStatePagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
////			FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//			ViewPager pager = (ViewPager) findViewById(R.id.pager);
//			pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//			pager.setOffscreenPageLimit(size);
//			TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//			indicator.setViewPager(pager);
//			View headerView = findViewById(R.id.header);
//			initHeader(headerView, true, searchString);



		} catch (Exception e) {
			new GrocermaxBaseException("SearchTabs","onResume",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
//		clickStatus=0;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
		try {
			if(HomeScreen.bFromHome) {
				HomeScreen.isFromFragment = false;    //work fine for home
			}else{
				HomeScreen.isFromFragment = true;
			}
		}catch(Exception e){}
	}

	//	@Override
//	public void onBackPressed() {
//		// TODO Auto-generated method stub
//		super.onBackPressed();
//		for(int i=0;i<asyncTasks.size();i++)
//			if(!asyncTasks.get(i).isCancelled())
//			{
//				asyncTasks.get(i).cancel(true);
//				System.out.println(asyncTasks.get(i)+"----"+asyncTasks.get(i).isCancelled());
//			}
//			
//	}
	
//public void addTextView(LinearLayout ll,String value)
//{
//	    TextView tv1=new TextView(this);
//	    LinearLayout .LayoutParams layoutParams1= new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.MATCH_PARENT);
//	    tv1.setLayoutParams(layoutParams1);
//	    tv1.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
//	    tv1.setPadding(30,0,30,0);
//	    tv1.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
////	    tv1.setTextColor(Color.GRAY);
//	    tv1.setTextColor(getResources().getColor(R.color.breadcrum_text_color));
//	    tv1.setTextSize(13);
////	    tv1.setTypeface(null, Typeface.BOLD);
//	    tv1.setText(value);
//	    ll.addView(tv1);
//}

//public void addImageView(LinearLayout ll)
//{
//	ImageView img=new ImageView(this);
//	LinearLayout .LayoutParams layoutParams1= new LinearLayout.LayoutParams(20,20);
//	layoutParams1.gravity=Gravity.CENTER_VERTICAL;
//	
//	img.setLayoutParams(layoutParams1);
////	img.setPadding(10, 0, 10, 0);
//    img.setImageResource(R.drawable.forward_icon);
//    ll.addView(img);
//}
	
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

//		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
//		ViewPager pager = (ViewPager) findViewById(R.id.pager);
//		pager.setAdapter(adapter);
////        pager.setOffscreenPageLimit(catObj.size());
//		pager.setOffscreenPageLimit(size);
//		TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
//		indicator.setViewPager(pager);
//		View headerView = findViewById(R.id.header);
//		initHeader(headerView, true, searchString);
//		UtilityMethods.getInstance().dismissDialog();

//		EasyTracker.getInstance(this).activityStart(this);
////    	tracker.activityStart(this);
//    	FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
//    	FlurryAgent.onPageView();         //Use onPageView to report page view count.
//		EasyTracker.getInstance(this).activityStart(this);
		FlurryAgent.onStartSession(this,getResources().getString(R.string.flurry_api_key));
		FlurryAgent.onPageView();         //Use onPageView to report page view count.
	}catch(Exception e){}
	/*screen tracking using rocq*/
	try {
		RocqAnalytics.initialize(this);
		RocqAnalytics.startScreen(this);
	} catch (Exception e) {
		e.printStackTrace();
	}
       /*------------------------------*/
}

@Override
protected void onStop() {
	// TODO Auto-generated method stub
	super.onStop();
	try{
//		EasyTracker.getInstance(this).activityStop(this);
		FlurryAgent.onEndSession(this);
	}catch(Exception e){}
	try {
		RocqAnalytics.stopScreen(this);
	} catch (Exception e) {
		e.printStackTrace();
	}
}
	
}

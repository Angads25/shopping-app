package com.rgretail.grocermax;

import android.content.Context;
import android.content.Intent;
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

import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.flurry.android.FlurryAgent;
import com.melnykov.fab.FloatingActionButton;
import com.rgretail.grocermax.api.ConnectionService;
import com.rgretail.grocermax.api.MyReceiverActions;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.bean.CategoriesProducts;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductDetailsListBean;
import com.rgretail.grocermax.bean.Simple;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.Constants;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;
import com.viewpagerindicator.TabPageIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

//import com.google.analytics.tracking.android.EasyTracker;

public class CategoryTabs extends BaseActivity {
    //private String header;
    //private ArrayList<CategorySubcategoryBean> catObj;
    public Product product;
    TextView tv_bradcrum;
    LinearLayout ll_brad_crum;
    HorizontalScrollView hscrollview;
    ImageView iconHeaderHome;
    Context mContext = this;
    String strHeader;
    public static int clickStatus = 0;
    public static ArrayList<ProductListFragments.CallAPI> asyncTasks = new ArrayList<ProductListFragments.CallAPI>();
    //	EasyTracker tracker;
    String selectedCatId;
    private ArrayList<CategoriesProducts> alCategory;
    private boolean isFromDrawer;
    public static String SCREENNAME = "CategoryTabs-";
    ProductListFragments productListFragments;
    public static String sort_condition;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.s_category_tabs);

        try{
            AppsFlyerLib.setCurrencyCode("INR");
            AppsFlyerLib.setAppsFlyerKey("XNjhQZD7Yhe2dFs8kL7bpn");     //SDK�Initialization�and�Installation�Event (Minimum� Requirement�for�Tracking)�
            AppsFlyerLib.sendTracking(getApplicationContext());
        }catch(Exception e){}

        try {
            sort_condition="popularity";
            addActionsInFilter(MyReceiverActions.PRODUCT_CONTENT_LIST);
            addActionsInFilter(MyReceiverActions.ADD_TO_CART);
            ProductListFragments p = new ProductListFragments();

            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                isFromDrawer = bundle.getBoolean("isFromDrawer");

                //catObj = (ArrayList<CategorySubcategoryBean>) bundle.getSerializable("Categories");
                //header = bundle.getString("Header");

                Simple responseBean = (Simple) bundle.getSerializable("PRODUCTDATA");
                strHeader = bundle.getString("HEADERNAME");

                alCategory = new ArrayList<CategoriesProducts>();

                List<Product> listAll = new ArrayList<Product>();                      //contain All products

//				alCategory.add(categoriesproduct)

                if (responseBean.getFlag().equalsIgnoreCase("1")) {
                    ArrayList<CategoriesProducts> hotproduct = responseBean.getHotproduct();
                    ArrayList<CategoriesProducts> productList = responseBean.getProductList();

                    if (hotproduct != null) {
                        if (hotproduct.size() > 0) {
                            for (int i = 0; i < hotproduct.size(); i++) {
                                CategoriesProducts categoriesProducts = hotproduct.get(i);
                                if (categoriesProducts.getItems().size() > 0) {
                                    alCategory.add(categoriesProducts);
//									for (int k = 0; k < categoriesProducts.getItems().size(); k++) {
//										Product pro = categoriesProducts.getItems().get(k);
//										listAll.add(categoriesProducts.getItems().get(k));
//									}
                                }
                            }
                        }
                    }

                    if (productList != null) {
                        if (productList.size() > 0) {
                            for (int j = 0; j < productList.size(); j++) {
                                CategoriesProducts categoriesProducts = productList.get(j);
                                if (categoriesProducts.getItems().size() > 0) {
                                    alCategory.add(categoriesProducts);
                                    for (int k = 0; k < categoriesProducts.getItems().size(); k++) {
                                        listAll.add(categoriesProducts.getItems().get(k));
                                    }
                                }
                            }

                        }
                    }


///////////    responsible for All tab in starting   ////////////////////
//                    if (listAll.size() > 0) {
//                        CategoriesProducts categoriesProducts = new CategoriesProducts();
//                        categoriesProducts.setCategory_id(null);
//                        categoriesProducts.setCategory_name("All");
//                        categoriesProducts.setItems(listAll);
//                        categoriesProducts.setFlag(null);
//                        categoriesProducts.setMobile(null);
//                        categoriesProducts.setResult(null);
//                        categoriesProducts.setTotalItem(0);
//                        alCategory.add(0, categoriesProducts);
//                    }
///////////    responsible for All tab in starting   ////////////////////


//				Simple responseBean = (Simple) bundle.getSerializable(ConnectionService.RESPONSE);
//				ArrayList<CategoriesProducts> hotproduct = responseBean.getHotproduct();
//				ArrayList<CategoriesProducts> productList = responseBean.getProductList();
//				if(hotproduct.size() > 0 && productList.size() > 0){
//					for(int i=0;i<hotproduct.size();i++){
//						CategoriesProducts categoriesProducts = hotproduct.get(0);
//						String strName = hotproduct.get(0).getCategory_name();
//						List<Product> products = categoriesProducts.getItems();
//						System.out.println(strName+"=="+i+"==HOT NAME=="+product.getName());
//					}
//					for(int j=0;j<productList.size();j++){
//						CategoriesProducts categoriesProducts = productList.get(0);
//						List<Product> products = categoriesProducts.getItems();
//						String strName = hotproduct.get(0).getCategory_name();
//						System.out.println(strName+"=="+j+"==PRODUCT NAME=="+product.getName());
//					}
                }


//				ArrayList<CategoriesProducts> hotproduct = responseBean.getHotproduct();
//				PRODUCTDATA

			/*for(int i=0;i<catObj.size();i++)
            {
				ProductListFragments.CallAPI callapi=new ProductListFragments().new CallAPI();
				asyncTasks.add(callapi);
			}*/


                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Intent intent = new Intent(mContext, ProductSorting.class);
                        startActivityForResult(intent,0);
                    }
                });


            }

            iconHeaderHome = (ImageView) findViewById(R.id.icon_header_home);
            iconHeaderHome.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub

                    Intent intent = new Intent(mContext, HomeScreen.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();

//                    Intent intent = new Intent(mContext, HomeScreen.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
//                    finish();
                }
            });


            LinearLayout llBreadcrumb = (LinearLayout) findViewById(R.id.llbreadcrum);
            llBreadcrumb.setVisibility(View.GONE);

            tv_bradcrum = (TextView) findViewById(R.id.tv_Bradcrum);

            ll_brad_crum = (LinearLayout) findViewById(R.id.ll_Bradcrum);
            ll_brad_crum.setBackgroundColor(getResources().getColor(R.color.breadcrum_color));

//			hscrollview = (HorizontalScrollView) findViewById(R.id.hscrollview);


//			if (MySharedPrefs.INSTANCE.getBradecrum() != null) {
//				String brade_crum[] = MySharedPrefs.INSTANCE.getBradecrum().split(">>");
//				for (int i = 0; i < brade_crum.length; i++) {
//					addImageView(ll_brad_crum);
//					addTextView(ll_brad_crum, brade_crum[i]);
//				}
//			}

//			hscrollview.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
//				@Override
//				public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
//					hscrollview.removeOnLayoutChangeListener(this);
//					hscrollview.fullScroll(View.FOCUS_RIGHT);
//				}
//			});

            ll_brad_crum.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());

            ViewPager pager = (ViewPager) findViewById(R.id.pager);
            pager.setAdapter(adapter);
//			pager.setOffscreenPageLimit(catObj.size());
//			pager.setOffscreenPageLimit(alCategory.size());
            pager.setOffscreenPageLimit(0);
           // pager.setCurrentItem(2);

            final TabPageIndicator indicator = (TabPageIndicator) findViewById(R.id.indicator);
            if (pager != null)
                indicator.setViewPager(pager);

            /*for landing on specific tab*/
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

            View headerView = findViewById(R.id.header);

            initHeader(headerView, true, strHeader);

            indicator.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    //indicator.getChildAt(position).getT
                    try{
                        String catName=alCategory.get(position % alCategory.size()).getCategory_name();
                        UtilityMethods.clickCapture(activity,"L4","",catName,"",MySharedPrefs.INSTANCE.getSelectedCity());
                        RocqAnalytics.trackEvent("L4", new ActionProperties("Category", "L4", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label",catName));
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                @Override
                public void onPageScrollStateChanged(int state) {
                }
            });
//			initHeader(headerView, true, header.replaceAll("/", " >> "));
//			if (MySharedPrefs.INSTANCE.getBradecrum() != null) {
//				String brade_crum[] = MySharedPrefs.INSTANCE.getBradecrum().split(">>");
//				initHeader(headerView, true, brade_crum[brade_crum.length-1]);
//			}else{
//				initHeader(headerView, true, null);
//			}


//			TextView textView = (TextView) headerView.findViewById(R.id.screenName);
//			TextView textView = (TextView) headerView.findViewById(R.id.tv_appbar_breadcrumb);

//			ImageView ivBack = (ImageView) headerView.findViewById(R.id.icon_header_back);

//			if (MySharedPrefs.INSTANCE.getBradecrum() != null) {
//				String brade_crum[] = MySharedPrefs.INSTANCE.getBradecrum().split(">>");
//				if (textView != null) {
//					textView.setText(brade_crum[0]);
//				}
//			}

//			textView.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					finish();
//				}
//			});
//			ivBack.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View arg0) {
//					finish();
//				}
//			});

        } catch (Exception e) {
            e.printStackTrace();
            new GrocermaxBaseException("CategoryTabs", "onCreate", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
        }


//	}
//
//        ViewPager pager = (ViewPager)findViewById(R.id.pager);
//        pager.setAdapter(adapter);
//        pager.setOffscreenPageLimit(catObj.size());
//
//        TabPageIndicator indicator = (TabPageIndicator)findViewById(R.id.indicator);
//        indicator.setViewPager(pager);
//
//        View headerView = findViewById(R.id.header);
//		if(headerView !=null)
//        	initHeader(headerView, true, header.replaceAll("/", " >> "));
//
//        TextView textView = (TextView)headerView.findViewById(R.id.screenName);
//
//        textView.setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View arg0) {
//				finish();
//			}
//		});
    }



    class GoogleMusicAdapter extends FragmentPagerAdapter {

        public GoogleMusicAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            try {
                productListFragments=ProductListFragments.newInstance(alCategory.get(position % alCategory.size()));
                return productListFragments;
                //return ProductListFragments.newInstance(alCategory.get(position % alCategory.size()));
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryTabs", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return new Fragment();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            try {
//            	return catObj.get(position % catObj.size()).getCategory().toUpperCase();
//				String str = alCategory.get(position % alCategory.size()).getCategory_name().toUpperCase();;
//				System.out.println("==Cat value is=="+str);

                return alCategory.get(position % alCategory.size()).getCategory_name().toUpperCase();
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryTabs", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return "";
        }

        @Override
        public int getCount() {
            try {
//          		return catObj.size();
                return alCategory.size();
            } catch (Exception e) {
                new GrocermaxBaseException("CategoryTabs", "GoogleMusicAdapter", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
            }
            return 0;

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
            if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
                url = UrlsConstants.ADD_TO_CART_URL
                        + MySharedPrefs.INSTANCE.getUserId() + "&products="
                        + URLEncoder.encode(products.toString(), "UTF-8");
            } else {
                url = UrlsConstants.ADD_TO_CART_URL
                        + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
                        + URLEncoder.encode(products.toString(), "UTF-8");
            }

            myApi.reqAddToCart(url);
        } catch (NullPointerException e) {
            new GrocermaxBaseException("CategoryTabs", "addToCart", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
        } catch (Exception e) {
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

            url = UrlsConstants.ADD_TO_CART_GUEST_URL + "quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
                    + URLEncoder.encode(products.toString(), "UTF-8");
            myApi.reqAddToCart(url);

        } catch (NullPointerException e) {
            new GrocermaxBaseException("CategoryTabs", "addToCartGuest", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "product_id" + product_id + "quantity" + quantity);
        } catch (Exception e) {
            new GrocermaxBaseException("CategoryTabs", "addToCartGuest", e.getMessage(), GrocermaxBaseException.EXCEPTION, "product_id" + product_id + "quantity" + quantity);
        }
    }

    @Override
    public void OnResponse(Bundle bundle) {
        try {
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
                        UtilityMethods.customToast(Constants.ToastConstant.PRODUCT_ADDED_CART, mContext);
                        MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
                        MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
                    } else if (bean.getFlag().equalsIgnoreCase("0")) {
                        UtilityMethods.customToast(bean.getResult(), mContext);
                    } else {
                        UtilityMethods.customToast(Constants.ToastConstant.ERROR_MSG, mContext);
                    }
                }
            }
        } catch (NullPointerException e) {
            new GrocermaxBaseException("CategoryTabs", "OnResponse", e.getMessage(), GrocermaxBaseException.NULL_POINTER, String.valueOf(bundle));
        } catch (Exception e) {
            new GrocermaxBaseException("CategoryTabs", "OnResponse", e.getMessage(), GrocermaxBaseException.EXCEPTION, String.valueOf(bundle));
        }
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try {
//			if (MySharedPrefs.INSTANCE.getBradecrum() != null) {
//				String brade_crum[] = MySharedPrefs.INSTANCE.getBradecrum().split(">>");
//				initHeader(findViewById(R.id.header), true, brade_crum[brade_crum.length-1]);
//
//			}else{
//				initHeader(findViewById(R.id.header), true, null);
//			}
            initHeader(findViewById(R.id.header), true, strHeader);
            initBottom(findViewById(R.id.footer));
            if (martHeader != null) {
                martHeader.setVisibility(View.GONE);
            }
            clickStatus = 0;
        } catch (Exception e) {
            new GrocermaxBaseException("CategoryTabs", "onResume", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
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
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();
        try {
            HomeScreen.isFromFragment = false;
        }catch(Exception e){}

        isFromCategoryTabs = true;
        try {
            for (int i = 0; i < asyncTasks.size(); i++)
                if (!asyncTasks.get(i).isCancelled()) {
                    asyncTasks.get(i).cancel(true);
                    System.out.println(asyncTasks.get(i) + "----" + asyncTasks.get(i).isCancelled());
                }
        } catch (Exception e) {
            new GrocermaxBaseException("CategoryTabs", "onBackPressed", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
        }
    }

    public void addTextView(LinearLayout ll, String value) {
        TextView tv1 = new TextView(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT);
        tv1.setLayoutParams(layoutParams1);
        tv1.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        tv1.setPadding(30, 0, 30, 0);
        tv1.setTypeface(CustomFonts.getInstance().getRobotoLight(this));
//	    tv1.setTextColor(Color.GRAY);
        tv1.setTextColor(getResources().getColor(R.color.breadcrum_text_color));
        tv1.setTextSize(13);
//	    tv1.setTypeface(null, Typeface.BOLD);
        tv1.setText(value);
        ll.addView(tv1);
    }

    public void addImageView(LinearLayout ll) {
        ImageView img = new ImageView(this);
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(20, 20);
        layoutParams1.gravity = Gravity.CENTER_VERTICAL;

        img.setLayoutParams(layoutParams1);
//	img.setPadding(10, 0, 10, 0);
        img.setImageResource(R.drawable.forward_icon);
        ll.addView(img);
    }

    public int getdp(int a) {
        int paddingPixel = a;
        float density = this.getResources().getDisplayMetrics().density;
        int paddingDp = (int) (paddingPixel * density);
        return paddingDp;
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        try{
            AppsFlyerLib.onActivityResume(this);
        }catch(Exception e){}
        try {
//            EasyTracker.getInstance(this).activityStart(this);
            FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
            FlurryAgent.onPageView();         //Use onPageView to report page view count.
        } catch (Exception e) {
        }
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
            AppsFlyerLib.onActivityPause(this);
        }catch(Exception e){}
        try {
//            EasyTracker.getInstance(this).activityStop(this);
            FlurryAgent.onEndSession(this);
        } catch (Exception e) {
        }
        try {
            RocqAnalytics.stopScreen(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

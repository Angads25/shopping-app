package com.sakshay.grocermax;

import java.net.URLEncoder;
import java.sql.Array;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.analytics.tracking.android.EasyTracker;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sakshay.grocermax.KeyboardStatusDetector.KeyboardVisibilityListener;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyApi;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.api.SearchLoader;
import com.sakshay.grocermax.bean.AddressList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.CartDetailBean;
import com.sakshay.grocermax.bean.OrderHistoryBean;
import com.sakshay.grocermax.bean.ProductListBean;
import com.sakshay.grocermax.bean.UserDetailBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.AppConstants.ToastConstant;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;
//import android.widget.Toast;

public abstract class BaseActivity extends FragmentActivity {

	protected Context mContext = this;
	EditText etSearchBckup;  //when press on search icon and it came you to previous screen.
	public static Activity activity;
	public DisplayImageOptions baseImageoptions;
	private static SearchLoader searchLoader = null;
	ImageView icon_header_back;
//	ImageView icon_header_logo,
	ImageView icon_header_logo_without_search,icon_header_logo_with_search;
	ImageView /*icon_header_cart,*/ icon_header_search;
	ImageButton icon_header_cart;
	public static ImageButton icon_header_user;                     //icon differ in login and logout case.
	public static TextView cart_count_txt;
	private PopupWindow popupMenuOption;
	private LinearLayout llSearchLayout;
	public EditText edtSearch;
	private ImageView imgSearchIcon;
	private ImageView imgSearchCloseIcon;
	private RelativeLayout rlSearchLook;
	private LinearLayout llLeftIcon;
	private LinearLayout llLeftIcon1;
	public View martHeader;
	public TextView tvHeaderName;

	private String search_key;
	public static boolean keyboardVisibility=false;
	EasyTracker tracker;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity = this;
		try {
			etSearchBckup = new EditText(this);
			addActionsInFilter(MyReceiverActions.CHECKOUT);
			addActionsInFilter(MyReceiverActions.USER_DETAILS);
			addActionsInFilter(MyReceiverActions.ORDER_HISTORY);
			addActionsInFilter(MyReceiverActions.VIEW_CART);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART_NEW_PRODUCT);           //add new product to cart when user has already product in cart
			addActionsInFilter(MyReceiverActions.ADDRESS_BOOK);
			addActionsInFilter(MyReceiverActions.ADD_TO_CART_GUEST);

			myApi = new MyApi(mContext);
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	protected void initHeader(View view, boolean showSearch, String name) {
		try {
			KeyboardStatusDetector keyStatus = new KeyboardStatusDetector();
			keyStatus.registerActivity(BaseActivity.this);
			keyStatus.setVisibilityListener(null);
			keyStatus.setVisibilityListener(new KeyboardVisibilityListener() {

				@Override
				public void onVisibilityChanged(boolean keyboardVisible) {
					if (keyboardVisible) {
						System.out.println("Visible");
						keyboardVisibility = true;
					} else {
						System.out.println("Hide");
						keyboardVisibility = false;
					}
				}
			});

//			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//
//			if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
//				if(!keyboardVisibility)
//					imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
//			}else{
//				if(keyboardVisibility)
//					imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
//			}


		    icon_header_back = (ImageView) view.findViewById(R.id.icon_header_back);
			tvHeaderName = (TextView) view.findViewById(R.id.tv_appbar_breadcrumb);
			icon_header_user = (ImageButton) view.findViewById(R.id.icon_header_user);
			icon_header_cart = (ImageButton) view.findViewById(R.id.icon_header_cart);
			cart_count_txt = (TextView) view.findViewById(R.id.nom_producte);
			icon_header_search = (ImageView) view
					.findViewById(R.id.icon_header_search);
			icon_header_logo_without_search = (ImageView) view.findViewById(R.id.icon_header_logo_without_search);
			icon_header_logo_with_search = (ImageView) view.findViewById(R.id.icon_header_logo_with_search);
			llSearchLayout = (LinearLayout) view.findViewById(R.id.llSearchLayout);
			edtSearch = (EditText) view.findViewById(R.id.edtSearch);
			imgSearchIcon = (ImageView) view.findViewById(R.id.imgSearchIcon);
			imgSearchCloseIcon = (ImageView) view
					.findViewById(R.id.imgSearchCloseIcon);
			rlSearchLook = (RelativeLayout) view.findViewById(R.id.rl_serach_look);

			llLeftIcon = (LinearLayout) view.findViewById(R.id.ll_left_icon);

			llLeftIcon1 = (LinearLayout) view.findViewById(R.id.ll_left_icon_1);

			martHeader = (View) view.findViewById(R.id.header_shadow);

			if (MySharedPrefs.INSTANCE.getUserId() != null) {
				//TODO: commented this out as it was superimposing another image which was bigge than params specified,
				//please handle again for login logout
//				BaseActivity.icon_header_user.setEnabled(true);
//				BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon);  //login icon
//				BaseActivity.icon_header_user.setImageResource(R.drawable.profile);  //login icon
				BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_1);  //login icon
			} else {
//				BaseActivity.icon_header_user.setEnabled(true);
//				BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_logout);  //logout icon
				BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_2);  //logout icon
			}

			if (MySharedPrefs.INSTANCE.getTotalItem() != null) {
				cart_count_txt.setText(MySharedPrefs.INSTANCE.getTotalItem());
			} else {
				cart_count_txt.setText("0");
			}

//		TextView screenName = (TextView) view.findViewById(R.id.screenName);
//		if (name != null && name.length() > 0) {
//			screenName.setText(name);
//		} else {
//			screenName.setVisibility(View.GONE);
//		}
			if (name != null && name.length() > 0) {
				if(tvHeaderName != null) {
					tvHeaderName.setText(name);
				}
			} else {
				if(tvHeaderName != null) {
					tvHeaderName.setVisibility(View.GONE);
				}
			}

			if (showSearch) {
				icon_header_search.setOnClickListener(headerClick);
				addActionsInFilter(MyReceiverActions.SEARCH_PRODUCT_LIST);
			} else {
				icon_header_search.setVisibility(View.GONE);
			}


		icon_header_logo_with_search.setOnClickListener(headerClick);
		if(icon_header_logo_without_search != null)
			icon_header_logo_without_search.setOnClickListener(headerClick);
		icon_header_user.setOnClickListener(headerClick);
		icon_header_cart.setOnClickListener(headerClick);
		cart_count_txt.setOnClickListener(headerClick);
		imgSearchIcon.setOnClickListener(headerClick);
		imgSearchCloseIcon.setOnClickListener(headerClick);

		if(icon_header_back != null && tvHeaderName != null) {
			icon_header_back.setOnClickListener(headerClick);
			tvHeaderName.setOnClickListener(headerClick);
		}

		edtSearch.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {

				if (actionId == EditorInfo.IME_ACTION_SEARCH) {
					UtilityMethods.hideKeyboardFromContext(BaseActivity.this);

					goforsearch();
				}
				return false;
				}
			});
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity","initHeader",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
		}



	}



	public void setCartCount()
	{
		cart_count_txt.setText(""+AppConstants.cart_count);
	}

	OnClickListener headerClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			try {
				switch (view.getId()) {
					case R.id.tv_appbar_breadcrumb:
						finish();
						break;
					case R.id.icon_header_back:
						finish();
						break;
					case R.id.icon_header_logo_with_search:
						Intent intent = new Intent(mContext, HomeScreen.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
						break;
					case R.id.icon_header_logo_without_search:
						Intent intent1 = new Intent(mContext, HomeScreen.class);
						intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent1);
						finish();
						break;
					case R.id.icon_header_search:
						showSearchView(true);
//						etSearchBckup = edtSearch;
//						if (UtilityMethods.getCurrentClassName(mContext).equals(getPackageName() + ".SearchTabs")) {
//							finish();
//						}
//						if(etSearchBckup != null) {
//							etSearchBckup.setCursorVisible(true);
//							etSearchBckup.setFocusable(true);
//							etSearchBckup.requestFocus();
//							etSearchBckup.getText().clear();
//						}
//						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//						imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
						break;
					case R.id.icon_header_user:
						if (keyboardVisibility)
							UtilityMethods.hideKeyBoard(BaseActivity.this);
//							showMoreOption(icon_header_user);
						if(UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".CartProductList")) {
								finish();
						 }
							Intent intent2 = new Intent(mContext,UserHeaderProfile.class);
							startActivity(intent2);
						break;
					case R.id.nom_producte:
//				ArrayList<CartDetail> cart_products = UtilityMethods.readCloneCart(BaseActivity.this, Constants.localCloneFile);
//				Toast.makeText(BaseActivity.this, "==total count=="+cart_products.size(), Toast.LENGTH_SHORT).show();
//				SyncCartData();
						goToCart();
						break;
					case R.id.icon_header_cart:
//				SyncCartData();
						goToCart();
						break;
					case R.id.imgSearchIcon:
						// UtilityMethods.hideKeyboard(BaseActivity.this);
						UtilityMethods.hideKeyboardFromContext(BaseActivity.this);
						goforsearch();
						break;
					case R.id.imgSearchCloseIcon:
						showSearchView(false);
						break;
					default:

						break;
				}
			} catch (Exception e) {
				new GrocermaxBaseException("BaseActivity", "initHeader", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
			}
		}
	};

//	Timer timer = null;
//	int count;
//	private void SyncCartData(){
//		timer = null;
//		count = 0;
//		timer = new Timer();
//		showDialog();
//		timer.scheduleAtFixedRate(new TimerTask() {
//
//			@Override
//			public void run() {
//				// TODO Auto-generated method stub
//				runOnUiThread(new Runnable() {
//
//					@Override
//					public void run() {
//						// TODO Auto-generated method stub
//
//						if(count < 60){
//							if(UpdateCartbg.getInstance().bLocally){
//								//no action perform b/c update cart already working in background when go forward or backward.
//							}else{
//								goToCart();
//								if(timer != null){
//									timer.cancel();
//									timer = null;
//								}
//								dismissDialog();
//							}
//						}else{
//							if(timer != null){
//								timer.cancel();
//								timer = null;
//							}
//							dismissDialog();
//						}
//					}
//				});
//			}
//		},1000,1000);
//	}

	public void showSearchView(boolean b) {

		try {

		if (b) {
			cart_count_txt.setVisibility(View.GONE);
			icon_header_user.setVisibility(View.GONE);
			icon_header_cart.setVisibility(View.GONE);
			rlSearchLook.setVisibility(View.VISIBLE);

			llLeftIcon.setVisibility(View.VISIBLE);               //
			llLeftIcon1.setVisibility(View.GONE);               //

			icon_header_search.setVisibility(View.GONE);
			llSearchLayout.setVisibility(View.VISIBLE);
			edtSearch.setCursorVisible(true);
			edtSearch.setFocusable(true);
			edtSearch.requestFocus();

			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
		} else {
			cart_count_txt.setVisibility(View.VISIBLE);
			icon_header_user.setVisibility(View.VISIBLE);
			icon_header_cart.setVisibility(View.VISIBLE);
			rlSearchLook.setVisibility(View.VISIBLE);

			llLeftIcon.setVisibility(View.GONE);                       //
			llLeftIcon1.setVisibility(View.VISIBLE);               //

			icon_header_search.setVisibility(View.VISIBLE);
			llSearchLayout.setVisibility(View.GONE);
			edtSearch.getText().clear();
			InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

			imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);


//				if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//					keyboardVisibility = true;
//					}else{
//						keyboardVisibility = false;
//					}
//					if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
//						if(!keyboardVisibility)
//							imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
//					}else{
//						if(keyboardVisibility)
//							imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
//				}
//				imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);


		}
		}catch(Exception e) {
			new GrocermaxBaseException("BaseActivity", "showSearchView", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
		// UtilityMethods.hideKeyboard(BaseActivity.this);
		//UtilityMethods.hideKeyboardFromContext(BaseActivity.this);

	}

	public void goforsearch() {
		try {

			search_key = edtSearch.getText().toString().trim();
			if (!search_key.equals("")) {
				UtilityMethods.hideKeyBoard(BaseActivity.this);

				if(search_key.length() < 3){
					UtilityMethods.customToast(ToastConstant.APPROPRIATE_QUERY,mContext);
					return;
				}
//				showDialog();
//				if (UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".CartProductList")){
//					finish();
//				}
//				if (UtilityMethods.getCurrentClassName(this).equals(getPackageName() + ".SearchTabs")) {
//					((SearchTabs) this).finish();
//					 finish();
//				}


//			String url = UrlsConstants.SEARCH_PRODUCT + search_key + "&page=1";

				String url = UrlsConstants.SEARCH_PRODUCT + search_key;
//			myApi.reqSearchProductList(url);
				url = url.replace(" ", "%20");
//				new SearchLoader(this,search_key).execute(url);

//				if (UtilityMethods.getCurrentClassName(this).equals(getApplicationContext().getPackageName() + ".SearchTabs")) {
//
//				}

//				if(BaseActivity.searchLoader != null){
//					if(!BaseActivity.searchLoader.isCancelled()){
//						BaseActivity.searchLoader.cancel(true);
//					}
//				}

//				BaseActivity.searchLoader = new SearchLoader(this,search_key);
//				BaseActivity.searchLoader.execute(url);
				SearchLoader searchLoader  = new SearchLoader(this,search_key);
				searchLoader.execute(url);


				Log.i("SEARCH_REQUEST", "URL::" + url);
			} else {
//			Toast.makeText(mContext,ToastConstant.ENTER_TEXT,
//					Toast.LENGTH_LONG).show();
				UtilityMethods.customToast(ToastConstant.ENTER_TEXT, mContext);
			}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "goforsearch", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public void goToCart() {
		try {
		/*if(MySharedPrefs.INSTANCE.getUserId() != null)
		{*/

			if (MySharedPrefs.INSTANCE.getUserId() == null || MySharedPrefs.INSTANCE.getUserId().equals("")) {              //user not login
				ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(BaseActivity.this, Constants.localCartFile);
			/*Intent intent = new Intent(mContext, CartProductList.class);
			Bundle bundle = new Bundle();
			bundle.putParcelableArrayList("cartList", cart_products);
			intent.putExtras(bundle);
			startActivity(intent);*/
				if (cart_products.size() > 0) {
				/*Intent intent = new Intent(mContext, LoginActivity.class);
				startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);*/

					try {
						JSONArray products = new JSONArray();
						for (int i = 0; i < cart_products.size(); i++) {
							JSONObject prod_obj = new JSONObject();
							prod_obj.put("productid", cart_products.get(i).getItem_id());
							prod_obj.put("quantity", cart_products.get(i).getQty());
							products.put(prod_obj);
						}

						String url;
						if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals(""))     //user not login and going to view cart(in this case quote id will generate)
						{
							showDialog();
							System.out.println("without quote json=" + products.toString());
							url = UrlsConstants.ADD_TO_CART_GUEST_URL + "products="
									+ URLEncoder.encode(products.toString(), "UTF-8");
							myApi.reqAddToCartGuest(url);
						} else                                                                                 //user not login and going to view cart for 2nd or more than 2nd time
						{         //using there again guest URL even we already have quote id as it is generated before to deleted all items in cart.
							showDialog();
							System.out.println("without quote json=" + products.toString());
//						url = UrlsConstants.ADD_TO_CART_GUEST_URL+"products="
//								+ URLEncoder.encode(products.toString(), "UTF-8")+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
//						myApi.reqAddToCartGuest(url);

							url = UrlsConstants.ADD_TO_CART_GUEST_URL
									+ MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
									+ URLEncoder.encode(products.toString(), "UTF-8");
							myApi.reqAddToCartGuest(url);

//						showDialog();
//						addToCartNewProduct();

//						showDialog();
//						url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
//						myApi.reqViewCart(url);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else                                                     //user not login,local cart size is 0 and user is going to view cart
				{
					if (cart_count_txt.getText().toString().equals("0"))
						UtilityMethods.customToast(ToastConstant.CART_EMPTY, mContext);
					else {
						showDialog();
						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
						myApi.reqViewCart(url);
					}
				}
			} else {                                                        //user login
				if (MySharedPrefs.INSTANCE.getQuoteId() != null && !(MySharedPrefs.INSTANCE.getQuoteId().equals("")))       //user not login and added products and has viewed cart (so that quite id generated by guest URL)
				{                      //user already login OR going to login.
					showDialog();
					ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(BaseActivity.this, Constants.localCartFile);
					if (cart_products != null && cart_products.size() > 0)      //user is login,and add some products locally,now these products first add and then display cart.
					{
						addToCartNewProduct();
					} else {                                                    //user is login,and going to view his cart
						String url = UrlsConstants.VIEW_CART_URL + MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId();
//						System.out.println("==view cart URL==" + url);
						myApi.reqViewCart(url);
					}
				} else                                       //user is login first time and at login time blank quote id coming from server
				{
					ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(BaseActivity.this, Constants.localCartFile);
					if (cart_products != null && cart_products.size() > 0) {
						showDialog();
						addToCartNewProduct();
					} else {
						UtilityMethods.customToast(ToastConstant.NO_ITEM_CART, mContext);
					}
				}
			}
		/*}
		else
		{
			//Toast.makeText(mContext,ToastConstant.LOGIN_TO_SEE_CART,0).show();
			Intent intent = new Intent(mContext, LoginActivity.class);
			startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
		}*/

		}catch(NullPointerException e){
			new GrocermaxBaseException("BaseActivity", "goToCart", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}
		catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "goToCart", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
		
	}
	
	private void addToCartNewProduct(){

		ArrayList<CartDetail> cart_products = null;
		try {
			cart_products = UtilityMethods.readLocalCart(BaseActivity.this, Constants.localCartFile);
			if (cart_products != null && cart_products.size() >= 0) {

					JSONArray products = new JSONArray();
					for (int i = 0; i < cart_products.size(); i++) {
						JSONObject prod_obj = new JSONObject();
						prod_obj.put("productid", cart_products.get(i).getItem_id());
						prod_obj.put("quantity", cart_products.get(i).getQty());
						products.put(prod_obj);
					}

					String url;
					if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
						System.out.println("without quote json=" + products.toString());
						url = UrlsConstants.ADD_TO_CART_URL
								+ MySharedPrefs.INSTANCE.getUserId() + "&products="
								+ URLEncoder.encode(products.toString(), "UTF-8");
					} else {
						System.out.println("with quote json=" + products.toString());
						url = UrlsConstants.ADD_TO_CART_URL
								+ MySharedPrefs.INSTANCE.getUserId() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
								+ URLEncoder.encode(products.toString(), "UTF-8");
					}
					String urls = url;
					//String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
					myApi.reqAddToCartNewProduct(url);

			}
		}catch(NullPointerException e){
			new GrocermaxBaseException("BaseActivity", "addToCartNewProduct", e.getMessage(), GrocermaxBaseException.NULL_POINTER, String.valueOf(cart_products));
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "addToCartNewProduct", e.getMessage(), GrocermaxBaseException.EXCEPTION, String.valueOf(cart_products));
		}
	}
	
	

	private TextView[] footerButton;

	protected void initFooter(View view, int selected, int disable) {
		try {
//			view.setVisibility(View.GONE);
		/*
		 * footerButton = new TextView[5]; footerButton[0] =
		 * (TextView)view.findViewById(R.id.shop); footerButton[1] =
		 * (TextView)view.findViewById(R.id.basket); footerButton[2] =
		 * (TextView)view.findViewById(R.id.product); footerButton[3] =
		 * (TextView)view.findViewById(R.id.offer); footerButton[4] =
		 * (TextView)view.findViewById(R.id.checkout);
		 * 
		 * setAllFooterEnable();
		 * 
		 * setFooterDisable(disable);
		 * 
		 * setFooterSelected(selected);
		 * 
		 * for (int i = 0; i < footerButton.length; i++) {
		 * footerButton[i].setOnClickListener(footerClick); }
		 */
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "initFooter", String.valueOf(e), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	OnClickListener footerClick = new OnClickListener() {
		@Override
		public void onClick(View view) {
			Intent intent = null;
			try {
				switch (view.getId()) {
					case R.id.shop:
						intent = new Intent(mContext, BrowseActivity.class);
						startActivity(intent);

						break;
					case R.id.basket:
						// setFooterSelected(1);
//					goToCart();
						break;
					case R.id.product:
						setFooterSelected(2);
//				Toast.makeText(mContext, "Base activity footer product",
//						Toast.LENGTH_SHORT).show();
						UtilityMethods.customToast("Base activity footer product", mContext);
						break;
					case R.id.offer:
						setFooterSelected(3);
//				Toast.makeText(mContext, ToastConstant.BASE_ACTIVITY_FOOTER_OTHER,
//						Toast.LENGTH_SHORT).show();
						UtilityMethods.customToast(ToastConstant.BASE_ACTIVITY_FOOTER_OTHER, mContext);
						break;
					case R.id.checkout:
						setFooterSelected(4);
						if (AppConstants.token != "") {
							showDialog();

							String url = UrlsConstants.ACTIVE_ORDER_URL
									+ MySharedPrefs.INSTANCE.getUserEmail();
							myApi.reqActiveOrder(url);
							intent = new Intent(mContext, ChooseAddress.class);
							startActivity(intent);
						}
						break;
					default:
						break;
				}
			}catch(NullPointerException e){
				new GrocermaxBaseException("BaseActivity", "footerClick", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
			}
			catch (Exception e){
				new GrocermaxBaseException("BaseActivity", "footerClick", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
			}
		}
	};

	private void setFooterSelected(int position) {
		try {
			if (footerButton == null) {
				return;
			}
			for (int i = 0; i < footerButton.length; i++) {
				if (i == position) {
					footerButton[i].setSelected(true);
				} else {
					footerButton[i].setSelected(false);
				}
			}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "setFooterSelected", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void setFooterDisable(int position) {
		try{
		if (footerButton == null) {
			return;
		}
		for (int i = 0; i < footerButton.length; i++) {
			if (i == position) {
				footerButton[i].setEnabled(false);
			}
		}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "setFooterDisable", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void setAllFooterEnable() {
		try {
			for (int i = 0; i < footerButton.length; i++) {
				footerButton[i].setEnabled(true);
			}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "setAllFooterEnable", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void showMoreOption(View v) {
		try {
			if (popupMenuOption == null || !popupMenuOption.isShowing()) {
				Resources r = getResources();
				float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
						1, r.getDisplayMetrics());

				Display display = getWindowManager().getDefaultDisplay();
				int width = display.getWidth();
				int height = display.getHeight();
				int wwwidth = width / 4;
				int hhheight = 30;

				View view = getLayoutInflater().inflate(R.layout.user_menu, null);
				popupMenuOption = new PopupWindow(view, LayoutParams.MATCH_PARENT,
						LayoutParams.MATCH_PARENT, true);
				popupMenuOption.setBackgroundDrawable(new BitmapDrawable());
				popupMenuOption.setOutsideTouchable(true);

//			ImageView ivTopArrow = (ImageView) view.findViewById(R.id.iv_ttoopp);
////		    Bitmap bmp=BitmapFactory.decodeResource(getResources(), R.drawable.top_menu_arrow);
//		    int wwwidth=(width/4)+((width/4)/2)+20;

////		    ivTopArrow.setPadding(wwwidth, 0,0,0);
//		    LinearLayout.LayoutParams lastTxtParams = new LinearLayout.LayoutParams(400, LinearLayout.LayoutParams.WRAP_CONTENT);
//			lastTxtParams.setMargins(wwwidth, 0, 0, 0);
//			ivTopArrow.setLayoutParams(lastTxtParams);

				ScrollView sv = (ScrollView) view.findViewById(R.id.sv_user_menu);
//		    sv.setPadding(wwwidth,0,0,0);
				LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width / 2, LinearLayout.LayoutParams.WRAP_CONTENT);
				lp.setMargins(wwwidth, 0, 0, 0);
				sv.setLayoutParams(lp);
////		    sv.invalidate();

				ImageView imgLoginArrow = (ImageView) view
						.findViewById(R.id.imgLoginArrow);
				TextView tvUserName = (TextView) view.findViewById(R.id.tvUserName);
				TextView tvSignout = (TextView) view.findViewById(R.id.tvSignOut);
				// TextView my_fav_tv = (TextView)
				// view.findViewById(R.id.my_fav_tv);
				LinearLayout llLogin = (LinearLayout) view
						.findViewById(R.id.llLogin);
				LinearLayout my_fav_lay = (LinearLayout) view
						.findViewById(R.id.my_fav_lay);
				LinearLayout llViewProfile = (LinearLayout) view
						.findViewById(R.id.llViewProfile);

				LinearLayout llEditProfile = (LinearLayout) view
						.findViewById(R.id.llEditProfile);
				LinearLayout llOrderHistory = (LinearLayout) view
						.findViewById(R.id.llOrderHistory);
				LinearLayout llAddressBook = (LinearLayout) view
						.findViewById(R.id.llAddressBook);
				LinearLayout llFeedback = (LinearLayout) view
						.findViewById(R.id.feedback_lay);
				LinearLayout llSupport = (LinearLayout) view
						.findViewById(R.id.llSupport);

				if (MySharedPrefs.INSTANCE.getLoginStatus()) {
					String name = MySharedPrefs.INSTANCE.getUserEmail();
					tvUserName.setText(name);
					imgLoginArrow.setVisibility(view.GONE);
					tvUserName.setTextColor(Color.WHITE);
					// tvUserName.setTextAppearance(this,
					// R.style.headerWhiteOnTeal);

					llLogin.setBackgroundColor(getResources().getColor(
							R.color.app_header));

				} else {
					tvUserName.setText(getString(R.string.Login));
					tvSignout.setVisibility(View.GONE);
					imgLoginArrow.setVisibility(view.VISIBLE);
					llLogin.setBackgroundColor(Color.WHITE);

					tvUserName.setTextAppearance(this, R.style.normal_textsize);

					// my_fav_lay.setVisibility(View.GONE);
				}
				tvUserName.setOnClickListener(menuClick);
				llViewProfile.setOnClickListener(menuClick);
				llEditProfile.setOnClickListener(menuClick);
				llOrderHistory.setOnClickListener(menuClick);
				llAddressBook.setOnClickListener(menuClick);
				my_fav_lay.setOnClickListener(menuClick);
				tvSignout.setOnClickListener(menuClick);
				llFeedback.setOnClickListener(menuClick);
				llSupport.setOnClickListener(menuClick);

				popupMenuOption.setTouchInterceptor(new OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
							popupMenuOption.dismiss();
							return true;
						}
						return false;
					}
				});

				view.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						popupMenuOption.dismiss();

					}
				});

				// svUserMenu.setOnClickListener(new OnClickListener()
				// {
				//
				// @Override
				// public void onClick(View arg0) {
				// // TODO Auto-generated method stub
				// popupMenuOption.dismiss();
				//
				// }
				// });
				popupMenuOption.setAnimationStyle(0);
//			popupMenuOption.showAsDropDown(v, 0, 30);
				popupMenuOption.showAsDropDown(v, 0, 22);
			} else {
				popupMenuOption.dismiss();
			}
		}catch (NullPointerException e){
			new GrocermaxBaseException("BaseActivity", "showMoreOption", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "showMoreOption", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private OnClickListener menuClick = new OnClickListener() {

		@Override
		public void onClick(View v) {
			try {
				String userId = MySharedPrefs.INSTANCE.getUserId();
				switch (v.getId()) {

					case R.id.tvUserName:
						TextView tv = (TextView) v;
						String optionName = tv.getText().toString();
						if (optionName.equalsIgnoreCase(getString(R.string.Login))) {
							if (UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".CartProductList")) {  //return back to CartProductList class
								Intent intent = new Intent(mContext, LoginActivity.class);
								startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
							} else {
								Intent intent = new Intent(mContext, LoginActivity.class);
//					//startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
								startActivityForResult(intent, 555);
							}
						}

//				TextView tv = (TextView) v;
//				String optionName = tv.getText().toString();
//				if (optionName.equalsIgnoreCase(getString(R.string.Login))) {
//					Intent intent = new Intent(mContext, LoginActivity.class);
//					//startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
//					startActivityForResult(intent, 555);
//				}


						// selectItem("Sign In/Register");
						break;

					case R.id.llViewProfile:
						if (!UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".UserProfile")) {
							if (userId != null && userId.trim().length() > 0) {
								showDialog();
								String url = UrlsConstants.USER_DETAIL_URL + userId;
								myApi.reqUserDetails(url);
							} else {
					/*Toast.makeText(mContext, ToastConstant.LOGIN_FIRST,
							Toast.LENGTH_SHORT).show();*/

								Intent intent = new Intent(mContext, LoginActivity.class);
								startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
							}
						}
						break;

					case R.id.llEditProfile:
						if (!UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".EditProfile")) {
							if (userId != null && userId.trim().length() > 0) {
								Intent intent = new Intent(mContext, EditProfile.class);
								startActivity(intent);
							} else {
					/*Toast.makeText(mContext, ToastConstant.LOGIN_FIRST,
							Toast.LENGTH_SHORT).show();*/
								Intent intent = new Intent(mContext, LoginActivity.class);
								startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
							}
						}
						break;

					case R.id.llOrderHistory:
						if (!UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".OrderHistory")) {
							if (userId != null && userId.trim().length() > 0) {
								openOrderHistory();
							} else {
								//Toast.makeText(mContext, ToastConstant.LOGIN_FIRST, Toast.LENGTH_SHORT).show();
								Intent intent = new Intent(mContext, LoginActivity.class);
								startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
							}
						}
						// selectItem("My Orders");
						break;

					case R.id.llAddressBook:
						if (!UtilityMethods.getCurrentClassName(BaseActivity.this).equals(getApplicationContext().getPackageName() + ".AddressDetail")) {
							if (userId != null && userId.trim().length() > 0) {
								showDialog();
								String url = UrlsConstants.ADDRESS_BOOK + userId;
								myApi.reqAddressBook(url, MyReceiverActions.ADDRESS_BOOK);
							} else {
					/*Toast.makeText(mContext, ToastConstant.LOGIN_FIRST,
							Toast.LENGTH_SHORT).show();*/
								Intent intent = new Intent(mContext, LoginActivity.class);
								startActivityForResult(intent, AppConstants.LOGIN_REQUEST_CODE);
							}
						}
						break;

					case R.id.tvSignOut:
						MySharedPrefs.INSTANCE.clearUserInfo();
						MySharedPrefs.INSTANCE.putTotalItem("0");
						cart_count_txt.setText("0");
						BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_2);
						UtilityMethods.deleteCloneCart(BaseActivity.this);

						////Fb logout/////////
						if (MySharedPrefs.INSTANCE.getFacebookId() != null) {
							Session session = getSession();
							if (!session.isClosed()) {
								MySharedPrefs.INSTANCE.clearAllData();
								session.closeAndClearTokenInformation();
							}
						}

						if (MySharedPrefs.INSTANCE.getGoogleId() != null) {
//					LoginActivity loginActivity = new LoginActivity();
//					loginActivity.googlePlusLogoutLocally();
							LoginActivity.googlePlusLogout();
							Registration.googlePlusLogoutReg();
//					loginActivity.googlePlusLogout();
							MySharedPrefs.INSTANCE.clearAllData();
						}

//				Toast.makeText(mContext,ToastConstant.LOGOUT_SUCCESS, Toast.LENGTH_LONG).show();
//				if(MySharedPrefs.INSTANCE.getGoogleId() != null){
//					new LoginActivity().googlePlusLogout();
//				}

						UtilityMethods.customToast(ToastConstant.LOGOUT_SUCCESS, mContext);
						Intent intent = new Intent(mContext, HomeScreen.class);
						intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
						startActivity(intent);
						finish();
						break;
					case R.id.feedback_lay:
						PackageInfo pInfo = null;
						try {
							pInfo = getPackageManager().getPackageInfo(
									getPackageName(), 0);
						} catch (NameNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						if (pInfo != null) {
							String subject = AppConstants.subject;
							shareToGMail(AppConstants.email, subject);
						}
						break;
					case R.id.llSupport:
						Intent callIntent = new Intent(Intent.ACTION_CALL,
								Uri.parse(AppConstants.customer_care));
						startActivity(callIntent);
						break;
					case R.id.my_fav_lay:
						UtilityMethods.shareApp(mContext);
						break;
					default:
						break;
				}
				try {
					if (popupMenuOption != null)
						popupMenuOption.dismiss();
				} catch (Exception e) {
					System.out.println(e.getMessage());
					System.out.println(e.getMessage());
				}
			} catch (Exception e) {
				new GrocermaxBaseException("BaseActivity", "OnClickListener", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
			}
		}
	};
	
	public void openOrderHistory()
	{
		try{
			String email = MySharedPrefs.INSTANCE.getUserEmail();
			showDialog();
			String url = UrlsConstants.ORDER_HISTORY_URL+MySharedPrefs.INSTANCE.getUserEmail();//email;
			myApi.reqOrderHistory(url);
		}catch(NullPointerException e){
			new GrocermaxBaseException("BaseActivity", "openOrderHistory", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "openOrderHistory", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public void shareToGMail(String email, String subject) {
		try{
			Intent emailIntent = new Intent(Intent.ACTION_SEND);
			emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] { email });
			emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
			emailIntent.setType("text/plain");
			final PackageManager pm = this.getPackageManager();
			final List<ResolveInfo> matches = pm.queryIntentActivities(emailIntent,
					0);
			ResolveInfo best = null;
			for (final ResolveInfo info : matches)
				if (info.activityInfo.packageName.endsWith(".gm")
						|| info.activityInfo.name.toLowerCase().contains("gmail"))
					best = info;
			if (best != null)
				emailIntent.setClassName(best.activityInfo.packageName,
						best.activityInfo.name);
			this.startActivity(emailIntent);
		}catch(NullPointerException e){
			new GrocermaxBaseException("BaseActivity", "shareToGMail", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "shareToGMail", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private ProgressDialog mProgressDialog;
	private IntentFilter intentFilter = new IntentFilter();
	protected MyApi myApi;
	private boolean isRegister = false;

	public void showDialog() {
		try {
			mProgressDialog = new ProgressDialog(mContext);
			mProgressDialog.setMessage("Loading...");
			mProgressDialog.show();
			mProgressDialog.setCancelable(false);
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "showDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	public void dismissDialog() {
		try{
		if (mProgressDialog != null && mProgressDialog.isShowing()) {
			mProgressDialog.dismiss();
		}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "dismissDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void onResume() {
		super.onResume();
		try{
			registerReceiver();
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "onResume", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	@Override
	public void onPause() {
		super.onPause();
		try{
			dismissDialog();
			unRegisterReceiver();
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "onPause", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	protected void addActionsInFilter(String action) {
		intentFilter.addAction(action);
	}

	private void registerReceiver() {
		try{
		if (!isRegister) {
			LocalBroadcastManager.getInstance(mContext).registerReceiver(
					receiver, intentFilter);
			isRegister = true;
		}
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "registerReceiver", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void unRegisterReceiver() {
		try {
			if (isRegister) {
				LocalBroadcastManager.getInstance(mContext).unregisterReceiver(
						receiver);
				isRegister = false;
			}
		}
		catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "unRegisterReceiver", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			try{
			dismissDialog();
			Bundle bundle = intent.getBundleExtra(ConnectionService.DATA);
			String errorString = bundle.getString(ConnectionService.ERROR);
			if (errorString == null) {
				if (intent.getAction().equals(MyReceiverActions.USER_DETAILS)) {
					UserDetailBean bean = (UserDetailBean) bundle
							.getSerializable(ConnectionService.RESPONSE);
					Intent i = new Intent(mContext, UserProfile.class);
					i.putExtra("UserDetailBean", bean);
					startActivity(i);
				} else if (intent.getAction().equals(
						MyReceiverActions.ORDER_HISTORY)) {
					OrderHistoryBean bean = (OrderHistoryBean) bundle
							.getSerializable(ConnectionService.RESPONSE);
					Intent i = new Intent(mContext, OrderHistory.class);
					i.putExtra("OrderHistory", bean);
					startActivity(i);
				}
//				else if(intent.getAction().equalsIgnoreCase(MyReceiverActions.VIEW_CART_UPDATE_LOCALLY)){
//					System.out.print("====");
//					BaseResponseBean bean = (BaseResponseBean) bundle
//							.getSerializable(ConnectionService.RESPONSE);
//				}
					
				else if(intent.getAction().equalsIgnoreCase(MyReceiverActions.ADD_TO_CART_NEW_PRODUCT)){
//					if (action.equalsIgnoreCase(MyReceiverActions.ADD_TO_CART)) {
						BaseResponseBean bean = (BaseResponseBean) bundle
								.getSerializable(ConnectionService.RESPONSE);
						if (bean.getFlag().equalsIgnoreCase(Constants.SERVER_SUCCESS)) {
//							Toast.makeText(mContext, ToastConstant.PRODUCT_ADDED_CART,
//									Toast.LENGTH_LONG).show();
							showDialog();

							UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, mContext);
							MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
							MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
							cart_count_txt.setText(String.valueOf(bean.getTotalItem()));
							UtilityMethods.deleteLocalCart(BaseActivity.this);
							
							String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
							myApi.reqViewCart(url);
							
						} else {
//							Toast.makeText(
//									mContext,
//									bean.getResult(),
//									Toast.LENGTH_LONG).show();
							
//							MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
//							MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
//							finish();
							UtilityMethods.customToast(bean.getResult(), mContext);
						}
//					}
				}
				
				else if (intent.getAction().equals(
						MyReceiverActions.VIEW_CART)) {


					cart_count_txt.setText(String.valueOf(MySharedPrefs.INSTANCE.getTotalItem()));               //added latest

 					CartDetailBean cartBean = (CartDetailBean) bundle.getSerializable(ConnectionService.RESPONSE);
					if(cartBean.getItems().size()>0)
					{
						UtilityMethods.deleteLocalCart(BaseActivity.this);                   //new 1/9/2015
						UtilityMethods.deleteCloneCart(BaseActivity.this);
						for(int i=0;i<cartBean.getItems().size();i++)
						{
							UtilityMethods.writeCloneCart(BaseActivity.this, Constants.localCloneFile, cartBean.getItems().get(i));
						}			

						Intent i = new Intent(mContext, CartProductList.class);
						Bundle bundle_cart = new Bundle();
						bundle_cart.putParcelableArrayList("cartList",cartBean.getItems());
						bundle_cart.putSerializable("cartBean", cartBean);
						i.putExtras(bundle_cart);
						startActivity(i);
					}
					else
					{
						UtilityMethods.customToast(ToastConstant.CART_EMPTY, mContext);
					}
				
				
				} else if (intent.getAction().equals(
						MyReceiverActions.ADDRESS_BOOK)) {
					AddressList bean = (AddressList) bundle
							.getSerializable(ConnectionService.RESPONSE);
					Intent i = new Intent(mContext, AddressDetail.class);
					i.putExtra("AddressList", bean);
					startActivity(i);
				} else if (intent.getAction().equals(
						MyReceiverActions.SEARCH_PRODUCT_LIST)) {

					ProductListBean productListBean = (ProductListBean) bundle
							.getSerializable(ConnectionService.RESPONSE);
					if (productListBean == null) {
//						Toast.makeText(mContext,ToastConstant.NO_PRODUCT,
//								Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(ToastConstant.NO_PRODUCT, mContext);
						return;
					}
					edtSearch.getText().clear();
					edtSearch.requestFocus();
					MySharedPrefs.INSTANCE.putIsSearched(true);
					MySharedPrefs.INSTANCE.putSearchKey(search_key.trim());
					MySharedPrefs.INSTANCE.putBradecrum("");

					if (productListBean.getFlag().equalsIgnoreCase("1")) {
						Intent call = new Intent(mContext,
								ProductListScreen.class);
						Bundle call_bundle = new Bundle();
						call_bundle.putSerializable("ProductList",
								productListBean);
						call_bundle.putSerializable("Header", "");
						// call_bundle.putString("cat_id",
						// category.getCategoryId());
						call.putExtras(call_bundle);
						startActivity(call);

					} else {
//						Toast.makeText(mContext, productListBean.getResult(),
//								Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(productListBean.getResult(), mContext);
					}

				} else if(intent.getAction().equals(
						MyReceiverActions.PRODUCT_LIST_FROM_HOME)){
					ProductListBean productListBean = (ProductListBean) bundle
							.getSerializable(ConnectionService.RESPONSE);
					if (productListBean.getFlag().equalsIgnoreCase("1")) {
						MySharedPrefs.INSTANCE.putIsSearched(false);
						Intent call = new Intent(mContext,
								ProductListScreen.class);
						Bundle call_bundle = new Bundle();
						call_bundle.putSerializable("ProductList",
								productListBean);
						call_bundle.putSerializable("Header", "");
						 call_bundle.putString("cat_id",MySharedPrefs.INSTANCE.getCatId());
						call.putExtras(call_bundle);
						startActivity(call);

					} else {
//						Toast.makeText(mContext, productListBean.getResult(),Toast.LENGTH_LONG).show();
						UtilityMethods.customToast(productListBean.getResult(), mContext);
					}
				}else if(intent.getAction().equals(MyReceiverActions.ADD_TO_CART_GUEST)){

					BaseResponseBean bean = (BaseResponseBean) bundle.getSerializable(ConnectionService.RESPONSE);
					if (bean.getFlag().equalsIgnoreCase("1")) {
						cart_count_txt.setText(String.valueOf(bean.getTotalItem()));
						UtilityMethods.customToast(ToastConstant.PRODUCT_ADDED_CART, mContext);
						MySharedPrefs.INSTANCE.putQuoteId(bean.getQuoteId());
						MySharedPrefs.INSTANCE.putTotalItem(String.valueOf(bean.getTotalItem()));
						UtilityMethods.deleteLocalCart(BaseActivity.this);
						showDialog();
						String url = UrlsConstants.VIEW_CART_URL+ MySharedPrefs.INSTANCE.getUserId()+"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId();
						myApi.reqViewCart(url);
					} else if(bean.getFlag().equalsIgnoreCase("0")){
						UtilityMethods.customToast(bean.getResult(), mContext);
					}else {
						UtilityMethods.customToast(ToastConstant.ERROR_MSG, mContext);
					}
				
					
				}else {
					bundle.putString("ACTION", intent.getAction());
					OnResponse(bundle);
				}
			} else {
				if (errorString
						.equalsIgnoreCase(ConnectionService.IO_EXCEPTION))
//					Toast.makeText(mContext, ToastConstant.msgNoInternet,
//							Toast.LENGTH_SHORT).show();
					UtilityMethods.customToast(ToastConstant.msgNoInternet, mContext);
				else
//					Toast.makeText(mContext,ToastConstant.ERROR_MSG,
//							Toast.LENGTH_SHORT).show();
					UtilityMethods.customToast(ToastConstant.ERROR_MSG, mContext);
			}
			}catch(NullPointerException e){
				new GrocermaxBaseException("BaseActivity", "BroadcastReceiver", e.getMessage(), GrocermaxBaseException.NULL_POINTER, "nodetail");
			}catch(Exception e){
				new GrocermaxBaseException("BaseActivity", "BroadcastReceiver", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
			}
		}
	};

	abstract void OnResponse(Bundle bundle);

	public void initImageLoaderM() {
		try {
			baseImageoptions = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.place_holder_icon)
					.showImageForEmptyUri(R.drawable.place_holder_icon)
					.showImageOnFail(R.drawable.place_holder_icon)
					.cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
					.build();

			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
					mContext).threadPriority(Thread.NORM_PRIORITY - 2)
					.denyCacheImageMultipleSizesInMemory()
					.diskCacheFileNameGenerator(new Md5FileNameGenerator())
					.diskCacheSize(5 * 1024 * 1024) // 50 Mb
					.tasksProcessingOrder(QueueProcessingType.LIFO).build();
			ImageLoader.getInstance().init(config);
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "initImageLoaderM", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	
	
	@Override
	protected void onResumeFragments() {
		// TODO Auto-generated method stub
		super.onResumeFragments();
		try {
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		}catch(Exception e){
			new GrocermaxBaseException("BaseActivity", "onResumeFragments", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}
	
	public Session getSession() {
			return Session.openActiveSession(this, false, callback);
	}
	 
	private Session.StatusCallback callback = new Session.StatusCallback() {
	     public void call(Session session, SessionState state,
	              Exception exception) {
	         if (session.isOpened()) {
	             //Do something
	         }
	     }
	};

}

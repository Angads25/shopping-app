package com.sakshay.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flurry.android.FlurryAgent;
import com.google.analytics.tracking.android.EasyTracker;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.LocationListBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.hotoffers.HotOffersActivity;
import com.sakshay.grocermax.hotoffers.fragment.MenuFragment;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends BaseActivity {
    public static LocationListBean locationList = null;
    private ImageView []ivLocation;
    private TextView []tvLocation;
    LinearLayout llLocation;
    private int position = 0;
    TextView tvSelctionLoc;           //previously selected color change to bluish
    ImageView ivSelectionLoc;
    String strFromWhereToCome;                      //used for check whether to come from drawyer or from starting the app.
//    public static String strSelectedCity,strSelectedState,
//            strSelectedStateId,                                          //store id
//            strSelectedStateRegionId;                                   //state id for create new address
//    public static float densityPhone = 0;



//    @Override
//    protected void onStart() {
//        super.onStart();
//        EasyTracker.getInstance(this).activityStart(this);
////	    	tracker.activityStart(this);
//        FlurryAgent.onStartSession(this, getResources().getString(R.string.flurry_api_key));
//        FlurryAgent.onPageView();         //Use onPageView to report page view count.
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{
        setContentView(R.layout.location_screen);
        addActionsInFilter(MyReceiverActions.CATEGORY_LIST);

//        DisplayMetrics metrics = getResources().getDisplayMetrics();
//        float xAxis = metrics.xdpi;
//        float yAxis = metrics.ydpi;

        AppConstants.densityPhone =  getResources().getDisplayMetrics().density;    //0.75 - ldpi  1.0 - mdpi  1.5 - hdpi 2.0 - xhdpi  3.0 - xxhdpi  4.0 - xxxhdpi

            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;

        System.out.println(width+"====xaxis===="+height);
//index - 2.0 - xhdpi
//lenovo - 3.0 - xxhdpi
        llLocation = (LinearLayout)findViewById(R.id.location_main_layout);
        TextView tvSave = (TextView) findViewById(R.id.location_save);
        tvSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(locationList != null){
//                    locationList.getItems().get(position).getApiUrl();
//                    locationList.getItems().get(position).getCityName();
//                    locationList.getItems().get(position).getId()
//                    locationList.getItems().get(position).getStateId();

                    if(strFromWhereToCome.equalsIgnoreCase("fromdrawyer")){
                        if(MenuFragment.txvLocation != null) {
                            MenuFragment.txvLocation.setText(AppConstants.strSelectedCity);
                            MySharedPrefs.INSTANCE.clearQuote();
                        }
                        finish();
                    }else{
                        showDialog();
                        String url = UrlsConstants.CATEGORY_COLLECTION_LISTING_URL;
                        myApi.reqCategorySubCategoryList(url);
                    }
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        LayoutInflater inflater = this.getLayoutInflater();
        if (bundle != null) {
            try {

                locationList = (LocationListBean)bundle.getSerializable("Location");
                strFromWhereToCome = bundle.getString("FromDrawer");

                if(locationList.getFlag().equals("1")){  //success
//                    ImageView iv1 = (ImageView)findViewById(R.id.iv1);
//                    ImageView iv2 = (ImageView)findViewById(R.id.iv2);
//                    ImageView iv3 = (ImageView)findViewById(R.id.iv3);

//                    TextView tv1 = (TextView)findViewById(R.id.tv1);
//                    TextView tv2 = (TextView)findViewById(R.id.tv2);
//                    TextView tv3 = (TextView)findViewById(R.id.tv3);

                    ivSelectionLoc = new ImageView(this);
                    tvSelctionLoc = new TextView(this);
                    ivLocation = new ImageView[locationList.getItems().size()];
                    tvLocation = new TextView[locationList.getItems().size()];

                    if(locationList.getItems().size() > 0){
                        for(int i=0;i<locationList.getItems().size();i++) {
                            View view = inflater.inflate(R.layout.location_items, null);
                            ivLocation[i] = (ImageView) view.findViewById(R.id.iv_location);
                            tvLocation[i] = (TextView) view.findViewById(R.id.tv_location);
                            tvLocation[i].setText(locationList.getItems().get(i).getCityName());

                            AppConstants.strSelectedCity = locationList.getItems().get(0).getCityName();    //selected default city
                            AppConstants.strSelectedState = locationList.getItems().get(0).getStateName();   //selected default state
                            AppConstants.strSelectedStateId = locationList.getItems().get(0).getId();  //selected default id
                            AppConstants.strSelectedStateRegionId = locationList.getItems().get(0).getStateId();  //selected state region id

//                            ivLocation[i].setImageResource(getImageResource(catObj.get(i).getCategory()));

                            if (i != 0) {
                                ivLocation[i].setImageResource(R.drawable.unselect_location);
//                                ivLocation[i].setBackground(getResources().getDrawable(R.drawable.unselect_location));
                            }else{
                                tvSelctionLoc = tvLocation[i];
                                ivSelectionLoc = ivLocation[i];
                                ivLocation[i].setImageResource(R.drawable.select_location);
//                                ivLocation[i].setBackground(getResources().getDrawable(R.drawable.select_location));
//                                tvLocation[i].setTextColor();
                            }

                            view.setTag(i);
                            view.setOnClickListener(listener);
//                            catImageArray[i].setMinimumHeight((linearMainCat[i].getMeasuredHeight())); // trying to make it a square

                            llLocation.addView(view);
                        }
                    }
                }

            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
        }catch(Exception e){
            new GrocermaxBaseException("LocationActivity","onCreate",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                keyboardVisibility = true;
            } else {
                keyboardVisibility = false;
            }

            if (android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                if (!keyboardVisibility)
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

            } else {
                if (keyboardVisibility)
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
            }
            ivLocation[position].setImageResource(R.drawable.unselect_location);
            position = (Integer) view.getTag();

            AppConstants.strSelectedCity = locationList.getItems().get(position).getCityName();           //selected city
            AppConstants.strSelectedState = locationList.getItems().get(position).getStateName();         //selected state
            AppConstants.strSelectedStateId = locationList.getItems().get(position).getId();              //selected state id
            AppConstants.strSelectedStateRegionId = locationList.getItems().get(position).getStateId();   //selected state region id

            ivLocation[position].setImageResource(R.drawable.select_location);
        }catch(Exception e){
            new GrocermaxBaseException("LocationActivity","listener",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
        }
    };

    @Override
    public void OnResponse(Bundle bundle) {
        try{
        dismissDialog();
        String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
		//UtilityMethods.write("response",jsonResponse,SplashScreen.this);
		ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
		if (!jsonResponse.trim().equals("") && category.size() > 0) {
			UtilityMethods.writeCategoryResponse(LocationActivity.this, AppConstants.categoriesFile, jsonResponse);
			Intent call = new Intent(LocationActivity.this, HotOffersActivity.class);
			Bundle call_bundle = new Bundle();
			call_bundle.putSerializable("Categories", category);
			call.putExtras(call_bundle);
			startActivity(call);
			finish();
		} else {
			UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
		}
        }catch(Exception e){
            new GrocermaxBaseException("LocationActivity","onResponse",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

//    @Override
//    protected void onStop() {
//        // TODO Auto-generated method stub
//        super.onStop();
//        try{
//            tracker.activityStop(this);
//            FlurryAgent.onEndSession(this);
//        }catch(Exception e){
//            new GrocermaxBaseException("CreateNewAddress","onStop",e.getMessage(),GrocermaxBaseException.EXCEPTION,"nodetail");
//        }
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

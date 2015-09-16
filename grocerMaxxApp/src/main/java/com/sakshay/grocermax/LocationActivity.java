package com.sakshay.grocermax;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.LocationListBean;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import java.util.ArrayList;
import java.util.List;


public class LocationActivity extends BaseActivity {
    LocationListBean locationList = null;
    private ImageView []ivLocation;
    private TextView []tvLocation;
    LinearLayout llLocation;
    private int position = 0;
    TextView tvSelctionLoc;           //previously selected color change to bluish
    ImageView ivSelectionLoc;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.location_screen);
        addActionsInFilter(MyReceiverActions.CATEGORY_LIST);

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
                    showDialog();
                    String url = UrlsConstants.CATEGORY_COLLECTION_LISTING_URL;
                    myApi.reqCategorySubCategoryList(url);
                }
            }
        });

        Bundle bundle = getIntent().getExtras();
        LayoutInflater inflater = this.getLayoutInflater();
        if (bundle != null) {
            try {
                locationList =  (LocationListBean)bundle.getSerializable("Location");
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

            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                keyboardVisibility = true;
            }else{
                keyboardVisibility = false;
            }

            if(android.os.Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT){
                if(!keyboardVisibility)
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);

            }else{
                if(keyboardVisibility)
                    imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
            }

//            for(int i=0;i<expandableListView.getExpandableListAdapter().getGroupCount();i++)
//            {
//                if(expandableListView.isGroupExpanded(i)){
////					expandableListView.collapseGroupWithAnimation(i);
//                    expandableListView.collapseGroup(i);
//                }
//            }
//            tvSelctionCat.setTextColor(getResources().getColor(R.color.white));         //unselected text color bluish for previously selected in main category
//            catSelectedLL[position].setVisibility(View.INVISIBLE);                     //unselected previous main category selected
//            tvSelctionLoc.setTextColor(getResources().getColor(R.color.white));
//            ivSelectionLoc.setImageResource(R.drawable.unselect_location);
            ivLocation[position].setImageResource(R.drawable.unselect_location);
            position = (Integer) view.getTag();

//            TextView tvSelectedLocation = (TextView) view.findViewById(R.id.tv_location);                           //get view of currently selected main category
//            ImageView ivSelectedLocation = (ImageView) view.findViewById(R.id.iv_location);                           //get view of currently selected main category
//            ivSelectionLoc.setImageResource(R.drawable.select_location);

            ivLocation[position].setImageResource(R.drawable.select_location);



//            cat_name.setTextColor(getResources().getColor(R.color.main_cat_text_selected));              //selected text color white of main category
//            catSelectedLL[position] = (LinearLayout) view.findViewById(R.id.ll_cat_main_selected);
//            catSelectedLL[position].setVisibility(View.VISIBLE);
//            TODO, unselect others now

//			LinearLayout catSelectedLL = (LinearLayout) view.findViewById(R.id.ll_cat_main_selected);
//			catSelectedLL.setVisibility(View.VISIBLE);
//            catSelectedLL[position].setVisibility(View.VISIBLE);                     //select current selected main category

//            categoryTV.setText(catObj.get(position).getCategory());

//			linearMainCat[position].setBackgroundColor(getResources().getColor(R.color.main_cat_selected));

//            first_level=catObj.get(position).getCategory();
//            mAdapter.refreshList(catObj.get(position).getChildren());
//            exAdapter.refreshList(catObj.get(position).getChildren());
//            hideAllImage();
//			arrowImageArray[position].setVisibility(View.VISIBLE);
            //catImageArray[position].setSelected(true);
            //scroll_view.scrollTo(0, 0);

//            tvSelctionCat = cat_name;                         //assign currently selected view to previously selected holder

            //expandableListView.setBackgroundResource(backImage[position]);
//            expandableListView.setCacheColorHint(android.R.color.transparent);
        }
    };

    @Override
    void OnResponse(Bundle bundle) {
        dismissDialog();
        String jsonResponse = (String) bundle.getSerializable(ConnectionService.RESPONSE);
		//UtilityMethods.write("response",jsonResponse,SplashScreen.this);
		ArrayList<CategorySubcategoryBean> category = UtilityMethods.getCategorySubCategory(jsonResponse);
		if (!jsonResponse.trim().equals("") && category.size() > 0) {
			UtilityMethods.writeCategoryResponse(LocationActivity.this, AppConstants.categoriesFile, jsonResponse);
			Intent call = new Intent(LocationActivity.this, HomeScreen.class);
			Bundle call_bundle = new Bundle();
			call_bundle.putSerializable("Categories", category);
			call.putExtras(call_bundle);
			startActivity(call);
			finish();
		} else {
			UtilityMethods.customToast(AppConstants.ToastConstant.DATA_NOT_FOUND, mContext);
		}
    }

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

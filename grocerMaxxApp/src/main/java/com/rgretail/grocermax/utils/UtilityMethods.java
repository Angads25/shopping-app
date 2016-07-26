package com.rgretail.grocermax.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcel;
import android.provider.Settings;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appsflyer.AFInAppEventParameterName;
import com.appsflyer.AppsFlyerLib;
import com.dq.rocq.RocqAnalytics;
import com.dq.rocq.models.ActionProperties;
import com.facebook.AccessToken;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginManager;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.LoginActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.Registration;
import com.rgretail.grocermax.UserHeaderProfile;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.api.SearchLoader;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.CartDetailBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.preference.AlarmService;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.ListSublistConstants.ListConstant;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UtilityMethods {

	public static final String GA_PRICE = "ga_price";
	public static final String GA_CONTENT_TYPE = "ga_content_type";
	public static final String GA_CONTENT_ID = "ga_content_id";
	public static final String GA_CURRENCY = "ga_currency";
	public static final String GA_EVENTNAME = "ga_eventname";

//	private static UtilityMethods instance;
//	public static UtilityMethods getInstance(){
//		if(instance == null){
//			instance = new UtilityMethods();
//		}
//		return instance;
//	}
//	public UtilityMethods(){
//		instance = this;
//	}
//
//
//	public void showDialog(Context context) {
//		try {
//			mProgressDialog = new ProgressDialog(context);
//			mProgressDialog.setMessage("Loading...");
//			mProgressDialog.show();
//			mProgressDialog.setCancelable(false);
//		}catch(Exception e){
//			new GrocermaxBaseException("UtilityMethods", "showDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
//		}
//	}
//	private ProgressDialog mProgressDialog;
//
//
//	public void dismissDialog() {
//		try{
//			if (mProgressDialog != null && mProgressDialog.isShowing()) {
//				mProgressDialog.dismiss();
//			}
//		}catch(Exception e){
//			new GrocermaxBaseException("UtilityMethods", "dismissDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
//		}
//	}

	/**
	 * For check internet COnnectivity
	 */
	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v("APP_ROOT", "Internet Connection Not Present");
			return false;
		}
	}

	/**
	 * For setting text size according to device height and width
	 * */

	public static float calculateHeight(FontMetrics fm) {
		return fm.bottom - fm.top;
	}

	/**
	 * For email validation
	 * 
	 * @param target
	 * @return boolean varible depending up weather email is valid or not
	 */
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	/*
	 * For Encrypting the password
	 */
	public static String getSignature(String password) {
		// String signature = "";
		String newSignature = "";

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes(), 0, password.length());
			byte messageDigest[] = md5.digest();

			// signature = new BigInteger(1, messageDigest).toString(16);

			StringBuilder sb = new StringBuilder();
			for (byte b : messageDigest)
				sb.append(String.format("%02x", b & 0xff));
			newSignature = sb.toString();

		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return newSignature;
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	public static void hideKeyboardFromContext(Context mContext) {
        InputMethodManager inputManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View view = ((Activity) mContext).getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}
    public static AlertDialog alert;
	public static void showSubscriptionPopup(final Context context, String msg,String ok_button_text,String cancel_button_text) {

        if (alert==null || !alert.isShowing()) {
            Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
            Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
            alertDialog.setView(dialogView);
            alertDialog.setCancelable(false);
            alert = alertDialog.create();
            TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
            tv_msg.setTypeface(typeface1);
            tv_msg.setText(msg);

            final EditText edtComment=(EditText)dialogView.findViewById(R.id.edt_otp);
            edtComment.setVisibility(View.VISIBLE);
            edtComment.setHint("Enter email id");

            TextView tv_skip = (TextView) dialogView.findViewById(R.id.tv_skip);
            tv_skip.setTypeface(typeface);
            tv_skip.setVisibility(View.VISIBLE);
            tv_skip.setText(cancel_button_text);
            tv_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                    String current_time = dateFormat.format(new Date());
                    MySharedPrefs.INSTANCE.putSubscriptionPopupCloseTime(current_time);
                    alert.dismiss();
                }
            });

            TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
            tv_update.setTypeface(typeface);
            tv_update.setText(ok_button_text);
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // alert.dismiss();
                    try {
                        String email=edtComment.getText().toString();
                        if(isValidEmail(email)){
                            final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                            String current_time = dateFormat.format(new Date());
                            MySharedPrefs.INSTANCE.putSubscriptionPopupCloseTime(current_time);
                            alert.dismiss();
                            ((BaseActivity)context).subscribeUser(email,getDeviceId(context));

                        }else{
                            customToast("Please enter valid email id",context);
                        }
                    } catch (Exception e){

                    }
                }
            });
            alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            alert.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    final SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
                    String current_time = dateFormat.format(new Date());
                    MySharedPrefs.INSTANCE.putSubscriptionPopupCloseTime(current_time);
                }
            });
            alert.show();
        }
    }

    public static void showExitAppPopup(final Context context) {


            Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
            Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
            alertDialog.setView(dialogView);
            alertDialog.setCancelable(false);
            alert = alertDialog.create();
            TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
            tv_msg.setTypeface(typeface1);
            tv_msg.setText(MySharedPrefs.INSTANCE.getExitAppMessage());

            final EditText edtComment=(EditText)dialogView.findViewById(R.id.edt_otp);
            edtComment.setVisibility(View.GONE);

            TextView tv_skip = (TextView) dialogView.findViewById(R.id.tv_skip);
            tv_skip.setTypeface(typeface);
            tv_skip.setVisibility(View.VISIBLE);
            tv_skip.setText(MySharedPrefs.INSTANCE.getExitAppCancelButtonText());
            tv_skip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

            TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
            tv_update.setTypeface(typeface);
            tv_update.setText(MySharedPrefs.INSTANCE.getExitAppOkButtonText());
            tv_update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // alert.dismiss();
                    try {
                        Intent intent = new Intent(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_HOME);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                        ((Activity)context).finish();
                    } catch (Exception e){

                    }
                }
            });
          //  alert.getWindow().getAttributes().windowAnimations = R.style.DialogTheme;
            alert.show();
    }

	public static void email(String[] recipients, String subject,
			String content, Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.putExtra(Intent.EXTRA_EMAIL, recipients);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		// intent.putExtra(Intent.EXTRA_CC,"ghi");
		intent.setType("text/html");
		context.startActivity(Intent.createChooser(intent, "Send mail"));
	}

	public static void contactUs(Context context) {
		String recepientEmail = "warning1987@gmail.com";
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		String subject = String.format("%s Feedback", context.getResources()
				.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, "Send from my android.");
		intent.setData(Uri.parse("mailto:" + recepientEmail));
		context.startActivity(intent);
	}

    public static void popUpOnDemand(final Context ctx,String response)
    {
        try {
           // response=loadJSONFromAsset(ctx,"notification.json");
            boolean alertShouldShow=true;
            JSONObject alertMessage=new JSONObject(response);
            String message_id=alertMessage.getString("id");
            String message=alertMessage.getString("message");
            String type=alertMessage.getString("type");
            long frequency=Long.parseLong(alertMessage.getString("frequency"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("yy/MM/dd HH:mm:ss");
            String current_time = dateFormat.format(new Date());
            DataHandler db=new DataHandler(ctx);
            db.Open();
            Cursor cr=db.getMessageFrequencyBasedOnMessageId(message_id);
            if(cr!=null && cr.getCount()>0){
               cr.moveToNext();
               String saved_time=cr.getString(cr.getColumnIndex(Constants.DatabaseConstant.C_MSG_TIME));

                Date d1 = null;
                Date d2 = null;
                try {
                    d1 = dateFormat.parse(saved_time);
                    d2 = dateFormat.parse(current_time);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long diff = d2.getTime() - d1.getTime();
                long diffMinuts = diff / (60 * 1000);
                if(frequency>diffMinuts)
                  alertShouldShow=false;
                else
                  alertShouldShow=true;
            }else{
                ContentValues cVal=new ContentValues();
                cVal.put(Constants.DatabaseConstant.C_MSG_ID,message_id);
                cVal.put(Constants.DatabaseConstant.C_MSG_TIME,current_time);
                db.insertDataInTable(Constants.DatabaseConstant.T_MESSAGE_FREQUENCY,cVal);
                alertShouldShow=true;
            }
            db.close();

            if(type.equalsIgnoreCase("logout")){
             if(MySharedPrefs.INSTANCE.getUserId()==null || MySharedPrefs.INSTANCE.getUserId().equals(""))
                 alertShouldShow=false;
            }

            if(alertShouldShow){
                db.Open();
                db.updateTime(message_id,current_time);
                db.close();
                if (type.equalsIgnoreCase("popup")||type.equalsIgnoreCase("logout")) {
                    final JSONArray buttonArray=alertMessage.getJSONArray("action");
                    Typeface typeface=Typeface.createFromAsset(ctx.getAssets(),"Roboto-Regular.ttf");
                    Typeface typeface1=Typeface.createFromAsset(ctx.getAssets(),"Roboto-Light.ttf");
                    AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                    LayoutInflater inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
                    builder.setView(dialogView);
                    builder.setCancelable(false);
                    final AlertDialog alert = builder.create();
                    TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
                    tv_msg.setTypeface(typeface1);
                    tv_msg.setText(message);

                    TextView tv_skip=(TextView)dialogView.findViewById(R.id.tv_skip);
                    tv_skip.setTypeface(typeface);
                    TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
                    tv_update.setTypeface(typeface);

                    if(buttonArray.length()>0 && buttonArray.length()==1){
                        tv_skip.setVisibility(View.GONE);
                        tv_update.setText(buttonArray.getJSONObject(0).getString("text"));
                    }else{
                        tv_skip.setVisibility(View.VISIBLE);
                        tv_update.setText(buttonArray.getJSONObject(0).getString("text"));
                        tv_skip.setText(buttonArray.getJSONObject(1).getString("text"));
                    }

                    tv_update.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupOperations(ctx,buttonArray,0,alert);
                        }
                    });

                    tv_skip.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            popupOperations(ctx,buttonArray,1,alert);
                        }
                    });
                    alert.show();
                } else {
                    UtilityMethods.customToast(message,ctx);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void popupOperations(Context ctx,JSONArray buttonArray,int arrayPosition,AlertDialog alert){
        try {
            alert.dismiss();
            String key=buttonArray.getJSONObject(arrayPosition).getString("key");
            if (buttonArray.getJSONObject(arrayPosition).getString("key")!=null && !buttonArray.getJSONObject(arrayPosition).getString("key").equals("")) {
                if(key.equalsIgnoreCase("upgrade")){
                    alert.dismiss();
                    UtilityMethods.rateApp(ctx);
                }else if(key.equalsIgnoreCase("search")){
                    AppConstants.strPopupData="";
                    alert.dismiss();
                    try {
                        String url = UrlsConstants.SEARCH_PRODUCT + buttonArray.getJSONObject(arrayPosition).getString("page");
                        url = url.replaceAll(" ", "%20");
                        SearchLoader searchLoader  = new SearchLoader(ctx,buttonArray.getJSONObject(arrayPosition).getString("page"),"");
                        searchLoader.execute(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                        new GrocermaxBaseException("UtilityMethods.java", "search through popup", e.getMessage(), GrocermaxBaseException.EXCEPTION, "");
                    }
                }else if(key.equalsIgnoreCase("home")){
                    AppConstants.strPopupData="";
                    alert.dismiss();
                    try {
                        Intent intent = new Intent(ctx, HomeScreen.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        ((Activity)ctx).startActivity(intent);
                        ((Activity)ctx).finish();
                    }catch(Exception e){
                        new GrocermaxBaseException("UtilityMethods.java","home page through popup",e.getMessage(), GrocermaxBaseException.EXCEPTION,"");
                    }
                }else if(key.equalsIgnoreCase("profile")){
                    AppConstants.strPopupData="";
                    alert.dismiss();
                    HomeScreen.isFromFragment=false;
                    Intent intent2 = null;
                    if (MySharedPrefs.INSTANCE.getLoginStatus()) {
                        intent2 = new Intent(ctx, UserHeaderProfile.class);
                    }else{
                        intent2 = new Intent(ctx, LoginActivity.class);
                    }
                    ((Activity)ctx).startActivity(intent2);

                    if(UtilityMethods.getCurrentClassName(ctx).equals(ctx.getPackageName() + ".CartProductList")) {
                        ((Activity)ctx).finish();
                    }
                }else if(key.equalsIgnoreCase("")){
                    AppConstants.strPopupData="";
                    alert.dismiss();
                }else if(key.equalsIgnoreCase("offerbydealtype")){
                    AppConstants.strPopupData="";
                    gotoInternalPages(ctx,key,buttonArray.getJSONObject(arrayPosition).getString("page"));
                }else if(key.equalsIgnoreCase("dealproductlisting")){
                    AppConstants.strPopupData="";
                    gotoInternalPages(ctx,key,buttonArray.getJSONObject(arrayPosition).getString("page"));
                }else if(key.equalsIgnoreCase("productlistall")){
                    AppConstants.strPopupData="";
                    gotoInternalPages(ctx,key,buttonArray.getJSONObject(arrayPosition).getString("page"));
                }else if(key.equalsIgnoreCase("shopbydealtype")){
                    AppConstants.strPopupData="";
                    gotoInternalPages(ctx,key,buttonArray.getJSONObject(arrayPosition).getString("page"));
                }else if(key.equalsIgnoreCase("dealsbydealtype")){
                    AppConstants.strPopupData="";
                    gotoInternalPages(ctx,key,buttonArray.getJSONObject(arrayPosition).getString("page"));
                }else if(key.equalsIgnoreCase("productdetail")){
                    AppConstants.strPopupData="";
                    ((BaseActivity)ctx).showDialog();
                    String url = UrlsConstants.PRODUCT_DETAIL_URL + buttonArray.getJSONObject(arrayPosition).getString("page");
                    ((BaseActivity)ctx).myApi.reqProductDetailFromNotification(url);
                }else if(key.equalsIgnoreCase("viewcart")){
                    AppConstants.strPopupData="";
                    HomeScreen.isFromFragment=false;
                    ((BaseActivity)ctx).viewCart();
                }else if(key.equalsIgnoreCase("logout")){
                    AppConstants.strPopupData="";
                    logout(ctx);
                }

            }else{
                AppConstants.strPopupData="";
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public static void gotoInternalPages(Context ctx,String key,String pageurl){
        Bundle bundle2=new Bundle();
        bundle2.putString("name", key);
        bundle2.putString("linkurl", pageurl);
        bundle2.putBoolean("IS_FROM_NOTIFICATION",true);
        Intent intent = new Intent(ctx, HomeScreen.class);
        intent.putExtras(bundle2);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        ((Activity)ctx).startActivity(intent);
        ((Activity)ctx).finish();
    }

    public static void logout(Context ctx){
        try {
                    /*tracking GA event for logout action*/
            try{
                UtilityMethods.clickCapture((Activity)ctx,"Profile Activity","","Logout","",MySharedPrefs.INSTANCE.getSelectedCity());
                RocqAnalytics.trackEvent("Profile Activity", new ActionProperties("Category", "Profile Activity", "Action", MySharedPrefs.INSTANCE.getSelectedCity(), "Label", "Logout"));
            }catch(Exception e){
                e.printStackTrace();
            }
                    /*--------------------------*/

            MySharedPrefs.INSTANCE.clearUserInfo();
            MySharedPrefs.INSTANCE.putTotalItem("0");
            BaseActivity.cart_count_txt.setText("0");
            BaseActivity.icon_header_user.setImageResource(R.drawable.user_icon_logout);
            UtilityMethods.deleteCloneCart((Activity)ctx);

            ////Fb logout/////////
            if (MySharedPrefs.INSTANCE.getFacebookId() != null) {
                if (AccessToken.getCurrentAccessToken() != null) {

                    String strCity = MySharedPrefs.INSTANCE.getSelectedCity();
                    String strRegionId = MySharedPrefs.INSTANCE.getSelectedStateRegionId();
                    String strState = MySharedPrefs.INSTANCE.getSelectedState();
                    String strStoreId = MySharedPrefs.INSTANCE.getSelectedStoreId();
                    String strStateId = MySharedPrefs.INSTANCE.getSelectedStateId();

                    MySharedPrefs.INSTANCE.clearAllData();

                    MySharedPrefs.INSTANCE.putSelectedCity(strCity);
                    MySharedPrefs.INSTANCE.putSelectedStateRegionId(strRegionId);
                    MySharedPrefs.INSTANCE.putSelectedState(strState);
                    MySharedPrefs.INSTANCE.putSelectedStoreId(strStoreId);
                    MySharedPrefs.INSTANCE.putSelectedStateId(strStateId);


                    LoginManager.getInstance().logOut();
                }
            }
            if (MySharedPrefs.INSTANCE.getGoogleId() != null) {

                LoginActivity.googlePlusLogout();

                Registration.googlePlusLogoutReg();

                String strCity = MySharedPrefs.INSTANCE.getSelectedCity();
                String strRegionId = MySharedPrefs.INSTANCE.getSelectedStateRegionId();
                String strState = MySharedPrefs.INSTANCE.getSelectedState();
                String strStoreId = MySharedPrefs.INSTANCE.getSelectedStoreId();
                String strStateId = MySharedPrefs.INSTANCE.getSelectedStateId();

                MySharedPrefs.INSTANCE.clearAllData();

                MySharedPrefs.INSTANCE.putSelectedCity(strCity);
                MySharedPrefs.INSTANCE.putSelectedStateRegionId(strRegionId);
                MySharedPrefs.INSTANCE.putSelectedState(strState);
                MySharedPrefs.INSTANCE.putSelectedStoreId(strStoreId);
                MySharedPrefs.INSTANCE.putSelectedStateId(strStateId);
            }

            UtilityMethods.customToast(AppConstants.ToastConstant.LOGOUT_SUCCESS, ctx);
            Intent intent = new Intent(ctx, HomeScreen.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            ((Activity)ctx).startActivity(intent);
            ((Activity)ctx).finish();
        }catch(Exception e){
            UtilityMethods.writeErrorInSdCard(ctx,"On Click of Logout----"+e.getMessage());
        }
    }



    public static void downloadPopUpNew(final Context context,String value){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
        Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
        builder.setView(dialogView);
        builder.setCancelable(false);
        final AlertDialog alert = builder.create();
        TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
        tv_msg.setTypeface(typeface1);
        TextView tv_skip=(TextView)dialogView.findViewById(R.id.tv_skip);
        tv_skip.setTypeface(typeface);
        TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
        tv_update.setTypeface(typeface);

        if(value.equals("1")){
            tv_msg.setText("Update the app for a fresher, faster experience!");
            tv_skip.setVisibility(View.GONE);
        }else if(value.equals("2")){
            tv_msg.setText("Update the app for a fresher, faster experience!");
            tv_skip.setVisibility(View.VISIBLE);
        }

        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                UtilityMethods.rateApp(context);
            }
        });

        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });


        alert.show();
    }


    public static void showPopUpForFeedback(Context context,final String rating_count,String msg){
        Typeface typeface=Typeface.createFromAsset(context.getAssets(),"Roboto-Regular.ttf");
        Typeface typeface1=Typeface.createFromAsset(context.getAssets(),"Roboto-Light.ttf");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.update_available_dialog, null);
        alertDialog.setView(dialogView);
        alertDialog.setCancelable(false);
        final AlertDialog alert = alertDialog.create();
        TextView tv_msg=(TextView)dialogView.findViewById(R.id.tv_msg);
        tv_msg.setTypeface(typeface1);
        tv_msg.setText(msg);

        EditText edtComment=(EditText)dialogView.findViewById(R.id.edt_otp);
        edtComment.setVisibility(View.VISIBLE);
        edtComment.setHint("Enter your comment");

        TextView tv_skip = (TextView) dialogView.findViewById(R.id.tv_skip);
        tv_skip.setTypeface(typeface);
        tv_skip.setVisibility(View.VISIBLE);
        tv_skip.setText("No Thanks!");
        tv_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        TextView tv_update=(TextView)dialogView.findViewById(R.id.tv_update);
        tv_update.setTypeface(typeface);
        tv_update.setText("Thanks");
        tv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                try {

                } catch (Exception e){

                }
            }
        });

        alert.show();
    }






	public static void download2DaysPopUp(final Context context){
		AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
		alertDialog.setTitle("We value your feedback.Love the app?");
//		alertDialog.setMessage("You are using old version\n"+"Updated version is available.\nDo you wish to download it?");
		alertDialog.setPositiveButton("Rate us 5 stars now!", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				try
				{
					UtilityMethods.rateApp(context);
				}
				catch (android.content.ActivityNotFoundException anfe){}
			}
		});
		alertDialog.setNegativeButton("Remind Later", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
			}
		});
		alertDialog.show();
	}

	public static void schedulerStart(Context context)
	{
		PendingIntent pendingIntent;
		try
		{
			Intent myIntent = new Intent(context, AlarmService.class);
			pendingIntent = PendingIntent.getService(context, 0, myIntent, 0);
			AlarmManager alarmManager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(System.currentTimeMillis());
			calendar.add(Calendar.SECOND, 50);
//			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 10 * 1, pendingIntent);  //after 10 seconds
//			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);  //after 60 minutes
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60 * 24, pendingIntent);  //after 24 hours
//	        Toast.makeText(context, "Starting an Alarm", Toast.LENGTH_LONG).show();
		}
		catch(Exception e){}
	}

	public static void rateApp(Context context) {
		Intent goToMarket = null;
		goToMarket = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + AppConstants.PKG_NAME));
		context.startActivity(goToMarket);
	}

	public static void shareApp(Context context) {
		String content = "Try out GrocerMax app, order groceries online and get hot offers on hundreds of products sitting at home.";
		String link_val = "https://play.google.com/store/apps/details?id="
				+ AppConstants.PKG_NAME;

		final String body = content + "\n<a href=\"" + link_val + "\">"
				+ "Download Now." + "</a>" + "\n Thanks.";

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Android App");
		shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
		shareIntent.setType("message/rfc822");
		context.startActivity(Intent.createChooser(shareIntent,
				"Invite friends..."));

    // NEED TO RESTRICT THE USER FOR THE MENTIONED APPS

    }

	@SuppressWarnings("unchecked")
	public static ArrayList<CartDetail> readLocalCart(Activity activity,
			String filename) {
		ArrayList<CartDetail> localCart = new ArrayList<CartDetail>();
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					byte[] data = new byte[(int) file.length()];

					int data1 = fin.read(data, 0, data.length);
					Parcel parcel = Parcel.obtain();
					parcel.unmarshall(data, 0, data.length);
					parcel.setDataPosition(0); // Set the position in the parcel
												// back to the beginning
					// so that we can read the stuff out of it
					localCart = parcel.readArrayList(CartDetail.class
							.getClassLoader());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localCart;
	}
	
	
/************* Clone Cart methods ****************/
	
	@SuppressWarnings("unchecked")
	public static ArrayList<CartDetail> readCloneCart(Activity activity,
			String filename) {
		ArrayList<CartDetail> localCart = new ArrayList<CartDetail>();
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					byte[] data = new byte[(int) file.length()];

					int data1 = fin.read(data, 0, data.length);
					Parcel parcel = Parcel.obtain();
					parcel.unmarshall(data, 0, data.length);
					parcel.setDataPosition(0); // Set the position in the parcel
												// back to the beginning
					// so that we can read the stuff out of it
					localCart = parcel.readArrayList(CartDetail.class
							.getClassLoader());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localCart;
	}

	
	public static boolean deleteCloneCartItem(Activity activity, String product_id) {
		boolean delete = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			/*
			 * HashMap<String, Integer> cart_count = readCountCart(activity,
			 * Constants.localCartFileCount); cart_count.remove(key); File file
			 * = new File(dir, Constants.localCartFileCount); delete =
			 * writeHashMapToFile(file, cart_count);
			 * 
			 * if(delete){
			 */ArrayList<CartDetail> cart_products = readLocalCart(activity,
					Constants.localCloneFile);
			ArrayList<CartDetail> updated_cart_products = new ArrayList<CartDetail>();
			for (int i = 0; i < cart_products.size(); i++) {
				// if (cart_products.get(i).getProductId().equals(product_id)) {
				// cart_products.remove(i);
				// }
				if (!cart_products.get(i).getItem_id().equals(product_id)) {
					updated_cart_products.add(cart_products.get(i));
				}
			}
			cart_products = updated_cart_products;
			File file1 = new File(dir, Constants.localCloneFile);
			delete = writeArrayListToFile(file1, cart_products);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return delete;
	}
	
	public static boolean writeCloneCart(Activity activity, String filename,
			CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readCloneCart(activity, filename);
			
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				int flagPos = -1;
				for (int i = 0; i < list.size(); i++) {
					String str = list.get(i).getNoDiscount();
					String str1 = prod.getNoDiscount();
					if (list.get(i).getItem_id().equals(prod.getItem_id())) {
						if(list.get(i).getNoDiscount() != null){
							if (list.get(i).getNoDiscount().equals("1")) {          //not offer                            [0 - means offer AND 1 - means not offer]
								flagPos = i;
								break;
							}
						}else {
//							if (list.get(i).getNoDiscount().equals("1")) {          //not offer                            [0 - means offer AND 1 - means not offer]
								flagPos = i;
								break;
//							}
						}
					}

				}
				if (flagPos != -1) {
					Integer quantity = list.get(flagPos).getQty();
					quantity = quantity + prod.getQty();
					list.get(flagPos).setQty(quantity);

				} else {

					list.add(prod);
				}

			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}
	
	public static void deleteCloneCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, Constants.localCloneFile);
		if (file.exists()) {
			file.delete();
		}

//		File file1 = new File(dir, Constants.localCartFileCount);
//		if (file1.exists()) {
//			file1.delete();
//		}
	}

	
	/************* Clone Cart methods ****************/

	public static boolean writeLocalCart(Activity activity, String filename,
			CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readLocalCart(activity, filename);
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				int flagPos = -1;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getItem_id().equals(prod.getItem_id())) {
						flagPos = i;
						break;
					}

				}
				if (flagPos != -1) {
					Integer quantity = list.get(flagPos).getQty();
					quantity = quantity + prod.getQty();
					list.get(flagPos).setQty(quantity);

				} else {

					list.add(prod);
				}

			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}

	public static boolean writeArrayListToFile(File file,
			ArrayList<?> cart_products) {
		boolean write = false;
		try {
			Parcel parcel = Parcel.obtain();
			FileOutputStream fout = new FileOutputStream(file);
			Log.d("ConnectionService",
					"write items to cache: " + cart_products.size());
			parcel.writeList(cart_products);
			byte[] data = parcel.marshall();
			fout.write(data);
			fout.close();
			write = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return write;
	}

	/*
	 * public static HashMap<String, Integer> readCountCart(Activity activity,
	 * String filename) { boolean write = false; HashMap<String, Integer>
	 * count_cart = null; try{ File mydir = activity.getDir("mydir",
	 * Context.MODE_PRIVATE); String dir = mydir.getAbsolutePath(); File file =
	 * new File(dir, filename); if(file.length() > 0){ FileInputStream f = new
	 * FileInputStream(file); ObjectInputStream s = new ObjectInputStream(f);
	 * count_cart = (HashMap<String, Integer>) s.readObject(); s.close(); } }
	 * catch(Exception e) { e.printStackTrace(); }
	 * 
	 * return count_cart; }
	 * 
	 * public static int writeCountCart(Activity activity, String filename,
	 * String key, String quantity) { int response = -1; HashMap<String,
	 * Integer> count_cart = null; try{ File mydir = activity.getDir("mydir",
	 * Context.MODE_PRIVATE); String dir = mydir.getAbsolutePath(); File file =
	 * new File(dir, filename); if(file.length() > 0) { count_cart =
	 * readCountCart(activity, filename); } if(count_cart == null) count_cart =
	 * new HashMap<String, Integer>(); if(count_cart.containsKey(key)) {
	 * response = 200; count_cart.put(key, count_cart.get(key) +
	 * Integer.parseInt(quantity)); } else { response = 201; count_cart.put(key,
	 * Integer.parseInt(quantity)); } writeHashMapToFile(file, count_cart); }
	 * catch(Exception e) { response = 400; e.printStackTrace(); }
	 * 
	 * return response; }
	 * 
	 * public static boolean writeHashMapToFile(File file, HashMap<?, ?>
	 * count_cart) { boolean write = false; try{ FileOutputStream f = new
	 * FileOutputStream(file); ObjectOutputStream s = new ObjectOutputStream(f);
	 * s.writeObject(count_cart); s.close(); write = true; } catch(Exception e)
	 * { e.printStackTrace(); } return write; }
	 */

	public static boolean deleteCartItem(Activity activity, String product_id) {
		boolean delete = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			/*
			 * HashMap<String, Integer> cart_count = readCountCart(activity,
			 * AppConstants.localCartFileCount); cart_count.remove(key); File file

			 * = new File(dir, AppConstants.localCartFileCount); delete =
			 * writeHashMapToFile(file, cart_count);
			 * 
			 * if(delete){
			 */ArrayList<CartDetail> cart_products = readLocalCart(activity,
					AppConstants.localCartFile);
			ArrayList<CartDetail> updated_cart_products = new ArrayList<CartDetail>();
			for (int i = 0; i < cart_products.size(); i++) {
				// if (cart_products.get(i).getProductId().equals(product_id)) {
				// cart_products.remove(i);
				// }
				if (!cart_products.get(i).getItem_id().equals(product_id)) {
					updated_cart_products.add(cart_products.get(i));
				}
			}
			cart_products = updated_cart_products;
			File file1 = new File(dir, AppConstants.localCartFile);
			delete = writeArrayListToFile(file1, cart_products);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return delete;
	}

	public static boolean writeServerCart(Activity activity, String filename,
			CartDetailBean cart) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				file.delete();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(cart);
			os.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}

	public static CartDetailBean readServerCart(Activity activity,
			String filename) {
		CartDetailBean cart = null;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					ObjectInputStream is = new ObjectInputStream(fin);
					cart = (CartDetailBean) is.readObject();
					is.close();
					fin.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cart;
	}

	public static void deleteLocalCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, AppConstants.localCartFile);
		if (file.exists()) {
			file.delete();
		}

		File file1 = new File(dir, AppConstants.localCartFileCount);
        if (file1.exists()) {
            file1.delete();
		}
	}

	public static void deleteServerCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, AppConstants.serverCartFile);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static boolean writeCategoryResponse(Activity activity, String filename, String categories)
	{
		boolean write = false;

		File dir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String directory = dir.getAbsolutePath();
		
//		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File logFile = new File(directory, filename);
		if (logFile.exists()) {
			logFile.delete();
		}
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.write(categories);
			buf.newLine();
			buf.close();
			// Constants.index++;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}
	
		
		return write;
	}
	
	public static String readCategoryResponse(Activity activity, String filename)
	{
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, filename);
		if (file.exists()) {
			int size = (int) file.length();
			byte[] bytes = new byte[size];
			BufferedInputStream buf = null;
			try {
				buf = new BufferedInputStream(new FileInputStream(file));
				buf.read(bytes, 0, bytes.length);
				return new String(bytes, "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					buf.close();
				} catch (Exception e2) {
				}
			}

		}
		return "nofile";
	}
	
	public static ArrayList<CategorySubcategoryBean> getCategorySubCategory(String content) {
		ArrayList<CategorySubcategoryBean> category = null;
		try {
			category = new ArrayList<CategorySubcategoryBean>();
			CategorySubcategoryBean categoryOb;
			JSONObject jsonObject = new JSONObject(content.trim());

			try {
				if(jsonObject.getString("urlImg") != null) {
					Constants.base_url_category_image = jsonObject.optString("urlImg");
				}
			}catch(Exception e){
                e.printStackTrace();
            }

			jsonObject=jsonObject.getJSONObject(ListConstant.TAG_CATEGORY).getJSONArray("children").getJSONObject(0);


			JSONArray jsonMainArray = jsonObject.getJSONArray(ListConstant.TAG_CHILDREN);
			for (int i = 0; i < jsonMainArray.length(); i++) {
				JSONObject jsonName = jsonMainArray.getJSONObject(i);
				categoryOb = new CategorySubcategoryBean();

				categoryOb.setCategory(""
						+ jsonName.getString(ListConstant.TAG_NAME));
				categoryOb.setCategoryId(""
						+ jsonName.getString(ListConstant.TAG_CATEGORYID));
				categoryOb.setBreadcrumb(""
						+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
				
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
					categorySubOb.setIsActive(""
							+ jsonName.getString(ListConstant.TAG_IS_ACTIVE));
					categorySubOb.setCategoryId(""
							+ jsonName.getString(ListConstant.TAG_CATEGORYID));
					categorySubOb.setBreadcrumb(""
							+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
					
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
						categorySubSubOb.setCategoryId(""
										+ jsonName.getString(ListConstant.TAG_CATEGORYID));
						categorySubSubOb.setBreadcrumb(""
								+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
						
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
							categorySubSubSubOb.setCategory(""
											+ jsonName.getString(ListConstant.TAG_NAME));
							categorySubSubSubOb.setCategoryId(""
											+ jsonName.getString(ListConstant.TAG_CATEGORYID));
							categorySubSubSubOb.setBreadcrumb(""
									+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
							
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
								categorySubSubSubSubOb.setCategory(""
												+ jsonName.getString(ListConstant.TAG_NAME));
								categorySubSubSubSubOb.setCategoryId(""
												+ jsonName.getString(ListConstant.TAG_CATEGORYID));
								categorySubSubSubSubOb.setBreadcrumb(""
										+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
								
								categorySubSubSubOb.addCategory(categorySubSubSubSubOb);
							}

							categorySubSubOb.addCategory(categorySubSubSubOb);
						}
						categorySubOb.addCategory(categorySubSubOb);
					}
					categoryOb.addCategory(categorySubOb);

				}
				category.add(categoryOb);
			}
		} catch (Exception e) {
            e.printStackTrace();
        }
		return category;
	}
	
	
	
	//////////////////////Writing JSON data in external File/////////////////
	 public static void write(String fileName, String data,Context context) {
		 Writer writer;
		    File root = Environment.getExternalStorageDirectory();
		    File outDir = new File(root.getAbsolutePath() + File.separator + "EZ_time_tracker");
		    if (!outDir.isDirectory()) {
		      outDir.mkdir();
		    }
		    try {
		      if (!outDir.isDirectory()) {
		        throw new IOException(
		            "Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
		      }
		      File outputFile = new File(outDir, fileName);
		      writer = new BufferedWriter(new FileWriter(outputFile));
		      writer.write(data);
		     /* Toast.makeText(context.getApplicationContext(),
		          "Report successfully saved to: " + outputFile.getAbsolutePath(),
		          Toast.LENGTH_LONG).show();*/
		      writer.close();
		    } catch (IOException e) {
		      Log.w("eztt", e.getMessage(), e);
		      Toast.makeText(context, e.getMessage() + Constants.ToastConstant.UNABLE_TO_WRITE,
		          Toast.LENGTH_LONG).show();
		    }

		  }
	 
	 public int dpToPx(int dp, Context context) {
         DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
		    return px;
		}

		public int pxToDp(int px,Context context) {
		    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		    return dp;
		}


	public static void hideKeyBoard(Context con)
	 {
		 if(BaseActivity.keyboardVisibility)
			{
				InputMethodManager imm = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
			}
	 }	 
	 public static void customToast(String strMsg,Context context)
	 {
         TextView textview = new TextView(context);
         textview.setText(strMsg);
         textview.setGravity(1);
         textview.setBackgroundColor(Color.WHITE);
         textview.setTextColor(Color.WHITE);
         textview.setPadding(10, 10, 10, 10);
         Toast toast = new Toast(context);
         toast.setView(textview);

//         toast.getView().setBackgroundDrawable(new ColorDrawable(0xff123456));
		 toast.getView().setBackgroundColor(Color.parseColor("#ee2d09"));
//		 android:background="@color/primaryColor"
         toast.setDuration(Toast.LENGTH_SHORT);
         toast.setGravity(Gravity.BOTTOM|Gravity.CENTER|Gravity.FILL_HORIZONTAL, 0, 0);
         toast.show();
	 }

    public static void customToastLong(String strMsg,Context context)
    {
        TextView textview = new TextView(context);
        textview.setText(strMsg);
        textview.setGravity(1);
        textview.setBackgroundColor(Color.WHITE);
        textview.setTextColor(Color.WHITE);
        textview.setPadding(10, 10, 10, 10);
        Toast toast = new Toast(context);
        toast.setView(textview);

//         toast.getView().setBackgroundDrawable(new ColorDrawable(0xff123456));
        toast.getView().setBackgroundColor(Color.parseColor("#ee2d09"));
//		 android:background="@color/primaryColor"
        toast.setDuration(6000);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER|Gravity.FILL_HORIZONTAL, 0, 0);
        toast.show();
    }


	 
	 public static String getCurrentClassName(Context con)
	 {
		 ActivityManager am = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
//		    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		 ActivityManager mActivityManager =(ActivityManager) BaseActivity.mContext.getSystemService(Context.ACTIVITY_SERVICE);
		 String asa = con.getApplicationContext().getPackageName();
		 String mPackageName = "";
		 if(Build.VERSION.SDK_INT > 20){
			 mPackageName = mActivityManager.getRunningAppProcesses().get(0).getClass().getName();
		 }
		 else{
			 mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		 }
		    /*Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
		    ComponentName componentInfo = taskInfo.get(0).topActivity;
		    componentInfo.getPackageName();*/

//		    return taskInfo.get(0).topActivity.getClassName();
		    return mPackageName;
	 }

    public static String getTopClassName(Context context){
        Activity activity = (Activity) context;//casting context into activity
        Class<?> currentClassName;
        try
        {
            currentClassName = Class.forName(activity.getClass().getName()); //getting the current activity's class name from activity object
            return currentClassName.getName();
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
        return "";
    }


	 
	 public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		    int targetWidth = 50;
		    int targetHeight = 50;
		    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		                        targetHeight,Bitmap.Config.ARGB_8888);

		    Canvas canvas = new Canvas(targetBitmap);
		    Path path = new Path();
		    path.addCircle(((float) targetWidth - 1) / 2,
					((float) targetHeight - 1) / 2,
					(Math.min(((float) targetWidth),
							((float) targetHeight)) / 2),
					Path.Direction.CCW);

		    canvas.clipPath(path);
		    Bitmap sourceBitmap = scaleBitmapImage;
		    canvas.drawBitmap(sourceBitmap,
					new Rect(0, 0, sourceBitmap.getWidth(),
							sourceBitmap.getHeight()),
					new Rect(0, 0, targetWidth, targetHeight), null);
		    return targetBitmap;
		}

	public static boolean writeStackCart(Activity activity, String filename,
										 CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readLocalCart(activity, filename);
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				list.add(prod);
			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return write;

	}

	public static void deleteStackCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, Constants.StackCartFile);
		if (file.exists()) {
			file.delete();
		}
	}






	public static void clickCapture(Context context,String strPrice,String strContentType,String strContentId,String strName,String strEventName){
		//		5 /Example 2: ​Purchase Event/

        /*For AppsFlyer event tracking*/
        try {
			Map<String, Object> eventValue = new HashMap<String, Object>();
			eventValue.put(AFInAppEventParameterName.PRICE, strPrice);
			eventValue.put(AFInAppEventParameterName.CONTENT_TYPE, strContentType);
			eventValue.put(AFInAppEventParameterName.CONTENT_ID, strContentId);
			eventValue.put("NAME", strName);
			eventValue.put(AFInAppEventParameterName.CURRENCY, "INR");
			;
			AppsFlyerLib.trackEvent(context, strEventName, eventValue);
		}catch(Exception e){}


        /*For google event tracking*/
        try {
			if(BaseActivity.mTracker != null) {
                if(MySharedPrefs.INSTANCE.getUserId()!=null){
                    System.out.println("userId = " + MySharedPrefs.INSTANCE.getUserId());
                    BaseActivity.mTracker.set("&uid", MySharedPrefs.INSTANCE.getUserId());
                }
				BaseActivity.mTracker.send(new HitBuilders.EventBuilder()
						.setCategory(strPrice)  //2nd parameter - price
						.setAction(strEventName)    //last parameter
						.setLabel(strContentId)    //3rd parameter - id
						.build());
			}
		}catch(Exception e){
			e.getMessage();
		}

        /*Event tracking using facebook*/
        try {
            System.out.println("FB event tracking entered");
            AppEventsLogger logger = AppEventsLogger.newLogger(context);
            Bundle parameters = new Bundle();
            parameters.putString("Category",strPrice);
            parameters.putString("Action",strEventName);
            parameters.putString("Label",strContentId);
            logger.logEvent(strPrice,parameters);
            System.out.println("FB event tracking executed");
        }catch(Exception e){
            System.out.println("FB event tracking = " + e.getMessage());
        }
	}


    public static void sendGTMEvent(Context context,String action,String label,String category){
         /*For google Gtm Tracking*/
        try {
            DataLayer dataLayer = TagManager.getInstance(context).getDataLayer();
            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                dataLayer.push("userID",MySharedPrefs.INSTANCE.getUserId());
            dataLayer.pushEvent("send", DataLayer.mapOf("hitType", "event","eventCategory",category,"eventAction",action,"eventLabel",label));
        }catch(Exception e){
            e.getMessage();
        }
    }
    public static void setQGraphevent(String event_name,JSONObject json){
        /*For QGraph event tracking*/
        try {
            if(MySharedPrefs.INSTANCE.getUserId()!=null)
                HomeScreen.qg.setUserId(MySharedPrefs.INSTANCE.getUserId());

          /*  JSONObject json = new JSONObject();
            try {
                json.put("eventCategory",category);
                json.put("eventAction", action);
                json.put("eventLabel", label);
            } catch (Exception e) {
            }*/
            HomeScreen.qg.logEvent(event_name, json);
        }catch(Exception e){
            e.getMessage();
        }
    }


	public static void transactionCapture(Context context,String orderid,String storeName,String totalOrder,String shippingCharge,String taxCharge){
        try {
            if(BaseActivity.mTracker != null) {

                if(MySharedPrefs.INSTANCE.getUserId()!=null){
                    System.out.println("userId = " + MySharedPrefs.INSTANCE.getUserId());
                    BaseActivity.mTracker.set("&uid", MySharedPrefs.INSTANCE.getUserId());
                }

                BaseActivity.mTracker.send(new HitBuilders.TransactionBuilder()
                        .setTransactionId(orderid)
                        .setAffiliation(storeName)
                        .setRevenue(Double.parseDouble(totalOrder))
                        .setShipping(Double.parseDouble(shippingCharge))
                        .setTax(Double.parseDouble(taxCharge))
                        .setCurrencyCode("INR")
                        .build());
            }
           /* Map<String, String> params=new HitBuilders.TransactionBuilder()
                    .setTransactionId(orderid)
                    .setAffiliation(storeName)
                    .setRevenue(Double.parseDouble(totalOrder))
                    .setShipping(Double.parseDouble(shippingCharge))
                    .setTax(Double.parseDouble(taxCharge))
                    .setCurrencyCode("INR")
                    .build();
            AnalyticsSampleApp app = ((AnalyticsSampleApp)((Activity)context).getApplication());
            Tracker appTracker = app.getTracker(AnalyticsSampleApp.TrackerName.APP_TRACKER);
            Tracker ecommerceTracker = app.getTracker(AnalyticsSampleApp.TrackerName.ECOMMERCE_TRACKER);
            appTracker.send(params);
            ecommerceTracker.send(params);*/

        }catch(Exception e){
            e.getMessage();
        }
	}

    public static void captureItemsInAOrder(Context context,String orderid,String name,String sku,String category,String price,String qty){
        try {
            if(BaseActivity.mTracker != null) {

                if(MySharedPrefs.INSTANCE.getUserId()!=null){
                    System.out.println("userId = " + MySharedPrefs.INSTANCE.getUserId());
                    BaseActivity.mTracker.set("&uid", MySharedPrefs.INSTANCE.getUserId());
                }


                BaseActivity.mTracker.send(new HitBuilders.ItemBuilder()
                        .setTransactionId(orderid)
                        .setName(name)
                        .setSku(sku)
                        .setCategory(category)
                        .setPrice(Double.parseDouble(price))
                        .setQuantity(Long.parseLong(qty))
                        .setCurrencyCode("INR")
                        .build());
            }

        }catch(Exception e){
            e.getMessage();
        }
    }

    public static void screenView(Activity activity,String screenName){                //screen count of GA
		try{
			// Get tracker.
//			Tracker t = ((AnalyticsSampleApp) activity.getApplication()).getTracker(
//					AnalyticsSampleApp.TrackerName.APP_TRACKER);
//			// Set screen name.
//			t.setScreenName(screenName);
//			// Send a screen view.
//			t.send(new HitBuilders.ScreenViewBuilder().build());

		}catch(Exception e){}

	}

    public static String getDeviceId(Context con) {
        String deviceId = Settings.Secure.getString(con.getContentResolver(), Settings.Secure.ANDROID_ID);
        return deviceId;
    }

    public static String getVersion(Context con){
        String version;
        try {
            PackageInfo pInfo = con.getPackageManager().getPackageInfo(con.getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            version=con.getResources().getString(R.string.app_version);
        }
        return version;
    }


   public static void writeErrorInSdCard(Context con,String msg){
       try {
           File myFile = new File(Environment.getExternalStorageDirectory(),"/Grocermax_log/log.txt");
           myFile.createNewFile();
           FileOutputStream fOut = new FileOutputStream(myFile);
           OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
           myOutWriter.append(new Date().toString()+"-----"+ msg);
           myOutWriter.close();
           fOut.close();

       } catch (Exception e) {

       }
   }

    public static String loadJSONFromAsset(Context mContext, String fileName) {
        String json = null;
        try {

            InputStream is = mContext.getAssets().open(fileName);

            int size = is.available();

            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();

            json = new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;

    }



//	public static void GAClickCapture(Activity activity,String strPrice,String strContentType,String strContentId,String strEventName)
//	{
//		try {
//			// Get tracker.
//			Tracker t = ((AnalyticsSampleApp) activity.getApplication()).getTracker(
//					AnalyticsSampleApp.TrackerName.APP_TRACKER);
//			// Set screen name.
////			t.setScreenName(screenName);
//			// Send a screen view.
////			t.send(new HitBuilders.ScreenViewBuilder().build());
//			Map<String,String> params = new HashMap<String,String>();
//			params.put(GA_PRICE, strPrice);
//			params.put(GA_CONTENT_TYPE,strContentType);
//			params.put(GA_CONTENT_ID, strContentId);
//			params.put(GA_CURRENCY, "INR");
//
//			t.send(new HitBuilders.EventBuilder()
//					.set(UtilityMethods.GA_EVENTNAME, strEventName)
//					.setAll(params)
//					.build());
//
//		}catch(Exception e){}
//	}


    public static DisplayImageOptions initImageLoaderMCtegoryDeal(Context context,int icon) {
        DisplayImageOptions  baseImageoptions=null;
        try {
              baseImageoptions = new DisplayImageOptions.Builder()
                    .showImageOnLoading(icon)
                    .showImageForEmptyUri(icon)
                    .showImageOnFail(icon)

//					.showImageOnLoading(R.drawable.cat_deals_holder)
//					.showImageForEmptyUri(R.drawable.cat_deals_holder)
//					.showImageOnFail(R.drawable.cat_deals_holder)
                    .cacheInMemory(true).cacheOnDisk(true).considerExifParams(true)
                    .build();

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                    context).threadPriority(Thread.NORM_PRIORITY - 2)
                    .denyCacheImageMultipleSizesInMemory()
                    .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                    .diskCacheSize(5 * 1024 * 1024)
                    .tasksProcessingOrder(QueueProcessingType.LIFO).build();
            ImageLoader.getInstance().init(config);
        }catch(Exception e){
            new GrocermaxBaseException("BaseActivity", "initImageLoaderM", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
        }
        return baseImageoptions;
    }

    public static boolean isValidPhone(String phone) {
        String PHONE_PATTERN = "^[987]\\d{9}$";
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }

    public static boolean checkAvailableTimeSlot(ArrayList<String> alAvailable){
        if(alAvailable.size()>0){
            for(int i=0;i<alAvailable.size();i++){
                if(alAvailable.get(i).equals("1")){
                  return false;
                }
            }
            return true;
        }
       return true;
    }

    public static void saveImageInInternalMemory(Context con,String file_name,Bitmap bitmapImage){
        try {
            ContextWrapper cw = new ContextWrapper(con);

            if (cw!=null) {
                File directory = cw.getDir("Grocermax_Banner", Context.MODE_PRIVATE);
                File mypath=new File(directory,file_name);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(mypath);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Bitmap loadImageFromStorage(Context con,String file_name)
    {
        try {
            ContextWrapper cw = new ContextWrapper(con);
            File directory = cw.getDir("Grocermax_Banner", Context.MODE_PRIVATE);
            File mypath=new File(directory,file_name);
            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(mypath));
            System.out.println("Server loadImageFromStorage="+file_name+"---"+b);
            return b;
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
      return null;
    }

    public static boolean isFilePresent(String file_name,Context con) {
        ContextWrapper cw = new ContextWrapper(con);
        File directory = cw.getDir("Grocermax_Banner", Context.MODE_PRIVATE);
        File mypath=new File(directory,file_name);
        System.out.println("Server isFilePresent="+mypath.exists());
        return mypath.exists();
    }

   public static void deleteBannerDirecort(Context con){
       ContextWrapper cw = new ContextWrapper(con);
       File dir = cw.getDir("Grocermax_Banner", Context.MODE_PRIVATE);
       if (dir.isDirectory())
       {
           String[] children = dir.list();
           for (int i = 0; i < children.length; i++)
           {
               new File(dir, children[i]).delete();
           }
       }
   }

    public static void setGridViewHeightBasedOnChildren(GridView gridView, int columns) {
        ListAdapter listAdapter = gridView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int items = listAdapter.getCount();
        int rows = 0;

        View listItem = listAdapter.getView(0, null, gridView);
        listItem.measure(0, 0);
        totalHeight = listItem.getMeasuredHeight();
        // Display display = getActivity().getWindowManager().getDefaultDisplay();
        //totalHeight = display.getWidth()/3;
        //width=width/3;

        float x = 1;
        if( items > columns ){
            x = items/columns;
            rows = (int) (x);
            totalHeight *= rows;
        }

        ViewGroup.LayoutParams params = gridView.getLayoutParams();
        params.height = totalHeight;
        gridView.setLayoutParams(params);

    }



    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

}

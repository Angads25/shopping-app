package com.sakshay.grocermax;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sakshay.grocermax.api.ConnectionService;
import com.sakshay.grocermax.api.MyReceiverActions;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.bean.OTPResponse;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;


public class OneTimePassword extends BaseActivity {
    OTPResponse otpDataBean;
    String params;
    String strEmail;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        Bundle bundle = getIntent().getExtras();
        addActionsInFilter(MyReceiverActions.REGISTER_USER);

        if (bundle != null) {
             otpDataBean = (OTPResponse)bundle.getSerializable("Otp");
             params = bundle.getString("USER_REGISTER_DATA");
             strEmail = bundle.getString("USER_EMAIL");
        }

        final EditText etOTP = (EditText)findViewById(R.id.et_otp);
        Button btnOTP = (Button)findViewById(R.id.btn_submit);
        btnOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etOTP.getText().toString().equals("")){
                    UtilityMethods.customToast("Please enter valid otp", OneTimePassword.this);
                    return;
                }
                String str = otpDataBean.getOTP();
                String str1 = etOTP.getText().toString();
                if(otpDataBean.getOTP().equals(etOTP.getText().toString())){
                    if (UtilityMethods.isInternetAvailable(mContext)) {
                        showDialog();
                        String url = UrlsConstants.REGESTRATION_URL;
                        url += params;
						myApi.reqUserRegistration(url);
                    }
                }else{
                    UtilityMethods.customToast("Please enter valid otp", OneTimePassword.this);
                }
            }
        });
        initHeader(findViewById(R.id.header), true, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_one_time_password, menu);
        return true;
    }

    @Override
    void OnResponse(Bundle bundle) {
        if (bundle.getString("ACTION").equals(MyReceiverActions.REGISTER_USER)) {
            dismissDialog();
            LoginResponse userDataBean = (LoginResponse) bundle.getSerializable(ConnectionService.RESPONSE);

            if (userDataBean.getFlag().equalsIgnoreCase("1")) {
                UtilityMethods.customToast(AppConstants.ToastConstant.REGISTER_SUCCESSFULL, mContext);
                //finish();
                MySharedPrefs.INSTANCE.putUserId(userDataBean.getUserID());
                if (MySharedPrefs.INSTANCE.getFacebookEmail() == null) {
//                    MySharedPrefs.INSTANCE.putUserEmail(((EditText) findViewById(R.id.et_register_email)).getText().toString().trim());
                    MySharedPrefs.INSTANCE.putUserEmail(strEmail);
                }
                else {
                    MySharedPrefs.INSTANCE.putUserEmail(MySharedPrefs.INSTANCE.getFacebookEmail());
                }
                MySharedPrefs.INSTANCE.putLoginStatus(true);

                ArrayList<CartDetail> cart_products = UtilityMethods.readLocalCart(OneTimePassword.this, AppConstants.localCartFile);
                if (cart_products != null && cart_products.size() > 0) {
                    try {
                        JSONArray products = new JSONArray();
                        for (int i = 0; i < cart_products.size(); i++) {
                            JSONObject prod_obj = new JSONObject();
                            prod_obj.put("productid", cart_products.get(i).getItem_id());
                            prod_obj.put("quantity", cart_products.get(i).getQty());
                            products.put(prod_obj);
                        }
                        showDialog();
                        String url;
                        if (MySharedPrefs.INSTANCE.getQuoteId() == null || MySharedPrefs.INSTANCE.getQuoteId().equals("")) {
                            System.out.println("without quote json=" + products.toString());
                            url = UrlsConstants.ADD_TO_CART_URL
                                    + userDataBean.getUserID() + "&products="
                                    + URLEncoder.encode(products.toString(), "UTF-8");
                        } else {
                            System.out.println("with quote json=" + products.toString());
                            url = UrlsConstants.ADD_TO_CART_URL
                                    + userDataBean.getUserID() + "&quote_id=" + MySharedPrefs.INSTANCE.getQuoteId() + "&products="
                                    + URLEncoder.encode(products.toString(), "UTF-8");
                        }
                        //String url = UrlsConstants.ADD_TO_CART_URL + userDataBean.getUserID() +"&quote_id="+MySharedPrefs.INSTANCE.getQuoteId()+ "&products="+ URLEncoder.encode(products.toString(), "UTF-8");
                        myApi.reqAddToCart(url);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    setResult(RESULT_OK);
                    finish();
                }
            } else {
                UtilityMethods.customToast(userDataBean.getResult(), mContext);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initHeader(findViewById(R.id.header), true, null);
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

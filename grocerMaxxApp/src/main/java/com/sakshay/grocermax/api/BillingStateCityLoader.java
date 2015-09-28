package com.sakshay.grocermax.api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sakshay.grocermax.AddressDetail;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.BillingAddress;
import com.sakshay.grocermax.CreateNewAddress;
import com.sakshay.grocermax.SearchTabs;
import com.sakshay.grocermax.ShippingAddress;
import com.sakshay.grocermax.bean.Address;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Abhishek on 9/16/2015.
 */
public class BillingStateCityLoader extends AsyncTask<String, String, String> {
    Context context;
    private Activity activity;
    Address address;
    String billing;
    String editIndex;
    public static ArrayList<String> alState = null;                //getting state name for billing address
    public static ArrayList<String> alStateId = null;                //getting state id for billing address
    public BillingStateCityLoader(Context context,Address address,String billing,String editIndex){
        this.context = context;
        this.address = address;
        this.billing = billing;
        this.editIndex = editIndex;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        alState = new ArrayList<String>();
        alStateId = new ArrayList<String>();
        if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
        {
            ((AddressDetail)context).showDialog();
        }else{
            ((BillingAddress)context).showDialog();
        }
    }

    @Override
    protected String doInBackground(String... params) {
        String strResult = "";
        HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
        String urlString = params[0];
        if(urlString.contains("?")) {
            urlString += "&version=1.0";
        }else{
            urlString += "?version=1.0";
        }
        HttpGet httpGet = new HttpGet(urlString);
        httpGet.setHeader("Content-Type", "application/json");
        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity);
        } catch (ClientProtocolException e) {
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","doInBackground",e.getMessage(),GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION,strResult);
        } catch (IOException e) {
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","doInBackground",e.getMessage(),GrocermaxBaseException.IO_EXCEPTION,strResult);
        }
        catch (Exception e){
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","doInBackground",e.getMessage(),GrocermaxBaseException.EXCEPTION,strResult);
        }
        return null;
    }

    protected void onPostExecute(String result) {
        try{
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).dismissDialog();
            }else{
                ((BillingAddress)context).dismissDialog();
            }
            System.out.print("==Result=="+result);
            JSONObject jsonObject = new JSONObject(result);

            String strResult = jsonObject.getString("Result");
            JSONArray jsonArray = jsonObject.getJSONArray("state");

            for(int i=0;i<jsonArray.length();i++){
                String strState = jsonArray.getJSONObject(i).getString("default_name");             //state name
                String strStateId = jsonArray.getJSONObject(i).getString("region_id");             //state id
                alState.add(strState);
                alStateId.add(strStateId);
            }

            if(billing.equalsIgnoreCase("profilenewaddressbilling")){
                if(alStateId.size() > 0) {                                                        //editing the billing address from MyProfile
                    Intent intent = new Intent(context, CreateNewAddress.class);
                    intent.putExtra("address", address);
                    intent.putExtra("shippingorbillingaddress", billing);
                    intent.putExtra("editindex", editIndex);                                    //means editing the address not adding.
                    ((AddressDetail) context).startActivityForResult(intent, AddressDetail.requestNewAddress);
                }
            }
            else{
                if(alStateId.size() > 0) {
                    if(address == null){                         //adding new address
                        Intent intent = new Intent(context, CreateNewAddress.class);
                        intent.putExtra("shippingorbillingaddress", billing);
                        intent.putExtra("editindex", "-1");                                    //means adding the address not editing.
                        ((BillingAddress) context).startActivityForResult(intent, BillingAddress.requestNewAddress);
                    }else{                                      //editing in existing address
                        Intent intent = new Intent(context, CreateNewAddress.class);
                        intent.putExtra("address", address);
                        intent.putExtra("shippingorbillingaddress", billing);
                        intent.putExtra("editindex", editIndex);                                    //means editing the address not adding.
                        ((BillingAddress) context).startActivityForResult(intent, BillingAddress.requestNewAddress);
                    }
                }
            }

//            if(jsonObject.getString("Result").equalsIgnoreCase("Categories Found")){
//
//            }
//            if(jsonObject.getString("Result").equalsIgnoreCase("No Result Found")){
//                ((BaseActivity)context).dismissDialog();
//                UtilityMethods.customToast(jsonObject.getString("Result"), context);
//                return;
//            }

        }catch(JSONException e){
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.JSON_EXCEPTION,result);
        }catch (NullPointerException e){
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.NULL_POINTER,result);
        }catch (Exception e){
            if(billing.equalsIgnoreCase("profilenewaddressbilling"))        //edit in billing address from My Profile
            {
                ((AddressDetail)context).showDialog();
            }else{
                ((BillingAddress)context).showDialog();
            }
            new GrocermaxBaseException("BillingStateCityLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.EXCEPTION,result);
        }
    }

}

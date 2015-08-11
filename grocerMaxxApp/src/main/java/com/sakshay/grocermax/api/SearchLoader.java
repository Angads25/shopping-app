package com.sakshay.grocermax.api;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;

import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.SearchTabs;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UtilityMethods;

public class SearchLoader extends AsyncTask<String, String, String> {
	Context context;
	public static List<HashMap<String, String>> listCategory;
	List<HashMap<String, String>> listProd;
	ArrayList<JSONObject> alProd;                            //contain product in JsonObject form so that assign in JsonArray
	JSONArray jsonArrMulProd[];
//	public static HashMap<String, JSONArray> valuePairs;
//	public static JSONObject jsonObject[];
	public static JSONObject jsonObjectTop[];                                 //contain array for individual tab. 
	private final String TAG_CAT_CATEGORY_ID = "category_id";
	private final String TAG_CAT_PARENT_ID = "parent_id";
	public static final String TAG_CAT_NAME = "name";
	private final String TAG_CAT_IS_ACTIVE = "is_active";
	private final String TAG_CAT_POSITION = "position";
	private final String TAG_CAT_LEVEL = "level";
	
	private final String TAG_PROD_FULL_NAME = "Name";
	private final String TAG_PROD_BRAND = "p_brand";
	private final String TAG_PROD_P_NAME = "p_name";
	private final String TAG_PROD_PACK = "p_pack";
	private final String TAG_PROD_PROMOTION_LEVEL = "promotion_level";
	private final String TAG_PROD_STATUS = "Status";
	private final String TAG_PROD_PRODUCT_ID = "productid";
	private final String TAG_PROD_PRICE = "Price";
	private final String TAG_PROD_SALE_PRICE = "sale_price";
	private final String TAG_PROD_IMAGE = "Image";
//	private final String TAG_PROD_PRODUCT_QTY = "product_qty";
//	private final String TAG_PROD_DESCRIPTION = "product_description";
	private final String TAG_PROD_CURRENCY_CODE = "currencycode";
	private final String TAG_PROD_CAT_ID = "categoryid";
	
	private Activity activity;
	public SearchLoader(Context context){
		this.context = context;
		this.activity = activity;
	}
	
    @Override
    protected String doInBackground(String... params) {
		String strResult = "";
    	HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
    	HttpGet httpGet = new HttpGet(params[0]);
		httpGet.setHeader("Content-Type", "application/json");
		HttpResponse response = null;
		try {
			response = client.execute(httpGet);
			HttpEntity resEntity = response.getEntity();
			strResult =  EntityUtils.toString(resEntity);
			return EntityUtils.toString(resEntity);
		} catch (ClientProtocolException e) {
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("SearchLoader","doInBackground",e.getMessage(),GrocermaxBaseException.CLIENT_PROTOCOL_EXCEPTION,"80",strResult);
		} catch (IOException e) {
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("SearchLoader","doInBackground",e.getMessage(),GrocermaxBaseException.IO_EXCEPTION,"83",strResult);
		}catch (Exception e){
			((BaseActivity)context).dismissDialog();
			new GrocermaxBaseException("SearchLoader","doInBackground",e.getMessage(),GrocermaxBaseException.EXCEPTION,"86",strResult);
		}
		return null;
    }
 
    protected void onPostExecute(String result) {
    	
    	try{
//    		if(result == null || result.equalsIgnoreCase(""))
//    		{
//    			SearchTabs.getInstance().endDialog();
//    			UtilityMethods.customToast("Error Occured", context);
//    			return;
//    		}
    		
    		System.out.print("==Result=="+result);
    		JSONObject jsonObject = new JSONObject(result);
    		if(jsonObject.getString("Result").equalsIgnoreCase("Categories Found")){
    			
    		}
    		if(jsonObject.getString("Result").equalsIgnoreCase("No Result Found")){
    			((BaseActivity)context).dismissDialog();
    			UtilityMethods.customToast(jsonObject.getString("Result"), context);
    			return;	
    		}

    		listCategory = new ArrayList<HashMap<String,String>>();
    		listProd = new ArrayList<HashMap<String,String>>();
    		HashMap<String, String> map;
    		
    		JSONArray jsonArrCat = jsonObject.getJSONArray("Category");
    		for(int i=0;i<jsonArrCat.length();i++)
    		{
    			map = new HashMap<String, String>();
    			JSONObject jsonObjectCat = jsonArrCat.getJSONObject(i);
    			String strCatId = jsonObjectCat.getString("category_id");
    			String strParentId = jsonObjectCat.getString("parent_id");
    			String strName = jsonObjectCat.getString("name");
    			String strIsActive = jsonObjectCat.getString("is_active");
    			String strPosition = jsonObjectCat.getString("position");
    			String strLevel = jsonObjectCat.getString("level");
    			
    			map.put(TAG_CAT_CATEGORY_ID, strCatId);
    			map.put(TAG_CAT_PARENT_ID, strParentId);
    			map.put(TAG_CAT_NAME, strName);
    			map.put(TAG_CAT_IS_ACTIVE, strIsActive);
    			map.put(TAG_CAT_POSITION, strPosition);
    			map.put(TAG_CAT_LEVEL, strLevel);
    			
    			listCategory.add(map);
    		}

    		JSONArray jsonArrProd = jsonObject.getJSONArray("Product");
    		alProd = new ArrayList<JSONObject>();
    		for(int i=0;i<jsonArrProd.length();i++)
    		{
    			map = new HashMap<String, String>();
    			JSONObject jsonObjectProd = jsonArrProd.getJSONObject(i);
    			String strProdName = jsonObjectProd.getString("Name");
    			String strProdBrand = jsonObjectProd.getString("p_brand");
    			String strProdPName = jsonObjectProd.getString("p_name");
    			String strProdPack = jsonObjectProd.getString("p_pack");
    			String strProdPositionLevel = jsonObjectProd.getString("promotion_level");
    			String strProdStatus = jsonObjectProd.getString("Status");
    			String strProdId = jsonObjectProd.getString("productid");
    			String strProdPrice = jsonObjectProd.getString("Price");
    			String strProdSalePrice = jsonObjectProd.getString("sale_price");
    			String strProdImage = jsonObjectProd.getString("Image");
//    			String strProdQuantity = jsonObjectProd.getString("product_qty");
//    			String strProdDesc = jsonObjectProd.getString("product_description");
    			String strProdCurrencyCode = jsonObjectProd.getString("currencycode");
    			
    			String strProdCatId = jsonObjectProd.getJSONArray("categoryid").getString(0);
    			
    			map.put(TAG_PROD_FULL_NAME, strProdName);
    			map.put(TAG_PROD_BRAND, strProdBrand);
    			map.put(TAG_PROD_P_NAME, strProdPName);
    			map.put(TAG_PROD_PACK, strProdPack);
    			map.put(TAG_PROD_PROMOTION_LEVEL, strProdPositionLevel);
    			map.put(TAG_PROD_STATUS, strProdStatus);
    			map.put(TAG_PROD_PRODUCT_ID, strProdId);
    			map.put(TAG_PROD_PRICE, strProdPrice);
    			map.put(TAG_PROD_SALE_PRICE, strProdSalePrice);
    			map.put(TAG_PROD_IMAGE, strProdImage);
//    			map.put(TAG_PROD_PRODUCT_QTY, strProdQuantity);
//    			map.put(TAG_PROD_DESCRIPTION, strProdDesc);
    			map.put(TAG_PROD_CURRENCY_CODE, strProdCurrencyCode);
    			map.put(TAG_PROD_CAT_ID, strProdCatId);
    			
    			alProd.add(jsonObjectProd);
    			listProd.add(map);
    		}    		

    	jsonArrMulProd = new JSONArray[listCategory.size()];
//    	valuePairs = new HashMap<String, JSONArray>();
    	jsonObjectTop = new JSONObject[listCategory.size()];
  
    	for(int m=0;m<listCategory.size();m++)
    	{
    		jsonArrMulProd[m] = new JSONArray();
    		jsonObjectTop[m] = new JSONObject();
    	}
    	
    	for(int i=0;i<listProd.size();i++)
    	{
    		for(int j=0;j<listCategory.size();j++)
    		{
    			String strCatid = listCategory.get(j).get(TAG_CAT_CATEGORY_ID);
    			String strCatName = listCategory.get(j).get(TAG_CAT_NAME);
    			String strProdId = listProd.get(i).get(TAG_PROD_CAT_ID);
    			String strProdName = listProd.get(i).get(TAG_PROD_FULL_NAME);
				System.out.println(strCatName+"==catid=="+strCatid);
				System.out.println(strProdName+"==prodid=="+strProdId);
    			if(listCategory.get(j).get(TAG_CAT_CATEGORY_ID).equalsIgnoreCase(listProd.get(i).get(TAG_PROD_CAT_ID)))
    			{
    				jsonArrMulProd[j].put(alProd.get(i));
    				break;
    			}
    		}
    	}

    	for(int k=0;k<jsonArrMulProd.length;k++)
    	{
    		System.out.println("==Final Prod Array=="+jsonArrMulProd[k]);
    		jsonObjectTop[k].put("Product",jsonArrMulProd[k]);
    	}
    	Intent call = new Intent(context, SearchTabs.class);
    	context.startActivity(call);

		}catch(JSONException e){
			new GrocermaxBaseException("SearchLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.JSON_EXCEPTION,"216",result);
		}catch (NullPointerException e){
			new GrocermaxBaseException("SearchLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.NULL_POINTER,"218",result);
		}catch (Exception e){
			new GrocermaxBaseException("SearchLoader","onPostExecute",e.getMessage(),GrocermaxBaseException.EXCEPTION,"220",result);
		}
    }
    
} 
package com.rgretail.grocermax.api;

import android.app.IntentService;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.rgretail.grocermax.CartProductList;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.api.ConnectionServiceParser.MyParserType;
import com.rgretail.grocermax.bean.BaseResponseBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.MyHttpUtils;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

public class ConnectionService extends IntentService {

	public static final String TAG = "ConnectionService";
	public static final String RESPONSE = "response";
	public static final String ERROR = "error";
	public static final String JSON = "json";
	public static final String DATA = "data";
	public static final String PARSE_TYPE = "parse_type";
	public static final String ACTION = "action";
	public static final String HTTP_REQUEST_TYPE = "http_request_type";
	public static final String URL = "url";
	public static final String PAIRS = "pairs";
	public static final String JSON_OBJECT = "json_object";              ////
	public static final String JSON_ARRAY = "json_array";
	public static final String JSON_STRING = "json_string";
	public static final String PHOTO_PAIRS = "photoPairs";
	public static final String ISRELOAD = "isreloaded";
	public static final String ISRELOADDATA = "isreloadeddata";
	public static final String IO_EXCEPTION = "IO Exception";
	public static final String JSON_EXCEPTION = "JSON_EXCEPTION";
	public static final String EXCEPTION = "Exception";
	public static final String ACCESS_TOKEN = "AccessToken";

	public ConnectionService() {
		super("ConnectionService");
	}

	private String mAction;
	private String requestType;
	private String urlString;
	private HashMap<String, String> mHashMap;
	private JSONObject jsonObject;
	private JSONArray jsonArray;
	private String jsonString;
	private String accessToken;

	@Override
	public void onCreate() {
		super.onCreate();

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected void onHandleIntent(Intent intent) {
		String response_str = null;
		Bundle bundle = new Bundle();
		try {
			mAction = intent.getStringExtra(ACTION);
			requestType = intent.getStringExtra(HTTP_REQUEST_TYPE);
			urlString = intent.getStringExtra(URL);
			accessToken = intent.getStringExtra(ACCESS_TOKEN);

			if (AppConstants.DEBUG) {
				Log.i(TAG, "URL::" + urlString);
			}



			if(intent.getSerializableExtra(JSON_OBJECT) != null){
				jsonObject = (JSONObject)intent
						.getSerializableExtra(JSON_OBJECT);
				if (AppConstants.DEBUG) {
					Log.i(TAG, "JSON_OBJECT::" + jsonObject);
				}
			}


			if(intent.getSerializableExtra(JSON_ARRAY) != null){
				jsonArray = (JSONArray)intent
						.getSerializableExtra(JSON_ARRAY);
				if (AppConstants.DEBUG) {
					Log.i(TAG, "JSON_ARRAY::" + jsonArray);
				}
			}

			if (intent.getSerializableExtra(PAIRS) != null) {
				mHashMap = (HashMap<String, String>) intent
						.getSerializableExtra(PAIRS);
				if (AppConstants.DEBUG) {
					Log.i(TAG, "HASHMAP::" + mHashMap);

				}
			}
			if (intent.getSerializableExtra(JSON_STRING) != null) {
				jsonString = intent.getStringExtra(JSON_STRING);
				if (AppConstants.DEBUG) {
					Log.i(TAG, "JSON STRING::" + jsonString);
				}
			}

			HashMap<String, String> photoMaps = null;
			if (intent.getSerializableExtra(PHOTO_PAIRS) != null) {
				photoMaps = (HashMap<String, String>) intent
						.getSerializableExtra(PHOTO_PAIRS);
				if (AppConstants.DEBUG) {
					Log.i(TAG, "photoMaps::" + photoMaps);
				}
			}

			urlString = urlString.replace(" ", "%20");

			HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();

			if (requestType.equalsIgnoreCase("MULTIPART"))
				response_str = processMultipartRequest(photoMaps, mHashMap,
						client, urlString);
			else
				response_str = processRequest(mHashMap, jsonString,
						requestType, urlString, client);

			if (AppConstants.DEBUG) {
				Log.i(TAG, "RESPONSE:::" + response_str);
			}
            parseData(response_str, intent.getIntExtra(PARSE_TYPE, -1), bundle);
            bundle.putString(ERROR, null);
            bundle.putString(JSON, response_str);

		} catch (IOException e) {
			bundle.putString(ERROR, IO_EXCEPTION);
			Log.e(TAG, "ERROR::" + e.getMessage());
			new GrocermaxBaseException("ConnectionService","onHandleIntent",e.getMessage(),GrocermaxBaseException.IO_EXCEPTION,response_str);
			e.printStackTrace();
		}
		catch (JSONException e) {
			bundle.putString(ERROR, JSON_EXCEPTION);
			Log.e(TAG, "ERROR::" + e.getMessage());
			new GrocermaxBaseException("ConnectionService","onHandleIntent",e.getMessage(),GrocermaxBaseException.JSON_EXCEPTION,response_str);
			e.printStackTrace();
		} catch (Exception e) {
			bundle.putString(ERROR, EXCEPTION);
			Log.e(TAG, "ERROR::" + "Unknown Error");
			new GrocermaxBaseException("ConnectionService","onHandleIntent",e.getMessage(),GrocermaxBaseException.EXCEPTION,response_str);
			e.printStackTrace();
		}
		finally{
			try {
				if (mAction.equals(MyReceiverActions.LOGIN) || mAction.equals(MyReceiverActions.ADD_TO_CART)) {
					if (MySharedPrefs.INSTANCE.getFacebookId() != null) {
					}
				}
			}catch(Exception e){}
		}
		try {

			Intent broadcastIntent = new Intent(mAction);
			broadcastIntent.putExtra(DATA, bundle);
			LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
		}catch(Exception e){
			e.printStackTrace();
			new GrocermaxBaseException("ConnectionService","onHandleIntentLOCALBROADCAST",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	private String processRequest(HashMap<String, String> hashMap,
								  String jsonString, String requestType, String urlString,
								  HttpClient client) throws ClientProtocolException, IOException,
			SAXException, ParserConfigurationException {


		List<NameValuePair> nameValuePairs = null;
		HttpResponse response = null;
		try {

			if (hashMap != null) {
				nameValuePairs = new ArrayList<NameValuePair>(hashMap.size());
				for (Map.Entry<String, String> entry : hashMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					nameValuePairs.add(new BasicNameValuePair(key, value));
				}
			}

			if (requestType.equalsIgnoreCase("POST")) {
				HttpPost httpPost = new HttpPost(urlString);
				//httpPost.setHeader("Content-Type", "application/json");
//				if (accessToken != null) {
				httpPost.setHeader("device", getResources().getString(R.string.app_device));
				httpPost.setHeader("version",getResources().getString(R.string.app_version));
				if(MySharedPrefs.INSTANCE.getSelectedStateId() != null) {
					httpPost.setHeader("storeid", MySharedPrefs.INSTANCE.getSelectedStoreId());
				}

//				}
				// IF Required.
				// httpPost.setHeader("Authorization", "Basic " +
				// Base64.NO_WRAP));

				System.out.println("==second parameters is==" + nameValuePairs);
				if (jsonString != null) {
					httpPost.setEntity(new StringEntity(jsonString));
				}else if (nameValuePairs != null) {
					httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				}

//			if(CartProductList.getInstance().jsonObjectUpdate != null){
//				httpPost.setEntity(new StringEntity(CartProductList.getInstance().jsonObjectUpdate.toString(), "UTF8"));
//			}
//
//			System.out.println("==post="+httpPost);
//			System.out.println("==json=="+CartProductList.getInstance().jsonObjectUpdate.toString());

				response = client.execute(httpPost);
				CartProductList.getInstance().jsonObjectUpdate = null;
			} else if (requestType.equalsIgnoreCase("GET")) {
//				if(urlString.contains("?")) {
//					urlString += "&version=1.0";
//				}else{
//					urlString += "?version=1.0";
//				}
				HttpGet httpGet = new HttpGet(urlString);
				httpGet.setHeader("device", getResources().getString(R.string.app_device));
				httpGet.setHeader("version",getResources().getString(R.string.app_version));
				if(MySharedPrefs.INSTANCE.getSelectedStateId() != null) {
					httpGet.setHeader("storeid", MySharedPrefs.INSTANCE.getSelectedStoreId());
				}
				if (AppConstants.DEBUG) {
					Log.i(TAG, "URL:::" + urlString);
				}

//				HttpParams httpParameters = new BasicHttpParams();
//				int timeoutConnection = 0;
//				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//				int timeoutSocket = 5000;
//				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);


//				httpGet.setHeader("Content-Type", "application/json");
//				if (accessToken != null) {
//					httpGet.setHeader("AccessToken", "" + accessToken);
//				}
				// IF Required.
				// httpGet.setHeader("Authorization", "Basic " +
				// Base64.NO_WRAP));
				response = client.execute(httpGet);
			}

            System.out.println("App version new="+ getResources().getString(R.string.app_version));
            try {
                /*0 - Noramal
                  1- Update available.You have to update
                  2- Update available.You can skip.
                  3- Under maintainance	*/

				//Header upgradeHeader = response.getFirstHeader("upgrade-app");
               // AppConstants.strUpgradeValue = "3";
                Header header[] = response.getAllHeaders();
				//String strHeader = upgradeHeader.getName();
                for(int i=0;i<header.length;i++){
                String strHeader = header[i].getName();
				if(strHeader != null) {
					if (strHeader.equals("upgrade-app")) {
						AppConstants.strUpgradeValue = header[i].getValue();
					}
                    if (strHeader.equals("notification")) {
                        AppConstants.strPopupData = header[i].getValue();
                    }
				}
               /* String strHeaderNotification = header[i].getName();
                if(strHeaderNotification != null) {
                    if (strHeaderNotification.equals("notification")) {
                        AppConstants.strPopupData = header[7].getValue();
                    }
                }*/
                }
//				Header header[] = response.getAllHeaders();
//				for (int i = 0; i < header.length; i++) {
//					String strHeader = header[i].getName();
//					if(strHeader.equals("upgrade-app")){
//						if (AppConstants.DEBUG) {
//							Log.i(TAG, "RESPONSE FOR PARSE:::" + strHeader);
//							String strHeaderValue = header[i].getValue();
//						}
//					}else{
//						if (AppConstants.DEBUG) {
//							Log.i(TAG, "RESPONSE FOR PARSE:::" + header);
//						}
//					}
//				}
			}catch(Exception e){}

			HttpEntity resEntity = response.getEntity();
			return EntityUtils.toString(resEntity);

		}catch (ConnectTimeoutException e) {
			//Here Connection TimeOut excepion
//			Toast.makeText(xyz.this, "Your connection timedout", 10000).show();
		}
		catch (Exception e) {
			new GrocermaxBaseException("ConnectionService","processRequest",e.getMessage(),GrocermaxBaseException.EXCEPTION,EntityUtils.toString(response.getEntity()));
			e.printStackTrace();
		}

		return "";
	}

	private String processMultipartRequest(HashMap<String, String> photoUris,
										   HashMap<String, String> hashMap, HttpClient client, String urlString)
			throws ParseException, IOException {
		try {
			MultipartEntity multiPartEntity = new MultipartEntity(
					HttpMultipartMode.BROWSER_COMPATIBLE);
			if (photoUris != null) {
				for (Map.Entry<String, String> entry : photoUris.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					Bitmap bm = BitmapFactory.decodeFile(value);
					ByteArrayOutputStream bos = new ByteArrayOutputStream();
					bm.compress(CompressFormat.JPEG, 75, bos);
					multiPartEntity.addPart(key,
							new ByteArrayBody(bos.toByteArray(), "minitime.jpg"));
				}
			}


			if (hashMap != null) {
				for (Map.Entry<String, String> entry : hashMap.entrySet()) {
					String key = entry.getKey();
					String value = entry.getValue();
					multiPartEntity.addPart(key, new StringBody(value));
				}
			}
			HttpPost httpPost = new HttpPost(urlString);
			httpPost.setEntity(multiPartEntity);
			HttpResponse response = client.execute(httpPost);
			HttpEntity resEntity = response.getEntity();
			return EntityUtils.toString(resEntity);
		}catch(Exception e){
			new GrocermaxBaseException("ConnectionService","processMultipartRequest",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return "";
	}

	private void parseData(String response, int type, Bundle bundle)
			throws SAXException, ParserConfigurationException, IOException,
			JSONException, RemoteException, OperationApplicationException {

		try {

			switch (type) {
//				case MyParserType.SEARCH_BY_CATEGORY:
//					bundle.putSerializable(RESPONSE,
//							(Serializable) ConnectionServiceParser
//									.parseSearchResponse(response));
//					break;



				case MyParserType.ALL_PRODUCTS_CATEGORY:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseAllProductsCategoriesResponse(response));
					break;

				case MyParserType.LOGIN:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseLoginResponse(response));
					break;
				case MyParserType.REGISTRATION:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;
				case MyParserType.OTP_SUCCESSFULL:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseOTPResponse(response));
					break;
				case MyParserType.FORGOT_PWD:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;
				case MyParserType.EDIT_PROFILE:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;

				case MyParserType.CATEGORY_LIST:
					if (AppConstants.DEBUG) {
						Log.i(TAG, "RESPONSE FOR PARSE:::" + response);
					}
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseCategoryResponse(response));
					// bundle.putSerializable(RESPONSE, (Serializable) response);
					break;
				case MyParserType.CATEGORY_SUBCATEGORY_LIST:
					if (AppConstants.DEBUG) {
						Log.i(TAG, "RESPONSE FOR PARSE:::" + response);

					}
					// bundle.putSerializable(RESPONSE,
					// (Serializable) ConnectionServiceParser
					// .parseCategoryResponse(response));
					bundle.putSerializable(RESPONSE, (Serializable) response);
					break;
				case MyParserType.PRODUCT_LIST:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseProductResponse(response));
					break;
				case MyParserType.SEARCH_PRODUCT_LIST:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseProductResponse(response));
					break;
				case MyParserType.DEAL_PRODUCT_LIST:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseDeal(response));
					break;


				case MyParserType.PRODUCT_CONTENT_LIST:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseProductContentResponse(response));
					break;
//				case MyParserType.ADD_TO_CART:
//					bundle.putSerializable(RESPONSE,
//							(Serializable) ConnectionServiceParser
//									.parseSimpleResponse(response));
//					break;

				case ConnectionServiceParser.MyParserType.ADD_TO_CART:
					BaseResponseBean responseBean = ConnectionServiceParser
							.parseSimpleResponse(response);
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
//              if(MySharedPrefs.INSTANCE.getQuoteId()==null)
//              {
					if(responseBean.getFlag().equalsIgnoreCase("1")) {
						if(responseBean.getQuoteId() != null && !responseBean.getQuoteId().equals("")) {
							MySharedPrefs.INSTANCE.clearQuote();
							MySharedPrefs.INSTANCE.putQuoteId(responseBean.getQuoteId());
							System.out.println("==new quote id cart service==" + MySharedPrefs.INSTANCE.getQuoteId());
						}
					}
//              }

					break;



				case MyParserType.DELETE_FROM_CART:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;
				case MyParserType.LOCATION:

					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseLocationResponse(response));
					break;
				case MyParserType.VIEW_CART:

					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseViewCartResponse(response));
					break;
				case MyParserType.VIEW_CART_UPDATE_LOCALLY:

					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseViewCartResponseLocally(response));
					break;
				case MyParserType.USER_DETAILS:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseUserDetailsResponse(response));
					break;
				case MyParserType.ORDER_HISTORY:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseOrderHistoryResponse(response));
					break;

				case MyParserType.ORDER_DETAIL:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.ORDER_REORDER:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.ADDRESS_BOOK:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseAddressBookResponse(response));
					break;
				case MyParserType.ADD_ADDRESS:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;
				case MyParserType.DELETE_ADDRESS:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseSimpleResponse(response));
					break;
				case MyParserType.FINAL_CHECKOUT:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseFinalCheckoutResponse(response));
					break;
				case MyParserType.CHECKOUT_ADDRESS:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser
									.parseCheckoutAddressResponse(response));
					break;
				case MyParserType.GET_SET_ORDERSTATUS:
					bundle.putSerializable(RESPONSE, (Serializable) response);
					break;
				case MyParserType.SET_PAYTM_ORDER_STATUS_SUCCESS:                            //paytm success
					bundle.putSerializable(RESPONSE, (Serializable) response);
					break;
                case MyParserType.WALLET_INFO:
                    bundle.putSerializable(RESPONSE,
                            (Serializable) response);
                    break;
				case MyParserType.SINGLE_PAGE_DATA:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.REDEEM_POINT:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.APPLY_REMOVE_COUPON:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.PRAMOTION_DATA:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.SUBSCRIBE_USER:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;
				case MyParserType.TOP_PRODUCTS_LIST:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser.parseProductResponse(response));
					break;
				case MyParserType.CATEGORY_BANNER:
					bundle.putSerializable(RESPONSE,
							(Serializable) ConnectionServiceParser.parseCategoryBannerResponse(response));
					break;
                case MyParserType.TERM_CONDITION:
                    bundle.putSerializable(RESPONSE,
                            (Serializable) response);
                    break;
                case MyParserType.WALLET_TRANSACTION:
                    bundle.putSerializable(RESPONSE,
                            (Serializable)ConnectionServiceParser
                                    .parseWalletTransactionResponse(response));
                    break;
				case MyParserType.REG_DEVICE_TOKEN:
					bundle.putSerializable(RESPONSE,
							(Serializable) response);
					break;

			}

		}
		catch (JSONException e) {
			e.printStackTrace();
			bundle.putString(ERROR, JSON_EXCEPTION);
			Log.e(TAG, "ERROR::" + e.getMessage());
			new GrocermaxBaseException("ConnectionService","parseData",e.getMessage(),GrocermaxBaseException.JSON_EXCEPTION,response);
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
			bundle.putString(ERROR, EXCEPTION);
			Log.e(TAG, "ERROR::" + "Unknow Error");
			new GrocermaxBaseException("ConnectionService","parseData",e.getMessage(),GrocermaxBaseException.EXCEPTION,response);
		}
	}





}

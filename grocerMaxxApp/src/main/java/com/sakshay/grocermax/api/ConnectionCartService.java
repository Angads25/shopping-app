package com.sakshay.grocermax.api;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.content.OperationApplicationException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.facebook.Session;
import com.facebook.SessionState;
import com.google.gson.Gson;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.CartProductList;
import com.sakshay.grocermax.bean.BaseResponseBean;
import com.sakshay.grocermax.bean.CartDetail;
import com.sakshay.grocermax.bean.LoginResponse;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants;
import com.sakshay.grocermax.utils.Constants;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.xml.sax.SAXException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by manish.verma on 22-09-2015.
 */
public class ConnectionCartService  extends IntentService {

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
    public static final String JSON_STRING = "json_string";
    public static final String PHOTO_PAIRS = "photoPairs";
    public static final String ISRELOAD = "isreloaded";
    public static final String ISRELOADDATA = "isreloadeddata";
    public static final String IO_EXCEPTION = "IO Exception";
    public static final String JSON_EXCEPTION = "JSON_EXCEPTION";
    public static final String EXCEPTION = "Exception";
    public static final String ACCESS_TOKEN = "AccessToken";

    public ConnectionCartService() {
        super("ConnectionService");
    }

    private String mAction;
    private String requestType;
    private String urlString;
    private HashMap<String, String> mHashMap;
    private JSONObject jsonObject;
    private String jsonString;
    private String accessToken;
    private int position;

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
        CartDetail carDetail = null;
        try {
            mAction = intent.getStringExtra(ACTION);
            requestType = intent.getStringExtra(HTTP_REQUEST_TYPE);
            urlString = intent.getStringExtra(URL);
            accessToken = intent.getStringExtra(ACCESS_TOKEN);
            position = intent.getIntExtra("position",0);
            if (AppConstants.DEBUG) {
                Log.i(TAG, "URL::" + urlString);
            }
            Log.e("manish1","URL:"+urlString);
            String[] url = urlString.split("products=");
             String actualUrl = url[1];
            actualUrl = java.net.URLDecoder.decode(actualUrl, "UTF-8");

            Gson gson = new Gson();
            Log.e("manish","URL:"+actualUrl);
            actualUrl = actualUrl.replace("[","");
            actualUrl = actualUrl.replace("]","");
            actualUrl = actualUrl.replace("productid","product_id");
            actualUrl = actualUrl.replace("quantity","qty");
            carDetail = gson.fromJson(actualUrl,CartDetail.class);
          Log.e("PRADEEP","obj:"+carDetail.toString());
// Bundle bun = intent.getExtras();
//			System.out.println("===bundle===="+bun);
//			if(bun != null){
//				HashMap<String, JSONObject> hashMap = (HashMap<String, JSONObject>)bun.getSerializable(JSON_OBJECT);
//				if(hashMap != null){
//					JSONObject jsonObject = (JSONObject) hashMap.get(JSON_OBJECT);
//					System.out.println("===jsonObject 11s===="+jsonObject);
//				}
//			}


            if(intent.getSerializableExtra(JSON_OBJECT) != null){
                jsonObject = (JSONObject)intent
                        .getSerializableExtra(JSON_OBJECT);
                if (AppConstants.DEBUG) {
                    Log.i(TAG, "JSON_OBJECT::" + jsonObject);
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


//			nameValuePairs = new ArrayList<NameValuePair>(hashMap.size());
//			HashMap<K, V> = new ArrayList<NameValuePair>(1);
//			nameValuePairs.add(new BasicNameValuePair("sample", "test"));
//			mHashMap = new HashMap<String, String>();
//			mHashMap.put("example1", "valuesre1");
//			mHashMap.put("example2", "valuesre2");
//			mHashMap.put("example3", "valuesre3");
//			mHashMap.put("example4", "valuesre4");

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
            Log.e(TAG, "ERROR::" + e.getMessage());
            if(carDetail!=null) {
                BaseActivity.addToStack(carDetail);
                Log.e("inside Exception","exc"+carDetail.getPrice());
            }
            new GrocermaxBaseException("ConnectionService","onHandleIntent",e.getMessage(),GrocermaxBaseException.EXCEPTION,response_str);
            e.printStackTrace();
        }
        finally{
            if(mAction.equals(MyReceiverActions.LOGIN) || mAction.equals(MyReceiverActions.ADD_TO_CART))
            {
                if(MySharedPrefs.INSTANCE.getFacebookId()!=null)
                {
                    Session session = getSession();
                    if (!session.isClosed()) {
                        MySharedPrefs.INSTANCE.clearAllData();
                        session.closeAndClearTokenInformation();
                    }
                }
            }
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
                httpPost.setHeader("Content-Type", "application/json");
                if (accessToken != null) {
                    httpPost.setHeader("AccessToken", "" + accessToken);
                }
                // IF Required.
                // httpPost.setHeader("Authorization", "Basic " +
                // Base64.NO_WRAP));


                if (nameValuePairs != null) {
                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                } else if (jsonString != null) {
                    httpPost.setEntity(new StringEntity(jsonString));
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
                if(urlString.contains("?")) {
                    urlString += "&version=1.0";
                }else{
                    urlString += "?version=1.0";
                }
                HttpGet httpGet = new HttpGet(urlString);
                if (AppConstants.DEBUG) {
                    Log.i(TAG, "URL:::" + urlString);
                }

//				HttpParams httpParameters = new BasicHttpParams();
//				int timeoutConnection = 3000;
//				HttpConnectionParams.setConnectionTimeout(httpParameters, timeoutConnection);
//				int timeoutSocket = 5000;
//				HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

                httpGet.setHeader("Content-Type", "application/json");
                if (accessToken != null) {
                    httpGet.setHeader("AccessToken", "" + accessToken);
                }
                // IF Required.
                // httpGet.setHeader("Authorization", "Basic " +
                // Base64.NO_WRAP));
                response = client.execute(httpGet);
            }
        }catch (Exception e) {
            new GrocermaxBaseException("ConnectionService","processRequest",e.getMessage(),GrocermaxBaseException.EXCEPTION, EntityUtils.toString(response.getEntity()));
            e.printStackTrace();
        }

        HttpEntity resEntity = response.getEntity();
        return EntityUtils.toString(resEntity);
    }

    private String processMultipartRequest(HashMap<String, String> photoUris,
                                           HashMap<String, String> hashMap, HttpClient client, String urlString)
            throws ParseException, IOException {

        MultipartEntity multiPartEntity = new MultipartEntity(
                HttpMultipartMode.BROWSER_COMPATIBLE);
        if (photoUris != null) {
            for (Map.Entry<String, String> entry : photoUris.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                Bitmap bm = BitmapFactory.decodeFile(value);
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 75, bos);
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
    }

    private void parseData(String response, int type, Bundle bundle)
            throws SAXException, ParserConfigurationException, IOException,
            JSONException, RemoteException, OperationApplicationException {

//		try {

        switch (type) {
            case ConnectionServiceParser.MyParserType.LOGIN:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseLoginResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.REGISTRATION:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.OTP_SUCCESSFULL:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseOTPResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.FORGOT_PWD:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.EDIT_PROFILE:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;

            case ConnectionServiceParser.MyParserType.CATEGORY_LIST:
                if (AppConstants.DEBUG) {
                    Log.i(TAG, "RESPONSE FOR PARSE:::" + response);
                }
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseCategoryResponse(response));
                // bundle.putSerializable(RESPONSE, (Serializable) response);
                break;
            case ConnectionServiceParser.MyParserType.CATEGORY_SUBCATEGORY_LIST:
                if (AppConstants.DEBUG) {
                    Log.i(TAG, "RESPONSE FOR PARSE:::" + response);

                }
                // bundle.putSerializable(RESPONSE,
                // (Serializable) ConnectionServiceParser
                // .parseCategoryResponse(response));
                bundle.putSerializable(RESPONSE, (Serializable) response);
                break;
            case ConnectionServiceParser.MyParserType.PRODUCT_LIST:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseProductResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.SEARCH_PRODUCT_LIST:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseProductResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.PRODUCT_CONTENT_LIST:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseProductContentResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.ADD_TO_CART:
                BaseResponseBean responseBean = ConnectionServiceParser
                        .parseSimpleResponse(response);
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
//              if(MySharedPrefs.INSTANCE.getQuoteId()==null)
//              {
                  if(responseBean.getFlag().equalsIgnoreCase("1")) {
                      if(responseBean.getQuoteId() != null) {
                          MySharedPrefs.INSTANCE.clearQuote();
                          MySharedPrefs.INSTANCE.putQuoteId(responseBean.getQuoteId());
                          System.out.println("==new quote id cart service==" + MySharedPrefs.INSTANCE.getQuoteId());
                      }
                  }
//              }

                break;
            case ConnectionServiceParser.MyParserType.DELETE_FROM_CART:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.LOCATION:

                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseLocationResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.VIEW_CART:

                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseViewCartResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.VIEW_CART_UPDATE_LOCALLY:

                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseViewCartResponseLocally(response));
                break;
            case ConnectionServiceParser.MyParserType.USER_DETAILS:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseUserDetailsResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.ORDER_HISTORY:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseOrderHistoryResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.ORDER_DETAIL:
                bundle.putSerializable(RESPONSE,
                        (Serializable) response);
                break;
            case ConnectionServiceParser.MyParserType.ADDRESS_BOOK:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseAddressBookResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.ADD_ADDRESS:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.DELETE_ADDRESS:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseSimpleResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.FINAL_CHECKOUT:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseFinalCheckoutResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.CHECKOUT_ADDRESS:
                bundle.putSerializable(RESPONSE,
                        (Serializable) ConnectionServiceParser
                                .parseCheckoutAddressResponse(response));
                break;
            case ConnectionServiceParser.MyParserType.GET_SET_ORDERSTATUS:
                bundle.putSerializable(RESPONSE, (Serializable) response);
                break;
        }

//		}
//		catch (JSONException e) {
//			bundle.putString(ERROR, JSON_EXCEPTION);
//			Log.e(TAG, "ERROR::" + e.getMessage());
//			new GrocermaxBaseException("ConnectionService","parseData",e.getMessage(),GrocermaxBaseException.JSON_EXCEPTION,response);
//			e.printStackTrace();
//		} catch (Exception e) {
//			bundle.putString(ERROR, EXCEPTION);
//			Log.e(TAG, "ERROR::" + "Unknow Error");
//			new GrocermaxBaseException("ConnectionService","parseData",e.getMessage(),GrocermaxBaseException.EXCEPTION,response);
//			e.printStackTrace();
//		}
    }

    private Session getSession() {

        // return Session.openActiveSession(getApplicationContext(), false, callback);
        return Session.getActiveSession();
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

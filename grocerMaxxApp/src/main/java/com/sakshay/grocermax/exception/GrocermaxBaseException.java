package com.sakshay.grocermax.exception;

import android.content.Context;
import android.os.AsyncTask;

import com.paymentsdk.android.MyApp;
import com.sakshay.grocermax.BaseActivity;
import com.sakshay.grocermax.LocationActivity;
import com.sakshay.grocermax.MyApplication;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Created by Abhishek on 8/5/2015.
 */
public class GrocermaxBaseException extends Exception {
    public static final String ARRAY_OUT_OF_BOUND = "ARRAY_OUT_OF_BOUND";
    public static final String NULL_POINTER =  "NULL_POINTER";
    public static final String NULL_RESPONSE =   "NULL_RESPONSE";
    public static final String REQUEST_URL_NULL =  "REQUEST_URL_NULL";
    public static final String REQUEST_NOT_FOUND =  "REQUEST_NOT_FOUND";
    public static final String WRONG_REQUEST_PARAM =  "WRONG_REQUEST_PARAM";
    public static final String EMPTY_REQUEST_PARAM =  "EMPTY_REQUEST_PARAM";
    public static final String RESPONSE_NOT_IN_PROPER_FORMAT =  "RESPONSE_NOT_IN_PROPER_FORMAT";
    public static final String ASYNC_TASK_ALREADY_USED =  "ASYNC_TASK_ALREADY_USED";
    public static final String UNSUPPORTED_ENCODING =  "UNSUPPORTED_ENCODING";
    public static final String CLIENT_PROTOCOL_EXCEPTION =  "CLIENT_PROTOCOL_EXCEPTION";
    public static final String RESPONSE_NOT_FOUND =  "RESPONSE_NOT_FOUND";
    public static final String FILE_NOT_CREATED =  "FILE_NOT_CREATED";
    public static final String REQUEST_NOT_IN_PROPER_FORMAT =  "REQUEST_NOT_IN_PROPER_FORMAT";

    public static final String IO_EXCEPTION =  "IO_EXCEPTION";
    public static final String JSON_EXCEPTION =  "JSON_EXCEPTION";
    public static final String EXCEPTION =  "EXCEPTION";
    public static final String UnsupportedEncodingException =  "UnsupportedEncodingException";
    public static final String ILLEGALSTATEEXCEPTION =  "ILLEGALSTATEEXCEPTION";
//    public GrocermaxBaseException(String strClassName, String strMethodName,String strMessage,String strErrorCode,String strLineNo) {
//        super();
//        //    new SearchLoader(this).execute(url);
//    }

    public GrocermaxBaseException(String strClassName, String strMethodName,String strMessage,String strErrorCode,String strServerResponse) {
        super();
        String strUrl = UrlsConstants.NEW_BASE_URL+"errorlog?error=";
//        String strUrl = "http://staging.grocermax.com/webservice/new_services/errorlog?error=";
//          UtilityMethods.customToast(strClassName+"=ERROR="+strMethodName, MyApplication.getInstance());
        new AppCrash(MyApplication.getInstance(),strClassName,strMethodName,strMessage,strErrorCode,strServerResponse).execute(strUrl);
//        new AppCrash(MyApplication.getInstance(),strClassName,strMethodName,strMessage,strErrorCode,strServerResponse).execute(UrlsConstants.ERROR_REPORT);
//        new AppCrash()
//               new SearchLoader(this).execute(url);
//        UtilityMethods.customToast("message", MyApplication.getInstance());
    }
}

class AppCrash extends AsyncTask<String, String, String>
{
    Context context;
    String strClassName;
    String strMethodName;
    String strMessage;
//    String strErrorCode;
    String strServerResponse;
    public AppCrash(Context mContext,String strClassName,String strMethodName,String strMessage,String strErrorCode,String strServerResponse){
        context = mContext;
        this.strClassName = strClassName;
        this.strMethodName = strMethodName;
        this.strMessage = strMessage;
//        this.strErrorCode = strErrorCode;
        this.strServerResponse = strServerResponse;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
        String strExceptionReport = params[0];
        strExceptionReport += "CLASSNAME:"+strClassName+",METHODNAME:"+strMethodName+",APPERROR:"+strMessage+",SERVERRESPONSE:"+strServerResponse;
        if(strExceptionReport.contains("?")) {
            strExceptionReport += "&version=1.0";
        }else{
            strExceptionReport += "?version=1.0";
        }
        HttpGet httpGet = new HttpGet(strExceptionReport);                        //getting URL
        httpGet.setHeader("Content-Type", "application/json");
        HttpResponse response = null;
        try {
            response = client.execute(httpGet);
            HttpEntity resEntity = response.getEntity();
            return EntityUtils.toString(resEntity);
        } catch (IOException e) {}
          catch(Exception e){}
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
    }

}
package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rgretail.grocermax.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchit-pc on 23-Jan-16.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private ArrayList<String> mData;
    Context con;


    public AutoCompleteAdapter(Context context) {
        mData = new ArrayList<String>();
        this.con=context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=inflater.inflate(R.layout.listview_element,null);
        }
        TextView name=(TextView)convertView.findViewById(R.id.name);
        name.setText(mData.get(position));

        return convertView;
    }

    @Override
    public String getItem(int index) {
        return mData.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();
                if(constraint != null) {
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                    StyleFetcher fetcher = new StyleFetcher();
                    try {
                        mData = fetcher.retrieveResults(constraint.toString(),"GET","https://maps.googleapis.com/maps/api/place/autocomplete/json?input=");
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = mData;
                    filterResults.count = mData.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                if(results != null && results.count > 0) {
                    notifyDataSetChanged();
                }
                else {
                    notifyDataSetInvalidated();
                }
            }
        };
        return myFilter;
    }


   public class StyleFetcher{
        InputStream httpResponseStream = null;
       String jsonString = "";
       public ArrayList<String> retrieveResults(String data,String method,String url){
           ArrayList<String> mList=new ArrayList<>();
           List<NameValuePair> params=null;
           try {
               // get a Http client
               DefaultHttpClient httpClient = new DefaultHttpClient();

               // If required HTTP method is POST
               if (method == "POST") {
                   // Create a Http POST object
                   HttpPost httpPost = new HttpPost(url);
                   // Encode the passed parameters into the Http request
                   httpPost.setEntity(new UrlEncodedFormEntity(params));
                   // Execute the request and fetch Http response
                   HttpResponse httpResponse = httpClient.execute(httpPost);
                   // Extract the result from the response
                   HttpEntity httpEntity = httpResponse.getEntity();
                   // Open the result as an input stream for parsing
                   httpResponseStream = httpEntity.getContent();
               }
               // Else if it is GET
               else if (method == "GET") {
                   // Format the parameters correctly for HTTP transmission
                   //String paramString = URLEncodedUtils.format(params, "utf-8");
                   // Add parameters to url in GET format
                     url += "" + URLEncoder.encode(data, "utf-8")+"&types=geocode&sensor=false&key=AIzaSyAUlshWWtBduQdUrTSA9VMThhWfGk3Hm9A";
                   // Execute the request
                   HttpGet httpGet = new HttpGet(url);
                   // Execute the request and fetch Http response
                   HttpResponse httpResponse = httpClient.execute(httpGet);
                   // Extract the result from the response
                   HttpEntity httpEntity = httpResponse.getEntity();
                   // Open the result as an input stream for parsing
                   httpResponseStream = httpEntity.getContent();
               }
               // Catch Possible Exceptions
           } catch (UnsupportedEncodingException e) {
               e.printStackTrace();
           } catch (ClientProtocolException e) {
               e.printStackTrace();
           } catch (IOException e) {
               e.printStackTrace();
           }

           try {
               // Create buffered reader for the httpResponceStream
               BufferedReader httpResponseReader = new BufferedReader(
                       new InputStreamReader(httpResponseStream, "iso-8859-1"), 8);
               // String to hold current line from httpResponseReader
               String line = null;
               // Clear jsonString
               jsonString = "";
               // While there is still more response to read
               while ((line = httpResponseReader.readLine()) != null) {
                   // Add line to jsonString
                   jsonString += (line + "\n");
               }
               // Close Response Stream
               httpResponseStream.close();
           } catch (Exception e) {
               Log.e("Buffer Error", "Error converting result " + e.toString());
           }

           try {
               // Create jsonObject from the jsonString and return it
               System.out.println("Response="+jsonString);
               JSONObject jsonData=new JSONObject(jsonString);
               JSONArray dataArray=jsonData.getJSONArray("predictions");
               for(int i=0;i<dataArray.length();i++){
                   mList.add(dataArray.getJSONObject(i).getString("description"));
               }
               return mList;
           } catch (JSONException e) {
               Log.e("JSON Parser", "Error parsing data " + e.toString());
               // Return null if in error
               return null;
           }
       }
   }
}
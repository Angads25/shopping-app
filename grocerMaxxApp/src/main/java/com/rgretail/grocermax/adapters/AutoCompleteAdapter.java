package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.utils.CustomFonts;
import com.rgretail.grocermax.utils.CustomTypefaceSpan;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

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
    private ArrayList<Product> autoSuggestList;
   //private ArrayList<String> autoSuggestList;
    Context con;


    public AutoCompleteAdapter(Context context) {
        autoSuggestList = new ArrayList<Product>();
        //autoSuggestList = new ArrayList<String>();
        this.con=context;
    }

    @Override
    public int getCount() {
        return autoSuggestList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView=inflater.inflate(R.layout.listview_element,null);
            convertView=inflater.inflate(R.layout.auto_suggest_search,null);
            holder = new ViewHolder();
            holder.name=(TextView)convertView.findViewById(R.id.product_name);
            holder.image=(ImageView)convertView.findViewById(R.id.product_image);
            holder.prod_gram_or_ml = (TextView) convertView.findViewById(R.id.product_gram_or_ml);
            holder.sale_price = (TextView) convertView.findViewById(R.id.sale_price);
            holder.amount = (TextView) convertView.findViewById(R.id.amount);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.amount.setPaintFlags(holder.amount.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        //TextView name=(TextView)convertView.findViewById(R.id.name);
        Typeface font3 = Typeface.createFromAsset(con.getAssets(), "Roboto-Regular.ttf");
        Typeface font4 =   Typeface.createFromAsset(con.getAssets(), "Rupee.ttf");

        holder.name.setText(autoSuggestList.get(position).getName());
        holder.name.setTypeface(CustomFonts.getInstance().getRobotoRegular(con));
        holder.prod_gram_or_ml.setText(autoSuggestList.get(position).getGramsORml());
        holder.prod_gram_or_ml.setTypeface(CustomFonts.getInstance().getRobotoRegular(con));
        if(autoSuggestList.get(position).getSalePrice().toString() != null) {
            SpannableStringBuilder SS = new SpannableStringBuilder("`" + autoSuggestList.get(position).getSalePrice().toString());
            SS.setSpan(new CustomTypefaceSpan("", font4), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            SS.setSpan(new CustomTypefaceSpan("", font3), 1, autoSuggestList.get(position).getSalePrice().toString().length() + 1, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.sale_price.setText(SS);
        }
        if(autoSuggestList.get(position).getPrice().toString() != null) {
            SpannableStringBuilder SS = new SpannableStringBuilder("`" + autoSuggestList.get(position).getPrice().toString());
            SS.setSpan(new CustomTypefaceSpan("", font4), 0, 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            SS.setSpan(new CustomTypefaceSpan("", font3), 1, autoSuggestList.get(position).getPrice().toString().length() - (autoSuggestList.get(position).getPrice().toString().length() - 1), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
            holder.amount.setText(SS);
        }
        //name.setText(autoSuggestList.get(position).split("@@@@")[0]);

        ImageLoader.getInstance().displayImage(autoSuggestList.get(position).getImage(),
                holder.image, ((BaseActivity) con).baseImageoptions);



        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) con).showDialog();
                String url = UrlsConstants.PRODUCT_DETAIL_URL + autoSuggestList.get(position).getProductid();
                UtilityMethods.hideKeyboardFromContext(con);
                //        String url = UrlsConstants.PRODUCT_DETAIL_URL + autoSuggestList.get(position).split("@@@@")[1];
                ((BaseActivity) con).myApi.reqProductDetailFromNotification(url);
            }
        });


        return convertView;
    }


    static class ViewHolder {
        private TextView name;
        private TextView prod_gram_or_ml;
        private ImageView image;
        private TextView sale_price;
        private TextView amount;
    }

    @Override
    public Object getItem(int index) {
        return autoSuggestList.get(index);
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
                        autoSuggestList = fetcher.retrieveResults(constraint.toString(),"GET",UrlsConstants.SEARCH_PRODUCT);
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = autoSuggestList;
                    filterResults.count = autoSuggestList.size();
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
       public ArrayList<Product> retrieveResults(String data,String method,String url){
           ArrayList<Product> mList=new ArrayList<>();
           //ArrayList<String> mList=new ArrayList<>();
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
                     url += "" + URLEncoder.encode(data, "utf-8");
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
               JSONArray dataArray=jsonData.getJSONArray("Product");
               for(int i=0;i<dataArray.length();i++){
                   Product p=new Product(dataArray.getJSONObject(i).getString("Name"));
                   p.setProductid(dataArray.getJSONObject(i).getString("productid"));
                   p.setImage(dataArray.getJSONObject(i).getString("Image"));
                   p.setGramsORml(dataArray.getJSONObject(i).getString("p_pack"));
                   p.setSalePrice(dataArray.getJSONObject(i).getString("sale_price"));
                   p.setPrice(dataArray.getJSONObject(i).getString("Price"));
                   mList.add(p);
                   //mList.add(dataArray.getJSONObject(i).getString("Name")+"@@@@"+dataArray.getJSONObject(i).getString("productid"));
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
package com.rgretail.grocermax.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.utils.DataHandler;

import java.util.ArrayList;

/**
 * Created by anchit-pc on 23-Jan-16.
 */
public class AutoCompleteAdapter extends BaseAdapter implements Filterable {
    private ArrayList<Product> autoSuggestList;
    private ArrayList<Product> manuallSuggestList;
    private ArrayList<Product> totalSuggestionList=new ArrayList<>();
    Context con;
    boolean isDataSetChanged;
    DataHandler db;
    private ArrayList<Product> suggestions;


    public AutoCompleteAdapter(Context context,ArrayList<Product> autoSuggestList) {
       // this.totalSuggestionList = autoSuggestList;
        this.con=context;
        isDataSetChanged=false;
        db=new DataHandler(con);
        db.Open();
        totalSuggestionList=db.getAllSearchKeys();
        db.close();
      //  this.totalSuggestionList.addAll(manuallSuggestList);
       // this.totalSuggestionList.addAll(autoSuggestList);
        this.suggestions = new ArrayList<Product>(totalSuggestionList);


    }

    @Override
    public int getCount() {
        try {
            return totalSuggestionList.size();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }

    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        final ViewHolder holder;
        if(convertView==null){
            LayoutInflater inflater=(LayoutInflater)con.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView=inflater.inflate(R.layout.listview_element,null);
            convertView=inflater.inflate(R.layout.auto_suggest_search,null);
            holder = new ViewHolder();
            holder.name=(TextView)convertView.findViewById(R.id.product_name);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        Typeface font3 = Typeface.createFromAsset(con.getAssets(), "Roboto-Regular.ttf");
        Typeface font4 =   Typeface.createFromAsset(con.getAssets(), "Rupee.ttf");

        holder.name.setText(totalSuggestionList.get(position).getName());
        holder.name.setTypeface(font3);


        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((BaseActivity) con).serachFromDropDown(holder.name.getText().toString());
            }
        });

        return convertView;
    }


    static class ViewHolder {
        private TextView name;

    }

    @Override
    public Object getItem(int index) {
        return totalSuggestionList.get(index);
    }

    @Override
    public Filter getFilter() {
        Filter myFilter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults filterResults = new FilterResults();

                if (suggestions == null) {
                    suggestions = new ArrayList<Product>(totalSuggestionList);
                }


                if (constraint == null || constraint.length() == 0) {
                        ArrayList<Product> list = new ArrayList<Product>(suggestions);
                        filterResults.values = list;
                        filterResults.count = list.size();
                } else {
                    final String prefixString = constraint.toString().toLowerCase();

                   // ArrayList<Product> values = suggestions;
                    int count = suggestions.size();

                    ArrayList<Product> newValues = new ArrayList<Product>(count);

                    for (int i = 0; i < count; i++) {
                        Product item = suggestions.get(i);
                        if (item.getName().toLowerCase().startsWith(prefixString)) {
                            newValues.add(item);
                        }
                    }
                    filterResults.values = newValues;
                    filterResults.count = newValues.size();
                }





               /* if(constraint != null) {
                    suggestions.clear();
                    // A class that queries a web API, parses the data and returns an ArrayList<Style>
                  //  StyleFetcher fetcher = new StyleFetcher();
                    try {
                        for (Product product : totalSuggestionList) {
                            if(product.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
                                suggestions.add(product);
                            }
                        }
                    }
                    catch(Exception e) {
                        Log.e("myException", e.getMessage());
                    }
                    // Now assign the values and count to the FilterResults object
                    filterResults.values = suggestions;
                    filterResults.count = suggestions.size();
                }*/
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence contraint, FilterResults results) {
                /*if (isDataSetChanged) {
                    if(results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }*/

                if(results.values!=null){
                    totalSuggestionList = (ArrayList<Product>) results.values;
                }else{
                    totalSuggestionList = new ArrayList<Product>();
                }
                if (results.count > 0) {
                    notifyDataSetChanged();
                } else {
                    notifyDataSetInvalidated();
                }


            }
        };
        return myFilter;
    }


   /*public class StyleFetcher{
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

                    // url += "" + URLEncoder.encode(data, "utf-8");

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
               jsonString=jsonString.replace("<pre>","");
               jsonString=jsonString.replace("</pre>","");
               JSONObject jsonData=new JSONObject(jsonString);
               jsonData=jsonData.getJSONObject("Result");
               JSONArray dataArray=jsonData.getJSONArray("data");
               for(int i=0;i<dataArray.length();i++){
                   Product p=new Product(dataArray.getJSONObject(i).getString("name1"));
                   p.setProductid(dataArray.getJSONObject(i).getString("id"));
                   mList.add(p);
               }
               return mList;
           } catch (JSONException e) {
               Log.e("JSON Parser", "Error parsing data " + e.toString());
               // Return null if in error
               return null;
           }
       }
   }*/
}
package com.sakshay.grocermax;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sakshay.grocermax.adapters.CategorySubcategoryBean;
import com.sakshay.grocermax.adapters.ProductListAdapter;
import com.sakshay.grocermax.bean.Product;
import com.sakshay.grocermax.bean.ProductListBean;
import com.sakshay.grocermax.exception.GrocermaxBaseException;
import com.sakshay.grocermax.preference.MySharedPrefs;
import com.sakshay.grocermax.utils.AppConstants.ToastConstant;
import com.sakshay.grocermax.utils.MyHttpUtils;
import com.sakshay.grocermax.utils.UrlsConstants;
import com.sakshay.grocermax.utils.UtilityMethods;

public final class ProductListFragments extends Fragment implements OnScrollListener {
	public static final String TAG = "ConnectionService";
	int currentFirstVisibleItem = 0;
	int currentVisibleItemCount = 10;
	int totalItemCount = 10;
	int currentScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
	boolean isLoading = false;
	int itemPerPage = 10;
	boolean hasMoreItem = true;
	private ListView mList;
	private ProductListAdapter mAdapter;
	private ProductListBean productListBean;
	private int pageNo = 1;
	private String cat_id = "";
	private List<Product> product_list;
	private CategoryTabs categoryTabs;
	private View footerView;
	private LinearLayout main_lay;
	private ProgressBar progressBar;
	public static TextView tvGlobalUpdateProductList;        //will update product listing screen if on product description page quantity has been changed and user press back btn OR cross btn
	public static ImageView imgAddedProductCount;           //will update product listing screen if on product description page quantity has been changed and user press back btn OR cross btn
    public static ProductListFragments newInstance(CategorySubcategoryBean categorySubcategoryBean) {
    	ProductListFragments fragment = new ProductListFragments();
    	fragment.cat_id = categorySubcategoryBean.getCategoryId();
        return fragment;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
    	categoryTabs = ((CategoryTabs)getActivity());
    	
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
			if ((savedInstanceState != null) && savedInstanceState.containsKey("ProductList")) {
				productListBean = (ProductListBean) savedInstanceState
						.getSerializable("ProductList");
				cat_id = savedInstanceState.getString("cat_id");
			}
			}catch(Exception e){
				new GrocermaxBaseException("ProductListFragments","onStart",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    	View view = inflater.inflate(R.layout.fragment_categoty_list, container, false);
    	try{
    	main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
    	progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    	
    	main_lay.setVisibility(View.GONE);
    	progressBar.setVisibility(View.VISIBLE);
    	
    	mList = (ListView) view.findViewById(R.id.category_list);
    	footerView = (LinearLayout) view.findViewById(R.id.load_more_progressBar);
    	
		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long arg3) {
               if(CategoryTabs.clickStatus==0)
               {
            	CategoryTabs.clickStatus=1;
				if(productListBean!=null)
				{
				  MySharedPrefs.INSTANCE.putItemQuantity(productListBean.getProduct().get(position).getQuantity());
				  categoryTabs.product = productListBean.getProduct().get(position);
				  categoryTabs.showDialog();
				  
				  tvGlobalUpdateProductList = (TextView) view.findViewById(R.id.added_product_count);
				  imgAddedProductCount = (ImageView) view.findViewById(R.id.img_added_product_count);

				  String url = UrlsConstants.PRODUCT_DETAIL_URL + categoryTabs.product.getProductid();
				  categoryTabs.myApi.reqProductContentList(url);
				}
               }
			}
		});
		mList.setOnScrollListener(this);

		CallAPI callapi=new CallAPI();
		CategoryTabs.asyncTasks.add(callapi);
		callapi.execute(UrlsConstants.PRODUCT_LIST_URL + cat_id);
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

        return view;
    }
    
	/*@TargetApi(Build.VERSION_CODES.HONEYCOMB) // API 11
    void startMyTask(AsyncTask<String, String, String> asyncTask,String params) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            asyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
        else
            asyncTask.execute(params);
    }*/
    
    public class CallAPI extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
        	HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
			String strURL = params[0];
			if(strURL.contains("?")) {
				strURL += "&version=1.0";
			}else{
				strURL += "?version=1.0";
			}
        	HttpGet httpGet = new HttpGet(strURL);
			httpGet.setHeader("Content-Type", "application/json");
			HttpResponse response = null;
			try {
				response = client.execute(httpGet);
				HttpEntity resEntity = response.getEntity();
				return EntityUtils.toString(resEntity);
			} catch (ClientProtocolException e) {
				new GrocermaxBaseException("ProductListFragments","doInBackground",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			} catch (IOException e) {
				new GrocermaxBaseException("ProductListFragments","doInBackground",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
			}
			return null;
        }
     
        protected void onPostExecute(String result) {
		  try{
        	main_lay.setVisibility(View.VISIBLE);
        	progressBar.setVisibility(View.GONE);
        	ProductListBean listBean = null;
        	if (result!=null) {
        		Log.i(TAG, "RESPONSE:::" + result);
        			Gson gson = new Gson();
        			listBean = gson.fromJson(result, ProductListBean.class);

        		if (listBean!=null && listBean.getFlag().equalsIgnoreCase("1")) {
    				if (listBean.getProduct().size() < itemPerPage) {
    					hasMoreItem = false;
    				} else {
    					hasMoreItem = true;
    				}
    				footerView.setVisibility(View.GONE);
    				if (mAdapter != null) {

    				    for(int i=0;i<listBean.getProduct().size();i++)                //new 10 records
    				    	listBean.getProduct().get(i).setQuantity("1");          
    					product_list.addAll(listBean.getProduct());                    //added more records in product_list                                      
    					productListBean.setProduct(product_list);
    					mAdapter.updateList(product_list);
					}else{
						productListBean = listBean;
						product_list = productListBean.getProduct();
						mAdapter = new ProductListAdapter(categoryTabs, product_list);
						mList.setAdapter(mAdapter);
					}
	    			} else
	    			{
	    				product_list=new ArrayList<Product>();
	    				Product product=new Product("No product found for this category");
	    				product_list.add(product);
	    				mAdapter = new ProductListAdapter(categoryTabs, product_list);
						mList.setAdapter(mAdapter);
	    			}
			}
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onPostExecute",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
        }

        
    } 
    
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		this.currentFirstVisibleItem = firstVisibleItem;
		this.currentVisibleItemCount = visibleItemCount;
		this.totalItemCount = totalItemCount;
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		this.currentScrollState = scrollState;
		this.isScrollCompleted();
	}

	private void isScrollCompleted() {
		if (this.currentVisibleItemCount + this.currentFirstVisibleItem >= totalItemCount && this.currentScrollState == SCROLL_STATE_IDLE) {
			/***
			 * In this way I detect if there's been a scroll which has completed
			 ***/
			/*** do the work for load more date! ***/
			if (!isLoading) {
				//isLoading = true;
				loadMoreData();
			}
		}
	}

	private void loadMoreData() {
		try{
		if (UtilityMethods.isInternetAvailable(categoryTabs.mContext)) {
			if (hasMoreItem) {
				// mList.addFooterView(footerView);
				pageNo++;
				String url;
				footerView.setVisibility(View.VISIBLE);
					url = UrlsConstants.PRODUCT_LIST_URL + cat_id + "&page="
							+ pageNo;

					new CallAPI().execute(url);
					//callApi.execute(url);
					//startMyTask(callApi, url);
			} else {
//				Toast.makeText(categoryTabs.mContext, ToastConstant.listFull, Toast.LENGTH_SHORT).show();
				UtilityMethods.customToast(ToastConstant.listFull, categoryTabs.mContext);
			}
		} else {
//			Toast.makeText(categoryTabs.mContext,ToastConstant.msgNoInternet,
//					Toast.LENGTH_LONG).show();
			UtilityMethods.customToast(ToastConstant.msgNoInternet, categoryTabs.mContext);
		}
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","loadMoreData",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
		try {
			outState.putSerializable("ProductList", productListBean);
			outState.putString("cat_id", cat_id);
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onSaveInstanceState",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
    
   
	
	
	
	
    
}

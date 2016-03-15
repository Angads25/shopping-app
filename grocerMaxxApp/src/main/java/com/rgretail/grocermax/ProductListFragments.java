package com.rgretail.grocermax;

import android.content.Intent;
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
import com.melnykov.fab.FloatingActionButton;
import com.rgretail.grocermax.adapters.ProductListAdapter;
import com.rgretail.grocermax.bean.CategoriesProducts;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.AppConstants;
import com.rgretail.grocermax.utils.MyHttpUtils;
import com.rgretail.grocermax.utils.UrlsConstants;
import com.rgretail.grocermax.utils.UtilityMethods;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class ProductListFragments extends Fragment implements OnScrollListener
{
	public static final String TAG = "ConnectionService";
	int currentFirstVisibleItem = 0;
	int currentVisibleItemCount = 10;
	int totalItemCount = 10;
	int currentScrollState = OnScrollListener.SCROLL_STATE_IDLE;
	boolean isLoading = false;
	int itemPerPage = 10;
	boolean hasMoreItem = true;
	private ListView mList;
	private ProductListAdapter mAdapter;
	private ProductListBean productListBean;
	private int pageNo = 1;
	private String cat_id = "";
	private String cat_name = "";
	private List<Product> product_list;
	private CategoryTabs categoryTabs;
	private View footerView;
	private LinearLayout main_lay;
	private ProgressBar progressBar;
	public static TextView tvGlobalUpdateProductList;        //will update product listing screen if on product description page quantity has been changed and user press back btn OR cross btn
	public static ImageView imgAddedProductCount;           //will update product listing screen if on product description page quantity has been changed and user press back btn OR cross btn

	private CategoriesProducts categoryData;
	private String SCREENNAME = "ProductListFragments-";

     /*for sending category name to GA when Tab clicked*/
    public static String categogy_name;

//    public static ProductListFragments newInstance(CategorySubcategoryBean categorySubcategoryBean) {
//    	ProductListFragments fragment = new ProductListFragments();
//    	fragment.cat_id = categorySubcategoryBean.getCategoryId();
//        return fragment;
//    }

	//	ArrayList<CategoriesProducts> alCategory = null;
	public static ProductListFragments newInstance(CategoriesProducts categorie) {
		ProductListFragments fragment = new ProductListFragments();
//		fragment.cat_id = alCategory.getCategoryId();
//		alCategory.get(0).getItems().get(0).get
//		List<Product> product = alCategory.get(0).getItems();
//		fragment.alCategory = alCategory.get(0).getItems();

			fragment.cat_id = categorie.getCategory_id();
			fragment.categoryData = categorie;
			fragment.cat_name = categorie.getCategory_name();
		try {
			fragment.itemPerPage = categorie.getTotalCount();
		}catch(Exception e){
			fragment.itemPerPage = 10;
		}
		return fragment;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		try{
			categoryTabs = ((CategoryTabs)getActivity());
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onActivityCreated",e.getMessage(), GrocermaxBaseException.EXCEPTION,"noresult");
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			if ((savedInstanceState != null) && savedInstanceState.containsKey("ProductList")) {
				productListBean = (ProductListBean) savedInstanceState.getSerializable("ProductList");
				cat_id = savedInstanceState.getString("cat_id");
            }
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onCreate",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
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
						if (productListBean.getProduct().get(position).getPromotionLevel() != null) {
							try {
								//UtilityMethods.clickCapture(HomeScreen.mContext, "", "", "", "", SCREENNAME+AppConstants.GA_EVENT_OFFER_IN_CATEGORY);
							} catch (Exception e) {
							}
						}
					}
				}
			});
			mList.setOnScrollListener(this);

			FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
			fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), ProductSorting.class);
					intent.putExtra("comming_from","category");
					startActivityForResult(intent, 0);
				}
			});



//		CallAPI callapi=new CallAPI();
//		CategoryTabs.asyncTasks.add(callapi);
//		callapi.execute(UrlsConstants.PRODUCT_LIST_URL + cat_id);
			makeUI(CategoryTabs.sort_condition);
			return view;
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

		return null;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			String condition=data.getStringExtra("condition");
			System.out.println("condition="+condition);
			CategoryTabs.sort_condition=condition;
			makeUI(CategoryTabs.sort_condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	public void descriptionCall(){
//		if(CategoryTabs.clickStatus==0)
//		{
//			CategoryTabs.clickStatus=1;
//			if(productListBean!=null)
////			{
////				MySharedPrefs.INSTANCE.putItemQuantity(productListBean.getProduct().get(position).getQuantity());
////				categoryTabs.product = productListBean.getProduct().get(position);
////				categoryTabs.showDialog();
////
////				tvGlobalUpdateProductList = (TextView) view.findViewById(R.id.added_product_count);
////				imgAddedProductCount = (ImageView) view.findViewById(R.id.img_added_product_count);
////
////				String url = UrlsConstants.PRODUCT_DETAIL_URL + categoryTabs.product.getProductid();
////				categoryTabs.myApi.reqProductContentList(url);
////			}
//		}
//	}

	public void makeUI(String condition){
		try{
			System.out.println("make ui condition="+condition);
			main_lay.setVisibility(View.VISIBLE);
			progressBar.setVisibility(View.GONE);
			ProductListBean listBean = null;
//			if (result!=null) {
//				Log.i(TAG, "RESPONSE:::" + result);
//			JSONArray products = new JSONArray();
//			int size= category.getItems().size();
//			System.out.println("====size si=="+size);
//			for(int i=0;i<category.getItems().size();i++)
//			{
//				Product pro = category.getItems().get(i);
			listBean = new ProductListBean();
			List<Product> lis = new ArrayList<Product>();
			lis = categoryData.getItems();


            /*get the name of category when tab item is clicked*/
            categogy_name=categoryData.getCategory_name();
            /*----------------------------*/

			listBean.setProduct(lis);
			////jsonarray parse in listbean////////////////
//			JSONArray productsss = new JSONArray();
//			for(int i=0;i<category.getItems().size();i++){
//				Product pro = category.getItems().get(i);
//				JSONObject jsonObject = new JSONObject();
//				jsonObject.put("productid",pro.getProductid());
//				jsonObject.put("Name",pro.getProductName());
//				jsonObject.put("p_brand",pro.getBrand());
//				jsonObject.put("p_name",pro.getName());
//				jsonObject.put("p_pack",pro.getGramsORml());
//				jsonObject.put("promotion_level",pro.getPromotionLevel());
//				jsonObject.put("Price",pro.getPrice());
//				jsonObject.put("sale_price",pro.getSalePrice());
//				jsonObject.put("Image",pro.getImage());
//				jsonObject.put("Status",pro.getStatus());
//
//				productsss.put(jsonObject);
//			}
//			JSONObject json = new JSONObject();
//			json.put("Product", productsss);
//			System.out.print("2222===="+json);
//			Gson gson = new Gson();
//			listBean = gson.fromJson(String.valueOf(json), ProductListBean.class);
			////jsonarray parse in listbean////////////////

//			Gson gson = new Gson();
//			listBean = gson.fromJson(String.valueOf(products), ProductListBean.class);

//			List<Product> listP = category.getItems();
//			listP.size()
//			listP.get(0).getBrand()

//			listBean = gson.fromJson(category.getItems().toString(), ProductListBean.class);

//				if (listBean!=null && listBean.getFlag().equalsIgnoreCase("1")) {
			if (listBean!=null ) {
//			if (category.getItems() != null ) {
				if(cat_name.equalsIgnoreCase("All")){
					hasMoreItem = false;
				}else {
					if(hasMoreItem) {                                           //it setted false(extra condition) in postexecute (but set true again b/c calculates initial 10 records with below limit))
						if (listBean.getProduct().size() >= itemPerPage) {
							hasMoreItem = false;
						} else {
							hasMoreItem = true;
						}
					}
				}
				footerView.setVisibility(View.GONE);
				if (mAdapter != null) {

					for(int i=0;i<listBean.getProduct().size();i++)                //new 10 records
						listBean.getProduct().get(i).setQuantity("1");
					product_list = new ArrayList<Product>();
					product_list.addAll(listBean.getProduct());                    //added more records in product_list
//					product_list.addAll(category.getItems());                    //added more records in product_list

					mAdapter = new ProductListAdapter(getActivity(), product_list);
					mList.setAdapter(mAdapter);

//					productListBean.setProduct(product_list);
//				mAdapter.updateList(product_list);
				}else{
					productListBean = listBean;
					product_list = productListBean.getProduct();
//					product_list = category.getItems();
//					mAdapter = new ProductListAdapter(categoryTabs, product_list);
					mAdapter = new ProductListAdapter(getActivity(), product_list);
					mList.setAdapter(mAdapter);
				}
			} else
			{
				product_list=new ArrayList<Product>();
				Product product=new Product("No product found for this category");
				product_list.add(product);
//				mAdapter = new ProductListAdapter(categoryTabs, product_list);
				mAdapter = new ProductListAdapter(getActivity(), product_list);

				mList.setAdapter(mAdapter);
			}


			/*---------------For sorting the product list------------------------------------------*/

			if (product_list!=null) {
				if(condition.equals("price_low_to_hign")){
                    Collections.sort(product_list, new Comparator<Product>() {

                        public int compare(Product p1, Product p2) {
                            float sale_price1 = Float.parseFloat(p1.getSalePrice());
                            float sale_price2 = Float.parseFloat(p2.getSalePrice());

                             /*For ascending order*/
							return Float.compare(sale_price1, sale_price2);
                        }
                    });
                }else if(condition.equals("a_to_z")){

                        Collections.sort(product_list, new Comparator<Product>() {

							public int compare(Product p1, Product p2) {
								return p1.getProductName().compareTo(p2.getProductName());
							}
						});
                }else if(condition.equals("z_to_a")){

					Collections.sort(product_list, new Comparator<Product>() {

						public int compare(Product p1, Product p2) {
							return p2.getProductName().compareTo(p1.getProductName());
						}
					});
				}else if(condition.equals("price_hign_to_low")){
					Collections.sort(product_list, new Comparator<Product>() {

						public int compare(Product p1, Product p2) {
							float sale_price1 = Float.parseFloat(p1.getSalePrice());
							float sale_price2 = Float.parseFloat(p2.getSalePrice());

                             /*For desending order*/
							return Float.compare(sale_price2, sale_price1);
						}
					});
				}else if(condition.equals("popularity")){
					Collections.sort(product_list, new Comparator<Product>() {

						public int compare(Product p1, Product p2) {
							int rank1 = Integer.parseInt(p1.getRank());
							int rank2 = Integer.parseInt(p2.getRank());

                             /*For desending order*/
							return (int) (rank2-rank1);
						}
					});
				}else if(condition.equals("discount")){
					Collections.sort(product_list, new Comparator<Product>() {

						public int compare(Product p1, Product p2) {
							float discount1 = Float.parseFloat(p1.getDiscount());
							float discount2 = Float.parseFloat(p2.getDiscount());

                             /*For desending order*/
							return Float.compare(discount2, discount1);
						}
					});
				}
				//productListBean.setProduct(product_list);
				//mAdapter.updateList(product_list);
				return;
			}
/*---------------------------------------------------------------------------------------------*/

//			}
		}catch(Exception e){
			e.printStackTrace();
			new GrocermaxBaseException("ProductListFragments","onPostExecute",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
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
			try {
				HttpClient client = MyHttpUtils.INSTANCE.getHttpClient();
				String strURL = params[0];
//				if(strURL.contains("?")) {
//					strURL += "&version=1.0";
//				}else{
//					strURL += "?version=1.0";
//				}
				HttpGet httpGet = new HttpGet(strURL);
				httpGet.setHeader("device", getResources().getString(R.string.app_device));
				httpGet.setHeader("version", getResources().getString(R.string.app_version));
				if(MySharedPrefs.INSTANCE.getSelectedStateId() != null) {
					httpGet.setHeader("storeid", MySharedPrefs.INSTANCE.getSelectedStoreId());
				}


//				httpGet.setHeader("Content-Type", "application/json");
				HttpResponse response = null;

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

						if(cat_name.equalsIgnoreCase("All")){
							hasMoreItem = false;
						}
						else {
							if(hasMoreItem) {
//								if (listBean.getProduct().size() < itemPerPage) {         //remove
								if(mList != null) {                                   //extra
									if (listBean.getProduct().size() + mList.getAdapter().getCount() >= itemPerPage) {       //extra
										hasMoreItem = false;
									} else {
										hasMoreItem = true;
									}
								}
							}
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

	public void onScroll(AbsListView view, int firstVisibleItem,int visibleItemCount, int totalItemCount) {
		try {
			this.currentFirstVisibleItem = firstVisibleItem;
			this.currentVisibleItemCount = visibleItemCount;
			this.totalItemCount = totalItemCount;
		}catch(Exception e){
			new GrocermaxBaseException("ProductListFragments","onScroll",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
	}

	public void onScrollStateChanged(AbsListView view, int scrollState) {
		try{
			this.currentScrollState = scrollState;
			this.isScrollCompleted();
		}catch(Exception e) {
			new GrocermaxBaseException("ProductListFragments", "onScrollStateChanged", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
		}
	}

	private void isScrollCompleted() {
		try {
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
		}catch(Exception e) {
			new GrocermaxBaseException("ProductListFragments", "isScrollCompleted", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
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

					System.out.println("========URL more pages==============="+url);
					new CallAPI().execute(url);
					//callApi.execute(url);
					//startMyTask(callApi, url);
					//try{UtilityMethods.clickCapture(getActivity(),"","","","",SCREENNAME+AppConstants.CATEGORY_SCROLL_BROWSING_CATEGORIES);}catch(Exception e){}

				} else {
//				Toast.makeText(categoryTabs.mContext, ToastConstant.listFull, Toast.LENGTH_SHORT).show();
					UtilityMethods.customToast(AppConstants.ToastConstant.listFull, categoryTabs.mContext);
				}
			} else {
//			Toast.makeText(categoryTabs.mContext,ToastConstant.msgNoInternet,
//					Toast.LENGTH_LONG).show();
				UtilityMethods.customToast(AppConstants.ToastConstant.msgNoInternet, categoryTabs.mContext);
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

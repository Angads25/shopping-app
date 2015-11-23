package com.rgretail.grocermax;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.google.gson.Gson;
import com.rgretail.grocermax.adapters.ProductListAdapter;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;


public final class SearchProductFragments extends Fragment implements OnScrollListener {
	public static final String TAG = "ConnectionService";
//	int currentFirstVisibleItem = 0;
//	int currentVisibleItemCount = 10;
//	int totalItemCount = 10;
	int currentScrollState = AbsListView.OnScrollListener.SCROLL_STATE_IDLE;
	boolean isLoading = false;
//	int itemPerPage = 10;
//	boolean hasMoreItem = true;
	private ListView mList;

	private ProductListAdapter mAdapter;
	private ProductListBean productListBean;
//	private int pageNo = 1;
//	private String cat_id = "";
//	HashMap<String, JSONArray> valuePairs;
//	JSONArray jsonArray;
	JSONObject jsonObject;
	private List<Product> product_list;
//	private CategoryTabs categoryTabs;
	private SearchTabs searchTabs;
//	private View footerView;
//	private LinearLayout main_lay;
	private ProgressBar progressBar;
	public static TextView tvGlobalUpdateProductList;


	
//    public static ProductListFragments newInstance(CategorySubcategoryBean categorySubcategoryBean) {
//    	ProductListFragments fragment = new ProductListFragments();
////    	fragment.cat_id = categorySubcategoryBean.getCategoryId();
//        return fragment;
//    }

//	 public static SearchProductFragments newInstance(JSONObject jsonObject) {
	public static SearchProductFragments newInstance(String str) {
		try {


			JSONObject jsonObject = new JSONObject(str);
			SearchProductFragments fragment = new SearchProductFragments();
//	    	fragment.cat_id = categorySubcategoryBean.getCategoryId();
//	    	fragment.valuePairs = valuePairs;
//		    fragment.jsonArray = jsonArray;
			fragment.jsonObject = jsonObject;
			return fragment;
		}catch(Exception e){
			new GrocermaxBaseException("SearchProductFragments","newInstance",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}


	        return null;
	    }

	
	
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
    	super.onActivityCreated(savedInstanceState);
		try{
//    	categoryTabs = ((CategoryTabs)getActivity());
    	searchTabs = ((SearchTabs)getActivity());
		}catch(Exception e){
			new GrocermaxBaseException("SearchProductFragments","onActivityCreated",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
    }
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
//		if ((savedInstanceState != null) && savedInstanceState.containsKey("ProductList")) {
//			productListBean = (ProductListBean) savedInstanceState
//					.getSerializable("ProductList");
//				cat_id = savedInstanceState.getString("cat_id");
//		}
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    	try{
		View view = inflater.inflate(R.layout.fragment_categoty_list, container, false);
//    	main_lay = (LinearLayout) view.findViewById(R.id.main_lay);
    	progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);
    	
//    	main_lay.setVisibility(View.GONE);
//    	progressBar.setVisibility(View.VISIBLE);
    	
    	mList = (ListView) view.findViewById(R.id.category_list);
//    	footerView = (LinearLayout) view.findViewById(R.id.load_more_progressBar);

		mList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,int position, long arg3) {
//               if(CategoryTabs.clickStatus==0)
//               {
//            	CategoryTabs.clickStatus=1;
				if(productListBean!=null)
				{
				  MySharedPrefs.INSTANCE.putItemQuantity(productListBean.getProduct().get(position).getQuantity());
				  searchTabs.product = productListBean.getProduct().get(position);
				  searchTabs.showDialog();
				  
				  tvGlobalUpdateProductList = (TextView) view.findViewById(R.id.added_product_count);

				  String url = UrlsConstants.PRODUCT_DETAIL_URL + searchTabs.product.getProductid();
				  searchTabs.myApi.reqProductContentList(url);
				}
//               }
			}
		});
		mList.setOnScrollListener(this);

		makeUI();
		return view;
		}catch(Exception e){
			new GrocermaxBaseException("SearchProductFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return  null;
    }
    
    private void makeUI() {
		try{
//		main_lay.setVisibility(View.VISIBLE);
		progressBar.setVisibility(View.GONE);
		ProductListBean listBean = null;
		Gson gson = new Gson();
		listBean = gson.fromJson(jsonObject.toString(), ProductListBean.class);

		if (listBean != null)
		{
			if (listBean.getProduct().size() > 0)
			{
//				footerView.setVisibility(View.GONE);
				productListBean = listBean;
				product_list = productListBean.getProduct();
	//			mAdapter = new ProductListAdapter(categoryTabs, product_list);
	//			mAdapter = new ProductListAdapter(searchTabs, product_list);
				mAdapter = new ProductListAdapter(getActivity(), product_list);
				mList.setAdapter(mAdapter);
		    }
			else {
				product_list = new ArrayList<Product>();
				Product product = new Product("No product found for this category");
				product_list.add(product);
	//			mAdapter = new ProductListAdapter(categoryTabs, product_list);
	//			mAdapter = new ProductListAdapter(searchTabs, product_list);
				mAdapter = new ProductListAdapter(getActivity(), product_list);
				mList.setAdapter(mAdapter);
		}
	  }
		else{
			product_list = new ArrayList<Product>();
			Product product = new Product("No product found for this category");
			product_list.add(product);
			//			mAdapter = new ProductListAdapter(categoryTabs, product_list);
			//			mAdapter = new ProductListAdapter(searchTabs, product_list);
			mAdapter = new ProductListAdapter(getActivity(), product_list);
			mList.setAdapter(mAdapter);
		}
//			((BaseActivity)SearchTabs.context).dismissDialog();
//			UtilityMethods.getInstance().dismissDialog();
		} catch (Exception e) {
//			UtilityMethods.getInstance().dismissDialog();
//			((BaseActivity)SearchTabs.context).dismissDialog();
			new GrocermaxBaseException("SearchProductFragments","makeUI",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}

    }
    

	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		try{

		}catch(Exception e){

		}
//		this.currentFirstVisibleItem = firstVisibleItem;
//		this.currentVisibleItemCount = visibleItemCount;
//		this.totalItemCount = totalItemCount;
	}

	@Override
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}

}

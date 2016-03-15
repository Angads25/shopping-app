package com.rgretail.grocermax;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.melnykov.fab.FloatingActionButton;
import com.rgretail.grocermax.adapters.ProductListAdapter;
import com.rgretail.grocermax.bean.Product;
import com.rgretail.grocermax.bean.ProductListBean;
import com.rgretail.grocermax.exception.GrocermaxBaseException;
import com.rgretail.grocermax.preference.MySharedPrefs;
import com.rgretail.grocermax.utils.UrlsConstants;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


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

			FloatingActionButton fab = (FloatingActionButton)view.findViewById(R.id.fab);
			fab.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent(getActivity(), ProductSorting.class);
					intent.putExtra("comming_from","search");
					startActivityForResult(intent, 0);
				}
			});


		makeUI(SearchTabs.sort_condition);
		return view;
		}catch(Exception e){
			new GrocermaxBaseException("SearchProductFragments","onCreateView",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
		}
		return  null;
    }

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		try {
			String condition=data.getStringExtra("condition");
			System.out.println("condition="+condition);
			SearchTabs.sort_condition=condition;
			makeUI(SearchTabs.sort_condition);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    private void makeUI(String condition) {
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

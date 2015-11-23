package com.rgretail.grocermax.utils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Writer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint.FontMetrics;
import android.graphics.Path;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Parcel;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.MyApplication;

import com.rgretail.grocermax.R;
import com.rgretail.grocermax.adapters.CategorySubcategoryBean;
import com.rgretail.grocermax.bean.CartDetail;
import com.rgretail.grocermax.bean.CartDetailBean;
import com.rgretail.grocermax.utils.ListSublistConstants.ListConstant;

public class UtilityMethods {

//	private static UtilityMethods instance;
//	public static UtilityMethods getInstance(){
//		if(instance == null){
//			instance = new UtilityMethods();
//		}
//		return instance;
//	}
//	public UtilityMethods(){
//		instance = this;
//	}
//
//
//	public void showDialog(Context context) {
//		try {
//			mProgressDialog = new ProgressDialog(context);
//			mProgressDialog.setMessage("Loading...");
//			mProgressDialog.show();
//			mProgressDialog.setCancelable(false);
//		}catch(Exception e){
//			new GrocermaxBaseException("UtilityMethods", "showDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
//		}
//	}
//	private ProgressDialog mProgressDialog;
//
//
//	public void dismissDialog() {
//		try{
//			if (mProgressDialog != null && mProgressDialog.isShowing()) {
//				mProgressDialog.dismiss();
//			}
//		}catch(Exception e){
//			new GrocermaxBaseException("UtilityMethods", "dismissDialog", e.getMessage(), GrocermaxBaseException.EXCEPTION, "nodetail");
//		}
//	}

	/**
	 * For check internet COnnectivity
	 */
	public static boolean isInternetAvailable(Context context) {
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		// test for connection
		if (cm.getActiveNetworkInfo() != null
				&& cm.getActiveNetworkInfo().isAvailable()
				&& cm.getActiveNetworkInfo().isConnected()) {
			return true;
		} else {
			// Log.v("APP_ROOT", "Internet Connection Not Present");
			return false;
		}
	}

	/**
	 * For setting text size according to device height and width
	 * */

	public static float calculateHeight(FontMetrics fm) {
		return fm.bottom - fm.top;
	}

	/**
	 * For email validation
	 * 
	 * @param target
	 * @return boolean varible depending up weather email is valid or not
	 */
	public static boolean isValidEmail(CharSequence target) {
		if (target == null) {
			return false;
		} else {
			return android.util.Patterns.EMAIL_ADDRESS.matcher(target)
					.matches();
		}
	}

	/*
	 * For Encrypting the password
	 */
	public static String getSignature(String password) {
		// String signature = "";
		String newSignature = "";

		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");
			md5.update(password.getBytes(), 0, password.length());
			byte messageDigest[] = md5.digest();

			// signature = new BigInteger(1, messageDigest).toString(16);

			StringBuilder sb = new StringBuilder();
			for (byte b : messageDigest)
				sb.append(String.format("%02x", b & 0xff));
			newSignature = sb.toString();

		} catch (final NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return newSignature;
	}

	public static void hideKeyboard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
	}

	public static void hideKeyboardFromContext(Context mContext) {
		InputMethodManager inputManager = (InputMethodManager) mContext
				.getSystemService(Context.INPUT_METHOD_SERVICE);

		// check if no view has focus:
		View view = ((Activity) mContext).getCurrentFocus();
		if (view != null) {
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	public static void showAlert(Context context, String title, String msg,
			String btnOneTxt, String btnTwoTxt) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(btnOneTxt,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

							}
						})
				.setNegativeButton(btnTwoTxt,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, just close
								// the dialog box and do nothing
								dialog.cancel();
							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public static void showAlert(Context context, String title, String msg,
			String btnOneTxt) {
		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
				context);

		// set title
		alertDialogBuilder.setTitle(title);

		// set dialog message
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton(btnOneTxt,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// if this button is clicked, close
								// current activity

							}
						});

		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();

		// show it
		alertDialog.show();

	}

	public static void email(String[] recipients, String subject,
			String content, Context context) {
		Intent intent = new Intent(Intent.ACTION_SEND);

		intent.putExtra(Intent.EXTRA_EMAIL, recipients);
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, content);
		// intent.putExtra(Intent.EXTRA_CC,"ghi");
		intent.setType("text/html");
		context.startActivity(Intent.createChooser(intent, "Send mail"));
	}

	public static void contactUs(Context context) {
		String recepientEmail = "warning1987@gmail.com";
		Intent intent = new Intent(Intent.ACTION_SENDTO);
		String subject = String.format("%s Feedback", context.getResources()
				.getString(R.string.app_name));
		intent.putExtra(Intent.EXTRA_SUBJECT, subject);
		intent.putExtra(Intent.EXTRA_TEXT, "Send from my android.");
		intent.setData(Uri.parse("mailto:" + recepientEmail));
		context.startActivity(intent);
	}

	public static void moreApp(Context context) {
		Intent goToMarket = null;
		// goToMarket = new
		// Intent(Intent.ACTION_VIEW,Uri.parse("market://details?id=com.manu.valentine"));
		goToMarket = new Intent(
				Intent.ACTION_VIEW,
				Uri.parse("https://play.google.com/store/apps/developer?id=Manu+Dev(M.D.)"));
		context.startActivity(goToMarket);
	}

	public static void rateApp(Context context) {
		Intent goToMarket = null;
		goToMarket = new Intent(Intent.ACTION_VIEW,
				Uri.parse("market://details?id=" + AppConstants.PKG_NAME));
		context.startActivity(goToMarket);
	}

	public static void shareApp(Context context) {
		String content = "Try out GrocerMax app, order groceries online and get hot offers on hundreds of products sitting at home.";
		String link_val = "https://play.google.com/store/apps/details?id="
				+ AppConstants.PKG_NAME;

		final String body = content + "\n<a href=\"" + link_val + "\">"
				+ "Download Now." + "</a>" + "\n Thanks.";

		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Best Android App");
		shareIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(body));
		shareIntent.setType("message/rfc822");
		context.startActivity(Intent.createChooser(shareIntent,
				"Invite friends..."));

    // NEED TO RESTRICT THE USER FOR THE MENTIONED APPS

    }

	@SuppressWarnings("unchecked")
	public static ArrayList<CartDetail> readLocalCart(Activity activity,
			String filename) {
		ArrayList<CartDetail> localCart = new ArrayList<CartDetail>();
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					byte[] data = new byte[(int) file.length()];

					int data1 = fin.read(data, 0, data.length);
					Parcel parcel = Parcel.obtain();
					parcel.unmarshall(data, 0, data.length);
					parcel.setDataPosition(0); // Set the position in the parcel
												// back to the beginning
					// so that we can read the stuff out of it
					localCart = parcel.readArrayList(CartDetail.class
							.getClassLoader());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localCart;
	}
	
	
/************* Clone Cart methods ****************/
	
	@SuppressWarnings("unchecked")
	public static ArrayList<CartDetail> readCloneCart(Activity activity,
			String filename) {
		ArrayList<CartDetail> localCart = new ArrayList<CartDetail>();
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					byte[] data = new byte[(int) file.length()];

					int data1 = fin.read(data, 0, data.length);
					Parcel parcel = Parcel.obtain();
					parcel.unmarshall(data, 0, data.length);
					parcel.setDataPosition(0); // Set the position in the parcel
												// back to the beginning
					// so that we can read the stuff out of it
					localCart = parcel.readArrayList(CartDetail.class
							.getClassLoader());
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return localCart;
	}

	
	public static boolean deleteCloneCartItem(Activity activity, String product_id) {
		boolean delete = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			/*
			 * HashMap<String, Integer> cart_count = readCountCart(activity,
			 * Constants.localCartFileCount); cart_count.remove(key); File file
			 * = new File(dir, Constants.localCartFileCount); delete =
			 * writeHashMapToFile(file, cart_count);
			 * 
			 * if(delete){
			 */ArrayList<CartDetail> cart_products = readLocalCart(activity,
					Constants.localCloneFile);
			ArrayList<CartDetail> updated_cart_products = new ArrayList<CartDetail>();
			for (int i = 0; i < cart_products.size(); i++) {
				// if (cart_products.get(i).getProductId().equals(product_id)) {
				// cart_products.remove(i);
				// }
				if (!cart_products.get(i).getItem_id().equals(product_id)) {
					updated_cart_products.add(cart_products.get(i));
				}
			}
			cart_products = updated_cart_products;
			File file1 = new File(dir, Constants.localCloneFile);
			delete = writeArrayListToFile(file1, cart_products);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return delete;
	}
	
	public static boolean writeCloneCart(Activity activity, String filename,
			CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readCloneCart(activity, filename);
			
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				int flagPos = -1;
				for (int i = 0; i < list.size(); i++) {
					String str = list.get(i).getNoDiscount();
					String str1 = prod.getNoDiscount();
					if (list.get(i).getItem_id().equals(prod.getItem_id())) {
						if(list.get(i).getNoDiscount() != null){
							if (list.get(i).getNoDiscount().equals("1")) {          //not offer                            [0 - means offer AND 1 - means not offer]
								flagPos = i;
								break;
							}
						}else {
//							if (list.get(i).getNoDiscount().equals("1")) {          //not offer                            [0 - means offer AND 1 - means not offer]
								flagPos = i;
								break;
//							}
						}
					}

				}
				if (flagPos != -1) {
					Integer quantity = list.get(flagPos).getQty();
					quantity = quantity + prod.getQty();
					list.get(flagPos).setQty(quantity);

				} else {

					list.add(prod);
				}

			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}
	
	public static void deleteCloneCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, Constants.localCloneFile);
		if (file.exists()) {
			file.delete();
		}

//		File file1 = new File(dir, Constants.localCartFileCount);
//		if (file1.exists()) {
//			file1.delete();
//		}
	}

	
	/************* Clone Cart methods ****************/

	public static boolean writeLocalCart(Activity activity, String filename,
			CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readLocalCart(activity, filename);
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				int flagPos = -1;
				for (int i = 0; i < list.size(); i++) {
					if (list.get(i).getItem_id().equals(prod.getItem_id())) {
						flagPos = i;
						break;
					}

				}
				if (flagPos != -1) {
					Integer quantity = list.get(flagPos).getQty();
					quantity = quantity + prod.getQty();
					list.get(flagPos).setQty(quantity);

				} else {

					list.add(prod);
				}

			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}

	public static boolean writeArrayListToFile(File file,
			ArrayList<?> cart_products) {
		boolean write = false;
		try {
			Parcel parcel = Parcel.obtain();
			FileOutputStream fout = new FileOutputStream(file);
			Log.d("ConnectionService",
					"write items to cache: " + cart_products.size());
			parcel.writeList(cart_products);
			byte[] data = parcel.marshall();
			fout.write(data);
			fout.close();
			write = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return write;
	}

	/*
	 * public static HashMap<String, Integer> readCountCart(Activity activity,
	 * String filename) { boolean write = false; HashMap<String, Integer>
	 * count_cart = null; try{ File mydir = activity.getDir("mydir",
	 * Context.MODE_PRIVATE); String dir = mydir.getAbsolutePath(); File file =
	 * new File(dir, filename); if(file.length() > 0){ FileInputStream f = new
	 * FileInputStream(file); ObjectInputStream s = new ObjectInputStream(f);
	 * count_cart = (HashMap<String, Integer>) s.readObject(); s.close(); } }
	 * catch(Exception e) { e.printStackTrace(); }
	 * 
	 * return count_cart; }
	 * 
	 * public static int writeCountCart(Activity activity, String filename,
	 * String key, String quantity) { int response = -1; HashMap<String,
	 * Integer> count_cart = null; try{ File mydir = activity.getDir("mydir",
	 * Context.MODE_PRIVATE); String dir = mydir.getAbsolutePath(); File file =
	 * new File(dir, filename); if(file.length() > 0) { count_cart =
	 * readCountCart(activity, filename); } if(count_cart == null) count_cart =
	 * new HashMap<String, Integer>(); if(count_cart.containsKey(key)) {
	 * response = 200; count_cart.put(key, count_cart.get(key) +
	 * Integer.parseInt(quantity)); } else { response = 201; count_cart.put(key,
	 * Integer.parseInt(quantity)); } writeHashMapToFile(file, count_cart); }
	 * catch(Exception e) { response = 400; e.printStackTrace(); }
	 * 
	 * return response; }
	 * 
	 * public static boolean writeHashMapToFile(File file, HashMap<?, ?>
	 * count_cart) { boolean write = false; try{ FileOutputStream f = new
	 * FileOutputStream(file); ObjectOutputStream s = new ObjectOutputStream(f);
	 * s.writeObject(count_cart); s.close(); write = true; } catch(Exception e)
	 * { e.printStackTrace(); } return write; }
	 */

	public static boolean deleteCartItem(Activity activity, String product_id) {
		boolean delete = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			/*
			 * HashMap<String, Integer> cart_count = readCountCart(activity,
			 * AppConstants.localCartFileCount); cart_count.remove(key); File file

			 * = new File(dir, AppConstants.localCartFileCount); delete =
			 * writeHashMapToFile(file, cart_count);
			 * 
			 * if(delete){
			 */ArrayList<CartDetail> cart_products = readLocalCart(activity,
					AppConstants.localCartFile);
			ArrayList<CartDetail> updated_cart_products = new ArrayList<CartDetail>();
			for (int i = 0; i < cart_products.size(); i++) {
				// if (cart_products.get(i).getProductId().equals(product_id)) {
				// cart_products.remove(i);
				// }
				if (!cart_products.get(i).getItem_id().equals(product_id)) {
					updated_cart_products.add(cart_products.get(i));
				}
			}
			cart_products = updated_cart_products;
			File file1 = new File(dir, AppConstants.localCartFile);
			delete = writeArrayListToFile(file1, cart_products);
			// }
		} catch (Exception e) {
			e.printStackTrace();
		}

		return delete;
	}

	public static boolean writeServerCart(Activity activity, String filename,
			CartDetailBean cart) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				file.delete();
			}
			if (!file.exists()) {
				try {
					file.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			FileOutputStream fos = new FileOutputStream(file);
			ObjectOutputStream os = new ObjectOutputStream(fos);
			os.writeObject(cart);
			os.close();
			fos.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

		return write;
	}

	public static CartDetailBean readServerCart(Activity activity,
			String filename) {
		CartDetailBean cart = null;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);
			if (file.exists()) {
				FileInputStream fin = new FileInputStream(file);
				if (file.length() > 0) {
					ObjectInputStream is = new ObjectInputStream(fin);
					cart = (CartDetailBean) is.readObject();
					is.close();
					fin.close();
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cart;
	}

	public static void deleteLocalCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, AppConstants.localCartFile);
		if (file.exists()) {
			file.delete();
		}

		File file1 = new File(dir, AppConstants.localCartFileCount);
		if (file1.exists()) {
			file1.delete();
		}
	}

	public static void deleteServerCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, AppConstants.serverCartFile);
		if (file.exists()) {
			file.delete();
		}
	}
	
	public static boolean writeCategoryResponse(Activity activity, String filename, String categories)
	{
		boolean write = false;

		File dir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String directory = dir.getAbsolutePath();
		
//		File dir = new File(directory);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File logFile = new File(directory, filename);
		if (logFile.exists()) {
			logFile.delete();
		}
		if (!logFile.exists()) {
			try {
				logFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			// BufferedWriter for performance, true to set append to file flag
			BufferedWriter buf = new BufferedWriter(new FileWriter(logFile,
					true));
			buf.write(categories);
			buf.newLine();
			buf.close();
			// Constants.index++;
		} catch (IOException e) {

			e.printStackTrace();
			return false;
		}
	
		
		return write;
	}
	
	public static String readCategoryResponse(Activity activity, String filename)
	{
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, filename);
		if (file.exists()) {
			int size = (int) file.length();
			byte[] bytes = new byte[size];
			BufferedInputStream buf = null;
			try {
				buf = new BufferedInputStream(new FileInputStream(file));
				buf.read(bytes, 0, bytes.length);
				return new String(bytes, "UTF-8");
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					buf.close();
				} catch (Exception e2) {
				}
			}

		}
		return "nofile";
	}
	
	public static ArrayList<CategorySubcategoryBean> getCategorySubCategory(String content) {
		ArrayList<CategorySubcategoryBean> category = null;
		try {
			category = new ArrayList<CategorySubcategoryBean>();
			CategorySubcategoryBean categoryOb;
			JSONObject jsonObject = new JSONObject(content.trim());

			Constants.base_url_category_image = jsonObject.getString("urlImg");

			jsonObject=jsonObject.getJSONObject(ListConstant.TAG_CATEGORY).getJSONArray("children").getJSONObject(0);


			JSONArray jsonMainArray = jsonObject.getJSONArray(ListConstant.TAG_CHILDREN);
			for (int i = 0; i < jsonMainArray.length(); i++) {
				JSONObject jsonName = jsonMainArray.getJSONObject(i);
				categoryOb = new CategorySubcategoryBean();

				categoryOb.setCategory(""
						+ jsonName.getString(ListConstant.TAG_NAME));
				categoryOb.setCategoryId(""
						+ jsonName.getString(ListConstant.TAG_CATEGORYID));
				categoryOb.setBreadcrumb(""
						+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
				
				JSONArray jsonChild;
				try {
					jsonChild = jsonName
							.getJSONArray(ListConstant.TAG_CHILDREN);
				} catch (Exception e) {
					jsonChild = new JSONArray();
				}

				for (int j = 0; j < jsonChild.length(); j++) {
					jsonName = jsonChild.getJSONObject(j);
					CategorySubcategoryBean categorySubOb = new CategorySubcategoryBean();
					categorySubOb.setCategory(""
							+ jsonName.getString(ListConstant.TAG_NAME));
					categorySubOb.setIsActive(""
							+ jsonName.getString(ListConstant.TAG_IS_ACTIVE));
					categorySubOb.setCategoryId(""
							+ jsonName.getString(ListConstant.TAG_CATEGORYID));
					categorySubOb.setBreadcrumb(""
							+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
					
					JSONArray jsonChildCategory;
					try {
						jsonChildCategory = jsonName
								.getJSONArray(ListConstant.TAG_CHILDREN);
					} catch (Exception e) {
						jsonChildCategory = new JSONArray();
					}
					for (int k = 0; k < jsonChildCategory.length(); k++) {
						jsonName = jsonChildCategory.getJSONObject(k);
						CategorySubcategoryBean categorySubSubOb = new CategorySubcategoryBean();
						categorySubSubOb.setCategory(""
								+ jsonName.getString(ListConstant.TAG_NAME));
						categorySubSubOb.setCategoryId(""
										+ jsonName.getString(ListConstant.TAG_CATEGORYID));
						categorySubSubOb.setBreadcrumb(""
								+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
						
						JSONArray jsonChildSubCategory;
						try {
							jsonChildSubCategory = jsonName
									.getJSONArray(ListConstant.TAG_CHILDREN);
						} catch (Exception e) {
							jsonChildSubCategory = new JSONArray();
						}
						for (int l = 0; l < jsonChildSubCategory.length(); l++) {
							jsonName = jsonChildSubCategory.getJSONObject(l);
							CategorySubcategoryBean categorySubSubSubOb = new CategorySubcategoryBean();
							categorySubSubSubOb.setCategory(""
											+ jsonName.getString(ListConstant.TAG_NAME));
							categorySubSubSubOb.setCategoryId(""
											+ jsonName.getString(ListConstant.TAG_CATEGORYID));
							categorySubSubSubOb.setBreadcrumb(""
									+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
							
							JSONArray jsonChildSubSubCategory;
							try {
								jsonChildSubSubCategory = jsonName
										.getJSONArray(ListConstant.TAG_CHILDREN);
							} catch (Exception e) {
								jsonChildSubSubCategory = new JSONArray();
							}
							for (int m = 0; m < jsonChildSubSubCategory
									.length(); m++) {
								jsonName = jsonChildSubSubCategory
										.getJSONObject(m);
								CategorySubcategoryBean categorySubSubSubSubOb = new CategorySubcategoryBean();
								categorySubSubSubSubOb.setCategory(""
												+ jsonName.getString(ListConstant.TAG_NAME));
								categorySubSubSubSubOb.setCategoryId(""
												+ jsonName.getString(ListConstant.TAG_CATEGORYID));
								categorySubSubSubSubOb.setBreadcrumb(""
										+ jsonName.optString(ListConstant.TAG_BREADCRUMB));
								
								categorySubSubSubOb.addCategory(categorySubSubSubSubOb);
							}

							categorySubSubOb.addCategory(categorySubSubSubOb);
						}
						categorySubOb.addCategory(categorySubSubOb);
					}
					categoryOb.addCategory(categorySubOb);

				}
				category.add(categoryOb);
			}
		} catch (JSONException e) {
		}
		return category;
	}
	
	
	
	//////////////////////Writing JSON data in external File/////////////////
	 public static void write(String fileName, String data,Context context) {
		 Writer writer;
		    File root = Environment.getExternalStorageDirectory();
		    File outDir = new File(root.getAbsolutePath() + File.separator + "EZ_time_tracker");
		    if (!outDir.isDirectory()) {
		      outDir.mkdir();
		    }
		    try {
		      if (!outDir.isDirectory()) {
		        throw new IOException(
		            "Unable to create directory EZ_time_tracker. Maybe the SD card is mounted?");
		      }
		      File outputFile = new File(outDir, fileName);
		      writer = new BufferedWriter(new FileWriter(outputFile));
		      writer.write(data);
		     /* Toast.makeText(context.getApplicationContext(),
		          "Report successfully saved to: " + outputFile.getAbsolutePath(),
		          Toast.LENGTH_LONG).show();*/
		      writer.close();
		    } catch (IOException e) {
		      Log.w("eztt", e.getMessage(), e);
		      Toast.makeText(context, e.getMessage() + Constants.ToastConstant.UNABLE_TO_WRITE,
		          Toast.LENGTH_LONG).show();
		    }

		  }
	 
	 public int dpToPx(int dp,Context context) {
		    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		    int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));       
		    return px;
		}

		public int pxToDp(int px,Context context) {
		    DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
		    int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
		    return dp;
		}


	public static void hideKeyBoard(Context con)
	 {
		 if(BaseActivity.keyboardVisibility)
			{
				InputMethodManager imm = (InputMethodManager)con.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.toggleSoftInput(InputMethodManager.RESULT_HIDDEN, 0);
			}
	 }	 
	 public static void customToast(String strMsg,Context context)
	 {
         TextView textview = new TextView(context);
         textview.setText(strMsg);
         textview.setGravity(1);
         textview.setBackgroundColor(Color.WHITE);
         textview.setTextColor(Color.WHITE);
         textview.setPadding(10, 10, 10, 10);
         Toast toast = new Toast(context);
         toast.setView(textview);

//         toast.getView().setBackgroundDrawable(new ColorDrawable(0xff123456));
		 toast.getView().setBackgroundColor(Color.parseColor("#ee2d09"));
//		 android:background="@color/primaryColor"
         toast.setDuration(Toast.LENGTH_SHORT);
         toast.setGravity(Gravity.BOTTOM|Gravity.CENTER|Gravity.FILL_HORIZONTAL, 0, 0);
         toast.show();
	 }
	 
	 public static String getCurrentClassName(Context con)
	 {
		 ActivityManager am = (ActivityManager) con.getSystemService(Context.ACTIVITY_SERVICE);
//		    List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
		 ActivityManager mActivityManager =(ActivityManager) MyApplication.getInstance().getSystemService(Context.ACTIVITY_SERVICE);
		 String asa = con.getApplicationContext().getPackageName();
		 String mPackageName = "";
		 if(Build.VERSION.SDK_INT > 20){
			 mPackageName = mActivityManager.getRunningAppProcesses().get(0).getClass().getName();
		 }
		 else{
			 mPackageName = mActivityManager.getRunningTasks(1).get(0).topActivity.getClassName();
		 }
		    /*Log.d("topActivity", "CURRENT Activity ::" + taskInfo.get(0).topActivity.getClassName());
		    ComponentName componentInfo = taskInfo.get(0).topActivity;
		    componentInfo.getPackageName();*/

//		    return taskInfo.get(0).topActivity.getClassName();
		    return mPackageName;
	 }
	 
	 public static Bitmap getRoundedShape(Bitmap scaleBitmapImage) {
		    int targetWidth = 50;
		    int targetHeight = 50;
		    Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		                        targetHeight,Bitmap.Config.ARGB_8888);

		    Canvas canvas = new Canvas(targetBitmap);
		    Path path = new Path();
		    path.addCircle(((float) targetWidth - 1) / 2,
		        ((float) targetHeight - 1) / 2,
		        (Math.min(((float) targetWidth), 
		        ((float) targetHeight)) / 2),
		        Path.Direction.CCW);

		    canvas.clipPath(path);
		    Bitmap sourceBitmap = scaleBitmapImage;
		    canvas.drawBitmap(sourceBitmap, 
		        new Rect(0, 0, sourceBitmap.getWidth(),
		        sourceBitmap.getHeight()), 
		        new Rect(0, 0, targetWidth, targetHeight), null);
		    return targetBitmap;
		}

	public static boolean writeStackCart(Activity activity, String filename,
										 CartDetail prod) {
		boolean write = false;
		try {
			File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
			String dir = mydir.getAbsolutePath();
			File file = new File(dir, filename);

			ArrayList<CartDetail> list = readLocalCart(activity, filename);
			if (list == null) {
				list = new ArrayList<CartDetail>();
				list.add(prod);
			} else {
				list.add(prod);
			}
			write = writeArrayListToFile(file, list);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return write;

	}

	public static void deleteStackCart(Activity activity) {
		File mydir = activity.getDir("mydir", Context.MODE_PRIVATE);
		String dir = mydir.getAbsolutePath();
		File file = new File(dir, Constants.StackCartFile);
		if (file.exists()) {
			file.delete();
		}
	}
}

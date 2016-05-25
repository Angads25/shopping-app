package com.rgretail.grocermax.utils;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFonts {
	public static CustomFonts instance;
	public static CustomFonts getInstance(){
		
		if(instance == null){
			instance = new CustomFonts();
		}
		return instance;
	}
	
	 Typeface face;
	 public Typeface getRobotoBlack(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Black.ttf");
		 return face;
	 }
	 

	 
	 public Typeface getRobotoBold(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
		 return face;
	 }
	 

	 
	 
	 public Typeface getRobotoLight(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
		 return face;
	 }
	 

	 
	 public Typeface getRobotoMedium(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
		 return face;
	 }


	 public Typeface getRobotoRegular(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
		 return face;
	 }




	 
}

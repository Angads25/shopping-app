package com.sakshay.grocermax.utils;

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
	 
	 public Typeface getRobotoBlackItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-BlackItalic.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoBold(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Bold.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoBoldItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-BoldItalic.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Italic.ttf");
		 return face;
	 }
	 
	 
	 public Typeface getRobotoLight(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Light.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoLightItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-LightItalic.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoMedium(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Medium.ttf");
		 return face;
	 }

	 public Typeface getRobotoMediumItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-MediumItalic.ttf");
		 return face;
	 }
	 public Typeface getRobotoRegular(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Regular.ttf");
		 return face;
	 }
	 public Typeface getRobotoThin(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-Thin.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoThinItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Roboto-ThinItalic.ttf");
		 return face;
	 }

	 public Typeface getRobotoCondensedBold(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Bold.ttf");
		 return face;
	 }

	 public Typeface getRobotoCondensedBoldItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-BoldItalic.ttf");
		 return face;
	 }

	 public Typeface getRobotoCondensedItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Italic.ttf");
		 return face;
	 }

	 public Typeface getRobotoCondensedLight(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Light.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoCondensedLightItalic(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-LightItalic.ttf");
		 return face;
	 }
	 
	 public Typeface getRobotoCondensedRegular(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "RobotoCondensed-Regular.ttf");
		 return face;
	 }

	 public Typeface getRupee(Context context){
		 face = Typeface.createFromAsset(context.getAssets(), "Rupee.ttf");
		 return face;
	 }

	 
}

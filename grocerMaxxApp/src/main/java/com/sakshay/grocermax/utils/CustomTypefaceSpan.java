package com.sakshay.grocermax.utils;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.style.TypefaceSpan;

public class CustomTypefaceSpan extends TypefaceSpan{

	private final Typeface newType;

	public CustomTypefaceSpan(String family, Typeface type) {
	    super(family);
	    newType = type;
	}

	@Override
	public void updateDrawState(TextPaint ds) {
	    applyCustomTypeFace(ds, newType);
	}

	@Override
	public void updateMeasureState(TextPaint paint) {
	    applyCustomTypeFace(paint, newType);
	}

	private static void applyCustomTypeFace(Paint paint, Typeface tf) {
	    int oldStyle;
	    Typeface old = paint.getTypeface();
	    if (old == null) {
	        oldStyle = 0;
	    } else {
	        oldStyle = old.getStyle();
	    }

	    int fake = oldStyle & ~tf.getStyle();
	    if ((fake & Typeface.BOLD) != 0) {
	        paint.setFakeBoldText(true);
	    }

	    if ((fake & Typeface.ITALIC) != 0) {
	        paint.setTextSkewX(-0.25f);
	    }

	    paint.setTypeface(tf);
	}
	}


//Typeface font1 = Typeface.createFromAsset(activity.getAssets(), "Rupee.ttf");
//Typeface font2 = Typeface.createFromAsset(activity.getAssets(), "Roboto-Light.ttf");
//SpannableStringBuilder SS = new SpannableStringBuilder("`"+obj.getPrice().toString());
//SS.setSpan (new CustomTypefaceSpan("", font1), 0, 1,Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//SS.setSpan (new CustomTypefaceSpan("", font2), 1, obj.getPrice().toString().length()-(obj.getPrice().toString().length()-1),Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//holder.amount.setText(SS);

package com.sakshay.grocermax.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.widget.EditText;

import com.sakshay.grocermax.utils.AppConstants;

//import com.sakshay.grocermax.HomeScreen;

/**
 * Created by Abhishek on 9/30/2015.
 */
public class CustomEditSearch extends EditText {

    //The image we are going to use for the Clear button
//    private Drawable imgCloseButton = getResources().getDrawable(R.drawable.clear_button_image);

    public CustomEditSearch(Context context) {
        super(context);

    }

    public CustomEditSearch(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

    }

    public CustomEditSearch(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private BackPressedListener mOnImeBack;

    /* constructors */

    @Override
    public boolean onKeyPreIme(int keyCode, KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
//            if (mOnImeBack != null) mOnImeBack.onImeBack(this);
            AppConstants.bBack = true;
        }
        return super.dispatchKeyEvent(event);
    }

    public void setBackPressedListener(BackPressedListener listener) {
        mOnImeBack = listener;
    }

    public interface BackPressedListener {
//        void onImeBack(BackAwareEditText editText);
    }

}

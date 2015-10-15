package com.sakshay.grocermax;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;

import com.sakshay.grocermax.exception.GrocermaxBaseException;

public class KeyboardStatusDetector {
    KeyboardVisibilityListener visibilityListener;

    boolean keyboardVisible = false;

    public void registerFragment(Fragment f) {
        registerView(f.getView());
    }

    public void registerActivity(Activity a) {
        registerView(a.getWindow().getDecorView().findViewById(android.R.id.content));
    }

    public KeyboardStatusDetector registerView(final View v) {
        try {
            v.getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    v.getWindowVisibleDisplayFrame(r);

                    int heightDiff = v.getRootView().getHeight() - (r.bottom - r.top);
                    if (heightDiff > 100) { // if more than 100 pixels, its probably a keyboard...
                        /** Check this variable to debounce layout events */

                        if (!keyboardVisible) {
                            keyboardVisible = true;
                            if (visibilityListener != null)
                                visibilityListener.onVisibilityChanged(true);
                        }
                    } else {
                        if (keyboardVisible) {
                            keyboardVisible = false;
                            if (visibilityListener != null)
                                visibilityListener.onVisibilityChanged(false);
                        }
                    }
                }
            });
        }catch(Exception e){
            new GrocermaxBaseException("KeyboardStatusDetector","OnResponse",e.getMessage(), GrocermaxBaseException.EXCEPTION,"");
        }
        return this;
    }

    public KeyboardStatusDetector setVisibilityListener(KeyboardVisibilityListener listener) {
        visibilityListener = listener;
        return this;
    }

    public static interface KeyboardVisibilityListener {
        public void onVisibilityChanged(boolean keyboardVisible);
    }
}

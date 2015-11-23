package com.rgretail.grocermax;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ScrollView;

import com.rgretail.grocermax.exception.GrocermaxBaseException;

public class ScrollViewExt extends ScrollView {
    private ScrollViewListener scrollViewListener = null;
    public ScrollViewExt(Context context) {
        super(context);
    }

    public ScrollViewExt(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    public ScrollViewExt(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setScrollViewListener(ScrollViewListener scrollViewListener) {
        try {
            this.scrollViewListener = scrollViewListener;
        }catch(Exception e){
            new GrocermaxBaseException("ScrollViewExt","setScrollViewListener",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        try {
            super.onScrollChanged(l, t, oldl, oldt);
            if (scrollViewListener != null) {
                scrollViewListener.onScrollChanged(this, l, t, oldl, oldt);
            }
        }catch(Exception e){
            new GrocermaxBaseException("ScrollViewExt","onScrollChanged",e.getMessage(), GrocermaxBaseException.EXCEPTION,"nodetail");
        }
    }
}
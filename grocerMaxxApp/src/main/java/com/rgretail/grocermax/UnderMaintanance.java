package com.rgretail.grocermax;

import android.os.Bundle;
import android.view.Window;

/**
 * Created by anchit-pc on 28-Dec-15.
 */
public class UnderMaintanance extends BaseActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.under_maintainance);
    }

    @Override
    public void OnResponse(Bundle bundle) {

    }

    @Override
    public void onBackPressed() {
    }
}

package com.rgretail.grocermax.hotoffers.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.rgretail.grocermax.BaseActivity;
import com.rgretail.grocermax.R;
import com.rgretail.grocermax.hotoffers.HomeScreen;
import com.rgretail.grocermax.utils.AppConstants;

/**
 * Created by anchit-pc on 22-Jul-16.
 */
public class SinglePageFragment extends Fragment {

    ImageView imgImage;
    String imagename,imageurl,deeplink;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if(getActivity() instanceof HomeScreen)
            HomeScreen.bFromHome = false;
        else
            HomeScreen.bFromHome = true;


        View view = inflater.inflate(R.layout.single_page_frag, container, false);
        try {
            imgImage = (ImageView) view.findViewById(R.id.imgImage);

            if(getActivity() instanceof HomeScreen){
                ((HomeScreen) getActivity()).isFromFragment = true;
                HomeScreen.bFromHome = false;
            }
            else{
                HomeScreen.isFromFragment = false;
                HomeScreen.bFromHome = true;
            }

            Bundle data = getArguments();
            imagename=data.getString("ImageName");
            imageurl=data.getString("ImageUrl");
            deeplink=data.getString("Deeplink");

            ((BaseActivity) getActivity()).initHeader(getActivity().findViewById(R.id.header_left), true, AppConstants.strTitleHotDeal);
            ((BaseActivity) getActivity()).findViewById(R.id.header_left).setVisibility(View.VISIBLE);
            ((BaseActivity) getActivity()).findViewById(R.id.header).setVisibility(View.GONE);

            //ImageLoader.getInstance().displayImage("http://i960.photobucket.com/albums/ae81/busybudgetingmama/SETUP-2.jpg",imgImage, ((BaseActivity) getActivity()).baseImageoptions);
            ImageLoader.getInstance().displayImage(imageurl,imgImage, ((BaseActivity) getActivity()).baseImageoptions);
            imgImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        Bundle bundle2=new Bundle();
                        bundle2.putString("linkurl", deeplink);
                        bundle2.putString("name",imagename);
                        ((HomeScreen) getActivity()).getNotificationData(bundle2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            getActivity().findViewById(R.id.header).setVisibility(View.GONE);
            getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        }catch(Exception e){}
        super.onAttach(activity);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        super.onStart();
    }

    @Override
    public void onResume() {
        getActivity().findViewById(R.id.header).setVisibility(View.GONE);
        getActivity().findViewById(R.id.header_left).setVisibility(View.VISIBLE);
        super.onResume();
    }
}

package com.sakshay.grocermax;

import java.io.InputStream;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.GooglePlayServicesUtil;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
//import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
//import com.google.android.gms.plus.Plus;
//import com.google.android.gms.plus.model.people.Person;
public class GooglePlus {
//implements OnClickListener, ConnectionCallbacks, OnConnectionFailedListener {
//
//    private static final int RC_SIGN_IN = 0;
//    // Google client to communicate with Google
//    private GoogleApiClient mGoogleApiClient;
//    private boolean mIntentInProgress;
//    private boolean signedInUser;
//    private ConnectionResult mConnectionResult;
//    private SignInButton signinButton;
//    private ImageView image;
//    private TextView username, emailLabel;
//    private LinearLayout profileFrame, signinFrame;
//    
//    Context context;
//    Activity activity;
//    
//    public GooglePlus(Activity activity,Context context){
//    	this.activity = activity;
//    	this.context = context;
//    	 mGoogleApiClient = new GoogleApiClient.Builder(context).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
//         mGoogleApiClient.connect();
//         googlePlusLogin();
//    }
//    
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_main);
////        signinButton = (SignInButton) findViewById(R.id.signin);
////        signinButton.setOnClickListener(this);
////        image = (ImageView) findViewById(R.id.image);
////        username = (TextView) findViewById(R.id.username);
////        emailLabel = (TextView) findViewById(R.id.email);
////        profileFrame = (LinearLayout) findViewById(R.id.profileFrame);
////        signinFrame = (LinearLayout) findViewById(R.id.signinFrame);
////        mGoogleApiClient = new GoogleApiClient.Builder(this).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API, Plus.PlusOptions.builder().build()).addScope(Plus.SCOPE_PLUS_LOGIN).build();
////    mGoogleApiClient.connect();
////    }
////    protected void onStart() {
////        super.onStart();
////        mGoogleApiClient.connect();
////    }
////    protected void onStop() {
////        super.onStop();
////        if (mGoogleApiClient.isConnected()) {
////            mGoogleApiClient.disconnect();
////        }
////    }
//    
//    ConnectionCallbacks connectionCallbacks = new ConnectionCallbacks() {
//		
//		@Override
//		public void onConnectionSuspended(int arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//		
//		@Override
//		public void onConnected(Bundle arg0) {
//			// TODO Auto-generated method stub
//			
//		}
//	};
//    
//    @Override
//    public void onConnectionFailed(ConnectionResult result) {
//        if (!result.hasResolution()) {
//            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), activity, 0).show();
//            return;
//        }
//        if (!mIntentInProgress) {
//            // store mConnectionResult
//            mConnectionResult = result;
//            if (signedInUser) {
//                resolveSignInError();
//            }
//        }
//    }
////    @Override
////    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
////        switch (requestCode) {
////        case RC_SIGN_IN:
////            if (responseCode == RESULT_OK) {
////                signedInUser = false; 
////            }
////            mIntentInProgress = false;
////            if (!mGoogleApiClient.isConnecting()) {
////                mGoogleApiClient.connect();
////            }
////            
////            break;
////        }
////        Toast.makeText(activity, "activity result", Toast.LENGTH_LONG).show();
////    }
//    @Override
//    public void onConnected(Bundle arg0) {
//        signedInUser = false;
//        Toast.makeText(context, "Connected", Toast.LENGTH_LONG).show();
//        getProfileInformation();
//    }
//    
//    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                mConnectionResult.startResolutionForResult(activity, RC_SIGN_IN);
//            } catch (SendIntentException e) {
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//    
//    private void updateProfile(boolean isSignedIn) {
//        if (isSignedIn) {
//            signinFrame.setVisibility(View.GONE);
//            profileFrame.setVisibility(View.VISIBLE);
//        } else {
//            signinFrame.setVisibility(View.VISIBLE);
//            profileFrame.setVisibility(View.GONE);
//        }
//    }
//    private void getProfileInformation() {
//        try {
//            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
//                Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
//                String personName = currentPerson.getDisplayName();
//                String personPhotoUrl = currentPerson.getImage().getUrl();
//                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
// 
//                username.setText(personName);
//                emailLabel.setText(email);
//                new LoadProfileImage(image).execute(personPhotoUrl);
//                // update profile frame with new info about Google Account
//                // profile
//                updateProfile(true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//    @Override
//    public void onConnectionSuspended(int cause) {
//        mGoogleApiClient.connect();
//        updateProfile(false);
//    }
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//        case R.id.signin:
//            googlePlusLogin();
//            break;
//        }
//    }
//    public void signIn(View v) {
//        googlePlusLogin();
//    }
//    public void logout(View v) {
//        googlePlusLogout();
//    }
//    private void googlePlusLogin() {
//    	Toast.makeText(context,"6666", Toast.LENGTH_SHORT).show();
//    	System.out.println("666 googlePlusLogin called");
//        if (!mGoogleApiClient.isConnecting()) {
//        	Toast.makeText(context,"9999", Toast.LENGTH_SHORT).show();
//            signedInUser = true;
//            resolveSignInError();
//        }
//    }
//    private void googlePlusLogout() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//            updateProfile(false);
//        }
//    }
//    // download Google Account profile image, to complete profile
//    private class LoadProfileImage extends AsyncTask {
//        ImageView downloadedImage;
//        public LoadProfileImage(ImageView image) {
//            this.downloadedImage = image;
//        }
//        protected Bitmap doInBackground(String... urls) {
//            String url = urls[0];
//            Bitmap icon = null;
//            try {
//                InputStream in = new java.net.URL(url).openStream();
//                icon = BitmapFactory.decodeStream(in);
//            } catch (Exception e) {
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return icon;
//        }
//        protected void onPostExecute(Bitmap result) {
//            downloadedImage.setImageBitmap(result);
//        }
//		@Override
//		protected Object doInBackground(Object... params) {
//			// TODO Auto-generated method stub
//			return null;
//		}
//    }
}

//http://examples.javacodegeeks.com/android/android-google-plus-login-example/

package com.rgretail.grocermax.info;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import com.rgretail.grocermax.utils.UtilityMethods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchit-pc on 10-Jun-16.
 */
public class ContactInfoService extends IntentService {


    JSONObject informationData;
    JSONArray contact_info;
    JSONObject device_info;
    JSONArray application_info;
    public ContactInfoService() {
        super("ContactInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        informationData=new JSONObject();
        contact_info=new JSONArray();
        device_info=new JSONObject();
        application_info=new JSONArray();
        try {
            pushContacts();
            getApplicationDetails();
            getDeviceInfo();
            sendDataToServer();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void sendDataToServer(){
        try {
            informationData.put("Contact_info",contact_info);
            informationData.put("Application_info",application_info);
            informationData.put("Device_info",device_info);
            System.out.println("informationData.toString() = " + informationData.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void pushContacts() {

        int BATCH_SIZE = 50;

        //create getContactsFromOS() to fetch OS Contacts
        List<ContactModel> contactsList = getContactsFromOS();

        if (contactsList != null && contactsList.size() > 0) {

            // Batching contact push
            for (int i = 0; i < (contactsList.size() / BATCH_SIZE) + 1; i++) {
                List<ContactModel> subList = null;
                if ((i + 1) * BATCH_SIZE > contactsList.size()) {
                    subList = contactsList.subList(i * BATCH_SIZE, contactsList.size());
                }
                else {
                    subList = contactsList.subList(i * BATCH_SIZE, (i + 1) * BATCH_SIZE);
                }
                //push the contacts to the server using
                pushContacts(contactsList);
            }
        }
    }

    public void pushContacts(List<ContactModel> contactsList){
        for(int i=0;i<contactsList.size();i++){
            try {
                JSONObject cont=new JSONObject();
                cont.put("name",contactsList.get(i).getContact_name());
                cont.put("id",contactsList.get(i).getContact_id());
                JSONArray numberArray=new JSONArray();
                for(int j=0;j<contactsList.get(i).getContact_no().size();j++){
                    numberArray.put(contactsList.get(i).getContact_no().get(j));
                }
                JSONArray emailArray=new JSONArray();
                for(int j=0;j<contactsList.get(i).getContact_email().size();j++){
                    emailArray.put(contactsList.get(i).getContact_email().get(j));
                }
                cont.put("numbers",numberArray);
                cont.put("emails",emailArray);
                contact_info.put(cont);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void getDeviceInfo(){
        try {
            String OSNAME = System.getProperty("os.name");
            device_info.put("os_name",OSNAME);

            String OSVERSION = System.getProperty("os.version");
            device_info.put("os_version",OSVERSION);

            String RELEASE = android.os.Build.VERSION.RELEASE;
            device_info.put("release_version",RELEASE);

            String DEVICE = android.os.Build.DEVICE;
            device_info.put("device",DEVICE);

            String MODEL = android.os.Build.MODEL;
            device_info.put("model",MODEL);

            String PRODUCT = android.os.Build.PRODUCT;
            device_info.put("product",PRODUCT);

            String BRAND = android.os .Build.BRAND;
            device_info.put("brand",BRAND);

            String DISPLAY = android.os.Build.DISPLAY;
            device_info.put("display",DISPLAY);

            String MANUFACTURER = android.os.Build.MANUFACTURER;
            device_info.put("manufacturer",MANUFACTURER);

            String SERIAL = android.os.Build.SERIAL;
            device_info.put("serial",SERIAL);

            String USER = android.os.Build.USER;
            device_info.put("user",USER);

            device_info.put("device_id", UtilityMethods.getDeviceId(this));

            String HOST = android.os.Build.HOST;
            System.out.println("APP HOST = " + HOST);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public List<ContactModel> getContactsFromOS(){
        List<ContactModel> contactsList=new ArrayList<>();
        ContactModel contactModel;

        ContentResolver contactResolver = this.getContentResolver();
        Cursor cursor = contactResolver.query(ContactsContract.Contacts.CONTENT_URI, new String[] { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME, ContactsContract.Contacts.HAS_PHONE_NUMBER }, null, null, null);
        if(cursor.getCount()>0)
            while ( cursor.moveToNext()) {
                contactModel=new ContactModel();

                String displayName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                Log.d("TAG",  " Name: " + displayName);

                contactModel.setContact_name(displayName);
                contactModel.setContact_id(contactId);
                ArrayList<String> contact_no=new ArrayList<>();
                if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                {
                    Cursor pCur = contactResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[] { contactId }, null);
                    while (pCur.moveToNext())
                    {
                        String phone = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        String type = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
                        String s = (String) ContactsContract.CommonDataKinds.Phone.getTypeLabel(this.getResources(), Integer.parseInt(type), "");
                        Log.d("TAG", s + " phone: " + phone);
                        contact_no.add(phone);
                    }
                    pCur.close();
                }
                contactModel.setContact_no(contact_no);

                Cursor emailCursor = contactResolver.query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", new String[] { contactId }, null);
                ArrayList<String> contact_email=new ArrayList<>();
                while (emailCursor.moveToNext())
                {
                    String email = emailCursor.getString(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
                    int type = emailCursor.getInt(emailCursor.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));
                    String s = (String) ContactsContract.CommonDataKinds.Email.getTypeLabel(this.getResources(), type, "");
                    Log.d("TAG", s + " email: " + email);
                    contact_email.add(email);
                }
                emailCursor.close();
                contactModel.setContact_email(contact_email);

                contactsList.add(contactModel);
            }
        cursor.close();

        return contactsList;
    }


   public void getApplicationDetails(){

     //  List<AppsInfoModel> appsInfoList=new ArrayList<>();
       List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
       for (int i=0; i < packList.size(); i++)
       {
           try {
               JSONObject appInfo=new JSONObject();
               PackageInfo packInfo = packList.get(i);
               if ( (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
               {
                   String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                   String appPackage=packInfo.packageName;
                   String versionName=packInfo.versionName;
                   String versionCode=String.valueOf(packInfo.versionCode);
                   appInfo.put("appName",appName);
                   appInfo.put("appPackage",appPackage);
                   appInfo.put("versionName",versionName);
                   appInfo.put("versionCode",versionCode);
                   application_info.put(appInfo);
                   //appsInfoList.add(new AppsInfoModel(appPackage,appName,versionName,versionCode));

               }
           } catch (Exception e) {
               e.printStackTrace();
           }
       }

       /*for(int i=0;i<appsInfoList.size();i++){
           System.out.println("App number ="+(i+1));
           System.out.println("App name = " + appsInfoList.get(i).getApp_name());
           System.out.println("App package = " + appsInfoList.get(i).getApp_package_name());
           System.out.println("App versionName = " + appsInfoList.get(i).getApp_version_name());
           System.out.println("App versionCode = " + appsInfoList.get(i).getApp_version_code());
       }*/

   }



}

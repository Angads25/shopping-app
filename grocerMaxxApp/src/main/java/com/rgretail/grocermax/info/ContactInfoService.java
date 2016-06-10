package com.rgretail.grocermax.info;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by anchit-pc on 10-Jun-16.
 */
public class ContactInfoService extends IntentService {


    public ContactInfoService() {
        super("ContactInfoService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        pushContacts();
        getApplicationDetails();
        getDeviceInfo();
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
        System.out.println("New Batch");
        for(int i=0;i<contactsList.size();i++){
            System.out.println("Contct Name="+contactsList.get(i).getContact_name());
            System.out.println("Contct id="+contactsList.get(i).getContact_id());
            for(int j=0;j<contactsList.get(i).getContact_no().size();j++){
                System.out.println("Contct No"+j+"="+contactsList.get(i).getContact_no().get(j));
            }
            for(int j=0;j<contactsList.get(i).getContact_email().size();j++){
                System.out.println("Contct Email"+j+"="+contactsList.get(i).getContact_email().get(j));
            }
            System.out.println("Contct ------------------------------------------------------------------");
        }
        System.out.println("/////////////////////////////////////////////////////////////////////////");
    }

    public void getDeviceInfo(){
         String OSNAME = System.getProperty("os.name");
        System.out.println("APP OSNAME = " + OSNAME);
         String OSVERSION = System.getProperty("os.version");
        System.out.println("APPOSVERSION = " + OSVERSION);
         String RELEASE = android.os.Build.VERSION.RELEASE;
        System.out.println("APP RELEASE = " + RELEASE);
         String DEVICE = android.os.Build.DEVICE;
        System.out.println("APP DEVICE = " + DEVICE);
         String MODEL = android.os.Build.MODEL;
        System.out.println("APP MODEL = " + MODEL);
         String PRODUCT = android.os.Build.PRODUCT;
        System.out.println("APP PRODUCT = " + PRODUCT);
         String BRAND = android.os.Build.BRAND;
        System.out.println("APP BRAND = " + BRAND);
         String DISPLAY = android.os.Build.DISPLAY;
        System.out.println("APP DISPLAY = " + DISPLAY);
         String CPU_ABI = android.os.Build.CPU_ABI;
        System.out.println("APP CPU_ABI = " + CPU_ABI);
         String CPU_ABI2 = android.os.Build.CPU_ABI2;
        System.out.println("APP CPU_ABI2 = " + CPU_ABI2);
         String UNKNOWN = android.os.Build.UNKNOWN;
        System.out.println("APP UNKNOWN = " + UNKNOWN);
         String HARDWARE = android.os.Build.HARDWARE;
        System.out.println("APP HARDWARE = " + HARDWARE);
         String ID = android.os.Build.ID;
        System.out.println("APP ID = " + ID);
         String MANUFACTURER = android.os.Build.MANUFACTURER;
        System.out.println("APP MANUFACTURER = " + MANUFACTURER);
         String SERIAL = android.os.Build.SERIAL;
        System.out.println("APP SERIAL = " + SERIAL);
         String USER = android.os.Build.USER;
        System.out.println("APP USER = " + USER);
         String HOST = android.os.Build.HOST;
        System.out.println("APP HOST = " + HOST);



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


   public List<AppsInfoModel> getApplicationDetails(){

       List<AppsInfoModel> appsInfoList=new ArrayList<>();
       List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
       for (int i=0; i < packList.size(); i++)
       {
           PackageInfo packInfo = packList.get(i);
           if ( (packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)
           {
               String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
               String appPackage=packInfo.packageName;
               String versionName=packInfo.versionName;
               String versionCode=String.valueOf(packInfo.versionCode);
               appsInfoList.add(new AppsInfoModel(appPackage,appName,versionName,versionCode));
               Log.e("App № " + Integer.toString(i), appName);
           }
       }

       for(int i=0;i<appsInfoList.size();i++){
           System.out.println("App number ="+(i+1));
           System.out.println("App name = " + appsInfoList.get(i).getApp_name());
           System.out.println("App package = " + appsInfoList.get(i).getApp_package_name());
           System.out.println("App versionName = " + appsInfoList.get(i).getApp_version_name());
           System.out.println("App versionCode = " + appsInfoList.get(i).getApp_version_code());
       }

       return appsInfoList;
   }



}

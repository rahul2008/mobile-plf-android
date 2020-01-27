package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.appidentity.AppIdentityInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 310238655 on 6/1/2016.
 */
public class AppIndentityDemoPage extends AppCompatActivity {

    AppIdentityInterface mAppIdentityInterface = null;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appidentity);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        mAppIdentityInterface = appInfra.getAppIdentity();
        SecureStorageInterface mSecureStorage = appInfra.getSecureStorage();

        String enc = "4324332423432432432435425435435346465464547657567.000343242342";

        try {
            plainByte= enc.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }

        SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any
        encryptedByte=mSecureStorage.encryptData(plainByte,sseStore);
        try {
            String encBytesString = new String(encryptedByte, "UTF-8");
            Log.e("Encrypted Data",encBytesString);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] plainData= mSecureStorage.decryptData(encryptedByte,sseStore);
        String  result = Arrays.equals(plainByte,plainData)?"True":"False";
        try {
            String decBytesString = new String(plainByte, "UTF-8");
            Log.e("Decrypted Data",decBytesString);
        } catch (UnsupportedEncodingException e) {
        }
        AppTaggingInterface mAppTaggingInterface;
        mAppTaggingInterface = AILDemouAppInterface.getInstance().getAppInfra().getTagging().createInstanceForComponent("AppIndentityID", "AppIndentityIDVersion");

        mAppTaggingInterface.trackPageWithInfo("AppIndentityPageDemoPage", "AppIndentityKEy", "AppIndentityValue");


        try {
            ((TextView) findViewById(R.id.appNameValue)).setText(mAppIdentityInterface.getAppName());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        ((TextView) findViewById(R.id.localizedAppValue)).setText(mAppIdentityInterface.getLocalizedAppName());
        Log.i("getAppLocalizedNAme", "" + mAppIdentityInterface.getLocalizedAppName());


        try {

            ((TextView) findViewById(R.id.appVersionValue)).setText(mAppIdentityInterface.getAppVersion());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {
            if (mAppIdentityInterface.getAppState() != null) {
                ((TextView) findViewById(R.id.appStateValue)).setText(mAppIdentityInterface.getAppState().toString());
                Log.i("getAppState", "" + mAppIdentityInterface.getAppState());
            }
        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }
        try {
            ((TextView) findViewById(R.id.micrositeIdValue)).setText(mAppIdentityInterface.getMicrositeId());
            Log.i("getMicrositeId", "" + mAppIdentityInterface.getMicrositeId());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {

            ((TextView) findViewById(R.id.sectorValue)).setText(mAppIdentityInterface.getSector());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

        try {

            ((TextView) findViewById(R.id.servicediscoveryvalue)).setText(mAppIdentityInterface.getServiceDiscoveryEnvironment());

        } catch (IllegalArgumentException e) {
            Log.e("APPIDENTITY", e.getMessage());
            Toast.makeText(this, "" + e.toString(), Toast.LENGTH_LONG).show();
        }

    }


//        appInfra.getTagging().createInstanceForComponent("AppIdentityID", "AppIdentityVersion");


    //        mAppIdentityManager.loadJSONFromAsset();
//    Map testHash = new HashMap<String, String>();
//    testHash.put("Test","Test");
//    AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppIndentityDemoPage",testHash);

}




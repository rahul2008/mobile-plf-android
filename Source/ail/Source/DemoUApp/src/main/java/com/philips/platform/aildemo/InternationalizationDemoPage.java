package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class InternationalizationDemoPage extends AppCompatActivity {

    InternationalizationInterface mappIdentityinterface = null;
    AppTaggingInterface mAppTaggingInterface = null;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmain);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();

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
        mappIdentityinterface = appInfra.getInternationalization();
        mAppTaggingInterface = appInfra.getTagging().createInstanceForComponent("I18n", "I18nVersion");

        mAppTaggingInterface.trackPageWithInfo("InternationalizationDemoPage", "I18NKEy", "I18NValue");
        ((TextView) findViewById(R.id.localStringValue)).setText(mappIdentityinterface.getUILocaleString());
    }
}

package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.languagepack.LanguagePackInterface;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

public class LanguagePackActivity extends AppCompatActivity {

    private  LanguagePackInterface mLanguagePack;
    private  AppInfraInterface mAppInfra;
    private TextView overviewfileStatus, activatedUrl;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_pack);
        mLanguagePack = AILDemouAppInterface.getInstance().getAppInfra().getLanguagePack();
        SecureStorageInterface mSecureStorage = AILDemouAppInterface.getInstance().getAppInfra().getSecureStorage();

        String enc = "dsasdsa.000343242342";

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

        overviewfileStatus= (TextView) findViewById(R.id.overviewfileStatus);
        activatedUrl= (TextView) findViewById(R.id.activatedUrl);

        Button languagePackRefresh = (Button) findViewById(R.id.OverviewfileRefresh);
        Button languagePackActivate = (Button) findViewById(R.id.activate);
        languagePackRefresh.setOnClickListener(onClickRefresh());
        languagePackActivate.setOnClickListener(onClickActivate());
    }

    @NonNull
    private View.OnClickListener onClickActivate() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLanguagePack.activate(new LanguagePackInterface.OnActivateListener() {
                    @Override
                    public void onSuccess(String path) {
                        activatedUrl.setText(path);
                    }

                    @Override
                    public void onError(AILPActivateResult ailpActivateResult, String message) {
                        Log.e("LPactivity","error "+ailpActivateResult.toString() + "   "+message);
                        activatedUrl.setText(ailpActivateResult.toString() + "   "+message);
                    }

                });
            }
        };
    }

    @NonNull
    private View.OnClickListener onClickRefresh() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                overviewfileStatus.setText(null);
                mLanguagePack.refresh(new LanguagePackInterface.OnRefreshListener() {
                    @Override
                    public void onError(AILPRefreshResult error, String message) {
                        Log.e("LPactivity","error "+error.toString() + "   "+message);
                        overviewfileStatus.setText(error.toString() +"   "+message);
                    }

                    @Override
                    public void onSuccess(AILPRefreshResult result) {
                        Log.i("LPactivity","overview D/L success:  "+result);
                        overviewfileStatus.setText(result.toString());
                    }
                });

            }
        };
    }
}

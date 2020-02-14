package com.philips.platform.aildemo;


import android.app.Activity;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.util.Base64;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.securestoragev1.SecureStorageV1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class SecureStorageBulkActivity extends Activity {

    private static final String BULK_DATA_KEY = "bulk_data_key";
    private TextView bulkTextView;

    private AppInfraInterface appInfra;
    private SecureStorageInterface mSecureStorage;
    private byte[] encryptedData;
    private ScrollView scrollView;
    private ProgressBar progressBar;
    private boolean isOldSSEnabled;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage_bulk);
        bulkTextView = (TextView) findViewById(R.id.bulk_txt);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        progressBar=(ProgressBar)findViewById(R.id.bulk_activity_progress_bar);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        isOldSSEnabled=getIntent().getBooleanExtra(Constants.IS_OLD_SS_ENABLED,false);
        if(isOldSSEnabled) {
            mSecureStorage = new SecureStorageV1(appInfra);
            Toast.makeText(this,"Old secure storage is enabled",Toast.LENGTH_SHORT).show();
        }else{
            mSecureStorage=appInfra.getSecureStorage();
        }
    }

    public void onClick(View view) {
        SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any

        int id = view.getId();
        if (id == R.id.store_bulk_btn) {
            setProgressVisibility(true);
            mSecureStorage.storeValueForKey(BULK_DATA_KEY, readTxt(), sseStore);
            if (null != sseStore.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            }
            setProgressVisibility(false);
        } else if (id == R.id.read_bulk_btn) {
            setProgressVisibility(true);
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            String decryptedData = mSecureStorage.fetchValueForKey(BULK_DATA_KEY, sse);
            setProgressVisibility(false);
            if (null != sse.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            } else {
                bulkTextView.setText(decryptedData);
            }

        } else if (id == R.id.store_bulk_encrypt_btn) {
            setProgressVisibility(true);
            mSecureStorage.encryptBulkData(readTxt().getBytes(), new SecureStorageInterface.DataEncryptionListener() {
                @Override
                public void onEncryptionSuccess(final byte[] encryptedData) {
                    SecureStorageBulkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgressVisibility(false);
                            bulkTextView.setText(Base64.encodeToString(encryptedData,Base64.DEFAULT));
                        }
                    });
                }

                @Override
                public void onEncryptionError(final SecureStorageInterface.SecureStorageError secureStorageError) {
                    SecureStorageBulkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgressVisibility(false);
                            Toast.makeText(SecureStorageBulkActivity.this, secureStorageError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        } else if (id == R.id.read_bulk_decrypt_btn) {
            setProgressVisibility(true);

            mSecureStorage.decryptBulkData(Base64.decode(bulkTextView.getText().toString(), Base64.DEFAULT), new SecureStorageInterface.DataDecryptionListener() {
                @Override
                public void onDecryptionSuccess(final byte[] decryptedData) {
                    SecureStorageBulkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgressVisibility(false);
                            if(decryptedData!=null && readTxt().equalsIgnoreCase(new String(decryptedData))){
                                Toast.makeText(SecureStorageBulkActivity.this,"Text Match",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(SecureStorageBulkActivity.this,"Text didn't Match",Toast.LENGTH_SHORT).show();
                            }
                            bulkTextView.setText(new String(decryptedData));

                        }
                    });
                }

                @Override
                public void onDecyptionError(final SecureStorageInterface.SecureStorageError secureStorageError) {
                    SecureStorageBulkActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setProgressVisibility(false);
                            Toast.makeText(SecureStorageBulkActivity.this, secureStorageError.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            });
        } else if (id == R.id.scroll_bottom) {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else if (id == R.id.scroll_up) {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
        }
    }

    public void setProgressVisibility(boolean visibility){
        if(visibility){
            progressBar.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            scrollView.setVisibility(View.VISIBLE);
        }
    }


    private String readTxt() {

        InputStream inputStream = null;
        try {
            inputStream = getAssets().open("bulk_file");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ;
//     InputStream inputStream = getResources().openRawResource(R.raw.internals);
        System.out.println(inputStream);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        int i;
        try {
            i = inputStream.read();
            while (i != -1) {
                byteArrayOutputStream.write(i);
                i = inputStream.read();
            }
            inputStream.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return byteArrayOutputStream.toString();
    }
}

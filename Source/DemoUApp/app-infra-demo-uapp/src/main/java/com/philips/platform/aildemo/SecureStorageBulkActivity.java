package com.philips.platform.aildemo;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_secure_storage_bulk);
        bulkTextView = (TextView) findViewById(R.id.bulk_txt);
        scrollView = (ScrollView) findViewById(R.id.scroll_view);
        appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        mSecureStorage = appInfra.getSecureStorage();
    }

    public void onClick(View view) {
        SecureStorageInterface.SecureStorageError sseStore = new SecureStorageInterface.SecureStorageError(); // to get error code if any

        int id = view.getId();
        if (id == R.id.store_bulk_btn) {
            mSecureStorage.storeValueForKey(BULK_DATA_KEY, readTxt(), sseStore);
            if (null != sseStore.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sseStore.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            }
        } else if (id == R.id.read_bulk_btn) {
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            String decryptedData = mSecureStorage.fetchValueForKey(BULK_DATA_KEY, sse);
            if (null != sse.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            } else {
                bulkTextView.setText(decryptedData);
            }
        } else if (id == R.id.store_bulk_encrypt_btn) {
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            encryptedData = mSecureStorage.encryptData(readTxt().getBytes(), sse);
            if (null != sse.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            } else {
                bulkTextView.setText(new String(encryptedData));
            }

        } else if (id == R.id.read_bulk_decrypt_btn) {
            SecureStorageInterface.SecureStorageError sse = new SecureStorageInterface.SecureStorageError(); // to get error code if any
            byte[] decryptedData = mSecureStorage.decryptData(encryptedData, sse);
            if (null != sse.getErrorCode()) {
                Toast.makeText(SecureStorageBulkActivity.this, sse.getErrorCode().toString(), Toast.LENGTH_SHORT).show();
            } else {
                bulkTextView.setText(new String(decryptedData));
            }
        } else if (id == R.id.scroll_bottom) {
            scrollView.fullScroll(ScrollView.FOCUS_DOWN);
        } else if (id == R.id.scroll_up) {
            scrollView.fullScroll(ScrollView.FOCUS_UP);
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

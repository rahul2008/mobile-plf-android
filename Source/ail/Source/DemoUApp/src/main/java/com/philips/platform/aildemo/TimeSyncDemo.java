package com.philips.platform.aildemo;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;
import com.philips.platform.appinfra.timesync.TimeInterface;

import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by 310238655 on 6/30/2016.
 */
public class TimeSyncDemo extends AppCompatActivity {

    TimeInterface mTimeSyncInterface;
    Button refreshButton;
    SimpleDateFormat formatter;
    byte[] plainByte;
    byte[] encryptedByte;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesync_demopage);
        AILDemouAppInterface.getInstance().getAppInfra().getTime().refreshTime();

        SecureStorageInterface mSecureStorage = AILDemouAppInterface.getInstance().getAppInfra()
                .getSecureStorage();

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
        TextView localTimeTextView = (TextView) findViewById(R.id.localtime);
        final TextView localTimeTextvalue = (TextView) findViewById(R.id.localtimevalue);

        final TextView UTCtimeVal = (TextView) findViewById(R.id.utctimetextvalue);
        final TextView isSynchronized = (TextView) findViewById(R.id.isSynchronized);

        Button localTimeUpdateButton = (Button) findViewById(R.id.localtimebutton);
        refreshButton = (Button) findViewById(R.id.refreshbutton);
        Button syncButton = (Button) findViewById(R.id.syncbutton);

        mTimeSyncInterface = AILDemouAppInterface.getInstance().getAppInfra().getTime();

        //  AppInfraApplication.gAppInfra.getTagging().createInstanceForComponent("TimeSyncComponentID", "TimeSyncComponentVersion");
        //    AppInfraApplication.gAppInfra.getTagging().trackVideoStart("Time Synce");

        AILDemouAppInterface.getInstance().getAppInfra().getTagging().trackPageWithInfo("TimeSyncDemo", "TimeSyncDemoKey", "TimeSyncDemoVersion");

        localTimeTextvalue.setText(getDeviceTime());
//        utcTimeTextvalue.setText(mTimeSyncInterface.getUTCTime());
       Log.i("TimeSyncDemo", "UTCTime  " + mTimeSyncInterface.getUTCTime());
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
        localTimeUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                refreshButton.setVisibility(View.VISIBLE);
                localTimeTextvalue.setText(getDeviceTime());
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isSynchronized.setText("Not Synchronized");
                refreshButton.setVisibility(View.INVISIBLE);
                mTimeSyncInterface.refreshTime();
                Date date = mTimeSyncInterface.getUTCTime();
                formatter.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
                UTCtimeVal.setText(formatter.format(date));

            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimeSyncInterface.isSynchronized()){
                    refreshButton.setVisibility(View.VISIBLE);
                    isSynchronized.setText("Synchronized");
                }else{
                    isSynchronized.setText("Not Synchronized");
                }
                Date date = mTimeSyncInterface.getUTCTime();
                formatter.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
                UTCtimeVal.setText(formatter.format(date));
            }
        });


        Date date = mTimeSyncInterface.getUTCTime();
        formatter.setTimeZone(TimeZone.getTimeZone(TimeInterface.UTC));
        UTCtimeVal.setText(formatter.format(date));
    }

    public String getDeviceTime() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
        return formatter.format(c.getTime());
    }



}

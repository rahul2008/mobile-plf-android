package com.philips.platform.aildemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.timesync.TimeInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import java.text.SimpleDateFormat;
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesync_demopage);
        TextView localTimeTextView = (TextView) findViewById(R.id.localtime);
        final TextView localTimeTextvalue = (TextView) findViewById(R.id.localtimevalue);

        final TextView UTCtimeVal = (TextView) findViewById(R.id.utctimetextvalue);
        final TextView isSynchronized = (TextView) findViewById(R.id.isSynchronized);

        Button localTimeUpdateButton = (Button) findViewById(R.id.localtimebutton);
        refreshButton = (Button) findViewById(R.id.refreshbutton);
        Button syncButton = (Button) findViewById(R.id.syncbutton);

        mTimeSyncInterface = AILDemouAppInterface.mAppInfra.getTime();

        //  AppInfraApplication.gAppInfra.getTagging().createInstanceForComponent("TimeSyncComponentID", "TimeSyncComponentVersion");
        //    AppInfraApplication.gAppInfra.getTagging().trackVideoStart("Time Synce");

        AILDemouAppInterface.mAppInfra.getTagging().trackPageWithInfo("TimeSyncDemo", "TimeSyncDemoKey", "TimeSyncDemoVersion");

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
               /* Date date = mTimeSyncInterface.getUTCTime();
                formatter.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
                System.out.println("KAVYA DEMO"+" "+formatter.format(date));
                UTCtimeVal.setText(formatter.format(date));
*/
            }
        });

        syncButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mTimeSyncInterface.isSynchronized()){
                    refreshButton.setVisibility(View.VISIBLE);
                    isSynchronized.setText("Synchronized");
                }else{
                    refreshButton.setVisibility(View.INVISIBLE);
                    isSynchronized.setText("Not Synchronized");
                }
                Date date = mTimeSyncInterface.getUTCTime();
                formatter.setTimeZone(TimeZone.getTimeZone(TimeSyncSntpClient.UTC));
                System.out.println("KAVYA DEMO"+" "+formatter.format(date));
                UTCtimeVal.setText(formatter.format(date));
            }
        });
    }

    public String getDeviceTime() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
        return formatter.format(c.getTime());
    }



}

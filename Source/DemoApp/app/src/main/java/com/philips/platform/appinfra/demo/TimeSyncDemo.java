package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.timesync.TimeSyncInterface;
import com.philips.platform.appinfra.timesync.TimeSyncSntpClient;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 310238655 on 6/30/2016.
 */
public class TimeSyncDemo extends AppCompatActivity {

    TimeSyncInterface mTimeSyncInterface;
    SimpleDateFormat formatter;
     String dateString;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesync_demopage);
        TextView localTimeTextView = (TextView) findViewById(R.id.localtime);
        final TextView localTimeTextvalue = (TextView) findViewById(R.id.localtimevalue);
        TextView utcTimeTextView = (TextView) findViewById(R.id.utctime);
        final TextView utcTimeTextvalue = (TextView) findViewById(R.id.utctimetextvalue);

        Button localTimeUpdateButton = (Button) findViewById(R.id.localtimebutton);
        Button refreshButton = (Button) findViewById(R.id.refreshbutton);


        mTimeSyncInterface= AppInfraApplication.gAppInfra.getTimeSync();

        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("TimeSyncDemo", "SDKEy", "SDValue");
        formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
         dateString = formatter.format(new Date());
        localTimeTextvalue.setText(dateString);
//        utcTimeTextvalue.setText(mTimeSyncInterface.getUTCTime());
        Log.i("TimeSyncDemo", "UTCTime  "+mTimeSyncInterface.getUTCTime());

        localTimeUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dateString = formatter.format(new Date());
                localTimeTextvalue.setText(dateString);
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mTimeSyncInterface.refreshTime();
                utcTimeTextvalue.setText(mTimeSyncInterface.getUTCTime());
            }
        });
    }
}

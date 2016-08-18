package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.philips.platform.appinfra.timesync.TimeInterface;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by 310238655 on 6/30/2016.
 */
public class TimeSyncDemo extends AppCompatActivity {

    TimeInterface mTimeSyncInterface;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timesync_demopage);
        TextView localTimeTextView = (TextView) findViewById(R.id.localtime);
        final TextView localTimeTextvalue = (TextView) findViewById(R.id.localtimevalue);

        final TextView UTCtimeVal = (TextView) findViewById(R.id.utctimetextvalue);

        Button localTimeUpdateButton = (Button) findViewById(R.id.localtimebutton);
        Button refreshButton = (Button) findViewById(R.id.refreshbutton);

        mTimeSyncInterface = AppInfraApplication.gAppInfra.getTime();

        AppInfraApplication.gAppInfra.getTagging().createInstanceForComponent("TimeSyncComponentID", "TimeSyncComponentVersion");

        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("TimeSyncDemo", "TimeSyncDemoKey", "TimeSyncDemoVersion");

        localTimeTextvalue.setText(getDeviceTime());
//        utcTimeTextvalue.setText(mTimeSyncInterface.getUTCTime());
        Log.i("TimeSyncDemo", "UTCTime  " + mTimeSyncInterface.getUTCTime());

        localTimeUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                localTimeTextvalue.setText(getDeviceTime());
            }
        });
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
                mTimeSyncInterface.refreshTime();
               // Calendar c = mTimeSyncInterface.getUTCTime();
                UTCtimeVal.setText(formatter.format(mTimeSyncInterface.getUTCTime()));
            }
        });
    }

    public String getDeviceTime() {
        final Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS a");
        return formatter.format(c.getTime());
    }

}

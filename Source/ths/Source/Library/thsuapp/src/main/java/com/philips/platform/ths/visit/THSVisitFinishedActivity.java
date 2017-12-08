package com.philips.platform.ths.visit;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;

import com.americanwell.sdk.entity.visit.Visit;

import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_FINISHED_EXTRAS;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_RESULT_CODE;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_APP_SERVER_DISCONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_PROVIDER_CONNECTED;
import static com.americanwell.sdk.activity.VideoVisitConstants.VISIT_STATUS_VIDEO_DISCONNECTED;

/**
 * Created by philips on 8/4/17.
 */

public class THSVisitFinishedActivity extends AppCompatActivity {
int mResultcode;
    Bundle visitExtras;
    Visit mVisit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        visitExtras = getIntent().getBundleExtra(VISIT_FINISHED_EXTRAS);
        if (visitExtras != null) {




        }





            /*ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            ComponentName componentName = am.getRunningTasks(1).get(0).topActivity;*/







            //THSVisitFinishedActivity.this.finish();
        THSVisitFinishedActivity.this.finish();

/*

        Intent returnIntent = new Intent("visitFinishedResult");
        returnIntent.putExtras(visitExtras);
       LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(returnIntent);
*/




    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Intent returnIntent = new Intent("visitFinishedResult");
       // returnIntent.putExtras(visitExtras);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(returnIntent);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int u=12;

    }
}

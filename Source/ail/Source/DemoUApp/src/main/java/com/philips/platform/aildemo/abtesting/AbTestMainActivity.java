package com.philips.platform.aildemo.abtesting;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.philips.platform.aildemo.AILDemouAppInterface;
import com.philips.platform.appinfra.abtestclient.ABTestClientInterface;
import com.philips.platform.appinfra.demo.R;

public class AbTestMainActivity extends AppCompatActivity {

    private static final String TAG = AbTestMainActivity.class.getSimpleName();
    private ABTestClientInterface abTesting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.abtest_activity_main);
        abTesting = AILDemouAppInterface.getInstance().getAppInfra().getAbTesting();

    }


    public void onClick(View view) {
//        fetchDataFromFireBase(this);
        if (view.getId() == R.id.testValue) {
            Log.d("test value -- ",abTesting.getTestValue("experiment_variant", "some_default", ABTestClientInterface.UPDATETYPES.EVERY_APP_START, null));
        } else {
            abTesting.updateCache(new ABTestClientInterface.OnRefreshListener() {
                @Override
                public void onSuccess() {
                    Log.d("update success", " update successfully");
                }

                @Override
                public void onError(ERRORVALUES error, String message) {
                    Log.d("update failed", " update failed");
                }
            });
        }
    }
}

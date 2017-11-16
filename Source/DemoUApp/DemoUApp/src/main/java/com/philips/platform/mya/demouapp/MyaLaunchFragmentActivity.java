package com.philips.platform.mya.demouapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.platform.mya.interfaces.MyaListener;
import com.philips.platform.mya.launcher.MyaDependencies;
import com.philips.platform.mya.launcher.MyaInterface;
import com.philips.platform.mya.launcher.MyaLaunchInput;
import com.philips.platform.mya.launcher.MyaSettings;
import com.philips.platform.uappframework.launcher.FragmentLauncher;

public class MyaLaunchFragmentActivity extends AppCompatActivity implements MyaListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mya_launch_fragment);

        MyaInterface myaInterface = new MyaInterface();
        myaInterface.init(new MyaDependencies(MyAccountDemoUAppInterface.getAppInfra()), new MyaSettings(this));
        MyaLaunchInput uappLaunchInput = new MyaLaunchInput(this, this);
        myaInterface.launch(new FragmentLauncher(this, R.id.main_container, null), uappLaunchInput);

    }

    @Override
    public boolean onClickMyaItem(String itemName) {
        Log.d("Testing call back = ",itemName);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            super.onBackPressed();
        } else
            finish();
    }
}

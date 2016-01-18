package com.philips.hor_productselection_android;

import android.content.DialogInterface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.philips.multiproduct.MultiProductConfigManager;

public class Launcher extends FragmentActivity implements View.OnClickListener {

    private Button mButton = null;
    private MultiProductConfigManager mConfigManager = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        initUIReferences();

        mConfigManager = MultiProductConfigManager.getInstance();
        mConfigManager.initializeDigitalCareLibrary(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activitybutton:
                launchMultiProductModule();
                break;
        }
    }

    private void initUIReferences() {
        mButton = (Button) findViewById(R.id.activitybutton);

        mButton.setOnClickListener(this);
    }

    private void launchMultiProductModule() {
        mConfigManager.setLocale("en", "GB");
        mConfigManager.invokeDigitalCareAsActivity(R.anim.abc_fade_in, R.anim.abc_fade_out, MultiProductConfigManager.ActivityOrientation.SCREEN_ORIENTATION_PORTRAIT);
    }
}

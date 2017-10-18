package com.philips.cdp2.ews.demoapplication;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.philips.cdp2.ews.configuration.BaseContentConfiguration;
import com.philips.cdp2.ews.configuration.HappyFlowContentConfiguration;
import com.philips.cdp2.ews.helper.EWSLaunchHelper;

public class EWSDemoActivity extends AppCompatActivity implements View.OnClickListener {

    private Switch configurationSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ewsdemo);
        findViewById(R.id.launchEWS).setOnClickListener(this);
        configurationSwitch = ((Switch)findViewById(R.id.switch1));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.launchEWS:
                new EWSLaunchHelper(this) {
                    @Override
                    public BaseContentConfiguration getBaseContentConfiguration() {
                        return createBaseContentConfiguration();
                    }

                    @Override
                    public HappyFlowContentConfiguration getHappyFlowContentConfiguration() {
                        return createHappyFlowConfiguration();
                    }
                }.launch();
                break;
            default:
                break;
        }
    }

    private BaseContentConfiguration createBaseContentConfiguration(){
        if (configurationSwitch.isChecked()){
            return new BaseContentConfiguration(R.string.lbl_appname, R.string.lbl_devicename);
        }
        return null;
    }

    private HappyFlowContentConfiguration createHappyFlowConfiguration(){
        if (configurationSwitch.isChecked()){
            return new HappyFlowContentConfiguration.HappyFlowConfigurationBuilder()
                    .setEWS_01_Title(R.string.lbl_connectwithdevice)
                    .setEWS02_01_Title(R.string.lbl_ews_02_01_title)
                    .setEWS02_01_Body(R.string.lbl_ews_02_01_body)
                    .build();
        }
        return null;
    }
}

package com.philips.platform.aildemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.demo.R;
import com.philips.platform.appinfra.internationalization.InternationalizationInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

/**
 * Created by 310238655 on 6/2/2016.
 */
public class InternationalizationDemoPage extends AppCompatActivity {

    InternationalizationInterface mappIdentityinterface = null;
    AppTaggingInterface mAppTaggingInterface = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_localmain);
        AppInfraInterface appInfra = AILDemouAppInterface.getInstance().getAppInfra();
        mappIdentityinterface = appInfra.getInternationalization();
        mAppTaggingInterface = appInfra.getTagging().createInstanceForComponent("I18n", "I18nVersion");

        mAppTaggingInterface.trackPageWithInfo("InternationalizationDemoPage", "I18NKEy", "I18NValue");
        ((TextView) findViewById(R.id.localStringValue)).setText(mappIdentityinterface.getUILocaleString());
    }
}

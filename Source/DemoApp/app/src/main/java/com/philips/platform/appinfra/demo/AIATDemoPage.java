/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.philips.platform.appinfra.tagging.AIAppTaggingInterface;

import java.util.HashMap;


/**
 * Created by 310238655 on 4/27/2016.
 */
public class AIATDemoPage extends AppCompatActivity {
    EditText key;
    EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagging_demo_page);

         key= (EditText)findViewById(R.id.tagg_key_field);
         value= (EditText)findViewById(R.id.tagg_value_filed);

        Button TaggPageBtn =  (Button) findViewById(R.id.tagg_page_button);
        Button TaggActionBtn =  (Button) findViewById(R.id.tagg_action_button);
        Button TaggOptInBtn =  (Button) findViewById(R.id.opt_in_btn);
        Button TaggOptOutBtn =  (Button) findViewById(R.id.opt_out_btn);
        Button TaggUnknownBtn =  (Button) findViewById(R.id.opt_unknown_btn);


        TaggPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  if(null==key.getText().toString() || key.getText().toString().isEmpty() || null==value.getText().toString() || value.getText().toString().isEmpty()){
                // invalid key value

            }else {
                if (key.getText().toString().contains(",")) { // if multiple keys passed
                    HashMap<String, String> keyValuePair ;
                    String[] keyArray  =key.getText().toString().split(",");
                    String[] valueArray =value.getText().toString().split(",");
                    if(keyArray.length >0 && keyArray.length==valueArray.length){ // number of keys should be same as that of values
                        keyValuePair = new HashMap<String, String>();
                        for(int keyCount=0;keyCount<keyArray.length;keyCount++){
                            keyValuePair.put(keyArray[keyCount].trim(),valueArray[keyCount].trim());
                        }
                        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
                    }
                }else { // if single key present
                    AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", key.getText().toString(), value.getText().toString());
                }
//                AIAppTaggingWrapper.setTrackingIdentifier(""+value.getText().toString());
//                AIAppTaggingWrapper.setLaunchingPageName("demoapp:AIAppTagging");
//                AIAppTaggingWrapper.setComponentVersionKey(""+key.getText().toString());
//                AIAppTaggingWrapper.setComponentVersionVersionValue("FrameworkTaggingValue");

//                AIAppTaggingWrapper.trackPage("DemoTaggingPAge", ""+key.getText().toString(), ""+value.getText().toString());
//                AIAppTaggingWrapper.trackAction("ButtonClick","Key", null );
            }

            }
        });
        TaggActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage",key.getText().toString(), value.getText().toString());
//               AIAppTaggingWrapper.trackAction("ButtonClick","Key", null );

            }
        });
        TaggOptInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.OPTIN);

//                AIAppTaggingWrapper.setPrivacyStatus(AIAppTaggingWrapper.AIATMobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_IN);

            }
        });
        TaggOptOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.OPTOUT);

//                AIAppTaggingWrapper.setPrivacyStatus(AIAppTaggingWrapper.AIATMobilePrivacyStatus.MOBILE_PRIVACY_STATUS_OPT_OUT);
            }
        });
        TaggUnknownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AIAppTaggingInterface.PrivacyStatus.UNKNOWN);

//                AIAppTaggingWrapper.setPrivacyStatus(AIAppTaggingWrapper.AIATMobilePrivacyStatus.MOBILE_PRIVACY_STATUS_UNKNOWN);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
//        AIAppTaggingWrapper.collectLifecycleData();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        AIAppTaggingWrapper.pauseCollectingLifecycleData();
    }
}

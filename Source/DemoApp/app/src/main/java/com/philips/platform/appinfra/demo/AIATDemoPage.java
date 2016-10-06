/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.philips.platform.appinfra.tagging.AppTaggingInterface;

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


        Button TaggActionStartBtn =  (Button) findViewById(R.id.actionstart);
        Button TaggActionEndBtn =  (Button) findViewById(R.id.actionend);
        Button TaggActionUpdateBtn  =  (Button) findViewById(R.id.actionupdate);
        Button TaggActionExistBtn=  (Button) findViewById(R.id.actionexists);
        Button CheckForSslBtn =  (Button) findViewById(R.id.checkssl);

        TaggActionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
                AppInfraApplication.mAIAppTaggingInterface.trackTimedActionStart("Tagging_trackTimedActionStart");
            }
        });

        TaggActionEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
                AppInfraApplication.mAIAppTaggingInterface.trackTimedActionEnd("Tagging_trackTimedActionStart");
            }
        });

        CheckForSslBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", "SSlCheck Key", "SSlCheck Value");
            }
        });

        TaggActionExistBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackingTimedActionExists("Tagging_trackingTimedActionExists");
            }
        });
        TaggActionUpdateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackTimedActionUpdate("Tagging_trackTimedActionUpdate");
            }
        });


        TaggPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {  if(null==key.getText().toString() || key.getText().toString().isEmpty() || null==value.getText().toString() || value.getText().toString().isEmpty()){
                // invalid key value
                Toast.makeText(AIATDemoPage.this,"Please enter any Key value pair", Toast.LENGTH_SHORT).show();

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
                }else {
                    AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", key.getText().toString(), value.getText().toString());
                }
            }

            }
        });
        TaggActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(null==key.getText().toString() || key.getText().toString().isEmpty() || null==value.getText().toString() || value.getText().toString().isEmpty()){
                    // invalid key value
                    Toast.makeText(AIATDemoPage.this,"Please enter any Key value pair", Toast.LENGTH_SHORT).show();

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
                            AppInfraApplication.mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
                        }
                    }else {
                        AppInfraApplication.mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", key.getText().toString(), value.getText().toString());
                    }
                }


            }
        });
        TaggOptInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
            }
        });
        TaggOptOutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTOUT);
            }
        });
        TaggUnknownBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.UNKNOWN);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}

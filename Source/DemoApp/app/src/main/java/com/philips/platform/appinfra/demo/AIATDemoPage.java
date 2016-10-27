/* Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */
package com.philips.platform.appinfra.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.philips.cdp.localematch.enums.Sector;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.HashMap;


/**
 * Created by 310238655 on 4/27/2016.
 */
public class AIATDemoPage extends AppCompatActivity {
    EditText key;
    EditText value;

    AppTaggingInterface.SocialMedium sSocialMedium;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tagging_demo_page);

        key = (EditText) findViewById(R.id.tagg_key_field);
        value = (EditText) findViewById(R.id.tagg_value_filed);

        Button TaggPageBtn = (Button) findViewById(R.id.tagg_page_button);
        Button TaggActionBtn = (Button) findViewById(R.id.tagg_action_button);
        Button TaggOptInBtn = (Button) findViewById(R.id.opt_in_btn);
        Button TaggOptOutBtn = (Button) findViewById(R.id.opt_out_btn);
        Button TaggUnknownBtn = (Button) findViewById(R.id.opt_unknown_btn);


        Button TaggActionStartBtn = (Button) findViewById(R.id.actionstart);
        Button TaggActionEndBtn = (Button) findViewById(R.id.actionend);
        Button CheckForSslBtn = (Button) findViewById(R.id.checkssl);

        Button TaggLinkExternal = (Button) findViewById(R.id.link_external);
        Button TaggVideoStart = (Button) findViewById(R.id.video_start);
        Button TaggVideoEnd = (Button) findViewById(R.id.video_end);
        Button TaggFileDownload = (Button) findViewById(R.id.file_download);

        Button Setdata = (Button) findViewById(R.id.setdata);
        Button getData= (Button) findViewById(R.id.getdata);

        Spinner social_share_spinner = (Spinner) findViewById(R.id.Spinner_social_share);

        String mSector[];
        mSector = getResources().getStringArray(R.array.social_share_list);
        ArrayAdapter<String> mSector_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mSector);
        social_share_spinner.setAdapter(mSector_adapter);
        social_share_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                sSocialMedium = AppTaggingInterface.SocialMedium.valueOf(parent.getAdapter().getItem(position).toString());
                Log.i("Social Medium Value", "" + parent.getAdapter().getItem(position).toString());
                switch (parent.getAdapter().getItem(position).toString()) {

                    case "facebook":
                        AppInfraApplication.mAIAppTaggingInterface.trackSocialSharing(AppTaggingInterface.SocialMedium.Facebook, "Tagging_trackSocial_FacceBook");
                        break;
                    case "mail":
                        AppInfraApplication.mAIAppTaggingInterface.trackSocialSharing(AppTaggingInterface.SocialMedium.Mail, "Tagging_trackSocial_Mail");
                        break;
                    case "twitter":
                        AppInfraApplication.mAIAppTaggingInterface.trackSocialSharing(AppTaggingInterface.SocialMedium.Twitter, "Tagging_trackSocial_Twitter");
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Setdata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.setPrivacyConsentForSensitiveData(true);
            }
        });
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("get Sensitive data", ""+AppInfraApplication.mAIAppTaggingInterface.getPrivacyConsentForSensitiveData());
            }
        });

        TaggLinkExternal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackLinkExternal("Tagging_trackLinkExternal");
            }
        });
        TaggVideoStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
//                AppInfraApplication.mAIAppTaggingInterface.trackVideoStart("Tagging_trackVideoStart");
            }
        });
        TaggVideoEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
//                AppInfraApplication.mAIAppTaggingInterface.trackVideoStart("Tagging_trackVideoStart");
            }
        });
        TaggFileDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackFileDownload("Tagging_trackFileDownload");
            }
        });

        TaggActionStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
//                AppInfraApplication.mAIAppTaggingInterface.trackTimedActionStart("Tagging_trackTimedActionStart");
            }
        });

        TaggActionEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AIATDemoPage.this, AndroidMediaPlayerExample.class);
                startActivity(intent);
//                AppInfraApplication.mAIAppTaggingInterface.trackTimedActionEnd("Tagging_trackTimedActionStart");
            }
        });

        CheckForSslBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", "SSlCheck Key", "SSlCheck Value");
            }
        });

               TaggPageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null == key.getText().toString() || key.getText().toString().isEmpty() || null == value.getText().toString() || value.getText().toString().isEmpty()) {
                    // invalid key value
                    Toast.makeText(AIATDemoPage.this, "Please enter any Key value pair", Toast.LENGTH_SHORT).show();

                } else {
                    if (key.getText().toString().contains(",")) { // if multiple keys passed
                        HashMap<String, String> keyValuePair;
                        String[] keyArray = key.getText().toString().split(",");
                        String[] valueArray = value.getText().toString().split(",");
                        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
                            keyValuePair = new HashMap<String, String>();
                            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
                            }
                            AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", keyValuePair);
                        }
                    } else {
                        AppInfraApplication.mAIAppTaggingInterface.trackPageWithInfo("AppTaggingDemoPage", key.getText().toString(), value.getText().toString());
                    }
                }

            }
        });
        TaggActionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (null == key.getText().toString() || key.getText().toString().isEmpty() || null == value.getText().toString() || value.getText().toString().isEmpty()) {
                    // invalid key value
                    Toast.makeText(AIATDemoPage.this, "Please enter any Key value pair", Toast.LENGTH_SHORT).show();

                } else {
                    if (key.getText().toString().contains(",")) { // if multiple keys passed
                        HashMap<String, String> keyValuePair;
                        String[] keyArray = key.getText().toString().split(",");
                        String[] valueArray = value.getText().toString().split(",");
                        if (keyArray.length > 0 && keyArray.length == valueArray.length) { // number of keys should be same as that of values
                            keyValuePair = new HashMap<String, String>();
                            for (int keyCount = 0; keyCount < keyArray.length; keyCount++) {
                                keyValuePair.put(keyArray[keyCount].trim(), valueArray[keyCount].trim());
                            }
                            AppInfraApplication.mAIAppTaggingInterface.trackActionWithInfo("AppTaggingDemoPage", keyValuePair);
                        }
                    } else {
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

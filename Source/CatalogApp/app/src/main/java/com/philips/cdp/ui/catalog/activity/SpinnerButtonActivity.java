/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UiKitSpinnerButton;
import com.philips.cdp.uikit.drawable.VectorDrawable;

/**
 * Created by 310240027 on 5/6/2016.
 */
public class SpinnerButtonActivity extends CatalogActivity implements View.OnClickListener {
    private UiKitSpinnerButton spinnerOnButton;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    private Button stopProgressButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogapp_spinner_button);
        spinnerOnButton = (UiKitSpinnerButton) findViewById(R.id.spinnerOnButton);
        spinnerOnButton.setButtonText("Philips account");
        spinnerOnButton.setDrawable(VectorDrawable.create(getApplicationContext(), com.philips.cdp.uikit.R.drawable.uikit_philips_logo_small).getConstantState().newDrawable().mutate(),"left");
        stopProgressButton = (Button) findViewById(R.id.stopProgress);
        spinnerOnButton.setOnClickListener(this);
        stopProgressButton.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.spinnerOnButton:
                spinnerOnButton.setProgress(0);
                progressStatus= 0;
                new Thread(new Runnable() {
                    public void run() {
                        while (progressStatus < 100) {
                            progressStatus ++;
                            // Update the progress bar and display the
                            //current value in the text view
                            Runnable runnable = new Runnable() {
                                public void run() {
                                    spinnerOnButton.setProgress(progressStatus);

                                }
                            };
                            handler.post(runnable);
                            try {
                                // Sleep for 200 milliseconds.
                                //Just to display the progress slowly
                                Thread.sleep(200);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }).start();
                break;
            case R.id.stopProgress:
                spinnerOnButton.disableProgress("Finish Loading");
                break;
        }
    }
}

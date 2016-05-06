/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.UiKitSpinnerButton;

/**
 * Created by 310240027 on 5/6/2016.
 */
public class SpinnerButtonActivity extends CatalogActivity {
    UiKitSpinnerButton spinnerOnButton;
    private int progressStatus = 0;
    private Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.catalogapp_spinner_button);
        spinnerOnButton = (UiKitSpinnerButton) findViewById(R.id.spinnerOnButton);
    }

    @Override
    protected void onResume() {
        super.onResume();
        new Thread(new Runnable() {
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 1;
                    // Update the progress bar and display the
                    //current value in the text view
                    handler.post(new Runnable() {
                        public void run() {
                            spinnerOnButton.setProgress(progressStatus);


                        }
                    });
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
    }
}

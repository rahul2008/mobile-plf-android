package com.philips.cdp.ui.catalog.activity;

import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ProgressBar;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiPopoverAlert;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.utils.FontIconUtils;

public class ButtonsActivity extends CatalogActivity {

    private PuiSwitch changeButtonState;
    private Button themeButton, outlinedButton, transparentButton, whiteTranspararentButton;
    private Button showNotification;
    private PuiPopoverAlert puiPopoverAlert;
    private ProgressBar popoverProgress;
    private int progress = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_button);

        changeButtonState = (PuiSwitch) findViewById(R.id.change_button_state);
        changeButtonState.setChecked(true);
        themeButton = (Button) findViewById(R.id.theme_button);
        outlinedButton = (Button) findViewById(R.id.outlined_button);
        transparentButton = (Button) findViewById(R.id.outlined_transparent_button);
        whiteTranspararentButton = (Button) findViewById(R.id.outlined_transparent_white_button);

        showNotification = (Button) findViewById(R.id.show_notification);

        showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                puiPopoverAlert.show();
                new ProgresAsyncTask().execute();
            }
        });

        changeButtonState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final CompoundButton buttonView, final boolean isChecked) {
                themeButton.setEnabled(isChecked);
                outlinedButton.setEnabled(isChecked);
                transparentButton.setEnabled(isChecked);
                whiteTranspararentButton.setEnabled(isChecked);
            }
        });

        puiPopoverAlert = (PuiPopoverAlert) findViewById(R.id.popover_alert);
        puiPopoverAlert.setLeftIcon(FontIconUtils.getInfo(this, FontIconUtils.ICONS.INFO, 22, Color.WHITE,
                false));
        puiPopoverAlert.setRightIcon(FontIconUtils.getInfo(this, FontIconUtils.ICONS.CROSS, 15, Color.WHITE,
                false));
        popoverProgress = puiPopoverAlert.getProgressBar();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private class ProgresAsyncTask extends AsyncTask<Void, Integer, Void> {
        private Object lock = new Object();

        @Override
        protected Void doInBackground(final Void... params) {
            synchronized (lock) {
                while (progress <= 100) {
                    try {
                        lock.wait(5);
                    } catch (InterruptedException ie) {

                    }
                    publishProgress(progress);
                    progress++;
                }
                //publishProgress(100);
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(final Integer... values) {
            popoverProgress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(final Void aVoid) {
            synchronized (lock) {
                popoverProgress.setProgress(100);
                new Handler(getMainLooper()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        puiPopoverAlert.dismiss();
                        progress = 0;
                    }
                }, 500);
            }
        }
    }
}

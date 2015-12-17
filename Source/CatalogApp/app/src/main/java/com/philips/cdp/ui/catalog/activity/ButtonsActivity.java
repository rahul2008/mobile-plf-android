package com.philips.cdp.ui.catalog.activity;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiPopoverAlert;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.FontIconUtils;

public class ButtonsActivity extends CatalogActivity {

    private PuiSwitch changeButtonState;
    private Button themeButton, outlinedButton, transparentButton, whiteTranspararentButton;
    private Button showNotification;
    private PuiPopoverAlert puiPopoverAlert;
    private ProgressBar popoverProgress;
    private int progress = 0;
    private AsyncTask notificationTask;

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

        puiPopoverAlert = (PuiPopoverAlert) findViewById(R.id.popover_alert);

        showNotification = (Button) findViewById(R.id.show_notification);

        showNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                progress = 0;
                puiPopoverAlert.getProgressBar().setProgress(0);
                notificationTask = new ProgresAsyncTask().execute();
                puiPopoverAlert.show();
            }
        });

        ImageView closeIcon = (ImageView) findViewById(R.id.uikit_popover_close_icon);
        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (!notificationTask.isCancelled() || notificationTask.getStatus() !=
                        AsyncTask.Status.FINISHED) {
                    notificationTask.cancel(true);
                    puiPopoverAlert.dismiss();
                    progress = 0;
                }
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


        puiPopoverAlert.setLeftIcon(FontIconUtils.getInfo(this, FontIconUtils.ICONS.INFO, 22, Color.WHITE,
                false));
        puiPopoverAlert.setRightIcon(FontIconUtils.getInfo(this, FontIconUtils.ICONS.CROSS, 15, Color.WHITE,
                false));
        popoverProgress = puiPopoverAlert.getProgressBar();

        //Set the icon in springboard icon buttons
        Button spIconButton = (Button) findViewById(R.id.spButtonIcon);
        spIconButton.setCompoundDrawables(getSettingsIcon(),null,null,null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    private Drawable getSettingsIcon() {
        Resources r = getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33,
                r.getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, r
                .getDisplayMetrics());
        Drawable d = VectorDrawable.create(this, R.drawable.uikit_gear);
        d.setBounds(0,0,(int)width,(int)height);
        return d;
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
                        break;
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

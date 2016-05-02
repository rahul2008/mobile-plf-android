/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

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
import com.philips.cdp.uikit.customviews.PopoverAlert;
import com.philips.cdp.uikit.customviews.PuiSwitch;
import com.philips.cdp.uikit.drawable.VectorDrawable;
import com.philips.cdp.uikit.utils.FontIconUtils;

/**
 *
 *     Different theme attributes can be used to apply button styles
 *     Avaialable attributes are <b>uikit_outlinedButtonStyle, outlinedButtonTransparentStyle,
 *     springBoardButton, springBoardButtonWithIcon, springBoardButtonOutLined,
 *     springBoardButtonOutLinedWithIcon</b>.
 *     <p>
 *         Usage: <b>style="?attr/uikit_oneofbuttonStyles"</b>
 *     <br>
 *         Example:
 *              <pre>
 *                     &lt;Button
 *                          android:layout_width="wrap_content"
 *                          android:layout_height="wrap_content"
 *                          <b>style="?attr/uikit_springBoardButton"</b>/&gt;
 *                  </pre>
 *     </p>
 * <p>
 *     UIKit provides lot of default background selector for different themes.
 *     Different background can be used if themed background is not applicable.<br>
 *     Default style for dark blue theme is as below:
 *     <pre>
 *         &lt;style name="Button.Solid.DarkBlue"&gt;
 *              &lt;item name="android:background"&gt;@drawable/uikit_dark_blue_selector&lt;/item&gt;
 *         &lt;/style&gt;
 *         <br>
 *         &lt;style name="Button.OutlinedDarkBlue"&gt;
 *              &lt;item name="android:textColor"&gt;@drawable/uikit_dark_blue_outlined_text_selector&lt;/item&gt;
 *              &lt;item name="android:background"&gt;@drawable/uikit_dark_blue_outlined_selector&lt;/item&gt;
 *         &lt;/style&gt;
 *     </pre>
 * </p>
 * <p>
 *     <p>
 *         <H4>Outlined SpringBoard Buttons with Icons</H4>
 *              Supporting tint on compound drawable is not default.
 *              Use {@link com.philips.cdp.uikit.customviews.UIKitButton} with outlined styled
 *              button style.
 *              Example: <br>
 *                  <pre>
 *                     &lt;com.philips.cdp.uikit.customviews.UIKitButton
 *                          android:layout_width="wrap_content"
 *                          android:layout_height="wrap_content"
 *                          style="?attr/uikit_springBoardButtonOutLinedWithIcon"/&gt;
 *                  </pre>
 *     </p>
 *     For all the available styles please refer below files in aar file
 *
 *     <ul>
 *          <li><b>Buttons:</b> values/uikit_style_buttons.xml  </li>
 *          <li><b>SpringBoard Buttons:</b> values/uikit_style_springboard_buttons.xml </li>
 *          <li><b>SpringBoard Buttons with icon:</b> values/uikit_style_springboard_buttons_with_icon.xml</li>
 *          </ul>
 * </p>
 * <p>Below pic explains the different styles and effect on UI.
 * <img src="../../../../../../img/all_buttons.png"
 *      alt="Buttons available with UIKit Styling." border="0" /></p>
 */
public class ButtonsActivity extends CatalogActivity {

    private PuiSwitch changeButtonState;
    private Button themeButton, outlinedButton, transparentButton, whiteTranspararentButton;
    private Button showNotification;
    private PopoverAlert puiPopoverAlert;
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


        puiPopoverAlert = (PopoverAlert) findViewById(R.id.popover_alert);

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
        spIconButton.setCompoundDrawables(getSettingsIcon(), null, null, null);

        //SpringBoard outlined button with tintable drawable
        Button spIconButtonOutLined = (Button) findViewById(R.id.spButtonOutlinedWithIcon);
        spIconButtonOutLined.setCompoundDrawables(getSettingsIcon(), null, null, null);
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
        Drawable d = VectorDrawable.create(this, R.drawable.uikit_gear).mutate();
        d.setBounds(0, 0, (int) width, (int) height);
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

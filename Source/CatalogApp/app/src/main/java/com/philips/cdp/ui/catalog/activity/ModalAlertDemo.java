/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.ui.catalog.ModalAlertDemoFragment;
import com.philips.cdp.ui.catalog.R;

/**
 * <b>Find the below steps to use Modal Alert</b><br>
 *     <pre>
 *         1. Create new Fragment which extends BlurDialogFragment for example <b>ModalAlertDemoFragment</b> and override onCreateView()
 *         2. Based on requirement call below code to show modal alert
 *                  ModalAlertDemoFragment modalAlertDemoFragment = new ModalAlertDemoFragment();
 *                  modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
 *         3. Find below code for reference
 *                  public class ModalAlertDemoFragment extends BlurDialogFragment {
 *
 *                  Override
 *                  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
 *                  View v = inflater.inflate(com.philips.cdp.uikit.R.layout.uikit_modal_alert, container, false);
 *                  return v;
 *                }
 *     </pre>
 */
public class ModalAlertDemo extends CatalogActivity {

    private Bundle savedInstanceState;
    private ModalAlertDemoFragment modalAlertDemoFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        setContentView(R.layout.modal_alert_demo);

        Button showAlert = (Button) findViewById(R.id.show_modal_dialog);
        showAlert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                modalAlertDemoFragment = new ModalAlertDemoFragment();
                modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (modalAlertDemoFragment != null && modalAlertDemoFragment.isVisible()) {
            outState.putBoolean("dialogState", true);
            modalAlertDemoFragment.dismiss();
        }

        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        new Handler(getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (savedInstanceState != null && savedInstanceState.getBoolean("dialogState")) {
                    modalAlertDemoFragment = new ModalAlertDemoFragment();
                    modalAlertDemoFragment.show(getSupportFragmentManager(), "dialog");
                }
            }
        }, 100);
    }

}
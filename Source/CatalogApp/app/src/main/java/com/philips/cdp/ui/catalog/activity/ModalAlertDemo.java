package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.LinearLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsDialog;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ModalAlertDemo extends CatalogActivity {
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.modal_alert_demo);
        findViewById(R.id.show_modal_dialog).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final PhilipsDialog alert = new PhilipsDialog(ModalAlertDemo.this);
                alert.setContentView(R.layout.uikit_modal_alert);
                LinearLayout parent = (LinearLayout)alert.findViewById(R.id.parent_id);
                Button justOnce = (Button) alert.findViewById(R.id.dialogButtonCancel);
                Button always = (Button) alert.findViewById(R.id.dialogButtonOK);
                justOnce.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                always.setOnClickListener(ModalAlertDemo.this.onClick(alert));
                alert.show();

                Animation animationScaleDown = AnimationUtils.loadAnimation(ModalAlertDemo.this, R.anim.zoom_out);

                AnimationSet growShrink = new AnimationSet(true);

                growShrink.addAnimation(animationScaleDown);
                parent.startAnimation(growShrink);

            }
        });

    }

    @NonNull
    private View.OnClickListener onClick(final PhilipsDialog alert) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation animationScaleUp = AnimationUtils.loadAnimation(ModalAlertDemo.this, R.anim.zoom_in);
                AnimationSet growShrinkTest = new AnimationSet(false);
                growShrinkTest.addAnimation(animationScaleUp);
                LinearLayout parent = null;
                parent = (LinearLayout)alert.findViewById(R.id.parent_id);
                parent.startAnimation(growShrinkTest);
                alert.dismiss();

            }
        };
    }


}

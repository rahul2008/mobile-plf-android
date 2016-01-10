package com.philips.cdp.ui.catalog.activity;

import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.MultiStateControls;

public class ControlsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        MultiStateControls controls = (MultiStateControls) this.findViewById(R.id.controls);
        controls.enableMultipleChoice(false);
        TypedArray controlIcons = getResources()
                .obtainTypedArray(R.array.control_icons);
        int[] resIds = new int[controlIcons.length()];
        for (int i = 0; i < controlIcons.length(); i++) {
            resIds[i] = controlIcons.getResourceId(i, -1);
        }
        controlIcons.recycle();
        controls.drawControlsWithImageBackground(resIds, true);
        controls.setOnButtonStateChangedListener(new MultiStateControls.OnButtonStateChangeListener() {
            @Override
            public void onButtonStateChanged(int position) {
                Log.d(getClass() + "", "Position: " + position);
            }
        });

    }

}

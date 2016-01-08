package com.philips.cdp.ui.catalog.activity;

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
        controls.enableMultipleChoice(true);
        controls.setOnButtonStateChangedListener(new MultiStateControls.OnButtonStateChangeListener() {
            @Override
            public void onButtonStateChanged(int position) {
                Log.d(getClass() + "", "Position: " + position);
            }
        });

    }

}

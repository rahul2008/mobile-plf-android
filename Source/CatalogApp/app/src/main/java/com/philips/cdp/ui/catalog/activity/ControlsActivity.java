package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.MultiStateControls;
import com.philips.cdp.uikit.customviews.ToggleButton;

public class ControlsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        MultiStateControls mstb = (MultiStateControls) this.findViewById(R.id.controls);
        mstb.enableMultipleChoice(true);
        mstb.setOnValueChangedListener(new ToggleButton.OnValueChangedListener() {
            @Override
            public void onValueChanged(int position) {
                Log.d(getClass() + "", "Position: " + position);
            }
        });

    }

}

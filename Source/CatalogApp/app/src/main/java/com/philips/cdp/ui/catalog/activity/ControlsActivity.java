package com.philips.cdp.ui.catalog.activity;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.StateControls;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class ControlsActivity extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);

        StateControls controls = (StateControls) this.findViewById(R.id.controls);
        controls.enableMultipleChoice(false);
        TypedArray controlIcons = getResources()
                .obtainTypedArray(R.array.control_icons);
        Drawable[] drawables = new Drawable[controlIcons.length()];
        for (int i = 0; i < controlIcons.length(); i++) {
            drawables[i] = VectorDrawable.create(this, controlIcons.getResourceId(i, -1));
        }
        controlIcons.recycle();
        controls.drawControlsWithImageBackground(drawables, true);
        controls.setOnButtonStateChangedListener(new StateControls.OnButtonStateChangeListener() {
            @Override
            public void onButtonStateChanged(int position) {
                Log.d(getClass() + "", "Position: " + position);
            }
        });

    }

}

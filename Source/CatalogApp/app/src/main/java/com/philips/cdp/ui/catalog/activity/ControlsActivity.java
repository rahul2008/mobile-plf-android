package com.philips.cdp.ui.catalog.activity;

import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiCheckBox;
import com.philips.cdp.uikit.customviews.StateControls;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class ControlsActivity extends CatalogActivity {
    private RadioButton gradientRadioButton , solidRadioButton;
    private RadioGroup radioGroup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controls);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        radioGroup.clearCheck();

        /* Attach CheckedChangeListener to radio group */
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb && checkedId > -1) {

                   // Toast.makeText(this, rb.getText(), Toast.LENGTH_SHORT).show();
                }

            }
        });


        StateControls controls = (StateControls) this.findViewById(R.id.controls);
        StateControls controls0 = (StateControls) this.findViewById(R.id.controls0);
        controls.enableMultipleChoice(false);
        TypedArray controlIcons = getResources()
                .obtainTypedArray(R.array.control_icons);
        Drawable[] drawables = new Drawable[controlIcons.length()];
        Drawable[] drawables2 = new Drawable[controlIcons.length()];
        for (int i = 0; i < controlIcons.length(); i++) {
            drawables[i] = VectorDrawable.create(this, controlIcons.getResourceId(i, -1));
            drawables2[i] = VectorDrawable.create(this, controlIcons.getResourceId(i, -1));
        }
        controlIcons.recycle();
        controls.drawControls(drawables, true);
        controls0.drawControls(drawables2, true);
        controls.setOnButtonStateChangedListener(new StateControls.OnButtonStateChangeListener() {
            @Override
            public void onButtonStateChanged(int position) {
                Log.d(getClass() + "", "Position: " + position);
            }
        });
        PuiCheckBox puiCheckBox = (PuiCheckBox) findViewById(R.id.checkBox);
        puiCheckBox.setOnCheckedChangeListener(new PuiCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(final View view, final boolean checked) {
                Log.d(getClass() + "", "checked state: " + checked);
            }
        });

    }
    public void onClear(View v) {
        /* Clears all selected radio buttons to default */
        radioGroup.clearCheck();
    }

    public void onSubmit(View v) {
        RadioButton rb = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
       // Toast.makeText(MainActivity.this, rb.getText(), Toast.LENGTH_SHORT).show();
    }
}


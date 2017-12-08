/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
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

/**
 * <b>Find the below steps to use Controls</b><br>
 * <br>
 * <pre> <b>XML Approach</b>
 * &lt;com.philips.cdp.uikit.customviews.StateControls
 * android:id="@+id/controls"
 * android:layout_width="wrap_content"
 * android:layout_height="wrap_content"
 * android:layout_gravity="center_horizontal"
 * android:layout_marginTop="20dp"
 * app:controlButtonHeight="44dp"
 * app:controlButtonWidth="42dp"
 * app:controlEntries="@array/controls_array"
 * app:controlMultiChoice="true"/&gt;
 * <p/>
 * 1. As Shown above use attribute controlButtonHeight and controlButtonWidth for setting button height and width, by default it's wrap_content.
 * 2. Use attribute controlMultiChoice to enable multi choice on buttons
 * 3. Use attribute controlEntries to refer button text through Array of String
 * 4. Use attribute controlCount for setting buttons count
 * </pre>
 * <br>
 * <pre> <b>Java Code Approach</b>
 * StateControls controls = (StateControls) this.findViewById(R.id.controls);
 * controls.drawControls(drawables, false); // To draw controls with Image Icons
 * controls.drawControls(strings[], false); // To draw controls with Strings
 * <p/>
 * </pre>
 *
 * <b> Find steps below to use Radio button </b><br>
 * <pre> <b>XML Approach</b>
 * &lt;com.philips.cdp.uikit.customviews.UIKitRadioButton
 * <com.philips.cdp.uikit.customviews.UIKitRadioButton
 *android:id="@+id/radioButton1"
 *android:layout_width="wrap_content"
 *android:layout_height="wrap_content"
 *android:layout_marginBottom="5dp"
 *android:text="One " /&gt;
 *
 *</p>
 * 1. group multiple radio buttons in a radio group
 * 2. To change width or height provide it in height and width attribute
 * </pre>
 *
 * <b> Find steps below to use Check Box </b><br>
 * <pre> <b>XML Approach</b>
 * &lt;com.philips.cdp.uikit.customviews.PuiCheckBox
 *android:id="@+id/checkBox"
 *android:layout_width="wrap_content"
 *android:layout_height="wrap_content"/&gt;
 *
 *</p>
 * 1. To change width or height provide it in height and width attribute
 * </pre>
 *
 */
public class ControlsActivity extends CatalogActivity {
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
        controls.drawControls(drawables, false);
        controls0.drawControls(drawables2, false);
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


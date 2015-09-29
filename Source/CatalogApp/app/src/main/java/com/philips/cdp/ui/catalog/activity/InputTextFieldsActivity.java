package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.PuiEditText;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InputTextFieldsActivity extends CatalogActivity {

    PuiEditText puiEditText1;
    PuiEditText puiEditText2;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_text_fields);

        puiEditText1 = (PuiEditText) findViewById(R.id.input_field_1);
        puiEditText1.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return ("EMAIL".equals(inputToBeValidated));
            }
        });
    }


}

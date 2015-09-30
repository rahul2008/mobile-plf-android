package com.philips.cdp.ui.catalog.activity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.PuiEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

        RelativeLayout relativeLayout = (RelativeLayout)findViewById(R.id.input_field_container);

        puiEditText1 = (PuiEditText) findViewById(R.id.input_field_1);
        puiEditText1.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return validateEmail(inputToBeValidated);
            }
        });

        puiEditText2 = (PuiEditText) findViewById(R.id.input_field_2);
        puiEditText2.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return validateEmail(inputToBeValidated);
            }
        });

        LayoutTransition transition = relativeLayout.getLayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
    }

    private  static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";

    private boolean validateEmail(final String email) {
        if(email == null) return false;
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }


}

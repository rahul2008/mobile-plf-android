package com.philips.cdp.ui.catalog.activity;

import android.animation.LayoutTransition;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.util.TypedValue;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PuiEditText;
import com.philips.cdp.uikit.customviews.UikitPasswordEditText;
import com.philips.cdp.uikit.drawable.VectorDrawable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class InputTextFieldsActivity extends CatalogActivity {

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";

    PuiEditText puiEditText1;
    PuiEditText puiEditText2;
    PuiEditText edit;
    TextView errorText1;
    TextView errorText2;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_input_text_fields);

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.input_field_container);

        puiEditText1 = (PuiEditText) findViewById(R.id.input_field_1);
        puiEditText1.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return validateEmail(inputToBeValidated);
            }
        });
        edit=(PuiEditText) findViewById(R.id.password);
        errorText1 = puiEditText1.getErrorText();
        errorText1.setPadding(0, 0, 0, 9);

        puiEditText2 = (PuiEditText) findViewById(R.id.input_field_2);
        puiEditText2.setValidator(new PuiEditText.Validator() {
            @Override
            public boolean validate(final String inputToBeValidated) {
                return validateEmail(inputToBeValidated);
            }
        });

        errorText2 = puiEditText2.getErrorText();
        errorText2.setPadding(0, 0, 0, 9);
        edit.setPassword();

        int currentApiVersion = Build.VERSION.SDK_INT;
        if (currentApiVersion >= Build.VERSION_CODES.JELLY_BEAN) {
            LayoutTransition transition = relativeLayout.getLayoutTransition();
            transition.enableTransitionType(LayoutTransition.CHANGING);
        }
    }

    private boolean validateEmail(final String email) {
        if (email == null) return false;
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private Drawable getSettingsIcon() {
        Resources r = getResources();
        float width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 33,
                r.getDisplayMetrics());
        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 32, r
                .getDisplayMetrics());
        Drawable d = VectorDrawable.create(this, R.drawable.uikit_password_show_icon).mutate();
        d.setBounds(0, 0, 100, 70);
        return d;
    }
}

package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsTextInputLayout;
import com.philips.cdp.uikit.customviews.PuiEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class TextLayoutInputFeildInlineForms extends CatalogActivity{

    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inline_forms);
        disableActionbarShadow(this);
        final PhilipsTextInputLayout layout = (PhilipsTextInputLayout) findViewById(R.id.InlineForms);
        /*final EditText fullnamelastfirstnamevalue = (EditText) layout.findViewById(R.id.fullnamelastfirstnamevalue);

        fullnamelastfirstnamevalue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                boolean result = validateEmail(fullnamelastfirstnamevalue,fullnamelastfirstnamevalue.hasFocus());
                if(result){
                    layout.removeError(fullnamelastfirstnamevalue);
                }
            }
        });
*/
        /*final EditText firstName = (EditText) layout.findViewById(R.id.firstnamevalue);

        firstName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                boolean result = validateEmail(firstName,firstName.hasFocus());
                if(result){
                    layout.removeError(firstName);
                }
            }
        });*/


        final EditText email = (EditText) layout.findViewById(R.id.lastnamevalue);
       // email.setFocusable(true);

        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                boolean result = validateEmail(email,email.hasFocus());
                if(result){
                        layout.removeError(email);
                }
            }
        });

        layout.setValidator(new PhilipsTextInputLayout.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.lastnamevalue && hasFocus == false) {
                    boolean result = validateEmail(editText, hasFocus);
                    if (!result) {
                        layout.showError((EditText) editText);
                    }
                }

                /*if (editText.getId() == R.id.firstnamevalue && hasFocus == false) {
                    boolean result = validateEmail(editText, hasFocus);
                    if (!result) {
                        layout.showError((EditText) editText);
                    }
                }*/

                /*if (editText.getId() == R.id.fullnamelastfirstnamevalue && hasFocus == false) {
                    boolean result = validateEmail(editText, hasFocus);
                    if (!result) {
                        layout.showError((EditText) editText);
                    }
                }*/
            }
        });

    }

    public void disableActionbarShadow(Activity activity) {
        if (activity == null) return;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (activity instanceof AppCompatActivity) {
                if (((AppCompatActivity) activity).getSupportActionBar() != null)
                    ((AppCompatActivity) activity).getSupportActionBar().setElevation(0);
            } else {
                if (activity.getActionBar() != null)
                    activity.getActionBar().setElevation(0);
            }
        } else {
            View content = activity.findViewById(android.R.id.content);
            if (content != null && content.getParent() instanceof ActionBarOverlayLayout) {
                ((ViewGroup) content.getParent()).setWillNotDraw(true);

                if (content instanceof FrameLayout) {
                    ((FrameLayout) content).setForeground(null);
                }
            }
        }
    }

    private boolean validateEmail(View editText,boolean hasFocus) {
        String stringToBeValidated = ((EditText)editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();

        return matches;
    }
}

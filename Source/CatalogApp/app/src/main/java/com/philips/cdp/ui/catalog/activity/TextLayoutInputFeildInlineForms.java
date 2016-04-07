/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */

package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ActionBarOverlayLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.InlineForms;
import com.philips.cdp.uikit.customviews.UikitPasswordEditText;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UIKit uses design library InlineForms.
 * Please refer {@link InlineForms} for managing Inline Forms.
 */
public class TextLayoutInputFeildInlineForms extends CatalogActivity {

    /**
     * For checking the Email Address validity
     */
    private static final String EMAIL_PATTERN =
            "^[A-Za-z0-9._%+\\-]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]{2,30}+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,5})$";

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.inline_forms);
        disableActionbarShadow(this);

        /**
         * The Below Layout acts as one item in the inline form
         */
        final InlineForms layout = (InlineForms) findViewById(R.id.InlineForms);
        final EditText email = (EditText) layout.findViewById(R.id.lastnamevalue);
        final UikitPasswordEditText passwordEditText = (UikitPasswordEditText) layout.findViewById(R.id.passwordValue);

        passwordEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (validatePassword(passwordEditText)) {
                    layout.removeError(passwordEditText);
                }
            }
        });


        email.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(final CharSequence s, final int start, final int count, final int after) {

            }

            @Override
            public void onTextChanged(final CharSequence s, final int start, final int before, final int count) {

            }

            @Override
            public void afterTextChanged(final Editable s) {
                boolean result = validateEmail(email, email.hasFocus());
                if (result) {
                    /**
                     * Error Layout should be removed after the entered text is verified as the right Email Address
                     */
                    layout.removeError(email);
                }
            }
        });

        layout.setValidator(new InlineForms.Validator() {
            @Override
            public void validate(View editText, boolean hasFocus) {
                if (editText.getId() == R.id.lastnamevalue && hasFocus == false) {
                    boolean result = validateEmail(editText, hasFocus);
                    if (!result) {
                        layout.setErrorMessage("invalid_email_format");
                        layout.showError((EditText) editText);
                    }
                }
                else if(editText.getId() == R.id.passwordValue && hasFocus == false){
                    if(!validatePassword(passwordEditText)){
                        layout.setErrorMessage("invalid_password");
                        layout.showError(passwordEditText);
                    }
                }
            }
        });
    }

    /**
     * Method to check for password validity
     * @param view the UikitPasswordEditText to validate
     * @return true if password matches, false if it doesn't
     */
    private boolean validatePassword(View view) {
        String passwordToCheck = "Philips123@";
        String passwordCheck = ((UikitPasswordEditText) view).getText().toString();
        return passwordCheck.equals(passwordToCheck);
    }

    /**
     * This removes the Shaw present on the Top Layout
     * @param activity - takes context as the parameter
     */
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
                    ((FrameLayout)content).setForeground(null);
                }
            }
        }
    }

    /**
     * Match the Email Pattern and return the result accordingly
     * @param editText - The Edit text to be validated
     * @param hasFocus - weather the Edit Text has Focus
     * @return
     */
    private boolean validateEmail(View editText, boolean hasFocus) {
        String stringToBeValidated = ((EditText) editText).getText().toString();
        if (stringToBeValidated == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(stringToBeValidated);
        boolean matches = matcher.matches();

        return matches;
    }
}

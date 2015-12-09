package com.philips.cdp.ui.catalog.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.internal.widget.ActionBarOverlayLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.customviews.PhilipsTextInputLayout;

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
        final PhilipsTextInputLayout layout = (PhilipsTextInputLayout) findViewById(R.id.lastnamelayout);
        final EditText email = (EditText) layout.findViewById(R.id.lastnamevalue);
        email.setFocusable(true);



        email.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {

                if (hasFocus) {
                    // Toast.makeText(getApplicationContext(), "on focus", Toast.LENGTH_LONG).show();
                } else {
                    //   Toast.makeText(getApplicationContext(), "lost focus", Toast.LENGTH_LONG).show();
                    boolean validate = validateEmail(email.getText().toString());
                    if (!validate)
                        layout.showError(email);
                }
            }
        });

        /*final PhilipsTextInputLayout layout1 = (PhilipsTextInputLayout) findViewById(R.id.firstnamelayout);
        final EditText email1 = (EditText) layout1.findViewById(R.id.firstnamevalue);
        email1.setFocusable(true);



        email1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(final View v, final boolean hasFocus) {

                if(hasFocus){
                    // Toast.makeText(getApplicationContext(), "on focus", Toast.LENGTH_LONG).show();
                }else {
                    //   Toast.makeText(getApplicationContext(), "lost focus", Toast.LENGTH_LONG).show();
                    boolean validate = validateEmail(email1.getText().toString());
                    if(!validate)
                        layout.showError(email1);
                }
            }
        });*/

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

    private boolean validateEmail(final String email) {
        if (email == null) {
            return false;
        }
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        boolean matches = matcher.matches();

        return matches;
    }
}

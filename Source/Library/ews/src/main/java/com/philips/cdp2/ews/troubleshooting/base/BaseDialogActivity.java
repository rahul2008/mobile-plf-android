/**
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */
package com.philips.cdp2.ews.troubleshooting.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.philips.cdp2.ews.R;
import com.philips.cdp2.ews.view.DynamicThemeApplyingActivity;

public class BaseDialogActivity extends DynamicThemeApplyingActivity {

    private final static String FRAGMENT_NAME = "fragment_name";

    public static void startActivity(Context context, String fragmentName) {
        Intent intent = new Intent(context, BaseDialogActivity.class);
        intent.putExtra(FRAGMENT_NAME, fragmentName);
        context.startActivity(intent);
    }

    public static void startActivityForResult(Fragment fragment, String fragmentName, int requestCode) {
        Intent intent = new Intent(fragment.getContext(), BaseDialogActivity.class);
        intent.putExtra(FRAGMENT_NAME, fragmentName);
        fragment.startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_dialog);
        if (getIntent().hasExtra(FRAGMENT_NAME)) {
            attachFragment();
        }
    }

    private void attachFragment() {
        String fragmentName = getIntent().getStringExtra(FRAGMENT_NAME);
        Fragment fragment = Fragment.instantiate(this, fragmentName);
        getSupportFragmentManager().beginTransaction().replace(R.id.dialog_content, fragment).commit();
    }
}

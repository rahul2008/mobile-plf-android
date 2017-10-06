package com.philips.cdp2.ews.troubleshooting.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp2.ews.R;

public class BaseDialogActivity extends AppCompatActivity {
    private final static String FRAGMENT_NAME = "framgnet_name";

    public static void startActivity(Context context, String fragmentName) {
        Intent intent = new Intent(context, BaseDialogActivity.class);
        intent.putExtra(FRAGMENT_NAME, fragmentName);
        context.startActivity(intent);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_dialog);
        if (getIntent().hasExtra(FRAGMENT_NAME)) {
            String fragmentName = getIntent().getStringExtra(FRAGMENT_NAME);
            Fragment fragment = Fragment.instantiate(this, fragmentName);
            getSupportFragmentManager().beginTransaction().replace(R.id.dialog_content, fragment).commit();
        }
    }
}

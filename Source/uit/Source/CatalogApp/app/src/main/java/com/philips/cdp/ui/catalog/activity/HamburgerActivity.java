package com.philips.cdp.ui.catalog.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.philips.cdp.ui.catalog.R;

public class HamburgerActivity extends CatalogActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hamburger);
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.hamburger_menu:
                intent = new Intent(this, HamburgerMenuDemo.class);
                intent.putExtra("feature", 1);
                break;
            case R.id.hamburger_menu_icon:
                intent = new Intent(this, HamburgerMenuDemo.class);
                intent.putExtra("feature", 2);
                break;
            default:
                break;
        }
        startActivity(intent);
    }
}

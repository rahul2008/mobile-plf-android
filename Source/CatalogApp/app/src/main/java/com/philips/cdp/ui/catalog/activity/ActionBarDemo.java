package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ActionBarDemo extends CatalogActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.uikit_actionbar_menu, menu);

        final MenuItem refreshwithtext = menu.findItem(R.id.action_settings);
        refreshwithtext.setActionView(R.layout.uikit_actionbar_layout);
        TextView textontop = (TextView) refreshwithtext.getActionView().findViewById(R.id.actionbar_overlay_title);

        return super.onCreateOptionsMenu(menu);
    }
}

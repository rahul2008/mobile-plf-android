package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

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

        final MenuItem refreshWithText = menu.findItem(R.id.action_settings);
        refreshWithText.setActionView(R.layout.uikit_actionbar_layout);
        ImageView imageView = (ImageView) refreshWithText.getActionView().findViewById(R.id.actionbar_up_right_arrow);
        imageView.setImageDrawable(VectorDrawable.create(this, R.drawable.uikit_right_arrow));

        return super.onCreateOptionsMenu(menu);
    }
}

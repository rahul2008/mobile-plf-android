package com.philips.cdp.ui.catalog;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.activity.CatalogActivity;

public class RatingBarLauncher extends CatalogActivity {
    //LinearLayout ratingBarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
    }
}

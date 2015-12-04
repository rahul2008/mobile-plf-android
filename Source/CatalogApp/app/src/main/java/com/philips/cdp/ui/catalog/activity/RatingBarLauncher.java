package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;

public class RatingBarLauncher extends CatalogActivity {
    //LinearLayout ratingBarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
    }
}

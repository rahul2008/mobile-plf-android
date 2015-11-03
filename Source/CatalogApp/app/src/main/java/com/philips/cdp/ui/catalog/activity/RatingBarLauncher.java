package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;

public class RatingBarLauncher extends CatalogActivity {
    //LinearLayout ratingBarLayout;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
       /* ratingBarLayout = (LinearLayout) findViewById(R.id.ratingtheme);
        PhilipsRatingBar ratingBarCustom = new PhilipsRatingBar(this);
        ratingBarLayout.addView(ratingBarCustom);*/
    }
}

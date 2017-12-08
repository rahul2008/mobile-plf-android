
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 * UIKit uses design library UIKitRatingBar.
 * Please refer {@link com.philips.cdp.uikit.customviews.UIKitRatingBar} for managing RatingBar.
 */
public class RatingBarLauncher extends CatalogActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
    }
}


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 * Activity For Demonstrating Customized Rating Bar
 */
public class RatingBarLauncher extends CatalogActivity {

    /**
     * @param savedInstanceState
     * <li>Define a RatingBar in the layout file and assign it a unique ID.
     * <pre>
     * &lt;com.philips.cdp.uikit.customviews.UIKitRatingBar
     *      android:id="@+id/ratingbig"
     *      android:layout_width="wrap_content"
     *      android:layout_height="wrap_content"
     *      android:layout_centerInParent="true"
     *      app:isratingbarbig="true"/&gt;
     * </pre></li>
     *  <li>For Big Star give  <pre>app:isratingbarbig="true"</pe></li>
     *  <li>For Small Star give  <pre>app:isratingbarbig="false"</pe></li>
     */

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
    }
}

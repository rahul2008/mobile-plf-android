
/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;

/**
 * <b></b> RatingBarLauncher is class to demonstrate the use of custom RatingBar (Big or Small) two types of RatingBar</b>
 * <p/>
 * <p/>
 * <b></b>Inorder to use Custom RatingBar include the Following lines in the layout File</b><br>
 * <pre>&lt;com.philips.cdp.uikit.customviews.UIKitRatingBar
 *      android:id="@+id/ratingbig"
 *      android:layout_width="wrap_content"
 *      android:layout_height="wrap_content"
 *      android:layout_centerInParent="true"
 *      app:isratingbarbig="true"/&gt;
 *
 * </pre>
 * <b></b>For Big Star give  <pre>app:isratingbarbig="true"</b><br>
 * <b></b>For Small Star give  <pre>app:isratingbarbig="false"</b><br>
 */
public class RatingBarLauncher extends CatalogActivity {

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rating_star);
    }
}

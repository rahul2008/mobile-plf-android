/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.philips.platform.uid.activity.BaseTestActivity;

import static com.philips.platform.uid.activity.BaseTestActivity.CONTENT_COLOR_KEY;

public class BaseTest {

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor, final int contentColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @NonNull
    protected Intent getIntentWithContentRange(final int contentColorIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColorIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @NonNull
    protected Intent getIntentWithColorRange(final int colorRangeIndex) {
        final Bundle bundleExtra = new Bundle();
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        bundleExtra.putInt(BaseTestActivity.COLOR_RANGE_KEY, colorRangeIndex);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor, final int contentColor, final int colorRange) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        bundleExtra.putInt(BaseTestActivity.CONTENT_COLOR_KEY, contentColor);
        bundleExtra.putInt(BaseTestActivity.COLOR_RANGE_KEY, colorRange);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }
}

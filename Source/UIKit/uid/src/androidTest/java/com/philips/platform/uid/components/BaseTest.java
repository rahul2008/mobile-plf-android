package com.philips.platform.uid.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.philips.platform.uid.activity.BaseTestActivity;

public class BaseTest {

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }
}

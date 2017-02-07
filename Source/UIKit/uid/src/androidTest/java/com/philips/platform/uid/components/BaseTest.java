package com.philips.platform.uid.components;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;

import com.philips.platform.uid.activity.BaseTestActivity;

import org.junit.After;

public class BaseTest {

    private IdlingResource mIdlingResource;

    public void registerIdlingResources(final BaseTestActivity baseTestActivity) {
        mIdlingResource = baseTestActivity.getIdlingResource();
        // To prove that the test fails, omit this call:
        Espresso.registerIdlingResources(mIdlingResource);
    }

    @NonNull
    protected Intent getLaunchIntent(final int navigationColor) {
        final Bundle bundleExtra = new Bundle();
        bundleExtra.putInt(BaseTestActivity.NAVIGATION_COLOR_KEY, navigationColor);
        final Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.putExtras(bundleExtra);
        return intent;
    }

    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            Espresso.unregisterIdlingResources(mIdlingResource);
        }
    }
}

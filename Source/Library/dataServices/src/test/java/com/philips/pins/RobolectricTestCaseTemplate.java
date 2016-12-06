package com.philips.pins;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.support.annotation.NonNull;
import com.philips.platform.dataservices.BuildConfig;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
@Ignore("This is base class for all Robolectric tests")
@RunWith(UGROWRobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "com.philips.pins.ugrow")
public abstract class RobolectricTestCaseTemplate {

    @SuppressLint("ValidFragment")
    public class TestFragment extends Fragment{

    }
}

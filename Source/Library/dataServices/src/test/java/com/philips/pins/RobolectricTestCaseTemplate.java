package com.philips.pins;

import android.annotation.SuppressLint;
import android.app.Fragment;

import com.philips.platform.dataservices.BuildConfig;

import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;

@Ignore("This is base class for all Robolectric tests")
@RunWith(DSRoboelectricTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 21, packageName = "com.philips.platform.dataservices")
public abstract class RobolectricTestCaseTemplate {

    @SuppressLint("ValidFragment")
    public class TestFragment extends Fragment {

    }
}

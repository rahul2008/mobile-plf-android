package com.philips.cdp.di.iap.Fragments;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.philips.cdp.di.iap.R;

import org.junit.runners.model.InitializationError;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

/**
 * Created by Apple on 05/07/16.
 */
public class IAPBaseFragmentTestRunner extends RobolectricTestRunner {
    /**
     * Creates a runner to run {@code testClass}. Looks in your working directory for your AndroidManifest.xml file
     * and res directory by default. Use the {@link Config} annotation to configure.
     *
     * @param testClass the test class to be run
     * @throws InitializationError if junit says so
     */
    public IAPBaseFragmentTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    public static void addFragment(BaseAnimationSupportFragment newFragment,
                                   String newFragmentTag) {

        FragmentManager fragmentManager = new AppCompatActivity().getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fl_mainFragmentContainer, newFragment, newFragmentTag);
        transaction.addToBackStack(newFragmentTag);
        transaction.commitAllowingStateLoss();

    }
}


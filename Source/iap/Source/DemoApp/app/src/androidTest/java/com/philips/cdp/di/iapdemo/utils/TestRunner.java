package com.philips.cdp.di.iapdemo.utils;

import android.app.Application;
import android.content.Context;
import androidx.test.runner.AndroidJUnitRunner;

import com.philips.cdp.di.iapdemo.DemoApplication;

/**
 * Created by F1sherKK on 14/04/16.
 */
public class TestRunner extends AndroidJUnitRunner {

    @Override
    public Application newApplication(ClassLoader cl, String className, Context context)
            throws InstantiationException, IllegalAccessException, ClassNotFoundException {
        return super.newApplication(cl, DemoApplication.class.getName(), context);
    }
}

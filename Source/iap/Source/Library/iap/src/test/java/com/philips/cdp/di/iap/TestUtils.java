/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.cdp.di.iap;

import android.content.Context;

import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.integration.MockIAPSetting;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;
import com.philips.cdp.di.iap.store.StoreListener;
import com.philips.cdp.di.iap.utils.IAPLog;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.pif.DataInterface.USR.UserDataInterface;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.mockito.Mockito.mock;

public class TestUtils {
    private static HybrisDelegate delegate;

    public static HybrisDelegate getStubbedHybrisDelegate() {
        if (delegate != null) {
            return delegate;
        }
        delegate = HybrisDelegate.getInstance();
        Context context = getInstrumentation().getContext();

        NetworkController mockController = new MockNetworkController(context, new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        try {
            //Set the controller
            Class<?> cls = delegate.getClass();
            Field controller = cls.getDeclaredField("controller");
            controller.setAccessible(true);
            controller.set(delegate, mockController);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());
        }

        return delegate;
    }

    public static StoreListener getStubbedStore() {
        Context context = getInstrumentation().getContext();
        StoreListener mockStore = new MockStore(context, mock(IAPUser.class)).getStore(new MockIAPSetting(context),new MockIAPDependencies(mock(AppInfra.class),mock(UserDataInterface.class)));
        mockStore.initStoreConfig(/*"en", "US",*/ null);

        return mockStore;
    }

    public static String readFile(Class<?> cls, String fileName) {
        BufferedReader br = null;
        String path = prepareCurrentPath(cls, fileName);
        try {
            br = new BufferedReader(new FileReader(path));
            StringBuilder sb = new StringBuilder();
            String line = br.readLine();

            while (line != null) {
                sb.append(line);
                sb.append("\n");
                line = br.readLine();
            }

            return sb.toString();
        } catch (Exception e) {
            IAPLog.e(IAPLog.LOG, e.getMessage());

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                IAPLog.e(IAPLog.LOG, e.getMessage());
            }
        }
        return null;
    }

    private static String prepareCurrentPath(Class<?> cls, String fileName) {
        String pckg = cls.getName();
        String[] paths = pckg.split("\\.");
        StringBuilder sb = new StringBuilder();
        sb.append("src").append(File.separator);
        sb.append("test").append(File.separator);
        sb.append("java").append(File.separator);
        for (int index = 0; index < paths.length - 1; index++) {

            sb.append(paths[index]).append(File.separator);
        }
        sb.append(fileName);
        return sb.toString();
    }
}

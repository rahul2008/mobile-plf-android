/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.di.iap;

import android.content.Context;

import com.philips.cdp.di.iap.core.StoreSpec;
import com.philips.cdp.di.iap.integration.MockIAPDependencies;
import com.philips.cdp.di.iap.session.HybrisDelegate;
import com.philips.cdp.di.iap.session.MockNetworkController;
import com.philips.cdp.di.iap.session.NetworkController;
import com.philips.cdp.di.iap.store.HybrisStore;
import com.philips.cdp.di.iap.store.IAPUser;
import com.philips.cdp.di.iap.store.MockStore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;

import static org.mockito.Mockito.mock;

public class TestUtils {
    private static HybrisDelegate delegate;
    private static HybrisStore mockHybrisStore;

    public static HybrisDelegate getStubbedHybrisDelegate() {
        if (delegate != null) {
            return delegate;
        }
        delegate = HybrisDelegate.getInstance();
        NetworkController mockController = new MockNetworkController(mock(Context.class), new MockIAPDependencies());
        try {
            //Set the controller
            Class<?> cls = delegate.getClass();
            Field controller = cls.getDeclaredField("controller");
            controller.setAccessible(true);
            controller.set(delegate, mockController);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return delegate;
    }

    public static StoreSpec getStubbedStore() {
        if (mockHybrisStore != null) {
            return mockHybrisStore;
        }
        StoreSpec mockStore = new MockStore(mock(Context.class), mock(IAPUser.class)).getStore(new MockIAPDependencies());
        mockStore.initStoreConfig("en", "US", null);
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
            e.printStackTrace();

        } finally {
            try {
                if (br != null) {
                    br.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
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

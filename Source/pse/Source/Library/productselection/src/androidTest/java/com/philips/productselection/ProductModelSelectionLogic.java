/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.productselection;

import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.annotation.IdRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.philips.cdp.productselection.fragments.detailedscreen.NavigationFragment;
import com.philips.cdp.productselection.fragments.detailedscreen.adapter.ProductAdapter;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Created by 310190678 on 04-Mar-16.
 */
public class ProductModelSelectionLogic {

    private static final String TAG = ProductModelSelectionLogic.class.getSimpleName();
    private PrxRequest mProductAssetBuilder = null;

    @Test
    public void testProductimagesLoadingAsset() {
        JSONObject mJsonObject = null;
        try {
            StringBuilder sb = new StringBuilder();
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(getInstrumentation().getContext().getResources().getAssets().open("asset_template_one.txt")));

                // do reading, usually loop until end of file reading
                String mLine = reader.readLine();
                while (mLine != null) {
                    // process line
                    sb.append(mLine);
                    mLine = reader.readLine();
                }

                reader.close();
            } catch (IOException e) {
                ProductSelectionLogger.i(TAG, "IOException - " + e.getMessage());
            }
            ProductSelectionLogger.d(TAG, "Parsed Data : " + sb.toString());
            mJsonObject = new JSONObject(sb.toString());
            ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            AssetModel mAssetModel = new AssetModel();
            ResponseData responseData = mAssetModel.parseJsonResponseData(mJsonObject);
            assertNotNull(responseData);
        } catch (JSONException e) {
            ProductSelectionLogger.d(TAG, "JSON : " + e);
        } catch (Exception e) {
            ProductSelectionLogger.d(TAG, "IO " + e);
        }
    }

    @Test
    public void testNavigatioImageonLoadCase() {
        boolean expected = false;
        NavigationFragment mNavigationFragment = new NavigationFragment();
        Bitmap mBitmap = mNavigationFragment.getBlankThumbnail(12);
        if (mBitmap != null)
            expected = true;
        assertTrue(expected);
    }

    @Test
    public void testDetailedScreenImageAdapter() {
        String[] mImagesLengthToLoad = new String[5];
        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public Fragment findFragmentByTag(String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(Bundle bundle, String key, Fragment fragment) {

            }

            @Override
            public Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Override
            public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

            }

            @Override
            public Fragment getPrimaryNavigationFragment() {
                return null;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }

            @Override
            public boolean isStateSaved() {
                return false;
            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertEquals(5, productAdapter.getCount());
    }

    @Test
    public void testDetailedScreenImageAdapterTitle() {
        String[] mImagesLengthToLoad = new String[5];
        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public Fragment findFragmentByTag(String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(Bundle bundle, String key, Fragment fragment) {

            }

            @Override
            public Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Override
            public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

            }

            @Override
            public Fragment getPrimaryNavigationFragment() {
                return null;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }

            @Override
            public boolean isStateSaved() {
                return false;
            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertNull(productAdapter.getPageTitle(2));
    }

    @Test
    public void testDetailedScreenImagesCount() {
        String[] mImagesLengthToLoad = new String[5];
        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public Fragment findFragmentByTag(String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(Bundle bundle, String key, Fragment fragment) {

            }

            @Override
            public Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Override
            public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

            }

            @Override
            public Fragment getPrimaryNavigationFragment() {
                return null;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }

            @Override
            public boolean isStateSaved() {
                return false;
            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertEquals(5, productAdapter.getCount());
    }

    @Test
    public void testDetailedScreenPageTitle() {
        String[] mImagesLengthToLoad = new String[5];
        FragmentManager fragmentManager = new FragmentManager() {
            @Override
            public FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public Fragment findFragmentByTag(String tag) {
                return null;
            }

            @Override
            public void popBackStack() {

            }

            @Override
            public boolean popBackStackImmediate() {
                return false;
            }

            @Override
            public void popBackStack(String name, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(String name, int flags) {
                return false;
            }

            @Override
            public void popBackStack(int id, int flags) {

            }

            @Override
            public boolean popBackStackImmediate(int id, int flags) {
                return false;
            }

            @Override
            public int getBackStackEntryCount() {
                return 0;
            }

            @Override
            public BackStackEntry getBackStackEntryAt(int index) {
                return null;
            }

            @Override
            public void addOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void removeOnBackStackChangedListener(OnBackStackChangedListener listener) {

            }

            @Override
            public void putFragment(Bundle bundle, String key, Fragment fragment) {

            }

            @Override
            public Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<Fragment> getFragments() {
                return null;
            }

            @Override
            public Fragment.SavedState saveFragmentInstanceState(Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void registerFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb, boolean recursive) {

            }

            @Override
            public void unregisterFragmentLifecycleCallbacks(FragmentLifecycleCallbacks cb) {

            }

            @Override
            public Fragment getPrimaryNavigationFragment() {
                return null;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }

            @Override
            public boolean isStateSaved() {
                return false;
            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertNull(productAdapter.getPageTitle(2));
    }
}

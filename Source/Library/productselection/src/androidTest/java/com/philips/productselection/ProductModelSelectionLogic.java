package com.philips.productselection;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.widget.Toast;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.detailedscreen.NavigationFragment;
import com.philips.cdp.productselection.fragments.detailedscreen.adapter.ProductAdapter;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.prxclient.datamodels.assets.AssetModel;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.cdp.prxclient.response.ResponseData;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileDescriptor;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.List;

/**
 * Created by 310190678 on 04-Mar-16.
 */
public class ProductModelSelectionLogic extends InstrumentationTestCase {


    private static final String TAG = ProductModelSelectionLogic.class.getSimpleName();
    private PrxRequest mProductAssetBuilder = null;

    @Override
    public void setUp() throws Exception {
        super.setUp();

    }

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
                // log the exception
                e.printStackTrace();
            }
            Log.d(TAG, "Parsed Data : " + sb.toString());
            mJsonObject = new JSONObject(sb.toString());
            ResponseData mResponseData = mProductAssetBuilder.getResponseData(mJsonObject);
            AssetModel mAssetModel = new AssetModel();
            ResponseData responseData = mAssetModel.parseJsonResponseData(mJsonObject);
            assertNotNull(responseData);
        } catch (JSONException e) {
            Log.d(TAG, "JSON : " + e);

        } catch (Exception e) {
            Log.d(TAG, "IO " + e);
        }
    }

    public void testNavigatioImageonLoadCase() {
        boolean expected = false;
        NavigationFragment mNavigationFragment = new NavigationFragment();
        Bitmap mBitmap = mNavigationFragment.getBlankThumbnail(12);
        if (mBitmap != null)
            expected = true;
        assertEquals(true, expected);

    }

    public void testDetailedScreenImageAdapter() {
        String[] mImagesLengthToLoad = new String[5];
        android.support.v4.app.FragmentManager fragmentManager = new android.support.v4.app.FragmentManager() {
            @Override
            public android.support.v4.app.FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentByTag(String tag) {
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
            public void putFragment(Bundle bundle, String key, android.support.v4.app.Fragment fragment) {

            }

            @Override
            public android.support.v4.app.Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<android.support.v4.app.Fragment> getFragments() {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertEquals(5, productAdapter.getCount());

    }


    public void testDetailedScreenImageAdapterTitle() {
        String[] mImagesLengthToLoad = new String[5];
        android.support.v4.app.FragmentManager fragmentManager = new android.support.v4.app.FragmentManager() {
            @Override
            public android.support.v4.app.FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentByTag(String tag) {
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
            public void putFragment(Bundle bundle, String key, android.support.v4.app.Fragment fragment) {

            }

            @Override
            public android.support.v4.app.Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<android.support.v4.app.Fragment> getFragments() {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertNull(productAdapter.getPageTitle(2));

    }


    public void testDetailedScreenImagesCount() {
        String[] mImagesLengthToLoad = new String[5];
        android.support.v4.app.FragmentManager fragmentManager = new android.support.v4.app.FragmentManager() {
            @Override
            public android.support.v4.app.FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentByTag(String tag) {
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
            public void putFragment(Bundle bundle, String key, android.support.v4.app.Fragment fragment) {

            }

            @Override
            public android.support.v4.app.Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<android.support.v4.app.Fragment> getFragments() {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertEquals(5, productAdapter.getCount());

    }

    public void testDetailedScreenPageTitle() {
        String[] mImagesLengthToLoad = new String[5];
        android.support.v4.app.FragmentManager fragmentManager = new android.support.v4.app.FragmentManager() {
            @Override
            public android.support.v4.app.FragmentTransaction beginTransaction() {
                return null;
            }

            @Override
            public boolean executePendingTransactions() {
                return false;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentById(@IdRes int id) {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment findFragmentByTag(String tag) {
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
            public void putFragment(Bundle bundle, String key, android.support.v4.app.Fragment fragment) {

            }

            @Override
            public android.support.v4.app.Fragment getFragment(Bundle bundle, String key) {
                return null;
            }

            @Override
            public List<android.support.v4.app.Fragment> getFragments() {
                return null;
            }

            @Override
            public android.support.v4.app.Fragment.SavedState saveFragmentInstanceState(android.support.v4.app.Fragment f) {
                return null;
            }

            @Override
            public boolean isDestroyed() {
                return false;
            }

            @Override
            public void dump(String prefix, FileDescriptor fd, PrintWriter writer, String[] args) {

            }
        };

        ProductAdapter productAdapter = new ProductAdapter(fragmentManager, mImagesLengthToLoad);
        assertNull(productAdapter.getPageTitle(2));

    }

/*

    public void testProductSelectionComponentConfigManager() {
        ProductModelSelectionHelper mProductSelectionHelper = ProductModelSelectionHelper.getInstance();

        String[] ctnList = new String[10];
        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);

        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(getInstrumentation().getTargetContext().getApplicationContext());
        mProductSelectionHelper.setLocale("en", "GB");

        ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground);
        uiLauncher.setAnimation(R.anim.abc_fade_in, R.anim.abc_fade_out);
       */
/* ProductModelSelectionHelper.getInstance().setSummaryDataListener(new SummaryDataListener() {
            @Override
            public void onSuccess(List<SummaryModel> summaryModels) {

                if (summaryModels != null)
                    Toast.makeText(Launcher.this, "Summary Size : " + summaryModels.size(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Launcher.this, "Summary returned null", Toast.LENGTH_SHORT).show();

            }
        });*//*

        ProductModelSelectionHelper.getInstance().setProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel productSummaryModel) {

            }
        });
        //ProductModelSelectionHelper.getInstance().invokeProductSelection(uiLauncher, productsSelection);
       // ProductSelectionLogger.enableLogging();
       assertNotNull(ProductModelSelectionHelper.getInstance().getContext());
      */
/*  assertNotNull(ProductModelSelectionHelper.getInstance().getLauncherType());
        assertNotNull(ProductModelSelectionHelper.getInstance().getLocale());
        assertNotNull(ProductModelSelectionHelper.getInstance().getProductModelSelectionType());
        assertNotNull(ProductModelSelectionHelper.getInstance().getProductSelectionListener());
        assertNotNull(ProductModelSelectionHelper.getInstance().getProductModelSelectionType());*//*



    }
*/

}

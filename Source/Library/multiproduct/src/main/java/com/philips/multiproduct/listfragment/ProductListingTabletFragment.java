package com.philips.multiproduct.listfragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.multiproduct.R;
import com.philips.multiproduct.detailedscreen.DetailedScreenFragment;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.savedscreen.SavedScreenFragment;
import com.philips.multiproduct.utils.ProductSelectionLogger;

/**
 * ProductListingTabletFragment class will be the base class for Product Listing in tablet.
 *
 * @author : ritesh.jha@philips.com
 * @since : 16 Feb 2016
 */
public class ProductListingTabletFragment extends MultiProductBaseFragment {

    private String TAG = ProductListingTabletFragment.class.getSimpleName();
    private static View mRootView = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if (mRootView != null) {
            ViewGroup parent = (ViewGroup) mRootView.getParent();
            if (parent != null)
                parent.removeView(mRootView);
        }
        try {
            mRootView = inflater.inflate(R.layout.fragment_product_list_tablet, container, false);
        } catch (InflateException e) {
        }

        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//        replaceFragmentTablet(new DetailedScreenFragment());

        try {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentTabletProductDetailsParent, new DetailedScreenFragment(), "DetailedScreenFragment");
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product);
    }


    @Override
    public void setViewParams(Configuration config) {
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

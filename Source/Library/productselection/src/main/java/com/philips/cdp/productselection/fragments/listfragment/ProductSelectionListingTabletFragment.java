package com.philips.cdp.productselection.fragments.listfragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.detailedscreen.DetailedScreenFragmentSelection;

/**
 * ProductSelectionListingTabletFragment class will be the base class for Product Listing in tablet.
 *
 * @author : ritesh.jha@philips.com
 * @since : 16 Feb 2016
 */
public class ProductSelectionListingTabletFragment extends ProductSelectionBaseFragment {

    private String TAG = ProductSelectionListingTabletFragment.class.getSimpleName();

    private LinearLayout.LayoutParams mRightPanelLayoutParams = null;
    private RelativeLayout mRightPanelLayout = null;
    private static View mRootView = null;
    private LinearLayout.LayoutParams mLeftPanelLayoutParams = null;

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

        mRightPanelLayout = (RelativeLayout) getActivity().findViewById(R.id.fragmentTabletProductDetailsParent);
        mRightPanelLayoutParams = (LinearLayout.LayoutParams) mRightPanelLayout.getLayoutParams();
        mLeftPanelLayoutParams = (LinearLayout.LayoutParams) getActivity().findViewById(R.id.fragmentTabletProductList).getLayoutParams();

        try {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentTabletProductDetailsParent, new DetailedScreenFragmentSelection(), "DetailedScreenFragmentSelection");
            fragmentTransaction.commit();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.Product_Title);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setViewParams(newConfig);
    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
//            Fragment fragmentTablet = getActivity().getSupportFragmentManager().findFragmentById(R.id.productListContainerTablet);
//            fragmentTablet.
            mLeftPanelLayoutParams.weight = 0.0f;
            getActivity().findViewById(R.id.fragmentTabletProductList).setVisibility(View.GONE);
            mRightPanelLayoutParams.weight = 1.0f;
            mRightPanelLayoutParams.leftMargin = mRightPanelLayoutParams.rightMargin = mLeftRightMarginPort;
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            getActivity().findViewById(R.id.fragmentTabletProductList).setVisibility(View.VISIBLE);
            mRightPanelLayoutParams.weight = 0.55f;
            mLeftPanelLayoutParams.weight = 0.45f;
        }
        mRightPanelLayout.setLayoutParams(mRightPanelLayoutParams);
        getActivity().findViewById(R.id.fragmentTabletProductList).setLayoutParams(mLeftPanelLayoutParams);
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

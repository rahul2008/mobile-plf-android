package com.philips.cdp.productselection.fragments.listfragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
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

import java.util.Observable;
import java.util.Observer;

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
    private RelativeLayout mLeftPanelLayout = null;
    private static View mRootView = null;
    private LinearLayout.LayoutParams mLeftPanelLayoutParams = null;
    private Fragment mFragmentDetailsTablet = null;
    private DetailedScreenFragmentSelection mDetailedScreenFragmentSelection = null;
    private static final int UPDATE_UI = 0;

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
        setListViewRequiredInTablet(true);
        return mRootView;
    }

    private Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch(msg.what){
                case UPDATE_UI:
                    if(mHandler == null){
                        return;
                    }
                    mHandler.removeMessages(UPDATE_UI);
                    alignGui();
                    replaceFragmentForTablet();
                    break;
            }
        }
    };

    private void alignGui() {
        if(getActivity() != null) {
            Configuration configuration = getResources().getConfiguration();
            setViewParams(configuration);
        }
    }

    private void replaceFragmentForTablet() {
        try {
            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
            Fragment fragmentDetailsTablet = getActivity().getSupportFragmentManager().findFragmentByTag("DetailedScreenFragmentSelection");
            fragmentTransaction.remove(fragmentDetailsTablet)/*.commit()*/;

            mDetailedScreenFragmentSelection = new DetailedScreenFragmentSelection();
            fragmentTransaction.add(R.id.fragmentTabletProductDetailsParent, mDetailedScreenFragmentSelection, "DetailedScreenFragmentSelection");
            fragmentTransaction.commitAllowingStateLoss();

//            addDetailedScreenAtRight();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mLeftPanelLayout = (RelativeLayout) getActivity().findViewById(R.id.fragmentTabletProductList);
        mRightPanelLayout = (RelativeLayout) getActivity().findViewById(R.id.fragmentTabletProductDetailsParent);
        mRightPanelLayoutParams = (LinearLayout.LayoutParams) mRightPanelLayout.getLayoutParams();
        mLeftPanelLayoutParams = (LinearLayout.LayoutParams) mLeftPanelLayout.getLayoutParams();

        addListScreenAtLeft();

        addDetailedScreenAtRight();

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
    }

    private void addListScreenAtLeft() {
        try {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.fragmentTabletProductList, new ProductSelectionListingFragment(mHandler), "ProductSelectionListingFragment");
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void addDetailedScreenAtRight() {
        try {
            FragmentTransaction fragmentTransaction = getActivity()
                    .getSupportFragmentManager().beginTransaction();
            mDetailedScreenFragmentSelection = new DetailedScreenFragmentSelection();
            fragmentTransaction.add(R.id.fragmentTabletProductDetailsParent, mDetailedScreenFragmentSelection, "DetailedScreenFragmentSelection");
            fragmentTransaction.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            ProductSelectionLogger.e(TAG, "IllegalStateException" + e.getMessage());
            e.printStackTrace();
        }
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

            if(isListViewRequiredInTablet()) {
                mLeftPanelLayout.setVisibility(View.VISIBLE);
                mRightPanelLayout.setVisibility(View.GONE);
                mLeftPanelLayoutParams.weight = 1.0f;
                mRightPanelLayoutParams.weight = 0.0f;
            }
            else{
                mLeftPanelLayoutParams.weight = 0.0f;
                mRightPanelLayoutParams.weight = 1.0f;
                mRightPanelLayout.setVisibility(View.VISIBLE);
                mLeftPanelLayout.setVisibility(View.GONE);
            }
//            mRightPanelLayoutParams.leftMargin = mRightPanelLayoutParams.rightMargin = mLeftRightMarginPort;
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mLeftPanelLayout.setVisibility(View.VISIBLE);
            mRightPanelLayout.setVisibility(View.VISIBLE);
            mRightPanelLayoutParams.weight = 0.55f;
            mLeftPanelLayoutParams.weight = 0.45f;
//            mRightPanelLayoutParams.leftMargin = mRightPanelLayoutParams.rightMargin = 0;
        }
        mRightPanelLayout.setLayoutParams(mRightPanelLayoutParams);
        mLeftPanelLayout.setLayoutParams(mLeftPanelLayoutParams);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        setListViewRequiredInTablet(true);
        mHandler = null;
    }
}

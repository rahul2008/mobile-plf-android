package com.philips.cdp.productselection.fragments.welcomefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.tagging.Tagging;

/**
 * DirectFragment class is used as a welcome screen when CTN is not been choosen.
 *
 * @author : ritesh.jha@philips.com
 * @since : 20 Jan 2016
 */
public class WelcomeScreenFragmentSelection extends ProductSelectionBaseFragment implements View.OnClickListener {

    private String TAG = WelcomeScreenFragmentSelection.class.getSimpleName();
    private RelativeLayout mSelectProduct = null;
    private LinearLayout mWelcomeScreenParent = null;
    private FrameLayout.LayoutParams mParams = null;
    private static View mRootView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        mSelectProduct = (RelativeLayout) mRootView.findViewById(R.id.welcome_screen_parent_two);
        mWelcomeScreenParent = (LinearLayout) mRootView.findViewById(
                R.id.welcome_screen_parent_one);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mParams = (FrameLayout.LayoutParams) mWelcomeScreenParent.getLayoutParams();
        mSelectProduct.setOnClickListener(this);

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);

        trackFirstPage(Constants.PAGE_WELCOME_SCREEN);
    }

    private boolean isTabletPortrait;

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            isTabletPortrait = true;
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
        } else {
            isTabletPortrait = false;
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
        }
        mWelcomeScreenParent.setLayoutParams(mParams);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.Select_Product_Title);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.welcome_screen_parent_two) {
            if (isConnectionAvailable()) {
                Configuration configuration = getResources().getConfiguration();
                showFragment(new ProductSelectionListingFragment());
            }

            Tagging.trackAction(Constants.ACTION_KEY_SEND_DATA, Constants.ACTION_NAME_SPECIAL_EVENT,
                    Constants.ACTION_VALUE_FIND_PRODUCT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void trackFirstPage(String currPage) {
        if (getPreviousName() != null && !(getPreviousName().equalsIgnoreCase(Constants.PAGE_WELCOME_SCREEN))) {
            Tagging.trackPage(currPage, getPreviousName());
        } else if (null != Tagging.getLaunchingPageName()) {
            Tagging.trackPage(currPage, Tagging.getLaunchingPageName());
        } else {
            Tagging.trackPage(currPage, null);
        }
        setPreviousPageName(Constants.PAGE_WELCOME_SCREEN);
    }
}

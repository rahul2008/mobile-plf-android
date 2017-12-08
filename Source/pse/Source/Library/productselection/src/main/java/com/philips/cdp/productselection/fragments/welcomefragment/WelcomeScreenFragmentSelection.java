package com.philips.cdp.productselection.fragments.welcomefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;

/**
 * DirectFragment class is used as a welcome screen when CTN is not been choosen.
 *
 * @author : ritesh.jha@philips.com
 * @since : 20 Jan 2016
 */
public class WelcomeScreenFragmentSelection extends ProductSelectionBaseFragment implements View.OnClickListener {

    private String TAG = WelcomeScreenFragmentSelection.class.getSimpleName();
    private LinearLayout mWelcomeScreenParent;
    private FrameLayout.LayoutParams mParams;
    private Button mFindProcuctBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mWelcomeScreenParent = (LinearLayout) view.findViewById(
                R.id.welcome_screen_parent_one);
        mFindProcuctBtn = (Button) view.findViewById(R.id.find_product_btn);
        mFindProcuctBtn.setOnClickListener(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ProductSelectionLogger.i(TAG, "Product selection welcome screen shown for user to select products\n");
        mParams = (FrameLayout.LayoutParams) mWelcomeScreenParent.getLayoutParams();
        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
        trackFirstPage(Constants.PAGE_WELCOME_SCREEN);
    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
        } else {
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
        return getResources().getString(R.string.pse_Select_Your_Product_Title);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.find_product_btn) {
            if (isConnectionAvailable()) {
                ProductSelectionLogger.i(TAG, "User clicked on find products");
                showFragment(new ProductSelectionListingFragment());
            }
            ProductModelSelectionHelper.getInstance().getTaggingInterface().trackActionWithInfo
                    (Constants.ACTION_KEY_SEND_DATA, Constants.ACTION_NAME_SPECIAL_EVENT,
                            Constants.ACTION_VALUE_FIND_PRODUCT);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void trackFirstPage(String currPage) {
        if (getPreviousName() != null && !(getPreviousName().equalsIgnoreCase(Constants.
                PAGE_WELCOME_SCREEN))) {
            ProductModelSelectionHelper.getInstance().getTaggingInterface().trackPageWithInfo
                    (currPage, getPreviousName(), getPreviousName());
        }else {
            ProductModelSelectionHelper.getInstance().getTaggingInterface().trackPageWithInfo
                    (currPage, Constants.PAGE_DIGITALCARE_HOME_SCREEN, Constants.PAGE_DIGITALCARE_HOME_SCREEN);
        }
        setPreviousPageName(Constants.PAGE_WELCOME_SCREEN);
    }
}

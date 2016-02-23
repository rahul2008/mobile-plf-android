package com.philips.multiproduct.welcomefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.multiproduct.R;
import com.philips.multiproduct.homefragment.ProductSelectionBaseFragment;
import com.philips.multiproduct.listfragment.ProductSelectionListingFragment;
import com.philips.multiproduct.listfragment.ProductSelectionListingTabletFragment;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_screen, container, false);
        mSelectProduct = (RelativeLayout) view.findViewById(R.id.welcome_screen_parent_two);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mWelcomeScreenParent = (LinearLayout) getActivity().findViewById(
                R.id.welcome_screen_parent_one);
        mParams = (FrameLayout.LayoutParams) mWelcomeScreenParent.getLayoutParams();
        mSelectProduct.setOnClickListener(this);

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
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
        return getResources().getString(R.string.select_product);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.welcome_screen_parent_two) {
            if (isConnectionAvailable()) {
                if (isTablet() && !isTabletPortrait) {
                    Toast.makeText(getActivity(), "Tablet", Toast.LENGTH_SHORT).show();
                    showFragment(new ProductSelectionListingTabletFragment());
                } else {
                    showFragment(new ProductSelectionListingFragment());
                }
            }
        }
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

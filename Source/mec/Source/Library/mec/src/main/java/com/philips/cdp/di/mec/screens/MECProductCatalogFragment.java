package com.philips.cdp.di.mec.screens;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.cdp.di.mec.R;
import com.philips.cdp.di.mec.activity.MECActivity;
import com.philips.cdp.di.mec.session.NetworkConstants;

/**
 * A simple {@link Fragment} subclass.
 */
public class MECProductCatalogFragment extends InAppBaseFragment {

    public static final String TAG = MECProductCatalogFragment.class.getName();

    public static MECProductCatalogFragment createInstance(Bundle args, InAppBaseFragment.AnimationType animType) {
        MECProductCatalogFragment fragment = new MECProductCatalogFragment();
        args.putInt(NetworkConstants.EXTRA_ANIMATIONTYPE, animType.ordinal());
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mecproduct_catalog, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        setTitleAndBackButtonVisibility(R.string.mec_product_catalog, true);
    }

    @Override
    public boolean handleBackEvent() {
        if (getActivity() != null && getActivity() instanceof MECActivity) {
            int count = getFragmentManager().getBackStackEntryCount();
            for (int i = 0; i < count; i++) {
                getFragmentManager().popBackStack();
            }
            finishActivity();
        }
        return super.handleBackEvent();
    }

}

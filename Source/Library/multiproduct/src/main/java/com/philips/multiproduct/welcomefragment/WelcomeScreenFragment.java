package com.philips.multiproduct.welcomefragment;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.philips.multiproduct.R;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.listfragment.ProductListingFragment;
import com.philips.multiproduct.listfragment.ProductListingTabletFragment;

/**
 * DirectFragment class is used as a welcome screen when CTN is not been choosen.
 *
 * @author : ritesh.jha@philips.com
 * @since : 20 Jan 2016
 */
public class WelcomeScreenFragment extends MultiProductBaseFragment implements View.OnClickListener {

    private String TAG = WelcomeScreenFragment.class.getSimpleName();
    private RelativeLayout mSelectProduct = null;

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

        mSelectProduct.setOnClickListener(this);

    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.select_product);
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.welcome_screen_parent_two) {
            if (isConnectionAvailable()) {
                if(isTablet()){
                    Toast.makeText(getActivity(), "Tablet", Toast.LENGTH_SHORT).show();
                    showFragment(new ProductListingTabletFragment());
                }
                else {
                    showFragment(new ProductListingFragment());
                }
            }
        }
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

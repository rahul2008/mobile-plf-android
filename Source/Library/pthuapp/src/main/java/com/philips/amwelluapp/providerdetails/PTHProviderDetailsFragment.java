package com.philips.amwelluapp.providerdetails;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.amwelluapp.R;
import com.philips.amwelluapp.base.PTHBaseFragment;

public class PTHProviderDetailsFragment extends PTHBaseFragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.pth_provider_details_fragment, container, false);
        return view;
    }


    @Override
    public void onResume() {
        super.onResume();
        if(null != getActionBarListener()) {
            getActionBarListener().updateActionBar("Provider details", true);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}

package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;

import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;

public class THSPickTimeFragment extends THSProviderDetailsFragment {
    public static final String TAG = THSPickTimeFragment.class.getSimpleName();

    Date mDate;
    THSProviderEntity thsProviderEntity;
    //AvailableProvider mAvailableProvider;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_pick_time), true);
        }
        Bundle arguments = getArguments();
        thsProviderEntity = arguments.getParcelable(THSConstants.THS_PROVIDER_ENTITY);
        mDate = (Date) arguments.getSerializable(THSConstants.THS_DATE);
       // mAvailableProvider = arguments.getParcelable(THSConstants.THS_AVAILABLE_PROVIDER_LIST);
        onRefresh();
    }

    @Override
    public String getFragmentTag() {
        return THSPickTimeFragment.TAG;
    }

}

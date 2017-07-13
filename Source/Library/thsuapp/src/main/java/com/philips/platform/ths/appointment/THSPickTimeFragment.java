package com.philips.platform.ths.appointment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.americanwell.sdk.entity.provider.AvailableProvider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.philips.platform.ths.R;
import com.philips.platform.ths.providerdetails.THSProviderDetailsFragment;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Date;
import java.util.List;

public class THSPickTimeFragment extends THSProviderDetailsFragment {
    public static final String TAG = THSPickTimeFragment.class.getSimpleName();

    Date mDate;
    ProviderInfo mProviderInfo;
    AvailableProvider mAvailableProvider;


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_pick_time), true);
        }
        Bundle arguments = getArguments();
        mProviderInfo = arguments.getParcelable(THSConstants.THS_PROVIDER_INFO);
        mDate = (Date) arguments.getSerializable(THSConstants.THS_DATE);
        mAvailableProvider = arguments.getParcelable(THSConstants.THS_AVAILABLE_PROVIDER_LIST);
        onRefresh();
    }



    @Override
    public ProviderInfo getProviderInfo() {
        return mProviderInfo;
    }

    @Override
    public List<Date> getAppointmentTimeSlots() {
        return mAvailableProvider.getAvailableAppointmentTimeSlots();
    }

}

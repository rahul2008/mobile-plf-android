package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.provider.ProviderInfo;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.providerdetails.THSProviderDetailsCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class THSProviderNotAvailablePresenter implements THSBasePresenter{
    THSBaseFragment mThsBaseFragment;

    THSProviderNotAvailablePresenter(THSBaseFragment thsBaseFragment){
        mThsBaseFragment = thsBaseFragment;
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.calendar_view) {
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment);

            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year,month,day);
                    final Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    ((THSProviderNotAvailableFragment)mThsBaseFragment).setDate(date);

                   //TODO: Take decision and Launch the Fragment
                    mThsBaseFragment.showToast("Not yet Implemented - Coming Soon!");

                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        }
    }
}

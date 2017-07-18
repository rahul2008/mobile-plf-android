package com.philips.platform.ths.appointment;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.providerdetails.THSProviderEntity;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.utility.THSConstants;

import java.util.Calendar;
import java.util.Date;

public class THSDatePickerFragment extends THSBaseFragment implements DatePickerDialog.OnDateSetListener{
    public static final String TAG = THSDatePickerFragment.class.getSimpleName();

    private Date date;
    private Calendar calendar;
    THSDatePickerPresenter thsDatePickerPresenter;

    private Practice mPractice;
    private boolean mIsLaunchedFromDetails;
    private Provider mProvider;

    private THSProviderEntity mThsProviderEntity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ths_date_picker, container, false);
        initDateTimeFields();
        showDatePicker();
        thsDatePickerPresenter = new THSDatePickerPresenter(this);
        Bundle arguments = getArguments();
        mPractice = arguments.getParcelable(THSConstants.THS_PRACTICE_INFO);
        mIsLaunchedFromDetails = arguments.getBoolean(THSConstants.THS_IS_DETAILS);
        mProvider = arguments.getParcelable(THSConstants.THS_PROVIDER);
        mThsProviderEntity = arguments.getParcelable(THSConstants.THS_PROVIDER_ENTITY);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (null != getActionBarListener()) {
            getActionBarListener().updateActionBar(getString(R.string.ths_select_a_date_and_time), true);
        }
    }

    private void initDateTimeFields() {
        date = new Date(System.currentTimeMillis());
        calendar = Calendar.getInstance();
        calendar.setTime(date);
    }

    public void showDatePicker() {
        calendar.setTime(date);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this.getActivity(),
                this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setButton(DialogInterface.BUTTON_NEGATIVE, getString(R.string.cancel), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_NEGATIVE) {
                    getFragmentManager().popBackStackImmediate();
                }
            }
        });
        datePickerDialog.show();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            datePickerDialog.setOnDateSetListener(this);
        }
        datePickerDialog.setCancelable(false);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        calendar.set(year,month,day);
        date.setTime(calendar.getTimeInMillis());
        thsDatePickerPresenter.setDate(date);
        if(mIsLaunchedFromDetails){
            thsDatePickerPresenter.launchProviderDetailBasedOnTime();
        }else {
            thsDatePickerPresenter.launchProviderListBasedOnTime();
        }
    }


    @Override
    public int getContainerID() {
        return ((ViewGroup)getView().getParent()).getId();
    }

    public Practice getPractice() {
        return mPractice;
    }

    public void setPractice(Practice mPractice) {
        this.mPractice = mPractice;
    }

    public Provider getProvider() {
        return mProvider;
    }

    public void setProvider(Provider mProvider) {
        this.mProvider = mProvider;
    }

    public THSProviderEntity getThsProviderEntity() {
        return mThsProviderEntity;
    }

    public void setThsProviderEntity(THSProviderEntity mThsProviderInfo) {
        this.mThsProviderEntity = mThsProviderInfo;
    }
}

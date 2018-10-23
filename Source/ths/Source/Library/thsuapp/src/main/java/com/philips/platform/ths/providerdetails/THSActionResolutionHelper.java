package com.philips.platform.ths.providerdetails;

import android.os.Bundle;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderCallback;
import com.philips.platform.ths.appointment.THSProviderNotAvailableFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.practice.THSPracticeCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.providerslist.THSProvidersListFragment;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSManager;

import java.util.Date;
import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_APPOINTMENTS;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PRACTICE;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER;

public class THSActionResolutionHelper {
    protected void launchAvailableProviderDetailBasedOnAvailibity(final Date date, final THSBaseFragment mThsBaseFragment, final THSProviderInfo thsProviderInfo, final Practice practice) {
        try {
            THSManager.getInstance().getProviderDetails(mThsBaseFragment.getContext(),
                    thsProviderInfo, new THSProviderDetailsCallback() {
                        @Override
                        public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
                            if (null != sdkError) {
                                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(), ANALYTIC_FETCH_PROVIDER, sdkError));
                            } else {
                                if (mThsBaseFragment instanceof THSProviderDetailsFragment) {
                                    ((THSProviderDetailsFragment) mThsBaseFragment).setProvider(provider);
                                } else {
                                    ((THSProvidersListFragment) mThsBaseFragment).setProvider(provider);
                                }
                                try {
                                    THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                                            date, new THSAvailableProviderCallback<List<Date>, THSSDKError>() {
                                                @Override
                                                public void onResponse(final List<Date> dates, THSSDKError sdkError) {
                                                    if (null != sdkError.getSdkError()) {
                                                        mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(), ANALYTICS_FETCH_APPOINTMENTS, sdkError.getSdkError()));
                                                    } else {
                                                        if (practice == null) {
                                                            try {
                                                                THSManager.getInstance().getPractice(mThsBaseFragment.getContext(), thsProviderInfo.getProviderInfo().getPracticeInfo(), new THSPracticeCallback<Practice, SDKError>() {
                                                                    @Override
                                                                    public void onResponse(Practice practice, SDKError practiceSdkError) {
                                                                        if (null != practiceSdkError) {
                                                                            if (null != practiceSdkError.getSDKErrorReason()) {
                                                                                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(), ANALYTIC_FETCH_PRACTICE, practiceSdkError));
                                                                            }
                                                                        } else {
                                                                            launchFragmentBasedOnAvailibity(practice, dates, date, mThsBaseFragment, thsProviderInfo);
                                                                        }
                                                                    }

                                                                    @Override
                                                                    public void onFailure(Throwable throwable) {
                                                                        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                                                                            mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
                                                                            mThsBaseFragment.hideProgressBar();
                                                                        }
                                                                    }
                                                                });
                                                            } catch (AWSDKInstantiationException e) {

                                                            }
                                                            return;
                                                        }
                                                        launchFragmentBasedOnAvailibity(practice, dates, date, mThsBaseFragment, thsProviderInfo);
                                                    }
                                                }

                                                @Override
                                                public void onFailure(Throwable throwable) {
                                                    if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                                                        mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
                                                        mThsBaseFragment.hideProgressBar();
                                                    }
                                                }
                                            });
                                } catch (AWSDKInstantiationException e) {

                                    mThsBaseFragment.hideProgressBar();
                                }
                            }
                        }

                        @Override
                        public void onProviderDetailsFetchError(Throwable throwable) {
                            if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                                mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
                            }
                            mThsBaseFragment.hideProgressBar();
                        }
                    });
        } catch (AWSDKInstantiationException e) {

            mThsBaseFragment.hideProgressBar();
        }
    }

    private void launchFragmentBasedOnAvailibity(Practice practice, List<Date> dates, Date date, THSBaseFragment mThsBaseFragment, THSProviderEntity thsProviderInfo) {
        if (dates == null || dates.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(THSConstants.THS_DATE, date);
            bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, practice);
            if (mThsBaseFragment instanceof THSProviderDetailsFragment) {
                bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSProviderDetailsFragment) mThsBaseFragment).getProvider());
                bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, ((THSProviderDetailsFragment) mThsBaseFragment).getProviderEntitiy());
            } else {
                bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSProvidersListFragment) mThsBaseFragment).getProvider());
                bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, thsProviderInfo);
            }
            final THSProviderNotAvailableFragment fragment = new THSProviderNotAvailableFragment();
            fragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
            mThsBaseFragment.addFragment(fragment, THSProviderNotAvailableFragment.TAG, bundle, true);
            mThsBaseFragment.hideProgressBar();
        } else {
            new THSBasePresenterHelper().launchAvailableProviderDetailFragment(mThsBaseFragment, thsProviderInfo,
                    date, practice);
        }
    }
}

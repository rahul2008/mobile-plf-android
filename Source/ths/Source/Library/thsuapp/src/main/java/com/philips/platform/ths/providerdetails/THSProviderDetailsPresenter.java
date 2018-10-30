/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.intake.THSVisitContextCallBack;
import com.philips.platform.ths.providerslist.THSOnDemandSpeciality;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.sdkerrors.THSSDKError;
import com.philips.platform.ths.sdkerrors.THSSDKErrorFactory;
import com.philips.platform.ths.utility.THSConstants;
import com.philips.platform.ths.utility.THSDateEnum;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.ths.utility.THSTagUtils;
import com.philips.platform.ths.welcome.THSWelcomeFragment;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import java.util.Calendar;
import java.util.Date;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_ON_DEMAND_SPECIALITIES;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_START_MATCHING;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_ALERT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

class THSProviderDetailsPresenter extends THSActionResolutionHelper implements THSBasePresenter, THSProviderDetailsCallback, THSFetchEstimatedCostCallback, THSMatchMakingCallback {

    private THSProviderDetailsViewInterface viewInterface;

    private THSBaseFragment mThsBaseFragment;

    THSProviderDetailsPresenter(THSProviderDetailsViewInterface viewInterface, THSBaseFragment thsBaseFragment) {
        this.viewInterface = viewInterface;
        mThsBaseFragment = thsBaseFragment;
    }

    void fetchProviderDetails() {
        try {
            if (viewInterface.getTHSProviderInfo() != null)
                getPTHManager().getProviderDetails(viewInterface.getContext(), viewInterface.getTHSProviderInfo(), this);
            else
                viewInterface.dismissRefreshLayout();
        } catch (AWSDKInstantiationException e) {

        }

    }

    private THSManager getPTHManager() {
        return THSManager.getInstance();
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            if (null != sdkError) {
                mThsBaseFragment.showError( THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(), ANALYTIC_FETCH_PROVIDER,sdkError), true, false);
            } else {
                /*try {
                    THSManager.getInstance().fetchEstimatedVisitCost(viewInterface.getContext(), provider, this);
                } catch (AWSDKInstantiationException e) {

                }*/
                viewInterface.updateView(provider);
            }
        }
    }

    @Override
    public void onProviderDetailsFetchError(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
            mThsBaseFragment.hideProgressBar();
        }
    }

    @Override
    public void onEvent(int componentID) {
        if (componentID == R.id.detailsButtonOne) {

                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "startInstantAppointment");
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, viewInterface.getTHSProviderInfo());
                bundle.putParcelable(THSConstants.THS_PROVIDER, viewInterface.getProvider());
                THSSymptomsFragment thsSymptomsFragment = new THSSymptomsFragment();
                thsSymptomsFragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
                mThsBaseFragment.addFragment(thsSymptomsFragment, THSSymptomsFragment.TAG, bundle, true);


        } else if (componentID == R.id.detailsButtonTwo) {
            THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "startSchedulingAnAppointment");
            final THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment, THSDateEnum.HIDEPREVDATEANDSIXMONTHSLATERDATE);


            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    thsDatePickerFragmentUtility.setCalendar(year, month, day);
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    THSProviderInfo thsProviderInfo = viewInterface.getTHSProviderInfo();
                    if (thsProviderInfo == null) {
                        final Provider provider = viewInterface.getProvider();
                        THSProviderInfo thsProviderInfo1 = new THSProviderInfo();
                        thsProviderInfo1.setTHSProviderInfo(provider);
                        thsProviderInfo = thsProviderInfo1;
                    }

                    launchAvailableProviderDetailBasedOnAvailibity(date, mThsBaseFragment, thsProviderInfo, viewInterface.getPractice());

                }
            };

            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);


        } else if (componentID == R.id.calendar_container) {
            THSDatePickerFragmentUtility thsDatePickerFragmentUtility = new THSDatePickerFragmentUtility(mThsBaseFragment, THSDateEnum.HIDEPREVDATEANDSIXMONTHSLATERDATE);


            final DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    THSAvailableProviderDetailFragment thsAvailableProviderDetailFragment = new THSAvailableProviderDetailFragment();
                    thsAvailableProviderDetailFragment.setTHSProviderEntity(viewInterface.getTHSProviderInfo());

                    Calendar calendar = Calendar.getInstance();
                    calendar.set(year, month, day);
                    Date date = new Date();
                    date.setTime(calendar.getTimeInMillis());

                    Bundle bundle = new Bundle();
                    bundle.putSerializable(THSConstants.THS_DATE, date);
                    bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, viewInterface.getPractice());
                    bundle.putParcelable(THSConstants.THS_PROVIDER, viewInterface.getProvider());
                    bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, viewInterface.getTHSProviderInfo());
                    thsAvailableProviderDetailFragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
                    mThsBaseFragment.addFragment(thsAvailableProviderDetailFragment, THSAvailableProviderDetailFragment.TAG, bundle, true);
                }
            };
            thsDatePickerFragmentUtility.showDatePicker(onDateSetListener);
        } else if (componentID == R.id.uid_dialog_positive_button) {
            // matchmaking failed
            ((THSProviderDetailsFragment) mThsBaseFragment).alertDialogFragment.dismiss();
            mThsBaseFragment.getFragmentManager().popBackStack(THSWelcomeFragment.TAG, 0);
        }
    }

    @Override
    public void onEstimatedCostFetchSuccess(EstimatedVisitCost estimatedVisitCost, SDKError sdkError) {

    }

    @Override
    public void onError(Throwable throwable) {
        if  (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }

    public void getFirstAvailableProvider(THSOnDemandSpeciality onDemandSpecialties) throws AWSDKInstantiationException {
        if(null == THSManager.getInstance().getVisitContext()) {
            THSManager.getInstance().getVisitContextWithOnDemandSpeciality(mThsBaseFragment.getContext(), onDemandSpecialties, new THSVisitContextCallBack<VisitContext, THSSDKError>() {
                @Override
                public void onResponse(VisitContext thsVisitContext, THSSDKError thssdkError) {
                    if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                        if (null != thssdkError.getSdkError()) {
                            mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(mThsBaseFragment.getFragmentActivity(), ANALYTICS_ON_DEMAND_SPECIALITIES, thssdkError.getSdkError()), true, false);
                        } else {
                            THSManager.getInstance().setVisitContext(thsVisitContext);
                            doMatchMaking();
                        }
                    }
                }

                @Override
                public void onFailure(Throwable throwable) {
                    if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
                        mThsBaseFragment.hideProgressBar();
                        mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
                    }

                }
            });
        }else {
            onProviderDetailsReceived(THSManager.getInstance().getProviderObject(), null);
        }
    }

    void doMatchMaking() {
        showMatchMakingProgressbar();
        try {
            THSManager.getInstance().doMatchMaking(mThsBaseFragment.getContext(), THSManager.getInstance().getVisitContext(), this);
        } catch (AWSDKInstantiationException e) {

        }
    }

    private void showMatchMakingProgressbar() {
        ((THSProviderDetailsFragment) mThsBaseFragment).displayDODView(true);
    }

    @Override
    public void onMatchMakingProviderFound(Provider provider) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            ((THSProviderDetailsFragment) mThsBaseFragment).displayDODView(false);
            THSManager.getInstance().setProviderObject(provider);// update visit context, now this visit containd providerInfo
            ((THSProviderDetailsFragment) mThsBaseFragment).mPracticeInfo = provider.getPracticeInfo();
            THSProviderInfo tHSProviderInfo = new THSProviderInfo();
            tHSProviderInfo.setTHSProviderInfo(provider);
            ((THSProviderDetailsFragment) mThsBaseFragment).mThsProviderInfo = tHSProviderInfo;
            ((THSProviderDetailsFragment) mThsBaseFragment).setProvider(provider);
            onProviderDetailsReceived(provider, null);
        }
    }

    @Override
    public void onMatchMakingProviderListExhausted() {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            ((THSProviderDetailsFragment) mThsBaseFragment).displayDODView(false);
            showMatchmakingError(true, true);
        }
    }

    @Override
    public void onMatchMakingRequestGone() {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            showMatchmakingError(true, true);
        }
    }

    @Override
    public void onMatchMakingResponse(Void aVoid, SDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            if (null != sdkError) {
                if (null != sdkError.getSDKErrorReason()) {
                    mThsBaseFragment.showError( THSSDKErrorFactory.getErrorType(mThsBaseFragment.getContext(), ANALYTICS_START_MATCHING,sdkError));
                }
            } else {
                showMatchmakingError(true, true);
            }
        }
    }

    @Override
    public void onMatchMakingFailure(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            showMatchmakingError(true, true);
            mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
        }

    }

    private void showMatchmakingError(final boolean showLargeContent, final boolean isWithTitle) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(mThsBaseFragment.getFragmentActivity())
                    .setMessage(showLargeContent ? mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_matchmaking_error_text) : mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_matchmaking_error_text)).
                            setPositiveButton(mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_Ok_title), ((THSProviderDetailsFragment) mThsBaseFragment));

            if (isWithTitle) {
                builder.setTitle(mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_matchmaking_error));

            }
            ((THSProviderDetailsFragment) mThsBaseFragment).alertDialogFragment = builder.setCancelable(false).create();
            ((THSProviderDetailsFragment) mThsBaseFragment).alertDialogFragment.show(mThsBaseFragment.getFragmentManager(), THS_PROVIDER_DETAIL_ALERT);
        }
    }


    void cancelMatchMaking() {
        try {
            THSManager.getInstance().cancelMatchMaking(mThsBaseFragment.getContext(), THSManager.getInstance().getVisitContext());
        } catch (AWSDKInstantiationException e) {

        }
    }
}

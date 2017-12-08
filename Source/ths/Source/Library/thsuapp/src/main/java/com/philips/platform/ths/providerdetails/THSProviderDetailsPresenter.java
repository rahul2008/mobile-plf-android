/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.providerdetails;

import android.app.DatePickerDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.DatePicker;

import com.americanwell.sdk.entity.SDKError;
import com.americanwell.sdk.entity.practice.Practice;
import com.americanwell.sdk.entity.provider.EstimatedVisitCost;
import com.americanwell.sdk.entity.provider.Provider;
import com.americanwell.sdk.entity.visit.VisitContext;
import com.americanwell.sdk.exception.AWSDKInstantiationException;
import com.philips.platform.ths.R;
import com.philips.platform.ths.appointment.THSAvailableProviderCallback;
import com.philips.platform.ths.appointment.THSAvailableProviderDetailFragment;
import com.philips.platform.ths.appointment.THSDatePickerFragmentUtility;
import com.philips.platform.ths.appointment.THSProviderNotAvailableFragment;
import com.philips.platform.ths.base.THSBaseFragment;
import com.philips.platform.ths.base.THSBasePresenter;
import com.philips.platform.ths.base.THSBasePresenterHelper;
import com.philips.platform.ths.intake.THSCheckPharmacyConditionsFragment;
import com.philips.platform.ths.intake.THSSymptomsFragment;
import com.philips.platform.ths.practice.THSPracticeCallback;
import com.philips.platform.ths.providerslist.THSProviderInfo;
import com.philips.platform.ths.registration.THSConsumerWrapper;
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
import java.util.List;

import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_FETCH_APPOINTMENTS;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTICS_START_MATCHING;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PRACTICE;
import static com.philips.platform.ths.sdkerrors.THSAnalyticTechnicalError.ANALYTIC_FETCH_PROVIDER;
import static com.philips.platform.ths.utility.THSConstants.THS_PROVIDER_DETAIL_ALERT;
import static com.philips.platform.ths.utility.THSConstants.THS_SEND_DATA;
import static com.philips.platform.ths.utility.THSConstants.THS_SPECIAL_EVENT;

class THSProviderDetailsPresenter implements THSBasePresenter, THSProviderDetailsCallback, THSFetchEstimatedCostCallback, THSMatchMakingCallback {

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
            e.printStackTrace();
        }

    }

    private THSManager getPTHManager() {
        return THSManager.getInstance();
    }

    @Override
    public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            if (null != sdkError) {
                mThsBaseFragment.showError( THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PROVIDER,sdkError), true);
            } else {
                THSConsumerWrapper thsConsumer = new THSConsumerWrapper();
                thsConsumer.setConsumer(viewInterface.getConsumerInfo());
                try {
                    THSManager.getInstance().fetchEstimatedVisitCost(viewInterface.getContext(), provider, this);
                } catch (AWSDKInstantiationException e) {
                    e.printStackTrace();
                }
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

            if (THSManager.getInstance().isMatchMakingVisit()) {
                // go to pharmacy and shipping if DOD
                mThsBaseFragment.addFragment(new THSCheckPharmacyConditionsFragment(), THSCheckPharmacyConditionsFragment.TAG, null, true);
            } else {
                THSTagUtils.doTrackActionWithInfo(THS_SEND_DATA, THS_SPECIAL_EVENT, "startInstantAppointment");
                THSConsumerWrapper THSConsumer = new THSConsumerWrapper();
                THSConsumer.setConsumer(viewInterface.getConsumerInfo());
                Bundle bundle = new Bundle();
                bundle.putParcelable(THSConstants.THS_PROVIDER_INFO, viewInterface.getTHSProviderInfo());
                bundle.putParcelable(THSConstants.THS_PROVIDER, viewInterface.getProvider());
                THSSymptomsFragment thsSymptomsFragment = new THSSymptomsFragment();
                thsSymptomsFragment.setConsumerObject(THSConsumer);
                thsSymptomsFragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
                mThsBaseFragment.addFragment(thsSymptomsFragment, THSSymptomsFragment.TAG, bundle, true);
            }

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

                    launchAvailableProviderDetailBasedOnAvailibity(date);

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

    private void launchAvailableProviderDetailBasedOnAvailibity(final Date date) {
        try {
            THSProviderInfo thsProviderInfo = viewInterface.getTHSProviderInfo();
            if (thsProviderInfo == null) {
                final Provider provider = viewInterface.getProvider();
                THSProviderInfo thsProviderInfo1 = new THSProviderInfo();
                thsProviderInfo1.setTHSProviderInfo(provider);
                thsProviderInfo = thsProviderInfo1;
            }
            THSManager.getInstance().getProviderDetails(mThsBaseFragment.getContext(),
                    thsProviderInfo, new THSProviderDetailsCallback() {
                        @Override
                        public void onProviderDetailsReceived(Provider provider, SDKError sdkError) {
                            if (null != sdkError) {
                                mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PROVIDER, sdkError));
                            } else {
                                ((THSProviderDetailsFragment) mThsBaseFragment).setProvider(provider);
                                try {
                                    THSManager.getInstance().getProviderAvailability(mThsBaseFragment.getContext(), provider,
                                            date, new THSAvailableProviderCallback<List<Date>, THSSDKError>() {
                                                @Override
                                                public void onResponse(final List<Date> dates, THSSDKError sdkError) {
                                                    if (null != sdkError.getSdkError()) {
                                                        mThsBaseFragment.showError(THSSDKErrorFactory.getErrorType(ANALYTICS_FETCH_APPOINTMENTS, sdkError.getSdkError()));
                                                    } else {
                                                        if (viewInterface.getPractice() == null) {
                                                            try {
                                                                THSManager.getInstance().getPractice(mThsBaseFragment.getContext(), viewInterface.getPracticeInfo(), new THSPracticeCallback<Practice, SDKError>() {
                                                                    @Override
                                                                    public void onResponse(Practice practice, SDKError practiceSdkError) {
                                                                        if (null != practiceSdkError) {
                                                                            if (null != practiceSdkError.getSDKErrorReason()) {
                                                                                mThsBaseFragment.showError( THSSDKErrorFactory.getErrorType(ANALYTIC_FETCH_PRACTICE,practiceSdkError));
                                                                            }
                                                                        } else {
                                                                            launchFragmentBasedOnAvailibity(practice, dates, date);
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
                                                                e.printStackTrace();
                                                            }
                                                            return;
                                                        }
                                                        launchFragmentBasedOnAvailibity(viewInterface.getPractice(), dates, date);
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
                                    e.printStackTrace();
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
            e.printStackTrace();
            mThsBaseFragment.hideProgressBar();
        }
    }

    private void launchFragmentBasedOnAvailibity(Practice practice, List<Date> dates, Date date) {
        if (dates == null || dates.size() == 0) {
            Bundle bundle = new Bundle();
            bundle.putSerializable(THSConstants.THS_DATE, date);
            bundle.putParcelable(THSConstants.THS_PRACTICE_INFO, practice);
            bundle.putParcelable(THSConstants.THS_PROVIDER, ((THSProviderDetailsFragment) mThsBaseFragment).getProvider());
            bundle.putParcelable(THSConstants.THS_PROVIDER_ENTITY, ((THSProviderDetailsFragment) mThsBaseFragment).getProviderEntitiy());
            final THSProviderNotAvailableFragment fragment = new THSProviderNotAvailableFragment();
            fragment.setFragmentLauncher(mThsBaseFragment.getFragmentLauncher());
            mThsBaseFragment.addFragment(fragment, THSProviderNotAvailableFragment.TAG, bundle, true);
            mThsBaseFragment.hideProgressBar();
        } else {
            new THSBasePresenterHelper().launchAvailableProviderDetailFragment(mThsBaseFragment, viewInterface.getTHSProviderInfo(),
                    date, practice);
        }
    }

    @Override
    public void onEstimatedCostFetchSuccess(EstimatedVisitCost estimatedVisitCost, SDKError sdkError) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            viewInterface.updateEstimatedCost(estimatedVisitCost);
        }
    }

    @Override
    public void onError(Throwable throwable) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.showError(mThsBaseFragment.getString(R.string.ths_se_server_error_toast_message));
        }
    }


    void doMatchMaking() {
        showMatchMakingProgressbar();
        try {
            THSManager.getInstance().doMatchMaking(mThsBaseFragment.getContext(), THSManager.getInstance().getPthVisitContext(), this);
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }

    private void showMatchMakingProgressbar() {
        ((THSProviderDetailsFragment) mThsBaseFragment).mProgressBarWithLabelContainer.setVisibility(View.VISIBLE);
        ((THSProviderDetailsFragment) mThsBaseFragment).showProgressbar();
        Resources resources = mThsBaseFragment.getResources();
        String highlightedChildMatchMakingMessage = resources.getString(R.string.ths_matchmaking_progressbar_meesage_child_text);
        String parentMatchmakingString = String.format(resources.getString(R.string.ths_matchmaking_progressbar_meesage_parent_text), highlightedChildMatchMakingMessage);
        SpannableString matchMakingProgrressMessage = new SpannableString(parentMatchmakingString);
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {

            }
        };
        int startingIndex = parentMatchmakingString.indexOf(highlightedChildMatchMakingMessage);
        int endIndex = startingIndex + highlightedChildMatchMakingMessage.length();
        matchMakingProgrressMessage.setSpan(clickableSpan, startingIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ((THSProviderDetailsFragment) mThsBaseFragment).mProgressBarLabel.setText(matchMakingProgrressMessage.toString());
    }

    @Override
    public void onMatchMakingProviderFound(Provider provider, VisitContext visitContext) {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.hideProgressBar();
            ((THSProviderDetailsFragment) mThsBaseFragment).mProgressBarWithLabelContainer.setVisibility(View.GONE);
            THSManager.getInstance().getPthVisitContext().setVisitContext(visitContext); // update visit context, now this visit containd providerInfo
            ((THSProviderDetailsFragment) mThsBaseFragment).mPracticeInfo = provider.getPracticeInfo();
            THSProviderInfo tHSProviderInfo = new THSProviderInfo();
            tHSProviderInfo.setTHSProviderInfo(provider);
            ((THSProviderDetailsFragment) mThsBaseFragment).mThsProviderInfo = tHSProviderInfo;
            ((THSProviderDetailsFragment) mThsBaseFragment).setProvider(provider);
            ((THSProviderDetailsFragment) mThsBaseFragment).dodProviderFoundMessage.setVisibility(View.VISIBLE);
            onProviderDetailsReceived(provider, null);
        }
    }

    @Override
    public void onMatchMakingProviderListExhausted() {
        if (null != mThsBaseFragment && mThsBaseFragment.isFragmentAttached()) {
            mThsBaseFragment.hideProgressBar();
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
                    mThsBaseFragment.showError( THSSDKErrorFactory.getErrorType(ANALYTICS_START_MATCHING,sdkError));
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
                            setPositiveButton(mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_matchmaking_ok_button), ((THSProviderDetailsFragment) mThsBaseFragment));

            if (isWithTitle) {
                builder.setTitle(mThsBaseFragment.getFragmentActivity().getResources().getString(R.string.ths_matchmaking_error));

            }
            ((THSProviderDetailsFragment) mThsBaseFragment).alertDialogFragment = builder.setCancelable(false).create();
            ((THSProviderDetailsFragment) mThsBaseFragment).alertDialogFragment.show(mThsBaseFragment.getFragmentManager(), THS_PROVIDER_DETAIL_ALERT);
        }
    }


    void cancelMatchMaking() {
        try {
            THSManager.getInstance().cancelMatchMaking(mThsBaseFragment.getContext(), THSManager.getInstance().getPthVisitContext());
        } catch (AWSDKInstantiationException e) {
            e.printStackTrace();
        }
    }


}

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.app.tagging.AppTagging;
import com.philips.cdp.registration.app.tagging.AppTagingConstants;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.ui.customviews.RegCoppaAlertDialog;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalCaringSharingFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;
import com.philips.cdp.registration.coppa.ui.fragment.RegistrationCoppaFragment;
import com.philips.cdp.registration.coppa.utils.AppCoppaTaggingConstants;
import com.philips.cdp.registration.coppa.utils.CoppaInterface;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;
import com.philips.ntputils.ServerTime;
import com.philips.ntputils.constants.ServerTimeConstants;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ParentalApprovalFragmentController implements RefreshUserHandler,
        View.OnClickListener {

    private final ServiceDiscoveryInterface serviceDiscoveryInterface;

    private boolean isParentalConsent = false;
    private ParentalApprovalFragment mParentalApprovalFragment;
    private CoppaExtension mCoppaExtension;
    private FragmentManager mFragmentManager;
    private View.OnClickListener mOkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegCoppaAlertDialog.dismissDialog();
            if (RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener() != null) {
                RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                        onUserRegistrationComplete( mParentalApprovalFragment.getActivity());
            }else {
                RegUtility.showErrorMessage(mParentalApprovalFragment.getActivity());
            }
        }
    };

    public ParentalApprovalFragmentController(ParentalApprovalFragment fragment) {
        serviceDiscoveryInterface = CoppaInterface.getComponent().getServiceDiscoveryInterface();
        mParentalApprovalFragment = fragment;
        mParentalApprovalFragment.getRegistrationFragment();
        if (fragment.getRegistrationFragment().getParentActivity() != null) {
            mParentalApprovalFragment.getRegistrationFragment();
            mCoppaExtension = new CoppaExtension(fragment.getRegistrationFragment()
                    .getParentActivity().getApplicationContext());
            final Bundle bunble = mParentalApprovalFragment.getArguments();
            if (bunble != null) {
                isParentalConsent = bunble.getBoolean(RegConstants.IS_FROM_PARENTAL_CONSENT, false);
            }
        }
    }

    public void refreshUser() {
        mParentalApprovalFragment.getRegistrationFragment();
        RegistrationCoppaFragment.showRefreshProgress(
                mParentalApprovalFragment.getActivity());
        final User user = new User(mParentalApprovalFragment.getContext());
        user.refreshUser(this);
    }

    public boolean isCountryUs() {
        boolean isCountryUs;
        if (getCoppaExtension().getConsent().getLocale() != null) {
            isCountryUs = getCoppaExtension().getConsent().getLocale().substring(3,5).equalsIgnoreCase(RegConstants.COUNTRY_CODE_US);
        } else {
            isCountryUs = RegistrationHelper.getInstance().getCountryCode().equalsIgnoreCase(RegConstants.COUNTRY_CODE_US);
        }
        return isCountryUs;
    }

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        mParentalApprovalFragment.showContent();
        mParentalApprovalFragment.getRegistrationFragment();
        RegistrationCoppaFragment.hideRefreshProgress();
        updateUIBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }

    @Override
    public void onRefreshUserFailed(int error) {
        mParentalApprovalFragment.getRegistrationFragment();
        RegistrationCoppaFragment.hideRefreshProgress();
        showServerErrorAlert();
    }

    public void showServerErrorAlert() {
        RegCoppaAlertDialog.showCoppaDialogMsg("",
                mParentalApprovalFragment.getContext().getResources().getString(
                        R.string.reg_JanRain_Server_Connection_Failed),
                mParentalApprovalFragment.getActivity(), mOkBtnClick);
    }

    public CoppaExtension getCoppaExtension() {
        return mCoppaExtension;
    }

    private void addParentalConsentFragment(final CoppaStatus coppaStatus) {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                ParentalCaringSharingFragment parentalCaringSharingFragment = new
                        ParentalCaringSharingFragment();
                final Bundle bundle = new Bundle();
                bundle.putString(RegConstants.COPPA_STATUS, coppaStatus.toString());
                parentalCaringSharingFragment.setArguments(bundle);
                final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container,
                        parentalCaringSharingFragment, "Parental Access");
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction " +
                                "Exception occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }

    private void updateUIBasedOnConsentStatus(final CoppaStatus coppaStatus) {
        RLog.d("Consent status :", "" + coppaStatus);
        if (coppaStatus == CoppaStatus.kDICOPPAConsentPending || coppaStatus ==
                CoppaStatus.kDICOPPAConsentNotGiven) {
            mParentalApprovalFragment.setConfirmApproval();
            RLog.d("ParentalApprovalFragmentController Consent Pending :", "");
        } else if (coppaStatus == CoppaStatus.kDICOPPAConfirmationGiven) {
            addParentalConsentFragment(coppaStatus);
        } else {
            //first consent success
            if (RegUtility.isCountryUS(mCoppaExtension.getConsent().getLocale())) {
                if ((hoursSinceLastConsent() >= 24L)) {
                    new User(mParentalApprovalFragment.getContext()).refreshUser(
                            new RefreshUserHandler() {
                                @Override
                                public void onRefreshUserSuccess() {
                                    mCoppaExtension.buildConfiguration();
                                    if (coppaStatus == CoppaStatus.kDICOPPAConfirmationPending ||
                                            coppaStatus == CoppaStatus.kDICOPPAConfirmationNotGiven) {
                                        RLog.d("ParentalApprovalFragmentController  :",
                                                "Consent status"
                                                        + hoursSinceLastConsent());
                                        AppTagging.trackAction(AppTagingConstants.SEND_DATA,
                                                AppTagingConstants.SPECIAL_EVENTS,
                                                AppCoppaTaggingConstants.SECOND_CONSENT_VIEW);
                                        // mParentalApprovalFragment.setIsUSRegionCode();
                                        addReConfirmParentalConsentFragment();
                                    } else {
                                        if (RegistrationConfiguration.getInstance().
                                                getUserRegistrationUIEventListener() != null) {
                                            RegistrationConfiguration.getInstance().
                                                    getUserRegistrationUIEventListener().
                                                    onUserRegistrationComplete(
                                                            mParentalApprovalFragment.getActivity());
                                        }
                                    }
                                }

                                @Override
                                public void onRefreshUserFailed(int error) {

                                }
                            });
                } else {

                    //Need to check and act on it on what bases it arraived here
                    //If comes from the general way then need not be load 24 hours screen else load
                    if (isParentalConsent) {
                        if (RegistrationConfiguration.getInstance().
                                getUserRegistrationUIEventListener() != null) {
                            RegistrationConfiguration.getInstance().
                                    getUserRegistrationUIEventListener().
                                    onUserRegistrationComplete(
                                            mParentalApprovalFragment.getActivity());
                        }
                    } else {
                        // Need to load the fragment of 24
                        addParentalConsentFragment(coppaStatus);
                    }
                }
            } else {
                if (RegistrationConfiguration.getInstance().
                        getUserRegistrationUIEventListener() != null) {
                    RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                            onUserRegistrationComplete( mParentalApprovalFragment.getActivity());
                }else {
                    RegUtility.showErrorMessage(mParentalApprovalFragment.getActivity());
                }
            }
        }
    }

    private long hoursSinceLastConsent() {

        Date date;
        SimpleDateFormat format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_COPPA,
                Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long diff = 0;
        try {
            date = format.parse(mCoppaExtension.getConsent().getStoredAt());
            long millisecondsatConsentGiven = date.getTime();

            final String timeNow = ServerTime.getCurrentUTCTimeWithFormat(
                    ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
            format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP, Locale.ROOT);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(timeNow);
            long timeinMillisecondsNow = date.getTime();
            diff = timeinMillisecondsNow - millisecondsatConsentGiven;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return diff / 3600000;
    }

    @Override
    public void onClick(View v) {
        final int id = v.getId();

        if (id == R.id.reg_btn_agree) {
          handleAgree();
        } else if (id == R.id.reg_btn_dis_agree) {

           handleDisAgree();

            if (mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentNotGiven
                    || mCoppaExtension.getCoppaEmailConsentStatus() ==
                    CoppaStatus.kDICOPPAConfirmationNotGiven) {
                if (RegistrationConfiguration.getInstance().
                        getUserRegistrationUIEventListener() != null) {
                    RegistrationConfiguration.getInstance().getUserRegistrationUIEventListener().
                            onUserRegistrationComplete( mParentalApprovalFragment.getActivity());
                }else {
                    RegUtility.showErrorMessage(mParentalApprovalFragment.getActivity());
                }
            }
        }
    }

    private void addReConfirmParentalConsentFragment() {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                final ParentalConsentFragment parentalConsentFragment =
                        new ParentalConsentFragment();
                final FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalConsentFragment,
                        "Parental Access");
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception occured in" +
                                " addFragment  :"
                                + e.getMessage());
            }
        }
    }
    private void handleAgree() {
        serviceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                agreeConstent(Locale.getDefault().getLanguage()+"_"+s.trim());
            }
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                agreeConstent(Locale.getDefault().getLanguage()+"_"+s.trim());
            }
        });
    }
    private void handleDisAgree() {
        serviceDiscoveryInterface.getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                disAgreeConstent(Locale.getDefault().getLanguage()+"_"+s.trim());

            }
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                disAgreeConstent(Locale.getDefault().getLanguage()+"_"+s.trim());
            }
        });
    }

    private void agreeConstent(String locale) {
        final ConsentHandler consentHandler = new ConsentHandler(mCoppaExtension,
                mParentalApprovalFragment.getContext());
        consentHandler.agreeConsent(AppTagingConstants.SEND_DATA, AppCoppaTaggingConstants.
                FIRST_LEVEL_CONSENT, mParentalApprovalFragment,locale);
    }

    private void disAgreeConstent(String locale) {
        final ConsentHandler consentHandler = new ConsentHandler(mCoppaExtension,
                mParentalApprovalFragment.getContext());
        consentHandler.disAgreeConsent( mParentalApprovalFragment,locale);
    }
}

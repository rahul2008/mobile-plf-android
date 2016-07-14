/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.coppa.ui.controllers;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.apptagging.AppTagingConstants;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.ui.customviews.RegCoppaAlertDialog;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalCaringSharingFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;
import com.philips.cdp.registration.coppa.utils.AppCoppaTaggingConstants;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.servertime.constants.ServerTimeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


public class ParentalApprovalFragmentController implements RefreshUserHandler, View.OnClickListener {
    private boolean isParentalConsent = false;
    private ParentalApprovalFragment mParentalApprovalFragment;
    private CoppaExtension mCoppaExtension;
    private boolean isCoppaConsent;
    public static FragmentManager mFragmentManager;
    private ParentalConsentFragment mParentalConsentFragment;


    public ParentalApprovalFragmentController(ParentalApprovalFragment fragment) {
        mParentalApprovalFragment = fragment;
        if (mParentalApprovalFragment.getRegistrationFragment().getParentActivity() != null) {
            mCoppaExtension = new CoppaExtension(mParentalApprovalFragment.getRegistrationFragment()
                    .getParentActivity().getApplicationContext());
            Bundle bunble = mParentalApprovalFragment.getArguments();
            if (bunble != null) {
                isParentalConsent = bunble.getBoolean(RegConstants.IS_FROM_PARENTAL_CONSENT, false);
            }
        }
    }


    public void refreshUser() {
        mParentalApprovalFragment.getRegistrationFragment().showRefreshProgress(mParentalApprovalFragment.getActivity());
        User user = new User(mParentalApprovalFragment.getContext());
        user.refreshUser(this);
    }

    public boolean isCountryUS() {
        boolean isCountryUS;
        if (getCoppaExtension().getConsent().getLocale() != null) {
            isCountryUS = getCoppaExtension().getConsent().getLocale().equalsIgnoreCase("en_US");
        } else {
            isCountryUS = RegistrationHelper.getInstance().getCountryCode().equalsIgnoreCase("US");
        }
        return isCountryUS;
    }

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        mParentalApprovalFragment.showContent();
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
        updateUIBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }


    private View.OnClickListener mOkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegCoppaAlertDialog.dismissDialog();
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
            }
        }
    };

    @Override
    public void onRefreshUserFailed(int error) {
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
        showServerErrorALert();

    }

    public void showServerErrorALert() {
        RegCoppaAlertDialog.showCoppaDialogMsg("", mParentalApprovalFragment.getContext().getResources().getString(R.string.reg_JanRain_Server_Connection_Failed), mParentalApprovalFragment.getActivity(), mOkBtnClick);
    }


    public CoppaExtension getCoppaExtension() {
        return mCoppaExtension;
    }

    private void addParentalConsentFragment(final CoppaStatus coppaStatus) {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                ParentalCaringSharingFragment parentalCaringSharingFragment = new ParentalCaringSharingFragment();
                Bundle bundle = new Bundle();
                bundle.putString(RegConstants.COPPA_STATUS, coppaStatus.toString());
                parentalCaringSharingFragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalCaringSharingFragment, "Parental Access");
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }

    private void updateUIBasedOnConsentStatus(final CoppaStatus coppaStatus) {
        RLog.d("Consent status :", "" + coppaStatus);
        if (coppaStatus == CoppaStatus.kDICOPPAConsentPending || coppaStatus == CoppaStatus.kDICOPPAConsentNotGiven) {
            mParentalApprovalFragment.setConfirmApproval();
            isCoppaConsent = true;
            RLog.d("ParentalApprovalFragmentController Consent Pending :", "");
        } else if (coppaStatus == CoppaStatus.kDICOPPAConfirmationGiven) {
            addParentalConsentFragment(coppaStatus);
        } else {
            //first consent success
            if (mCoppaExtension.getConsent().getLocale().equalsIgnoreCase("en_US")) {
                if ((hoursSinceLastConsent() >= 24L)) {
                    new User(mParentalApprovalFragment.getContext()).refreshUser(new RefreshUserHandler() {
                        @Override
                        public void onRefreshUserSuccess() {
                            mCoppaExtension.buildConfiguration();
                            if (coppaStatus == CoppaStatus.kDICOPPAConfirmationPending || coppaStatus == CoppaStatus.kDICOPPAConfirmationNotGiven) {
                                RLog.d("ParentalApprovalFragmentController  :", "Consent status" + hoursSinceLastConsent());
                                isCoppaConsent = false;
                                AppTagging.trackAction(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS, AppCoppaTaggingConstants.SECOND_CONSENT_VIEW);
                                // mParentalApprovalFragment.setIsUSRegionCode();
                                addReConfirmParentalConsentFragment();

                            } else {
                                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
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
                        if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                            RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                        }
                    } else {
                        // Need to load the fragment of 24
                        addParentalConsentFragment(coppaStatus);
                    }

                }
            } else {
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                }
            }
        }


    }

    private long hoursSinceLastConsent() {

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_COPPA, Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long diff = 0;
        try {
            date = format.parse(mCoppaExtension.getConsent().getStoredAt());
            long millisecondsatConsentGiven = date.getTime();

            String timeNow = ServerTime.getInstance().getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
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
        int id = v.getId();
        ConsentHandler consentHandler = new ConsentHandler(mCoppaExtension, mParentalApprovalFragment.getContext());
        ConfirmationHandler confirmationHandler = new ConfirmationHandler(mCoppaExtension, mParentalApprovalFragment.getContext());
        if (id == R.id.reg_btn_agree) {
            if (isCoppaConsent) {
                consentHandler.agreeConsent(AppTagingConstants.SEND_DATA, AppCoppaTaggingConstants.FIRST_LEVEL_CONSENT, mParentalApprovalFragment);

            } else {
                confirmationHandler.agreeConfirmation(AppTagingConstants.SEND_DATA, AppCoppaTaggingConstants.SECOND_LEVEL_CONSENT, mParentalConsentFragment);

            }
        } else if (id == R.id.reg_btn_dis_agree) {

            if (isCoppaConsent) {
                consentHandler.disAgreeConsent(mParentalApprovalFragment);

            } else {
                confirmationHandler.disAgreeConfirmation(mParentalConsentFragment);
            }

            if (mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentNotGiven || mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConfirmationNotGiven) {
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                }
            }

        }


    }

    private void addReConfirmParentalConsentFragment() {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                ParentalConsentFragment parentalConsentFragment = new ParentalConsentFragment();
                FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container, parentalConsentFragment, "Parental Access");
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction Exception occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }
}

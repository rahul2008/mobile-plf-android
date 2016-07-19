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

public class ParentalApprovalFragmentController implements RefreshUserHandler,
        View.OnClickListener {
    private boolean isParentalConsent = false;
    private ParentalApprovalFragment mParentalApprovalFragment;
    private CoppaExtension mCoppaExtension;
    private FragmentManager mFragmentManager;
    private View.OnClickListener mOkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegCoppaAlertDialog.dismissDialog();
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                        notifyonUserRegistrationCompleteEventOccurred(
                                mParentalApprovalFragment.getActivity());
            }
        }
    };

    public ParentalApprovalFragmentController(ParentalApprovalFragment fragment) {
        mParentalApprovalFragment = fragment;
        if (mParentalApprovalFragment.getRegistrationFragment().getParentActivity() != null) {
            mCoppaExtension = new CoppaExtension(mParentalApprovalFragment.getRegistrationFragment()
                    .getParentActivity().getApplicationContext());
            final Bundle bunble = mParentalApprovalFragment.getArguments();
            if (bunble != null) {
                isParentalConsent = bunble.getBoolean(RegConstants.IS_FROM_PARENTAL_CONSENT, false);
            }
        }
    }

    public void refreshUser() {
        mParentalApprovalFragment.getRegistrationFragment().showRefreshProgress(
                mParentalApprovalFragment.getActivity());
        final User user = new User(mParentalApprovalFragment.getContext());
        user.refreshUser(this);
    }

    public boolean isCountryUs() {
        boolean isCountryUs;
        if (getCoppaExtension().getConsent().getLocale() != null) {
            isCountryUs = getCoppaExtension().getConsent().getLocale().equalsIgnoreCase("en_US");
        } else {
            isCountryUs = RegistrationHelper.getInstance().getCountryCode().equalsIgnoreCase("US");
        }
        return isCountryUs;
    }

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        mParentalApprovalFragment.showContent();
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
        updateUIBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }

    @Override
    public void onRefreshUserFailed(int error) {
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
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
            if (mCoppaExtension.getConsent().getLocale().equalsIgnoreCase("en_US")) {
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
                                        if (RegistrationCoppaHelper.getInstance().
                                                getUserRegistrationListener() != null) {
                                            RegistrationCoppaHelper.getInstance().
                                                    getUserRegistrationListener()
                                                    .notifyonUserRegistrationCompleteEventOccurred(
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
                        if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() !=
                                null) {
                            RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                                    notifyonUserRegistrationCompleteEventOccurred(
                                            mParentalApprovalFragment.getActivity());
                        }
                    } else {
                        // Need to load the fragment of 24
                        addParentalConsentFragment(coppaStatus);
                    }
                }
            } else {
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                            notifyonUserRegistrationCompleteEventOccurred(
                                    mParentalApprovalFragment.getActivity());
                }
            }
        }
    }

    private long hoursSinceLastConsent() {

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_COPPA,
                Locale.ROOT);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long diff = 0;
        try {
            date = format.parse(mCoppaExtension.getConsent().getStoredAt());
            long millisecondsatConsentGiven = date.getTime();

            final String timeNow = ServerTime.getInstance().getCurrentUTCTimeWithFormat(
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
        final ConsentHandler consentHandler = new ConsentHandler(mCoppaExtension,
                mParentalApprovalFragment.getContext());
        if (id == R.id.reg_btn_agree) {
            consentHandler.agreeConsent(AppTagingConstants.SEND_DATA, AppCoppaTaggingConstants.
                    FIRST_LEVEL_CONSENT, mParentalApprovalFragment);
        } else if (id == R.id.reg_btn_dis_agree) {

            consentHandler.disAgreeConsent(mParentalApprovalFragment);

            if (mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentNotGiven
                    || mCoppaExtension.getCoppaEmailConsentStatus() ==
                    CoppaStatus.kDICOPPAConfirmationNotGiven) {
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                            notifyonUserRegistrationCompleteEventOccurred(
                                    mParentalApprovalFragment.getActivity());
                }
            }
        }
    }

    private void addReConfirmParentalConsentFragment() {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                final ParentalConsentFragment parentalConsentFragment = new ParentalConsentFragment();
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
}

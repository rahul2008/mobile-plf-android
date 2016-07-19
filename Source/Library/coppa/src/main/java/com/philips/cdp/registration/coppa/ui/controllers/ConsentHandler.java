/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 *
 */

package com.philips.cdp.registration.coppa.ui.controllers;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.apptagging.AppTagging;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalCaringSharingFragment;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by 310202337 on 5/3/2016.
 */
public class ConsentHandler implements RefreshUserHandler {

    private static FragmentManager mFragmentManager;
    final private CoppaExtension mCoppaExtension;
    private final Context mContext;
    private final User mUser;
    private String mTaggingState;
    private String mTaggingKey;
    private ParentalApprovalFragment mParentalApprovalFragment;

    public ConsentHandler(CoppaExtension coppaExtension, Context context) {
        mContext = context;
        mCoppaExtension = coppaExtension;
        mUser = new User(mContext);
    }

    public void agreeConsent(final String taggingState, final String taggingKey,
                             ParentalApprovalFragment parentalApprovalFragment) {
        mParentalApprovalFragment = parentalApprovalFragment;
        mTaggingState = taggingState;
        mTaggingKey = taggingKey;
        mParentalApprovalFragment.getRegistrationFragment().showRefreshProgress(
                mParentalApprovalFragment.getActivity());
        mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mCoppaExtension.updateCoppaConsentStatus(true, new CoppaConsentUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        AppTagging.trackAction(mTaggingState, mTaggingKey, "Yes");
                        mUser.refreshUser(ConsentHandler.this);
                    }

                    @Override
                    public void onFailure(int errorCode) {
                        AppTagging.trackAction(mTaggingState, mTaggingKey, "No");
                        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
                        if (errorCode == -1) {
                            Toast.makeText(mParentalApprovalFragment.getContext(),
                                    mParentalApprovalFragment.getContext().getResources().getString(
                                            R.string.reg_JanRain_Server_Connection_Failed)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                handleFailure();
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        });
    }

    public void disAgreeConsent(ParentalApprovalFragment parentalApprovalFragment) {
        mParentalApprovalFragment = parentalApprovalFragment;
        mParentalApprovalFragment.getRegistrationFragment().showRefreshProgress(
                mParentalApprovalFragment.getActivity());
        mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mCoppaExtension.updateCoppaConsentStatus(false, new CoppaConsentUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        mUser.refreshUser(new RefreshUserHandler() {
                            @Override
                            public void onRefreshUserSuccess() {
                                mParentalApprovalFragment.getRegistrationFragment().
                                        hideRefreshProgress();
                                mCoppaExtension.buildConfiguration();
                                if (RegistrationCoppaHelper.getInstance().
                                        getUserRegistrationListener() != null) {
                                    RegistrationCoppaHelper.getInstance().
                                            getUserRegistrationListener().
                                            notifyonUserRegistrationCompleteEventOccurred(
                                                    mParentalApprovalFragment.getActivity());
                                }
                            }

                            @Override
                            public void onRefreshUserFailed(final int error) {
                                mParentalApprovalFragment.getRegistrationFragment().
                                        hideRefreshProgress();
                                Toast.makeText(mParentalApprovalFragment.getContext(),
                                        mParentalApprovalFragment.getContext().
                                                getResources().getString(
                                                R.string.reg_JanRain_Server_Connection_Failed)
                                        , Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onFailure(int errorCode) {
                        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
                        if (errorCode == -1) {
                            Toast.makeText(mParentalApprovalFragment.getContext(),
                                    mParentalApprovalFragment.getContext().getResources()
                                            .getString(R.string.reg_JanRain_Server_Connection_Failed)
                                    , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override
            public void onRefreshLoginSessionFailedWithError(int error) {
                handleFailure();
            }

            @Override
            public void onRefreshLoginSessionInProgress(String message) {

            }
        });
    }

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        RLog.i("ConsentStatus", "Status :  " + mCoppaExtension.getCoppaEmailConsentStatus());
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
        updateUiBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }

    private void updateUiBasedOnConsentStatus(final CoppaStatus coppaStatus) {
        if (coppaStatus == CoppaStatus.kDICOPPAConfirmationPending) {
            completeConsentActions(coppaStatus);
        } else if (coppaStatus == CoppaStatus.kDICOPPAConfirmationGiven) {
            completeConsentActions(coppaStatus);
        } else {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                        notifyonUserRegistrationCompleteEventOccurred(
                                mParentalApprovalFragment.getActivity());
            }
        }
    }

    private void completeConsentActions(final CoppaStatus coppaStatus) {
        if (mCoppaExtension.getConsent().getLocale().equalsIgnoreCase("en_US")) {
            //show thank you and 24 hour screen
            addParentalConsentFragment(coppaStatus);
        } else {
            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().
                        notifyonUserRegistrationCompleteEventOccurred(
                                mParentalApprovalFragment.getActivity());
            }
        }
    }

    @Override
    public void onRefreshUserFailed(int error) {
        AppTagging.trackAction(mTaggingState, mTaggingKey, "No");
        handleFailure();
    }

    private void handleFailure() {
        mParentalApprovalFragment.getRegistrationFragment().hideRefreshProgress();
        Toast.makeText(mParentalApprovalFragment.getContext(),
                mParentalApprovalFragment.getContext().getResources().getString(
                        R.string.reg_JanRain_Server_Connection_Failed)
                , Toast.LENGTH_SHORT).show();
    }

    private void addParentalConsentFragment(final CoppaStatus coppaStatus) {
        mFragmentManager = mParentalApprovalFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                final ParentalCaringSharingFragment parentalCaringSharingFragment = new
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
                        "RegistrationCoppaFragment :FragmentTransaction Exception " +
                                "occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }
}

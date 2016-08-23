/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
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
import com.philips.cdp.registration.coppa.ui.fragment.ParentalCaringSharingFragment;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalConsentFragment;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.handlers.RefreshLoginSessionHandler;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * ConfirmationHandler class handle coppa 2nd constent .
 */

public class ConfirmationHandler implements RefreshUserHandler {

    private static FragmentManager mFragmentManager;
    final private CoppaExtension mCoppaExtension;
    final private Context mContext;
    final private User mUser;
    private String mTaggingState;
    private String mTaggingKey;
    private ParentalConsentFragment mParentalConsentFragment;

    public ConfirmationHandler(CoppaExtension coppaExtension, Context context) {
        mContext = context;
        mCoppaExtension = coppaExtension;
        mUser = new User(mContext);
    }

    public void agreeConfirmation(final String taggingState, final String taggingKey,
                                  ParentalConsentFragment parentalConsentFragment) {
        mParentalConsentFragment = parentalConsentFragment;
        mTaggingState = taggingState;
        mTaggingKey = taggingKey;
        mParentalConsentFragment.showRefreshProgress();
        mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mCoppaExtension.updateCoppaConsentConfirmationStatus(true,
                        new CoppaConsentUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                //2nd Consent
                                mUser.refreshUser(new RefreshUserHandler() {
                                    @Override
                                    public void onRefreshUserSuccess() {
                                        if ( RegistrationCoppaHelper.getInstance().
                                                getUserRegistrationUIEventListener() != null) {
                                            RegistrationCoppaHelper.getInstance().
                                                    getUserRegistrationUIEventListener().
                                                    onUserRegistrationComplete(
                                                            mParentalConsentFragment.getActivity());
                                        }
                                    }

                                    @Override
                                    public void onRefreshUserFailed(final int error) {
                                        AppTagging.trackAction(mTaggingState, mTaggingKey, "No");
                                        handleFailure();
                                    }
                                });
                                AppTagging.trackAction(mTaggingState, mTaggingKey, "Yes");
                                mParentalConsentFragment.hideRefreshProgress();
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                AppTagging.trackAction(mTaggingState, mTaggingKey, "No");
                                mParentalConsentFragment.hideRefreshProgress();
                                if (errorCode == -1) {
                                    Toast.makeText(mParentalConsentFragment.getContext(),
                                            mParentalConsentFragment.getContext().
                                                    getResources().getString(
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

    public void disAgreeConfirmation(ParentalConsentFragment parentalConsentFragment) {
        mParentalConsentFragment = parentalConsentFragment;
        mParentalConsentFragment.showRefreshProgress();
        mUser.refreshLoginSession(new RefreshLoginSessionHandler() {
            @Override
            public void onRefreshLoginSessionSuccess() {
                mCoppaExtension.updateCoppaConsentConfirmationStatus(false,
                        new CoppaConsentUpdateCallback() {
                            @Override
                            public void onSuccess() {
                                mParentalConsentFragment.hideRefreshProgress();
                                mCoppaExtension.buildConfiguration();
                                if ( RegistrationCoppaHelper.getInstance().
                                        getUserRegistrationUIEventListener() != null) {
                                    RegistrationCoppaHelper.getInstance().
                                            getUserRegistrationUIEventListener().
                                            onUserRegistrationComplete(
                                                    mParentalConsentFragment.getActivity());
                                }
                            }

                            @Override
                            public void onFailure(int errorCode) {
                                mParentalConsentFragment.hideRefreshProgress();
                                if (errorCode == -1) {
                                    Toast.makeText(mParentalConsentFragment.getContext(),
                                            mParentalConsentFragment.getContext().
                                                    getResources().getString(
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

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        RLog.i("ConsentStatus", "Status :  " + mCoppaExtension.getCoppaEmailConsentStatus());
        mParentalConsentFragment.hideRefreshProgress();
        updateUiBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }

    private void updateUiBasedOnConsentStatus(final CoppaStatus coppaStatus) {
        if (coppaStatus == CoppaStatus.kDICOPPAConfirmationPending) {
            //show thank you and 24 hour screen
            addParentalConsentFragment(coppaStatus);
        } else if (coppaStatus == CoppaStatus.kDICOPPAConfirmationGiven) {
            //show thank you and 24 hour screen
            addParentalConsentFragment(coppaStatus);
        } else {
            if ( RegistrationCoppaHelper.getInstance().
                    getUserRegistrationUIEventListener() != null) {
                RegistrationCoppaHelper.getInstance().
                        getUserRegistrationUIEventListener().
                        onUserRegistrationComplete(
                                mParentalConsentFragment.getActivity());
            }
        }
    }

    @Override
    public void onRefreshUserFailed(int error) {
        AppTagging.trackAction(mTaggingState, mTaggingKey, "No");
        handleFailure();
    }

    private void handleFailure() {
        mParentalConsentFragment.hideRefreshProgress();

        Toast.makeText(mParentalConsentFragment.getContext(),
                mParentalConsentFragment.getContext().getResources().getString(
                        R.string.reg_JanRain_Server_Connection_Failed)
                , Toast.LENGTH_SHORT).show();
    }

    private void addParentalConsentFragment(final CoppaStatus coppaStatus) {
        mFragmentManager = mParentalConsentFragment.getParentFragment().getChildFragmentManager();
        if (mFragmentManager != null) {
            try {
                ParentalCaringSharingFragment parentalCaringSharingFragment =
                        new ParentalCaringSharingFragment();
                final Bundle bundle = new Bundle();
                bundle.putString(RegConstants.COPPA_STATUS, coppaStatus.toString());
                parentalCaringSharingFragment.setArguments(bundle);
                final FragmentTransaction fragmentTransaction =
                        mFragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fl_reg_fragment_container,
                        parentalCaringSharingFragment, "Parental Access");
                fragmentTransaction.commitAllowingStateLoss();
            } catch (IllegalStateException e) {
                RLog.e(RLog.EXCEPTION,
                        "RegistrationCoppaFragment :FragmentTransaction" +
                                " Exception occured in addFragment  :"
                                + e.getMessage());
            }
        }
    }
}
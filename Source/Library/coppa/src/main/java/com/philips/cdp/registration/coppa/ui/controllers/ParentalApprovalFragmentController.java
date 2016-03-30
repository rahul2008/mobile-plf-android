package com.philips.cdp.registration.coppa.ui.controllers;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.coppa.ui.customviews.RegCoppaAlertDialog;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaHelper;
import com.philips.cdp.registration.coppa.utils.RegistrationCoppaLaunchHelper;
import com.philips.cdp.registration.handlers.RefreshUserHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.servertime.ServerTime;
import com.philips.cdp.servertime.constants.ServerTimeConstants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


/**
 * Created by 310202337 on 3/29/2016.
 */
 public class ParentalApprovalFragmentController implements RefreshUserHandler, View.OnClickListener {
    private ParentalApprovalFragment mParentalApprovalFragment;
    private CoppaExtension mCoppaExtension;
    private boolean isCoppaConsent;

    public ParentalApprovalFragmentController( ParentalApprovalFragment fragment){
        mParentalApprovalFragment = fragment;
        mCoppaExtension = new CoppaExtension();
    }


    public void refreshUser(){
        mParentalApprovalFragment.showRefreshProgress();
        User user = new User(mParentalApprovalFragment.getContext());
        user.refreshUser(this);



    }

    @Override
    public void onRefreshUserSuccess() {
        mCoppaExtension.buildConfiguration();
        mParentalApprovalFragment.hideRefreshProgress();
        updateUIBasedOnConsentStatus(mCoppaExtension.getCoppaEmailConsentStatus());
    }

    private void navigateToWelcome(){
        RegistrationCoppaLaunchHelper.isBackEventConsumedByRegistration(mParentalApprovalFragment.getActivity());
    }

    private View.OnClickListener mOkBtnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            RegCoppaAlertDialog.dismissDialog();
            navigateToWelcome();
        }
    };

    @Override
    public void onRefreshUserFailed(int error) {
        mParentalApprovalFragment.hideRefreshProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                RegCoppaAlertDialog.showCoppaDialogMsg("", "Server Connection Failed", mParentalApprovalFragment.getActivity(), mOkBtnClick);
            }
        }, 1000);

    }

    public CoppaExtension getCoppaExtension(){
        return mCoppaExtension;
    }

    private void updateUIBasedOnConsentStatus(final CoppaStatus coppaStatus){
        System.out.println("Consent status " +coppaStatus);
        if(coppaStatus == CoppaStatus.kDICOPPAConsentPending){
            mParentalApprovalFragment.setConfirmApproval();
            isCoppaConsent = true;
            System.out.println("Consent Pending");
        } else{

            if(mCoppaExtension.getConsent().getLocale().equalsIgnoreCase("en_US")) {
                if ( (hoursSinceLastConsent() >= 24)) {
                     new User(mParentalApprovalFragment.getContext()).refreshUser(new RefreshUserHandler() {
                         @Override
                         public void onRefreshUserSuccess() {
                             mCoppaExtension.buildConfiguration();
                             if (coppaStatus == CoppaStatus.kDICOPPAConfirmationPending) {
                                 System.out.println("Consent hours" + hoursSinceLastConsent());
                                 isCoppaConsent = false;

                                 mParentalApprovalFragment.setIsUSRegionCode();
                             }else{
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
                    if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                        RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                    }
                }
            }else{
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                }
            }
        }



    }

    private int hoursSinceLastConsent(){

        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_COPPA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long diff = 0;
        try {
            date = format.parse(mCoppaExtension.getConsent().getStoredAt());
            long millisecondsatConsentGiven = date.getTime();

            String timeNow = ServerTime.getInstance().getCurrentUTCTimeWithFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
            format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
            format.setTimeZone(TimeZone.getTimeZone("UTC"));
            date = format.parse(timeNow);
            long timeinMillisecondsNow = date.getTime();
            diff = timeinMillisecondsNow - millisecondsatConsentGiven;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        return (int)diff/3600000;

    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.reg_btn_agree) {

                if(isCoppaConsent) {
                    mParentalApprovalFragment.showRefreshProgress();
                    mCoppaExtension.updateCoppaConsentStatus(mParentalApprovalFragment.getContext(), true, new CoppaConsentUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            mParentalApprovalFragment.hideRefreshProgress();
                            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            mParentalApprovalFragment.hideRefreshProgress();
                        }
                    });
                }else{
                    mParentalApprovalFragment.showRefreshProgress();
                    mCoppaExtension.updateCoppaConsentConfirmationStatus(mParentalApprovalFragment.getContext(), true, new CoppaConsentUpdateCallback() {
                        @Override
                        public void onSuccess() {
                            mParentalApprovalFragment.hideRefreshProgress();
                            if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            mParentalApprovalFragment.hideRefreshProgress();
                        }
                    });
                }

          /*  if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
            }*/
        } else if (id == R.id.reg_btn_dis_agree) {

            if(isCoppaConsent ){
                mParentalApprovalFragment.showRefreshProgress();
                mCoppaExtension.updateCoppaConsentStatus(mParentalApprovalFragment.getContext(), false, new CoppaConsentUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        mParentalApprovalFragment.hideRefreshProgress();
                        if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                            RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                        }
                    }

                    @Override
                    public void onFailure(String message) {
                        mParentalApprovalFragment.hideRefreshProgress();
                    }
                });
            }else{
                mCoppaExtension.updateCoppaConsentConfirmationStatus(mParentalApprovalFragment.getContext(), false, new CoppaConsentUpdateCallback() {
                    @Override
                    public void onSuccess() {
                        if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                            RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                        }
                    }

                    @Override
                    public void onFailure(String message) {

                    }
                });
            }



            if(mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConsentNotGiven || mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConfirmationNotGiven){
                if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                    RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
                }
            }

        }


    }


}

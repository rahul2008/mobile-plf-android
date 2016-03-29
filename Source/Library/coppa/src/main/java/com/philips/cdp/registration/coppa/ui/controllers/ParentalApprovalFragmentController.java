package com.philips.cdp.registration.coppa.ui.controllers;

import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.philips.cdp.registration.User;
import com.philips.cdp.registration.coppa.base.CoppaStatus;
import com.philips.cdp.registration.coppa.R;
import com.philips.cdp.registration.coppa.base.CoppaExtension;
import com.philips.cdp.registration.coppa.interfaces.CoppaConsentUpdateCallback;
import com.philips.cdp.registration.coppa.ui.fragment.ParentalApprovalFragment;
import com.philips.cdp.registration.coppa.utils.RegCoppaAlertDialog;
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

    public ParentalApprovalFragmentController( ParentalApprovalFragment fragment){
        mParentalApprovalFragment = fragment;
        mCoppaExtension = new CoppaExtension();
    }


    public void refreshUser(){
        User user = new User(mParentalApprovalFragment.getContext());
        user.refreshUser(this);
        mParentalApprovalFragment.showRefreshProgress();


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
                RegCoppaAlertDialog.showResetPasswordDialog("", "Server Connection Failed", mParentalApprovalFragment.getActivity(), mOkBtnClick);
            }
        }, 1000);

    }

    private void updateUIBasedOnConsentStatus(CoppaStatus coppaStatus){
        hoursSinceLastConsent();
        if(coppaStatus == CoppaStatus.kDICOPPAConsentPending){
            mParentalApprovalFragment.setConfirmApproval();
        }else if(coppaStatus == CoppaStatus.kDICOPPAConsentGiven){
            if(RegistrationHelper.getInstance().getLocale(mParentalApprovalFragment.getContext()).toString().equalsIgnoreCase("en-US")) {
                if (hoursSinceLastConsent() >= 24) {
                   refreshUser();


                }
            }
        }else if(coppaStatus == CoppaStatus.kDICOPPAConfirmationPending){
            mParentalApprovalFragment.setIsUSRegionCode();
        }

    }

    private int hoursSinceLastConsent(){
        Date date = null;
        SimpleDateFormat format = new SimpleDateFormat(ServerTimeConstants.DATE_FORMAT_FOR_JUMP);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        long diff = 0;
        try {
             date = format.parse(mCoppaExtension.getConsent().getStoredAt());
            long millisecondsatConsentGiven = date.getTime();
            String timeNow = ServerTime.getInstance().getCurrentTime();
            Date dateNow = format.parse(timeNow);
            long timeinMillisecondsNow = dateNow.getTime();
             diff = timeinMillisecondsNow - millisecondsatConsentGiven;

        } catch (ParseException e) {
            e.printStackTrace();
        }

        System.out.println("Hours Since last Consent " +diff/3600000);

        return (int)diff/3600000;


    }



    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.reg_btn_agree) {
            Toast.makeText(mParentalApprovalFragment.getRegistrationFragment().getParentActivity().getApplicationContext(), "Agree", Toast.LENGTH_SHORT).show();

                mCoppaExtension.updateCoppaConsentStatus(mParentalApprovalFragment.getContext(), true, new CoppaConsentUpdateCallback() {
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

          /*  if (RegistrationCoppaHelper.getInstance().getUserRegistrationListener() != null) {
                RegistrationCoppaHelper.getInstance().getUserRegistrationListener().notifyonUserRegistrationCompleteEventOccurred(mParentalApprovalFragment.getActivity());
            }*/
        } else if (id == R.id.reg_btn_dis_agree) {

            if(mCoppaExtension.getCoppaEmailConsentStatus() == CoppaStatus.kDICOPPAConfirmationPending ){
                mCoppaExtension.updateCoppaConsentStatus(mParentalApprovalFragment.getContext(), false, new CoppaConsentUpdateCallback() {
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

        }


    }


}

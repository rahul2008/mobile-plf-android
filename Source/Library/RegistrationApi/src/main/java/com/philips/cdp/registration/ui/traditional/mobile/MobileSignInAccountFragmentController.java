package com.philips.cdp.registration.ui.traditional.mobile;

import android.view.View;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.apptagging.AppTaggingPages;
import com.philips.cdp.registration.dao.UserRegistrationFailureInfo;
import com.philips.cdp.registration.events.EventListener;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.TraditionalLoginHandler;
import com.philips.cdp.registration.ui.customviews.onUpdateListener;
import com.philips.cdp.registration.ui.traditional.SignInAccountFragment;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;

/**
 * Created by 310243576 on 6/21/2016.
 */
public class MobileSignInAccountFragmentController implements NetworStateListener, EventListener, onUpdateListener,TraditionalLoginHandler,View.OnClickListener {

    MobileSignInAccountFragment mMobileSignInAccountFragment;

    public MobileSignInAccountFragmentController(MobileSignInAccountFragment fragment) {
        mMobileSignInAccountFragment = fragment;
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        RLog.i(RLog.NETWORK_STATE, "SignInAccountFragment : onNetWorkStateReceived state :"
                + isOnline);
        mMobileSignInAccountFragment.handleUiState();
        mMobileSignInAccountFragment.updateUiStatus();
    }

    @Override
    public void onEventReceived(String event) {
        RLog.i(RLog.EVENT_LISTENERS, "SignInAccountFragment :onEventReceived is : " + event);
        if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
            mMobileSignInAccountFragment.updateUiStatus();
        }
    }
    @Override
    public void onUpadte() {
          mMobileSignInAccountFragment.onUIUpadte();
    }
    @Override
    public void onLoginFailedWithError(final UserRegistrationFailureInfo userRegistrationFailureInfo) {

                mMobileSignInAccountFragment.onLoginFailedWithErrorUI(userRegistrationFailureInfo);
    }
    @Override
    public void onLoginSuccess() {

        mMobileSignInAccountFragment.onLoginSuccessUI();

    }
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_sign_in) {
            RLog.d(RLog.ONCLICK, "SignInAccountFragment : SignIn");
            mMobileSignInAccountFragment.hideValidations();
            mMobileSignInAccountFragment.signIn();
        } else if (id == R.id.btn_reg_forgot_password) {
//            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Forgot Password");
//            hideValidations();
//            mPhoneNumber.clearFocus();
//            mEtPassword.clearFocus();
//            if (mPhoneNumber.getPhoneNumber().length() == 0) {
//                launchResetPasswordFragment();
//            } else {
//                resetPassword();
//            }
        } else if (id == R.id.btn_reg_resend) {
//            RLog.d(RLog.ONCLICK, "SignInAccountFragment : Resend");
//            mPhoneNumber.clearFocus();
//            mEtPassword.clearFocus();
//            RLog.d(RLog.ONCLICK, "AccountActivationFragment : Resend");
//            handleResend();
        } else if(id == R.id.btn_reg_login_using_mail){
           // trackPage(AppTaggingPages.SIGN_IN_ACCOUNT);
            mMobileSignInAccountFragment.getRegistrationFragment().addFragment(new SignInAccountFragment());

        }
    }
}

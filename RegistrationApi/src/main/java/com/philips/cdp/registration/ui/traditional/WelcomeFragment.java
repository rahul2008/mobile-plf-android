
package com.philips.cdp.registration.ui.traditional;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.cdp.registration.AppTagging.AppTaggingPages;
import com.philips.cdp.registration.AppTagging.AppTagingConstants;
import com.philips.cdp.registration.R;
import com.philips.cdp.registration.User;
import com.philips.cdp.registration.coppa.CoppaExtension;
import com.philips.cdp.registration.coppa.CoppaResendError;
import com.philips.cdp.registration.coppa.ResendCoppaEmailConsentHandler;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.events.NetworStateListener;
import com.philips.cdp.registration.handlers.UpdateReceiveMarketingEmailHandler;
import com.philips.cdp.registration.settings.RegistrationHelper;
import com.philips.cdp.registration.ui.customviews.XRegError;
import com.philips.cdp.registration.ui.utils.FontLoader;
import com.philips.cdp.registration.ui.utils.NetworkUtility;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.cdp.registration.ui.utils.RegConstants;
import com.philips.cdp.registration.ui.utils.RegUtility;

public class WelcomeFragment extends RegistrationBaseFragment implements OnClickListener,
        UpdateReceiveMarketingEmailHandler, OnCheckedChangeListener, NetworStateListener {

    private TextView mTvWelcome;

    private TextView mTvSignInEmail;

    private CheckBox mCbTerms;

    private LinearLayout mLlContinueBtnContainer;

    private User mUser;

    private Context mContext;

    private LinearLayout mLlEmailDetails;

    private boolean isfromVerification;

    private boolean isfromBegining;

    private Button mBtnSignOut;

    private Button mBtnContinue;

    private XRegError mRegError;

    private ProgressBar mPbWelcomeCheck;

    private DIUserProfile userProfile;

    @Override
    public void onAttach(Activity activity) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onAttach");
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onCreate");
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onCreateView");
        RegistrationHelper.getInstance().registerNetworkStateListener(this);
        Bundle bundle = getArguments();
        if (null != bundle) {
            isfromVerification = bundle.getBoolean(RegConstants.VERIFICATIN_SUCCESS);
            isfromBegining = bundle.getBoolean(RegConstants.IS_FROM_BEGINING);
        }

        View view = inflater.inflate(R.layout.fragment_welcome, null);
        mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
        mUser = new User(mContext);
        init(view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onActivityCreated");
    }

    @Override
    public void onStart() {
        super.onStart();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDestroyView");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDestroy");
        RegistrationHelper.getInstance().unRegisterNetworkListener(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        RLog.d(RLog.FRAGMENT_LIFECYCLE, " WelcomeFragment : onDetach");
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserWelcomeFragment : onConfigurationChanged");
        setViewParams(config);
    }

    @Override
    public void setViewParams(Configuration config) {
        applyParams(config, mTvWelcome);
        applyParams(config, mLlEmailDetails);
        applyParams(config, mLlContinueBtnContainer);
        applyParams(config, mCbTerms);
        applyParams(config, mRegError);
        applyParams(config, mTvSignInEmail);
    }

    private void init(View view) {
        consumeTouch(view);
        mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
        mLlContinueBtnContainer = (LinearLayout) view.findViewById(R.id.ll_reg_continue_id);
        mCbTerms = (CheckBox) view.findViewById(R.id.cb_reg_register_terms);
        FontLoader.getInstance().setTypeface(mCbTerms, "CentraleSans-Light.otf");
        mCbTerms.setPadding(RegUtility.getCheckBoxPadding(mContext), mCbTerms.getPaddingTop(), mCbTerms.getPaddingRight(), mCbTerms.getPaddingBottom());
        mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);
        mPbWelcomeCheck = (ProgressBar) view.findViewById(R.id.pb_reg_welcome_spinner);
        mLlEmailDetails = (LinearLayout) view.findViewById(R.id.ll_reg_email_details_container);
        mTvSignInEmail = (TextView) view.findViewById(R.id.tv_reg_sign_in_using);
        setViewParams(getResources().getConfiguration());
        mBtnSignOut = (Button) view.findViewById(R.id.btn_reg_sign_out);
        mBtnSignOut.setOnClickListener(this);
        mBtnContinue = (Button) view.findViewById(R.id.btn_reg_continue);
        mBtnContinue.setOnClickListener(this);

        userProfile = mUser.getUserInstance(mContext);
        if (isfromVerification) {
            mCbTerms.setVisibility(view.GONE);
            mLlEmailDetails.setVisibility(View.GONE);
        }

        if (isfromBegining) {
            mBtnSignOut.setVisibility(View.GONE);
            mLlEmailDetails.setVisibility(View.GONE);
            mBtnContinue.setText(getResources().getString(R.string.SignInSuccess_SignOut_btntxt));
            mCbTerms.setVisibility(view.VISIBLE);
            mCbTerms.setChecked(mUser.getUserInstance(mContext).getReceiveMarketingEmail());
            mCbTerms.setOnCheckedChangeListener(this);

        } else {
            View regLineView = (View) view.findViewById(R.id.reg_view_line);
            regLineView.setVisibility(View.GONE);

        }

        mTvWelcome.setText(getString(R.string.RegWelcomeText) + " " + userProfile.getGivenName());

        String email = getString(R.string.InitialSignedIn_SigninEmailText);
        email = String.format(email, userProfile.getEmail());
        mTvSignInEmail.setText(email);

        Button btnFetchConsent = (Button) view.findViewById(R.id.btn_resend_consent);

        if (RegistrationHelper.getInstance().isCoppaFlow()) {
            btnFetchConsent.setVisibility(View.GONE);
            btnFetchConsent.setOnClickListener(this);
        } else {
            btnFetchConsent.setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_reg_sign_out) {
            RLog.d(RLog.ONCLICK, "WelcomeFragment : Sign Out");
            handleLogout();
        } else if (id == R.id.btn_reg_continue) {
            if (isfromBegining) {
                RLog.d(RLog.ONCLICK, "WelcomeFragment : Continue Sign out");
                handleLogout();
            } else {
                RLog.d(RLog.ONCLICK, " WelcomeFragment : Continue");
                RegistrationHelper.getInstance().getUserRegistrationListener()
                        .notifyonUserRegistrationCompleteEventOccurred(getRegistrationFragment().getParentActivity());
            }
        } else if (id == R.id.btn_resend_consent) {
            resendCoppaMail();
        }
    }

    private void handleLogout() {
        trackPage(AppTaggingPages.HOME);
        trackActionStatus(AppTagingConstants.SEND_DATA, AppTagingConstants.SPECIAL_EVENTS,
                AppTagingConstants.SIGN_OUT);
        mUser.logout();
        getRegistrationFragment().replaceWithHomeFragment();
    }

    private void resendCoppaMail() {
        CoppaExtension coppaExtension = new CoppaExtension();
        coppaExtension.resendCoppaEmailConsentForUserEmail(userProfile.getEmail(),
                new ResendCoppaEmailConsentHandler() {

                    @Override
                    public void didResendCoppaEmailConsentSucess() {

                    }

                    @Override
                    public void didResendCoppaEmailConsentFailedWithError(
                            CoppaResendError coppaResendError) {

                    }
                });

    }

    @Override
    public void onCheckedChanged(
            CompoundButton buttonView, boolean isChecked) {
        handleUpdate();
    }

    private void handleUpdate() {
        if (NetworkUtility.isNetworkAvailable(mContext)) {
            mRegError.hideError();
            showProgressBar();
            updateUser();
        } else {
            mCbTerms.setOnCheckedChangeListener(null);
            mCbTerms.setChecked(!mCbTerms.isChecked());
            mCbTerms.setOnCheckedChangeListener(this);
            mRegError.setError(getString(R.string.NoNetworkConnection));
            trackActionRegisterError(AppTagingConstants.NETWORK_ERROR_CODE);
        }
    }

    private void showProgressBar() {
        mPbWelcomeCheck.setVisibility(View.VISIBLE);
        mCbTerms.setEnabled(false);
        mBtnContinue.setEnabled(false);
    }

    private void updateUser() {
        mUser.updateReceiveMarketingEmail(this, mCbTerms.isChecked());
    }

    @Override
    public void onUpdateReceiveMarketingEmailSuccess() {
        hideProgressBar();
        if (mCbTerms.isChecked()) {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
        } else {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
        }
    }

    private void hideProgressBar() {
        mPbWelcomeCheck.setVisibility(View.GONE);
        mCbTerms.setEnabled(true);
        mBtnContinue.setEnabled(true);
    }

    @Override
    public void onUpdateReceiveMarketingEmailFailedWithError(int error) {
        hideProgressBar();
        mCbTerms.setOnCheckedChangeListener(null);
        mCbTerms.setChecked(!mCbTerms.isChecked());
        mCbTerms.setOnCheckedChangeListener(this);
    }

    @Override
    public void onNetWorkStateReceived(boolean isOnline) {
        if (isOnline) {
            mRegError.hideError();
        }
    }

    @Override
    public int getTitleResourceId() {
        if (isfromBegining) {
            return R.string.RegBaseViewNav_TitleTxt;
        }
        return R.string.SigIn_TitleTxt;
    }

}

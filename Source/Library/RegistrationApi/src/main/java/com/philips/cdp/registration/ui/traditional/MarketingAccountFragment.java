/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.traditional;

import android.app.*;
import android.content.*;
import android.content.res.Configuration;

import android.os.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.Button;
import android.widget.*;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.*;
import com.philips.cdp.registration.app.tagging.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.settings.*;
import com.philips.cdp.registration.ui.customviews.*;
import com.philips.cdp.registration.ui.traditional.mobile.*;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.uid.view.widget.*;

import butterknife.*;


public class MarketingAccountFragment extends RegistrationBaseFragment implements
        View.OnClickListener, MarketingAccountContract {

    @BindView(R2.id.usr_marketingScreen_countMe_button)
    ProgressBarButton countMeButton;

    @BindView(R2.id.usr_marketingScreen_maybeLater_button)
    Button mayBeLaterButton;

    @BindView(R2.id.usr_marketingScreen_rootLayout_scrollView)
    ScrollView rootLayoutScrollView;

    @BindView(R2.id.usr_marketingScreen_error_regerror)
    XRegError errorRegError;

    @BindView(R2.id.usr_marketingScreen_philipsNews_label)
    Label receivePhilipsNewsLabel;

    @BindView(R2.id.usr_marketingScreen_rootContainer_linearLayout)
    LinearLayout usr_marketingScreen_rootContainer_linearLayout;



    private ProgressDialog mProgressDialog;

    private User mUser;

    private Context mContext;

    private Bundle mBundle;

    private long mTrackCreateAccountTime;

    MarketingAccountPresenter marketingAccountPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        URInterface.getComponent().inject(this);

        View view = inflater.inflate(R.layout.reg_fragment_marketing_opt, container, false);

        marketingAccountPresenter = new MarketingAccountPresenter(this);
        marketingAccountPresenter.register();

        ButterKnife.bind(this, view);
        initUI(view);
        setContentConfig(view);
        handleOrientation(view);
        mTrackCreateAccountTime = System.currentTimeMillis();
        return view;
    }

    private void setContentConfig(View view) {
        if (getRegistrationFragment().getContentConfiguration() != null) {
            updateText(view, R.id.usr_marketingScreen_headTitle_Lable,
                    getRegistrationFragment().getContentConfiguration().getOptInQuessionaryText());
            updateText(view, R.id.usr_marketingScreen_specialOffer_label,
                    getRegistrationFragment().getContentConfiguration().getOptInDetailDescription());
            if (getRegistrationFragment().getContentConfiguration().getOptInBannerText() != null) {
                updateText(view, R.id.usr_marketingScreen_joinNow_Label,
                        getRegistrationFragment().getContentConfiguration().getOptInBannerText());
            } else {
                defalutBannerText(view);
            }
        } else {
            defalutBannerText(view);
        }
    }

    void defalutBannerText(View view) {
        String joinNow = mContext.getResources().getString(R.string.reg_Opt_In_Join_Now);
        String updateJoinNowText = " " + "<b>" + mContext.getResources().getString(R.string.reg_Opt_In_Over_Peers) + "</b> ";
        joinNow = String.format(joinNow, updateJoinNowText);
        updateText(view, R.id.usr_marketingScreen_joinNow_Label, joinNow);
    }

    private void updateText(View view, int textViewId, String text) {
        TextView marketBeTheFirstTextView = (TextView) view.findViewById(textViewId);
        if (text != null && text.length() > 0) {
            marketBeTheFirstTextView.setText(Html.fromHtml(text));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onActivityCreated");
    }

    @Override
    public void onDestroy() {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onDestroy");
        marketingAccountPresenter.unRegister();
        RLog.d(RLog.EVENT_LISTENERS,
                "CreateAccountFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS");
        super.onDestroy();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        mBundle = outState;
        super.onSaveInstanceState(mBundle);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mBundle = null;
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        RLog.d(RLog.FRAGMENT_LIFECYCLE, "CreateAccountFragment : onConfigurationChanged");
        super.onConfigurationChanged(config);
        setCustomParams(config);
    }

    @Override
    public void setViewParams(Configuration config, int width) {
        applyParams(config,usr_marketingScreen_rootContainer_linearLayout,width);
    }

    @Override
    protected void handleOrientation(View view) {
        handleOrientationOnView(view);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.usr_marketingScreen_countMe_button) {
            showRefreshProgress();
            marketingAccountPresenter.updateMarketingEmail(mUser, true);
        } else if (v.getId() == R.id.usr_marketingScreen_maybeLater_button) {
            showRefreshProgress();
            marketingAccountPresenter.updateMarketingEmail(mUser, false);
        }
    }

    private void initUI(View view) {
        consumeTouch(view);
        RegUtility.linkifyPhilipsNewsMarketing(receivePhilipsNewsLabel,
                getRegistrationFragment().getParentActivity(), mPhilipsNewsClick);
        countMeButton.setOnClickListener(this);
        mayBeLaterButton.setOnClickListener(this);
        handleUiState();
        mUser = new User(mContext);
    }

    private ClickableSpan mPhilipsNewsClick = new ClickableSpan() {
        @Override
        public void onClick(View widget) {
            getRegistrationFragment().addPhilipsNewsFragment();
            trackPage(AppTaggingPages.PHILIPS_ANNOUNCEMENT);
        }
    };

    @Override
    public void handleRegistrationSuccess() {
        RLog.i(RLog.CALLBACK, "CreateAccountFragment : onRegisterSuccess");
        hideRefreshProgress();
        if (RegistrationConfiguration.getInstance().isEmailVerificationRequired() && !(mUser.isEmailVerified() || mUser.isMobileVerified())) {
            if (FieldsValidator.isValidEmail(mUser.getEmail().toString())) {
                launchAccountActivateFragment();
            } else {
                launchMobileVerifyCodeFragment();
            }
        } else if (RegistrationConfiguration.getInstance().isEmailVerificationRequired() && (mUser.isEmailVerified() || mUser.isMobileVerified())) {
            getRegistrationFragment().userRegistrationComplete();
        } else {
            getRegistrationFragment().userRegistrationComplete();
        }
        if (mTrackCreateAccountTime == 0 && RegUtility.getCreateAccountStartTime() > 0) {
            mTrackCreateAccountTime = (System.currentTimeMillis() - RegUtility.
                    getCreateAccountStartTime()) / 1000;
        } else {
            mTrackCreateAccountTime = (System.currentTimeMillis() - mTrackCreateAccountTime) / 1000;
        }

        mTrackCreateAccountTime = 0;
    }

    private void launchAccountActivateFragment() {
        getRegistrationFragment().addFragment(new AccountActivationFragment());
        trackPage(AppTaggingPages.ACCOUNT_ACTIVATION);
    }

    private void launchMobileVerifyCodeFragment() {
        getRegistrationFragment().addFragment(new MobileVerifyCodeFragment());
        trackPage(AppTaggingPages.MOBILE_VERIFY_CODE);
    }

    @Override
    public int getTitleResourceId() {
        return R.string.reg_Opt_In_Be_The_First;
    }

    @Override
    public String getTitleResourceText() {
        String titleResourceText = null;
        if (getRegistrationFragment().getContentConfiguration() != null) {
            titleResourceText = getRegistrationFragment().getContentConfiguration().getOptInActionBarText();
        }
        return titleResourceText;
    }

    @Override
    public void trackRemarketing() {
        if (mUser.getReceiveMarketingEmail()) {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_IN);
        } else {
            trackActionForRemarkettingOption(AppTagingConstants.REMARKETING_OPTION_OUT);
        }
    }

    private void showRefreshProgress() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity(), R.style.reg_Custom_loaderTheme);
            mProgressDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Large);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        } else {
            mProgressDialog.show();
        }
    }

    @Override
    public void hideRefreshProgress() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void handleUiState() {
        if (new NetworkUtility(mContext).isNetworkAvailable()) {
            if (UserRegistrationInitializer.getInstance().isJanrainIntialized()) {
                errorRegError.hideError();
                countMeButton.setEnabled(true);
                mayBeLaterButton.setEnabled(true);
            } else {
                countMeButton.setEnabled(false);
                mayBeLaterButton.setEnabled(false);
                errorRegError.setError(getString(R.string.reg_NoNetworkConnection));
            }
        } else {
            errorRegError.setError(getString(R.string.reg_NoNetworkConnection));
            countMeButton.setEnabled(false);
            mayBeLaterButton.setEnabled(false);
            scrollViewAutomatically(errorRegError, rootLayoutScrollView);
        }
    }
}



package com.philips.cl.di.reg.ui.traditional;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.User;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsConstants;
import com.philips.cl.di.reg.adobe.analytics.AnalyticsPages;
import com.philips.cl.di.reg.configuration.RegistrationConfiguration;
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.events.NetworStateListener;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XProviderButton;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class HomeFragment extends RegistrationBaseFragment implements OnClickListener,
        NetworStateListener, SocialProviderLoginHandler, EventListener {

	private Button mBtnCreateAccount;

	private XProviderButton mBtnMyPhilips;

	private TextView mTvWelcome;

	private TextView mTvWelcomeDesc;

	private LinearLayout mLlCreateBtnContainer;

	private LinearLayout mLlLoginBtnContainer;

	private LinearLayout mLlSocialProviderBtnContainer;

	private XRegError mRegError;

	private User mUser;

	private String mProvider;

	private ProgressBar mPbJanrainInit;

	private Context mContext;

	@Override
	public void onAttach(Activity activity) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onAttach");
		super.onAttach(activity);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onCreate");
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onCreateView");
		mContext = getRegistrationFragment().getParentActivity().getApplicationContext();
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
		EventHelper.getInstance().registerEventNotification(RegConstants.PARSING_COMPLETED, this);
		RegistrationHelper.getInstance().registerNetworkStateListener(this);
		RLog.i(RLog.EVENT_LISTENERS,
		        "HomeFragment register: NetworStateListener,JANRAIN_INIT_SUCCESS,JANRAIN_INIT_FAILURE,PARSING_COMPLETED");
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		initUI(view);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onActivityCreated");
	}

	@Override
	public void onStart() {
		super.onStart();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onStart");
	}

	@Override
	public void onResume() {
		super.onResume();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onResume");
	}

	@Override
	public void onPause() {
		super.onPause();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onPause");
	}

	@Override
	public void onStop() {
		super.onStop();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onStop");
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDestroyView");
	}

	@Override
	public void onDestroy() {
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDestroy");
		RegistrationHelper.getInstance().unRegisterNetworkListener(this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.PARSING_COMPLETED, this);
		RLog.i(RLog.EVENT_LISTENERS,
		        "HomeFragment unregister: NetworStateListener,JANRAIN_INIT_SUCCESS,JANRAIN_INIT_FAILURE,PARSING_COMPLETED");
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		super.onDetach();
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onDetach");
	}

	private void handleSocialProviders(final String countryCode) {
		RLog.d("HomeFragment : ", "handleSocialProviders method country code : " + countryCode);
		if (null != RegistrationConfiguration.getInstance().getSocialProviders()) {
			mLlSocialProviderBtnContainer.post(new Runnable() {

				@Override
				public void run() {
					mLlSocialProviderBtnContainer.removeAllViews();
					ArrayList<String> providers = new ArrayList<String>();
					providers = RegistrationConfiguration.getInstance().getSocialProviders()
					        .getSocialProvidersForCountry(countryCode);
					if (null != providers) {
						for (int i = 0; i < providers.size(); i++) {
							inflateEachProviderBtn(providers.get(i));
						}
						RLog.d("HomeFragment", "social providers : " + providers);
					}
				}
			});
		}
	}

	private void inflateEachProviderBtn(String provider) {
		String providerTempName = provider.substring(0, 1).toUpperCase()
		        + provider.substring(1).toLowerCase();
		String providerName = "Welcome_" + providerTempName + "_btntxt";
		String providerDrawable = "reg_" + provider + "_ic";

		int resourceId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(providerName, "string",
		        getRegistrationFragment().getParentActivity().getPackageName());
		int drawableId = getRegistrationFragment().getParentActivity().getResources().getIdentifier(providerDrawable, "drawable",
		        getRegistrationFragment().getParentActivity().getPackageName());
		mLlSocialProviderBtnContainer.addView(getProviderBtn(provider, resourceId, drawableId));
	}

	private XProviderButton getProviderBtn(final String providerName, int providerNameStringId,
	        int providerLogoDrawableId) {
		final XProviderButton providerBtn = new XProviderButton(mContext);
		providerBtn.setProviderName(providerNameStringId);
		providerBtn.setProviderLogoID(providerLogoDrawableId);
		providerBtn.setTag(providerName);

		providerBtn.setEnabled(true);
		if (NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			providerBtn.setEnabled(true);
		} else {
			providerBtn.setEnabled(false);
		}
		providerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				RLog.d(RLog.ONCLICK, "HomeFragment : " + providerName);
				callSocialProvider(providerName);
				providerBtn.showProgressBar();
			}
		});
		return providerBtn;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "HomeFragment : onConfigurationChanged");
		setViewParams(config);
	}

	private void initUI(View view) {
		consumeTouch(view);
		mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
		mTvWelcomeDesc = (TextView) view.findViewById(R.id.tv_reg_welcome_desc);
		mLlCreateBtnContainer = (LinearLayout) view
		        .findViewById(R.id.ll_reg_create_account_container);
		mLlLoginBtnContainer = (LinearLayout) view.findViewById(R.id.rl_reg_singin_options);
		mBtnCreateAccount = (Button) view.findViewById(R.id.btn_reg_create_account);
		mBtnCreateAccount.setOnClickListener(this);
		mBtnMyPhilips = (XProviderButton) view.findViewById(R.id.btn_reg_my_philips);
		mBtnMyPhilips.setOnClickListener(this);

		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

		mPbJanrainInit = (ProgressBar) view.findViewById(R.id.pb_reg_janrain_init);
		mPbJanrainInit.setClickable(false);
		mPbJanrainInit.setEnabled(false);
		mLlSocialProviderBtnContainer = (LinearLayout) view
		        .findViewById(R.id.ll_reg_social_provider_container);

		handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());
		mUser = new User(mContext);
		setViewParams(getResources().getConfiguration());
		linkifyTermAndPolicy(mTvWelcomeDesc);
		handleJanrainInitPb();
		enableControls(false);
		handleUiState();
	}

	private void handleJanrainInitPb() {
		if (NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mPbJanrainInit.setVisibility(View.GONE);
		} else if (NetworkUtility.isNetworkAvailable(mContext)
		        && !RegistrationHelper.getInstance().isJanrainIntialized()) {
			mPbJanrainInit.setVisibility(View.VISIBLE);
		} else {
			mPbJanrainInit.setVisibility(View.GONE);
		}
	}

	@Override
	public void onClick(View v) {
		/**
		 * Library does not include resource constants after ADT 14 Link
		 * :http://tools.android.com/tips/non-constant-fields
		 */
		if (v.getId() == R.id.btn_reg_create_account) {
			RLog.d(RLog.ONCLICK, "HomeFragment : Create Account");
			launchCreateAccountFragment();

		} else if (v.getId() == R.id.btn_reg_my_philips) {
			RLog.d(RLog.ONCLICK, "HomeFragment : My Philips");
			trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.LOGIN_CHANNEL,
			        AnalyticsConstants.MY_PHILIPS);
			launchSignInFragment();
		}
	}

	private void launchSignInFragment() {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.SIGN_IN_ACCOUNT);
		getRegistrationFragment().addFragment(new SignInAccountFragment());
	}

	private void launchCreateAccountFragment() {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.CREATE_ACCOUNT);
		getRegistrationFragment().addFragment(new CreateAccountFragment());
	}

	private void callSocialProvider(String providerName) {
		RLog.d("HomeFragment", "callSocialProvider method provider name :" + providerName);
		mProvider = providerName;
		if (null == mUser)
			return;
		if (NetworkUtility.isNetworkAvailable(mContext)
		        && RegistrationHelper.getInstance().isJanrainIntialized()) {
			trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.LOGIN_CHANNEL,
			        providerName);
			mUser.loginUserUsingSocialProvider(getActivity(), providerName, this, null);
		}
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mTvWelcome);
		applyParams(config, mTvWelcomeDesc);
		applyParams(config, mLlCreateBtnContainer);
		applyParams(config, mLlLoginBtnContainer);
	}

	@Override
	public int getTitleResourceId() {
		return R.string.SigIn_TitleTxt;
	}

	@Override
	public void onEventReceived(String event) {
		RLog.i(RLog.EVENT_LISTENERS, "HomeFragment :onEventReceived is : " + event);
		if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			enableControls(true);
			handleJanrainInitPb();
		} else if (RegConstants.JANRAIN_INIT_FAILURE.equals(event)) {
			enableControls(false);
			handleJanrainInitPb();
		} else if (RegConstants.PARSING_COMPLETED.equals(event)) {
			handleSocialProvider();
		}
	}

	private void handleSocialProvider() {
		RegistrationConfiguration.getInstance().getSocialProviders();
		handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());
	}

	private void handleUiState() {
		if (NetworkUtility.isNetworkAvailable(mContext)) {
			if (RegistrationHelper.getInstance().isJanrainIntialized()) {
				mRegError.hideError();
				enableControls(true);
			} else {
				mRegError.hideError();
			}
		} else {
			mRegError.setError(mContext.getResources().getString(R.string.NoNetworkConnection));
			enableControls(false);
			trackActionLoginError(AnalyticsConstants.NETWORK_ERROR_CODE);
		}
	}

	private void enableControls(boolean state) {
		if (state && NetworkUtility.isNetworkAvailable(mContext)) {
			handleBtnClickableStates(state);
			setAlphaForView(mBtnMyPhilips, 1);
			setAlphaForView(mLlSocialProviderBtnContainer, 1);
			mRegError.hideError();
		} else {
			handleBtnClickableStates(state);
			setAlphaForView(mBtnMyPhilips, 0.75f);
			setAlphaForView(mLlSocialProviderBtnContainer, 0.75f);
		}
	}

	private void handleBtnClickableStates(boolean state) {
		mBtnCreateAccount.setEnabled(state);
		mBtnMyPhilips.setEnabled(state);
		enableSocialProviders(state);
	}

	private void enableSocialProviders(boolean enableState) {
		for (int i = 0; i < mLlSocialProviderBtnContainer.getChildCount(); i++) {
			mLlSocialProviderBtnContainer.getChildAt(i).setEnabled(enableState);
		}
	}

	private void linkifyTermAndPolicy(TextView pTvPrivacyPolicy) {
		String termAndPrivacy = getResources().getString(R.string.LegalNoticeText);
		String terms = getResources().getString(R.string.TermsAndConditionsText);
		String privacy = getResources().getString(R.string.PrivacyPolicyText);
		int termStartIndex = termAndPrivacy.toLowerCase(Locale.getDefault()).indexOf(
		        terms.toLowerCase(Locale.getDefault()));
		int privacyStartIndex = termAndPrivacy.toLowerCase(Locale.getDefault()).indexOf(
		        privacy.toLowerCase(Locale.getDefault()));

		SpannableString spanableString = new SpannableString(termAndPrivacy);
		spanableString.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				handleTermsCondition();
			}
		}, termStartIndex, termStartIndex + terms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		spanableString.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				handlePrivacyPolicy();
			}

		}, privacyStartIndex, privacyStartIndex + privacy.length(),
		        Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

		removeUnderlineFromLink(spanableString);

		pTvPrivacyPolicy.setText(spanableString);
		pTvPrivacyPolicy.setMovementMethod(LinkMovementMethod.getInstance());
		pTvPrivacyPolicy.setLinkTextColor(getResources().getColor(
		        R.color.reg_hyperlink_highlight_color));
		pTvPrivacyPolicy.setHighlightColor(getResources().getColor(android.R.color.transparent));
	}

	private void removeUnderlineFromLink(SpannableString spanableString) {
		for (ClickableSpan u : spanableString.getSpans(0, spanableString.length(),
		        ClickableSpan.class)) {
			spanableString.setSpan(new UnderlineSpan() {

				public void updateDrawState(TextPaint tp) {
					tp.setUnderlineText(false);
				}
			}, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
		}

		for (URLSpan u : spanableString.getSpans(0, spanableString.length(), URLSpan.class)) {
			spanableString.setSpan(new UnderlineSpan() {

				public void updateDrawState(TextPaint tp) {
					tp.setUnderlineText(false);
				}
			}, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
		}
	}

	private void setAlphaForView(View view, float alpha) {
		AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
		animation.setDuration(0);
		animation.setFillAfter(true);
		view.startAnimation(animation);
	}

	private void handlePrivacyPolicy() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(
		        R.string.PrivacyPolicyURL)));
		startActivity(browserIntent);
	}

	private void handleTermsCondition() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(
		        R.string.PrivacyPolicyURL)));
		startActivity(browserIntent);
	}

	@Override
	public void onLoginSuccess() {
		trackActionStatus(AnalyticsConstants.SEND_DATA, AnalyticsConstants.SPECIAL_EVENTS,
		        AnalyticsConstants.SUCCESS_LOGIN);
		RLog.i(RLog.CALLBACK, "HomeFragment : onLoginSuccess");
		hideProviderProgress();
		enableControls(true);
		User user = new User(mContext);
		if (user.getEmailVerificationStatus(mContext)) {
			launchWelcomeFragment();
		} else {
			launchAccountActivationFragment();
		}
	}

	private void launchAccountActivationFragment() {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.ACCOUNT_ACTIVATION);
		getRegistrationFragment().addFragment(new AccountActivationFragment());
	}

	private void launchWelcomeFragment() {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.WELCOME);
		getRegistrationFragment().addWelcomeFragmentOnVerification();
	}

	private void hideProviderProgress() {
		if (null != getView().findViewWithTag(mProvider)) {
			XProviderButton providerButton = (XProviderButton) getView().findViewWithTag(mProvider);
			providerButton.hideProgressBar();
		}
	}

	private void showProviderProgress() {
		if (null != getView().findViewWithTag(mProvider)) {
			XProviderButton providerButton = (XProviderButton) getView().findViewWithTag(mProvider);
			providerButton.showProgressBar();
		}
	}

	@Override
	public void onLoginFailedWithError(UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithError");
		hideProviderProgress();
		enableControls(true);
		if (null != userRegistrationFailureInfo && null != userRegistrationFailureInfo.getError()) {
			trackActionLoginError(userRegistrationFailureInfo.getError().code);
		}
	}

	@Override
	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
	        String socialRegistrationToken) {
		RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithTwoStepError");
		hideProviderProgress();
		enableControls(true);
		RLog.i("HomeFragment", "Login failed with two step error" + "JSON OBJECT :"
		        + prefilledRecord);
		launchAlmostDoneFragment(prefilledRecord, socialRegistrationToken);
	}

	private void launchAlmostDoneFragment(JSONObject prefilledRecord, String socialRegistrationToken) {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.ALMOST_DONE);
		getRegistrationFragment().addAlmostDoneFragment(prefilledRecord, mProvider,
		        socialRegistrationToken);
	}

	@Override
	public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
	        String conflictingIdentityProvider, String conflictingIdpNameLocalized,
	        String existingIdpNameLocalized) {
		RLog.i(RLog.CALLBACK, "HomeFragment : onLoginFailedWithMergeFlowError");
		hideProviderProgress();
		enableControls(true);
		if (mUser.handleMergeFlowError(existingProvider)) {
			launchMergeAccountFragment(mergeToken, existingProvider);
		} else {
			if (NetworkUtility.isNetworkAvailable(mContext)
			        && RegistrationHelper.getInstance().isJanrainIntialized()) {
				mProvider = existingProvider;
				showProviderProgress();
				mUser.loginUserUsingSocialProvider(getActivity(), existingProvider, this,
				        mergeToken);
			}

		}
	}

	private void launchMergeAccountFragment(String mergeToken, String existingProvider) {
		trackPage(AnalyticsPages.HOME, AnalyticsPages.MERGE_ACCOUNT);
		getRegistrationFragment().addMergeAccountFragment(mergeToken, existingProvider);
	}

	@Override
	public void onContinueSocialProviderLoginSuccess() {
		RLog.i(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginSuccess");
		hideProviderProgress();
		enableControls(true);
		launchWelcomeFragment();
	}

	@Override
	public void onContinueSocialProviderLoginFailure(
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		RLog.i(RLog.CALLBACK, "HomeFragment : onContinueSocialProviderLoginFailure");
		hideProviderProgress();
		enableControls(true);
		if (null != userRegistrationFailureInfo && null != userRegistrationFailureInfo.getError()) {
			trackActionLoginError(userRegistrationFailureInfo.getError().code);
		}

	}

	@Override
	public void onNetWorkStateReceived(boolean isOnline) {
		RLog.i(RLog.NETWORK_STATE, "HomeFragment :onNetWorkStateReceived state :" + isOnline);
		handleUiState();
		handleJanrainInitPb();
	}
}

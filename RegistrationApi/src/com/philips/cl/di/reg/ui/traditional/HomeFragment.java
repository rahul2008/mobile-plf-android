
package com.philips.cl.di.reg.ui.traditional;

import java.util.ArrayList;
import java.util.Locale;

import org.json.JSONObject;

import android.annotation.SuppressLint;
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
import com.philips.cl.di.reg.dao.UserRegistrationFailureInfo;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.handlers.SocialProviderLoginHandler;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XProviderButton;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class HomeFragment extends RegistrationBaseFragment implements OnClickListener,
        EventListener, SocialProviderLoginHandler {

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

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		EventHelper.getInstance().registerEventNotification(RegConstants.IS_ONLINE, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance()
		        .registerEventNotification(RegConstants.JANRAIN_INIT_FAILURE, this);
		EventHelper.getInstance().registerEventNotification(RegConstants.PARSING_COMPLETED, this);
		mUser = new User(getRegistrationMainActivity().getApplicationContext());
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		initUI(view);

		return view;
	}

	private void handleSocialProviders(String countryCode) {
		if (null != RegistrationHelper.getInstance().getSocialProviders()) {
			ArrayList<String> providers = new ArrayList<String>();
			providers = RegistrationHelper.getInstance().getSocialProviders()
			        .getSocialProvidersForCountry(countryCode);
			
			if (null != providers) {
				new Thread(){
					public void run() {
						mLlSocialProviderBtnContainer.removeAllViews();
					};
				};
				
				for (int i = 0; i < providers.size(); i++) {
					inflateEachProviderButton(providers.get(i));
				}
			}
		}
	}

	@SuppressLint("ResourceAsColor")
	private void inflateEachProviderButton(String provider) {
		if (SocialProvider.FACEBOOK.equals(provider)) {
			mLlSocialProviderBtnContainer.addView(getProviderBtn(provider,
			        R.string.Welcome_Facebook_btntxt, R.drawable.reg_facebook_ic,
			        R.drawable.reg_facebook_bg_rect, R.color.reg_btn_text_enable_color));

		} else if (SocialProvider.TWITTER.equals(provider)) {
			mLlSocialProviderBtnContainer.addView(getProviderBtn(provider,
			        R.string.Welcome_Twitter_btntxt, R.drawable.reg_twitter_ic,
			        R.drawable.reg_twitter_bg_rect, R.color.reg_btn_text_enable_color));
		} else if (SocialProvider.GOOGLE_PLUS.equals(provider)) {
			mLlSocialProviderBtnContainer.addView(getProviderBtn(provider,
			        R.string.GooglePlus_btntxt, R.drawable.reg_google_plus_ic,
			        R.drawable.reg_google_plus_bg_rect, R.color.reg_btn_text_enable_color));
		}
	}

	private XProviderButton getProviderBtn(final String providerName, int providerNameStringId,
	        int providerLogoDrawableId, int providerBgDrawableId, int providerTextColorId) {
		final XProviderButton providerBtn = new XProviderButton(getRegistrationMainActivity()
		        .getApplicationContext());
		providerBtn.setProviderName(providerNameStringId);
		providerBtn.setProviderLogoID(providerLogoDrawableId);
		providerBtn.setProviderBackgroundID(providerBgDrawableId);
		providerBtn.setProviderTextColor(providerTextColorId);
		providerBtn.setTag(providerName);
		providerBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				callSocialProvider(providerName);
				providerBtn.showProgressBar();
			}
		});
		return providerBtn;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void onDestroy() {
		EventHelper.getInstance().unregisterEventNotification(RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_SUCCESS,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.JANRAIN_INIT_FAILURE,
		        this);
		EventHelper.getInstance().unregisterEventNotification(RegConstants.PARSING_COMPLETED, this);
		super.onDestroy();
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

		setViewParams(getResources().getConfiguration());
		linkifyTermAndPolicy(mTvWelcomeDesc);
		handleJanrainInitPb();
		enableControls(false);
		handleUiState();
	}

	private void handleJanrainInitPb() {
		if (NetworkUtility.getInstance().isOnline() && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mPbJanrainInit.setVisibility(View.GONE);
		} else if (NetworkUtility.getInstance().isOnline()
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
			getRegistrationMainActivity().addFragment(new CreateAccountFragment());
		} else if (v.getId() == R.id.btn_reg_my_philips) {
			getRegistrationMainActivity().addFragment(new SignInAccountFragment());
		}
	}

	private void callSocialProvider(String providerName) {
		mProvider = providerName;
		if (null == mUser)
			return;
		if (NetworkUtility.getInstance().isOnline() && RegistrationHelper.getInstance().isJanrainIntialized()) {
			mUser.loginUserUsingSocialProvider(getActivity(), providerName, this, null);
		}
	}

	public interface SocialProvider {

		public String FACEBOOK = "facebook";

		public String TWITTER = "twitter";

		public String GOOGLE_PLUS = "googleplus";
	}

	@Override
	public void setViewParams(Configuration config) {
		applyParams(config, mTvWelcome);
		applyParams(config, mTvWelcomeDesc);
		applyParams(config, mLlCreateBtnContainer);
		applyParams(config, mLlLoginBtnContainer);
	}

	@Override
	public String getActionbarTitle() {
		return getResources().getString(R.string.SigIn_TitleTxt);
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiState();
			handleJanrainInitPb();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
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
		RegistrationHelper.getInstance().getSocialProviders();
		handleSocialProviders(RegistrationHelper.getInstance().getCountryCode());
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.getInstance().isJanrainIntialized()) {
				mRegError.hideError();
				enableControls(true);
			} else {
				mRegError.hideError();
			}
		} else {
			mRegError.setError(getString(R.string.NoNetworkConnection));
			enableControls(false);
		}
	}

	private void enableControls(boolean state) {
		if (state && NetworkUtility.getInstance().isOnline()) {
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
		hideProviderProgress();
		RLog.i("HomeFragment", "social login success");
		enableControls(true);
		User user = new User(getRegistrationMainActivity().getApplicationContext());
		if (user.getEmailVerificationStatus(getRegistrationMainActivity().getApplicationContext())) {
			getRegistrationMainActivity().addWelcomeFragmentOnVerification();
		} else {
			getRegistrationMainActivity().addFragment(new AccountActivationFragment());
		}
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
		hideProviderProgress();
		RLog.i("HomeFragment", "login failed");
		enableControls(true);

	}

	@Override
	public void onLoginFailedWithTwoStepError(JSONObject prefilledRecord,
	        String socialRegistrationToken) {
		hideProviderProgress();
		enableControls(true);
		RLog.i("HomeFragment", "Login failed with two step error" + "JSON OBJECT :"
		        + prefilledRecord);
		getRegistrationMainActivity().addAlmostDoneFragment(prefilledRecord, mProvider,
		        socialRegistrationToken);
	}

	@Override
	public void onLoginFailedWithMergeFlowError(String mergeToken, String existingProvider,
	        String conflictingIdentityProvider, String conflictingIdpNameLocalized,
	        String existingIdpNameLocalized) {
		hideProviderProgress();
		enableControls(true);
		RLog.i("HomeFragment", "login failed with merge flow");
		if (mUser.handleMergeFlowError(existingProvider)) {
			getRegistrationMainActivity().addMergeAccountFragment(mergeToken, existingProvider);
		} else {
			if (NetworkUtility.getInstance().isOnline() && RegistrationHelper.getInstance().isJanrainIntialized()) {
				mProvider = existingProvider;
				showProviderProgress();
				mUser.loginUserUsingSocialProvider(getActivity(), existingProvider, this,
				        mergeToken);
			}
			
		}

	}

	@Override
	public void onContinueSocialProviderLoginSuccess() {
		hideProviderProgress();
		RLog.i("HomeFragment", "onContinueSocialProviderLoginSuccess");
		enableControls(true);
		getRegistrationMainActivity().addFragment(new WelcomeFragment());
	}

	@Override
	public void onContinueSocialProviderLoginFailure(
	        UserRegistrationFailureInfo userRegistrationFailureInfo) {
		hideProviderProgress();
		enableControls(true);
		RLog.i("HomeFragment", "onContinueSocialProviderLoginFailure");
	}

}

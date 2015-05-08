package com.philips.cl.di.reg.ui.traditional;

import java.util.Locale;

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
import android.widget.TextView;

import com.philips.cl.di.reg.R;
import com.philips.cl.di.reg.events.EventHelper;
import com.philips.cl.di.reg.events.EventListener;
import com.philips.cl.di.reg.settings.RegistrationHelper;
import com.philips.cl.di.reg.ui.customviews.XProviderButton;
import com.philips.cl.di.reg.ui.customviews.XRegError;
import com.philips.cl.di.reg.ui.utils.NetworkUtility;
import com.philips.cl.di.reg.ui.utils.RLog;
import com.philips.cl.di.reg.ui.utils.RegConstants;

public class HomeFragment extends RegistrationBaseFragment implements
		OnClickListener, EventListener {

	private Button mBtnCreateAccount;

	private XProviderButton mBtnMyPhilips;

	private XProviderButton mBtnFacebook;

	private XProviderButton mBtnTwitter;

	private TextView mTvWelcome;

	private TextView mTvWelcomeDesc;

	private LinearLayout mLlCreateBtnContainer;

	private LinearLayout mLlLoginBtnContainer;

	private XRegError mRegError;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventHelper.getInstance().registerEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().registerEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		RLog.d(RLog.FRAGMENT_LIFECYCLE, "UserSignInFragment : onCreateView");
		View view = inflater.inflate(R.layout.fragment_home, container, false);
		initUI(view);
		return view;
	}

	@Override
	public void onConfigurationChanged(Configuration config) {
		super.onConfigurationChanged(config);
		RLog.d(RLog.FRAGMENT_LIFECYCLE,
				"UserSignInFragment : onConfigurationChanged");
		setViewParams(config);
	}

	@Override
	public void onDestroy() {
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.IS_ONLINE, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_SUCCESS, this);
		EventHelper.getInstance().unregisterEventNotification(
				RegConstants.JANRAIN_INIT_FAILURE, this);
		super.onDestroy();
	}

	private void initUI(View view) {
		consumeTouch(view);
		mTvWelcome = (TextView) view.findViewById(R.id.tv_reg_welcome);
		mTvWelcomeDesc = (TextView) view.findViewById(R.id.tv_reg_welcome_desc);
		mLlCreateBtnContainer = (LinearLayout) view
				.findViewById(R.id.ll_reg_create_account_container);
		mLlLoginBtnContainer = (LinearLayout) view
				.findViewById(R.id.rl_reg_singin_options);
		mBtnCreateAccount = (Button) view
				.findViewById(R.id.btn_reg_create_account);
		mBtnCreateAccount.setOnClickListener(this);
		mBtnMyPhilips = (XProviderButton) view
				.findViewById(R.id.btn_reg_my_philips);
		mBtnMyPhilips.setOnClickListener(this);

		mBtnFacebook = (XProviderButton) view
				.findViewById(R.id.btn_reg_facebook);
		mBtnFacebook.setOnClickListener(this);

		mBtnTwitter = (XProviderButton) view.findViewById(R.id.btn_reg_twitter);
		mBtnTwitter.setOnClickListener(this);

		mRegError = (XRegError) view.findViewById(R.id.reg_error_msg);

		setViewParams(getResources().getConfiguration());
		linkifyTermAndPolicy(mTvWelcomeDesc);
		enableControls(false);
		handleUiState();
	}

	@Override
	public void onClick(View v) {
		/**
		 * Library does not include resource constants after ADT 14 Link
		 * :http://tools.android.com/tips/non-constant-fields
		 */
		if (v.getId() == R.id.btn_reg_create_account) {
			getRegistrationMainActivity().addFragment(
					new CreateAccountFragment());
		} else if (v.getId() == R.id.btn_reg_my_philips) {
			getRegistrationMainActivity().addFragment(
					new SignInAccountFragment());
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
	public String getActionbarTitle() {
		return getResources().getString(R.string.SigIn_TitleTxt);
	}

	@Override
	public void onEventReceived(String event) {
		if (RegConstants.IS_ONLINE.equals(event)) {
			handleUiState();
		} else if (RegConstants.JANRAIN_INIT_SUCCESS.equals(event)) {
			System.out.println("reint");
			enableControls(true);
		} else if (RegConstants.JANRAIN_INIT_FAILURE.equals(event)) {
			enableControls(false);
		}
	}

	private void handleUiState() {
		if (NetworkUtility.getInstance().isOnline()) {
			if (RegistrationHelper.isJanrainIntialized()) {
				mRegError.hideError();
				enableControls(true);
			} else {
				mRegError.setError(getString(R.string.NoNetworkConnection));
			}
		} else {
			mRegError.setError(getString(R.string.NoNetworkConnection));
			enableControls(false);
		}
	}

	private void enableControls(boolean state) {

		mBtnCreateAccount.setEnabled(state);
		mBtnMyPhilips.setEnabled(state);

		/*
		 * mBtnFacebook.setEnabled(state); mLlTwitter.setEnabled(state);
		 * mLlGooglePlus.setEnabled(state);
		 */

		if (state) {
			setAlphaForView(mBtnMyPhilips, 1);
			setAlphaForView(mBtnFacebook, 1);
			setAlphaForView(mBtnTwitter, 1);
			mRegError.hideError();
		} else {
			setAlphaForView(mBtnMyPhilips, 0.75f);
			setAlphaForView(mBtnFacebook, 0.75f);
			setAlphaForView(mBtnTwitter, 0.75f);
		}
	}

	private void linkifyTermAndPolicy(TextView pTvPrivacyPolicy) {
		String termAndPrivacy = getResources().getString(
				R.string.LegalNoticeText);
		String terms = getResources()
				.getString(R.string.TermsAndConditionsText);
		String privacy = getResources().getString(R.string.PrivacyPolicyText);
		int termStartIndex = termAndPrivacy.toLowerCase(Locale.getDefault())
				.indexOf(terms.toLowerCase(Locale.getDefault()));
		int privacyStartIndex = termAndPrivacy.toLowerCase(Locale.getDefault())
				.indexOf(privacy.toLowerCase(Locale.getDefault()));

		SpannableString spanableString = new SpannableString(termAndPrivacy);
		spanableString.setSpan(new ClickableSpan() {

			@Override
			public void onClick(View widget) {
				handleTermsCondition();
			}
		}, termStartIndex, termStartIndex + terms.length(),
				Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

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
		pTvPrivacyPolicy.setHighlightColor(getResources().getColor(
				android.R.color.transparent));
	}

	private void removeUnderlineFromLink(SpannableString spanableString) {
		for (ClickableSpan u : spanableString.getSpans(0,
				spanableString.length(), ClickableSpan.class)) {
			spanableString.setSpan(new UnderlineSpan() {

				public void updateDrawState(TextPaint tp) {
					tp.setUnderlineText(false);
				}
			}, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
		}

		for (URLSpan u : spanableString.getSpans(0, spanableString.length(),
				URLSpan.class)) {
			spanableString.setSpan(new UnderlineSpan() {

				public void updateDrawState(TextPaint tp) {
					tp.setUnderlineText(false);
				}
			}, spanableString.getSpanStart(u), spanableString.getSpanEnd(u), 0);
		}
	}

	private void setAlphaForView(View v, float alpha) {
		AlphaAnimation animation = new AlphaAnimation(alpha, alpha);
		animation.setDuration(0);
		animation.setFillAfter(true);
		v.startAnimation(animation);
	}

	private void handlePrivacyPolicy() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(getResources().getString(R.string.PrivacyPolicyURL)));
		startActivity(browserIntent);
	}

	private void handleTermsCondition() {
		Intent browserIntent = new Intent(Intent.ACTION_VIEW,
				Uri.parse(getResources().getString(R.string.PrivacyPolicyURL)));
		startActivity(browserIntent);
	}

}

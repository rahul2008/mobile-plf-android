/**
 * ContactUsFragment will help to provide options to contact Philips.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus.fragments;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.localematch.LocaleMatchHandlerObserver;
import com.philips.cdp.digitalcare.request.RequestData;
import com.philips.cdp.digitalcare.request.ResponseCallback;
import com.philips.cdp.digitalcare.social.facebook.FacebookWebFragment;
import com.philips.cdp.digitalcare.social.twitter.TwitterWebFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

/*import com.philips.cdp.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cdp.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cdp.digitalcare.social.twitter.TwitterSupportFragment;*/

/*import com.philips.cdp.digitalcare.social.facebook.FacebookAuthenticate;
 import com.philips.cdp.digitalcare.social.facebook.FacebookHelper;
 import com.philips.cdp.digitalcare.social.facebook.FacebookScreenFragment;
 import com.facebook.Session;
 import com.facebook.SessionState;*/


public class ContactUsFragment extends DigitalCareBaseFragment implements
        /*TwitterAuthenticationCallback,*/ OnClickListener, ResponseCallback, Observer {
    private static final String CDLS_URL_PORT = "https://www.philips.com/prx/cdls/%s/%s/%s/%s.querytype.(fallback)";
    private static final String TAG = ContactUsFragment.class.getSimpleName();
    private static final String USER_SELECTED_PRODUCT_CTN = "mCtnFromPreference";
    private static final String USER_PREFERENCE = "user_product";
    private static final String USER_SELECTED_PRODUCT_CTN_CALL = "contact_call";
    private static final String USER_SELECTED_PRODUCT_CTN_HOURS = "contact_hours";
    private static View mView = null;
    private static boolean isFirstTimeCdlsCall = true;
    private SharedPreferences prefs = null;
    private LinearLayout mContactUsParent = null;
    private LinearLayout mSocialProviderParent = null;
    private FrameLayout.LayoutParams mParams = null;
    private DigitalCareFontButton mChat = null;
    private DigitalCareFontButton mEmail = null;
    private DigitalCareFontButton mCallPhilips = null;
    private CdlsResponseModel mCdlsParsedResponse = null;
    private TextView mFirstRowText = null;
    private TextView mContactUsOpeningHours = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private String mCdlsResponseStr = null;
    private ProgressDialog mPostProgress = null;
    private final Runnable mTwitteroAuthRunnable = new Runnable() {

        @Override
        public void run() {
            if (mPostProgress != null && mPostProgress.isShowing()) {
                mPostProgress.dismiss();
                mPostProgress = null;
            }
        }
    };
    private ProgressDialog mDialog = null;
    private final CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            if ((!isCdlsResponseNull(response)) && response.getSuccess()) {
                mCdlsParsedResponse = response;
                updateUi();
            } else {
              /*
                First hit CDLS server wit SubCategory, if that fails then hit
                CDLS again with Category.
                 */
                if (isFirstTimeCdlsCall) {
                    isFirstTimeCdlsCall = false;
                    requestCdlsData();
                } else {
                    fadeoutButtons();
                }
            }
        }
    };
    private Configuration config = null;
    private View mSocialDivider = null;
    private int mSdkVersion;
    private Utils mUtils = null;

    private boolean isCdlsResponseNull(CdlsResponseModel response) {
        return response == null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSdkVersion = Build.VERSION.SDK_INT;
      /*  DigiCareLogger.i(TAG, "ContactUsFragment : onCreate");*/
        isFirstTimeCdlsCall = true;

        // mTwitterProgresshandler = new Handler();
        // if (isConnectionAvailable())
        // requestCdlsData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /*DigiCareLogger.i(TAG, "ContactUsFragment : onCreateView: mView - "
                + mView);*/

        prefs = getActivity().getSharedPreferences(
                USER_PREFERENCE, Context.MODE_PRIVATE);
        if (mView != null) {
            final ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.consumercare_fragment_contact_us, container, false);
        } catch (InflateException e) {
            DigiCareLogger.e(TAG, "UI Inflation error : " + e);
        }
        mSocialDivider = mView.findViewById(R.id.socialDivider);

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
       /* DigiCareLogger.i(TAG,
                "ContactUsFragment : onActivityCreated : mConactUsParent == "
                        + mContactUsParent);*/
        // if (mContactUsParent == null) {
        // mTwitterProgresshandler = new Handler();

        mContactUsParent = (LinearLayout) getActivity().findViewById(
                R.id.contactUsParent);
        mChat = (DigitalCareFontButton) getActivity().findViewById(
                R.id.contactUsChat);
        mCallPhilips = (DigitalCareFontButton) getActivity().findViewById(
                R.id.contactUsCall);
        mEmail = (DigitalCareFontButton) getActivity().findViewById(
                R.id.contactUsEmail);
        mContactUsOpeningHours = (TextView) getActivity().findViewById(
                R.id.contactUsOpeningHours);
        mFirstRowText = (TextView) getActivity()
                .findViewById(R.id.firstRowText);
        mSocialProviderParent = (LinearLayout) getActivity().findViewById(
                R.id.contactUsSocialParent);


        // mFacebook.setOnClickListener(this);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        mUtils = new Utils();
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);

        createSocialProviderMenu();
        final float density = getResources().getDisplayMetrics().density;
        setHelpButtonParams(density);
        //Live chat is configurable parameter. Developer can enable/disable it.
        if (getResources().getBoolean(R.bool.live_chat_required)) {
            mChat.setVisibility(View.VISIBLE);
        }
        mChat.setOnClickListener(this);
        mChat.setTransformationMethod(null);
        mCallPhilips.setOnClickListener(this);
        mCallPhilips.setTransformationMethod(null);
        mEmail.setOnClickListener(this);
        mEmail.setTransformationMethod(null);

        if (isInternetAvailable && isCdlsUrlNull()) {
            requestCdlsData();
        } else {
            LocaleMatchHandlerObserver observer = DigitalCareConfigManager.getInstance().getObserver();
            if (observer != null) {
                observer.addObserver(this);
            }

            String contactNumber = prefs.getString(USER_SELECTED_PRODUCT_CTN_CALL, "");
            String hours = prefs.getString(USER_SELECTED_PRODUCT_CTN_HOURS, "");

            DigiCareLogger.v(TAG, "CACHED Number : " + contactNumber);
            DigiCareLogger.v(TAG, "CACHED Hours : " + hours);
            if (mFirstRowText != null && isContactHoursCached()) {
                mFirstRowText.setVisibility(View.VISIBLE);
                mFirstRowText.setText(hours);
            }
            if (mCallPhilips != null && isContactNumberCached()) {
                mCallPhilips.setVisibility(View.VISIBLE);
                mContactUsOpeningHours.setVisibility(View.VISIBLE);
                mCallPhilips.setText(getResources().getString(R.string.call_number)
                        + " "
                        + contactNumber);
            }
            isEmailButtonEnabled();

        }

        mParams = (FrameLayout.LayoutParams) mContactUsParent.getLayoutParams();

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACT_US,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
        config = getResources().getConfiguration();
    }


    protected boolean isEmailButtonEnabled() {
        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager.getInstance().getConsumerProductInfo();
        if (consumerProductInfo.getCategory() == null) {
            mEmail.setVisibility(View.GONE);

        } else {
            mEmail.setVisibility(View.VISIBLE);
        }

        return (mEmail.getVisibility() == View.VISIBLE) ? true : false;
    }

    protected boolean isContactNumberCached() {
        String customerSupportNumber = null;
        customerSupportNumber = prefs.getString(USER_SELECTED_PRODUCT_CTN_CALL, "");
        return (customerSupportNumber != null && customerSupportNumber != "");
    }


    protected boolean isContactHoursCached() {
        String contactHours = null;
        contactHours = prefs.getString(USER_SELECTED_PRODUCT_CTN_HOURS, "");
        return (contactHours != null && contactHours != "");
    }

    private void createSocialProviderMenu() {
        TypedArray titles = getResources().obtainTypedArray
                (R.array.social_service_provider_menu_title);
        final TypedArray resources = getResources().obtainTypedArray
                (R.array.social_service_provider_menu_resources);

        if (titles.length() == 0) {
            mSocialProviderParent.setVisibility(View.GONE);
            mSocialDivider.setVisibility(View.GONE);
        } else {
            for (int i = 0; i < titles.length(); i++) {
                createButtonLayout(titles.getResourceId(i, 0), resources.getResourceId(i, 0));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
        setViewParams(config);
    }

    /*
     * Forming CDLS url. This url will be different for US and other countries.
     */
    protected String getCdlsUrl() {
        final Locale localeCoutryFallback = DigitalCareConfigManager.getInstance().
                getLocaleMatchResponseWithCountryFallBack();

        if (localeCoutryFallback == null) {
            return null;
        }
        final ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();

        return getCdlsUrl(consumerProductInfo.getSector(),
                localeCoutryFallback.toString(),
                consumerProductInfo.getCatalog(),
                isFirstTimeCdlsCall ? consumerProductInfo.getSubCategory()
                        : consumerProductInfo.getCategory());
    }
/*
    @Override
	public void onTwitterLoginFailed() {
		DigiCareLogger.d(TAG, "Twitter Authentication Failed");
	}

	@Override
	public void onTwitterLoginSuccessful() {
		showFragment(new TwitterSupportFragment());
	}*/

    protected String getCdlsUrl(String sector, String locale, String catalog,
                                String subcategory) {
        return String.format(CDLS_URL_PORT, sector, locale, catalog,
                subcategory);
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setViewParams(config);
    }

    protected void requestCdlsData() {
        DigiCareLogger.d(TAG, "CDLS Request Thread is started");
        startProgressDialog();
        if (isCdlsUrlNull()) {
            String url = getCdlsUrl();
            DigiCareLogger.d(TAG, "CDLS Request URL : " + url);
            RequestData requestData = new RequestData();
            requestData.setRequestUrl(url);
            requestData.setResponseCallback(this);
            requestData.execute();
        }
    }

    private boolean isCdlsUrlNull() {
        return getCdlsUrl() != null;
    }

    @Override
    public void onResponseReceived(String response) {
        isEmailButtonEnabled();
        if (isAdded()) {
            closeProgressDialog();
           /* DigiCareLogger.i(TAG, "onResponseReceived : " + response);*/
            if (response != null && isAdded()) {
                mCdlsResponseStr = response;
                parseCdlsResponse(response);
            } else {
                /*
                First hit CDLS server wit SubCategory, if that fails then hit
                CDLS again with Category.
                 */
                if (isFirstTimeCdlsCall) {
                    isFirstTimeCdlsCall = false;
                    requestCdlsData();
                } else {
                    fadeoutButtons();
                }
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        closeProgressDialog();
    }

    protected void parseCdlsResponse(String response) {
        final CdlsResponseParser cdlsResponseParser = new CdlsResponseParser(
                mParsingCompletedCallback);
        cdlsResponseParser.parseCdlsResponse(response);
    }

    protected void updateUi() {
        if (mCdlsParsedResponse.getSuccess()/*|| mCdlsParsedResponse.getError() != null*/) {
            final CdlsPhoneModel phoneModel = mCdlsParsedResponse.getPhone();
            if (phoneModel != null) {
                if (phoneModel.getPhoneNumber() != null) {
                    mCallPhilips.setVisibility(View.VISIBLE);
                }
                enableBottomText();
                final StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(phoneModel.getOpeningHoursWeekdays())
                        .append(phoneModel.getOpeningHoursSaturday())
                        .append(phoneModel.getOpeningHoursSunday())
                        .append(phoneModel.getOptionalData1())
                        .append(phoneModel.getOptionalData2())
                        .append("\n" + phoneModel.getmPhoneTariff() + "\n");
                enableBottomText();
                mCallPhilips
                        .setText(getResources().getString(R.string.call_number)
                                + " "
                                + mCdlsParsedResponse.getPhone()
                                .getPhoneNumber());
                mFirstRowText.setText(stringBuilder);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_SELECTED_PRODUCT_CTN_HOURS, stringBuilder.toString());
                editor.putString(USER_SELECTED_PRODUCT_CTN_CALL, mCdlsParsedResponse.getPhone()
                        .getPhoneNumber());

                editor.apply();
            }

          /*  if (hasEmptyChatContent(mCdlsParsedResponse)) {
                mChat.setBackgroundResource(R.drawable.consumercare_selector_option_button_faded_bg);
                mChat.setEnabled(false);
            }*/
        } else if (isCdlsResponseModelNull()) {
            fadeoutButtons();
        } else {
            fadeoutButtons();
        }

    }

    private boolean isCdlsResponseModelNull() {
        return mCdlsParsedResponse.getError() != null;
    }

    protected boolean hasEmptyChatContent(CdlsResponseModel cdlsResponseModel) {
        return ((cdlsResponseModel.getChat() == null
                || cdlsResponseModel.getChat().getContent() == null
                || cdlsResponseModel.getChat().getContent()
                .equalsIgnoreCase(""))
                &&
                (cdlsResponseModel.getChat() == null
                        || cdlsResponseModel.getChat().getScript() == null
                        || cdlsResponseModel.getChat().getScript()
                        .equalsIgnoreCase("")));
    }

    protected void closeProgressDialog() {


        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog.cancel();
            mDialog = null;
        }
    }

    protected void startProgressDialog() {

        if (getActivity() == null) {
            return;
        }
        if (mDialog == null) {
            mDialog = new ProgressDialog(getActivity());
            mDialog.setCancelable(true);
        }
        mDialog.setMessage(getActivity().getResources().getString(
                R.string.loading));
        if (!(getActivity().isFinishing())) {
            mDialog.show();
        }
    }

    protected void callPhilips() {
        try {
            final Intent myintent = new Intent(Intent.ACTION_DIAL);
            myintent.setData(Uri.parse("tel:"
                    + prefs.getString(USER_SELECTED_PRODUCT_CTN_CALL, "")));
            myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myintent);
        } catch (NullPointerException e) {
            // DigiCareLogger.d(TAG, "on Call Click : "+ mCdlsParsedResponse.getPhone().getPhoneNumber());
            DigiCareLogger.e(TAG, "Null Pointer Exception on  callPhilips method : " + e);
        }
    }

    @Override
    public void onClick(View view) {
        final int id = view.getId();
        final String tag = (String) view.getTag();

        boolean actionTaken = false;

        try {
            if (tag != null) {
                actionTaken = DigitalCareConfigManager.getInstance()
                        .getSocialProviderListener()
                        .onSocialProviderItemClicked(tag.toString());
            }
        } catch (NullPointerException exception) {
            DigiCareLogger.e(TAG, "SocialProvider listener not added in vertical side..");
        }
        if (actionTaken) {
            return;
        }

        if (id == R.id.contactUsChat && isConnectionAvailable()) {
//            if (mCdlsResponseStr == null) {
//                showAlert(getActivity().getString(R.string.no_data));
//                return;
//            } else if (mCdlsParsedResponse != null
//                    && !mCdlsParsedResponse.getSuccess()) {
//                showAlert(getActivity().getString(R.string.no_data));
//                return;
//            }
            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CHAT);
            showFragment(new ChatFragment());
        } else if (id == R.id.contactUsCall) {
            if (!isContactNumberCached()) {
                showAlert(getActivity().getString(R.string.no_data));
            } else if (isSimAvailable()) {
                tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CALL);
                callPhilips();
            } else if (!isSimAvailable()) {
                showAlert(getActivity().getString(R.string.check_sim));
            } else {
                showAlert(getActivity().getString(R.string.check_sim));
            }
        } else if (tag != null
                && tag.equalsIgnoreCase(getStringKey(R.string.facebook))
                && isConnectionAvailable()) {

            launchFacebookFeature();

			/*
             * Session mFacebookSession = Session.getActiveSession();
			 *
			 * DigiCareLogger.d(TAG, "Session - getSession from Facebook SDK " +
			 * mFacebookSession); if (mFacebookSession == null) {
			 * DigiCareLogger.d(TAG,
			 * "Session is null so Starting FacebookSession");
			 * startFacebookSession(); } else if ((mFacebookSession != null) &&
			 * (mFacebookSession.getState() ==
			 * SessionState.CLOSED_LOGIN_FAILED)) { DigiCareLogger.d(TAG,
			 * "Session is state is CLOSED_LOGIN_FAILED" +
			 * " so Starting Facebook Session"); startFacebookSession(); } else
			 * if ((mFacebookSession != null) && (mFacebookSession.getState() ==
			 * SessionState.OPENED)) { DigiCareLogger.d(TAG,
			 * "Session - getSession from Facebook SDK is not NULL  : " +
			 * mFacebookSession); showFragment(new FacebookScreenFragment());
			 * DigiCareLogger.d(TAG, "Session is not null");
			 *
			 * }
			 */
        } else if (tag != null
                && tag.equalsIgnoreCase(getStringKey(R.string.twitter))
                && isConnectionAvailable()) {
            // mTwitter.setClickable(false);
            launchTwitterFeature();
            /*TwitterAuthentication mTwitter = TwitterAuthentication
                    .getInstance(getActivity());
			mTwitter.initSDK(this);
			mPostProgress = new ProgressDialog(getActivity());
			mPostProgress.setMessage(getActivity().getResources().getString(
					R.string.loading));
			mPostProgress.setCancelable(false);
			if (!(getActivity().isFinishing()))
				mPostProgress.show();
			mTwitterProgresshandler.postDelayed(mTwitteroAuthRunnable, 10000l);*/

        } else if ((id == R.id.contactUsEmail) && isConnectionAvailable()) {
            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_EMAIL);
            sendEmail();
        }
    }

    private boolean isSimAvailable() {
        return mUtils.isSimAvailable(getActivity());
    }

    private void launchFacebookFeature() {
        tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_Facebook);
        try {
            final Uri uri = Uri.parse("fb://page/"
                    + getActivity().getResources().getString(
                    R.string.facebook_product_pageID));
            DigiCareLogger.i(TAG, "Launching Facebook with Information : " + uri);
            final Map<String, Object> contextData = new HashMap<String, Object>();
            contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL,
                    AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE,
                    AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_EXIT_LINK,
                    uri + toString());
            AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK,
                    contextData);

            getActivity().getApplicationContext().getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
            DigiCareLogger.v(TAG, "Launced Installed Facebook Application");
        } catch (Exception e) {
            DigiCareLogger.v(TAG, "Launching Facebook Webpage");
            showFragment(new FacebookWebFragment());
        }
    }

    protected void launchTwitterFeature() {
        tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
        final Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        String information = getProductInformation();
        tweetIntent.putExtra(Intent.EXTRA_TEXT, information);
        DigiCareLogger.i(TAG, "Launching Twitter with information : " + information);
        tweetIntent.setType("text/plain");

        final PackageManager packManager = getActivity().getPackageManager();
        final List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent,
                PackageManager.MATCH_DEFAULT_ONLY);

        boolean resolved = false;
        for (ResolveInfo resolveInfo : resolvedInfoList) {
            if (resolveInfo.activityInfo.packageName.startsWith("com.twitter.android")) {
                tweetIntent.setClassName(
                        resolveInfo.activityInfo.packageName,
                        resolveInfo.activityInfo.name);
                resolved = true;
                break;
            }
        }
        if (resolved) {
            final String twitterUrl = "www.twitter.com/";
            final String twitterSupportAccount = getActivity().getString(R.string.twitter_page);
            final String twitterPageName = twitterUrl + "@" + twitterSupportAccount;
            final Map<String, Object> contextData = new HashMap<String, Object>();
            contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
            contextData.put(AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
            contextData.put(AnalyticsConstants.ACTION_KEY_EXIT_LINK, twitterPageName);
            AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK, contextData);

            startActivity(tweetIntent);
        } else {
            showFragment(new TwitterWebFragment());
        }
    }

    protected String getProductInformation() {
        final String twitterPage = getActivity().getString(R.string.twitter_page);
        final String twitterLocalizedPrefixText = getActivity().getResources().getString(
                R.string.support_productinformation);
        final String productTitle = DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getProductTitle();
        final String ctn = DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getCtn();
        return "@" + twitterPage + " " + twitterLocalizedPrefixText
                + " "
                + productTitle
                + " "
                + ctn;
    }

    @Override
    public void onPause() {
        if (mPostProgress != null && mPostProgress.isShowing()) {
            mPostProgress.dismiss();
            mPostProgress = null;
        }
        super.onPause();
    }

    private void tagServiceRequest(String serviceChannel) {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SERVICE_REQUEST,
                AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, serviceChannel);
    }

    private void tagTechnicalError() {
        AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SET_ERROR,
                AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_RESPONSE_CDLS);
    }

    /*
     * If CDLS response received then enable to bottom text.
     */
    protected void enableBottomText() {
        mCallPhilips.setVisibility(View.VISIBLE);
        mContactUsOpeningHours.setVisibility(View.VISIBLE);
        mFirstRowText.setVisibility(View.VISIBLE);
    }

    /*
     * If feature is not available then fade it out.
     */
    protected void fadeoutButtons() {
        tagTechnicalError();
        if (mCallPhilips != null) {
            mCallPhilips.setVisibility(View.GONE);
//            mCallPhilips
//                    .setBackgroundResource(R.drawable.consumercare_selector_option_button_faded_bg);
//            mCallPhilips.setEnabled(false);
        }

        TypedArray titles = getResources().obtainTypedArray
                (R.array.social_service_provider_menu_title);
        boolean isSocialButtonsEnabled = titles.length() > 0 ? true : false;
        boolean isEmailEnabled = mEmail.getVisibility() == View.VISIBLE ? true : false;
        boolean isChatEnabled = mChat.getVisibility() == View.VISIBLE ? true : false;
        boolean isCallEnabled = mCallPhilips.getVisibility() == View.VISIBLE ? true : false;

       /* if (!(isSocialButtonsEnabled && isEmailEnabled && isChatEnabled && isCallEnabled)) {
            showAlert(getResources().getString(R.string.NO_SUPPORT_KEY));
        }*/

        if (!isSocialButtonsEnabled && !isEmailEnabled) {
            showAlert(getResources().getString(R.string.NO_SUPPORT_KEY));
        }

      /*if (mChat != null) {
            mChat.setVisibility(View.GONE);
        }*/
    }


    @Override
    public void setViewParams(Configuration config) {

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            // if (mLeftRightMarginPort != 0)
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;
        } else {
            // if (mLeftRightMarginLand != 0)
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
        }
        mContactUsParent.setLayoutParams(mParams);
    }

    /*
     * TODO: Sending message is been implemented through gmail. So this is
     * temperory.
     *
     * Wouter is working on In-App messaging.
     */
    protected void sendEmail() {
//        try {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("message/rfc822");
//            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources()
//                    .getString(R.string.support_email_id)});
//            intent.putExtra(
//                    Intent.EXTRA_TEXT, getGmailContentInformation());
//            intent.setPackage("com.google.android.gm");
//            getActivity().startActivity(intent);
//        } catch (ActivityNotFoundException e) {
//            Intent intent = new Intent(Intent.ACTION_SEND);
//            intent.setType("message/rfc822");
//            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{getResources()
//                    .getString(R.string.support_email_id)});
//            intent.putExtra(
//                    Intent.EXTRA_TEXT, getGmailContentInformation());
//            getActivity().startActivity(Intent.createChooser(intent, null));
//        }
        showFragment(new EmailFragment());
    }

    protected String getGmailContentInformation() {
        return getActivity().getResources().getString(
                R.string.support_productinformation)
                + " "
                + DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getProductTitle()
                + " "
                + DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getCtn() + " ";
    }

    private Drawable getDrawable(int resId) {
        return ContextCompat.getDrawable(getActivity(), resId);
        // getResources().getDrawable(resId);
    }

    private String getStringKey(int resId) {
        return getResources().getResourceEntryName(resId);
    }

    /**
     * Create RelativeLayout at runTime. RelativeLayout will have button and
     * image together.
     */
    @SuppressLint("NewApi")
    private void createButtonLayout(int buttonTitleResId, int buttonDrawableResId) {

        final String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);
        final String buttonDrawable = getResources().getResourceEntryName(buttonDrawableResId);

        final String packageName = getActivity().getPackageName();
        final int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);
        final int drawable = getResources().getIdentifier(
                packageName + ":drawable/" + buttonDrawable, null, null);
        final float density = getResources().getDisplayMetrics().density;

        final RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        final Button button = createButton(density, title, drawable);
        relativeLayout.addView(button);
        setButtonParams(button, density);
        setHelpButtonParams(density);
        mSocialProviderParent.addView(relativeLayout);
        setRelativeLayoutParams(relativeLayout, density);

        //Setting tag because we need to get String title for this view
        // which needs to be handled at button click.

        relativeLayout.setTag(buttonTitle);
        relativeLayout.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        final RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        relativeLayout.setLayoutParams(params);
        if (mSdkVersion < Build.VERSION_CODES.JELLY_BEAN) {
            relativeLayout.setBackgroundResource(R.drawable.consumercare_prod_reg_social_border_btn);
        } else {
            relativeLayout
                    .setBackground(getDrawable(R.drawable.consumercare_prod_reg_social_border_btn));
        }
        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density) {
        final LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();
        param.topMargin = (int) (15 * density);
        relativeLayout.setLayoutParams(param);
    }

    @SuppressLint("NewApi")
    private Button createButton(float density, int title, int resId) {
        final Button button = new Button(getActivity(), null, R.style.fontButton);

        button.setGravity(Gravity.CENTER);
        // button.setPadding((int) (80 * density), 0, 0, 0);

        if (Build.VERSION.SDK_INT < 23) {
            button.setTextAppearance(getActivity(), R.style.fontButton);
        } else {
            button.setTextAppearance(R.style.fontButton);
        }
        final Typeface buttonTypeface = Typeface.createFromAsset
                (getActivity().getAssets(),
                        "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
        if (mSdkVersion < Build.VERSION_CODES.JELLY_BEAN) {
            button.setBackgroundResource(resId);
            button.setGravity(Gravity.CENTER);
        } else {
            button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            button.setBackground(getDrawable(resId));
        }
        button.setText(title);
        return button;
    }

    private void setButtonParams(Button button, float density) {
        // RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
        // .getLayoutParams();
        final RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);
    }

    private void setHelpButtonParams(float density) {

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);
        mCallPhilips.setLayoutParams(params);
        mChat.setLayoutParams(params);
        mEmail.setLayoutParams(params);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.contact_us);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACT_US;
    }

	/*
     * protected void startFacebookSession() { FacebookHelper mHelper =
	 * FacebookHelper.getInstance(getActivity());
	 * mHelper.openFacebookSession(new FacebookAuthenticate() {
	 *
	 * @Override public void onSuccess() { showFragment(new
	 * FacebookScreenFragment()); } }); }
	 */

    @Override
    public void update(Observable observable, Object data) {
        if (!(getActivity() == null)) {
            requestCdlsData();
        }
    }

}

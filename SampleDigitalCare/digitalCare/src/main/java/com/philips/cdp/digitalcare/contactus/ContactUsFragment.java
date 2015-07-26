package com.philips.cdp.digitalcare.contactus;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
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
import com.philips.cdp.digitalcare.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.RequestData;
import com.philips.cdp.digitalcare.ResponseCallback;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontButton;
import com.philips.cdp.digitalcare.social.facebook.FacebookWebFragment;
import com.philips.cdp.digitalcare.social.twitter.TwitterWebFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.Utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*import com.philips.cdp.digitalcare.social.twitter.TwitterAuthentication;
import com.philips.cdp.digitalcare.social.twitter.TwitterAuthenticationCallback;
import com.philips.cdp.digitalcare.social.twitter.TwitterSupportFragment;*/

/*import com.philips.cdp.digitalcare.social.facebook.FacebookAuthenticate;
 import com.philips.cdp.digitalcare.social.facebook.FacebookHelper;
 import com.philips.cdp.digitalcare.social.facebook.FacebookScreenFragment;
 import com.facebook.Session;
 import com.facebook.SessionState;*/

/**
 * ContactUsFragment will help to provide options to contact Philips.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 */
public class ContactUsFragment extends DigitalCareBaseFragment implements
        /*TwitterAuthenticationCallback,*/ OnClickListener, ResponseCallback {
    private static final String CDLS_URL_PORT = "http://www.philips.com/prx/cdls/%s/%s/%s/%s.querytype.(fallback)";
    private static final String TAG = ContactUsFragment.class.getSimpleName();
    private static View mView = null;
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
    private Handler mTwitterProgresshandler = null;
    private ProgressDialog mPostProgress = null;
    private ProgressDialog mDialog = null;
    private Configuration config = null;
    private View mSocialDivider = null;


    private CdlsParsingCallback mParsingCompletedCallback = new CdlsParsingCallback() {
        @Override
        public void onCdlsParsingComplete(final CdlsResponseModel response) {
            if (response != null) {
                mCdlsParsedResponse = response;
                updateUi();
            } else {
                fadeoutButtons();
            }
        }
    };
    private Runnable mTwitteroAuthRunnable = new Runnable() {

        @Override
        public void run() {
            if (mPostProgress != null && mPostProgress.isShowing()) {
                mPostProgress.dismiss();
                mPostProgress = null;
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DigiCareLogger.i(TAG, "ContactUsFragment : onCreate");
        // mTwitterProgresshandler = new Handler();
        // if (isConnectionAvailable())
        // requestCDLSData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.i(TAG, "ContactUsFragment : onCreateView: mView - "
                + mView);

        if (mView != null) {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        try {
            mView = inflater.inflate(R.layout.fragment_contact_us, container, false);
        } catch (InflateException e) {
        }

        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        DigiCareLogger.i(TAG,
                "ContactUsFragment : onActivityCreated : mConactUsParent == "
                        + mContactUsParent);
        // if (mContactUsParent == null) {
        mTwitterProgresshandler = new Handler();
        if (isConnectionAvailable())
            requestCDLSData();

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


        mSocialDivider = (View) getActivity().findViewById(R.id.socialDivider);
        // mFacebook.setOnClickListener(this);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);

        createSocialProviderMenu();

		/*
         * Live chat is configurable parameter. Developer can enable/disable it.
		 */
        if (getResources().getBoolean(R.bool.live_chat_required)) {
            mChat.setVisibility(View.VISIBLE);
        }
        mChat.setOnClickListener(this);
        mChat.setTransformationMethod(null);
        mCallPhilips.setOnClickListener(this);
        mCallPhilips.setTransformationMethod(null);
        mEmail.setOnClickListener(this);
        mEmail.setTransformationMethod(null);
        mParams = (FrameLayout.LayoutParams) mContactUsParent.getLayoutParams();

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_CONTACT_US,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
        config = getResources().getConfiguration();
    }

    private void createSocialProviderMenu() {
        TypedArray titles = getResources().obtainTypedArray(R.array.social_service_provider_menu_title);
        TypedArray resources = getResources().obtainTypedArray(R.array.social_service_provider_menu_resources);

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
        if (mActionBarMenuIcon != null && mActionBarArrow != null)
            if (mActionBarMenuIcon.getVisibility() == View.VISIBLE)
                enableActionBarLeftArrow();
        setViewParams(config);
    }


    private void enableActionBarLeftArrow() {
        mActionBarMenuIcon.setVisibility(View.GONE);
        mActionBarArrow.setVisibility(View.VISIBLE);
        mActionBarArrow.bringToFront();
    }

    /*
     * Forming CDLS url. This url will be different for US and other countries.
     */
    protected String formCdlsURL() {
        if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() == null)
            return null;

        ConsumerProductInfo consumerProductInfo = DigitalCareConfigManager
                .getInstance().getConsumerProductInfo();
        return getCdlsUrl(consumerProductInfo.getSector(),
                DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().toString(),
                consumerProductInfo.getCatalog(),
                consumerProductInfo.getSubCategory());
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

    protected void requestCDLSData() {
        DigiCareLogger.d(TAG, "CDLS Request Thread is started");
        startProgressDialog();
        if (formCdlsURL() != null)
            new RequestData(formCdlsURL(), this).execute();
    }

    @Override
    public void onResponseReceived(String response) {
        closeProgressDialog();
        DigiCareLogger.i(TAG, "response : " + response);
        if (response != null && isAdded()) {
            mCdlsResponseStr = response;
            parseCDLSResponse(response);
        } else {
            fadeoutButtons();
        }
    }

    protected void parseCDLSResponse(String response) {
        DigiCareLogger.d(TAG, "Parsing CDLS Response");
        CdlsResponseParser cdlsResponseParser = new CdlsResponseParser(
                mParsingCompletedCallback);
        cdlsResponseParser.parseCdlsResponse(response);
    }

    protected void updateUi() {
        DigiCareLogger.d(TAG, "Updating Contact Information");

        if (mCdlsParsedResponse.getSuccess()
                /*|| mCdlsParsedResponse.getError() != null*/) {
            CdlsPhoneModel phoneModel = mCdlsParsedResponse.getPhone();
            if (phoneModel != null) {
                if(phoneModel.getPhoneNumber()!= null){
                    mCallPhilips.setVisibility(View.VISIBLE);
                }
                enableBottomText();
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(phoneModel.getOpeningHoursWeekdays())
                        .append(phoneModel.getOpeningHoursSaturday())
                        .append(phoneModel.getOpeningHoursSunday())
                        .append(phoneModel.getOptionalData1())
                        .append(phoneModel.getOptionalData2())
                        .append("\n" + phoneModel.getmPhoneTariff());
                enableBottomText();
                mCallPhilips
                        .setText(getResources().getString(R.string.call_number)
                                + " "
                                + mCdlsParsedResponse.getPhone()
                                .getPhoneNumber());
                mFirstRowText.setText(stringBuilder);
            }

            if (hasEmptyChatContent(mCdlsParsedResponse)) {
                mChat.setBackgroundResource(R.drawable.selector_option_button_faded_bg);
                mChat.setEnabled(false);
            }
        } else if (mCdlsParsedResponse.getError() != null) {
            fadeoutButtons();
        } else {
            fadeoutButtons();
        }

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
        DigiCareLogger.v(TAG, "Progress Dialog Cancelled");

        if (mDialog != null && mDialog.isShowing()) {
            mDialog.dismiss();
            mDialog.cancel();
            mDialog = null;
        }
    }

    protected void startProgressDialog() {
        DigiCareLogger.v(TAG, "Progress Dialog Started");
        if (mDialog == null)
            mDialog = new ProgressDialog(getActivity());
        mDialog.setMessage(getActivity().getResources().getString(
                R.string.loading));
        if (!(getActivity().isFinishing())) {
            mDialog.show();
        }
    }

    ;

    protected void callPhilips() {
        try {
            Intent myintent = new Intent(Intent.ACTION_CALL);
            myintent.setData(Uri.parse("tel:"
                    + mCdlsParsedResponse.getPhone().getPhoneNumber()));
            myintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(myintent);
        }catch(NullPointerException e)
        {
           // DigiCareLogger.d(TAG, "on Call Click : "+ mCdlsParsedResponse.getPhone().getPhoneNumber());
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        String tag = (String) view.getTag();

        boolean actionTaken = false;

        if (tag != null) {
            actionTaken = DigitalCareConfigManager.getInstance()
                    .getSocialProviderListener()
                    .onSocialProviderItemClicked(tag.toString());
        }
        if (actionTaken) {
            return;
        }

        if (id == R.id.contactUsChat && isConnectionAvailable()) {
            if (mCdlsResponseStr == null) {
                showAlert(getActivity().getString(R.string.no_data));
                return;
            } else if (mCdlsParsedResponse != null
                    && !mCdlsParsedResponse.getSuccess()) {
                showAlert(getActivity().getString(R.string.no_data));
                return;
            }
            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CHAT);
            showFragment(new ChatFragment());
        } else if (id == R.id.contactUsCall) {
            if (mCdlsResponseStr == null) {
                showAlert(getActivity().getString(R.string.no_data));
                return;
            } else if (mCdlsParsedResponse != null
                    && !mCdlsParsedResponse.getSuccess()) {
                showAlert(getActivity().getString(R.string.no_data));
                return;
            } else if (Utils.isSimAvailable(getActivity())) {
                tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CALL);
                callPhilips();
            } else if (!Utils.isSimAvailable(getActivity())) {
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

        } else if (id == R.id.contactUsEmail) {
            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_EMAIL);
            sendEmail();
        }
    }

    private void launchFacebookFeature() {

        try {
            Uri uri = Uri.parse("fb://page/"
                    + getActivity().getResources().getString(
                    R.string.facebook_product_pageID));
            Map<String, Object> contextData = new HashMap<String, Object>();
            contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE, AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_EXIT_LINK, uri + toString());
            AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_EXIT_LINK, contextData);

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
        Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        tweetIntent.putExtra(Intent.EXTRA_TEXT, getProductInformation());
        tweetIntent.setType("text/plain");

        PackageManager packManager = getActivity().getPackageManager();
        List<ResolveInfo> resolvedInfoList = packManager.queryIntentActivities(tweetIntent, PackageManager.MATCH_DEFAULT_ONLY);

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
            String twitterPageName = "www.twitter.com/" + "@" + getActivity().getString(R.string.twitter_page);
            Map<String, Object> contextData = new HashMap<String, Object>();
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
        return "@" + getActivity().getString(R.string.twitter_page) + " " + getActivity().getResources().getString(
                R.string.support_productinformation)
                + " "
                + DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getProductTitle()
                + " "
                + DigitalCareConfigManager.getInstance()
                .getConsumerProductInfo().getCtn();
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
//                    .setBackgroundResource(R.drawable.selector_option_button_faded_bg);
//            mCallPhilips.setEnabled(false);
        }
      if (mChat != null) {
            mChat.setVisibility(View.GONE);
        }
    }
    protected void fadeinButtons() {
        tagTechnicalError();
//        if (mCallPhilips != null) {

//            mCallPhilips
//                    .setBackgroundResource(R.drawable.selector_option_button_faded_bg);
//            mCallPhilips.setEnabled(false);
//        }
        if (mChat != null) {
            mChat.setVisibility(View.VISIBLE);
        }
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
        return getResources().getDrawable(resId);
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

        String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);
        String buttonDrawable = getResources().getResourceEntryName(buttonDrawableResId);

        String packageName = getActivity().getPackageName();
        int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);
        int drawable = getResources().getIdentifier(
                packageName + ":drawable/" + buttonDrawable, null, null);
        float density = getResources().getDisplayMetrics().density;

        RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        Button button = createButton(density, title, drawable);
        relativeLayout.addView(button);
        setButtonParams(button, density);
        setHelpButtonParams(density);
        mSocialProviderParent.addView(relativeLayout);
        setRelativeLayoutParams(relativeLayout, density);

		/*
         * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
        relativeLayout.setTag(buttonTitle);
        relativeLayout.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        relativeLayout.setLayoutParams(params);
        relativeLayout
                .setBackground(getDrawable(R.drawable.prod_reg_social_border_btn));

        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();
        param.topMargin = (int) (15 * density);
        relativeLayout.setLayoutParams(param);
    }

    @SuppressLint("NewApi")
    private Button createButton(float density, int title, int resId) {
        Button button = new Button(getActivity(), null, R.style.fontButton);

        button.setGravity(Gravity.CENTER);
        // button.setPadding((int) (80 * density), 0, 0, 0);
        button.setTextAppearance(getActivity(), R.style.fontButton);
        Typeface buttonTypeface = Typeface.createFromAsset(getActivity().getAssets(), "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
        button.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        button.setBackground(getDrawable(resId));
        button.setText(title);
        return button;
    }

    private void setButtonParams(Button button, float density) {
        // RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
        // .getLayoutParams();
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);
    }

    private void setHelpButtonParams(float density) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
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
}

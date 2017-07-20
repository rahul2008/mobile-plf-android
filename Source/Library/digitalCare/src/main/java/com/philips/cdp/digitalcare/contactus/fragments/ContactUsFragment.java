/**
 * ContactUsFragment will help to provide options to contact Philips.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 19 Jan 2015
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.contactus.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.contactus.models.CdlsPhoneModel;
import com.philips.cdp.digitalcare.contactus.models.CdlsResponseModel;
import com.philips.cdp.digitalcare.contactus.parser.CdlsParsingCallback;
import com.philips.cdp.digitalcare.contactus.parser.CdlsResponseParser;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.request.RequestData;
import com.philips.cdp.digitalcare.request.ResponseCallback;
import com.philips.cdp.digitalcare.social.facebook.FacebookWebFragment;
import com.philips.cdp.digitalcare.social.twitter.TwitterWebFragment;
import com.philips.cdp.digitalcare.util.CommonRecyclerViewAdapter;
import com.philips.cdp.digitalcare.util.MenuItem;
import com.philips.cdp.digitalcare.util.Utils;
import com.philips.platform.uid.view.widget.Label;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.digitalcare.util.DigitalCareConstants;
import com.philips.cdp.digitalcare.util.Utils;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.shamanland.fonticon.FontIconDrawable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class ContactUsFragment extends DigitalCareBaseFragment implements OnClickListener, ResponseCallback, Observer {
    private static final String USER_PREFERENCE = "user_product";
    private static final String USER_SELECTED_PRODUCT_CTN_CALL = "contact_call";
    private static final String USER_SELECTED_PRODUCT_CTN_HOURS = "contact_hours";
    private boolean isFirstTimeCdlsCall = true;
    private SharedPreferences prefs = null;
    private Button mChatBtn = null;
    private Button mCallPhilipsBtn = null;
    private CdlsResponseModel mCdlsParsedResponse = null;
    private TextView mFirstRowText = null;
    private TextView mContactUsOpeningHours = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private ProgressDialog mPostProgress = null;
    private RecyclerView mContactUsSocilaProviderButtonsParent = null;
    private LinearLayout.LayoutParams mSecondContainerParams = null;
    private LinearLayout mLLSocialParent = null;
    private ProgressDialog mDialog = null;
    private static String TAG = ContactUsFragment.class.getSimpleName();

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
    private Utils mUtils = null;
    private AlertDialog mAlertDialog = null;

    private boolean isCdlsResponseNull(CdlsResponseModel response) {
        return response == null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isFirstTimeCdlsCall = true;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        prefs = getActivity().getSharedPreferences(
                USER_PREFERENCE, Context.MODE_PRIVATE);
        View view = inflater.inflate(R.layout.consumercare_fragment_contact_us, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initialiseSD();
        mContactUsSocilaProviderButtonsParent = (RecyclerView) getActivity().findViewById(
                R.id.contactUsSocialProvideButtonsParent);
        mSecondContainerParams = (LinearLayout.LayoutParams) mContactUsSocilaProviderButtonsParent
                .getLayoutParams();

        mChatBtn = (Button) getActivity().findViewById(
                R.id.contactUsChat);
        mCallPhilipsBtn = (Button) getActivity().findViewById(
                R.id.contactUsCall);
        mContactUsOpeningHours = (TextView) getActivity().findViewById(
                R.id.contactUsOpeningHours);
        mFirstRowText = (TextView) getActivity()
                .findViewById(R.id.firstRowText);

        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        mLLSocialParent = (LinearLayout) getActivity().findViewById(R.id.contactUsSocialParent);
        mUtils = new Utils();
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);


        final float density = getResources().getDisplayMetrics().density;
        setHelpButtonParams(density);
        //Live chat is configurable parameter. Developer can enable/disable it.
        if (getResources().getBoolean(R.bool.live_chat_required)) {
            mChatBtn.setVisibility(View.VISIBLE);
        }
        mChatBtn.setOnClickListener(this);
        mChatBtn.setTransformationMethod(null);
        mCallPhilipsBtn.setOnClickListener(this);
        mCallPhilipsBtn.setTransformationMethod(null);

        if (isInternetAvailable && isCdlsUrlNull()) {
            requestCdlsData();
        } else {
            String contactNumber = prefs.getString(USER_SELECTED_PRODUCT_CTN_CALL, "");
            String hours = prefs.getString(USER_SELECTED_PRODUCT_CTN_HOURS, "");
            if (mFirstRowText != null && isContactHoursCached()) {
                mFirstRowText.setVisibility(View.VISIBLE);
                mFirstRowText.setText(hours);
            }
            if (mCallPhilipsBtn != null && isContactNumberCached()) {
                mCallPhilipsBtn.setVisibility(View.VISIBLE);
                mContactUsOpeningHours.setVisibility(View.VISIBLE);
                mCallPhilipsBtn.setText(getResources().getString(R.string.call_number)
                        + " "
                        + contactNumber);
            }

        }
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                    (AnalyticsConstants.PAGE_CONTACT_US,
                            getPreviousName(), getPreviousName());

    config = getResources().getConfiguration();

    createContactUsSocialProvideMenu();
    setViewParams(config);
}

    private void initialiseSD() {

        ArrayList<String> var1 = new ArrayList<>();
        var1.add(DigitalCareConstants.SERVICE_ID_CC_CDLS);
        var1.add(DigitalCareConstants.SERVICE_ID_CC_EMAILFROMURL);

        final HashMap<String,String> hm=new HashMap<String,String>();

        hm.put(DigitalCareConstants.KEY_PRODUCT_SECTOR, DigitalCareConfigManager.getInstance().getConsumerProductInfo().getSector());
        hm.put(DigitalCareConstants.KEY_PRODUCT_CATALOG, DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCatalog());
        hm.put(DigitalCareConstants.KEY_PRODUCT_CATEGORY, DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCategory());
        hm.put(DigitalCareConstants.KEY_APPNAME, getAppName());


        DigitalCareConfigManager.getInstance().getAPPInfraInstance().getServiceDiscovery().getServicesWithCountryPreference(var1, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
            @Override
            public void onSuccess(Map<String, ServiceDiscoveryService> map) {

                ServiceDiscoveryService serviceDiscoveryService = map.get("cc.emailformurl");
                if(serviceDiscoveryService != null){
                    DigitalCareConfigManager.getInstance().setEmailUrl(serviceDiscoveryService.getConfigUrls());
                    DigiCareLogger.v(TAG,"Response from Service Discovery : Service ID : 'cc.emailformurl' - "+serviceDiscoveryService.getConfigUrls());
                }

                if(DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCategory() != null && !DigitalCareConfigManager.getInstance().getConsumerProductInfo().getCategory().isEmpty()) {
                    serviceDiscoveryService = map.get("cc.cdls");
                    if (serviceDiscoveryService != null) {
                        DigitalCareConfigManager.getInstance().setCdlsUrl(serviceDiscoveryService.getConfigUrls());
                        DigiCareLogger.v(TAG, "Response from Service Discovery : Service ID : 'cc.cdls' - " + serviceDiscoveryService.getConfigUrls());
                    }
                }

            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
                DigiCareLogger.v(TAG,"Error Response from Service Discovery :"+s);
                DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo(AnalyticsConstants.ACTION_SET_ERROR, AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR, s);
            }
        }, hm);

    }


    protected boolean isContactNumberCached() {
        String customerSupportNumber = prefs.getString(USER_SELECTED_PRODUCT_CTN_CALL, "");
        return (customerSupportNumber != null && customerSupportNumber != "");
    }


    protected boolean isContactHoursCached() {
        String contactHours = prefs.getString(USER_SELECTED_PRODUCT_CTN_HOURS, "");
        return (contactHours != null && contactHours != "");
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
        return DigitalCareConfigManager.getInstance().getCdlsUrl();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        setViewParams(config);
    }

    protected void requestCdlsData() {
        startProgressDialog();
        if (isCdlsUrlNull()) {
            String url = getCdlsUrl();
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
        if (isAdded()) {
            closeProgressDialog();
            if (response != null && isAdded()) {
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
        if (mCdlsParsedResponse.getSuccess()) {
            final CdlsPhoneModel phoneModel = mCdlsParsedResponse.getPhone();
            if (phoneModel != null) {
                if (phoneModel.getPhoneNumber() != null) {
                    mCallPhilipsBtn.setVisibility(View.VISIBLE);
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
                mCallPhilipsBtn
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
        } else if (isCdlsResponseModelNull()) {
            fadeoutButtons();
        } else {
            fadeoutButtons();
        }

    }

    private boolean isCdlsResponseModelNull() {
        return mCdlsParsedResponse.getError() != null;
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
                        .getCcListener()
                        .onSocialProviderItemClicked(tag.toString());
            }
        } catch (NullPointerException exception) {
        }
        if (actionTaken) {
            return;
        }

        if (id == R.id.contactUsChat && isConnectionAvailable()) {
            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CHAT);
            showFragment(new ChatFragment());
        } else if (id == R.id.contactUsCall) {
            if (!isContactNumberCached()) {
                showDialog(getActivity().getString(R.string.no_data));
            } else if (isSimAvailable() && !isTelephonyEnabled()){
                //show alert
                showDialog(getActivity().getString(R.string.no_call_functionality));
            } else if (isSimAvailable()) {
                tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_CALL);
                callPhilips();
            } else if (!isSimAvailable()) {
                showDialog(getActivity().getString(R.string.check_sim));
            }
            else {
                showDialog(getActivity().getString(R.string.check_sim));
            }
        } else if (tag != null
                && tag.equalsIgnoreCase(getStringKey(R.string.facebook))
                && isConnectionAvailable()) {

            launchFacebookFeature();

        } else if (tag != null
                && tag.equalsIgnoreCase(getStringKey(R.string.twitter))
                && isConnectionAvailable()) {
            launchTwitterFeature();


        } else if (tag != null && (tag.equalsIgnoreCase(getStringKey(R.string.send_email))) && isConnectionAvailable()) {

            tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_EMAIL);
            sendEmail();
        }
    }

    private boolean isSimAvailable() {
        return mUtils.isSimAvailable(getActivity());
    }

    private boolean isTelephonyEnabled(){
        return mUtils.isTelephonyEnabled(getActivity());
    }

    private void launchFacebookFeature() {
        tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_Facebook);
        try {
            final Uri uri = Uri.parse("fb://page/"
                    + getActivity().getResources().getString(
                    R.string.facebook_product_pageID));
            final Map<String, String> contextData = new HashMap<String, String>();
            contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL,
                    AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE,
                    AnalyticsConstants.ACTION_VALUE_FACEBOOK);
            contextData.put(AnalyticsConstants.ACTION_KEY_EXIT_LINK,
                    uri + toString());
            DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                    (AnalyticsConstants.ACTION_EXIT_LINK,
                            contextData);

            getActivity().getApplicationContext().getPackageManager()
                    .getPackageInfo("com.facebook.katana", 0);
            startActivity(new Intent(Intent.ACTION_VIEW, uri));
        } catch (Exception e) {
            showFragment(new FacebookWebFragment());
        }
    }

    protected void launchTwitterFeature() {
        tagServiceRequest(AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
        final Intent tweetIntent = new Intent(Intent.ACTION_SEND);
        String information = getProductInformation();
        tweetIntent.putExtra(Intent.EXTRA_TEXT, information);
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
            trackTwitterAction();
            startActivity(tweetIntent);
        } else {
            showFragment(new TwitterWebFragment());
        }
    }

    private void trackTwitterAction() {
        final String twitterUrl = "www.twitter.com/";
        final String twitterSupportAccount = getActivity().getString(R.string.twitter_page);
        final String twitterPageName = twitterUrl + "@" + twitterSupportAccount;
        final Map<String, String> contextData = new HashMap<String, String>();
        contextData.put(AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
        contextData.put(AnalyticsConstants.ACTION_KEY_SOCIAL_TYPE, AnalyticsConstants.ACTION_VALUE_SERVICE_CHANNEL_TWITTER);
        contextData.put(AnalyticsConstants.ACTION_KEY_EXIT_LINK, twitterPageName);
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackPageWithInfo
                (AnalyticsConstants.ACTION_EXIT_LINK, contextData);
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
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                (AnalyticsConstants.ACTION_SERVICE_REQUEST,
                        AnalyticsConstants.ACTION_KEY_SERVICE_CHANNEL, serviceChannel);
    }

    private void tagTechnicalError() {
        DigitalCareConfigManager.getInstance().getTaggingInterface().trackActionWithInfo
                (AnalyticsConstants.ACTION_SET_ERROR,
                        AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR,
                        AnalyticsConstants.ACTION_VALUE_TECHNICAL_ERROR_RESPONSE_CDLS);
    }

    protected void enableBottomText() {
        mCallPhilipsBtn.setVisibility(View.VISIBLE);
        mContactUsOpeningHours.setVisibility(View.VISIBLE);
        mFirstRowText.setVisibility(View.VISIBLE);
    }

    protected void fadeoutButtons() {
        tagTechnicalError();
        mCallPhilipsBtn.setVisibility(View.GONE);
        TypedArray titles = getResources().obtainTypedArray
                (R.array.social_service_provider_menu_title);
        boolean isSocialButtonsEnabled = titles.length() > 0 ? true : false;
        if (!isSocialButtonsEnabled && DigitalCareConfigManager.getInstance().getEmailUrl() == null) {
            showDialog(getResources().getString(R.string.NO_SUPPORT_KEY));
        }
    }

    @Override
    public void setViewParams(Configuration config) {

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
        } else {
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
        }
        mContactUsSocilaProviderButtonsParent.setLayoutParams(mSecondContainerParams);
    }

    protected void sendEmail() {
        showFragment(new EmailFragment());
    }

    private String getStringKey(int resId) {
        return getResources().getResourceEntryName(resId);
    }

    private void setHelpButtonParams(float density) {

        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        params.topMargin = (int) getActivity().getResources().getDimension(R.dimen.marginTopButton);
        mCallPhilipsBtn.setLayoutParams(params);
        mChatBtn.setLayoutParams(params);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.contact_us);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_CONTACT_US;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(null != mAlertDialog && mAlertDialog.isShowing())
            mAlertDialog.cancel();
    }

    @Override
    public void update(Observable observable, Object data) {
        if (!(getActivity() == null)) {
            requestCdlsData();
        }
    }

    private void showDialog(String message){
        mAlertDialog = new AlertDialog.Builder(getActivity(), R.style.alertDialogStyle)
                .setTitle(null)
                .setMessage(message)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                mAlertDialog.dismiss();

                            }
                        }).show();

    }

    private void createContactUsSocialProvideMenu() {
        final ContactUsFragment context = this;

        TypedArray titles = getResources().obtainTypedArray(R.array.social_service_provider_menu_title);
        TypedArray resources = getResources().obtainTypedArray(R.array.social_service_provider_menu_resources);
        ArrayList<MenuItem> menus = new ArrayList<>();

        if (DigitalCareConfigManager.getInstance().getEmailUrl() != null){
            menus.add(new MenuItem(R.string.icon_dls_questionmark, R.string.send_email));
        }

        if(!Utils.isCountryChina())
            for (int i = 0; i < titles.length(); i++) {
                menus.add(new MenuItem(resources.getResourceId(i, 0), titles.getResourceId(i, 0)));
            }

        if(menus.size() == 0){
            hideSocialView();
        }

        updateRecyclerView(context, menus);
    }

    private void hideSocialView() {
        mLLSocialParent.setVisibility(View.GONE);
        View view = (View) getActivity().findViewById(R.id.dividerContactUsSplit);
        view.setVisibility(View.GONE);
    }

    private void updateRecyclerView(final ContactUsFragment context, final ArrayList<MenuItem> menus) {
        RecyclerView recyclerView = mContactUsSocilaProviderButtonsParent;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        recyclerView.setAdapter(new CommonRecyclerViewAdapter<MenuItem>(menus, R.layout.consumercare_icon_button) {
            @Override
            public void bindData(RecyclerView.ViewHolder holder, MenuItem item) {
                View container = holder.itemView.findViewById(R.id.icon_button);
                Label label = (Label) container.findViewById(R.id.icon_button_text);
                label.setText(item.mText);
                TextView icon = (TextView) container.findViewById(R.id.icon_button_icon);
                icon.setText(item.mIcon);
                container.setTag(getResources().getResourceEntryName(item.mText));
                container.setOnClickListener(context);
            }
        });
    }

}

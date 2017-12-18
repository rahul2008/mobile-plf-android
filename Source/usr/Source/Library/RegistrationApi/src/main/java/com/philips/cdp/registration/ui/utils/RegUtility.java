/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */

package com.philips.cdp.registration.ui.utils;

import android.app.*;
import android.content.*;
import android.os.*;
import android.support.annotation.*;
import android.support.v4.content.*;
import android.text.*;
import android.text.method.*;
import android.text.style.*;
import android.widget.*;

import com.philips.cdp.registration.*;
import com.philips.cdp.registration.configuration.*;
import com.philips.cdp.registration.events.*;
import com.philips.platform.appinfra.abtestclient.*;

import org.json.*;

import java.util.*;


public class RegUtility {
    private static long createAccountStartTime;

    private static UIFlow uiFlow;

    public static void setUiFlow(UIFlow uiFlowOverride) {
        uiFlow = uiFlowOverride;
    }

    public static int getCheckBoxPadding(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int padding;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN ||
                android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            padding = (int) (35 * scale + 0.5f);
        } else {
            padding = (int) (10 * scale + 0.5f);
        }
        return padding;
    }


    //Temp: Code (For the existing usage on other fragments)
    public static void linkifyTermsandCondition(
            TextView termsAndConditionsAcceptance,
            final Activity activity, ClickableSpan termsAndConditionClickListener) {

        String termsAndCondition = activity.getString(R.string.reg_TermsAndConditionsAcceptanceText);
        String acceptTermsAndCondition = activity.getString(R.string.reg_TermsAndConditionsText);
        termsAndCondition = String.format(termsAndCondition, acceptTermsAndCondition);
        termsAndConditionsAcceptance.setText(termsAndCondition);
        String terms = activity.getString(R.string.reg_TermsAndConditionsText);
        setupLinkify(termsAndConditionsAcceptance, activity, termsAndConditionClickListener, termsAndCondition, terms);
    }

    public static void linkifyTermsandCondition(
            CheckBox termsAndConditionsAcceptance,
            final Activity activity, ClickableSpan termsAndConditionClickListener) {

        String termsAndCondition = activity.getString(R.string.reg_DLS_TermsAndConditionsAcceptanceText);
        String acceptTermsAndCondition ="\n"+ activity.getString(R.string.reg_DLS_TermsAndConditionsText);
        termsAndCondition = String.format(termsAndCondition, acceptTermsAndCondition);
        termsAndConditionsAcceptance.setText(termsAndCondition);
        String terms = activity.getString(R.string.reg_DLS_TermsAndConditionsText);
        setupLinkify(termsAndConditionsAcceptance, activity, termsAndConditionClickListener, termsAndCondition, terms);
    }

    public static void linkifyPhilipsNews(TextView receivePhilipsNewsView,
                                          final Activity activity, ClickableSpan
                                                  receivePhilipsNewsClickListener) {
        String receivePhilipsNews = activity.getString(R.string.reg_DLS_OptIn_Promotional_Message_Line1);
        String doesThisMeanStr =  activity.getString(R.string.reg_Receive_Philips_News_Meaning_lbltxt);
        receivePhilipsNews = receivePhilipsNews +"\n" + doesThisMeanStr;
        receivePhilipsNewsView.setText(receivePhilipsNews);
        String link =  activity.getString(R.string.reg_Receive_Philips_News_Meaning_lbltxt);
        setupLinkify(receivePhilipsNewsView, activity, receivePhilipsNewsClickListener, receivePhilipsNews, link);
    }

    public static void linkifyPhilipsNewsMarketing(TextView receivePhilipsNewsView,
                                                   final Activity activity, ClickableSpan
                                                           receivePhilipsNewsClickListener) {
        String receivePhilipsNews = activity.getString(R.string.reg_DLS_OptIn_Promotional_Message_Line1);
        String doesThisMeanStr =  activity.getString(R.string.reg_Receive_Philips_News_Meaning_lbltxt);
        receivePhilipsNews = receivePhilipsNews +"\n" + doesThisMeanStr;
        receivePhilipsNewsView.setText(receivePhilipsNews);
        String link = "\n" + activity.getString(R.string.reg_Receive_Philips_News_Meaning_lbltxt);
        setupLinkify(receivePhilipsNewsView, activity, receivePhilipsNewsClickListener, receivePhilipsNews, link);
    }

    public static void linkifyAccountSettingPhilips(
            TextView accountSettingPhilipsNews, final Activity activity,
            ClickableSpan accountSettingsPhilipsClickListener) {
        String moreAccountSettings = activity.getString(R.string.reg_Access_More_Account_Setting_lbltxt);
        String doesThisMeanStr = activity.getString(R.string.reg_Philips_URL_txt);
        moreAccountSettings = String.format(moreAccountSettings, doesThisMeanStr);
        accountSettingPhilipsNews.setText(moreAccountSettings);
        String link = activity.getString(R.string.reg_Philips_URL_txt);
        setupLinkify(accountSettingPhilipsNews, activity, accountSettingsPhilipsClickListener, moreAccountSettings, link);
    }

    private static void setupLinkify(TextView accountSettingPhilipsNews, Activity activity,
                                     ClickableSpan accountSettingsPhilipsClickListener,
                                     String moreAccountSettings, String link) {
        SpannableString spanableString = new SpannableString(moreAccountSettings);
        int termStartIndex = moreAccountSettings.toLowerCase().indexOf(
                link.toLowerCase());
        spanableString.setSpan(accountSettingsPhilipsClickListener, termStartIndex,
                termStartIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        //   removeUnderlineFromLink(spanableString);
        accountSettingPhilipsNews.setText(spanableString);
        accountSettingPhilipsNews.setMovementMethod(LinkMovementMethod.getInstance());
        accountSettingPhilipsNews.setLinkTextColor(ContextCompat.getColor
                (activity, R.color.reg_hyperlink_highlight_color));
        accountSettingPhilipsNews.setHighlightColor(ContextCompat.getColor
                (activity, android.R.color.transparent));
    }


    private static void removeUnderlineFromLink(SpannableString spanableString) {
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


    public static Configuration getConfiguration(String registrationEnv) {
        if (registrationEnv == null) {
            return Configuration.EVALUATION;
        }
        if (registrationEnv.equalsIgnoreCase(Configuration.DEVELOPMENT.getValue()))
            return Configuration.DEVELOPMENT;

        if (registrationEnv.equalsIgnoreCase(Configuration.PRODUCTION.getValue()))
            return Configuration.PRODUCTION;

        if (registrationEnv.equalsIgnoreCase(Configuration.STAGING.getValue()))
            return Configuration.STAGING;

        if (registrationEnv.equalsIgnoreCase(Configuration.TESTING.getValue()))
            return Configuration.TESTING;

        return Configuration.EVALUATION;
    }

    public static UIFlow getUiFlow() {

        if (uiFlow != null) {
            return uiFlow;
        }

        ABTestClientInterface abTestClientInterface = RegistrationConfiguration.getInstance().getComponent().getAbTestClientInterface();
        String flowType = abTestClientInterface.getTestValue(RegConstants.DOT_RECEIVE_MARKETING_OPT_IN, UIFlow.FLOW_A.getValue(),
                ABTestClientInterface.UPDATETYPES.ONLY_AT_APP_UPDATE, null);
        if (flowType.equalsIgnoreCase(UIFlow.FLOW_B.getValue())) {
            return UIFlow.FLOW_B;
        }
        return UIFlow.FLOW_A;
    }

    public static void checkIsValidSignInProviders(HashMap<String, ArrayList<String>> providers) {
        if (providers != null) {
            for (Map.Entry<String, ArrayList<String>> entry : providers.entrySet()) {
                String countryKeyCode = entry.getKey();
                ArrayList<String> value = entry.getValue();
                for (String val : value) {
                    if (providers.get(countryKeyCode).contains(SocialProvider.TWITTER)) {
                        throw new RuntimeException(SocialProvider.TWITTER +
                                " Provider is not supporting");
                    }
                }
            }
        }
    }

    public static long getCreateAccountStartTime() {
        return createAccountStartTime;
    }

    public static void setCreateAccountStartTime(long createAccountStartTime) {
        RegUtility.createAccountStartTime = createAccountStartTime;
    }

    /**
     * @param local : This parameter passes language and Country Code to Check weather Counter code
     *              is US or Not
     * @return Country code US return true  or False
     */
    public static boolean isCountryUS(String local) {
        if (local != null && local.length() == 5) {
            return local.substring(3, 5).equalsIgnoreCase(RegConstants.COUNTRY_CODE_US);
        }
        return false;
    }

    public static String[] getDefaultSupportedHomeCountries() {
        return defaultSupportedHomeCountries;
    }

    private static String[] defaultSupportedHomeCountries = new String[]{"RW", "BG", "CZ", "DK", "AT", "CH", "DE", "GR", "AU", "CA", "GB", "HK", "ID", "IE", "IN", "MY", "NZ", "PH", "PK", "SA", "SG", "US", "ZA", "AR", "CL", "CO", "ES", "MX", "PE", "EE", "FI", "BE", "FR", "HR", "HU", "IT", "JP", "KR", "LT", "LV", "NL", "NO", "PL", "BR", "PT", "RO", "RU", "UA", "SI", "SK", "SE", "TH", "TR", "VN", "CN", "TW"};


    public static String getErrorMessageFromInvalidField(JSONObject serverResponse) {
        try {
            JSONObject jsonObject = (JSONObject) serverResponse.get(RegConstants.INVALID_FIELDS);
            if (jsonObject != null) {
                jsonObject.keys();
                List<String> keys = new ArrayList<String>();
                Iterator<?> i = jsonObject.keys();
                do {
                    String k = i.next().toString();
                    keys.add(k);
                } while (i.hasNext());

                StringBuilder stringBuilder = new StringBuilder();
                for (int j = 0; j < keys.size(); j++) {
                    JSONArray jsonObject1 = (JSONArray) jsonObject.opt(keys.get(j));
                    stringBuilder.append(jsonObject1.getString(0)).append("\n");
                }
                return stringBuilder.toString();
            }
        } catch (Exception e) {
            //NOP
        }
        return null;
    }


    public static List<String> supportedCountryList() {
        ArrayList<String> defaultCountries = new ArrayList<String>(Arrays.asList(RegUtility.getDefaultSupportedHomeCountries()));
        List<String> supportedHomeCountries = RegistrationConfiguration.getInstance().getSupportedHomeCountry();
        List<String> serviceDiscoveryCountries = RegistrationConfiguration.getInstance().getServiceDiscoveryCountries();
        if (serviceDiscoveryCountries != null && serviceDiscoveryCountries.size() > 0) {
            for (String country : serviceDiscoveryCountries) {
                String countryCode = country.toUpperCase();
                if (!defaultCountries.contains(countryCode)) {
                    defaultCountries.add(countryCode);
                }
                if (null != supportedHomeCountries && !supportedHomeCountries.contains(countryCode)) {
                    supportedHomeCountries.add(countryCode);
                }
            }
        }
        if (null != supportedHomeCountries) {
            List<String> filteredCountryList = new ArrayList<String>(supportedHomeCountries);
            filteredCountryList.retainAll(defaultCountries);
            if (filteredCountryList.size() > 0) {
                return filteredCountryList;
            }
        }
        return defaultCountries;
    }

    @NonNull
    public static String getFallbackCountryCode() {
        String fallbackCountry = RegistrationConfiguration.getInstance().getFallBackHomeCountry();
        String selectedCountryCode;
        if (null != fallbackCountry) {
            selectedCountryCode = fallbackCountry;
        } else {
            selectedCountryCode = RegConstants.COUNTRY_CODE_US;
        }
        return selectedCountryCode.toUpperCase();
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html){
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html,Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static void showErrorMessage(Activity parentActivity) {
        parentActivity.runOnUiThread(() -> Toast.makeText(parentActivity,parentActivity.getResources().getString(R.string.reg_Generic_Network_Error),Toast.LENGTH_SHORT).show());
    }
}

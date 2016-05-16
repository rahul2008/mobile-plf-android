package com.philips.cdp.registration.ui.utils;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.style.UnderlineSpan;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.configuration.Configuration;
import com.philips.cdp.registration.events.SocialProvider;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by 310190722 on 8/6/2015.
 */
public class RegUtility {


    private static final String FILE_NAME = "FILE_NAME";
    private static final String TRADITIONAL_PASSWORD_ID = "TRADITIONAL_PASSWORD_ID";
    private static long createAccountStartTime;

    public static int getCheckBoxPadding(Context context) {
        float scale = context.getResources().getDisplayMetrics().density;
        int padding;
        if (android.os.Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN || android.os.Build.VERSION.SDK_INT == Build.VERSION_CODES.JELLY_BEAN_MR1) {
            padding = (int) (35 * scale + 0.5f);
        } else {
            padding = (int) (10 * scale + 0.5f);
        }
        return padding;
    }



    public static void linkifyTermsandCondition(TextView termsAndConditionsAcceptance, final Activity activity, ClickableSpan termsAndConditionClickListener) {

        String termsAndCondition = activity.getString(R.string.TermsAndConditionsAcceptanceText);
        String acceptTermsAndCondition = activity.getString(R.string.TermsAndConditionsText);
        termsAndCondition = String.format(termsAndCondition, acceptTermsAndCondition);
        termsAndConditionsAcceptance.setText(termsAndCondition);
        String terms = activity.getString(R.string.TermsAndConditionsText);
        SpannableString spanableString = new SpannableString(termsAndCondition);

        int termStartIndex = termsAndCondition.toLowerCase().indexOf(
                terms.toLowerCase());
        spanableString.setSpan(termsAndConditionClickListener, termStartIndex, termStartIndex + terms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        removeUnderlineFromLink(spanableString);

        termsAndConditionsAcceptance.setText(spanableString);
        termsAndConditionsAcceptance.setMovementMethod(LinkMovementMethod.getInstance());
        termsAndConditionsAcceptance.setLinkTextColor(activity.getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        termsAndConditionsAcceptance.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));
    }

    public static void linkifyPhilipsNews(TextView receivePhilipsNewsView, final Activity activity, ClickableSpan receivePhilipsNewsClickListener) {

        String receivePhilipsNews = activity.getString(R.string.Receive_Philips_News_lbltxt);
        String doesThisMeanStr = activity.getString(R.string.Receive_Philips_News_Meaning_lbltxt);
        receivePhilipsNews = String.format(receivePhilipsNews, doesThisMeanStr);
        receivePhilipsNewsView.setText(receivePhilipsNews);
        String link = activity.getString(R.string.Receive_Philips_News_Meaning_lbltxt);
        SpannableString spanableString = new SpannableString(receivePhilipsNews);

        int termStartIndex = receivePhilipsNews.toLowerCase().indexOf(
                link.toLowerCase());
        spanableString.setSpan(receivePhilipsNewsClickListener, termStartIndex, termStartIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        removeUnderlineFromLink(spanableString);

        receivePhilipsNewsView.setText(spanableString);
        receivePhilipsNewsView.setMovementMethod(LinkMovementMethod.getInstance());
        receivePhilipsNewsView.setLinkTextColor(activity.getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        receivePhilipsNewsView.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));

    }

    public static void linkifyAccountSettingPhilips(TextView accountSettingPhilipsNews, final Activity activity, ClickableSpan accountSettingsPhilipsClickListener) {

        String moreAccountSettings = activity.getString(R.string.Access_More_Account_Setting_lbltxt);
        String doesThisMeanStr = activity.getString(R.string.Philips_URL_txt);

        moreAccountSettings = String.format(moreAccountSettings, doesThisMeanStr);
        accountSettingPhilipsNews.setText(moreAccountSettings);
        String link = activity.getString(R.string.Philips_URL_txt);
        SpannableString spanableString = new SpannableString(moreAccountSettings);

        int termStartIndex = moreAccountSettings.toLowerCase().indexOf(
                link.toLowerCase());
        spanableString.setSpan(accountSettingsPhilipsClickListener, termStartIndex, termStartIndex + link.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        removeUnderlineFromLink(spanableString);

        accountSettingPhilipsNews.setText(spanableString);
        accountSettingPhilipsNews.setMovementMethod(LinkMovementMethod.getInstance());
        accountSettingPhilipsNews.setLinkTextColor(activity.getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        accountSettingPhilipsNews.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));
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

    public static void handleTermsCondition(Activity activity) {
        RegistrationHelper.getInstance().getUserRegistrationListener()
                .notifyOnTermsAndConditionClickEventOccurred(activity);
    }


    public static Configuration getConfiguration(String registrationEnv) {
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

    public static void checkIsValidSignInProviders(HashMap<String, ArrayList<String>> providers) {
        if(providers!=null){
            for (Map.Entry<String, ArrayList<String>> entry : providers.entrySet()) {
                String countryKeyCode = entry.getKey();
                ArrayList<String> value = entry.getValue();
                for(String val : value){
                    if(providers.get(countryKeyCode).contains(SocialProvider.TWITTER)){
                        throw new RuntimeException( SocialProvider.TWITTER +" Provider is not supporting");
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
}

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
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.settings.RegistrationHelper;

import java.util.Locale;

/**
 * Created by 310190722 on 8/6/2015.
 */
public class RegUtility {


    private static final String FILE_NAME = "FILE_NAME";
    private static final String TRADITIONAL_PASSWORD_ID = "TRADITIONAL_PASSWORD_ID";

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

    public static void invalidalertvisibilitygone(View view){
        android.widget.RelativeLayout.LayoutParams userNameParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
        userNameParam.addRule(RelativeLayout.LEFT_OF, R.id.rl_reg_parent_verified_field);
        view.setLayoutParams(userNameParam);
    }

    public static void invalidalertvisibilityview(View view){
        android.widget.RelativeLayout.LayoutParams userNameParam = (RelativeLayout.LayoutParams) view.getLayoutParams();
        userNameParam.addRule(RelativeLayout.LEFT_OF, R.id.fl_reg_invalid_alert);
        view.setLayoutParams(userNameParam);
    }

    public static void linkifyTermsandCondition(TextView termsAndConditionsAcceptance, final Activity activity, ClickableSpan termsAndConditionClickListener) {

        String termsAndCondition = activity.getString(R.string.TermsAndConditionsAcceptanceText);
        String acceptTermsAndCondition = activity.getString(R.string.TermsAndConditionsText);
        termsAndCondition = String.format(termsAndCondition, acceptTermsAndCondition);
        termsAndConditionsAcceptance.setText(termsAndCondition);
        String terms = activity.getString(R.string.TermsAndConditionsText);
        SpannableString spanableString = new SpannableString(termsAndCondition);

        int termStartIndex = termsAndCondition.toLowerCase(Locale.getDefault()).indexOf(
                terms.toLowerCase(Locale.getDefault()));
        spanableString.setSpan(termsAndConditionClickListener, termStartIndex, termStartIndex + terms.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        removeUnderlineFromLink(spanableString);

        termsAndConditionsAcceptance.setText(spanableString);
        termsAndConditionsAcceptance.setMovementMethod(LinkMovementMethod.getInstance());
        termsAndConditionsAcceptance.setLinkTextColor(activity.getResources().getColor(
                R.color.reg_hyperlink_highlight_color));
        termsAndConditionsAcceptance.setHighlightColor(activity.getResources().getColor(android.R.color.transparent));
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
}

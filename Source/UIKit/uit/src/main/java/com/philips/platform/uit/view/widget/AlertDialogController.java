package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;

public class AlertDialogController {

    public static final String UID_ALERT_DAILOG_MESSAGE_KEY = "UID_ALERT_DAILOG_MESSAGE_KEY";
    public static final String UID_ALERT_DAILOG_TITLE_KEY = "UID_ALERT_DAILOG_TITLE_KEY";
    public static final String UID_ALERT_DAILOG_TITLE_ICON_KEY = "UID_ALERT_DAILOG_TITLE_ICON_KEY";
    public static final String UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY = "UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY";
    public static final String UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY = "UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY";

    public static class DialogParams {
        public String message;
        public String title;

        public View.OnClickListener positiveButtonLister;
        public View.OnClickListener negativeButtonListener;
        public Context context;
        @DrawableRes
        public int iconId;
        public Drawable iconDrawable;
        public boolean cancelable;
        public String negativeButtonText;
        public String positiveButtonText;
    }
}

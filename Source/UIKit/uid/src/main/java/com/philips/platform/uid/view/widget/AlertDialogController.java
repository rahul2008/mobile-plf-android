package com.philips.platform.uid.view.widget;

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
        private String message;
        private String title;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public View.OnClickListener getPositiveButtonLister() {
            return positiveButtonLister;
        }

        public void setPositiveButtonLister(View.OnClickListener positiveButtonLister) {
            this.positiveButtonLister = positiveButtonLister;
        }

        public View.OnClickListener getNegativeButtonListener() {
            return negativeButtonListener;
        }

        public void setNegativeButtonListener(View.OnClickListener negativeButtonListener) {
            this.negativeButtonListener = negativeButtonListener;
        }

        public Context getContext() {
            return context;
        }

        public void setContext(Context context) {
            this.context = context;
        }

        public int getIconId() {
            return iconId;
        }

        public void setIconId(int iconId) {
            this.iconId = iconId;
        }

        public Drawable getIconDrawable() {
            return iconDrawable;
        }

        public void setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
        }

        public boolean isCancelable() {
            return cancelable;
        }

        public void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        public String getNegativeButtonText() {
            return negativeButtonText;
        }

        public void setNegativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
        }

        public String getPositiveButtonText() {
            return positiveButtonText;
        }

        public void setPositiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
        }

        private View.OnClickListener positiveButtonLister;
        private View.OnClickListener negativeButtonListener;
        private Context context;
        @DrawableRes
        private int iconId;
        private Drawable iconDrawable;
        private boolean cancelable;
        private String negativeButtonText;
        private String positiveButtonText;
    }
}

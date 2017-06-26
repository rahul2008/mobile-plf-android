/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.view.View;

class AlertDialogController {

    static final String UID_ALERT_DAILOG_MESSAGE_KEY = "UID_ALERT_DAILOG_MESSAGE_KEY";
    static final String UID_ALERT_DAILOG_TITLE_KEY = "UID_ALERT_DAILOG_TITLE_KEY";
    static final String UID_ALERT_DAILOG_TITLE_ICON_KEY = "UID_ALERT_DAILOG_TITLE_ICON_KEY";
    static final String UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY = "UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY";
    static final String UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY = "UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY";

    static class DialogParams {
        private String message;
        private String title;
        private View.OnClickListener positiveButtonLister;
        private View.OnClickListener negativeButtonListener;
        private Context context;
        @DrawableRes
        private int iconId;
        private Drawable iconDrawable;
        private boolean cancelable;
        private String negativeButtonText;
        private String positiveButtonText;
        boolean hideNegativeButton;

        String getMessage() {
            return message;
        }

        void setMessage(String message) {
            this.message = message;
        }

        String getTitle() {
            return title;
        }

        void setTitle(String title) {
            this.title = title;
        }

        View.OnClickListener getPositiveButtonLister() {
            return positiveButtonLister;
        }

        void setPositiveButtonLister(View.OnClickListener positiveButtonLister) {
            this.positiveButtonLister = positiveButtonLister;
        }

        View.OnClickListener getNegativeButtonListener() {
            return negativeButtonListener;
        }

        void setNegativeButtonListener(View.OnClickListener negativeButtonListener) {
            this.negativeButtonListener = negativeButtonListener;
        }

        Context getContext() {
            return context;
        }

        void setContext(Context context) {
            this.context = context;
        }

        int getIconId() {
            return iconId;
        }

        void setIconId(int iconId) {
            this.iconId = iconId;
        }

        Drawable getIconDrawable() {
            return iconDrawable;
        }

        void setIconDrawable(Drawable iconDrawable) {
            this.iconDrawable = iconDrawable;
        }

        boolean isCancelable() {
            return cancelable;
        }

        void setCancelable(boolean cancelable) {
            this.cancelable = cancelable;
        }

        String getNegativeButtonText() {
            return negativeButtonText;
        }

        void setNegativeButtonText(String negativeButtonText) {
            this.negativeButtonText = negativeButtonText;
        }

        String getPositiveButtonText() {
            return positiveButtonText;
        }

        void setPositiveButtonText(String positiveButtonText) {
            this.positiveButtonText = positiveButtonText;
        }
    }
}

package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;

import com.philips.cdp.registration.R;

public class URNotification {

    private final Activity mActivity;
    private final NotificationType mNotificationType;

    public URNotification(Activity mActivity, NotificationType mNotificationType) {
        this.mActivity = mActivity;
        this.mNotificationType = mNotificationType;
    }

    public void showNotification(String message, String title) {

        switch (mNotificationType) {

            case INLINE:
                new XRegError(mActivity).setError(message);

                break;

            case NOTIFICATION_BAR:
                new NotificationBarView(mActivity).showError(message, title, mActivity.findViewById(R.id.usr_startScreen_baseLayout_LinearLayout));
                break;
            case DIALOG:
                break;
        }
    }

    public void hideNotification() {

        switch (mNotificationType) {

            case INLINE:
                new XRegError(mActivity).hideError();
                break;

            case NOTIFICATION_BAR:
                new NotificationBarView(mActivity).hidePopup();
            case DIALOG:
                break;
        }
    }


}

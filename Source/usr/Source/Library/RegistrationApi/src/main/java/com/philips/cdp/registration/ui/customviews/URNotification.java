package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;

import com.philips.cdp.registration.R;

public class URNotification {

    private final Activity mActivity;
    private final NotificationType mNotificationType;
    private NotificationBarView notificationBarView;
    private XRegError xRegError;

    public URNotification(Activity mActivity, NotificationType mNotificationType) {
        this.mActivity = mActivity;
        this.mNotificationType = mNotificationType;
        if (mNotificationType == NotificationType.NOTIFICATION_BAR)
            notificationBarView = new NotificationBarView(mActivity);
        else {
            xRegError = new XRegError(mActivity);
        }
    }

    public void showNotification(String message, String title) {

        switch (mNotificationType) {

            case INLINE:
                xRegError.setError(message);
                break;

            case NOTIFICATION_BAR:

                notificationBarView.showError(message, title, mActivity.findViewById(R.id.usr_startScreen_baseLayout_LinearLayout));
                break;
            case DIALOG:
                break;
        }
    }

    public void hideNotification() {

        switch (mNotificationType) {
            case INLINE:
                xRegError.hideError();
                break;

            case NOTIFICATION_BAR:
                notificationBarView.hidePopup();
            case DIALOG:
                break;
        }
    }


}

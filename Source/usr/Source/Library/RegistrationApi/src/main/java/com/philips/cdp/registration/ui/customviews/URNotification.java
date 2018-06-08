package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.errors.NotificationMessage;

import java.util.ArrayList;
import java.util.List;

public class URNotification {

    private final Activity mActivity;
    private NotificationType mNotificationType = NotificationType.NOTIFICATION_BAR;
    private NotificationBarView notificationBarView;
    private URNotificationInterface notificationInterface;

    public interface URNotificationInterface {
        void notificationInlineMsg(String msg);
    }

    public static List<Integer> INLINE_ERROR_CODE = new ArrayList<>();

    static {

        INLINE_ERROR_CODE.add(210);
        INLINE_ERROR_CODE.add(211);
        INLINE_ERROR_CODE.add(212);
        INLINE_ERROR_CODE.add(213);
        INLINE_ERROR_CODE.add(232);
        INLINE_ERROR_CODE.add(363);
        INLINE_ERROR_CODE.add(380);
        INLINE_ERROR_CODE.add(416);
        INLINE_ERROR_CODE.add(510);
        //URX Inline Error
        INLINE_ERROR_CODE.add(10);
        INLINE_ERROR_CODE.add(15);
        INLINE_ERROR_CODE.add(20);
        INLINE_ERROR_CODE.add(30);
        INLINE_ERROR_CODE.add(40);

    }

    public URNotification(Activity mActivity, URNotificationInterface notificationInterface) {

        this.mActivity = mActivity;
        this.notificationInterface = notificationInterface;
    }

    public void showNotification(NotificationMessage notificationMessage) {

        if (INLINE_ERROR_CODE.contains(notificationMessage.getErrorCode()))
            mNotificationType = NotificationType.INLINE;

        if (mNotificationType == NotificationType.NOTIFICATION_BAR)
            notificationBarView = new NotificationBarView(mActivity);

        switch (mNotificationType) {

            case INLINE:
                notificationInterface.notificationInlineMsg(notificationMessage.getMessage());
                break;

            case NOTIFICATION_BAR:
                notificationBarView.showError(notificationMessage.getMessage(), notificationMessage.getTitle(), mActivity.findViewById(R.id.usr_startScreen_baseLayout_LinearLayout));
                break;
        }
    }

    public void hideNotification() {
        switch (mNotificationType) {
            case NOTIFICATION_BAR:
                if(notificationBarView!=null)
                notificationBarView.hidePopup();
        }
    }


}

package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.ui.utils.NotificationBarHandler;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.uid.view.widget.Button;

import org.greenrobot.eventbus.EventBus;

public class NotificationBarView {

    private static final String TAG = NotificationBarView.class.getSimpleName();
    private final Activity mActivity;
    private Button showHideNotification;
    private boolean wasInfoNotificationShown;
    private View notificationView;
    private View infoNotificationView;
    private TextView gotIButton;
    private PopupWindow popupWindow;

    public NotificationBarView(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void showError(String msg, String title, View baseLayoutViewLocation) {
        if (popupWindow == null) {
            View view = getNotificationContentView(msg, title);
            popupWindow = new PopupWindow(mActivity);
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(true);
            popupWindow.setContentView(view);

        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (popupWindow != null) {
                popupWindow.showAtLocation(baseLayoutViewLocation, Gravity.TOP, 0, 0);
            }
        }
    }

    void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    private View getNotificationContentView(String title, String message) {
        RLog.i(TAG, "getNotificationContentView : isCalled");
        View view = View.inflate(mActivity, R.layout.reg_notification_bg_accent, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(title);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(message);
        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new NotificationBarHandler());
            }
        });
        return view;
    }
}

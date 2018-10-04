package com.philips.cdp.registration.ui.customviews;

import android.app.Activity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.philips.cdp.registration.R;
import com.philips.cdp.registration.events.EventHelper;
import com.philips.cdp.registration.ui.utils.RLog;
import com.philips.platform.uid.utils.UIDUtils;

public class NotificationBarView {

    private static final String TAG = "NotificationBarView";
    private final Activity mActivity;
    private PopupWindow popupWindow;

    NotificationBarView(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void showError(String msg, String title, View baseLayoutViewLocation) {
        if (popupWindow == null) {
            View view = getNotificationContentView(msg, title, true);
            popupWindow = new PopupWindow(mActivity);
            popupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
            popupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
            popupWindow.setFocusable(false);
            popupWindow.setContentView(view);

        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            if (popupWindow != null) {
                popupWindow.showAtLocation(baseLayoutViewLocation, Gravity.NO_GRAVITY, 0, UIDUtils.getActionBarHeight(mActivity) + UIDUtils.getStatusBarHeight(mActivity));
            }
        }
    }

    void hidePopup() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }

    boolean isNotificationBarViewShowing() {
        return popupWindow != null && popupWindow.isShowing();
    }

    private View getNotificationContentView(String title, String message, boolean isError) {
        RLog.i(TAG, "getNotificationContentView : is called ");
        final View view;
        if (isError)
            view = View.inflate(mActivity, R.layout.reg_notification_error_bg, null);
        else
            view = View.inflate(mActivity, R.layout.reg_notification_bg_accent, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(title);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(message);
        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setOnClickListener(v -> {
            //hidePopup();
            EventHelper.getInstance().notifyEventOccurred("A");
        });
        return view;
    }
}

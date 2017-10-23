package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentNotificationBarBinding;
import com.philips.platform.catalogapp.events.InfoMessageDismissedEvent;
import com.philips.platform.catalogapp.events.OptionMenuClickedEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationBarFragment extends BaseFragment {

    public static final String NOTIFICATION_SHOWING = "notification_showing";
    public static final String INFO_NOTIFICATION_SHOWN = "info_notification_Shown";
    protected boolean wasInfoNotificationShown;
    private Button showHideNotification;
    private boolean wasNotificationShowing;
    private View notificationView;
    private View infoNotificationView;
    private FragmentNotificationBarBinding binding;
    private TextView gotIButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_bar, container, false);
        binding.setFragment(this);

        showHideNotification = binding.showHideNotification;
        wasNotificationShowing = savedInstanceState != null && savedInstanceState.getBoolean(NOTIFICATION_SHOWING);
        infoNotificationView = getInfoNotificationContentView();
        return binding.getRoot();
    }

    protected void showInfoNotification() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                showInfoNotificationBar();
            }
        },300);
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            wasInfoNotificationShown = savedInstanceState.getBoolean(INFO_NOTIFICATION_SHOWN);
        }
        if (!wasInfoNotificationShown) {
            showInfoNotification();
        } else if (wasNotificationShowing) {
            showNotificationBar();
        }
    }

    protected void showNotificationBar() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                alterNotificationState();
            }
        }, 300);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(NOTIFICATION_SHOWING, wasNotificationShowing);
        outState.putBoolean(INFO_NOTIFICATION_SHOWN, wasInfoNotificationShown);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dismissNotification();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_notification_bar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(OptionMenuClickedEvent event) {
        dismissNotification();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(InfoMessageDismissedEvent event) {
        showNotificationBar();
    }

    private void createNotificationView(Context context) {
        notificationView = getNotificationContentView();
    }

    private View getNotificationContentView() {
        View view = binding.getRoot().findViewById(R.id.notification_view);//View.inflate(context, R.layout.uid_notification_bg_white, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(R.string.notification_bar_title);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(R.string.notification_bar_content);
        ((TextView) view.findViewById(R.id.uid_notification_btn_1)).setText(R.string.notification_bar_action_1);
        ((TextView) view.findViewById(R.id.uid_notification_btn_2)).setText(R.string.notification_bar_action_2);

        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_2).setVisibility(View.VISIBLE);

        view.findViewById(R.id.uid_notification_icon).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_icon).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alterNotificationState();
            }
        });
        return view;
    }

    private View getInfoNotificationContentView() {
        View view = binding.getRoot().findViewById(R.id.info_notification_view);//View.inflate(context, R.layout.uid_notification_bg_white, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(R.string.info_notification_note);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(R.string.info_notification_bar_content);
        gotIButton = (TextView) view.findViewById(R.id.uid_notification_btn_1);

        gotIButton.setText(R.string.info_notification_got_it);

        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_2).setVisibility(View.GONE);

        view.findViewById(R.id.uid_notification_icon).setVisibility(View.GONE);

        return view;
    }

    public void alterNotificationState() {
        if (notificationView == null) {
             createNotificationView(getActivity());
        }
        if (notificationView.getVisibility() == View.VISIBLE) {
            notificationView.setVisibility(View.GONE);
            wasNotificationShowing = false;
            showHideNotification.setText(R.string.notification_bar_notification_show);
        } else {
            wasNotificationShowing = true;
            notificationView.setVisibility(View.VISIBLE);
            if(infoNotificationView != null)
                infoNotificationView.setVisibility(View.GONE);

            showHideNotification.setText(R.string.notification_bar_notification_hide);
        }
        showHideNotification.setEnabled(true);
    }

    public void dismissNotification() {
        if (notificationView != null) {
            notificationView.setVisibility(View.GONE);
        }
        if (infoNotificationView != null) {
            infoNotificationView.setVisibility(View.GONE);
        }
    }

    protected void showInfoNotificationBar() {
        wasInfoNotificationShown = false;
        infoNotificationView.setVisibility(View.VISIBLE);

        gotIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (infoNotificationView != null && infoNotificationView.getVisibility() == View.VISIBLE) {
                    infoNotificationView.setVisibility(View.GONE);
                    wasInfoNotificationShown = true;
                }
                EventBus.getDefault().post(new InfoMessageDismissedEvent("Got It Clicked"));
            }
        });

        showHideNotification.setText(R.string.notification_bar_notification_hide);
        showHideNotification.setEnabled(false);
    }
}
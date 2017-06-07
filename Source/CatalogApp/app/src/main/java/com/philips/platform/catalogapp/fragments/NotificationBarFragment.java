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
import android.widget.PopupWindow;
import android.widget.TextView;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentNotificationBarBinding;
import com.philips.platform.catalogapp.events.InfoMessageDismissedEvent;
import com.philips.platform.catalogapp.events.OptionMenuClickedEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

public class NotificationBarFragment extends BaseFragment {

    public static final String POP_UP_SHOWING = "popupshowing";
    public static final String INFO_POPUP_SHOWN = "infoPopupShown";
    protected boolean wasInfoPopUPShown;
    private PopupWindow popupWindow;
    private Button showHideNotification;
    private boolean wasPopUPShowing;
    private PopupWindow infoPopupWindow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentNotificationBarBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification_bar, container, false);
        binding.setFragment(this);

        showHideNotification = binding.showHideNotification;

        if (savedInstanceState != null) {
            wasPopUPShowing = savedInstanceState.getBoolean(POP_UP_SHOWING);
        } else {
            wasPopUPShowing = false;
        }
        return binding.getRoot();
    }

    protected void showInfoNotification() {
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                showInfoPopup();
            }
        });
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            wasInfoPopUPShown = savedInstanceState.getBoolean(INFO_POPUP_SHOWN);
        }
        if (!wasInfoPopUPShown) {
            showInfoNotification();
        } else if (wasPopUPShowing) {
            showNotificationBar();
        }
        super.onViewStateRestored(savedInstanceState);
    }

    protected void showNotificationBar() {
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                alterPopUpState();
            }
        }, 200);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(POP_UP_SHOWING, wasPopUPShowing);
        outState.putBoolean(INFO_POPUP_SHOWN, wasInfoPopUPShown);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        dismissPopUp();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_notification_bar;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(OptionMenuClickedEvent event) {
        dismissPopUp();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessage(InfoMessageDismissedEvent event) {
        showNotificationBar();
    }

    private void createPopUpWindow(Context context) {
        View view = getNotificationContentView(context);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
    }

    private View getNotificationContentView(Context context) {
        View view = View.inflate(context, R.layout.uid_notification_bg_white, null);
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
                alterPopUpState();
            }
        });
        return view;
    }

    public void alterPopUpState() {
        if (popupWindow == null) {
            createPopUpWindow(getActivity());
        }
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
            wasPopUPShowing = false;
            showHideNotification.setText(R.string.notification_bar_notification_show);
        } else {
            wasPopUPShowing = true;
            popupWindow.showAsDropDown(getActivity().findViewById(R.id.uid_toolbar));
            showHideNotification.setText(R.string.notification_bar_notification_hide);
        }
        showHideNotification.setEnabled(true);
    }

    public void dismissPopUp() {
        if (popupWindow != null) {
            popupWindow.dismiss();
        }
        if (infoPopupWindow != null) {
            infoPopupWindow.dismiss();
        }
    }

    protected void showInfoPopup() {
        wasInfoPopUPShown = false;
        View view = View.inflate(getContext(), R.layout.uid_notification_bg_white, null);
        ((TextView) view.findViewById(R.id.uid_notification_title)).setText(R.string.info_notification_note);
        ((TextView) view.findViewById(R.id.uid_notification_content)).setText(R.string.info_notification_bar_content);
        final TextView gotIButton = (TextView) view.findViewById(R.id.uid_notification_btn_1);

        gotIButton.setText(R.string.info_notification_got_it);

        view.findViewById(R.id.uid_notification_title).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_content).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_1).setVisibility(View.VISIBLE);
        view.findViewById(R.id.uid_notification_btn_2).setVisibility(View.GONE);

        view.findViewById(R.id.uid_notification_icon).setVisibility(View.GONE);
        infoPopupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        infoPopupWindow.showAsDropDown(getActivity().findViewById(R.id.uid_toolbar));
        gotIButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (infoPopupWindow != null && infoPopupWindow.isShowing()) {
                    infoPopupWindow.dismiss();
                    wasInfoPopUPShown = true;
                }
                EventBus.getDefault().post(new InfoMessageDismissedEvent("Got It Clicked"));
            }
        });
        showHideNotification.setText(R.string.notification_bar_notification_hide);
        showHideNotification.setEnabled(false);
    }
}
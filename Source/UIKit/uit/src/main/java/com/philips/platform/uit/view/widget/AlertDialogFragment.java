package com.philips.platform.uit.view.widget;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.platform.uit.R;

public class AlertDialogFragment extends DialogFragment {

    private static final String UID_ALERT_DAILOG_MESSAGE_KEY = "UID_ALERT_DAILOG_MESSAGE_KEY";
    private static final String UID_ALERT_DAILOG_TITLE_KEY = "UID_ALERT_DAILOG_TITLE_KEY";
    private static final String UID_ALERT_DAILOG_TITLE_ICON_KEY = "UID_ALERT_DAILOG_TITLE_ICON_KEY";
    private String message;
    private String title;
    private
    @DrawableRes
    int iconResourceId = -1;

    public void setIconResourceId(final int iconResourceId) {
        this.iconResourceId = iconResourceId;
    }

    public void setMessage(final String message) {
        this.message = message;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.uid_alert_dialog, container, false);

        if (savedInstanceState != null) {
            message = savedInstanceState.getString(UID_ALERT_DAILOG_MESSAGE_KEY);
            title = savedInstanceState.getString(UID_ALERT_DAILOG_TITLE_KEY);
            iconResourceId = savedInstanceState.getInt(UID_ALERT_DAILOG_TITLE_ICON_KEY, -1);
        }
        android.widget.Button justOnce = (android.widget.Button) view.findViewById(R.id.uid_alert_negative_button);
        android.widget.Button always = (android.widget.Button) view.findViewById(R.id.uid_alert_positive_button);
        final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
        messageContent.setText(message);
        final TextView titleContent = (TextView) view.findViewById(R.id.uid_alert_title);
        titleContent.setText(title);
        if (iconResourceId != -1) {
            titleContent.setCompoundDrawables(ContextCompat.getDrawable(getContext(), iconResourceId).mutate(), null, null, null);
        }
        justOnce.setOnClickListener(dismissDialog());
        always.setOnClickListener(dismissDialog());
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(DialogFragment.STYLE_NORMAL, R.style.UIDAlertDialogStyle);
        super.onCreate(savedInstanceState);
    }

    private View.OnClickListener dismissDialog() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        };
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putString(UID_ALERT_DAILOG_MESSAGE_KEY, message);
        outState.putString(UID_ALERT_DAILOG_TITLE_KEY, title);
        outState.putInt(UID_ALERT_DAILOG_TITLE_ICON_KEY, iconResourceId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.UIDAlertDialogStyle;
    }
}

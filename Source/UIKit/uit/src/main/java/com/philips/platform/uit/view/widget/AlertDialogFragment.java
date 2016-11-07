package com.philips.platform.uit.view.widget;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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
    private Button positiveButton;
    private Button negativeButton;
    private int positiveButtonText;
    private int negativeButtonText;
    private View.OnClickListener positiveButtonLister;
    private View.OnClickListener negativeButtonListener;

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
        positiveButton = (Button) view.findViewById(R.id.uid_alert_positive_button);
        negativeButton = (Button) view.findViewById(R.id.uid_alert_negative_button);
        final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
        messageContent.setText(message);
        final TextView titleContent = (TextView) view.findViewById(R.id.uid_alert_title);
        titleContent.setText(title);

        positiveButton.setText(getString(positiveButtonText));
        positiveButton.setOnClickListener(positiveButtonLister);
        negativeButton.setText(negativeButtonText);
        negativeButton.setOnClickListener(negativeButtonListener);
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
        getDialog().getWindow().setBackgroundDrawableResource(R.color.uit_purple_level_90);
    }

    public void setPositiveButton(final int text, final View.OnClickListener onClickListener) {
        positiveButtonText = text;
        positiveButtonLister = onClickListener;
    }

    public void setNegativeButton(final int text, final View.OnClickListener onClickListener) {
        negativeButtonText = text;
        negativeButtonListener = onClickListener;
    }

    public void setIconDrawable(final Drawable drawable) {

    }
}

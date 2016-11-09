package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.uit.R;

/**
 *
 */
public class AlertDialogFragment extends DialogFragment {

    private final int theme;
    private AlertDialogController.DialogParams dialogParams;
    private TextView titleTextView;

    public AlertDialogFragment() {
        dialogParams = new AlertDialogController.DialogParams();
        theme = DialogFragment.STYLE_NORMAL;
    }

    private static AlertDialogFragment create(@NonNull final AlertDialogController.DialogParams dialogParams, final int mTheme) {
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setDialogParams(dialogParams);
        alertDialogFragment.setStyle(mTheme, R.style.UIDAlertDialogStyle);
        return alertDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.uid_alert_dialog, container, false);

        if (savedInstanceState != null) {
            dialogParams.message = savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_MESSAGE_KEY);
            dialogParams.title = savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_TITLE_KEY);
            dialogParams.iconId = savedInstanceState.getInt(AlertDialogController.UID_ALERT_DAILOG_TITLE_ICON_KEY, -1);
            dialogParams.positiveButtonText = savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY, null);
            dialogParams.negativeButtonText = savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY, null);
        }
        dialogParams.positiveButton = (Button) view.findViewById(R.id.uid_alert_positive_button);
        dialogParams.negativeButton = (Button) view.findViewById(R.id.uid_alert_negative_button);
        final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
        messageContent.setText(dialogParams.message);
        titleTextView = (TextView) view.findViewById(R.id.uid_alert_title);
        titleTextView.setText(dialogParams.title);

        final ViewGroup headerView = (ViewGroup) view.findViewById(R.id.uid_alert_dialog_header);
        setTitleIcon((ImageView) view.findViewById(R.id.uid_alert_icon), headerView);
        setPositiveButtonProperties();
        setNegativeButtonProperties();
        setCancelable(dialogParams.cancelable);
        return view;
    }

    private void setNegativeButtonProperties() {
        if (dialogParams.negativeButtonText != null) {
            dialogParams.negativeButton.setText(dialogParams.negativeButtonText);
            dialogParams.negativeButton.setOnClickListener(dialogParams.negativeButtonListener);
        } else {
            dialogParams.negativeButton.setVisibility(View.GONE);
        }
    }

    private void setPositiveButtonProperties() {
        if (dialogParams.positiveButtonText != null) {
            dialogParams.positiveButton.setText(dialogParams.positiveButtonText);
            dialogParams.positiveButton.setOnClickListener(dialogParams.positiveButtonLister);
        } else {
            dialogParams.positiveButton.setVisibility(View.GONE);
        }
    }

    private void setTitleIcon(final ImageView mIconView, final ViewGroup headerView) {

        if (mIconView != null) {
            if (dialogParams.iconId != 0) {
                mIconView.setVisibility(View.VISIBLE);
                mIconView.setImageResource(dialogParams.iconId);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }

        if (dialogParams.title != null) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(dialogParams.title);
        } else {
            headerView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        setStyle(theme, R.style.UIDAlertDialogStyle);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_MESSAGE_KEY, dialogParams.message);
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_TITLE_KEY, dialogParams.title);
        outState.putInt(AlertDialogController.UID_ALERT_DAILOG_TITLE_ICON_KEY, dialogParams.iconId);
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY, dialogParams.positiveButtonText);
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY, dialogParams.negativeButtonText);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.UIDAlertDialogStyle;
    }

    public void setDialogParams(final AlertDialogController.DialogParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    /**
     * sets the listener to receive callback on click of positive button
     *
     * @param listener
     */
    public void setPositiveButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.positiveButtonLister = listener;
        if (dialogParams.positiveButton != null) {
            dialogParams.positiveButton.setOnClickListener(listener);
        }
    }

    /**
     * sets the listener to receive callback on click of negative button
     *
     * @param listener
     */
    public void setNegativeButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.negativeButtonListener = listener;
        if (dialogParams.negativeButton != null) {
            dialogParams.negativeButton.setOnClickListener(listener);
        }
    }

    private void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        dialogParams.cancelable = canceledOnTouchOutside;
    }

    public static class Builder {
        public final AlertDialogController.DialogParams params;
        public final int mTheme;

        public Builder(final Context context) {
            this(context, R.style.UIDAlertDialogStyle);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            params = new AlertDialogController.DialogParams();
            params.context = context;
            mTheme = themeResId;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setTitle(@NonNull @StringRes int titleId) {
            params.title = params.context.getString(titleId);
            return this;
        }

        /**
         * Set the title displayed in the {@link AlertDialogFragment}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setTitle(@NonNull CharSequence title) {
            params.title = title.toString();
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setMessage(@NonNull @StringRes int messageId) {
            params.message = params.context.getString(messageId);
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setMessage(@NonNull CharSequence message) {
            params.message = message.toString();
            return this;
        }

        /**
         * Set the resource id of the {@link Drawable} to be used in the title.
         * <p>
         * Takes precedence over values set using {@link #setIcon(Drawable)}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setIcon(@DrawableRes int iconId) {
            params.iconId = iconId;
            return this;
        }

        /**
         * Set the {@link Drawable} to be used in the title.
         * <p>
         * <strong>Note:</strong> To ensure consistent styling, the drawable
         * should be inflated or constructed using the alert dialog's themed
         * context obtained via {@link #getContext()}.
         *
         * @return this Builder object to allow for chaining of calls to set
         * methods
         */
        public AlertDialogFragment.Builder setIcon(Drawable icon) {
            params.iconDrawable = icon;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the positive button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setPositiveButton(@StringRes int textId, final View.OnClickListener listener) {
            params.positiveButtonText = params.context.getString(textId);
            params.positiveButtonLister = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the positive button of the dialog is pressed.
         *
         * @param text     The text to display in the positive button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setPositiveButton(CharSequence text, final View.OnClickListener listener) {
            params.positiveButtonText = text.toString();
            params.positiveButtonLister = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param textId   The resource id of the text to display in the negative button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setNegativeButton(@StringRes int textId, final View.OnClickListener listener) {
            params.negativeButtonText = params.context.getString(textId);
            params.negativeButtonListener = listener;
            return this;
        }

        /**
         * Set a listener to be invoked when the negative button of the dialog is pressed.
         *
         * @param text     The text to display in the negative button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setNegativeButton(CharSequence text, final View.OnClickListener listener) {
            params.negativeButtonText = text.toString();
            params.negativeButtonListener = listener;
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setCancelable(boolean cancelable) {
            params.cancelable = cancelable;
            return this;
        }

        public AlertDialogFragment create() {
            // so we always have to re-set the theme
            final AlertDialogFragment dialog = AlertDialogFragment.create(params, mTheme);

            dialog.setCancelable(params.cancelable);
            if (params.cancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            return dialog;
        }
    }
}

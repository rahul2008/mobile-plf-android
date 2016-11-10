package com.philips.platform.uit.view.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Rect;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.platform.uit.R;
import com.philips.platform.uit.utils.MaxHeightScrollView;
import com.philips.platform.uit.utils.UIDUtils;

public class AlertDialogFragment extends DialogFragment {

    private AlertDialogController.DialogParams dialogParams;
    private TextView titleTextView;
    private MaxHeightScrollView messageContainer;
    private Button negativeButton;
    private Button positiveButton;

    private ViewGroup decorView;
    private View dimView;
    private FrameLayout dimViewContainer;
    private int mAnimDuration = 300;

    private int dimColor = Color.BLACK;
    private float dimColorAlpha = 0.8f;

    public AlertDialogFragment() {
        dialogParams = new AlertDialogController.DialogParams();
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
        positiveButton = (Button) view.findViewById(R.id.uid_alert_positive_button);
        negativeButton = (Button) view.findViewById(R.id.uid_alert_negative_button);
        final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
        messageContent.setText(dialogParams.message);

        messageContainer = (MaxHeightScrollView) view.findViewById(R.id.uid_alert_message_scroll_container);

        titleTextView = (TextView) view.findViewById(R.id.uid_alert_title);
        titleTextView.setText(dialogParams.title);
        final ViewGroup headerView = (ViewGroup) view.findViewById(R.id.uid_alert_dialog_header);
        setTitleIcon((ImageView) view.findViewById(R.id.uid_alert_icon), headerView);

        setPositiveButtonProperties();
        setNegativeButtonProperties();
        setCancelable(dialogParams.cancelable);

        //initialize container view
        setDimLayer();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.UIDAlertDialogStyle;
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
    public void onStart() {
        startEnterAnimation();
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        startExitAnimation();
        super.onDismiss(dialog);
    }

    private static AlertDialogFragment create(@NonNull final AlertDialogController.DialogParams dialogParams, final int themee) {
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setDialogParams(dialogParams);
        alertDialogFragment.setStyle(themee, R.style.UIDAlertDialogStyle);
        return alertDialogFragment;
    }

    private void setNegativeButtonProperties() {
        if (dialogParams.negativeButtonText != null) {
            negativeButton.setText(dialogParams.negativeButtonText);
            negativeButton.setOnClickListener(dialogParams.negativeButtonListener);
        } else {
            negativeButton.setVisibility(View.GONE);
        }
    }

    private void setPositiveButtonProperties() {
        if (dialogParams.positiveButtonText != null) {
            positiveButton.setText(dialogParams.positiveButtonText);
            positiveButton.setOnClickListener(dialogParams.positiveButtonLister);
        } else {
            positiveButton.setVisibility(View.GONE);
        }
    }

    private void setTitleIcon(final ImageView mIconView, final ViewGroup headerView) {

        if (mIconView != null) {
            if (dialogParams.iconId != 0) {
                mIconView.setImageResource(dialogParams.iconId);
            } else if (dialogParams.iconDrawable != null) {
                mIconView.setImageDrawable(dialogParams.iconDrawable);
            } else {
                mIconView.setVisibility(View.GONE);
            }
        }

        setTitle(headerView);
    }

    /**
     * Set the color and opacity for the dim background. Must be called before show to have effect.
     *
     * @param color           Color of background
     * @param alphaPercentage Percentage of alpha. Must be in range of 0..1
     */
    public void setDimColor(int color, float alphaPercentage) {
        dimColor = color;
        dimColorAlpha = alphaPercentage;
    }

    private void startEnterAnimation() {
        UIDUtils.animateAlpha(dimView, 1f, mAnimDuration, null);
    }

    private void startExitAnimation() {
        UIDUtils.animateAlpha(dimView, 0f, mAnimDuration, new Runnable() {
            @Override
            public void run() {
                decorView.removeView(dimViewContainer);
            }
        });
    }

    private void setDimLayer() {
        setDimColors();
        setDimContainer();
        addDimView();
    }

    private void setDimColors() {
        TypedArray array = getActivity().obtainStyledAttributes(R.styleable.UIDDialog);
        int bgColor = array.getColor(R.styleable.UIDDialog_uitDialogDimStrongColor, dimColor);
        float bgColorAlpha = array.getFloat(R.styleable.UIDDialog_uitDialogDimStrongColorAlpha, dimColorAlpha);
        setDimColor(bgColor, bgColorAlpha);
        array.recycle();
    }

    private void setDimContainer() {
        Rect visibleFrame = new Rect();
        decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        decorView.getWindowVisibleDisplayFrame(visibleFrame);

        dimViewContainer = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(visibleFrame.right - visibleFrame.left,
                visibleFrame.bottom - visibleFrame.top);
        params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0);
        dimViewContainer.setLayoutParams(params);
    }

    private void addDimView() {
        dimView = new View(getActivity());
        dimView.setBackgroundColor(getDimColorWithAlpha());
        dimView.setAlpha(0f);
        dimViewContainer.addView(dimView);
        decorView.addView(dimViewContainer);
    }

    private int getDimColorWithAlpha() {
        return UIDUtils.modulateColorAlpha(dimColor, dimColorAlpha);
    }

    private void setTitle(final ViewGroup headerView) {
        if (dialogParams.title != null) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(dialogParams.title);
        } else {
            headerView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
            final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) messageContainer.getLayoutParams();
            final int margintop = (int) getResources().getDimension(R.dimen.uid_alert_message_top_margin);
            layoutParams.setMargins(layoutParams.leftMargin, margintop, layoutParams.rightMargin, layoutParams.bottomMargin);
        }
    }

    public void setDialogParams(final AlertDialogController.DialogParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    /**
     * sets the listener to receive callback on click of positive button
     *
     * @param listener Listener for positive button
     */
    public void setPositiveButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.positiveButtonLister = listener;
        if (positiveButton != null) {
            positiveButton.setOnClickListener(listener);
        }
    }

    /**
     * sets the listener to receive callback on click of negative button
     *
     * @param listener Listener for negative button
     */
    public void setNegativeButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.negativeButtonListener = listener;
        if (negativeButton != null) {
            negativeButton.setOnClickListener(listener);
        }
    }

    private void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        dialogParams.cancelable = canceledOnTouchOutside;
    }

    public static class Builder {
        final AlertDialogController.DialogParams params;
        final int theme;

        public Builder(final Context context) {
            this(context, R.style.UIDAlertDialogStyle);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            params = new AlertDialogController.DialogParams();
            params.context = context;
            theme = themeResId;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setTitle(@StringRes int titleId) {
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
        public AlertDialogFragment.Builder setMessage(@StringRes int messageId) {
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
            final AlertDialogFragment dialog = AlertDialogFragment.create(params, theme);

            dialog.setCancelable(params.cancelable);
            if (params.cancelable) {
                dialog.setCanceledOnTouchOutside(true);
            }
            return dialog;
        }
    }
}
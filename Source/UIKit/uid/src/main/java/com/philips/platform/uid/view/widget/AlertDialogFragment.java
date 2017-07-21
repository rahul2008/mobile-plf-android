/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.MaxHeightScrollView;
import com.philips.platform.uid.utils.UIDUtils;

public class AlertDialogFragment extends DialogFragment {

    private AlertDialogController.DialogParams dialogParams;
    private TextView titleTextView;
    private MaxHeightScrollView messageContainer;
    private Button negativeButton;
    private Button positiveButton;

    private ViewGroup decorView;
    private View dimView;
    private FrameLayout dimViewContainer;
    private int animDuration = 300;

    private int dimColor = Color.BLACK;
    private float dimColorAlpha = 0.8f;

    public AlertDialogFragment() {
        dialogParams = new AlertDialogController.DialogParams();
    }

    private static AlertDialogFragment create(@NonNull final AlertDialogController.DialogParams dialogParams, final int themee) {
        final AlertDialogFragment alertDialogFragment = new AlertDialogFragment();
        alertDialogFragment.setDialogParams(dialogParams);
        alertDialogFragment.setStyle(themee, R.style.UIDAlertDialog);
        return alertDialogFragment;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.uid_alert_dialog, container, false);

        if (savedInstanceState != null) {
            dialogParams.setMessage(savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_MESSAGE_KEY));
            dialogParams.setTitle(savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_TITLE_KEY));
            dialogParams.setIconId(savedInstanceState.getInt(AlertDialogController.UID_ALERT_DAILOG_TITLE_ICON_KEY, -1));
            dialogParams.setPositiveButtonText(savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY, null));
            dialogParams.setNegativeButtonText(savedInstanceState.getString(AlertDialogController.UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY, null));
        }
        positiveButton = (Button) view.findViewById(R.id.uid_alert_positive_button);
        negativeButton = (Button) view.findViewById(R.id.uid_alert_negative_button);
        final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
        messageContent.setText(dialogParams.getMessage());

        messageContainer = (MaxHeightScrollView) view.findViewById(R.id.uid_alert_message_scroll_container);

        titleTextView = (TextView) view.findViewById(R.id.uid_alert_title);
        titleTextView.setText(dialogParams.getTitle());
        final ViewGroup headerView = (ViewGroup) view.findViewById(R.id.uid_alert_dialog_header);
        setTitleIcon((ImageView) view.findViewById(R.id.uid_alert_icon), headerView);

        setPositiveButtonProperties();
        setNegativeButtonProperties();
        setCancelable(dialogParams.isCancelable());

        handlePositiveOnlyButton();
        //initialize container view
        setDimLayer();

        return view;
    }

    private void handlePositiveOnlyButton() {
        if (!isViewVisible(negativeButton) && isViewVisible(positiveButton)) {

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
            int marginStartEnd = getContext().getResources().getDimensionPixelSize(R.dimen.uid_alert_dialog_positive_button_margin_end);
            params.setMarginStart(marginStartEnd);
            params.setMarginEnd(marginStartEnd);
            params.width = LinearLayout.LayoutParams.MATCH_PARENT;
            positiveButton.setLayoutParams(params);
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        try {
            getDialog().getWindow().setWindowAnimations(R.style.UIDAlertAnimation);
            setAlertWidth();
        } catch (NullPointerException e) {
            Log.e(e.toString(),e.getMessage());
        }
    }

    private void setAlertWidth() {
        final ViewGroup.LayoutParams lp;
        try {
            lp = getView().getLayoutParams();
            lp.width = (int) getResources().getDimension(R.dimen.uid_alert_dialog_width);
            getView().setLayoutParams(lp);
        } catch (NullPointerException e) {
            Log.e(e.toString(),e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_MESSAGE_KEY, dialogParams.getMessage());
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_TITLE_KEY, dialogParams.getTitle());
        outState.putInt(AlertDialogController.UID_ALERT_DAILOG_TITLE_ICON_KEY, dialogParams.getIconId());
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_POSITIVE_BUTTON_TEXT_KEY, dialogParams.getPositiveButtonText());
        outState.putString(AlertDialogController.UID_ALERT_DAILOG_NEGATIVE_BUTTON_TEXT_KEY, dialogParams.getNegativeButtonText());
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

    private void setNegativeButtonProperties() {
        if (dialogParams.getNegativeButtonText() != null) {
            negativeButton.setText(dialogParams.getNegativeButtonText());
            negativeButton.setOnClickListener(dialogParams.getNegativeButtonListener());
        } else {
            negativeButton.setVisibility(View.GONE);
        }
    }

    private void setPositiveButtonProperties() {
        if (dialogParams.getPositiveButtonText() != null) {
            positiveButton.setText(dialogParams.getPositiveButtonText());
            positiveButton.setOnClickListener(dialogParams.getPositiveButtonLister());
        } else {
            positiveButton.setVisibility(View.GONE);
        }
    }

    private void setTitleIcon(final ImageView mIconView, final ViewGroup headerView) {

        if (mIconView != null) {
            if (dialogParams.getIconId() != 0) {
                mIconView.setImageResource(dialogParams.getIconId());
            } else if (dialogParams.getIconDrawable() != null) {
                mIconView.setImageDrawable(dialogParams.getIconDrawable());
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
        UIDUtils.animateAlpha(dimView, 1f, animDuration, null);
    }

    private void startExitAnimation() {
        UIDUtils.animateAlpha(dimView, 0f, animDuration, new Runnable() {
            @Override
            public void run() {
                decorView.removeView(dimViewContainer);
                decorView = null;
                dimView = null;
                dimViewContainer = null;
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
        int bgColor = array.getColor(R.styleable.UIDDialog_uidDialogDimStrongColor, dimColor);
        float bgColorAlpha = array.getFloat(R.styleable.UIDDialog_uidDialogDimStrongColorAlpha, dimColorAlpha);
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
        if (dialogParams.getTitle() != null) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(dialogParams.getTitle());
        } else {
            headerView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
            final ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) messageContainer.getLayoutParams();
            final int margintop = (int) getResources().getDimension(R.dimen.uid_alert_dialog_message_top_margin_no_title);
            layoutParams.setMargins(layoutParams.leftMargin, margintop, layoutParams.rightMargin, layoutParams.bottomMargin);
        }
    }

    void setDialogParams(final AlertDialogController.DialogParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    /**
     * sets the listener to receive callback on click of positive button
     *
     * @param listener Listener for positive button
     */
    public void setPositiveButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.setPositiveButtonLister(listener);
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
        dialogParams.setNegativeButtonListener(listener);
        if (negativeButton != null) {
            negativeButton.setOnClickListener(listener);
        }
    }

    private void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        dialogParams.setCancelable(canceledOnTouchOutside);
    }

    private boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static class Builder {
        final AlertDialogController.DialogParams params;
        final int theme;

        public Builder(final Context context) {
            this(context, R.style.UIDAlertDialog);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            params = new AlertDialogController.DialogParams();
            params.setContext(context);
            theme = themeResId;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setTitle(@StringRes int titleId) {
            params.setTitle(params.getContext().getString(titleId));
            return this;
        }

        /**
         * Set the title displayed in the {@link AlertDialogFragment}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setTitle(@NonNull CharSequence title) {
            params.setTitle(title.toString());
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setMessage(@StringRes int messageId) {
            params.setMessage(params.getContext().getString(messageId));
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setMessage(@NonNull CharSequence message) {
            params.setMessage(message.toString());
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
            params.setIconId(iconId);
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
            params.setIconDrawable(icon);
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
            params.setPositiveButtonText(params.getContext().getString(textId));
            params.setPositiveButtonLister(listener);
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
            params.setPositiveButtonText(text.toString());
            params.setPositiveButtonLister(listener);
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
            params.setNegativeButtonText(params.getContext().getString(textId));
            params.setNegativeButtonListener(listener);
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
            params.setNegativeButtonText(text.toString());
            params.setNegativeButtonListener(listener);
            return this;
        }

        /**
         * Sets whether the dialog is cancelable or not.  Default is true.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         */
        public AlertDialogFragment.Builder setCancelable(boolean cancelable) {
            params.setCancelable(cancelable);
            return this;
        }

        public AlertDialogFragment create() {
            // so we always have to re-set the theme
            final AlertDialogFragment dialog = AlertDialogFragment.create(params, 0);

            dialog.setCancelable(params.isCancelable());
            if (params.isCancelable()) {
                dialog.setCanceledOnTouchOutside(true);
            }
            return dialog;
        }
    }
}
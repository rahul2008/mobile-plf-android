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
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.*;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.philips.platform.uid.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.utils.MaxHeightScrollView;
import com.philips.platform.uid.utils.UIDLog;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID AlertDialogFragment.
 * <p>
 * <P>UID AlertDialogFragment is an extension of DialogFragment which supports two variants.
 * <BR>{@link DialogConstants#TYPE_ALERT}
 * <BR>{@link DialogConstants#TYPE_DIALOG}
 * <p>
 * <P> AlertDialogFragment supports the below features which can be used in combination to convey critical information, take user inputs, or carry out multiple tasks.
 * <BR> > Alert  vs Dialog {@link AlertDialogFragment.Builder#setDialogType(int)}
 * <BR> > Modeless vs modal {@link AlertDialogFragment.Builder#setCancelable(boolean)}
 * <BR> > Subtle DIM Layer  vs Strong DIM Layer {@link AlertDialogFragment.Builder#setDimLayer(int)}
 * <BR> > With Dividers vs Without Dividers {@link AlertDialogFragment.Builder#setDividers(boolean)}
 * <p>
 * <p> It is recommended to use these features in a certain combination to adhere to DLS guidelines. For example, if using {@link DialogConstants#TYPE_DIALOG} and setting
 * <p> {@link AlertDialogFragment.Builder#setDialogLayout(int)} with a list. Top and bottom dividers should be set by calling {@link AlertDialogFragment.Builder#setDividers(boolean)}
 * <p>
 * <P> For usage of Dialog as per DLS recommendations, please refer to the DLS Catalog app or the confluence page below
 * <p>
 *
 * @see <a href="https://confluence.atlas.philips.com/display/UIT/Dialog">https://confluence.atlas.philips.com/display/UIT/Dialog</a>
 */

public class AlertDialogFragment extends DialogFragment {

    private AlertDialogParams dialogParams;
    private TextView titleTextView;
    private MaxHeightScrollView messageContainer;
    private NestedScrollView dialogContainer;
    private Button negativeButton;
    private Button positiveButton;
    private Button alternateButton;
    private View top_divider;
    private View bottom_divider;

    private LinearLayout buttonLayout;
    private ViewGroup decorView;
    private View dimView;
    private FrameLayout dimViewContainer;
    private int animDuration = 300;
    private int dimColor = Color.BLACK;

    public AlertDialogFragment() {
        dialogParams = new AlertDialogParams();
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        LayoutInflater layoutInflater = inflater.cloneInContext(UIDHelper.getPopupThemedContext(getContext()));

        if (savedInstanceState != null) {
            dialogParams.setMessage(savedInstanceState.getString(DialogConstants.UID_ALERT_DIALOG_MESSAGE_KEY));
            dialogParams.setTitle(savedInstanceState.getString(DialogConstants.UID_ALERT_DIALOG_TITLE_KEY));
            dialogParams.setIconId(savedInstanceState.getInt(DialogConstants.UID_ALERT_DIALOG_TITLE_ICON_KEY, -1));
            dialogParams.setPositiveButtonText(savedInstanceState.getString(DialogConstants.UID_ALERT_DIALOG_POSITIVE_BUTTON_TEXT_KEY, null));
            dialogParams.setNegativeButtonText(savedInstanceState.getString(DialogConstants.UID_ALERT_DIALOG_NEGATIVE_BUTTON_TEXT_KEY, null));
            dialogParams.setAlternateButtonText(savedInstanceState.getString(DialogConstants.UID_ALERT_DIALOG_ALTERNATE_BUTTON_TEXT_KEY, null));
            dialogParams.setContainerLayout(savedInstanceState.getInt(DialogConstants.UID_ALERT_DIALOG_CONTAINER_ITEM_KEY, -1));
            dialogParams.setShowDividers(savedInstanceState.getBoolean(DialogConstants.UID_ALERT_DIALOG_SHOW_DIVIDER_KEY));
            dialogParams.setDialogType(savedInstanceState.getInt(DialogConstants.UID_ALERT_DIALOG_TYPE_KEY));
            dialogParams.setDimLayer(savedInstanceState.getInt(DialogConstants.UID_ALERT_DIALOG_DIM_LAYER_KEY));
        }

        View view;

        if (dialogParams.getDialogType() == DialogConstants.TYPE_ALERT) {
            view = layoutInflater.inflate(R.layout.uid_alert_dialog, container, false);
            final TextView messageContent = (TextView) view.findViewById(R.id.uid_alert_message);
            messageContent.setText(dialogParams.getMessage());
            messageContainer = (MaxHeightScrollView) view.findViewById(R.id.uid_alert_message_scroll_container);
        } else {
            view = layoutInflater.inflate(R.layout.uid_dialog, container, false);
            alternateButton = (Button) view.findViewById(R.id.uid_dialog_alternate_button);
            dialogContainer = (NestedScrollView) view.findViewById(R.id.uid_dialog_container);
            top_divider = view.findViewById(R.id.uid_dialog_top_divider);
            bottom_divider = view.findViewById(R.id.uid_dialog_bottom_divider);
            setAlternateButtonProperties();
            handleDividers();
            resolveDialogLayout(layoutInflater);
        }

        positiveButton = (Button) view.findViewById(R.id.uid_dialog_positive_button);
        negativeButton = (Button) view.findViewById(R.id.uid_dialog_negative_button);
        titleTextView = (TextView) view.findViewById(R.id.uid_dialog_title);
        buttonLayout = (LinearLayout) view.findViewById(R.id.uid_dialog_control_area);
        titleTextView.setText(dialogParams.getTitle());
        final ViewGroup headerView = (ViewGroup) view.findViewById(R.id.uid_dialog_header);
        setTitleIcon((ImageView) view.findViewById(R.id.uid_dialog_icon), headerView);
        setPositiveButtonProperties();
        setNegativeButtonProperties();
        setCancelable(dialogParams.isCancelable());

        handleButtonLayout();
        handlePositiveOnlyButton();
        setDimLayer();

        return view;
    }

    private void resolveDialogLayout(LayoutInflater layoutInflater) {
        View containerView;
        if (dialogParams.getContainerLayout() == 0) {
            LinearLayout layout = new LinearLayout(getContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            dialogContainer.addView(layout);
            containerView = layout;
        } else {
            layoutInflater.inflate(dialogParams.getContainerLayout(), dialogContainer);
            containerView = dialogContainer.getChildAt(0); //Only one childView in NestedScrollView
        }

        if (dialogParams.getDialogView() != null && containerView instanceof ViewGroup) {
            ((ViewGroup) containerView).addView(dialogParams.getDialogView());
        }
    }

    private void handleDividers() {
        if (dialogParams.isShowDividers() && !isViewVisible(top_divider) && !isViewVisible(bottom_divider)) {
            top_divider.setVisibility(View.VISIBLE);
            bottom_divider.setVisibility(View.VISIBLE);
        } else if (!dialogParams.isShowDividers()) {
            top_divider.setVisibility(View.GONE);
            bottom_divider.setVisibility(View.GONE);
        }
    }

    private void handleButtonLayout() {

        int buttonMargin = getResources().getDimensionPixelSize(R.dimen.uid_dialog_button_margin);
        if (isViewVisible(alternateButton) && isViewVisible(negativeButton) && isViewVisible(positiveButton)) {
            buttonLayout.setOrientation(LinearLayout.VERTICAL);
            buttonLayout.setGravity(Gravity.CENTER_HORIZONTAL);

            ViewGroup.MarginLayoutParams positiveButtonLayoutParams = (ViewGroup.MarginLayoutParams) positiveButton.getLayoutParams();
            positiveButtonLayoutParams.setMargins(positiveButtonLayoutParams.getMarginStart(), buttonMargin, positiveButtonLayoutParams.getMarginEnd(), positiveButtonLayoutParams.bottomMargin);

            ViewGroup.MarginLayoutParams negativeButtonLayoutParams = (ViewGroup.MarginLayoutParams) negativeButton.getLayoutParams();
            negativeButtonLayoutParams.setMargins(negativeButtonLayoutParams.getMarginStart(), buttonMargin, negativeButtonLayoutParams.getMarginEnd(), negativeButtonLayoutParams.bottomMargin);
        } else {
            ViewGroup.MarginLayoutParams positiveButtonLayoutParams = (ViewGroup.MarginLayoutParams) positiveButton.getLayoutParams();
            positiveButtonLayoutParams.setMargins(buttonMargin, positiveButtonLayoutParams.topMargin, positiveButtonLayoutParams.getMarginEnd(), positiveButtonLayoutParams.bottomMargin);
        }
    }

    private void handlePositiveOnlyButton() {
        if (!isViewVisible(negativeButton) && !isViewVisible(alternateButton) && isViewVisible(positiveButton)) {

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
            if (dialogParams.getDialogType() == DialogConstants.TYPE_ALERT) {
                setAlertWidth();
            }
        } catch (NullPointerException e) {
            UIDLog.e(e.toString(), e.getMessage());
        }
    }

    /**
     * Display the dialog, adding the fragment to the given FragmentManager.  This
     * is a convenience for explicitly creating a transaction, adding the
     * fragment to it with the given tag, and committing it.  This does
     * <em>not</em> add the transaction to the back stack.  When the fragment
     * is dismissed, a new transaction will be executed to remove it from
     * the activity.
     *
     * @param manager The FragmentManager this fragment will be added to.
     * @param tag     The tag for this fragment, as per
     *                @since 3.0.0
     */
    public void showAllowingStateLoss(FragmentManager manager, String tag) {
        FragmentTransaction ft = manager.beginTransaction();
        ft.add(this, tag);
        ft.commitAllowingStateLoss();
    }

    private void setAlertWidth() {
        final ViewGroup.LayoutParams lp;
        try {
            lp = getView().getLayoutParams();
            lp.width = (int) getResources().getDimension(R.dimen.uid_alert_dialog_width);
            getView().setLayoutParams(lp);
        } catch (NullPointerException e) {
            UIDLog.e(e.toString(), e.getMessage());
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putString(DialogConstants.UID_ALERT_DIALOG_MESSAGE_KEY, dialogParams.getMessage());
        outState.putString(DialogConstants.UID_ALERT_DIALOG_TITLE_KEY, dialogParams.getTitle());
        outState.putInt(DialogConstants.UID_ALERT_DIALOG_TITLE_ICON_KEY, dialogParams.getIconId());
        outState.putString(DialogConstants.UID_ALERT_DIALOG_POSITIVE_BUTTON_TEXT_KEY, dialogParams.getPositiveButtonText());
        outState.putString(DialogConstants.UID_ALERT_DIALOG_NEGATIVE_BUTTON_TEXT_KEY, dialogParams.getNegativeButtonText());
        outState.putString(DialogConstants.UID_ALERT_DIALOG_ALTERNATE_BUTTON_TEXT_KEY, dialogParams.getAlternateButtonText());
        outState.putInt(DialogConstants.UID_ALERT_DIALOG_CONTAINER_ITEM_KEY, dialogParams.getContainerLayout());
        outState.putBoolean(DialogConstants.UID_ALERT_DIALOG_SHOW_DIVIDER_KEY, dialogParams.isShowDividers());
        outState.putInt(DialogConstants.UID_ALERT_DIALOG_TYPE_KEY, dialogParams.getDialogType());
        outState.putInt(DialogConstants.UID_ALERT_DIALOG_DIM_LAYER_KEY, dialogParams.getDimLayer());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        startEnterAnimation();
        super.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        decorView.removeView(dimViewContainer);
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

    private void setAlternateButtonProperties() {
        if (dialogParams.getAlternateButtonText() != null) {
            alternateButton.setText(dialogParams.getAlternateButtonText());
            alternateButton.setOnClickListener(dialogParams.getAlternateButtonListener());
        } else {
            alternateButton.setVisibility(View.GONE);
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
     * @param color Color of background
     *              @since 3.0.0
     */
    public void setDimColor(int color) {
        dimColor = color;
    }

    private void startEnterAnimation() {
        UIDUtils.animateAlpha(dimView, 1f, animDuration, null);
    }

    private void startExitAnimation() {
        UIDUtils.animateAlpha(dimView, 0f, animDuration, new Runnable() {
            @Override
            public void run() {
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
        TypedArray array = getActivity().obtainStyledAttributes(R.styleable.PhilipsUID);
        int bgColor = dialogParams.getDimLayer() == DialogConstants.DIM_SUBTLE ? array.getColor(R.styleable.PhilipsUID_uidDimLayerSubtleBackgroundColor, dimColor) : array.getColor(R.styleable.PhilipsUID_uidDimLayerStrongBackgroundColor, dimColor);
        setDimColor(bgColor);
        array.recycle();
    }

    private void setDimContainer() {
        decorView = (ViewGroup) getActivity().getWindow().getDecorView();
        dimViewContainer = new FrameLayout(getActivity());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        dimViewContainer.setLayoutParams(params);
    }

    private void addDimView() {
        dimView = new View(getActivity());
        dimView.setBackgroundColor(dimColor);
        dimView.setAlpha(0f);
        dimViewContainer.addView(dimView);
        decorView.post(new Runnable() {
            @Override
            public void run() {
                if (decorView != null) {
                    decorView.addView(dimViewContainer);
                }
            }
        });
    }

    private void setTitle(final ViewGroup headerView) {
        if (dialogParams.getTitle() != null) {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(dialogParams.getTitle());
        } else {
            headerView.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
            final int marginTop = (int) getResources().getDimension(R.dimen.uid_alert_dialog_message_top_margin_no_title);
            final ViewGroup.MarginLayoutParams layoutParams = dialogParams.getDialogType() == DialogConstants.TYPE_ALERT ? (ViewGroup.MarginLayoutParams) messageContainer.getLayoutParams() : (ViewGroup.MarginLayoutParams) dialogContainer.getLayoutParams();
            layoutParams.setMargins(layoutParams.leftMargin, marginTop, layoutParams.rightMargin, layoutParams.bottomMargin);
        }
    }

    void setDialogParams(final AlertDialogParams dialogParams) {
        this.dialogParams = dialogParams;
    }

    /**
     * sets the listener to receive callback on click of positive button
     *
     * @param listener Listener for positive button
     *                 @since 3.0.0
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
     *                 @since 3.0.0
     */
    public void setNegativeButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.setNegativeButtonListener(listener);
        if (negativeButton != null) {
            negativeButton.setOnClickListener(listener);
        }
    }

    /**
     * sets the listener to receive callback on click of alternate button
     *
     * @param listener Listener for alternate button
     *                 @since 3.0.0
     */
    public void setAlternateButtonListener(@NonNull final View.OnClickListener listener) {
        dialogParams.setAlternateButtonListener(listener);
        if (alternateButton != null) {
            alternateButton.setOnClickListener(listener);
        }
    }

    void setCanceledOnTouchOutside(final boolean canceledOnTouchOutside) {
        dialogParams.setCancelable(canceledOnTouchOutside);
    }

    private boolean isViewVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }

    public static class Builder {
        final AlertDialogParams params;
        final int theme;
        int dialogStyle;

        public Builder(final Context context) {
            this(context, R.style.UIDAlertDialog);
        }

        public Builder(@NonNull Context context, @StyleRes int themeResId) {
            params = new AlertDialogParams();
            params.setContext(context);
            theme = themeResId;
        }

        /**
         * Style that needs to be applied on dialog.
         *
         * @param dialogStyle Selects a standard style: may be {@link #STYLE_NORMAL},
         * {@link #STYLE_NO_TITLE}, {@link #STYLE_NO_FRAME}, or
         * {@link #STYLE_NO_INPUT}.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public Builder setDialogStyle(int dialogStyle) {
            this.dialogStyle = dialogStyle;
            return this;
        }

        /**
         * Set the title using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setTitle(@StringRes int titleId) {
            params.setTitle(params.getContext().getString(titleId));
            return this;
        }

        /**
         * Set the title displayed in the {@link AlertDialogFragment}.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setTitle(@NonNull CharSequence title) {
            params.setTitle(title.toString());
            return this;
        }

        /**
         * Set the message to display using the given resource id.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setMessage(@StringRes int messageId) {
            params.setMessage(params.getContext().getString(messageId));
            return this;
        }

        /**
         * Set the message to display.
         *
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
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
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setCancelable(boolean cancelable) {
            params.setCancelable(cancelable);
            return this;
        }

        /**
         * Set the type of dialog to show, there are two variants available Dialog and AlertDialog.
         *
         * @param dialogType Can be one of two options: DialogConstants.TYPE_DIALOG or DialogConstants.TYPE_ALERT
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setDialogType(int dialogType) {
            params.setDialogType(dialogType);
            return this;
        }

        /**
         * Set whether the background dim layer on dialog layout should be set to subtle or strong
         *
         * @param dimLayer Can be one of two options: DialogConstants.DIM_SUBTLE or DialogConstants.DIM_STRONG
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setDimLayer(int dimLayer) {
            params.setDimLayer(dimLayer);
            return this;
        }

        /**
         * Set the layout that should be shown on the dialog.
         *
         * @param layout ResID of the layout to be set to the dialog.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setDialogLayout(@LayoutRes int layout) {
            params.setContainerLayout(layout);
            return this;
        }

        /**
         * Set the layout that should be shown on the dialog.
         *
         * @param view View to be added to the dialog layout.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setDialogView(View view) {
            params.setDialogView(view);
            return this;
        }

        /**
         * Set a listener to be invoked when the alternate button of the dialog is pressed.
         *
         * @param text     The text to display in the alternate button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setAlternateButton(CharSequence text, final View.OnClickListener listener) {
            params.setAlternateButtonText(text.toString());
            params.setAlternateButtonListener(listener);
            return this;
        }

        /**
         * Set a listener to be invoked when the alternate button of the dialog is pressed.
         *
         * @param textId   String to display in the alternate button
         * @param listener The {@link View.OnClickListener} to use.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setAlternateButton(@StringRes int textId, final View.OnClickListener listener) {
            params.setAlternateButtonText(params.getContext().getString(textId));
            params.setAlternateButtonListener(listener);
            return this;
        }

        /**
         * Set whether top and bottom dividers should be shown on the dialog layout, should be used in case of lists
         *
         * @param showDividers boolean to set if dividers should be shown.
         * @return This Builder object to allow for chaining of calls to set methods
         * @since 3.0.0
         */
        public AlertDialogFragment.Builder setDividers(boolean showDividers) {
            params.setShowDividers(showDividers);
            return this;
        }

        /**
         * Sets the builder objects on dialog and returns {@link AlertDialogFragment}.
         *
         * @return AlertDialogFragment
         * @since 3.0.0
         */
        public AlertDialogFragment create() {
            return create(new AlertDialogFragment());
        }

        /**
         * Gives a chance to provide any extension of {@link AlertDialogFragment} to intercept calls of {@link DialogFragment}
         *
         * @param alertDialogFragment Fragment which extends {@link AlertDialogFragment}
         * @return AlertDialogFragment object
         * @since 3.0.0
         */
        public <T extends AlertDialogFragment> AlertDialogFragment create(final T alertDialogFragment) {
            alertDialogFragment.setCancelable(params.isCancelable());
            if (params.isCancelable()) {
                alertDialogFragment.setCanceledOnTouchOutside(true);
            }
            alertDialogFragment.setDialogParams(params);
            alertDialogFragment.setStyle(dialogStyle, theme);
            return alertDialogFragment;
        }
    }
}
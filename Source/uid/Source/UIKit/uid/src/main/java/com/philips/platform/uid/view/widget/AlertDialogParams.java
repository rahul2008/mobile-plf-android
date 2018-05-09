/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.StyleRes;
import android.view.View;

import com.philips.platform.uid.R;
import com.philips.platform.uid.utils.DialogConstants;

@SuppressWarnings("ALL")
public class AlertDialogParams {

    private String message;
    private String title;
    private View.OnClickListener positiveButtonLister;
    private View.OnClickListener negativeButtonListener;
    private View.OnClickListener alternateButtonListener;
    private Context context;
    @DrawableRes
    private int iconId;
    private Drawable iconDrawable;
    private boolean cancelable;
    private String negativeButtonText;
    private String positiveButtonText;
    private String alternateButtonText;
    @LayoutRes
    private int containerLayout;
    private View dialogView;
    private boolean showDividers;

    @StyleRes
    int overrideStyleRes = R.style.UIDDialogStylesOverrides;

    private int dialogType = DialogConstants.TYPE_ALERT;
    private int dimLayer = DialogConstants.DIM_STRONG;

    public int getDialogType() {
        return dialogType;
    }

    public void setDialogType(int dialogType) {
        this.dialogType = dialogType;
    }

    public View getDialogView() {
        return dialogView;
    }

    public void setDialogView(View dialogView) {
        this.dialogView = dialogView;
    }

    public int getDimLayer() {
        return dimLayer;
    }

    public void setDimLayer(int dimLayer) {
        this.dimLayer = dimLayer;
    }

    public View.OnClickListener getAlternateButtonListener() {
        return alternateButtonListener;
    }

    public void setAlternateButtonListener(View.OnClickListener alternateButtonListener) {
        this.alternateButtonListener = alternateButtonListener;
    }

    public String getAlternateButtonText() {
        return alternateButtonText;
    }

    public void setAlternateButtonText(String alternateButtonText) {
        this.alternateButtonText = alternateButtonText;
    }

    public int getContainerLayout() {
        return containerLayout;
    }

    public void setContainerLayout(int containerLayout) {
        this.containerLayout = containerLayout;
    }

    public boolean isShowDividers() {
        return showDividers;
    }

    public void setShowDividers(boolean showDividers) {
        this.showDividers = showDividers;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public View.OnClickListener getPositiveButtonLister() {
        return positiveButtonLister;
    }

    public void setPositiveButtonLister(View.OnClickListener positiveButtonLister) {
        this.positiveButtonLister = positiveButtonLister;
    }

    public View.OnClickListener getNegativeButtonListener() {
        return negativeButtonListener;
    }

    public void setNegativeButtonListener(View.OnClickListener negativeButtonListener) {
        this.negativeButtonListener = negativeButtonListener;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public Drawable getIconDrawable() {
        return iconDrawable;
    }

    public void setIconDrawable(Drawable iconDrawable) {
        this.iconDrawable = iconDrawable;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public void setCancelable(boolean cancelable) {
        this.cancelable = cancelable;
    }

    public String getNegativeButtonText() {
        return negativeButtonText;
    }

    public void setNegativeButtonText(String negativeButtonText) {
        this.negativeButtonText = negativeButtonText;
    }

    public String getPositiveButtonText() {
        return positiveButtonText;
    }

    public void setPositiveButtonText(String positiveButtonText) {
        this.positiveButtonText = positiveButtonText;
    }

    public void setDialogActionAreaOverrideStyle(@StyleRes int overrideStyleRes) {
        this.overrideStyleRes = overrideStyleRes;
    }
}
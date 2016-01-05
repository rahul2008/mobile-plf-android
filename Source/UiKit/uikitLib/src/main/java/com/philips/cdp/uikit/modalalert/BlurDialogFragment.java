/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
package com.philips.cdp.uikit.modalalert;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;

/**
 * BlurDialogFragment enables you to get Blur background followed by dialog
 */

public class BlurDialogFragment extends DialogFragment {

    private int mAnimDuration;
    private int mWindowAnimStyle;
    private int mBgColorResId;
    private ViewGroup mRoot;
    private View mBlurBgView;
    private ImageView mBlurImgView;
    private FrameLayout mBlurContainer;

    public BlurDialogFragment() {
        mWindowAnimStyle = R.style.PhilipsModalAlertAnimation;
        mBgColorResId = R.color.uikit_modal_alert_glass;
    }

    /**
     * This API enables to do initial resource initialization
     *
     * @param savedInstanceState
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimDuration = getActivity().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    /**
     * Lifecycle method which UIKitLib override to enable blur effect
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        applyBlur();
    }

    /**
     * Lifecycle method which UIKitLib override to start enter animation
     */
    @Override
    public void onStart() {
        startEnterAnimation();
        super.onStart();
    }

    /**
     * API which enables to dismiss dialog with exit animation
     * @param dialog
     */
    @Override
    public void onDismiss(DialogInterface dialog) {
        startExitAnimation();
        super.onDismiss(dialog);
    }

    private void applyBlur() {
        Window window = getDialog().getWindow();
        window.setWindowAnimations(mWindowAnimStyle);

        Rect visibleFrame = getVisibleRectWindowFrame();

        setUpBlurImage();

        applyBlurEffect(visibleFrame);

        setOnTouchListener();
    }

    private void setUpBlurImage() {
        mBlurBgView = new View(getActivity());
        mBlurBgView.setBackgroundColor(ContextCompat.getColor(getContext(), mBgColorResId));
        ModalAlertUtil.setAlpha(mBlurBgView, 0f);

        mBlurImgView = new ImageView(getActivity());
        ModalAlertUtil.setAlpha(mBlurImgView, 0f);

        mBlurContainer.addView(mBlurImgView);
        mBlurContainer.addView(mBlurBgView);

        mRoot.addView(mBlurContainer);
    }

    @NonNull
    private Rect getVisibleRectWindowFrame() {
        Rect visibleFrame = new Rect();
        mRoot = (ViewGroup) getActivity().getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);

        mBlurContainer = new FrameLayout(getActivity());
        if (ModalAlertUtil.isPostHoneycomb()) {
            mBlurContainer = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(visibleFrame.right - visibleFrame.left,
                    visibleFrame.bottom - visibleFrame.top);
            params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0);
            mBlurContainer.setLayoutParams(params);
        } else {
            mBlurContainer.setPadding(visibleFrame.left, visibleFrame.top, 0, 0);
        }
        return visibleFrame;
    }

    private void setOnTouchListener() {
        View view = getView();
        if (view != null) {
            view.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    dismiss();
                    return true;
                }
            });
        }
    }

    private void applyBlurEffect(Rect visibleFrame) {
        Bitmap bitmap = ModalAlertUtil.drawViewToBitmap(mRoot, visibleFrame.right,
                visibleFrame.bottom, visibleFrame.left, visibleFrame.top, 3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap blurred = ModalAlertUtil.apply(getActivity(), bitmap);
            mBlurImgView.setImageBitmap(blurred);
        }
        bitmap.recycle();
    }

    private void startEnterAnimation() {
        ModalAlertUtil.animateAlpha(mBlurBgView, 0f, 1f, mAnimDuration, null);
        ModalAlertUtil.animateAlpha(mBlurImgView, 0f, 1f, mAnimDuration, null);
    }

    private void startExitAnimation() {
        ModalAlertUtil.animateAlpha(mBlurBgView, 1f, 0f, mAnimDuration, null);
        ModalAlertUtil.animateAlpha(mBlurImgView, 1f, 0f, mAnimDuration, new Runnable() {
            @Override
            public void run() {
                mRoot.removeView(mBlurContainer);
            }
        });
    }

    /**
     * API to set user defined animation duration
     * @param mAnimDuration
     */
    public void setAnimDuration(int mAnimDuration) {
        this.mAnimDuration = mAnimDuration;
    }

    /**
     * API to set user defined animation style
     * @param mWindowAnimStyle
     */
    public void setWindowAnimStyle(int mWindowAnimStyle) {
        this.mWindowAnimStyle = mWindowAnimStyle;
    }
}
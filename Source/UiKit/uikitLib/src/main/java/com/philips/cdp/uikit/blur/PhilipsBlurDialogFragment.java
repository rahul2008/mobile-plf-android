package com.philips.cdp.uikit.blur;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.philips.cdp.uikit.R;

public class PhilipsBlurDialogFragment extends DialogFragment {

    private int mAnimDuration;
    private int mWindowAnimStyle;
    private int mBgColorResId;
    private ViewGroup mRoot;
    private View mBlurBgView;
    private ImageView mBlurImgView;
    private FrameLayout mBlurContainer;

    public PhilipsBlurDialogFragment() {
        mWindowAnimStyle = R.style.PhilipsModalAlertAnimation;
        mBgColorResId = R.color.uikit_modal_alert_glass;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAnimDuration = getActivity().getResources().getInteger(android.R.integer.config_mediumAnimTime);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        applyBlur();
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

    private void applyBlur() {
        Window window = getDialog().getWindow();
        window.setWindowAnimations(mWindowAnimStyle);

        Rect visibleFrame = new Rect();
        mRoot = (ViewGroup) getActivity().getWindow().getDecorView();
        mRoot.getWindowVisibleDisplayFrame(visibleFrame);

        mBlurContainer = new FrameLayout(getActivity());
        if (Util.isPostHoneycomb()) {
            mBlurContainer = new FrameLayout(getActivity());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(visibleFrame.right - visibleFrame.left,
                    visibleFrame.bottom - visibleFrame.top);
            params.setMargins(visibleFrame.left, visibleFrame.top, 0, 0);
            mBlurContainer.setLayoutParams(params);
        } else {
            mBlurContainer.setPadding(visibleFrame.left, visibleFrame.top, 0, 0);
        }

        mBlurBgView = new View(getActivity());
        mBlurBgView.setBackgroundColor(ContextCompat.getColor(getContext(), mBgColorResId));
        Util.setAlpha(mBlurBgView, 0f);

        mBlurImgView = new ImageView(getActivity());
        Util.setAlpha(mBlurImgView, 0f);

        mBlurContainer.addView(mBlurImgView);
        mBlurContainer.addView(mBlurBgView);

        mRoot.addView(mBlurContainer);

        // apply blur effect
        Bitmap bitmap = Util.drawViewToBitmap(mRoot, visibleFrame.right,
                visibleFrame.bottom, visibleFrame.left, visibleFrame.top, 3);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            Bitmap blurred = Blur.apply(getActivity(), bitmap);
            mBlurImgView.setImageBitmap(blurred);
        }
        bitmap.recycle();

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

    private void startEnterAnimation() {
        Util.animateAlpha(mBlurBgView, 0f, 1f, mAnimDuration, null);
        Util.animateAlpha(mBlurImgView, 0f, 1f, mAnimDuration, null);
    }

    private void startExitAnimation() {
        Util.animateAlpha(mBlurBgView, 1f, 0f, mAnimDuration, null);
        Util.animateAlpha(mBlurImgView, 1f, 0f, mAnimDuration, new Runnable() {
            @Override
            public void run() {
                mRoot.removeView(mBlurContainer);
            }
        });
    }
}
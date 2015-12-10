package com.philips.cdp.uikit.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.utils.BlurView;


/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitModalAlert extends Dialog {

    protected float blurRadius = 30.f;
    private boolean useBlur = true;
    private Activity activity;
    private boolean useAnimation = true;
    private boolean dismissState = false;
    private boolean startState = false;
    private Bitmap fastBlurBitmap;

    private Handler messageHandler = new Handler() {
        @SuppressWarnings("deprecation")
        //we need to support API lvl 14+, so cannot change to BitmapDrawable: sticking with deprecated API for now
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Window window = UiKitModalAlert.this.getWindow();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
            }
            window.setBackgroundDrawable(new BitmapDrawable(fastBlurBitmap));
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            if (useAnimation)
                startAnimation();

            startState = true;
            show();
        }
    };

    public UiKitModalAlert(Context context) {
        super(context);
        this.activity = (Activity) context;
    }

    public UiKitModalAlert(Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
    }

    protected UiKitModalAlert(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
    }

    private Bitmap takeScreenShot() {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();

        Bitmap drawingCache = view.getDrawingCache();
        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Bitmap screenShotBitmap = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return screenShotBitmap;
    }

    public float getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(float blurRadius) {
        this.blurRadius = blurRadius;
    }

    @Override
    public void show() {
        if (startState) {
            super.show();
            startState = false;
        } else if (useBlur)
            showFastBlur();
        else
            super.show();


    }

    private void startAnimation() {
        Animation animationScaleDown = AnimationUtils.loadAnimation(activity, R.anim.uikit_modal_alert_zoom_out);
        AnimationSet growShrink = new AnimationSet(true);
        growShrink.addAnimation(animationScaleDown);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_id);
        if (parent != null)
            parent.startAnimation(growShrink);
    }


    @Override
    public void dismiss() {
        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.uikit_modal_alert_zoom_in);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_id);
        if (dismissState) {
            super.dismiss();
            dismissState = false;
        } else {
            anim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {
                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    dismissState = true;
                    dismiss();
                }
            });

            if (parent != null)
                parent.startAnimation(anim);
        }
    }

    private void showFastBlur() {
        new Thread() {
            public void run() {
                Bitmap map = takeScreenShot();
                if (fastBlurBitmap == null)
                    fastBlurBitmap = new BlurView().fastBlur(map, (int) blurRadius);
                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }


    public void enableBlur(boolean useBlur) {
        this.useBlur = useBlur;
    }

    public void enableAnimation(boolean useAnimation) {
        this.useAnimation = useAnimation;
    }

}

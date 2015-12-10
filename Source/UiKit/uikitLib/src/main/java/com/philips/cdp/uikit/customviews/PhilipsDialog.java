package com.philips.cdp.uikit.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v8.renderscript.Allocation;
import android.support.v8.renderscript.Element;
import android.support.v8.renderscript.RenderScript;
import android.support.v8.renderscript.ScriptIntrinsicBlur;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
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
public class PhilipsDialog extends Dialog {

    protected float blurRadius = 25.f;
    private boolean isBlur = true;
    private Activity activity;
    private Bitmap fast;
    private boolean isAnimationEnabled = true;
    private Handler messageHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            final Drawable draw = new BitmapDrawable(getContext().getResources(), fast);
            Window window = PhilipsDialog.this.getWindow();
            window.setBackgroundDrawable(draw);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            window.setGravity(Gravity.CENTER);
            show();
        }
    };

    public PhilipsDialog(Context context) {
        super(context);
        this.activity = (Activity) context;
        setBackListener();

    }

    public PhilipsDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
        setBackListener();
    }

    protected PhilipsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
        setBackListener();
    }

    private void setBackListener() {
        setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface arg0, int keyCode,
                                 KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    if (isAnimationEnabled)
                        dismissPhilipsDialog();
                    else
                        dismiss();
                }
                return true;
            }
        });
    }

    public boolean isBlur() {
        return isBlur;
    }

    public void setIsBlur(boolean isBlur) {
        this.isBlur = isBlur;
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

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to BitmapDrawable: sticking with deprecated API for now
    private void renderUsingRenderScript(Bitmap screenShotBitmap) {
        RenderScript rs = RenderScript.create(activity);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpOut = Allocation.createFromBitmap(rs, screenShotBitmap);
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpOut);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(screenShotBitmap);
        Window window = this.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        window.setBackgroundDrawable(new BitmapDrawable(screenShotBitmap));
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    public float getBlurRadius() {
        return blurRadius;
    }

    public void setBlurRadius(float blurRadius) {
        this.blurRadius = blurRadius;
    }

    public void showPhilipsDialog() {
        if (isBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            renderUsingRenderScript(takeScreenShot());
        else if (isBlur)
            showFastBlurWithoutThreading();

        if (isAnimationEnabled)
            startAnimation();
        show();
    }

    @Override
    public void show() {
        if (isBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            renderUsingRenderScript(takeScreenShot());
        else if (isBlur)
            showFastBlurWithoutThreading();
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


    public void dismissPhilipsDialog() {
        Animation anim = AnimationUtils.loadAnimation(activity, R.anim.uikit_modal_alert_zoom_in);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_id);
        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                dismiss();
            }
        });
        if (parent != null)
        parent.startAnimation(anim);
    }

    private void showFastBlurWithoutThreading() {
        Bitmap map = takeScreenShot();
        if (fast == null)
            fast = new BlurView().fastBlur(map, (int) blurRadius);

        final Drawable draw = new BitmapDrawable(getContext().getResources(), fast);
        Window window = PhilipsDialog.this.getWindow();
        window.setBackgroundDrawable(draw);
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.setGravity(Gravity.CENTER);
    }

    private void showFastBlur() {
        new Thread() {
            public void run() {
                Bitmap map = takeScreenShot();
                if (fast == null)
                    fast = new BlurView().fastBlur(map, (int) blurRadius);
                messageHandler.sendEmptyMessage(0);
            }
        }.start();
    }
}

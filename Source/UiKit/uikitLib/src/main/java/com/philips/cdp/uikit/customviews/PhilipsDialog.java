package com.philips.cdp.uikit.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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

    }

    public PhilipsDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.activity = (Activity) context;
    }

    protected PhilipsDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.activity = (Activity) context;
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

    @Override
    public void show() {
        if (isBlur && Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            renderUsingRenderScript(takeScreenShot());
        else if (isBlur)
            showFastBlurWithoutThreading();

        startAnimation();
        super.show();
    }

    private void startAnimation() {
        Animation animationScaleDown = AnimationUtils.loadAnimation(activity, R.anim.uikit_zoom_out);
        AnimationSet growShrink = new AnimationSet(true);
        growShrink.addAnimation(animationScaleDown);
        LinearLayout parent = (LinearLayout) findViewById(R.id.parent_id);
        parent.startAnimation(growShrink);
    }

    private void stopAnimation() {
        Animation animationScaleUp = AnimationUtils.loadAnimation(activity, R.anim.uikit_zoom_in);
        AnimationSet growShrinkTest = new AnimationSet(false);
        growShrinkTest.addAnimation(animationScaleUp);
        LinearLayout parent = null;
        parent = (LinearLayout) findViewById(R.id.parent_id);
        parent.startAnimation(growShrinkTest);
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
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

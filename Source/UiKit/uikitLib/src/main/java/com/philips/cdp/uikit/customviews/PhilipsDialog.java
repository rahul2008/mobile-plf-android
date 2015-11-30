package com.philips.cdp.uikit.customviews;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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


        Bitmap b = Bitmap.createBitmap(drawingCache, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to BitmapDrawable: sticking with deprecated API for now
    private void renderUsingRenderScript(Bitmap outputBitmap) {
        RenderScript rs = RenderScript.create(activity);
        ScriptIntrinsicBlur theIntrinsic = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
        Allocation tmpOut = Allocation.createFromBitmap(rs, outputBitmap);
        theIntrinsic.setRadius(blurRadius);
        theIntrinsic.setInput(tmpOut);
        theIntrinsic.forEach(tmpOut);
        tmpOut.copyTo(outputBitmap);
        Window window = this.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable(outputBitmap));
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
        if (isBlur)
            renderUsingRenderScript(takeScreenShot());
        super.show();
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

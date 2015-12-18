package com.philips.cdp.uikit;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.philips.cdp.uikit.customviews.LayerListDrawable;
import com.philips.cdp.uikit.customviews.VectorDrawableImageView;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class UiKitActivity extends AppCompatActivity {

    @Override
    protected void attachBaseContext(final Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initFontIconLib();
    }

    @Override
    public void onCreate(final Bundle savedInstanceState, final PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/centralesans_book.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (validateHamburger()) {
            DrawerLayout philipsDrawerLayout = setLogoAlpha();
            configureStatusBarViews();
            philipsDrawerLayout.setScrimColor(Color.TRANSPARENT);
        }
    }

    @SuppressWarnings("deprecation")
    //we need to support API lvl 14+, so cannot change to imageView.setAlpha(): sticking with deprecated API for now
    private DrawerLayout setLogoAlpha() {
        VectorDrawableImageView vectorDrawableImageView = (VectorDrawableImageView) findViewById(R.id.philips_logo);
        DrawerLayout philipsDrawerLayout = (DrawerLayout) findViewById(R.id.philips_drawer_layout);
        if (vectorDrawableImageView != null)
            vectorDrawableImageView.setAlpha(229);
        return philipsDrawerLayout;
    }

    private void configureStatusBarViews() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }

    private boolean validateHamburger() {
        return findViewById(R.id.philips_drawer_layout) != null;
    }

    private void initFontIconLib() {
        try {
            FontIconTypefaceHolder.getTypeface();

        } catch (IllegalStateException e) {
            FontIconTypefaceHolder.init(getAssets(), "fonts/puicon.ttf");
        }
    }

    /**
     * (C) Koninklijke Philips N.V., 2015.
     * All rights reserved.
     */

    public static class UikitSpringBoardLayout extends LinearLayout {

        private Drawable selector;
        public static int STYLE_THEME = 1;
        int baseColor;

        int colorStyle = 1;
        int overlayColor = 0;
        Context mContext;

        public UikitSpringBoardLayout(Context context) {
            super(context);
        }

        public UikitSpringBoardLayout(Context context, AttributeSet attrs) {
            super(context, attrs);
            mContext = context;

            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.UikitSpringBoardLayout);
            colorStyle = typedArray.getInt(R.styleable.UikitSpringBoardLayout_opacityStyle, 0);
            typedArray.recycle();

            TypedArray ar = context.getTheme().obtainStyledAttributes(new int[]{R.attr.baseColor, R.attr.darkerColor});
            baseColor = ar.getInt(0, R.attr.baseColor);
            if (colorStyle == 0) {
                overlayColor = ar.getInt(1, R.attr.darkerColor);
            } else {

                overlayColor = ar.getInt(1, R.attr.darkerColor);
                overlayColor = Color.argb(89, Color.red(overlayColor), Color.green(overlayColor), Color.blue(overlayColor));
            }
            selector = getBackgroundSelector();
            ar.recycle();

        }

        public UikitSpringBoardLayout(Context context, AttributeSet attrs, int defStyle) {
            this(context, attrs);

        }

        @Override
        public void addView(View child, ViewGroup.LayoutParams params) {
            super.addView(child, params);
            selector = getBackgroundSelector();
            int version = Build.VERSION.SDK_INT;
            if (version < 16) {
                child.setBackgroundDrawable(selector);
            } else {
                child.setBackground(selector);
            }

        }


        private Drawable getBackgroundSelector() {

            if (colorStyle == 0) {
                GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();

                d.setColor(baseColor);
                StateListDrawable background = new StateListDrawable();
                background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
                background.addState(new int[]{}, d);

                return background;
            } else {
                GradientDrawable d = (GradientDrawable) getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();

                d.setColor(baseColor);
                StateListDrawable background = new StateListDrawable();
                background.addState(new int[]{android.R.attr.state_pressed}, getPressedDrawable());
                background.addState(new int[]{}, d);

                return background;
            }
        }

        private Drawable getPressedDrawable() {
            if (colorStyle == 0) {
                Drawable[] d = new Drawable[2];
                d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();
                ((GradientDrawable) d[0]).setColor(baseColor);

                d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_gridshape).mutate();
                ((GradientDrawable) d[1]).setColor(overlayColor);
                return new LayerListDrawable(d);

            } else {
                {
                    Drawable[] d = new Drawable[2];
                    d[0] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
                    ((GradientDrawable) d[0]).setColor(baseColor);

                    d[1] = getResources().getDrawable(R.drawable.uikit_springboard_layout_shape).mutate();
                    ((GradientDrawable) d[1]).setColor(overlayColor);
                    return new LayerListDrawable(d);

                }
            }

        }
    }
}

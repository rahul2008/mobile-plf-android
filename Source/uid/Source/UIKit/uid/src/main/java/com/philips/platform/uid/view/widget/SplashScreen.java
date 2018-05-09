/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.utils.UIDUtils;

/**
 * UID SplashScreen.
 * <p>
 * <P>UID SplashScreen is a view that is used to display application name and Icon with a themed background or an image background.
 * <P>Contents of the Splash Screen can be populated through the xml attributes as shown in the example below or through the API provided. By default, the splash screen follows
 * <P>the application theme if no background is set to the view. This can be overridden by setting the background view through xml attribute or through the setBackground API.
 * <BR>Usage, example:
 * <BR>    <com.philips.platform.uid.view.widget.SplashScreen
 * <BR>           android:layout_width="match_parent"
 * <BR>           android:layout_height="match_parent"
 * <BR>           app:uidSplashScreenAppName="@string/splash_screen_app_name"/>
 * <p>
 */
public class SplashScreen extends LinearLayout {

    private ImageView appIcon;
    private Label appName;

    public SplashScreen(Context context) {
        super(context);
    }

    public SplashScreen(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeViews(context, attrs);
        if (getBackground() == null)
            setGradientBackground();
    }

    private void setGradientBackground() {

        TypedArray typedArray = getContext().obtainStyledAttributes(new int[]{R.attr.uidLightDefaultLightCenterColor, R.attr.uidLightDefaultLightEdgeColor, R.attr.uidSplashScreenDefaultBackgroundColor});
        int startsColor = typedArray.getColor(0, 0);
        int endsColor = typedArray.getColor(1, 0);
        int backgroundColor = typedArray.getColor(2, 0);
        typedArray.recycle();
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.LEFT_RIGHT,
                new int[]{startsColor, endsColor});
        gradientDrawable.setGradientType(GradientDrawable.RADIAL_GRADIENT);
        gradientDrawable.setGradientRadius(Math.max(UIDUtils.getDeviceHeight(getContext()), UIDUtils.getDeviceWidth(getContext())) / 2);
        ColorDrawable colorDrawable = new ColorDrawable(backgroundColor);
        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{colorDrawable, gradientDrawable});
        this.setBackground(layerDrawable);
    }

    private void initializeViews(Context context, AttributeSet attrs) {

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View screenLayout = inflater.inflate(R.layout.uid_splash_screen, this);
        appIcon = (ImageView) screenLayout.findViewById(R.id.uid_splash_screen_icon);
        appName = (Label) screenLayout.findViewById(R.id.uid_splash_screen_app_name);

        resolveAppIcon(context);
        resolveAppName(context, attrs);
    }

    private void resolveAppName(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidSplashScreenAppName});
        CharSequence name = typedArray.getText(0);
        typedArray.recycle();

        appName.setText(name != null ? name : "");
    }


    private void resolveAppIcon(Context context) {

        DisplayMetrics metrics = new DisplayMetrics();
        ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(metrics);

        int size = Double.valueOf(metrics.heightPixels * 0.146).intValue();
        int sizeDP = Double.valueOf(size / metrics.density).intValue();

        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.uidSplashScreenDefaultShieldColor});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        FontIconDrawable drawable = new FontIconDrawable(context, getResources().getString(R.string.dls_shield), Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf"))
                .color(color)
                .sizeDp(sizeDP);

        appIcon.setImageDrawable(drawable);
        appIcon.setPadding(appIcon.getPaddingLeft(), appIcon.getPaddingTop(), appIcon.getPaddingRight(), size);
    }


    /**
     * This API will help you to set the exact string value to App name label. Alternatively app:uidSplashScreenAppName can be used in xml.
     *
     * @param text String to be set
     *             @since 3.0.1
     */
    public void setAppName(CharSequence text) {
        appName.setText(text);
    }

    /**
     * This API will help you to set the string to App name label through a string resource. Alternatively app:uidSplashScreenAppName can be used in xml.
     *
     * @param resID resourceId of string to be set
     *              @since 3.0.1
     */
    public void setAppName(@StringRes int resID) {
        appName.setText(resID);
    }

    /**
     * This API will help you to set the Splash screen logo using a drawable, by default Philips shield logo would be set.
     *
     * @param drawable drawable object to be set
     *                 @since 3.0.1
     */
    @SuppressWarnings("unused")
    public void setAppIcon(Drawable drawable) {
        appIcon.setImageDrawable(drawable);
    }

    /**
     * This API will help you to set the Splash screen logo using a drawable, by default Philips shield logo would be set.
     *
     * @param resID resourceId of drawable to be set
     *              @since 3.0.1
     */
    @SuppressWarnings("unused")
    public void setAppIcon(int resID) {
        appIcon.setImageResource(resID);
    }

    /**
     * This API will help you to set the Splash screen logo using a Bitmap, by default Philips shield logo would be set.
     *
     * @param bitmap bitmap to be set
     *               @since 3.0.1
     */
    @SuppressWarnings("unused")
    public void setAppIcon(Bitmap bitmap) {
        appIcon.setImageBitmap(bitmap);
    }
}

/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.uid.view.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.philips.platform.uid.R;
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.thememanager.UIDHelper;

/**
 * UID AboutScreen.
 * <p>
 * <P>UID AboutScreen is a view that is used to display the contents about an application like appName, Version, Copyright, terms and conditions, privacy policy and disclosure statement.
 * <P>Contents of the About Screen can be populated through the xml attributes as shown in the example below or through the api provided.
 * <BR>Usage, example:
 * <BR>    <com.philips.platform.uid.view.widget.AboutScreen
 * <BR>           android:layout_width="match_parent"
 * <BR>           android:layout_height="match_parent"
 * <BR>           android:id="@+id/catalog_about_screen"
 * <BR>           app:uidAboutScreenAppName="@string/about_screen_appname"
 * <BR>           app:uidAboutScreenAppVersion="@string/about_screen_appversion"
 * <BR>           app:uidAboutScreenCopyright="@string/about_screen_copyright"
 * <BR>           app:uidAboutScreenTerms="@string/about_screen_terms"
 * <BR>           app:uidAboutScreenPrivacy="@string/about_screen_privacy"
 * <BR>           app:uidAboutScreenDisclosure="@string/about_screen_disclosure"/>
 * <P>
 */

public class AboutScreen extends ScrollView {

    private ImageView appIcon;
    private Label appName;
    private Label appVersion;
    private Label copyright;
    private Label terms;
    private Label privacy;
    private Label disclosure;

    public AboutScreen(Context context) {
        this(context, null);
    }

    public AboutScreen(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.uidAboutScreenStyle);
    }

    public AboutScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        initializeViews(context, attrs);
    }

    private void initializeViews(Context context, AttributeSet attrs) {

        LayoutInflater inflater = LayoutInflater.from(context).cloneInContext(UIDHelper.getPopupThemedContext(context));
        ViewGroup screenLayout = (ViewGroup) inflater.inflate(R.layout.uid_about_screen, this);
        appIcon = (ImageView) screenLayout.findViewById(R.id.uid_about_screen_icon);
        appName = (Label) screenLayout.findViewById(R.id.uid_about_screen_app_name);
        appVersion = (Label) screenLayout.findViewById(R.id.uid_about_screen_version);
        copyright = (Label) screenLayout.findViewById(R.id.uid_about_screen_copyright);
        terms = (Label) screenLayout.findViewById(R.id.uid_about_screen_terms);
        privacy = (Label) screenLayout.findViewById(R.id.uid_about_screen_privacy);
        disclosure = (Label) screenLayout.findViewById(R.id.uid_about_screen_disclosure);

        resolveAppIcon(context);

        resolveXmlAttributes(context, attrs);

    }

    private void resolveXmlAttributes(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, new int[]{R.attr.uidAboutScreenAppName, R.attr.uidAboutScreenAppVersion, R.attr.uidAboutScreenCopyright,
                R.attr.uidAboutScreenTerms, R.attr.uidAboutScreenPrivacy, R.attr.uidAboutScreenDisclosure});
        CharSequence name = typedArray.getText(0);
        CharSequence version = typedArray.getText(1);
        CharSequence copyrightText = typedArray.getText(2);
        CharSequence termsText = typedArray.getText(3);
        CharSequence privacyText = typedArray.getText(4);
        CharSequence disclosureText = typedArray.getText(5);
        typedArray.recycle();

        appName.setText(name != null ? name : "");
        appVersion.setText(version != null ? version : "");
        copyright.setText(copyrightText != null ? copyrightText : "");
        terms.setText(termsText != null ? termsText : "");
        privacy.setText(privacyText != null ? privacyText : "");
        disclosure.setText(disclosureText != null ? disclosureText : "");
    }


    private void resolveAppIcon(Context context) {

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        windowManager.getDefaultDisplay().getRealSize(point);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);

        int size = Double.valueOf(Math.max(point.x, point.y) * 0.09).intValue();
        int sizeDP = Double.valueOf(size / displayMetrics.density).intValue();

        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.uidAboutScreenDefaultShieldColor});
        int color = typedArray.getColor(0, 0);
        typedArray.recycle();
        FontIconDrawable drawable = new FontIconDrawable(context, getResources().getString(R.string.dls_shield), Typeface.createFromAsset(context.getAssets(), "fonts/iconfont.ttf"))
                .color(color)
                .sizeDp(sizeDP);

        appIcon.setImageDrawable(drawable);
        appIcon.setPadding(0, size, 0, size);
    }

    /**
     * This API will help you to set the exact string value to App name label. Alternatively app:uidAboutScreenAppName can be used in xml.
     *
     * @param text String to be set
     */
    public void setAppName(CharSequence text) {
        appName.setText(text);
    }

    /**
     * This API will help you to set the exact string value to App version label. Alternatively app:uidAboutScreenAppVersion can be used in xml.
     *
     * @param text String to be set
     */
    public void setAppVersion(CharSequence text) {
        appVersion.setText(text);
    }

    /**
     * This API will help you to set the exact string value to Copyright label. Alternatively app:uidAboutScreenCopyright can be used in xml.
     *
     * @param text String to be set
     */
    public void setCopyright(CharSequence text) {
        copyright.setText(text);
    }

    /**
     * This API will help you to set the exact string value to Terms label. Alternatively app:uidAboutScreenTerms can be used in xml.
     *
     * @param text String to be set
     */
    public void setTerms(CharSequence text) {
        terms.setText(text);
    }

    /**
     * This API will help you to set the exact string value to Privacy label. Alternatively app:uidAboutScreenPrivacy can be used in xml.
     *
     * @param text String to be set
     */
    public void setPrivacy(CharSequence text) {
        privacy.setText(text);
    }

    /**
     * This API will help you to set the exact string value to Disclosure label. Alternatively app:uidAboutScreenDisclosure can be used in xml.
     *
     * @param text String to be set
     */
    public void setDisclosure(CharSequence text) {
        disclosure.setText(text);
    }

    /**
     * This API will help you to set the string to App name label through a string resource. Alternatively app:uidAboutScreenAppName can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setAppName(@StringRes int resID) {
        appName.setText(resID);
    }

    /**
     * This API will help you to set the string to Version label through a string resource. Alternatively app:uidAboutScreenAppVersion can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setAppVersion(@StringRes int resID) {
        appVersion.setText(resID);
    }

    /**
     * This API will help you to set the string to Copyright label through a string resource. Alternatively app:uidAboutScreenCopyright can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setCopyright(@StringRes int resID) {
        copyright.setText(resID);
    }

    /**
     * This API will help you to set the string to Terms label through a string resource. Alternatively app:uidAboutScreenTerms can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setTerms(@StringRes int resID) {
        terms.setText(resID);
    }

    /**
     * This API will help you to set the string to Privacy label through a string resource. Alternatively app:uidAboutScreenPrivacy can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setPrivacy(@StringRes int resID) {
        privacy.setText(resID);
    }

    /**
     * This API will help you to set the string to Disclosure label through a string resource. Alternatively app:uidAboutScreenDisclosure can be used in xml.
     *
     * @param resID resourceId of string to be set
     */
    public void setDisclosure(@StringRes int resID) {
        disclosure.setText(resID);
    }

    /**
     * This API will help you to set the About screen logo using a drawable, by default Philips shield logo would be set.
     *
     * @param drawable drawable object to be set
     */
    @SuppressWarnings("unused")
    public void setAppIcon(Drawable drawable){
        appIcon.setImageDrawable(drawable);
    }

    /**
     * This API will help you to set the About screen logo using a drawable, by default Philips shield logo would be set.
     *
     * @param resID resourceId of drawable to be set
     */
    @SuppressWarnings("unused")
    public void setAppIcon(int resID){
        appIcon.setImageResource(resID);
    }

    /**
     * This API will help you to set the About screen logo using a Bitmap, by default Philips shield logo would be set.
     *
     * @param bitmap bitmap to be set
     */
    @SuppressWarnings("unused")
    public void setAppIcon(Bitmap bitmap){
        appIcon.setImageBitmap(bitmap);
    }
}

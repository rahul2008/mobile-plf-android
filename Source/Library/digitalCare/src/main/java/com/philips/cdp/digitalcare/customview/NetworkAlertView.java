/**
 * @author naveen@philips.com
 *
 * Copyright (c) 2016 Philips. All rights reserved.
 *
 * @description Network Notification view used during Connection not available
 * in application component wise announcement.
 * @Since Apr 7, 2015
 */
package com.philips.cdp.digitalcare.customview;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;


@SuppressLint("NewApi")
public class NetworkAlertView {

    AlertDialog mAlertDialog = null;
    private ProgressDialog mProgressDialog = null;
    private Dialog mDialog = null;
    private Activity mActivity = null;

    /**
     * @param title      : String
     * @param message    : String
     * @param buttontext : String
     */
    public void showAlertBox(Activity activity, String title, String message,
                             String buttontext) {

        if (mAlertDialog == null) {

            mAlertDialog = new AlertDialog.Builder(activity)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(android.R.string.yes,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    mAlertDialog.dismiss();

                                }
                            }).show();

        }

    }

    public void showEULAAlertBox(Activity activity, String url) {
        LinearLayout mLayoutContainer = new LinearLayout(activity);
        mLayoutContainer.setOrientation(LinearLayout.VERTICAL);
        mLayoutContainer.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mActivity = activity;
        int ID = 10;
        WebView mWebView = new WebView(activity);
        mWebView.setId(ID);
        mWebView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        mLayoutContainer.addView(mWebView);

        if (mProgressDialog == null)
            mProgressDialog = new ProgressDialog(activity);

        if (mDialog == null)
            mDialog = new Dialog(activity);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(mLayoutContainer);
        WebView mView = (WebView) mDialog.findViewById(ID);
        mView.getSettings().setJavaScriptEnabled(true);
        if (Build.VERSION.SDK_INT >= 19) {
            mView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            mView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mView.getSettings().setAppCacheEnabled(true);
        mView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        mView.setWebViewClient(new WebViewClient());
        mView.loadUrl(url);

        mDialog.setCancelable(true);
        mDialog.show();
    }



	/*
     * public void showNetworkAlert(final Activity mContext) {
	 * 
	 * float mAlertbackGroundHeight = mContext.getResources().getDimension(
	 * R.dimen.support_btn_height); RelativeLayout mParent = new
	 * RelativeLayout(mContext);
	 * 
	 * RelativeLayout mToastContainer = new RelativeLayout(mContext);
	 * RelativeLayout.LayoutParams mContainerParams = new
	 * RelativeLayout.LayoutParams( RelativeLayout.LayoutParams.MATCH_PARENT,
	 * (int) Math.ceil(mAlertbackGroundHeight));
	 * mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
	 * mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
	 * mContainerParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
	 * 
	 * GradientDrawable mBackground = new GradientDrawable();
	 * mBackground.setColor(mContext.getResources().getColor(
	 * R.color.NetworkAlertView_background_color));
	 * mBackground.setCornerRadii(new float[] { 10, 10, 10, 10, 0, 0, 0, 0 });
	 * mToastContainer.setBackground(mBackground);
	 * 
	 * TextView mTextView = new TextView(mContext); RelativeLayout.LayoutParams
	 * mTextViewParams = new RelativeLayout.LayoutParams( -2, -2);
	 * mTextViewParams.addRule(RelativeLayout.CENTER_IN_PARENT);
	 * mTextView.setTextColor(mContext.getResources().getColor(
	 * R.color.NetworkAlertView_text_color));
	 * mTextView.setText(mContext.getResources().getString(
	 * R.string.no_internet)); // TODO: This is giving jenkins crash. //
	 * Typeface mTypeFace = Typeface.createFromAsset(mContext.getAssets(), //
	 * "fonts/centralesans-book.otf"); mTextView.setTypeface(null,
	 * Typeface.BOLD); mTextView.setLayoutParams(mTextViewParams);
	 * 
	 * mToastContainer.setLayoutParams(mContainerParams);
	 * mToastContainer.addView(mTextView); mParent.addView(mToastContainer);
	 * 
	 * mParent.setAnimation(setAnimation());
	 * 
	 * mContext.addContentView(mParent, new RelativeLayout.LayoutParams(
	 * LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)); }
	 * 
	 * private Animation setAnimation() { TranslateAnimation mAnim = new
	 * TranslateAnimation( TranslateAnimation.ABSOLUTE, 0.0f,
	 * TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 0.0f,
	 * TranslateAnimation.ABSOLUTE, 300); mAnim.setFillAfter(true);
	 * mAnim.setDuration(4000l); mAnim.setInterpolator(new
	 * AccelerateDecelerateInterpolator()); return mAnim; }
	 */

}

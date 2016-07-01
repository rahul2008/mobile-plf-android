/*import com.philips.cdp.horizontal.RequestManager;
import com.philips.cdp.network.listeners.AssetListener;
import com.philips.cdp.serviceapi.productinformation.assets.Assets;*/

/**
 * ProductDetailsFragment will help to show product details.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 16 Jan 2015
 * <p/>
 * Copyright (c) 2016 Philips. All rights reserved.
 */

package com.philips.cdp.digitalcare.productdetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.customview.DigitalCareFontTextView;
import com.philips.cdp.digitalcare.homefragment.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.listeners.prxSummaryCallback;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.prx.PrxWrapper;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ProductDetailsFragment extends DigitalCareBaseFragment implements
        OnClickListener {

    private static String TAG = ProductDetailsFragment.class.getSimpleName();
    private static ImageView mProductImageTablet = null;
    private static int mSmallerResolution = 0;
    private static boolean isTablet = false;
    private static int mHeight = 0;
    private static int mScrollPosition = 0;
    private static Activity mActivity = null;
    private RelativeLayout mFirstContainer = null;
    private LinearLayout.LayoutParams mFirstContainerParams = null;
    private LinearLayout.LayoutParams mSecondContainerParams = null;
    private LinearLayout mProdButtonsParent = null;
    private LinearLayout mProdVideoContainer = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private DigitalCareFontTextView mProductTitle = null;
    private DigitalCareFontTextView mProductVideoHeader = null;
    private DigitalCareFontTextView mCtn = null;
    private ImageView mProductImage = null;
    private HorizontalScrollView mVideoScrollView = null;
    private String mManualPdf = null;
    private String mProductPage = null;
    private ViewProductDetailsModel mViewProductDetailsModel = null;
    private int mBiggerResolution = 0;
    private LinearLayout.LayoutParams mScrollerLayoutParams = null;
    private LinearLayout.LayoutParams mProductVideoHeaderParams = null;
    private PrxWrapper mPrxWrapper = null;
    private int mSdkVersion = 0;
    private RelativeLayout mManualRelativeLayout = null;
    private String mManualButtonTitle = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        mSdkVersion = Build.VERSION.SDK_INT;
        View view = inflater.inflate(R.layout.consumercare_fragment_view_product,
                container, false);
        if (getActivity() != null)
            mActivity = getActivity();

        try {
            AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_VIEW_PRODUCT_DETAILS,
                    getPreviousName());
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "IllegaleArgumentException : " + e);
        }
        getDisplayWidth();
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mFirstContainer = (RelativeLayout) mActivity.findViewById(
                R.id.toplayout);
        mProdButtonsParent = (LinearLayout) mActivity.findViewById(
                R.id.prodbuttonsParent);

        mProdVideoContainer = (LinearLayout) mActivity.findViewById(
                R.id.videoContainerParent);

        mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
                .getLayoutParams();
        mSecondContainerParams = (LinearLayout.LayoutParams) mProdButtonsParent
                .getLayoutParams();
        mActionBarMenuIcon = (ImageView) mActivity.findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) mActivity.findViewById(R.id.back_to_home_img);

        mProductImageTablet = (ImageView) mActivity.findViewById(R.id.productImageTablet);
        mProductImage = (ImageView) mActivity.findViewById(R.id.productimage);

        mProductTitle = (DigitalCareFontTextView) mActivity.findViewById(R.id.name);
        mProductVideoHeader = (DigitalCareFontTextView) mActivity.findViewById(R.id.productVideoText);
        mCtn = (DigitalCareFontTextView) mActivity.findViewById(R.id.variant);
        mVideoScrollView = (HorizontalScrollView) mActivity.findViewById(R.id.videoScrollView);
        mScrollerLayoutParams = (LinearLayout.LayoutParams) mVideoScrollView
                .getLayoutParams();
        mProductVideoHeaderParams = (LinearLayout.LayoutParams) mProductVideoHeader
                .getLayoutParams();

        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        Configuration config = getResources().getConfiguration();
        createProductDetailsMenu();
        updateViewsWithData();
        setViewParams(config);

        mVideoScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {

            @Override
            public void onScrollChanged() {
                mScrollPosition = mVideoScrollView.getScrollX(); //for horizontalScrollView
            }
        });
/*
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPRX();
            }
        });
  */

    }

    private void initView(List<String> mVideoLength) throws NullPointerException {

        DigiCareLogger.d(TAG, "Video's Length : " + mVideoLength.size());
        if (mVideoLength != null && mVideoLength.size() > 0) {
            mProductVideoHeader.setVisibility(View.VISIBLE);
        } else {
            return;
        }

        for (int i = 0; i < mVideoLength.size(); i++) {
            View child = mActivity.getLayoutInflater().inflate(R.layout.consumercare_viewproduct_video_view, null);
            ImageView videoThumbnail = (ImageView) child.findViewById(R.id.videoContainer);
            ImageView videoPlay = (ImageView) child.findViewById(R.id.videoPlay);
            ImageView videoLeftArrow = (ImageView) child.findViewById(R.id.videoLeftArrow);
            ImageView videoRightArrow = (ImageView) child.findViewById(R.id.videoRightArrow);

            RelativeLayout.LayoutParams param = (RelativeLayout.LayoutParams) videoThumbnail
                    .getLayoutParams();
//            RelativeLayout.LayoutParams paramLeftArrow = (RelativeLayout.LayoutParams) videoLeftArrow
//                    .getLayoutParams();

//            RelativeLayout.LayoutParams paramRightArrow = (RelativeLayout.LayoutParams) videoRightArrow
//                    .getLayoutParams();

            if (mActivity != null) {
                float density = mActivity.getResources().getDisplayMetrics().density;

                if (mVideoLength.size() > 1 && (mVideoLength.size() - 1) != i && isTablet) {
                    param.rightMargin = (int) (25 * density);
                    videoThumbnail.setLayoutParams(param);
                }

//            paramLeftArrow.leftMargin =
//                    (int) (50 * density) + (int) getResources().getDimension(R.dimen.activity_margin);

//            videoRightArrow.setLayoutParams(paramRightArrow);
//            videoLeftArrow.setLayoutParams(paramLeftArrow);

                videoLeftArrow.bringToFront();
                videoRightArrow.bringToFront();

                addNewVideo(i, mVideoLength.get(i), child, videoThumbnail, videoPlay, videoLeftArrow, videoRightArrow);
            }
        }
    }

    private int getDisplayWidth() {
        DisplayMetrics metrics = new DisplayMetrics();
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        float density = metrics.density;

        if (widthPixels > heightPixels) {
            mSmallerResolution = heightPixels;
            mBiggerResolution = widthPixels;
        } else {
            mSmallerResolution = widthPixels;
            mBiggerResolution = heightPixels;
        }
        mHeight = mSmallerResolution;

        isTablet = ((float) mSmallerResolution / density > 360);

        if (isTablet) {
            return (int) mActivity.getResources().getDimension(R.dimen.view_prod_details_video_height);
        }

        return mSmallerResolution;
    }

    protected void loadVideoThumbnail(final ImageView imageView, final String thumbnail) {
//        String thumbnail = imagePath.replace("/content/", "/image/") + "?wid=" + getDisplayWidth() + "&amp;";

        ImageRequest request = new ImageRequest(thumbnail,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }
                }, 0, 0, null, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        imageView.setImageBitmap(addBlankThumbnail());
                    }
                });

        RequestQueue imageRequestQueue = Volley.newRequestQueue(getContext());
        imageRequestQueue.add(request);
    }


    private Bitmap addBlankThumbnail() {
        int height = 0;
        if (isTablet) {
            height = (getDisplayWidth() / 2) + 13;
        } else {
            height = (getDisplayWidth() / 2) + 46;
        }
        int width = 0;

        try {
            width = getDisplayWidth();
        } catch (NullPointerException e) {
            width = (int) mActivity.getResources().getDimension(R.dimen.view_prod_details_video_height);
        }

        Bitmap imageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        imageBitmap.eraseColor(Color.BLACK);
        return imageBitmap;
    }

    /*private View.OnClickListener videoModel = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(strMyVideo));
            intent.setDataAndType(Uri.parse(strMyVideo), "video/mp4");
            activity.startActivity(intent);
        }
    };*/

    private void addNewVideo(int counter, final String video, View child, ImageView videoThumbnail, ImageView videoPlay,
                             ImageView videoLeftArrow, ImageView videoRightArrow) {
        String tag = counter + "";
        final String thumbnail = video.replace("/content/", "/image/") + "?wid=" + getDisplayWidth() + "&amp;";

        loadVideoThumbnail(videoThumbnail, thumbnail);
        child.setTag(tag);
        //    videoPlay.setOnClickListener(videoModel);
        videoPlay.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                Map<String, Object> contextData = new HashMap<String, Object>();
                contextData.put(AnalyticsConstants.ACTION_KEY_VIEW_PRODUCT_VIDEO_NAME, video);
                AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_KEY_VIEW_PRODUCT_VIDEO_START, contextData);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.parse(video), "video/mp4");
                mActivity.startActivity(intent);
            }
        });

        if (isTablet) {
            videoLeftArrow.setVisibility(View.GONE);
            videoRightArrow.setVisibility(View.GONE);
        }
        videoLeftArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mScrollPosition >= 400) {
                    mVideoScrollView.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            mVideoScrollView.smoothScrollTo((mScrollPosition - 400), 0);
                        }
                    }, 5);
                }
            }
        });

        videoRightArrow.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mVideoScrollView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mVideoScrollView.smoothScrollTo((mScrollPosition + 400), 0);
                    }
                }, 5);
//                    mVideoScrollView.scrollTo(mScrollPosition, (mScrollPosition + 50));
            }
        });
        mProdVideoContainer.addView(child);
    }

    private void createProductDetailsMenu() {
        TypedArray titles = getResources().obtainTypedArray(R.array.product_menu_title);

        for (int i = 0; i < titles.length(); i++) {
            createButtonLayout(titles.getResourceId(i, 0));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    protected void updateViewsWithData() {
        mViewProductDetailsModel = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        if (mViewProductDetailsModel != null) {
            if (mViewProductDetailsModel.getProductName() != null) {
                onUpdateSummaryData();
                requestPRXAssetData();
            } else
                showAlert(getResources().getString(R.string.no_data_available));
        } else {
            showAlert(getResources().getString(R.string.no_data_available));
        }
    }

    protected void requestPRXAssetData() {
        mPrxWrapper = new PrxWrapper(mActivity, new prxSummaryCallback() {
            @Override
            public void onResponseReceived(SummaryModel isAvailable) {
                if (getContext() != null)
                    onUpdateAssetData();
            }
        });

        mPrxWrapper.executePrxAssetRequestWithSummaryData(mViewProductSummaryModel);
    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
            mProductVideoHeaderParams.leftMargin = mProductVideoHeaderParams.rightMargin = mLeftRightMarginPort;
            if (isTablet) {
                mVideoScrollView.setPadding(mProdButtonsParent.getPaddingLeft(), 0, mProdButtonsParent.getPaddingRight(), 0);
                mScrollerLayoutParams.leftMargin = mScrollerLayoutParams.rightMargin = mLeftRightMarginPort;
                mScrollerLayoutParams.bottomMargin = 0;
            }
        } else {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
            mProductVideoHeaderParams.leftMargin = mProductVideoHeaderParams.rightMargin = mLeftRightMarginLand;

            if (isTablet) {
                mVideoScrollView.setPadding(mProdButtonsParent.getPaddingLeft(), 0, mProdButtonsParent.getPaddingRight(), 0);
                mScrollerLayoutParams.leftMargin = mScrollerLayoutParams.rightMargin = mLeftRightMarginLand;
                mScrollerLayoutParams.bottomMargin = 0;
            }
        }

        mFirstContainer.setLayoutParams(mFirstContainerParams);
        mProdButtonsParent.setLayoutParams(mSecondContainerParams);
        mVideoScrollView.setLayoutParams(mScrollerLayoutParams);
        mProductVideoHeader.setLayoutParams(mProductVideoHeaderParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }


    /**
     * Create RelativeLayout at runTime. RelativeLayout will have button and
     * image together.
     */
    private void createButtonLayout(int buttonTitleResId) {
        String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);

        float density = getResources().getDisplayMetrics().density;
        String packageName = mActivity.getPackageName();

        int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);

        RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        Button button = createButton(density, title);
        relativeLayout.addView(button);
        setButtonParams(button);
        mProdButtonsParent.addView(relativeLayout);
        setRelativeLayoutParams(relativeLayout, density);
        /*
         * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
        relativeLayout.setTag(buttonTitle);
        relativeLayout.setOnClickListener(this);

        mViewProductDetailsModel = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        String mFilePath = null;

        if (mViewProductDetailsModel != null) {
            mFilePath = mViewProductDetailsModel.getManualLink();
        }

        if (mFilePath == null && buttonTitle.equalsIgnoreCase(
                getResources().getResourceEntryName(R.string.product_download_manual))) {
            relativeLayout.setVisibility(View.GONE);
            mManualRelativeLayout = relativeLayout;
            mManualButtonTitle = buttonTitle;
        }
    }

    @SuppressLint("NewApi")
    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        RelativeLayout relativeLayout = new RelativeLayout(mActivity);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (mActivity.getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        relativeLayout.setLayoutParams(params);
        relativeLayout
                .setBackgroundResource(R.drawable.consumercare_selector_option_button_bg);
        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();
        param.topMargin = (int) (15 * density);
        relativeLayout.setLayoutParams(param);
    }

    @SuppressLint("NewApi")
    private Button createButton(float density, int title) {
        Button button = new Button(mActivity, null, R.style.fontButton);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (mActivity.getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);

//        button.setGravity(Gravity.START | Gravity.CENTER);
        button.setPadding((int) (20 * density), 0, 0, 0);
        if (Build.VERSION.SDK_INT < 23) {
            button.setTextAppearance(getActivity(), R.style.fontButton);
        } else {
            button.setTextAppearance(R.style.fontButton);
        }
        Typeface buttonTypeface = Typeface.createFromAsset(mActivity.getAssets(), "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
        button.setGravity(Gravity.CENTER);
        button.setText(title);
        return button;
    }

    private void setButtonParams(Button button) {
        RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
                .getLayoutParams();
        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        button.setLayoutParams(buttonParams);
    }

    @Override
    public void onClick(View view) {

        mViewProductDetailsModel = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        String tag = (String) view.getTag();

        if (DigitalCareConfigManager.getInstance()
                .getProductMenuListener() != null) {
            DigitalCareConfigManager.getInstance()
                    .getProductMenuListener().onProductMenuItemClicked(tag);
        }

        if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_download_manual))) {
            Locale locale = DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack();
            String country = locale.getCountry();
            String language = locale.getLanguage();
            String mFilePath = mViewProductDetailsModel.getManualLink();
            DigiCareLogger.d(TAG, "Manual name : "+ mFilePath);

            // creating the name of the manual. So that Same manual should not be downloaded again and again.
            String pdfName = mViewProductDetailsModel.getProductName() + language + '_' + country + ".pdf";
            if ((mFilePath != null) && (mFilePath != "")) {
                if (isConnectionAvailable()) {
                    DownloadAndShowPDFHelper downloadAndShowPDFHelper = new DownloadAndShowPDFHelper();
                    downloadAndShowPDFHelper.downloadAndOpenPDFManual(getActivity(), mFilePath, pdfName, isConnectionAvailable());
//                    showFragment(new ProductManualFragment());
                }
            } else {
                showAlert(getResources().getString(R.string.no_data));
            }

        } else if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_information))) {
            if (isConnectionAvailable()) {
                showFragment(new ProductInformationFragment());
            }
        }
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product_info);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_VIEW_PRODUCT_DETAILS;
    }


    public void onUpdateSummaryData() {

        if (mViewProductDetailsModel.getProductName() != null)
            mProductTitle.setText(mViewProductDetailsModel.getProductName());
        if (mViewProductDetailsModel.getCtnName() != null)
            mCtn.setText(mViewProductDetailsModel.getCtnName());
        if (mViewProductDetailsModel.getProductImage() != null) {
            ImageRequest request = new ImageRequest(mViewProductDetailsModel.getProductImage(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            if (isTablet) {
                                mProductImageTablet.setVisibility(View.VISIBLE);
                                mProductImageTablet.setImageBitmap(bitmap);
                            } else {
                                mProductImage.setVisibility(View.VISIBLE);
                                mProductImage.setImageBitmap(bitmap);
                            }
                        }
                    }, 0, 0, null, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            // mProductImage.setImageResource(R.drawable.image_load_error);
                            Map<String, Object> contextData = new HashMap<String, Object>();
                            contextData.put(AnalyticsConstants.ACTION_KEY_TECHNICAL_ERROR, error.getMessage());
                            contextData.put(AnalyticsConstants.ACTION_KEY_URL, mViewProductDetailsModel.getProductImage());
                            AnalyticsTracker.trackAction(AnalyticsConstants.ACTION_SET_ERROR, contextData);
                        }
                    });
            RequestQueue imageRequestQueue = Volley.newRequestQueue(getContext());
            imageRequestQueue.add(request);
        }
    }

    public void onUpdateAssetData() {
        ViewProductDetailsModel viewProductDetailsModel = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
        mManualPdf = viewProductDetailsModel.getManualLink();
        if (mManualPdf != null)
            viewProductDetailsModel.setManualLink(mManualPdf);
        mProductPage = viewProductDetailsModel.getProductInfoLink();
        if (mProductPage != null)
            viewProductDetailsModel.setProductInfoLink(mProductPage);
        DigiCareLogger.d(TAG, "Manual Link : " + mManualPdf);
        DigiCareLogger.d(TAG, "Philips Page Link : " + mProductPage);
        List<String> productVideos = viewProductDetailsModel.getVideoLinks();
        if (productVideos != null)
            initView(viewProductDetailsModel.getVideoLinks());
        DigitalCareConfigManager.getInstance().setViewProductDetailsData(viewProductDetailsModel);

        if (mManualPdf != null && mManualButtonTitle != null) {
            if (mManualButtonTitle.equalsIgnoreCase(
                    getResources().getResourceEntryName(R.string.product_download_manual))) {
                mManualRelativeLayout.setVisibility(View.VISIBLE);
            }
        }
    }
}
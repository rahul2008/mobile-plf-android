package com.philips.cdp.digitalcare.productdetails;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Toast;

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
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.productdetails.model.listener.PrxCallback;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
/*import com.philips.cdp.horizontal.RequestManager;
import com.philips.cdp.network.listeners.AssetListener;
import com.philips.cdp.serviceapi.productinformation.assets.Assets;*/

/**
 * ProductDetailsFragment will help to show product details.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 16 Jan 2015
 */
public class ProductDetailsFragment extends DigitalCareBaseFragment implements PrxCallback,
        OnClickListener {

    private static String TAG = ProductDetailsFragment.class.getSimpleName();

    private RelativeLayout mFirstContainer = null;
    private LinearLayout.LayoutParams mFirstContainerParams = null;
    private LinearLayout.LayoutParams mSecondContainerParams = null;
    private LinearLayout mProdButtonsParent = null;
    private LinearLayout mProdVideoContainer = null;
    private ImageView mActionBarMenuIcon = null;
    private ImageView mActionBarArrow = null;
    private DigitalCareFontTextView mProductTitle = null;
    private DigitalCareFontTextView mCtn = null;
    private ImageView mProductImage = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_view_product,
                container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        initPrxRequest();
        mFirstContainer = (RelativeLayout) getActivity().findViewById(
                R.id.toplayout);
        mProdButtonsParent = (LinearLayout) getActivity().findViewById(
                R.id.prodbuttonsParent);

        mProdVideoContainer = (LinearLayout) getActivity().findViewById(
                R.id.videoContainer);

        mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
                .getLayoutParams();
        mSecondContainerParams = (LinearLayout.LayoutParams) mProdButtonsParent
                .getLayoutParams();
        mActionBarMenuIcon = (ImageView) getActivity().findViewById(R.id.home_icon);
        mActionBarArrow = (ImageView) getActivity().findViewById(R.id.back_to_home_img);
        mProductImage = (ImageView) getActivity().findViewById(R.id.productimage);
        mProductTitle = (DigitalCareFontTextView) getActivity().findViewById(R.id.name);
        mCtn = (DigitalCareFontTextView) getActivity().findViewById(R.id.variant);
        hideActionBarIcons(mActionBarMenuIcon, mActionBarArrow);
        Configuration config = getResources().getConfiguration();
        setViewParams(config);

        createProductDetailsMenu();

        initView();

        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_PRODCUT_DETAILS,
                getPreviousName());
/*
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                initPRX();
            }
        });
  */
    }

    private void initView() {
        int count = 4;

        for (int i = 0; i < count; i++) {
            addNewVideo(i + "");
        }
    }

    private void addNewVideo(String tag) {
        View child = getActivity().getLayoutInflater().inflate(R.layout.viewproduct_video_view, null);
        child.setTag(tag);
        child.setOnClickListener(videoModel);
        mProdVideoContainer.addView(child);
    }

    private View.OnClickListener videoModel = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            Toast.makeText(getActivity(), "Video : " + v.getTag(), Toast.LENGTH_SHORT).show();
        }
    };

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


    protected void initPrxRequest() {
        PrxProductData mPrxDataParse = new PrxProductData(getActivity().getApplicationContext(), this);
    }


    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
        } else {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
        }
        mFirstContainer.setLayoutParams(mFirstContainerParams);
        mProdButtonsParent.setLayoutParams(mSecondContainerParams);
    }

    @Override
    public void onResume() {
        super.onResume();
        enableActionBarLeftArrow(mActionBarMenuIcon, mActionBarArrow);
    }


  /*  protected void initPRX() {
        final String COUNTRY_URL = "www.philips.co.uk";
        final String SECTOR = "B2C";
        final String LANGUAGE = "en";
        final String COUNTRY = "GB";
        final String CATALOGCODE = "CONSUMER";
        final String CTN = "RQ1250/17";

        RequestManager mRequestManager = RequestManager.getInstance();
        mRequestManager.setServerInfo(COUNTRY_URL);
        mRequestManager.setSectorCode(SECTOR);
        mRequestManager.setLanguageCode(LANGUAGE);
        mRequestManager.setCountryCode(COUNTRY);
        mRequestManager.setCatalogCode(CATALOGCODE);
        mRequestManager.setCTN(CTN);

        Assets mAssets = new Assets(new AssetListener() {
            @Override
            public void onSuccess(Assets assets) {
                DigiCareLogger.d(TAG, "Passed : " + assets.isSuccess());
            }

            @Override
            public void onFailed(String s) {
                DigiCareLogger.d(TAG, "Failed : " + s);
            }
        });
    }

   */

    /**
     * Create RelativeLayout at runTime. RelativeLayout will have button and
     * image together.
     */
    private void createButtonLayout(int buttonTitleResId) {
        String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);

        float density = getResources().getDisplayMetrics().density;
        String packageName = getActivity().getPackageName();

        int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);

        RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        Button button = createButton(density, buttonTitle);
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
    }

    @SuppressLint("NewApi")
    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        relativeLayout.setLayoutParams(params);
        relativeLayout
                .setBackgroundResource(R.drawable.selector_option_button_bg);

        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();
        param.topMargin = (int) (15 * density);
        relativeLayout.setLayoutParams(param);
    }

    private Button createButton(float density, String title) {
        Button button = new Button(getActivity(), null, R.style.fontButton);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);

        button.setGravity(Gravity.START | Gravity.CENTER);
        button.setPadding((int) (20 * density), 0, 0, 0);
        button.setTextAppearance(getActivity(), R.style.fontButton);
        Typeface buttonTypeface = Typeface.createFromAsset(getActivity().getAssets(), "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
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

        String tag = (String) view.getTag();

        boolean actionTaken = false;
        if (DigitalCareConfigManager.getInstance()
                .getProductMenuListener() != null) {
            DigitalCareConfigManager.getInstance()
                    .getProductMenuListener().onProductMenuItemClicked(tag);
        }

        if (actionTaken) {
            return;
        }

        if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_open_manual))) {
        } else if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_download_manual))) {
        } else if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_information))) {
        }
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product_info);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_PRODCUT_DETAILS;
    }

    @Override
    public void onSummaryDataReceived(ViewProductDetailsModel object) {

        if (object.getmProductName() != null)
            mProductTitle.setText(object.getmProductName());
        if (object.getmCtnName() != null)
            mCtn.setText(object.getmCtnName());
        if (object.getmProductImage() != null) {
            ImageRequest request = new ImageRequest(object.getmProductImage(),
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap bitmap) {
                            mProductImage.setImageBitmap(bitmap);
                        }
                    }, 0, 0, null,
                    new Response.ErrorListener() {
                        public void onErrorResponse(VolleyError error) {
                            // mProductImage.setImageResource(R.drawable.image_load_error);
                        }
                    });

            RequestQueue imageRequestQueue = Volley.newRequestQueue(getContext());
            imageRequestQueue.add(request);

        }
    }

    @Override
    public void onAssetDataReceived(ViewProductDetailsModel object) {

        DigiCareLogger.d(TAG, "Manual Link : " + object.getmManualLink());
        DigiCareLogger.d(TAG, "Video's Length : " + object.getmVideoLinks().size());

    }

}

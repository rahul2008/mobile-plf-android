package com.philips.cdp.digitalcare.homefragment;

import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.contactus.fragments.ContactUsFragment;
import com.philips.cdp.digitalcare.faq.FaqFragment;
import com.philips.cdp.digitalcare.listeners.IPrxCallback;
import com.philips.cdp.digitalcare.locatephilips.fragments.LocatePhilipsFragment;
import com.philips.cdp.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cdp.digitalcare.productdetails.ProductSelectionProductInfo;
import com.philips.cdp.digitalcare.productdetails.PrxProductData;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.listeners.ActionbarUpdateListener;
import com.philips.cdp.productselection.listeners.ProductModelSelectionListener;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;


/**
 * SupportHomeFragment is the first screen of Support app. This class will give
 * all the possible options to navigate within digital support app.
 *
 * @author : Ritesh.jha@philips.com
 * @creation Date : 5 Dec 2014
 */

public class SupportHomeFragment extends DigitalCareBaseFragment implements IPrxCallback {

    private static final String TAG = SupportHomeFragment.class.getSimpleName();
    private static boolean isFirstTimeProductComponentlaunch = true;
    private static ProductSelectionProductInfo productInfo = null;
    private LinearLayout mOptionParent = null;
    private FrameLayout.LayoutParams mParams = null;
    private int ButtonMarginTop = 0;
    private int RegisterButtonMarginTop = 0;
    private boolean mIsFirstScreenLaunch = false;
    private View mView = null;
    private RelativeLayout mProductDetailsLayout = null;
    private ProductModelSelectionHelper mProductSelectionHelper = null;
    private PrxProductData mPrxProductData = null;
    private ActionbarUpdateListener actionBarClickListener = new ActionbarUpdateListener() {

        @Override
        public void updateActionbar(String titleActionbar, Boolean hamburgerIconAvailable) {
//            mActionBarTitle.setText(titleActionbar);
//            if (hamburgerIconAvailable) {
//                enableActionBarHome();
//            } else {
//                enableActionBarLeftArrow();
//            }
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_support, container,
                false);
        mIsFirstScreenLaunch = true;
        DigitalCareConfigManager.getInstance().setViewProductDetailsData(null);

        productInfo = new ProductSelectionProductInfo();
        if (DigitalCareConfigManager.getInstance().getProductModelSelectionType() != null) {
            productInfo.setSector(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getSector());
            productInfo.setCatalog(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getCatalog());
            if (DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length == 1)
                productInfo.setCtn(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList()[0]);
            DigitalCareConfigManager.getInstance().setConsumerProductInfo(productInfo);
        }
        if (mIsFirstScreenLaunch) {
            synchronized (this) {
                if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null &&
                        DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().toString() != null) {
                    mPrxProductData = new PrxProductData(getActivity(), this);
                    mPrxProductData.executeRequests();
                }
            }
        }
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mOptionParent = (LinearLayout) getActivity().findViewById(
                R.id.optionParent);
        mParams = (FrameLayout.LayoutParams) mOptionParent.getLayoutParams();
        Configuration config = getResources().getConfiguration();
        setViewParams(config);
        ButtonMarginTop = (int) getActivity().getResources().getDimension(R.dimen.marginTopButtonLayout);
        RegisterButtonMarginTop = (int) getActivity().getResources().getDimension(R.dimen.marginTopRegisterButton);
        if (!(mIsFirstScreenLaunch))
            createMainMenu();
        try {
            if (DigitalCareConfigManager.getInstance().getVerticalPageNameForTagging() != null && mIsFirstScreenLaunch) {
                AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_HOME,
                        DigitalCareConfigManager.getInstance().getVerticalPageNameForTagging());
                mIsFirstScreenLaunch = false;
            } else {
                AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_HOME,
                        getPreviousName());
            }
        } catch (Exception e) {
            DigiCareLogger.e(TAG, "LocaleMatch Crash Controlled : " + e);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginPort;

        } else {
            mParams.leftMargin = mParams.rightMargin = mLeftRightMarginLand;
        }
        mOptionParent.setLayoutParams(mParams);
    }

    /**
     * Create RelativeLayout at runTime. RelativeLayout will have button and
     * image together.
     */
    private void createButtonLayout(int buttonTitleResId, int buttonDrawableResId) {

        String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);

        String buttonDrawable = getResources().getResourceEntryName(buttonDrawableResId);
        float density = getResources().getDisplayMetrics().density;
        String packageName = getActivity().getPackageName();
        int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);
        int drawable = getResources().getIdentifier(
                packageName + ":drawable/" + buttonDrawable, null, null);
        RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        if (relativeLayout == null) {
            return;
        }
        Button button = createButton(density, title);
        relativeLayout.addView(button);
        setButtonParams(button, density);
        ImageView imageView = createImageView(density, drawable);
        relativeLayout.addView(imageView);
        setImageParams(imageView, density);
        mOptionParent.addView(relativeLayout);
        setRelativeLayoutParams(relativeLayout, density, buttonTitle);

		/*
         * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
        relativeLayout.setTag(buttonTitle);
        relativeLayout.setOnClickListener(this);
    }

    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));

        relativeLayout.setLayoutParams(params);

        if (buttonTitle.equals(getStringKey(R.string.product_selection))) {
            relativeLayout
                    .setBackgroundResource(R.drawable.selector_option_prod_reg_button_bg);

        } else {
            relativeLayout
                    .setBackgroundResource(R.drawable.selector_option_button_bg);
        }

        if ((DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length == 0) && (buttonTitle.equals(getStringKey(R.string.product_selection))))
            return null;

          /*
            If PRX response is fail/unsuccess then disable "View Product Button".
         */
        String viewProductText = getStringKey(R.string.view_product_details);
        ViewProductDetailsModel model = DigitalCareConfigManager.getInstance().getViewProductDetailsData();

        if ((buttonTitle == null) || (buttonTitle.equalsIgnoreCase(viewProductText) && (model == null || model.getCtnName() == null || model
                .getProductName() == null))) {
            mProductDetailsLayout = relativeLayout;
            mProductDetailsLayout.setVisibility(View.GONE);
            // return null;
        }

        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density, String buttonTitle) {

        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();

        if (buttonTitle.equals(getStringKey(R.string.product_selection))) {
            param.topMargin = RegisterButtonMarginTop;
        } else {
            param.topMargin = ButtonMarginTop;
        }
        relativeLayout.setLayoutParams(param);

    }

    private void setImageParams(ImageView imageView, float density) {
        LayoutParams imageViewParams = (LayoutParams) imageView
                .getLayoutParams();
        imageViewParams.height = (int) (35 * density);
        imageViewParams.width = (int) (35 * density);
        imageViewParams.topMargin = imageViewParams.bottomMargin = imageViewParams.rightMargin = (int) (8 * density);
        imageViewParams.leftMargin = (int) (19 * density);
        imageView.setLayoutParams(imageViewParams);
    }

    private void setButtonParams(Button button, float density) {
        RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
                .getLayoutParams();
        buttonParams.rightMargin = (int) (6 * density);
        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);

        button.setLayoutParams(buttonParams);
    }

    private ImageView createImageView(float density, int drawable) {
        ImageView imageView = new ImageView(getActivity(), null,
                R.style.supportHomeImageButton);
        imageView.setPadding(0, 0, 0, 0);
        imageView.setImageDrawable(getDrawable(drawable));

        return imageView;
    }

    private Button createButton(float density, int title) {
        Button button = new Button(getActivity(), null, R.style.fontButton);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);

        button.setGravity(Gravity.START | Gravity.CENTER);
        button.setPadding((int) (80 * density), 0, 0, 0);
        button.setTextAppearance(getActivity(), R.style.fontButton);
        Typeface buttonTypeface = Typeface.createFromAsset(getActivity().getAssets(), "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
        button.setText(title);

        return button;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstTimeProductComponentlaunch && (DigitalCareConfigManager.getInstance().getProductModelSelectionType() != null) && (DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length > 1)) {
            launchProductSelectionActivityComponent();
            isFirstTimeProductComponentlaunch = false;
        }
    }

    @Override
    public void onClick(View view) {

        String tag = (String) view.getTag();

        boolean actionTaken = false;
        if (DigitalCareConfigManager.getInstance()
                .getMainMenuListener() != null) {
            actionTaken = DigitalCareConfigManager.getInstance()
                    .getMainMenuListener().onMainMenuItemClicked(tag.toString());
        }

        if (actionTaken) {
            return;
        }

        if (tag.equals(getStringKey(R.string.contact_us))) {
            if (isConnectionAvailable())
                showFragment(new ContactUsFragment());
        } else if (tag.equals(getStringKey(R.string.view_product_details))) {
            if (isConnectionAvailable())
                showFragment(new ProductDetailsFragment());
        } else if (tag.equals(getStringKey(R.string.find_philips_near_you))) {
            if (isConnectionAvailable())
                showFragment(new LocatePhilipsFragment());
        } else if (tag.equals(getStringKey(R.string.view_faq))) {
            if (isConnectionAvailable())
                showFragment(new FaqFragment());
        } else if (tag.equals(getStringKey(R.string.feedback))) {
            if (isConnectionAvailable())
                showFragment(new RateThisAppFragment());
        } else if (tag.equals(getStringKey(R.string.product_selection))) {
            if (isConnectionAvailable()) {
                DigitalCareConfigManager digitalCareConfigManager = DigitalCareConfigManager.getInstance();

                if (digitalCareConfigManager.getUiLauncher() instanceof com.philips.cdp.digitalcare.component.ActivityLauncher) {
                    launchProductSelectionActivityComponent();
                } else {
                    // launchProductSelectionFragmentComponent();
                }
            }
        }
    }

   /* private void launchProductSelectionFragmentComponent() {
        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(getActivity().getApplicationContext());
        mProductSelectionHelper.setLocale(DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getLanguage(), DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getCountry());

        UiLauncher fragmentLauncher = new FragmentBuilder();
        fragmentLauncher.setAnimation(R.anim.abc_fade_in, R.anim.abc_fade_out);
        fragmentLauncher.setActionbarUpdateListener(actionBarClickListener);
        fragmentLauncher.setFragmentActivity(getActivity());
        fragmentLauncher.setmLayoutResourceID(DigitalCareConfigManager.getInstance().getUiLauncher().getLayoutResourceID());
        fragmentLauncher.setScreenOrientation(ProductModelSelectionHelper.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);
        ProductModelSelectionHelper.getInstance().setProductListener(new ProductModelSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel productSummaryModel) {
                if (productSummaryModel != null) {
                    SummaryModel summaryModel = productSummaryModel;
                    productInfo.setCtn(summaryModel.getData().getCtn());
                    mProductDetailsLayout.setVisibility(View.VISIBLE);

                    if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null &&
                            DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().toString() != null) {
                        mPrxProductData = new PrxProductData(getActivity(), null);
                        mPrxProductData.executeRequests();
                    }

                }
            }
        });
        ProductModelSelectionHelper.getInstance().invokeProductSelection(fragmentLauncher, DigitalCareConfigManager.getInstance()
                .getProductModelSelectionType());
        ProductSelectionLogger.enableLogging();
    }*/

    private void launchProductSelectionActivityComponent() {
        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(getActivity().getApplicationContext());
        mProductSelectionHelper.setLocale(DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getLanguage(), DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getCountry());


        ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);
        uiLauncher.setAnimation(DigitalCareConfigManager.getInstance().getUiLauncher().getEnterAnimation(),
                DigitalCareConfigManager.getInstance().getUiLauncher().getExitAnimation());
        ProductModelSelectionHelper.getInstance().setProductListener(new ProductModelSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel productSummaryModel) {
                if (productSummaryModel != null) {
                    SummaryModel summaryModel = productSummaryModel;
                    productInfo.setCtn(summaryModel.getData().getCtn());
                    mProductDetailsLayout.setVisibility(View.VISIBLE);

                    if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null &&
                            DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().toString() != null) {
                        mPrxProductData = new PrxProductData(getActivity(), null);

                        mPrxProductData.executePRXAssetRequestWithSummaryData(productSummaryModel);
                    }
                }
            }
        });
        ProductModelSelectionHelper.getInstance().invokeProductSelection(uiLauncher, DigitalCareConfigManager.getInstance()
                .getProductModelSelectionType());
        ProductSelectionLogger.enableLogging();
    }


    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.actionbar_title_support);
    }

    /*
     * This method will parse, how many features are available at DigitalCare
     * level.
     */
    private void createMainMenu() {
        TypedArray titles = getResources().obtainTypedArray(R.array.main_menu_title);
        TypedArray resources = getResources().obtainTypedArray(R.array.main_menu_resources);

        for (int i = 0; i < titles.length(); i++) {
            createButtonLayout(titles.getResourceId(i, 0), resources.getResourceId(i, 0));
        }
    }

    @Override
    public void onResponseReceived(boolean isAvailable) {
        createMainMenu();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        /*
        Commenting below finish() because of "Rally DE9081".
        [Coffee]After switching menu from consumer care to other menu from leftoffcanvas, our application getting close
         */
//        getActivity().finish();
    }

    private Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    private String getStringKey(int resId) {
        return getResources().getResourceEntryName(resId);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_HOME;
    }
}

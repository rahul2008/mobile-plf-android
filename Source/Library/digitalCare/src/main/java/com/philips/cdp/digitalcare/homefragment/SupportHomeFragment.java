package com.philips.cdp.digitalcare.homefragment;

import android.content.Context;
import android.content.SharedPreferences;
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

import com.philips.cdp.digitalcare.ConsumerProductInfo;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.contactus.fragments.ContactUsFragment;
import com.philips.cdp.digitalcare.faq.FaqFragment;
import com.philips.cdp.digitalcare.listeners.IPrxCallback;
import com.philips.cdp.digitalcare.locatephilips.fragments.LocatePhilipsFragment;
import com.philips.cdp.digitalcare.productdetails.ProductDetailsFragment;
import com.philips.cdp.digitalcare.productdetails.PrxProductData;
import com.philips.cdp.digitalcare.productdetails.model.ViewProductDetailsModel;
import com.philips.cdp.digitalcare.rateandreview.RateThisAppFragment;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.launchertype.FragmentLauncher;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.prxdatamodels.summary.Data;
import com.philips.cdp.prxclient.prxdatamodels.summary.SummaryModel;

import java.util.List;


/**
 * SupportHomeFragment is the first screen of Support app. This class will give
 * all the possible options to navigate within digital support app.
 *
 * @author : Ritesh.jha@philips.com
 * @creation Date : 5 Dec 2014
 */

public class SupportHomeFragment extends DigitalCareBaseFragment implements IPrxCallback {

    private static final String TAG = SupportHomeFragment.class.getSimpleName();
    private static final String USER_SELECTED_PRODUCT_CTN = "mCtnFromPreference";
    private static final String USER_PREFERENCE = "user_product";
    private static boolean isFirstTimeProductComponentlaunch = true;
    //  private boolean isfragmentFirstTimeVisited;
    private static boolean isPRXComponentChecked;
    private static boolean isSupportScreenLaunched;
    // private static boolean isProductSelectionFirstTime;
    private SharedPreferences prefs = null;
    private LinearLayout mOptionParent = null;
    private FrameLayout.LayoutParams mParams = null;
    private int ButtonMarginTop = 0;
    private int RegisterButtonMarginTop = 0;
    private boolean mIsFirstScreenLaunch = false;
    private View mView = null;
    private View mProductViewProductButton = null;
    private View mProductLocatePhilipsButton = null;
    private View mProductChangeButton = null;
    private View mProductFAQButton = null;
    private View mProductTellUsWhatYouThinkButton = null;
    private View mProductContactUsButton = null;
    private ProductModelSelectionHelper mProductSelectionHelper = null;
    private PrxProductData mPrxProductData = null;
    private ConsumerProductInfo mProductInfo = null;
    private String mCtnFromPreference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DigiCareLogger.d(TAG, "OnCreate Method");
        // isProductSelectionFirstTime = true;
        //  isfragmentFirstTimeVisited = true;
        isSupportScreenLaunched = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView Method");
        mView = inflater.inflate(R.layout.fragment_support, container,
                false);
        mIsFirstScreenLaunch = true;
        mProductInfo = new ConsumerProductInfo();
        DigitalCareConfigManager.getInstance().setConsumerProductInfo(mProductInfo);
        prefs = getActivity().getSharedPreferences(
                USER_PREFERENCE, Context.MODE_PRIVATE);

        updateConsumerProductInfo();
        if (mIsFirstScreenLaunch || DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length < 2) {
            synchronized (this) {
                if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null &&
                        DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null) {
                    if (DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length == 1) {
                        ProductModelSelectionType modelSelectionType = DigitalCareConfigManager.getInstance().getProductModelSelectionType();
                        DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(modelSelectionType.getHardCodedProductList()[0]);
                        DigitalCareConfigManager.getInstance().getConsumerProductInfo().setSector(modelSelectionType.getSector());
                        DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCatalog(modelSelectionType.getCatalog());
                    }

                    DigiCareLogger.v(TAG, "Sending PRX Request");
                    mPrxProductData = new PrxProductData(getActivity(), this);
                    mPrxProductData.executeRequests();
                }
            }
        } else
            createMainMenu();

        DigitalCareConfigManager digitalCareConfigManager = DigitalCareConfigManager.getInstance();

       /* if (!isFirstTimeProductComponentlaunch && mCtnFromPreference == "") {
            if (isProductSelectionFirstTime) {

                if (digitalCareConfigManager.getUiLauncher() instanceof FragmentLauncher) {
                    if (isfragmentFirstTimeVisited) {
                        isfragmentFirstTimeVisited = false;
                        launchProductSelectionComponent();
                    }
                } else {
                    launchProductSelectionComponent();
                }
            }
        }

        if (isFirstTimeProductComponentlaunch && (DigitalCareConfigManager.getInstance().getProductModelSelectionType() != null) && (DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length > 1) && mCtnFromPreference == "") {
            isFirstTimeProductComponentlaunch = false;
            if (digitalCareConfigManager.getUiLauncher() instanceof FragmentLauncher)
                isfragmentFirstTimeVisited = false;
            launchProductSelectionComponent();

        }*/


        return mView;
    }

    private void updateConsumerProductInfo() {
        if (DigitalCareConfigManager.getInstance().getProductModelSelectionType() != null) {
            DigitalCareConfigManager.getInstance().getConsumerProductInfo().setSector(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getSector());
            DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCatalog(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getCatalog());
          /*  if (DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length == 1)
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList()[0]);
        */
        }

        mCtnFromPreference = prefs.getString(USER_SELECTED_PRODUCT_CTN, "");

        if (mCtnFromPreference != null && mCtnFromPreference != "")
            DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(mCtnFromPreference);
    }

    private boolean isProductSelected() {
        String ctn = prefs.getString(USER_SELECTED_PRODUCT_CTN, "");
        if (ctn != null && ctn != "") {
            return false;
        } else
            return true;
    }


    private void enableSupportButtonClickable() {
        if (mProductViewProductButton != null && !mProductViewProductButton.isClickable())
            mProductViewProductButton.setClickable(true);
        if (mProductLocatePhilipsButton != null && !mProductLocatePhilipsButton.isClickable())
            mProductLocatePhilipsButton.setClickable(true);
        if (mProductChangeButton != null && !mProductChangeButton.isClickable())
            mProductChangeButton.setClickable(true);
        if (mProductFAQButton != null && !mProductFAQButton.isClickable())
            mProductFAQButton.setClickable(true);
        if (mProductTellUsWhatYouThinkButton != null && !mProductTellUsWhatYouThinkButton.isClickable())
            mProductTellUsWhatYouThinkButton.setClickable(true);
        if (mProductContactUsButton != null && !mProductContactUsButton.isClickable())
            mProductContactUsButton.setClickable(true);
    }

    private void disableSupportButtonClickable() {
        if (mProductViewProductButton != null)
            mProductViewProductButton.setClickable(false);
        if (mProductLocatePhilipsButton != null)
            mProductLocatePhilipsButton.setClickable(false);
        if (mProductChangeButton != null)
            mProductChangeButton.setClickable(false);
        if (mProductFAQButton != null)
            mProductFAQButton.setClickable(false);
        if (mProductTellUsWhatYouThinkButton != null)
            mProductTellUsWhatYouThinkButton.setClickable(false);
        if (mProductContactUsButton != null)
            mProductContactUsButton.setClickable(false);
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
        if (!(mIsFirstScreenLaunch)) {
            DigiCareLogger.d(TAG, "First Launcher has to create buttons");
            createMainMenu();
        }
        try {
            if (DigitalCareConfigManager.getInstance().getPreviousPageNameForTagging() != null && mIsFirstScreenLaunch) {
                AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_HOME,
                        DigitalCareConfigManager.getInstance().getPreviousPageNameForTagging());
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
        /*if (relativeLayout == null) {
            return;
        }*/
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
        ViewProductDetailsModel model = DigitalCareConfigManager.getInstance().getViewProductDetailsData();

        if (buttonTitle.equals(getStringKey(R.string.Change_Selected_Product))) {
            relativeLayout
                    .setBackgroundResource(R.drawable.selector_option_prod_reg_button_bg);
            mProductChangeButton = (View) relativeLayout;
            // if (isProductSelected() && !isSupportScreenLaunched)
            if (isProductSelected())
                mProductChangeButton.setVisibility(View.GONE);

        } else {
            relativeLayout
                    .setBackgroundResource(R.drawable.selector_option_button_bg);
        }

        if ((DigitalCareConfigManager.getInstance().getProductModelSelectionType().getHardCodedProductList().length < 2) && (buttonTitle.equals(getStringKey(R.string.Change_Selected_Product))))
            mProductChangeButton.setVisibility(View.GONE);

          /*
            If PRX response is fail/unsuccess then disable "View Product Button".
         */

        if (buttonTitle.equals(getStringKey(R.string.view_product_details))) {
            mProductViewProductButton = (View) relativeLayout;
                     /* if ((model.getCtnName() != null)
                    || (model.getProductName() != null))
                mProductViewProductButton.setVisibility(View.VISIBLE);
            else
                mProductViewProductButton.setVisibility(View.GONE);*/
            if (!isSupportScreenLaunched && isProductSelected())
                mProductViewProductButton.setVisibility(View.GONE);
            else
                mProductViewProductButton.setVisibility(View.VISIBLE);

        }

        if (buttonTitle.equals(getStringKey(R.string.view_faq))) {
            mProductFAQButton = (View) relativeLayout;
            if (isProductSelected() && !isSupportScreenLaunched)
                mProductFAQButton.setVisibility(View.GONE);
            else
                mProductFAQButton.setVisibility(View.VISIBLE);
        }

        if (buttonTitle.equals(getStringKey(R.string.find_philips_near_you))) {
            mProductLocatePhilipsButton = (View) relativeLayout;

            if (isProductSelected() && !isSupportScreenLaunched)
                mProductLocatePhilipsButton.setVisibility(View.GONE);
            else
                mProductLocatePhilipsButton.setVisibility(View.VISIBLE);

        }

        if (buttonTitle.equals(getStringKey(R.string.contact_us))) {
            mProductContactUsButton = (View) relativeLayout;
        }

        if (buttonTitle.equals(getStringKey(R.string.feedback))) {
            mProductTellUsWhatYouThinkButton = (View) relativeLayout;
        }
        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density, String buttonTitle) {

        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();

        if (buttonTitle.equals(getStringKey(R.string.Change_Selected_Product))) {
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

    private void launchProductSelectionComponent() {
        DigitalCareConfigManager digitalCareConfigManager = DigitalCareConfigManager.getInstance();

        if (digitalCareConfigManager.getUiLauncher() instanceof ActivityLauncher) {
            launchProductSelectionActivityComponent();
        } else if (digitalCareConfigManager.getUiLauncher() instanceof FragmentLauncher)
            launchProductSelectionFragmentComponent();
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
                if (isProductSelected() && isSupportScreenLaunched) {
                    disableSupportButtonClickable();
                    launchProductSelectionComponent();
                } else
                    showFragment(new ContactUsFragment());
        } else if (tag.equals(getStringKey(R.string.view_product_details))) {
            if (isConnectionAvailable())
                if (isProductSelected() && isSupportScreenLaunched) {
                    disableSupportButtonClickable();
                    launchProductSelectionComponent();
                } else
                    showFragment(new ProductDetailsFragment());
        } else if (tag.equals(getStringKey(R.string.find_philips_near_you))) {
            if (isConnectionAvailable())
                if (isProductSelected() && isSupportScreenLaunched) {
                    disableSupportButtonClickable();
                    launchProductSelectionComponent();
                } else
                    showFragment(new LocatePhilipsFragment());
        } else if (tag.equals(getStringKey(R.string.view_faq))) {
            if (isConnectionAvailable())
                if (isProductSelected() && isSupportScreenLaunched) {
                    disableSupportButtonClickable();
                    launchProductSelectionComponent();
                } else
                    showFragment(new FaqFragment());
        } else if (tag.equals(getStringKey(R.string.feedback))) {
            if (isConnectionAvailable())
                if (isProductSelected() && isSupportScreenLaunched) {
                    disableSupportButtonClickable();
                    launchProductSelectionComponent();
                } else
                    showFragment(new RateThisAppFragment());
        } else if (tag.equals(getStringKey(R.string.Change_Selected_Product))) {
            if (isConnectionAvailable()) {
                disableSupportButtonClickable();
                DigitalCareConfigManager digitalCareConfigManager = DigitalCareConfigManager.getInstance();

                if (digitalCareConfigManager.getUiLauncher() instanceof ActivityLauncher) {
                    launchProductSelectionActivityComponent();
                } else if (digitalCareConfigManager.getUiLauncher() instanceof FragmentLauncher) {
                    Configuration configuration = getResources().getConfiguration();
                    ProductModelSelectionHelper.getInstance().setCurrentOrientation(configuration);
                    launchProductSelectionFragmentComponent();
                }
            }
        }
    }

    private void launchProductSelectionFragmentComponent() {
        DigiCareLogger.i("testing", "Support -- Fragment Invoke");
        if (mProductChangeButton != null) {
            mProductChangeButton.setClickable(false);
        }
        FragmentLauncher fragmentLauncher = (FragmentLauncher) DigitalCareConfigManager.getInstance().getUiLauncher();
        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(getActivity());
        mProductSelectionHelper.setLocale(DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getLanguage(),
                DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getCountry());

        /*Initialize product selection tagging*/
        DigitalCareConfigManager ccConfigManager = DigitalCareConfigManager.getInstance();
        ProductModelSelectionHelper.getInstance().initializeTagging(ccConfigManager.isTaggingEnabled(), ccConfigManager.getAppNameForTagging(),
                ccConfigManager.getAppIdForTagging(), ccConfigManager.getPreviousPageNameForTagging());

        ProductModelSelectionHelper.getInstance().setProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel summaryModel) {
                isSupportScreenLaunched = false;
                if (summaryModel != null) {
                    mProductChangeButton.setClickable(true);
                    enableSupportButtonClickable();
                    updateSummaryData(summaryModel);
                } else {
                    showAlert(getString(R.string.productselection_erroresponse));
                    disablePrxDependentButtons();
                    enableSupportButtonClickable();
                }
            }
        });
        ProductModelSelectionHelper.getInstance().invokeProductSelection(fragmentLauncher, DigitalCareConfigManager.getInstance()
                .getProductModelSelectionType());
        ProductSelectionLogger.enableLogging();
    }

    private void launchProductSelectionActivityComponent() {
        if (mProductChangeButton != null) {
            mProductChangeButton.setClickable(false);
        }
        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(getActivity());
        mProductSelectionHelper.setLocale(DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getLanguage(), DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack().getCountry());

        /*Initialize product selection tagging*/
        DigitalCareConfigManager ccConfigManager = DigitalCareConfigManager.getInstance();
        ProductModelSelectionHelper.getInstance().initializeTagging(ccConfigManager.isTaggingEnabled(), ccConfigManager.getAppNameForTagging(),
                ccConfigManager.getAppIdForTagging(), ccConfigManager.getPreviousPageNameForTagging());

        ActivityLauncher uiLauncher = (ActivityLauncher) DigitalCareConfigManager.getInstance().getUiLauncher();
        uiLauncher = new ActivityLauncher(uiLauncher.getScreenOrientation(), uiLauncher.getmUiKitTheme());
        uiLauncher.setAnimation(DigitalCareConfigManager.getInstance().getUiLauncher().getEnterAnimation(),
                DigitalCareConfigManager.getInstance().getUiLauncher().getExitAnimation());
        ProductModelSelectionHelper.getInstance().setProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel summaryModel) {
                isSupportScreenLaunched = false;
                if (summaryModel != null) {
                    if (mProductChangeButton != null) {
                        mProductChangeButton.setClickable(true);
                        enableSupportButtonClickable();
                        updateSummaryData(summaryModel);
                    }
                } else {
                    showAlert(getString(R.string.productselection_erroresponse));
                    disablePrxDependentButtons();
                    enableSupportButtonClickable();
                }
            }
        });

        ProductModelSelectionHelper.getInstance().invokeProductSelection(uiLauncher, DigitalCareConfigManager.getInstance()
                .getProductModelSelectionType());
    }

    private void disablePrxDependentButtons() {
        if (mProductChangeButton != null) {
            mProductChangeButton.setClickable(true);
            mProductChangeButton.setVisibility(View.GONE);
        }
        if (mProductLocatePhilipsButton != null)
            mProductLocatePhilipsButton.setVisibility(View.GONE);
        if (mProductFAQButton != null)
            mProductFAQButton.setVisibility(View.GONE);
        if (mProductViewProductButton != null)
            mProductViewProductButton.setVisibility(View.GONE);

        if (mProductChangeButton != null) {
            mProductChangeButton.setVisibility(View.VISIBLE);
            mProductChangeButton.setClickable(true);
        }
    }

    protected void updateSummaryData(SummaryModel productSummaryModel) {
        if (productSummaryModel != null) {
            mViewProductSummaryModel = productSummaryModel;
            SummaryModel summaryModel = productSummaryModel;
            DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(summaryModel.getData().getCtn());
            if (mProductViewProductButton != null)
                mProductViewProductButton.setVisibility(View.VISIBLE);
            if (mProductLocatePhilipsButton != null)
                mProductLocatePhilipsButton.setVisibility(View.VISIBLE);
            if (mProductFAQButton != null)
                mProductFAQButton.setVisibility(View.VISIBLE);
            if (mProductChangeButton != null)
                mProductChangeButton.setVisibility(View.VISIBLE);


            if (DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null &&
                    DigitalCareConfigManager.getInstance().getLocaleMatchResponseWithCountryFallBack() != null) {
              /*  mPrxProductData = new PrxProductData(getActivity(), null);

                mPrxProductData.executePRXAssetRequestWithSummaryData(productSummaryModel);
*/
                Data summaryData = productSummaryModel.getData();
                List<String> filterKeys = summaryData.getFilterKeys();
                String productGroup = null;
                String productCategory = null;
                String productSubCategory = null;
                for (String filterData : filterKeys) {
                    if (filterData != null && filterData.endsWith("SU"))
                        productSubCategory = filterData;

                    if (filterData != null && filterData.endsWith("GR"))
                        productGroup = filterData;

                    if (filterData != null && filterData.endsWith("CA"))
                        productCategory = filterData;
                }

                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(summaryData.getCtn());
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setSubCategory(productSubCategory);
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setProductReviewUrl(summaryData.getProductURL());
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setGroup(productGroup);
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCategory(productCategory);

                ViewProductDetailsModel productDetailsModel = new ViewProductDetailsModel();
                productDetailsModel.setProductName(summaryData.getProductTitle());
                productDetailsModel.setCtnName(summaryData.getCtn());
                productDetailsModel.setProductImage(summaryData.getImageURL());
                productDetailsModel.setProductInfoLink(summaryData.getProductURL());
                DigitalCareConfigManager.getInstance().setViewProductDetailsData(productDetailsModel);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_SELECTED_PRODUCT_CTN, summaryData.getCtn());
                editor.apply();
            }
        }
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
        //Android OS issue so adding in try/catch control to this code snippet(Issue reproduce is very rare).
        // java.lang.IllegalStateException:
        //at android.support.v4.app.Fragment.getResources(Fragment.java:644)
        try {
            TypedArray titles = getResources().obtainTypedArray(R.array.main_menu_title);
            TypedArray resources = getResources().obtainTypedArray(R.array.main_menu_resources);

            for (int i = 0; i < titles.length(); i++) {
                createButtonLayout(titles.getResourceId(i, 0), resources.getResourceId(i, 0));
            }
        } catch (IllegalStateException ie) {
            DigiCareLogger.e(TAG, "Exception while generating SupportScreenButton : " + ie);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        enableSupportButtonClickable();
        if (mProductViewProductButton != null) {
            if (!mProductChangeButton.isClickable())
                mProductChangeButton.setClickable(true);
        }
    }

    @Override
    public void onResponseReceived(SummaryModel productSummaryModel) {
        DigiCareLogger.v(TAG, "Summary Response Received from PRX  Received");

        if (productSummaryModel == null) {
            createMainMenu();
            if (!isProductSelected() /*&& !isSupportScreenLaunched*/) {
                    /*ViewProductDetailsModel model = DigitalCareConfigManager.getInstance().getViewProductDetailsData();
                    if ((model.getCtnName() != null)
                            || (model.getProductName() != null))*/
                disablePrxDependentButtons();
            }
               /* if (isProductSelected())
                    disablePrxDependentButtons();*/

            DigiCareLogger.v(TAG, "Summary Response Received from PRX  Received with summaryModel Null");
        } else {

            try {
                DigiCareLogger.v(TAG, "Summary Response Received from PRX  Received with summaryModel NotNull");
                mViewProductSummaryModel = productSummaryModel;
                SummaryModel summaryModel = productSummaryModel;
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(summaryModel.getData().getCtn());
                if (mProductViewProductButton != null)
                    mProductViewProductButton.setVisibility(View.VISIBLE);


                Data summaryData = productSummaryModel.getData();
                List<String> filterKeys = summaryData.getFilterKeys();
                String productGroup = null;
                String productCategory = null;
                String productSubCategory = null;
                for (String filterData : filterKeys) {
                    if (filterData != null && filterData.endsWith("SU"))
                        productSubCategory = filterData;

                    if (filterData != null && filterData.endsWith("GR"))
                        productGroup = filterData;

                    if (filterData != null && filterData.endsWith("CA"))
                        productCategory = filterData;
                }

                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCtn(summaryData.getCtn());
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setSubCategory(productSubCategory);
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setProductReviewUrl(summaryData.getProductURL());
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setGroup(productGroup);
                DigitalCareConfigManager.getInstance().getConsumerProductInfo().setCategory(productCategory);

                ViewProductDetailsModel productDetailsModel = new ViewProductDetailsModel();
                productDetailsModel.setProductName(summaryData.getProductTitle());
                productDetailsModel.setCtnName(summaryData.getCtn());
                productDetailsModel.setProductImage(summaryData.getImageURL());
                productDetailsModel.setProductInfoLink(summaryData.getProductURL());
                DigitalCareConfigManager.getInstance().setViewProductDetailsData(productDetailsModel);

                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(USER_SELECTED_PRODUCT_CTN, summaryData.getCtn());
                editor.apply();
            } finally {
                DigiCareLogger.v(TAG, "Menu is creating in NonNull Summary");
                createMainMenu();
            }
        }
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

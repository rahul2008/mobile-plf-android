package com.philips.multiproduct.detailedscreen;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.philips.cdp.prxclient.prxdatamodels.assets.Asset;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.assets.Assets;
import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.customview.CustomFontTextView;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.detailedscreen.adapter.ProductAdapter;
import com.philips.multiproduct.prx.PrxAssetDataListener;
import com.philips.multiproduct.prx.PrxWrapper;
import com.philips.multiproduct.savedscreen.SavedScreenFragment;
import com.philips.multiproduct.utils.Constants;
import com.philips.multiproduct.utils.ProductSelectionLogger;

import java.util.ArrayList;
import java.util.List;

import java.util.Observable;
import java.util.Observer;

/**
 * This Fragments takes responsibility to show the complete detailed description of the
 * specific product with multiple images.
 * <p/>
 * The Data it shows is from the Philips IT System.
 *
 * @author naveen@philips.com
 * @Date 28/01/2016
 */
public class DetailedScreenFragment extends MultiProductBaseFragment implements View.OnClickListener, Observer {

    private static final String TAG = DetailedScreenFragment.class.getSimpleName();
    private static ViewPager mViewpager = null;
    private static CircleIndicator mIndicater = null;
    private static CustomFontTextView mProductName = null;
    private static CustomFontTextView mProductCtn = null;
    private static Button mSelectButton = null;
    private String[] mDetailedScreenimage = null;
    private ProgressDialog mAssetDialog = null;
    private List<String> mVideoList = null;
    private LinearLayout mDetailedScreenParentContainer, mDetailedScreenImageContainer = null;
    private LinearLayout.LayoutParams mDetailedScreenParams, mDetailedImageScreenParams = null;
    private int mPortraitTablet = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_screen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDetailedScreenParentContainer = (LinearLayout) getActivity().findViewById(R.id.detailed_screen_parent_one);
        mDetailedScreenParams = (LinearLayout.LayoutParams) mDetailedScreenParentContainer.getLayoutParams();
        mDetailedScreenImageContainer = (LinearLayout) getActivity().findViewById(R.id.detailed_screen_parent_image);
        mDetailedImageScreenParams = (LinearLayout.LayoutParams) mDetailedScreenImageContainer.getLayoutParams();
        mViewpager = (ViewPager) getActivity().findViewById(R.id.detailedscreen_pager);
        mIndicater = (CircleIndicator) getActivity().findViewById(R.id.detailedscreen_indicator);
        mProductName = (CustomFontTextView) getActivity().findViewById(R.id.detailed_screen_productname);
        mSelectButton = (Button) getActivity().findViewById(R.id.detailedscreen_select_button);
        mProductCtn = (CustomFontTextView) getActivity().findViewById(R.id.detailed_screen_productctn);
        mPortraitTablet = (int) getResources()
                .getDimension(R.dimen.activity_margin_tablet_portrait);

        if (isConnectionAvailable() && (ProductModelSelectionHelper.getInstance().getUserSelectedProduct() != null)) {
            getProductImagesFromPRX();
            mProductName.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getProductTitle());
            mProductCtn.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());
        } else
            ProductSelectionLogger.e(TAG, "Summary Model is null in Base");

        mVideoList = new ArrayList<String>();
        mDetailedScreenimage = new String[1];
        if (ProductModelSelectionHelper.getInstance().getUserSelectedProduct() != null)
            mDetailedScreenimage[0] = ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getImageURL();
        mViewpager.setAdapter(new ProductAdapter(getChildFragmentManager(), mDetailedScreenimage));
        mIndicater.setViewPager(mViewpager);
        mProductName.setTypeface(Typeface.DEFAULT_BOLD);
        mSelectButton.setOnClickListener(this);

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
    }

    private void initializeUi() {
        mProductName.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getProductTitle());
        mProductCtn.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());
//        getProductImagesFromPRX();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    @Override
    public void setViewParams(Configuration config) {

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT && isTablet()) {
            ProductSelectionLogger.i(TAG, "setViewParams  : portrait");
            mDetailedScreenParentContainer.setPadding(0, 0, 0, 0);
            mDetailedScreenParams.leftMargin = mDetailedScreenParams.rightMargin = mPortraitTablet;
            mDetailedImageScreenParams.leftMargin = mDetailedImageScreenParams.rightMargin = mPortraitTablet;
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet()) {

            // Control for Split Screen Margin

        }
        mDetailedScreenParentContainer.setLayoutParams(mDetailedScreenParams);
        mDetailedScreenImageContainer.setLayoutParams(mDetailedImageScreenParams);
    }

    @Override
    public String getActionbarTitle() {
        if ((ProductModelSelectionHelper.getInstance().getUserSelectedProduct() != null))
            return ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getProductTitle();
        else
            return "";
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }


    private void getProductImagesFromPRX() {

        if (mAssetDialog == null)
            mAssetDialog = new ProgressDialog(getActivity(), R.style.loaderTheme);
        mAssetDialog.setProgressStyle(android.R.style.Widget_Material_ProgressBar_Large);
        mAssetDialog.setIndeterminateDrawable(getResources().getDrawable(R.drawable.loader));
        mAssetDialog.setCancelable(true);
        if (!(getActivity().isFinishing()))
            mAssetDialog.show();

        PrxWrapper prxWrapperCode = new PrxWrapper(getActivity().getApplicationContext(), ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn(),
                ProductModelSelectionHelper.getInstance().getSectorCode(),
                ProductModelSelectionHelper.getInstance().getLocale().toString(),
                ProductModelSelectionHelper.getInstance().getCatalogCode());

        prxWrapperCode.requestPrxAssetData(new PrxAssetDataListener() {
                                               @Override
                                               public void onSuccess(AssetModel assetModel) {

                                                   if (getActivity() != null)
                                                       if (!(getActivity().isFinishing()) && mAssetDialog.isShowing()) {
                                                           mAssetDialog.dismiss();
                                                           mAssetDialog.cancel();
                                                       }

                                                   ProductSelectionLogger.d(TAG, " Asset Data received for the Ctn ; " + ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());
                                                   com.philips.cdp.prxclient.prxdatamodels.assets.Data data = assetModel.getData();

                                                   if (data != null) {
                                                       Assets assets = data.getAssets();
                                                       List<Asset> asset = assets.getAsset();
                                                       for (Asset assetObject : asset) {
                                                           String assetDescription = assetObject.getDescription();
                                                           String assetResource = assetObject.getAsset();
                                                           String assetExtension = assetObject.getType();
                                                           if ((assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES_APP)) || (assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES_DPP)) || (assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES_MI1) || (assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES_PID)) || (assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES_RTP)))) {
                                                               if (assetResource != null)
                                                                   mVideoList.add(assetResource.replace("/content/", "/image/") + "?wid=" + (int) (getResources().getDimension(R.dimen.productdetails_screen_image) / getResources().getDisplayMetrics().density) + "&amp;");
                                                           }
                                                       }
                                                   }
                                                   ProductSelectionLogger.d(TAG, "Images Size : " + mVideoList.size());

                                                   mDetailedScreenimage = new String[mVideoList.size()];
                                                   for (int i = 0; i < mVideoList.size(); i++) {
                                                       if (i < 5)
                                                           mDetailedScreenimage[i] = mVideoList.get(i);
                                                   }
                                                   mViewpager.setAdapter(new ProductAdapter(getChildFragmentManager(), mDetailedScreenimage));
                                               }

                                               @Override
                                               public void onFail(String errorMessage) {

                                                   if (getActivity() != null)
                                                       if (!(getActivity().isFinishing()) && mAssetDialog.isShowing()) {
                                                           mAssetDialog.dismiss();
                                                           mAssetDialog.cancel();
                                                       }

                                                   ProductSelectionLogger.d(TAG, " Asset Data Failed for the Ctn ; " + ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());

                                                   mDetailedScreenimage = new String[1];
                                                   mDetailedScreenimage[0] = ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getImageURL();
                                                   mViewpager.setAdapter(new ProductAdapter(getChildFragmentManager(), mDetailedScreenimage));
                                               }

                                           }

                , TAG);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.detailedscreen_select_button && isConnectionAvailable()) {
            if(isTablet()){
                replaceFragmentTablet(new SavedScreenFragment());
            }
            else {
                showFragment(new SavedScreenFragment());
            }
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        ProductSelectionLogger.d("testing", "Detailed Screen -- Clicked again : " + ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getProductTitle());

        if (mProductName == null) {
            return;
        }
        initializeUi();
    }
}

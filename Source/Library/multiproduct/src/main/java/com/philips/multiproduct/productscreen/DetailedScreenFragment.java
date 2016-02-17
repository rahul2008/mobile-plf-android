package com.philips.multiproduct.productscreen;

import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.cdp.prxclient.prxdatamodels.assets.Asset;
import com.philips.cdp.prxclient.prxdatamodels.assets.AssetModel;
import com.philips.cdp.prxclient.prxdatamodels.assets.Assets;
import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.multiproduct.ProductModelSelectionHelper;
import com.philips.multiproduct.R;
import com.philips.multiproduct.customview.CustomFontTextView;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.productscreen.adapter.ProductAdapter;
import com.philips.multiproduct.prx.PrxAssetDataListener;
import com.philips.multiproduct.prx.PrxWrapper;
import com.philips.multiproduct.savedscreen.SavedScreenFragment;
import com.philips.multiproduct.utils.Constants;
import com.philips.multiproduct.utils.ProductSelectionLogger;

import java.util.ArrayList;
import java.util.List;

/**
 * This Fragments takes responsibility to show the complete detailed description of the
 * specific product with multiple images.
 * <p/>
 * The Data it shows is from the Philips IT System.
 *
 * @author naveen@philips.com
 * @Date 28/01/2016
 */
public class DetailedScreenFragment extends MultiProductBaseFragment implements View.OnClickListener {

    private static final String TAG = DetailedScreenFragment.class.getSimpleName();
    private ViewPager mViewpager;
    private CircleIndicator mIndicater;
    private CustomFontTextView mProductName = null;
    private Button mSelectButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_screen, container, false);

        mViewpager = (ViewPager) view.findViewById(R.id.detailedscreen_pager);
        mIndicater = (CircleIndicator) view.findViewById(R.id.detailedscreen_indicator);
        mProductName = (CustomFontTextView) view.findViewById(R.id.detailed_screen_productname);
        mSelectButton = (Button) view.findViewById(R.id.detailedscreen_select_button);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (isConnectionAvailable() && (ProductModelSelectionHelper.getInstance().getUserSelectedProduct() != null))
            getAssetDataFromPRX();
        else
            ProductSelectionLogger.e(TAG, "Summary Model is null in Base");
        mViewpager.setAdapter(new ProductAdapter(getChildFragmentManager()));
        mIndicater.setViewPager(mViewpager);
        mProductName.setTypeface(Typeface.DEFAULT_BOLD);
        mSelectButton.setOnClickListener(this);
    }

    @Override
    public void setViewParams(Configuration config) {

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

    private ProgressDialog mAssetDialog = null;
    List<String> mVideoList = null;

    private void getAssetDataFromPRX() {

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
                                                       mVideoList = new ArrayList<String>();
                                                       for (Asset assetObject : asset) {
                                                           String assetDescription = assetObject.getDescription();
                                                           String assetResource = assetObject.getAsset();
                                                           String assetExtension = assetObject.getExtension();
                                                           if (assetExtension.equalsIgnoreCase(Constants.DETAILEDSCREEN_PRIDUCTIMAGES))
                                                               if (assetResource != null)
                                                                   mVideoList.add(assetResource.replace("/content/", "/image/") + "?wid=" + (int) (getResources().getDimension(R.dimen.productdetails_screen_image) / getResources().getDisplayMetrics().density) + "&amp;");
                                                       }
                                                   }
                                                   ProductSelectionLogger.d(TAG, "Images Size : " + mVideoList.size());
                                               }

                                               @Override
                                               public void onFail(String errorMessage) {

                                                   if (getActivity() != null)
                                                       if (!(getActivity().isFinishing()) && mAssetDialog.isShowing()) {
                                                           mAssetDialog.dismiss();
                                                           mAssetDialog.cancel();
                                                       }

                                                   ProductSelectionLogger.d(TAG, " Asset Data Failed for the Ctn ; " + ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());
                                               }

                                           }

                , TAG);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.detailedscreen_select_button)
            if (isConnectionAvailable())
                showFragment(new SavedScreenFragment());
    }
}

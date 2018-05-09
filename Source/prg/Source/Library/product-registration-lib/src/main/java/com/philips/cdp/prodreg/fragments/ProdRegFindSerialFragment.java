package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.MetadataSerNumbSampleContent;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.net.URL;
import java.util.List;

public class ProdRegFindSerialFragment extends ProdRegBaseFragment {

    public static final String urlBaseSeparator = "://";
    private ImageView serialNumberImageView;
    private Label serialNumberTextView;
    private Label serialNumberForamtTextView;
    private static final long serialVersionUID = -6635233525340545669L;



    @Override
    public int getActionbarTitleResId() {
        return R.string.PPR_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PPR_NavBar_Title);
    }

    @Override
    public boolean getBackButtonState() {
        return true;
    }

    @Override
    public List<RegisteredProduct> getRegisteredProducts() {
        return null;
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.prodreg_find_serial_number, container, false);
        initViews(view);
        return view;
    }

    private void initViews(final View view) {
        serialNumberImageView = (ImageView) view.findViewById(R.id.device_image);
        Button okFoundButton = (Button) view.findViewById(R.id.ok_found_button);
        serialNumberTextView = (Label) view.findViewById(R.id.serial_number_guide_text);
        serialNumberForamtTextView = (Label) view.findViewById(R.id.serial_number_guide_format_text);

        okFoundButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final FragmentManager supportFragmentManager = getActivity().getSupportFragmentManager();
                supportFragmentManager.popBackStackImmediate();
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable final Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            final ProductMetadataResponseData productMetadataResponseData = (ProductMetadataResponseData) bundle.getSerializable(ProdRegConstants.PROD_REG_PRODUCT_METADATA);
            setProductImage(productMetadataResponseData);
        }
    }

    private void setProductImage(final ProductMetadataResponseData productMetadataResponseData) {
        getImageUrl(productMetadataResponseData, new PrxRequest.OnUrlReceived() {
            @Override
            public void onSuccess(String url) {
                setSerialNumberTextView(productMetadataResponseData);
                Log.d("imageUrl", "imageUrl " + url);
                final ImageLoader imageLoader = ImageRequestHandler.getInstance(getActivity().getApplicationContext()).getImageLoader();
                setImgageviewwithAspectRation(serialNumberImageView);
                imageLoader.get(url, ImageLoader.getImageListener(serialNumberImageView,
                        R.drawable.product_placeholder, R.drawable.product_placeholder));
                serialNumberImageView.requestLayout();
            }
            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
            }
        });
    }

    private void setSerialNumberTextView(final ProductMetadataResponseData productMetadataResponseData) {
        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent =
                    productMetadataResponseData.getSerialNumberSampleContent();

            try {
                final String snExample = serialNumberSampleContent.getSnExample();
                final String snDescription = serialNumberSampleContent.getSnDescription();
                String stringSplit = ":";
                String emptyString = " ";
                String serialNumberExampleData = snExample.substring(snExample.indexOf(stringSplit)+1, snExample.length());
                serialNumberExampleData = serialNumberExampleData.replace(emptyString,"");
                String serialErrorText = getString(R.string.PPR_serial_number_consists).concat(emptyString) +
                        serialNumberExampleData.length() + emptyString + getString(R.string.PPR_number_starting).concat(emptyString)
                        + serialNumberExampleData.charAt(0) + emptyString + getString(R.string.PPR_eg) + serialNumberExampleData;
                serialNumberTextView.setText(serialErrorText);
                serialNumberForamtTextView.setText(snDescription);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private String getImageUrl(final ProductMetadataResponseData productMetadataResponseData, final PrxRequest.OnUrlReceived onUrlReceived) {
        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            final String asset = serialNumberSampleContent.getAsset();
            ProdRegLogger.i("Success values ***", serialNumberSampleContent.getAsset());
            if (asset != null) {
                PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().
                        getServiceUrlWithCountryPreference(ProdRegConstants.PRODUCTMETADATAREQUEST_SERVICE_ID,
                                new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                                    public void onSuccess(URL url) {

                                        String uriSubString = (url.getProtocol() + urlBaseSeparator + url.getHost()).concat(asset);
                                        ProdRegLogger.i("Success values ***", uriSubString);
                                        onUrlReceived.onSuccess(uriSubString);
                                    }

                                    public void onError(ERRORVALUES error, String message) {
                                        ProdRegLogger.i("ERRORVALUES ***", "" + message);
                                    }
                                });
            }
        }
        return null;
    }

}

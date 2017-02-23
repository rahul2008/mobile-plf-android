package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.model.metadata.MetadataSerNumbSampleContent;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.prxclient.Logger.PrxLogger;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;

import java.net.URL;
import java.util.List;

public class ProdRegFindSerialFragment extends ProdRegBaseFragment {

    private ImageView serialNumberImageView;
    private TextView serialNumberTextView;

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
        serialNumberTextView = (TextView) view.findViewById(R.id.serial_number_guide_text);
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
            getImageUrl(productMetadataResponseData, new PrxRequest.OnUrlReceived() {
                @Override
                public void onSuccess(String url) {
                    setSerialNumberTextView(productMetadataResponseData);
                    Log.d("imageUrl", "imageUrl " + url);
                    final ImageLoader imageLoader = ImageRequestHandler.getInstance(getActivity().getApplicationContext()).getImageLoader();
                    imageLoader.get(url, ImageLoader.getImageListener(serialNumberImageView, R.drawable.prodreg_placeholder, R.drawable.prodreg_placeholder));
                }
                @Override
                public void onError(ERRORVALUES errorvalues, String s) {
                }
            });
        }
    }

    private void setSerialNumberTextView(final ProductMetadataResponseData productMetadataResponseData) {
        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            final String snExample = serialNumberSampleContent.getSnExample();
            try {
                final String serialNumberExampleData = snExample.substring(snExample.indexOf(" "), snExample.length());
                final int exampleLength = serialNumberExampleData.length() - 1;
                String serialErrorText = getString(R.string.PPR_serial_number_consists).concat(" ") + exampleLength + " "
                        + getString(R.string.PPR_number_starting).concat(" ")
                        + serialNumberExampleData.charAt(1) + " " + getString(R.string.PPR_eg) + serialNumberExampleData;
                serialNumberTextView.setText(serialErrorText);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }


    private String getImageUrl(final ProductMetadataResponseData productMetadataResponseData, final PrxRequest.OnUrlReceived onUrlReceived) {

        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            final String asset = serialNumberSampleContent.getAsset();

            PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().getServiceUrlWithCountryPreference(ProdRegConstants.PRODUCTMETADATAREQUEST_SERVICE_ID, new ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                public void onSuccess(URL url) {
                    String uriSubString = (url.getProtocol() + "://" + url.getHost()).concat(asset);
                    PrxLogger.i("Success values ***", uriSubString);
                    onUrlReceived.onSuccess(uriSubString);
                }

                public void onError(ERRORVALUES error, String message) {
                    PrxLogger.i("ERRORVALUES ***", "" + message);
                }
            });
        }
        return null;
    }


}

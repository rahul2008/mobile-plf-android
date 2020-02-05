package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.AnalyticsConstants;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.launcher.PRUiHelper;
import com.philips.cdp.prodreg.logging.ProdRegLogger;
import com.philips.cdp.prodreg.model.metadata.MetadataSerNumbSampleContent;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.tagging.ProdRegTagging;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.prxclient.request.PrxRequest;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.appinfra.servicediscovery.model.ServiceDiscoveryService;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ProdRegFindSerialFragment extends ProdRegBaseFragment {

    public static final String urlBaseSeparator = "://";
    private ImageView serialNumberImageView;
    private Label serialNumberTextView;
    private Label serialNumberForamtTextView;
    private static final long serialVersionUID = -6635233525340545669L;


    @Override
    public int getActionbarTitleResId() {
        return R.string.PRG_NavBar_Title;
    }

    @Override
    public String getActionbarTitle() {
        return getString(R.string.PRG_NavBar_Title);
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
        ProdRegTagging.trackPage(AnalyticsConstants.PRG_FIND_SERIAL_NUMBER);
        initViews(view);
        return view;
    }

    private void initViews(final View view) {
        serialNumberImageView = view.findViewById(R.id.device_image);
        Button okFoundButton = view.findViewById(R.id.ok_found_button);
        serialNumberTextView = view.findViewById(R.id.serial_number_guide_text);
        serialNumberForamtTextView = view.findViewById(R.id.serial_number_guide_format_text);

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
                String serialNumberExampleData = snExample.substring(snExample.indexOf(stringSplit) + 1, snExample.length());
                serialNumberExampleData = serialNumberExampleData.replace(emptyString, "");
                String serialErrorText = getString(R.string.PRG_serial_number_consists).concat(emptyString) +
                        serialNumberExampleData.length() + emptyString + getString(R.string.PRG_number_starting).concat(emptyString)
                        + serialNumberExampleData.charAt(0) + emptyString + getString(R.string.PRG_eg) + serialNumberExampleData;
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
                ArrayList<String> serviceIDList = new ArrayList<>();
                serviceIDList.add(ProdRegConstants.PRODUCTMETADATAREQUEST_SERVICE_ID);
                PRUiHelper.getInstance().getAppInfraInstance().getServiceDiscovery().
                        getServicesWithCountryPreference(serviceIDList, new ServiceDiscoveryInterface.OnGetServiceUrlMapListener() {
                            @Override
                            public void onSuccess(Map<String, ServiceDiscoveryService> urlMap) {
                                try {
                                    URL url = new URL(urlMap.get(ProdRegConstants.PRODUCTMETADATAREQUEST_SERVICE_ID).getConfigUrls());
                                    String uriSubString = (url.getProtocol() + urlBaseSeparator + url.getHost()).concat(asset);
                                    ProdRegLogger.i("Success values ***", uriSubString);
                                    onUrlReceived.onSuccess(uriSubString);
                                } catch (MalformedURLException e) {
                                    ProdRegLogger.e("Exception values ***",e.getMessage());
                                }
                            }

                            @Override
                            public void onError(ERRORVALUES error, String message) {
                                ProdRegLogger.i("ERRORVALUES ***", "" + message);
                            }
                        },null);
            }
        }
        return null;
    }

}

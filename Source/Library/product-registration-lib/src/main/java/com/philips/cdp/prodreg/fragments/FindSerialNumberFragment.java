package com.philips.cdp.prodreg.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.philips.cdp.prodreg.constants.ProdRegConstants;
import com.philips.cdp.prodreg.imagehandler.ImageRequestHandler;
import com.philips.cdp.prodreg.model.metadata.MetadataSerNumbSampleContent;
import com.philips.cdp.prodreg.model.metadata.ProductMetadataResponseData;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;
import com.philips.cdp.registration.configuration.RegistrationConfiguration;

import java.util.List;

public class FindSerialNumberFragment extends ProdRegBaseFragment {

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
        final View view = inflater.inflate(R.layout.prod_reg_find_serial_number, container, false);
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
            final String imageUrl = getImageUrl(productMetadataResponseData);
            setSerialNumberTextView(productMetadataResponseData);
            final ImageLoader imageLoader = ImageRequestHandler.getInstance(getActivity().getApplicationContext()).getImageLoader();
            imageLoader.get(imageUrl, ImageLoader.getImageListener(serialNumberImageView, R.drawable.product_placeholder, R.drawable.product_placeholder));
        }
    }

    private void setSerialNumberTextView(final ProductMetadataResponseData productMetadataResponseData) {
        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            final String snExample = serialNumberSampleContent.getSnExample();
            try {
                final String serialNumberExampleData = snExample.substring(snExample.indexOf(" "), snExample.length());
                final int exampleLength = serialNumberExampleData.length() - 1;
                String serialErrorText = "The serial number consists of " + exampleLength + " numbers, starting with "
                        + serialNumberExampleData.charAt(1) + ". E.g." + serialNumberExampleData;
                serialNumberTextView.setText(serialErrorText);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }
    }

    private String getImageUrl(final ProductMetadataResponseData productMetadataResponseData) {
        String url = "";
        if (productMetadataResponseData != null) {
            final MetadataSerNumbSampleContent serialNumberSampleContent = productMetadataResponseData.getSerialNumberSampleContent();
            final String asset = serialNumberSampleContent.getAsset();
            url = getServerInfo().concat(asset);
        }
        return url;
    }

    public String getServerInfo() {
        String mConfiguration = RegistrationConfiguration.getInstance().getRegistrationEnvironment();
        String mServerInfo = "";
        if (mConfiguration.equalsIgnoreCase("Development")) {
            mServerInfo = "https://10.128.41.113.philips.com/prx";
        } else if (mConfiguration.equalsIgnoreCase("Testing")) {
            mServerInfo = "https://tst.philips.com/prx";
        } else if (mConfiguration.equalsIgnoreCase("Evaluation")) {
            mServerInfo = "https://acc.philips.com/prx";
        } else if (mConfiguration.equalsIgnoreCase("Staging")) {
            mServerInfo = "https://dev.philips.com/prx";
        } else if (mConfiguration.equalsIgnoreCase("Production")) {
            mServerInfo = "https://www.philips.com/prx";
        }
        return mServerInfo;
    }
}

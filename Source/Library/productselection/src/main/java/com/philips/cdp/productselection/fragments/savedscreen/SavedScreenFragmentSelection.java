package com.philips.cdp.productselection.fragments.savedscreen;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.customview.CustomFontTextView;
import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingTabletFragment;
import com.philips.cdp.productselection.prx.VolleyWrapper;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.productselection.R;

/**
 * This class holds responsible to inflate the UI of the saved screen & reselecting the product to save &
 * redirecting control the \
 * product information displaying screen.
 * <p/>
 * Created by naveen@philips.com on 03-Feb-16.
 */
public class SavedScreenFragmentSelection extends ProductSelectionBaseFragment implements View.OnClickListener {

    private static final String TAG = SavedScreenFragmentSelection.class.getSimpleName();
    private Button mSettings = null;
    private Button mRedirectingButton = null;
    private LinearLayout mProductContainer = null;
    private LinearLayout.LayoutParams mProductContainerParams;
    private LinearLayout mProductContainer1 = null;
    private LinearLayout.LayoutParams mProductContainerParams1;
    private CustomFontTextView mProductName = null;
    private CustomFontTextView mProductCtn = null;
    private ImageView mProductImage = null;

    /**
     * setting Listeners & setting the values & controls to the inflated view's of the screen "fragment_saved_screen.xml"
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mProductContainer = (LinearLayout) getActivity().findViewById(R.id.savedScreen_screen_child_one);
        mProductContainerParams = (LinearLayout.LayoutParams) mProductContainer.getLayoutParams();
        mProductContainer1 = (LinearLayout) getActivity().findViewById(R.id.savedScreen_screen_child_two);
        mProductContainerParams1 = (LinearLayout.LayoutParams) mProductContainer1.getLayoutParams();
        mProductName.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getProductTitle());
        mProductCtn.setText(ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getCtn());
        loadProductImage(mProductImage);

        mSettings.setOnClickListener(this);
        mRedirectingButton.setOnClickListener(this);

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);
    }


    /**
     * Inflating the View of the Screen "fragment_saved_screen.xmls"
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_saved_screen, container, false);
        mSettings = (Button) view.findViewById(R.id.savedscreen_button_settings);
        mRedirectingButton = (Button) view.findViewById(R.id.savedscreen_button_viewproductdetails);
        mProductName = (CustomFontTextView) view.findViewById(R.id.savedscreen_productname);
        mProductCtn = (CustomFontTextView) view.findViewById(R.id.savedscreen_productvariant);
        mProductImage = (ImageView) view.findViewById(R.id.savedscreen_productimage);
        return view;
    }


    protected void loadProductImage(final ImageView image) {
        String imagepath = ProductModelSelectionHelper.getInstance().getUserSelectedProduct().getData().getImageURL();
        int imageWidth = (int) (getResources().getDimension(R.dimen.productdetails_screen_image) * Resources.getSystem().getDisplayMetrics().density);
        imagepath = /*imagepath + "?wid=" + imageWidth + "&;";*/
                imagepath + "?wid=" + imageWidth +
                        "&hei=" + imageWidth +
                        "&fit=fit,1";

        ProductSelectionLogger.v(TAG, "Image : " + imagepath);

        final ImageRequest request = new ImageRequest(imagepath,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });


        VolleyWrapper.getInstance(getActivity()).addToRequestQueue(request);

    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    @Override
    public void setViewParams(Configuration config) {

//        if (config.orientation == Configuration.ORIENTATION_PORTRAIT && isTablet()) {
//            ProductSelectionLogger.i(TAG, "setViewParams  : portrait");
////            mProductContainerParams.leftMargin = mProductContainerParams.rightMargin = mPortraitTablet;
////            mProductContainerParams1.leftMargin = mProductContainerParams1.rightMargin = mPortraitTablet;
//        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE && isTablet()) {
//            // Control for Split Screen Margin
//        }
//        mProductContainer.setLayoutParams(mProductContainerParams);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.Confirmation_Title);
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    @Override
    public void onClick(View v) {
        if (isConnectionAvailable()) {
            if (v.getId() == R.id.savedscreen_button_settings) {
//                if (isConnectionAvailable()) {
                if (isTablet()) {
                    showFragment(new ProductSelectionListingTabletFragment());
                } else {
                    showFragment(new ProductSelectionListingFragment());
                }
//                }
            } else if (v.getId() == R.id.savedscreen_button_viewproductdetails) {
                if (isConnectionAvailable()) {
                    ProductModelSelectionHelper.getInstance().getProductListener().onProductModelSelected(ProductModelSelectionHelper.getInstance().getUserSelectedProduct());
                    clearBackStackHistory(getActivity());
                }
            }
        }
    }
}

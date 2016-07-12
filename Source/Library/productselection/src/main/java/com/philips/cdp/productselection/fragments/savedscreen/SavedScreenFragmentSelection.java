package com.philips.cdp.productselection.fragments.savedscreen;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.R;
import com.philips.cdp.productselection.customview.CustomFontTextView;
import com.philips.cdp.productselection.fragments.detailedscreen.DetailedScreenFragmentSelection;
import com.philips.cdp.productselection.fragments.homefragment.ProductSelectionBaseFragment;
import com.philips.cdp.productselection.fragments.listfragment.ProductSelectionListingFragment;
import com.philips.cdp.productselection.fragments.welcomefragment.WelcomeScreenFragmentSelection;
import com.philips.cdp.productselection.prx.VolleyWrapper;
import com.philips.cdp.productselection.utils.Constants;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.tagging.Tagging;

import java.util.List;

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
    private LinearLayout mProductContainerBelow = null;
    private LinearLayout.LayoutParams mProductContainerBelowParams;
    private LinearLayout.LayoutParams mProductContainerParams;
    private LinearLayout mProductContainer1 = null;
    private LinearLayout.LayoutParams mProductContainerParams1;
    private CustomFontTextView mProductName = null;
    private CustomFontTextView mProductCtn = null;
    private ImageView mProductImage = null;
    private RelativeLayout mRightPanelLayout = null;
    private RelativeLayout mLeftPanelLayout = null;
    private LinearLayout.LayoutParams mLeftPanelLayoutParams = null;
    private LinearLayout.LayoutParams mRightPanelLayoutParams = null;

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

        mProductContainerBelow = (LinearLayout) getActivity().findViewById(R.id.savedScreen_screen_parent);
        mProductContainerBelowParams = (LinearLayout.LayoutParams) mProductContainerBelow.getLayoutParams();

        mProductContainer1 = (LinearLayout) getActivity().findViewById(R.id.savedScreen_screen_child_two);
        mProductContainerParams1 = (LinearLayout.LayoutParams) mProductContainer1.getLayoutParams();
        mProductName.setText(mUserSelectedProduct.getData().getProductTitle());
        mProductCtn.setText(mUserSelectedProduct.getData().getCtn());
        loadProductImage(mProductImage);

        /* These views are required for tablet design(GUI).*/
        mLeftPanelLayout = (RelativeLayout) getActivity().findViewById(R.id.fragmentTabletProductList);
        mRightPanelLayout = (RelativeLayout) getActivity().findViewById(R.id.fragmentTabletProductDetailsParent);

        mSettings.setOnClickListener(this);
        mRedirectingButton.setOnClickListener(this);

        Configuration configuration = getResources().getConfiguration();
        setViewParams(configuration);


        Tagging.trackPage(Constants.PAGE_CONFIRMATION_SCREEN, getPreviousName());
        setPreviousPageName(Constants.PAGE_CONFIRMATION_SCREEN);
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
        mRedirectingButton = (Button) view.findViewById(R.id.savedscreen_button_continue);
        mProductName = (CustomFontTextView) view.findViewById(R.id.savedscreen_productname);
        mProductCtn = (CustomFontTextView) view.findViewById(R.id.savedscreen_productvariant);
        mProductImage = (ImageView) view.findViewById(R.id.savedscreen_productimage);
        return view;
    }


    protected void loadProductImage(final ImageView image) {
        String imagepath = mUserSelectedProduct.getData().getImageURL();
        int imageWidth = (int) (getResources().getDimension(R.dimen.productdetails_screen_image) * Resources.getSystem().getDisplayMetrics().density);
        imagepath = /*imagepath + "?wid=" + imageWidth + "&;";*/
                imagepath + "?wid=" + imageWidth +
                        "&hei=" + imageWidth +
                        "&fit=fit,1";

        ProductSelectionLogger.v(TAG, "Image loaded in the Saved Screen is from the PATH : " + imagepath);

        final ImageRequest request = new ImageRequest(imagepath,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        image.setImageBitmap(bitmap);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                        ProductSelectionLogger.e(TAG, "Selected Product Image is failed donalod  : " + error);
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


        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mProductContainerBelowParams.leftMargin = mProductContainerBelowParams.rightMargin = mLeftRightMarginPort;
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            mProductContainerBelowParams.leftMargin = mProductContainerBelowParams.rightMargin = mLeftRightMarginLand;
        }

        mProductContainerBelow.setLayoutParams(mProductContainerBelowParams);
    }

    private void guiAlignmentTablet(Configuration config) {

        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            View listViewSparator = ((View) getActivity().findViewById(R.id.listViewSeperator));
            if (listViewSparator != null)
                listViewSparator.setVisibility(View.GONE);
//            Fragment fragmentTablet = getActivity().getSupportFragmentManager().findFragmentById(R.id.productListContainerTablet);
//            fragmentTablet.

            if (isListViewRequiredInTablet()) {
                mLeftPanelLayout.setVisibility(View.VISIBLE);
                mRightPanelLayout.setVisibility(View.GONE);
                mLeftPanelLayoutParams.weight = 1.0f;
                mRightPanelLayoutParams.weight = 0.0f;
            } else {
                mLeftPanelLayoutParams.weight = 0.0f;
                mRightPanelLayoutParams.weight = 1.0f;
                mRightPanelLayout.setVisibility(View.VISIBLE);
                mLeftPanelLayout.setVisibility(View.GONE);
            }
//            mRightPanelLayoutParams.leftMargin = mRightPanelLayoutParams.rightMargin = mLeftRightMarginPort;
        } else if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            ((View) getActivity().findViewById(R.id.listViewSeperator)).setVisibility(View.VISIBLE);
            mLeftPanelLayout.setVisibility(View.VISIBLE);
            mRightPanelLayout.setVisibility(View.VISIBLE);
            mRightPanelLayoutParams.weight = 0.60f;
            mLeftPanelLayoutParams.weight = 0.39f;
//            mRightPanelLayoutParams.leftMargin = mRightPanelLayoutParams.rightMargin = 0;
        }
        mRightPanelLayout.setLayoutParams(mRightPanelLayoutParams);
        mLeftPanelLayout.setLayoutParams(mLeftPanelLayoutParams);
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.Confirmation_Title);
    }

    private void removeDetailsScreen(){
        FragmentManager fragManager = getActivity().getSupportFragmentManager();
        fragManager.popBackStack();
        List<Fragment> listFragment = fragManager.getFragments();
        for (int i = listFragment.size() - 1; i >= 0; i--) {
            Fragment fragment = listFragment.get(i);

            try {
                if (fragment != null && (fragment instanceof DetailedScreenFragmentSelection)) {
                    fragManager.popBackStack();
                }
            }catch (IllegalStateException e){
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (isConnectionAvailable()) {
            if (v.getId() == R.id.savedscreen_button_settings) {
//                if (isConnectionAvailable()) {
                Tagging.trackAction(Constants.ACTION_KEY_SEND_DATA, Constants.ACTION_NAME_SPECIAL_EVENT,
                        Constants.ACTION_VALUE_CHANGE_PRODUCT);
                removeDetailsScreen();
//                }
            } else if (v.getId() == R.id.savedscreen_button_continue) {
                Tagging.trackAction(Constants.ACTION_KEY_SEND_DATA, Constants.ACTION_NAME_SPECIAL_EVENT,
                        Constants.ACTION_VALUE_CONTINUE);
                setPreference(mUserSelectedProduct.getData().getCtn());
                ProductModelSelectionHelper.getInstance().getProductSelectionListener().onProductModelSelected(mUserSelectedProduct);
                clearBackStackHistory(getActivity());

//                    if (!isLaunchedAsTabletLandscape()) {
//                        removeProdSelectionScreens();
//                    }
            }
        }
    }
}

package com.philips.cdp.digitalcare.productdetails;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.philips.cdp.digitalcare.DigitalCareBaseFragment;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.R;
import com.philips.cdp.digitalcare.analytics.AnalyticsConstants;
import com.philips.cdp.digitalcare.analytics.AnalyticsTracker;
import com.philips.cdp.digitalcare.util.DigiCareLogger;

/**
 * ProductDetailsFragment will help to show product details.
 *
 * @author : Ritesh.jha@philips.com
 * @since : 16 Jan 2015
 */
public class ProductDetailsFragment extends DigitalCareBaseFragment implements
        OnClickListener {

    private static String TAG = ProductDetailsFragment.class.getSimpleName();

    private RelativeLayout mFirstContainer = null;
    private LinearLayout.LayoutParams mFirstContainerParams = null;
    private LinearLayout.LayoutParams mSecondContainerParams = null;
    private LinearLayout mProdButtonsParent = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onCreateView");
        View mView = inflater.inflate(R.layout.fragment_view_product,
                container, false);
        return mView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        DigiCareLogger.d(TAG, "onActivityCreated");
        super.onActivityCreated(savedInstanceState);
        mFirstContainer = (RelativeLayout) getActivity().findViewById(
                R.id.toplayout);
        mProdButtonsParent = (LinearLayout) getActivity().findViewById(
                R.id.prodbuttonsParent);

        mFirstContainerParams = (LinearLayout.LayoutParams) mFirstContainer
                .getLayoutParams();
        mSecondContainerParams = (LinearLayout.LayoutParams) mProdButtonsParent
                .getLayoutParams();
        Configuration config = getResources().getConfiguration();
        setViewParams(config);

        createProductDetailsMenu();
        AnalyticsTracker.trackPage(AnalyticsConstants.PAGE_PRODCUT_DETAILS,
                getPreviousName());
    }

    private void createProductDetailsMenu() {
        TypedArray titles = getResources().obtainTypedArray(R.array.product_menu_title);

        for (int i = 0; i < titles.length(); i++) {
            createButtonLayout(titles.getResourceId(i, 0));
        }
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);

        setViewParams(config);
    }

    @Override
    public void setViewParams(Configuration config) {
        if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginPort;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginPort;
        } else {
            mFirstContainerParams.leftMargin = mFirstContainerParams.rightMargin = mLeftRightMarginLand;
            mSecondContainerParams.leftMargin = mSecondContainerParams.rightMargin = mLeftRightMarginLand;
        }
        mFirstContainer.setLayoutParams(mFirstContainerParams);
        mProdButtonsParent.setLayoutParams(mSecondContainerParams);
    }

    /**
     * Create RelativeLayout at runTime. RelativeLayout will have button and
     * image together.
     */
    private void createButtonLayout(int buttonTitleResId) {
        String buttonTitle = getResources().getResourceEntryName(buttonTitleResId);

        float density = getResources().getDisplayMetrics().density;
        String packageName = getActivity().getPackageName();

        int title = getResources().getIdentifier(
                packageName + ":string/" + buttonTitle, null, null);

        RelativeLayout relativeLayout = createRelativeLayout(buttonTitle, density);
        Button button = createButton(density, buttonTitle);
        relativeLayout.addView(button);
        setButtonParams(button);
        mProdButtonsParent.addView(relativeLayout);
        setRelativeLayoutParams(relativeLayout, density);
        /*
		 * Setting tag because we need to get String title for this view which
		 * needs to be handled at button click.
		 */
        relativeLayout.setTag(buttonTitle);
        relativeLayout.setOnClickListener(this);
    }

    @SuppressLint("NewApi")
    private RelativeLayout createRelativeLayout(String buttonTitle, float density) {
        RelativeLayout relativeLayout = new RelativeLayout(getActivity());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        relativeLayout.setLayoutParams(params);
        relativeLayout
                .setBackgroundResource(R.drawable.selector_option_button_bg);

        return relativeLayout;
    }

    private void setRelativeLayoutParams(RelativeLayout relativeLayout,
                                         float density) {
        LinearLayout.LayoutParams param = (LinearLayout.LayoutParams) relativeLayout
                .getLayoutParams();
        param.topMargin = (int) (15 * density);
        relativeLayout.setLayoutParams(param);
    }

    private Button createButton(float density, String title) {
        Button button = new Button(getActivity(), null, R.style.fontButton);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, (int) (getActivity().getResources()
                .getDimension(R.dimen.support_btn_height) * density));
        button.setLayoutParams(params);

        button.setGravity(Gravity.START | Gravity.CENTER);
        button.setPadding((int) (20 * density), 0, 0, 0);
        button.setTextAppearance(getActivity(), R.style.fontButton);
        Typeface buttonTypeface = Typeface.createFromAsset(getActivity().getAssets(), "digitalcarefonts/CentraleSans-Book.otf");
        button.setTypeface(buttonTypeface);
        button.setText(title);
        return button;
    }

    private void setButtonParams(Button button) {
        RelativeLayout.LayoutParams buttonParams = (LayoutParams) button
                .getLayoutParams();
        buttonParams.addRule(RelativeLayout.CENTER_VERTICAL,
                RelativeLayout.TRUE);
        buttonParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT,
                RelativeLayout.TRUE);
        button.setLayoutParams(buttonParams);
    }

    @Override
    public void onClick(View view) {

        String tag = (String) view.getTag();

        boolean actionTaken = false;
        if (DigitalCareConfigManager.getInstance()
                .getProductMenuListener() != null) {
            DigitalCareConfigManager.getInstance()
                    .getProductMenuListener().onProductMenuItemClicked(tag);
        }

        if (actionTaken) {
            return;
        }

        if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_open_manual))) {
        } else if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_download_manual))) {
        } else if (tag.equalsIgnoreCase(getResources().getResourceEntryName(
                R.string.product_information))) {
        }
    }

    @Override
    public String getActionbarTitle() {
        return getResources().getString(R.string.product_info);
    }

    @Override
    public String setPreviousPageName() {
        return AnalyticsConstants.PAGE_PRODCUT_DETAILS;
    }
}

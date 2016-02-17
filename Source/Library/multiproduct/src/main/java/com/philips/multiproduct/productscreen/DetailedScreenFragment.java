package com.philips.multiproduct.productscreen;

import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.uikit.customviews.CircleIndicator;
import com.philips.multiproduct.R;
import com.philips.multiproduct.customview.CustomFontTextView;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;
import com.philips.multiproduct.productscreen.adapter.ProductAdapter;
import com.philips.multiproduct.savedscreen.SavedScreenFragment;

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

    private static ViewPager mViewpager = null;
    private static CircleIndicator mIndicater =null;
    private static CustomFontTextView mProductName = null;
    private static Button mSelectButton = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_detailed_screen, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewpager = (ViewPager) getActivity().findViewById(R.id.detailedscreen_pager);
        mIndicater = (CircleIndicator) getActivity().findViewById(R.id.detailedscreen_indicator);
        mProductName = (CustomFontTextView) getActivity().findViewById(R.id.detailed_screen_productname);
        mSelectButton = (Button) getActivity().findViewById(R.id.detailedscreen_select_button);
        mViewpager.setAdapter(new ProductAdapter(getChildFragmentManager()));
        mIndicater.setViewPager(mViewpager);
        mProductName.setTypeface(Typeface.DEFAULT_BOLD);
        mSelectButton.setOnClickListener(this);
    }

    private void initializeUi(){
        mProductName.setText(mProductSummaryModel.getData().getProductTitle());
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle() {
        return "Product Test";
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.detailedscreen_select_button)
            if (isConnectionAvailable())
                showFragment(new SavedScreenFragment());
    }

    @Override
    public void update(Observable observable, Object data) {
       Log.d("testing", "Detailed Screen -- Clicked again : " + mProductSummaryModel.getData().getProductTitle());
       if(mProductName == null){
            return;
        }
        initializeUi();
    }
}

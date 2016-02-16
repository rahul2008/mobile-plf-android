package com.philips.multiproduct.savedscreen;

import android.content.res.Configuration;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.philips.multiproduct.R;
import com.philips.multiproduct.listfragment.ProductListingFragment;
import com.philips.multiproduct.homefragment.MultiProductBaseFragment;

/**
 * This class holds responsible to inflate the UI of the saved screen & reselecting the product to save &
 * redirecting control the \
 * product information displaying screen.
 * <p/>
 * Created by naveen@philips.com on 03-Feb-16.
 */
public class SavedScreenFragment extends MultiProductBaseFragment implements View.OnClickListener {

    private static final String TAG = SavedScreenFragment.class.getSimpleName();
    private Button mSettings = null;
    private Button mRedirectingButton = null;


    /**
     * setting Listeners & setting the values & controls to the inflated view's of the screen "fragment_saved_screen.xml"
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
          mSettings.setOnClickListener(this);

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
        return view;
    }

    @Override
    public void setViewParams(Configuration config) {

    }

    @Override
    public String getActionbarTitle()   {
        return getResources().getString(R.string.confirmation);
    }

    @Override
    public String setPreviousPageName() {
        return null;
    }

    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.savedscreen_button_settings) {
            if (isConnectionAvailable())
                showFragment(new ProductListingFragment());
        }
    }
}

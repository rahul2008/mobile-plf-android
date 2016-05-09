package com.philips.cdp.sampledigitalcare;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.MainMenuListener;
import com.philips.cdp.digitalcare.productdetails.ProductMenuListener;
import com.philips.cdp.digitalcare.social.SocialProviderListener;
import com.philips.cdp.digitalcare.util.DigiCareLogger;
import com.philips.cdp.localematch.PILLocaleManager;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.sampledigitalcare.adapter.SampleAdapter;
import com.philips.cdp.sampledigitalcare.adapter.SimpleItemTouchHelperCallback;
import com.philips.cdp.sampledigitalcare.view.CustomDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
    This is sample class which will try to simulate, "how to use APIs and integrate digitalcare.

    @author: ritesh.jha@philips.com
 */

public class LaunchDigitalCare extends FragmentActivity implements OnClickListener,
        MainMenuListener, ProductMenuListener, SocialProviderListener {

    private static final String TAG = LaunchDigitalCare.class.getSimpleName();
    public static ArrayList<String> mList = null;
    private static boolean mActivityButtonSelected = true;
    private static boolean mFragmentButtonSelected = true;
    private Button mLaunchDigitalCare = null;
    private Button mLaunchAsFragment = null;
    private ImageButton mAddButton = null;
    private RecyclerView mRecyclerView = null;
    private SampleAdapter adapter = null;


    private Spinner mLanguage_spinner, mCountry_spinner;
    private String mLanguage[], mCountry[], mlanguageCode[], mcountryCode[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_digital_care);

        mLaunchDigitalCare = (Button) findViewById(R.id.launchDigitalCare);
        mLaunchAsFragment = (Button) findViewById(R.id.launchAsFragment);
        mAddButton = (ImageButton) findViewById(R.id.addimageButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddButton.setOnClickListener(this);

        // set listener
        mLaunchDigitalCare.setOnClickListener(this);
        mLaunchAsFragment.setOnClickListener(this);

        // setting language spinner
        mLanguage_spinner = (Spinner) findViewById(R.id.spinner1);
        mLanguage = getResources().getStringArray(R.array.Language);
        mlanguageCode = getResources().getStringArray(R.array.Language_code);
        ArrayAdapter<String> mLanguage_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mLanguage);
        mLanguage_spinner.setAdapter(mLanguage_adapter);

        // setting country spinner
        mCountry_spinner = (Spinner) findViewById(R.id.spinner2);
        mCountry = getResources().getStringArray(R.array.country);
        mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCountry);
        mCountry_spinner.setAdapter(mCountry_adapter);


        // Ctn List Code Snippet

        if (mList == null)
            mList = new ArrayList<String>();
        if (mList.size() == 0)
            addCtnData();

        if (adapter == null)
            adapter = new SampleAdapter(mList);
        adapter = setAdapter(mList);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);


        // Digital care initialization
        initializeDigitalCareLibrary();


    }


    private void addCtnData() {

        List<String> mCtnList = Arrays.asList(getResources().getStringArray(R.array.productselection_ctnlist));
        for (int i = 0; i < mCtnList.size(); i++) {
            mList.add(mCtnList.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        DigitalCareConfigManager.getInstance().unregisterMainMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterProductMenuListener(this);
        DigitalCareConfigManager.getInstance().unregisterSocialProviderListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        mLanguage_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initializeDigitalCareLibrary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        mCountry_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                initializeDigitalCareLibrary();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (mActivityButtonSelected) {
            mLaunchDigitalCare.setVisibility(View.VISIBLE);
        } else {
            mLaunchDigitalCare.setVisibility(View.INVISIBLE);
        }

        if (mFragmentButtonSelected) {
            mLaunchAsFragment.setVisibility(View.VISIBLE);
        } else {
            mLaunchAsFragment.setVisibility(View.INVISIBLE);
        }
    }

    @NonNull
    private SampleAdapter setAdapter(ArrayList<String> mList) {
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

    private void launchDialog() {
        CustomDialog dialog = new CustomDialog(this, mList, new Listener() {
            @Override
            public void updateList(ArrayList<String> productList) {
                mList = productList;
                setAdapter(mList);
                Log.d(TAG, " Products Size = " + mList.size());
            }
        });
        dialog.show();
    }

    private void initializeDigitalCareLibrary() {

        PILLocaleManager localeManager = new PILLocaleManager(this);
        localeManager.setInputLocale(mlanguageCode[mLanguage_spinner.getSelectedItemPosition()], mcountryCode[mCountry_spinner.getSelectedItemPosition()]);
      //  localeManager.setInputLocale("ar", "SA");

        // Initializing DigitalCare Component.
        DigitalCareConfigManager.getInstance().initializeDigitalCareLibrary(
                this);

        // Set DigitalCareLibrary Listeners
        DigitalCareConfigManager.getInstance().registerMainMenuListener(this);
        DigitalCareConfigManager.getInstance()
                .registerProductMenuListener(this);
        DigitalCareConfigManager.getInstance().registerSocialProviderListener(
                this);


        DigiCareLogger.enableLogging();
    }

    @Override
    public boolean onMainMenuItemClicked(String mainMenuItem) {
        if (mainMenuItem.equals(getStringKey(R.string.sign_into_my_philips))) {
            Intent intent = new Intent(LaunchDigitalCare.this,
                    DummyScreen.class);
            startActivity(intent);
            return true;
        }
        /*if (mainMenuItem.equals(getStringKey(R.string.view_product_details))) {
            Intent intent = new Intent(LaunchDigitalCare.this,
                    DummyScreen.class);
            startActivity(intent);
            return true;
        }*/
        return false;
    }


    private String getStringKey(int resId) {
        return getResources().getResourceEntryName(resId);
    }

    @Override
    public boolean onProductMenuItemClicked(String productMenu) {
        return false;
    }

    @Override
    public boolean onSocialProviderItemClicked(String socialProviderItem) {
        return false;
    }

    @Override
    public void onClick(View view) {
    /*
      Setting AppID is very much required from App side, in order to TAG the page. Here in below code
      we are putting dummy value. Please provide proper APP_ID from you App.
      Also if tagging is not enabled , consumer care is not tagging any events*/

        DigitalCareConfigManager.getInstance().setAppTaggingInputs(true, "App_ID_101", "AppName", "CurrentPageName");

        /*
         * Take values from GUI editText.
         */

        switch (view.getId()) {

            case R.id.addimageButton:
                launchDialog();
                break;
            case R.id.launchDigitalCare:

                mActivityButtonSelected = true;
                mFragmentButtonSelected = false;

                mLaunchAsFragment.setVisibility(View.INVISIBLE);


                String[] ctnList = new String[mList.size()];
                for (int i = 0; i < mList.size(); i++)
                    ctnList[i] = mList.get(i);
                //  if (ctnList.length != 0) {
                HardcodedProductList productsSelection = new HardcodedProductList(ctnList);
                productsSelection.setCatalog(Catalog.CARE);
                productsSelection.setSector(Sector.B2C);
                ActivityLauncher uiLauncher = new ActivityLauncher(com.philips.cdp.productselection.launchertype.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                        R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground);
                uiLauncher.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                DigitalCareConfigManager.getInstance().invokeDigitalCare(uiLauncher, productsSelection);
              /*  } else
                    Toast.makeText(this, "CTN list is null", Toast.LENGTH_SHORT).show();*/
                break;
            case R.id.launchAsFragment:

                mActivityButtonSelected = false;
                mFragmentButtonSelected = true;

                mLaunchDigitalCare.setVisibility(View.INVISIBLE);


                startActivity(new Intent(this, SampleActivity.class));
                break;

        }
    }

   /* private void setDigitalCareLocale(String language, String country) {

        DigitalCareConfigManager.getInstance().setLocale(language, country);


    }*/
}

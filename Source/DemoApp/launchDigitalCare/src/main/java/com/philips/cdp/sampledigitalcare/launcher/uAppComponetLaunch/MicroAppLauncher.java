package com.philips.cdp.sampledigitalcare.launcher.uAppComponetLaunch;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
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
import android.widget.TextView;

import com.philips.cdp.digitalcare.CcDependencies;
import com.philips.cdp.digitalcare.CcInterface;
import com.philips.cdp.digitalcare.CcLaunchInput;
import com.philips.cdp.digitalcare.CcSettings;
import com.philips.cdp.digitalcare.DigitalCareConfigManager;
import com.philips.cdp.digitalcare.listeners.CcListener;
import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.sampledigitalcare.DummyScreen;
import com.philips.cdp.sampledigitalcare.adapter.Listener;
import com.philips.cdp.sampledigitalcare.adapter.SampleAdapter;
import com.philips.cdp.sampledigitalcare.adapter.SimpleItemTouchHelperCallback;
import com.philips.cdp.sampledigitalcare.util.ThemeHelper;
import com.philips.cdp.sampledigitalcare.util.ThemeUtil;
import com.philips.cdp.sampledigitalcare.view.CustomDialog;
import com.philips.cl.di.dev.pa.R;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

//import com.philips.platform.appinfra.AppInfraSingleton;

/*
    This is sample class which will try to simulate, "how to use APIs and integrate digitalcare.

    @author: ritesh.jha@philips.com
 */

public class MicroAppLauncher extends FragmentActivity implements OnClickListener,
        CcListener {

    private static final String TAG = MicroAppLauncher.class.getSimpleName();
    public static ArrayList<String> mList = null;
    private static boolean mActivityButtonSelected = true;
    private static boolean mFragmentButtonSelected = true;
    private Button mLaunchDigitalCare = null;
    private Button mLaunchAsFragment = null;
    private Button mChangeTheme = null;
    private ImageButton mAddButton = null;
    private RecyclerView mRecyclerView = null;
    private SampleAdapter adapter = null;


    private Spinner mCountry_spinner;
    private String mCountry[], mcountryCode[];
    private CcSettings ccSettings;
    private CcLaunchInput ccLaunchInput;
    private AppInfraInterface mAppInfraInterface;
    private ThemeUtil mThemeUtil;
    private ThemeHelper themeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digital_care);


        mLaunchDigitalCare = (Button) findViewById(R.id.launchDigitalCare);
        mLaunchAsFragment = (Button) findViewById(R.id.launchAsFragment);
        mChangeTheme = (Button) findViewById(R.id.change_theme);
        mAddButton = (ImageButton) findViewById(R.id.addimageButton);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAddButton.setOnClickListener(this);

        // set listener
        mLaunchDigitalCare.setOnClickListener(this);
        mLaunchAsFragment.setOnClickListener(this);
        mChangeTheme.setOnClickListener(this);
        mThemeUtil = new ThemeUtil(getApplicationContext().getSharedPreferences(
                this.getString(R.string.app_name), Context.MODE_PRIVATE));

        // setting country spinner
        mCountry_spinner = (Spinner) findViewById(R.id.spinner2);
        mCountry = getResources().getStringArray(R.array.country);
        mcountryCode = getResources().getStringArray(R.array.country_code);
        ArrayAdapter<String> mCountry_adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, mCountry);
        mCountry_spinner.setAdapter(mCountry_adapter);
        restoreCountryOption();

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
    }


    private void addCtnData() {

        List<String> mCtnList = Arrays.asList(getResources().getStringArray(R.array.productselection_ctnlist));
        for (int i = 0; i < mCtnList.size(); i++) {
            mList.add(mCtnList.get(i));
        }
    }

    @Override
    protected void onDestroy() {
        DigitalCareConfigManager.getInstance().unRegisterCcListener(this);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        final TextView tv = (TextView) findViewById(R.id.textViewCurrentCountry);
        mAppInfraInterface.getServiceDiscovery().getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                tv.setText("Country from Service Discovery : " +s);
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {

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
        LinearLayoutManager layoutManager =new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                layoutManager.getOrientation());
        mRecyclerView.addItemDecoration(mDividerItemDecoration);
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

    private void restoreCountryOption() {
        if(mAppInfraInterface == null) {
            mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        }
        mAppInfraInterface.getServiceDiscovery().getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
            @Override
            public void onSuccess(String s, SOURCE source) {
                for (int i=0; i < mcountryCode.length; i++) {
                    if(s.equalsIgnoreCase(mcountryCode[i])) {
                        mCountry_spinner.setSelection(i);
                    }
                }
            }

            @Override
            public void onError(ERRORVALUES errorvalues, String s) {
            }
        });
    }

    private void initializeDigitalCareLibrary() {
/*

        if(!(mCountry_spinner.getSelectedItemId() == 0)){
            mAppInfraInterface.getServiceDiscovery().setHomeCountry(mcountryCode[mCountry_spinner.getSelectedItemPosition()]);
        }
*/
        if(mAppInfraInterface == null) {
            mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        }
        
        mAppInfraInterface.getServiceDiscovery().setHomeCountry(mcountryCode[mCountry_spinner.getSelectedItemPosition()]);
    }

    @Override
    public boolean onMainMenuItemClicked(String mainMenuItem) {
        if (mainMenuItem.equals(getStringKey(R.string.sign_into_my_philips))) {
            Intent intent = new Intent(MicroAppLauncher.this,
                    DummyScreen.class);
            startActivity(intent);
            return true;
        }
        /*if (mainMenuItem.equals(getStringKey(R.string.consumercare_view_product_details))) {
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

        //   DigitalCareConfigManager.getInstance().setAppTaggingInputs(true, "App_ID_101", "AppName", "CurrentPageName");

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
               /*  ActivityLauncher uiLauncher = new ActivityLauncher(com.philips.cdp.productselection.launchertype.ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                        R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground);
                uiLauncher.setAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);
                DigitalCareConfigManager.getInstance().invokeDigitalCare(uiLauncher, productsSelection);*/

                final com.philips.platform.uappframework.launcher.ActivityLauncher activityLauncher =
                        new com.philips.platform.uappframework.launcher.ActivityLauncher
                                (com.philips.platform.uappframework.
                                        launcher.ActivityLauncher.
                                        ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                                        mThemeUtil.getCurrentTheme());

                activityLauncher.setCustomAnimation(R.anim.slide_in_bottom, R.anim.slide_out_bottom);

//                mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());

                final CcInterface ccInterface = new CcInterface();
                if (ccSettings == null) ccSettings = new CcSettings(this);
                if (ccLaunchInput == null) ccLaunchInput = new CcLaunchInput();
                ccLaunchInput.setProductModelSelectionType(productsSelection);

                //CcDependencies ccDependencies = new CcDependencies(AppInfraSingleton.getInstance());
                CcDependencies ccDependencies = new CcDependencies(mAppInfraInterface);

                ccInterface.init(ccDependencies, ccSettings);
                mAppInfraInterface.getServiceDiscovery().getHomeCountry(new ServiceDiscoveryInterface.OnGetHomeCountryListener() {
                    @Override
                    public void onSuccess(String s, SOURCE source) {
                        if(s.equals("CN")) {
                            ccLaunchInput.setLiveChatUrl("http://ph-china.livecom.cn/webapp/index.html?app_openid=ph_6idvd4fj&token=PhilipsTest");
                        } else {
                            ccLaunchInput.setLiveChatUrl(null);
                        }
                        ccInterface.launch(activityLauncher, ccLaunchInput);
                    }

                    @Override
                    public void onError(ERRORVALUES errorvalues, String s) {
                        ccInterface.launch(activityLauncher, ccLaunchInput);
                    }
                });
                break;

            case R.id.launchAsFragment:

                mActivityButtonSelected = false;
                mFragmentButtonSelected = true;

                mLaunchDigitalCare.setVisibility(View.INVISIBLE);


                startActivity(new Intent(getApplicationContext(), MicroAppFragmentActivity.class));
                break;
            case R.id.change_theme:
                //Resources.Theme theme = super.getTheme();
                //theme.applyStyle(mThemeUtil.getNextTheme(), true);
                changeTheme();
                relaunchActivity();
                break;

        }
    }

    @Override
    public Resources.Theme getTheme() {
        Resources.Theme theme = super.getTheme();
        if(mThemeUtil ==null){
            mThemeUtil = new ThemeUtil(getApplicationContext().getSharedPreferences(
                    this.getString(R.string.app_name), Context.MODE_PRIVATE));
        }
        theme.applyStyle(mThemeUtil.getCurrentTheme(), true);
        return theme;
    }

    private void relaunchActivity() {
        Intent intent;
        int RESULT_CODE_THEME_UPDATED = 1;
        setResult(RESULT_CODE_THEME_UPDATED);
        intent = new Intent(this, MicroAppLauncher.class);
        startActivity(intent);
        finish();
    }


    protected  void initTheme(){
        UIDHelper.injectCalligraphyFonts();
        themeHelper = new ThemeHelper(this);
        ThemeConfiguration config = themeHelper.getThemeConfig();
        setTheme(themeHelper.getThemeResourceId());
        UIDHelper.init(config);
        FontIconTypefaceHolder.init(getAssets(),"digitalcarefonts/CCIcon.ttf");
    }

    protected void changeTheme(){
        themeHelper.changeTheme();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

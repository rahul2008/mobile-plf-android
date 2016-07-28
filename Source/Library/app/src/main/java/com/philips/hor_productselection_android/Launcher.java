package com.philips.hor_productselection_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.activity.ProductSelectionBaseActivity;
import com.philips.cdp.productselection.launchertype.ActivityLauncher;
import com.philips.cdp.productselection.listeners.ProductModelSelectionListener;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.hor_productselection_android.adapter.CtnListViewListener;
import com.philips.hor_productselection_android.adapter.SampleAdapter;
import com.philips.hor_productselection_android.adapter.SimpleItemTouchHelperCallback;
import com.philips.hor_productselection_android.view.CustomDialog;
import com.philips.hor_productselection_android.view.SampleActivitySelection;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraSingleton;
import com.philips.platform.appinfra.logging.AppInfraLogging;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher extends ProductSelectionBaseActivity implements View.OnClickListener {

    private static ArrayList<String> mList = null;
    private static int RESULT_CODE_THEME_UPDATED = 1;
    private static ConsumerProductInfo productInfo = null;
    private final String TAG = Launcher.class.getSimpleName();
    private ProductModelSelectionHelper mProductSelectionHelper = null;
    private Button mButtonActivity, mAdd = null;
    private Button mButtonFragment = null;
    private ImageButton mAddButton = null;
    private RecyclerView mRecyclerView = null;
    //    private ProductModelSelectionHelper mConfigManager = null;
    private SampleAdapter adapter = null;
    private Button change_theme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);


        AppInfraSingleton.setInstance(new AppInfra.Builder().build(getApplicationContext()));
        AppTaggingInterface aiAppTaggingInterface = ProductModelSelectionHelper.getInstance().getAPPInfraInstance().getTagging();
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);


        LoggingInterface appInfraLogging = ProductModelSelectionHelper.getInstance().getLoggerInterface();

        change_theme = (Button) findViewById(R.id.change_theme);
        mAddButton = (ImageButton) findViewById(R.id.addimageButton);
        change_theme.setOnClickListener(this);
        setViewState();
        if (mList == null)
            mList = new ArrayList<String>();
        initUIReferences();
        if (mList.size() == 0)
            addDummyData();

        if (adapter == null)
            adapter = new SampleAdapter(mList);
        adapter = setAdapter(mList);

        ItemTouchHelper.Callback callback =
                new SimpleItemTouchHelperCallback(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(mRecyclerView);


//        mConfigManager = ProductModelSelectionHelper.getInstance();
//        mConfigManager.initialize(this);
    }

    private void relaunchActivity() {
        Intent intent;
        setResult(RESULT_CODE_THEME_UPDATED);
        intent = new Intent(this, Launcher.class);
        startActivity(intent);
        finish();
    }

    @NonNull
    private SampleAdapter setAdapter(ArrayList<String> mList) {
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

    private void launchDialog() {
        CustomDialog dialog = new CustomDialog(this, mList, new CtnListViewListener() {
            @Override
            public void updateList(ArrayList<String> productList) {
                mList = productList;
                setAdapter(mList);
                Log.d(TAG, " Products Size = " + mList.size());
            }
        });
        dialog.show();
    }

    private void initUIReferences() {
        mButtonActivity = (Button) findViewById(R.id.buttonActivity);
        mButtonFragment = (Button) findViewById(R.id.buttonFragment);
        mAdd = (Button) findViewById(R.id.add_product);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mButtonActivity.setOnClickListener(this);
        mButtonFragment.setOnClickListener(this);
        mAdd.setOnClickListener(this);
        mAddButton.setOnClickListener(this);
    }

    private void addDummyData() {

        List<String> mCtnList = Arrays.asList(getResources().getStringArray(R.array.ctn_list));


        for (int i = 0; i < mCtnList.size(); i++) {
            mList.add(mCtnList.get(i));
        }
    }

    private void setViewState() {
//        String preferences = themeUtils.getThemePreferences();
//        ArrayList<String> prefData = themeUtils.getThemeTokens(preferences);
//        themeUtils.setColorString(prefData.get(0));
    }

    @Override
    public void onClick(View v) {
        //setCurrentOrientation API is requiered in order to achieve proper GUI on tablet.
        Configuration configuration = getResources().getConfiguration();
        ProductModelSelectionHelper.getInstance().setCurrentOrientation(configuration);

        switch (v.getId()) {
            case R.id.buttonActivity:
                launchProductSelectionAsActivity();
                //ProductModelSelectionHelper.getInstance().initializeTagging(true, "ProductSelection", "101", "vertical:productSelection:home");
                break;

            case R.id.buttonFragment:
                launchProductSelectionAsFragment();
              //  ProductModelSelectionHelper.getInstance().initializeTagging(true, "ProductSelection", "101", "vertical:productSelection:home");
                break;

            case R.id.addimageButton:
                launchDialog();
                break;

            case R.id.change_theme:
//                String preferences = null;
//                int themeValue = (int) (Math.random() * (4 - 0)) + 0;
//                switch (themeValue) {
//                    case 0:
//                        getUiKitThemeUtil().setThemePreferences(false);
//                        break;
//                    case 1:
//                        preferences = "blue|false|solid|0";
//                        getUiKitThemeUtil().setThemePreferences(preferences);
//                        break;
//                    case 2:
//                        preferences = "orange|false|solid|0";
//                        getUiKitThemeUtil().setThemePreferences(preferences);
//                        break;
//
//                    case 3:
//                        preferences = "aqua|false|solid|0";
//                        getUiKitThemeUtil().setThemePreferences(preferences);
//                        break;
//                }
//
//                relaunchActivity();
//                break;
        }
    }

    private void launchProductSelectionAsActivity() {

        String[] ctnList = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            ctnList[i] = mList.get(i);
        }

        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(Catalog.CARE);
        productsSelection.setSector(Sector.B2C);

        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(this);
        mProductSelectionHelper.setLocale("en", "GB");

        ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                R.style.Theme_Philips_BrightBlue_Gradient_WhiteBackground);
        uiLauncher.setAnimation(R.anim.abc_fade_in, R.anim.abc_fade_out);
       /* ProductModelSelectionHelper.getInstance().setSummaryDataListener(new SummaryDataListener() {
            @Override
            public void onSuccess(List<SummaryModel> summaryModels) {

                if (summaryModels != null)
                    Toast.makeText(Launcher.this, "Summary Size : " + summaryModels.size(), Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(Launcher.this, "Summary returned null", Toast.LENGTH_SHORT).show();

            }
        });*/
        ProductModelSelectionHelper.getInstance().setProductSelectionListener(new ProductSelectionListener() {
            @Override
            public void onProductModelSelected(SummaryModel productSummaryModel) {
                if (productSummaryModel != null) {
                    SummaryModel summaryModel = productSummaryModel;
                    productInfo.setCtn(summaryModel.getData().getCtn());
                    Toast.makeText(Launcher.this, "Selected ProductName is : " + summaryModel.getData().getProductTitle(),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        ProductModelSelectionHelper.getInstance().invokeProductSelection(uiLauncher, productsSelection);
        ProductSelectionLogger.enableLogging();

    }


    private void launchProductSelectionAsFragment() {
        startActivity(new Intent(this, SampleActivitySelection.class));
    }
}

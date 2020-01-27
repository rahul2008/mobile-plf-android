package com.philips.hor_productselection_android;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.ItemTouchHelper;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.productselection.ProductModelSelectionHelper;
import com.philips.cdp.productselection.activity.ProductSelectionBaseActivity;
import com.philips.cdp.productselection.listeners.ProductSelectionListener;
import com.philips.cdp.productselection.productselectiontype.HardcodedProductList;
import com.philips.cdp.productselection.productselectiontype.ProductModelSelectionType;
import com.philips.cdp.productselection.utils.ProductSelectionLogger;
import com.philips.cdp.prxclient.PrxConstants;
import com.philips.cdp.prxclient.datamodels.summary.SummaryModel;
import com.philips.hor_productselection_android.adapter.CtnListViewListener;
import com.philips.hor_productselection_android.adapter.SampleAdapter;
import com.philips.hor_productselection_android.adapter.SimpleItemTouchHelperCallback;
import com.philips.hor_productselection_android.util.ThemeHelper;
import com.philips.hor_productselection_android.view.CustomDialog;
import com.philips.hor_productselection_android.view.SampleActivitySelection;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.tagging.AppTaggingInterface;
import com.philips.platform.uappframework.launcher.ActivityLauncher;
import com.philips.platform.uid.thememanager.AccentRange;
import com.philips.platform.uid.thememanager.ColorRange;
import com.philips.platform.uid.thememanager.ContentColor;
import com.philips.platform.uid.thememanager.NavigationColor;
import com.philips.platform.uid.thememanager.ThemeConfiguration;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.ImageButton;
import com.shamanland.fonticon.FontIconTypefaceHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher extends ProductSelectionBaseActivity implements View.OnClickListener {

    private static ArrayList<String> mList = null;
    private static ConsumerProductInfo productInfo = null;
    private final String TAG = Launcher.class.getSimpleName();
    private ProductModelSelectionHelper mProductSelectionHelper = null;
    private Button mButtonActivity, mAdd = null;
    private Button mButtonFragment = null;
    private ImageButton mAddButton = null;
    private RecyclerView mRecyclerView = null;
    private SampleAdapter adapter = null;
    private Button change_theme = null;
    private AppInfraInterface mAppInfraInterface;
    private static int DLS_THEME;
    private ThemeHelper themeHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        initAppInfra();
        initTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        AppTaggingInterface aiAppTaggingInterface = ProductModelSelectionHelper.getInstance().getAPPInfraInstance().getTagging();
        aiAppTaggingInterface.setPreviousPage("demoapp:home");
        aiAppTaggingInterface.setPrivacyConsent(AppTaggingInterface.PrivacyStatus.OPTIN);
        change_theme = (Button) findViewById(R.id.change_theme);
        mAddButton = (ImageButton) findViewById(R.id.addimageButton);
        change_theme.setOnClickListener(this);
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
    }

    private void initAppInfra() {
        mAppInfraInterface = new AppInfra.Builder().build(getApplicationContext());
        mProductSelectionHelper = ProductModelSelectionHelper.getInstance();
        mProductSelectionHelper.initialize(this, mAppInfraInterface);
        mAppInfraInterface.getServiceDiscovery().setHomeCountry("GB");
    }

    private void relaunchActivity() {
        Intent intent;
        int RESULT_CODE_THEME_UPDATED = 1;
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
                ProductSelectionLogger.d(TAG, " Products Size = " + mList.size());
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

    @Override
    public void onClick(View v) {
        Configuration configuration = getResources().getConfiguration();
        ProductModelSelectionHelper.getInstance().setCurrentOrientation(configuration);

        switch (v.getId()) {
            case R.id.buttonActivity:
                launchProductSelectionAsActivity();
                break;

            case R.id.buttonFragment:
                launchProductSelectionAsFragment();
                break;

            case R.id.addimageButton:
                launchDialog();
                break;

            case R.id.change_theme:
                changeTheme();
                relaunchActivity();
                break;
        }
    }

    private void launchProductSelectionAsActivity() {

        String[] ctnList = new String[mList.size()];
        for (int i = 0; i < mList.size(); i++) {
            ctnList[i] = mList.get(i);
        }

        ProductModelSelectionType productsSelection = new HardcodedProductList(ctnList);
        productsSelection.setCatalog(PrxConstants.Catalog.CARE);
        productsSelection.setSector(PrxConstants.Sector.B2C);

        ActivityLauncher uiLauncher = new ActivityLauncher(ActivityLauncher.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED,
                getDlsThemeConfiguration(),themeHelper.getThemeResourceId(), null);

        //mProductSelectionHelper.setLocale("en", "IN");
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
    }


    private void launchProductSelectionAsFragment() {
        startActivity(new Intent(this, SampleActivitySelection.class));
    }

    protected void initTheme() {
        ThemeConfiguration config = getDlsThemeConfiguration();
        themeHelper = new ThemeHelper(this);
        setTheme(themeHelper.getThemeResourceId());
        UIDHelper.init(config);
        FontIconTypefaceHolder.init(getAssets(), "fonts/iconfont.ttf");
    }

    protected void changeTheme(){
        themeHelper.changeTheme();
    }

    protected ThemeConfiguration getDlsThemeConfiguration() {
        return new ThemeConfiguration(this, ColorRange.GROUP_BLUE, NavigationColor.BRIGHT, ContentColor.VERY_DARK, AccentRange.ORANGE);
    }
}

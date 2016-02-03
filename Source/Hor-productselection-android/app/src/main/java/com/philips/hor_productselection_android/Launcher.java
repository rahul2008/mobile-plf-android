package com.philips.hor_productselection_android;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;
import com.philips.hor_productselection_android.adapter.SampleAdapter;
import com.philips.hor_productselection_android.adapter.SimpleItemTouchHelperCallback;
import com.philips.hor_productselection_android.view.CustomDialog;
import com.philips.hor_productselection_android.view.SampleActivity;
import com.philips.multiproduct.MultiProductConfigManager;
import com.philips.multiproduct.activity.MultiProductBaseActivity;
import com.philips.multiproduct.listeners.ActionbarUpdateListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Launcher extends MultiProductBaseActivity implements View.OnClickListener {

    private static ArrayList<Product> mList = null;
    private static int RESULT_CODE_THEME_UPDATED = 1;
    private final String TAG = Launcher.class.getSimpleName();
    private Button mButtonActivity, mAdd = null;
    private Button mButtonFragment = null;
    private RecyclerView mRecyclerView = null;
    private MultiProductConfigManager mConfigManager = null;
    private SampleAdapter adapter = null;
    private Button change_theme = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        if (themeUtils == null) {
            themeUtils = new ThemeUtils(this.getSharedPreferences(
                    this.getString(R.string.app_name_multiproduct), Context.MODE_PRIVATE));
        }
        change_theme = (Button) findViewById(R.id.change_theme);
        change_theme.setOnClickListener(this);
        setViewState();
        if (mList == null)
            mList = new ArrayList<Product>();
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


        mConfigManager = MultiProductConfigManager.getInstance();
        mConfigManager.initializeDigitalCareLibrary(this);
    }

    private void relaunchActivity() {
        Intent intent;
        setResult(RESULT_CODE_THEME_UPDATED);
        intent = new Intent(this, Launcher.class);
        startActivity(intent);
        finish();
    }

    @NonNull
    private SampleAdapter setAdapter(ArrayList<Product> mList) {
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        return adapter;
    }

    private void launchDialog() {
        CustomDialog dialog = new CustomDialog(this, mList, new Listener() {
            @Override
            public void updateList(ArrayList<Product> productList) {
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
    }


    private void addDummyData() {

        List<String> mCtnList = Arrays.asList(getResources().getStringArray(R.array.ctn_list));


        for (int i = 0; i < mCtnList.size(); i++) {
            Product product = new Product();
            //  product.setmCtn((new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "/dummy");
            product.setmCtn(mCtnList.get(i));
            product.setmCatalog("B2C");
            product.setmCategory("CARE");
            mList.add(product);
        }
    }

    private void setViewState() {
//        String preferences = themeUtils.getThemePreferences();
//        ArrayList<String> prefData = themeUtils.getThemeTokens(preferences);
//        themeUtils.setColorString(prefData.get(0));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttonActivity:
                Toast.makeText(this, "Launch as Activity ", Toast.LENGTH_LONG).show();
                launchMultiProductAsActivity();
                break;

            case R.id.buttonFragment:
                Toast.makeText(this, "Launch as Fragment. Actionbar is not UI_Kit enabled. ", Toast.LENGTH_LONG).show();
                launchMultiProductAsFragment();
                break;

            case R.id.add_product:
                launchDialog();
                break;

            case R.id.change_theme:
                String preferences = null;
                int themeValue = (int) (Math.random() * (4 - 0)) + 0;
                switch (themeValue) {
                    case 0:
                        themeUtils.setThemePreferences(false);
                        break;
                    case 1:
                        preferences = "blue|false|solid|0";
                        themeUtils.setThemePreferences(preferences);
                        break;
                    case 2:
                        preferences = "orange|false|solid|0";
                        themeUtils.setThemePreferences(preferences);
                        break;

                    case 3:
                        preferences = "aqua|false|solid|0";
                        themeUtils.setThemePreferences(preferences);
                        break;
                }

                relaunchActivity();
                break;
        }
    }


    private void launchMultiProductAsActivity() {
        mConfigManager.setLocale("en", "GB");
        mConfigManager.invokeDigitalCareAsActivity(R.anim.abc_fade_in, R.anim.abc_fade_out, MultiProductConfigManager.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);
        mConfigManager.setMultiProductSize(mList.size());
    }

    private void launchMultiProductAsFragment() {
        startActivity(new Intent(this, SampleActivity.class));
    }
}

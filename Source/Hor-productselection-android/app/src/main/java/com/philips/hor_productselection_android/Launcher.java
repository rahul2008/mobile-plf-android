package com.philips.hor_productselection_android;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.philips.cdp.uikit.UiKitActivity;
import com.philips.hor_productselection_android.adapter.SampleAdapter;
import com.philips.hor_productselection_android.adapter.SimpleItemTouchHelperCallback;
import com.philips.hor_productselection_android.view.CustomDialog;

import java.util.ArrayList;
import java.util.Random;

import com.philips.multiproduct.MultiProductConfigManager;

public class Launcher extends UiKitActivity implements View.OnClickListener {

    private final String TAG = Launcher.class.getSimpleName();
    private Button mButton, mAdd = null;
    private static ArrayList<Product> mList = null;
    private RecyclerView mRecyclerView = null;
    private MultiProductConfigManager mConfigManager = null;
    SampleAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
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
        mButton = (Button) findViewById(R.id.activitybutton);
        mAdd = (Button) findViewById(R.id.add_product);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);

        mButton.setOnClickListener(this);
        mAdd.setOnClickListener(this);
    }


    private void addDummyData() {
        for (int i = 0; i < 2; i++) {
            Product product = new Product();
            product.setmCtn((new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "" + (new Random().nextInt(9)) + "/dummy");
            product.setmCatalog("B2C");
            product.setmCategory("CARE");
            mList.add(product);
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activitybutton:
                launchMultiProductModule();

                break;
            case R.id.add_product:
                launchDialog();
                break;
        }
    }


    private void launchMultiProductModule() {
        mConfigManager.setLocale("en", "GB");
        mConfigManager.invokeDigitalCareAsActivity(R.anim.abc_fade_in, R.anim.abc_fade_out, MultiProductConfigManager.ActivityOrientation.SCREEN_ORIENTATION_UNSPECIFIED);
    }
}

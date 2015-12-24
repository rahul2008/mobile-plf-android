package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;


public class CardActivity extends CatalogActivity {

    CustomRecyclerView recyclerView;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        handleOrientationView();
    }

    private void handleOrientationView() {
        recyclerView = (CustomRecyclerView) findViewById(R.id.recyclerView);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();

        int orientation = conf.orientation;

        LinearLayoutManager llm = new LinearLayoutManager(this);

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

}

package com.philips.cdp.ui.catalog.activity;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;
import android.view.WindowManager;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.cards.CardAdapter;

public class CardsActivity extends CatalogActivity {

    RecyclerView recyclerView;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        recyclerView = (SnappyRecyclerView) findViewById(R.id.recyclerView);

        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();

        int orientation = display.getRotation();
        LinearLayoutManager llm = new LinearLayoutManager(this);

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                llm.setOrientation(LinearLayoutManager.HORIZONTAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
            default:
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(llm);
                adapter = new CardAdapter(this);
                recyclerView.setAdapter(adapter);
                break;
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.HORIZONTAL);
            recyclerView.setLayoutManager(llm);
            adapter = new CardAdapter(this);
            recyclerView.setAdapter(adapter);
        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            LinearLayoutManager llm = new LinearLayoutManager(this);
            llm.setOrientation(LinearLayoutManager.VERTICAL);
            recyclerView.setLayoutManager(llm);
            adapter = new CardAdapter(this);
            recyclerView.setAdapter(adapter);
        }
    }
}
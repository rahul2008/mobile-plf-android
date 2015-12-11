package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.cards.CardAdapter;

public class CardsActivity extends CatalogActivity {

    RecyclerView recyclerView;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(llm);
        recyclerView.setHasFixedSize(true);
        adapter = new CardAdapter(this);
        recyclerView.setAdapter(adapter);

    }

}

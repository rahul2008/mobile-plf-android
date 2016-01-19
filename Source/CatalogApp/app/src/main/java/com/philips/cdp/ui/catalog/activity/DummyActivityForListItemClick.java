package com.philips.cdp.ui.catalog.activity;

import android.os.Bundle;

import com.philips.cdp.ui.catalog.R;

public class DummyActivityForListItemClick extends CatalogActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.uikit_listview_without_icons);
    }

}

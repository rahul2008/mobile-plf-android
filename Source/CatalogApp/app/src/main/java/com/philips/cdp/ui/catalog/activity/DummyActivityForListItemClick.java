package com.philips.cdp.ui.catalog.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.SplashLauncher;
import com.philips.cdp.ui.catalog.themeutils.ThemeUtils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class DummyActivityForListItemClick extends CatalogActivity  {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dummy);
    }

}

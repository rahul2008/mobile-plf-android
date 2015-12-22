package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.ui.catalog.activity.CatalogActivity;


public class CardActivity extends CatalogActivity {

    RecyclerView recyclerView;
    CardAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cards);
        handleOrientationView();
    }

    private void handleOrientationView() {
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        Resources res = getResources();
        Configuration conf = res.getConfiguration();

        int orientation = conf.orientation;

        LinearLayoutManager llm = new LinearLayoutManager(this);

        switch (orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                if (recyclerView != null) {
                    llm.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(llm);
                    adapter = new CardAdapter(this);
                    recyclerView.setAdapter(adapter);
                }
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                CardPagerAdapter cardPagerAdapter = new CardPagerAdapter(getSupportFragmentManager());
                ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
                viewPager.setAdapter(cardPagerAdapter);
                break;
        }
    }

    public class CardPagerAdapter extends FragmentPagerAdapter {

        public CardPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            return new CardContainerFragment();
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}

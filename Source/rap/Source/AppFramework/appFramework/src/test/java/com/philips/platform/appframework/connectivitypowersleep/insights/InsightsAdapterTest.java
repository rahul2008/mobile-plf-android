/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.connectivitypowersleep.insights;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.connectivitypowersleep.datamodels.MomentInsight;
import com.philips.platform.core.datatypes.Insight;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by philips on 9/5/17.
 */

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class InsightsAdapterTest {

    private InsightsAdapter insightsAdapter;
    private Context context;
    private InsightsAdapter.InsightsInfoViewHolder holder;
    private View listItemView;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        initializeTipsList();
        insightsAdapter = new InsightsAdapter(context, insightList);
        insightsAdapter.onAttachedToRecyclerView(new RecyclerView(context));
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        listItemView = inflater.inflate(R.layout.fragment_insights_item, null, false);
        holder = insightsAdapter.new InsightsInfoViewHolder(listItemView);
        insightsAdapter.onBindViewHolder(holder, 0);
    }

    @Test
    public void testListItemDataAreValid() {
        assertEquals("HIGH_DEEP_SLEEP", holder.tvTitle.getText().toString());
    }

    @Test
    public void testGetItemCountWithNullList() {
        insightsAdapter = new InsightsAdapter(context, null);
        assertEquals(0, insightsAdapter.getItemCount());
    }
    @After
    public void tearDown() {
        insightsAdapter = null;
        context = null;
        holder = null;
        listItemView = null;
    }
    private List<Insight> insightList;

    public void initializeTipsList() {

        insightList = new ArrayList<Insight>();

        Insight insight = new MomentInsight();
        insight.setTitle("HIGH_DEEP_SLEEP");

        insightList.add(insight);
    }


}

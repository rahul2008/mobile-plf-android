/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */
package com.philips.platform.baseapp.screens.cocoversion;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class CoCoVersionAdapterTest {

    private CocoVersionAdapter cocoVersionAdapter;
    private Context context;
    private CocoVersionAdapter.CocoInfoViewHolder holder;
    private View listItemView;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        cocoVersionAdapter = new CocoVersionAdapter(context, dummyList());
        cocoVersionAdapter.onAttachedToRecyclerView(new RecyclerView(context));
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        listItemView = inflater.inflate(R.layout.coco_version_listitem, null, false);
        holder = cocoVersionAdapter.new CocoInfoViewHolder(listItemView);
        cocoVersionAdapter.onBindViewHolder(holder, 0);
    }

    @Test
    public void testListItemDataAreValid() {
        assertEquals(holder.CocoName.getText().toString(), context.getString(R.string.RA_COCO_AppInfra));
        assertEquals(holder.cocoDescription.getText().toString(), context.getString(R.string.RA_COCO_AppInfra_desc));
        assertEquals(holder.CocoVersion.getText().toString(), com.philips.platform.appinfra.BuildConfig.VERSION_NAME);
    }

    @Test
    public void testDescriptionVisibilityWhenClickedOnce() throws InterruptedException {
        listItemView.performClick();
        cocoVersionAdapter.onBindViewHolder(holder, 0);
        assertTrue(holder.cocoDescription.getVisibility() == View.VISIBLE);
    }

    @Test
    public void testDescriptionVisibilityWhenClickedTwice() throws InterruptedException {
        listItemView.performClick();
        cocoVersionAdapter.onBindViewHolder(holder, 0);
        assertTrue(holder.cocoDescription.getVisibility() == View.VISIBLE);
        listItemView.performClick();
        cocoVersionAdapter.onBindViewHolder(holder, 0);
        assertFalse(holder.cocoDescription.getVisibility() == View.VISIBLE);
    }

    private ArrayList<CocoVersionItem> dummyList() {
        CocoVersionItem ai = new CocoVersionItem();
        ai.setTitle(context.getString(R.string.RA_COCO_AppInfra));
        ai.setDescription(context.getString(R.string.RA_COCO_AppInfra_desc));
        ai.setVersion(com.philips.platform.appinfra.BuildConfig.VERSION_NAME);

        ArrayList<CocoVersionItem> itemArrayList = new ArrayList<CocoVersionItem>();
        itemArrayList.add(ai);

        return itemArrayList;
    }
}

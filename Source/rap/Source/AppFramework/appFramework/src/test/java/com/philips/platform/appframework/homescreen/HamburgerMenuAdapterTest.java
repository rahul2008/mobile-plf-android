/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.homescreen;

import android.content.Context;
import android.content.res.TypedArray;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.models.HamburgerMenuItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(RobolectricTestRunner.class)
public class HamburgerMenuAdapterTest {

    private HamburgerMenuAdapter hamburgerMenuAdapter;
    private Context context;
    private HamburgerMenuAdapter.HamburgerMenuViewHolder holder;
    private View listItemView;
    private ArrayList<HamburgerMenuItem> hamburgerMenuItems;

    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();

    @Mock
    private HamburgerMenuItemClickListener hamburgerMenuItemClickListener;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        hamburgerMenuItems = getHamburgerMenuList();
        hamburgerMenuAdapter = new HamburgerMenuAdapter(hamburgerMenuItems);
        hamburgerMenuAdapter.onAttachedToRecyclerView(new RecyclerView(context));
        hamburgerMenuAdapter.setMenuItemClickListener(hamburgerMenuItemClickListener);
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        listItemView = inflater.inflate(R.layout.rap_hamburger_menu_item, null, false);
        holder = hamburgerMenuAdapter.new HamburgerMenuViewHolder(listItemView);
        hamburgerMenuAdapter.onBindViewHolder(holder, 0);
    }

    @Test
    public void testListItemDataAreValid() {
//        assertEquals(holder.menuTitle.getText().toString(), context.getString(R.string.RA_HomeTab_Menu_Title));
        assertNotNull(holder.menuIcon.getDrawable());
    }

    @Test
    public void testListItemClick() {
        listItemView.performClick();
        verify(hamburgerMenuItemClickListener, times(1)).onMenuItemClicked(0);
    }

    @Test
    public void testListItemClickWhenListenerNotSet() {
        hamburgerMenuAdapter.removeMenuItemClickListener();
        listItemView.performClick();
        verify(hamburgerMenuItemClickListener, times(0)).onMenuItemClicked(0);
    }

    @Test
    public void onCreateViewHolderTest() {
        RecyclerView.ViewHolder viewHolder = hamburgerMenuAdapter
                .onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
        assertNotNull(viewHolder);
        assertTrue(viewHolder instanceof HamburgerMenuAdapter.HamburgerMenuViewHolder);
    }

    @Test
    public void getItemCountTest() {
        assertEquals(hamburgerMenuItems.size(), hamburgerMenuAdapter.getItemCount());
    }

    private ArrayList<HamburgerMenuItem> getHamburgerMenuList() {

        String[] hamburgerMenuTitles = context.getResources().getStringArray(R.array.hamburger_drawer_items);
        ArrayList<HamburgerMenuItem> hamburgerMenuItems = new ArrayList<HamburgerMenuItem>();
        TypedArray typedArray = context.getResources().obtainTypedArray(R.array.hamburger_drawer_items_res);
        for (int i = 0; i < hamburgerMenuTitles.length; i++) {
            hamburgerMenuItems.add(new HamburgerMenuItem(typedArray.getResourceId(i, R.drawable.rap_question_mark), hamburgerMenuTitles[i], context));
        }
        return hamburgerMenuItems;
    }


    @After
    public void tearDown() {
        context = null;
        hamburgerMenuItemClickListener = null;
        hamburgerMenuAdapter = null;
        holder = null;
        listItemView = null;
    }
}
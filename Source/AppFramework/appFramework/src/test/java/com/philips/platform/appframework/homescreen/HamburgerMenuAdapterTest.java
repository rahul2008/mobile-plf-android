/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.CustomRobolectricRunner;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.models.HamburgerMenuItem;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;


@RunWith(CustomRobolectricRunner.class)
//@Config(application = TestAppFrameworkApplication.class)
public class HamburgerMenuAdapterTest {

    private HamburgerMenuAdapter hamburgerMenuAdapter;
    private Context context;
    private HamburgerMenuAdapter.HamburgerMenuViewHolder holder;
    private View listItemView;

    @Before
    public void setUp() {
        context = RuntimeEnvironment.application;
        hamburgerMenuAdapter = new HamburgerMenuAdapter(getHamburgerMenuList());
        hamburgerMenuAdapter.onAttachedToRecyclerView(new RecyclerView(context));
        LayoutInflater inflater = (LayoutInflater) RuntimeEnvironment.application.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //We have a layout especially for the items in our recycler view. We will see it in a moment.
        listItemView = inflater.inflate(R.layout.rap_hamburger_menu_item, null, false);
        holder = hamburgerMenuAdapter.new HamburgerMenuViewHolder(listItemView);
        hamburgerMenuAdapter.onBindViewHolder(holder, 0);
    }

    @Test
    public void testListItemDataAreValid() {
        assertEquals(holder.menuTitle.getText().toString(), context.getString(R.string.RA_DLS_HomeScreen_Title));
        assertNotNull(holder.menuIcon.getDrawable());
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
}
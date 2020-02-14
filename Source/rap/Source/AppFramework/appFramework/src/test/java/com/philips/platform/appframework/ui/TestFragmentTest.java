/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appframework.ui;


import android.os.Handler;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.FrameLayout;

import com.philips.platform.TestActivity;
import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.data.TestConfigManager;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.models.Chapter;
import com.philips.platform.appframework.models.CommonComponent;
import com.philips.platform.baseapp.screens.cocoversion.CocoVersionFragment;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(application = TestAppFrameworkApplication.class)
public class TestFragmentTest extends TestCase implements TestConfigManager.TestConfigCallback{
    private HamburgerActivity hamburgerActivity = null;
    private TestFragment testFragment;
    private ArrayList<Chapter> chapterArrayList;
    private TestConfigManager testConfigManager;
    private ActivityController<TestActivity> activityController;
    @After
    public void tearDown(){
        testFragment=null;
        activityController.pause().stop().destroy();
    }
    @Before
    public void setUp() throws Exception{
        super.setUp();
        setUpChapterList();

    }

    protected void setUpChapterList() {
        testConfigManager = TestConfigManager.getInstance();
        activityController= Robolectric.buildActivity(TestActivity.class);
        hamburgerActivity=activityController.create().start().get();
        testFragment = new TestFragment();


    }

    @Test
    public void testTestFragment(){

        assertNotNull(testFragment);
    }

    @Test
    public void testDisplayChapterList(){
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
//        SupportFragmentTestUtil.startFragment(testFragment);
        testFragment.displayChapterList(chapterArrayList);
        RecyclerView recyclerView = (RecyclerView) testFragment.getView().findViewById(R.id.chapter_recyclerview);
        ChapterAdapter chapterAdapter = (ChapterAdapter) recyclerView.getAdapter();
        assertEquals(2,chapterAdapter.getItemCount());
    }

    @Test
    public void testShowCoCoList(){
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();

        fragmentManager.beginTransaction().add(testFragment,"TestCoCoListFragment").commit();
        testFragment.showCoCoList(createChapterObject("Connectivity", "Blue Lib"));
        Fragment fragment = fragmentManager.findFragmentByTag(COCOListFragment.class.getSimpleName());
        assertTrue(fragment instanceof COCOListFragment);
    }

    @Test
    public void testShowCoCoListForAppInfo(){
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        fragmentManager.beginTransaction().add(testFragment,"TestCoCoListFragment").commit();
        testFragment.showCoCoList(createChapterObject("App Info", null));
        Fragment fragment = fragmentManager.findFragmentByTag(CocoVersionFragment.class.getSimpleName());
        assertTrue(fragment instanceof CocoVersionFragment);
    }

    @Test
    public void testCoCoName() {
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
//        SupportFragmentTestUtil.startFragment(testFragment);
        testFragment.displayChapterList(chapterArrayList);
        RecyclerView recyclerView = (RecyclerView) testFragment.getView().findViewById(R.id.chapter_recyclerview);
        ChapterAdapter chapterAdapter = (ChapterAdapter) recyclerView.getAdapter();
        ChapterAdapter.ChapterViewHolder viewHolder = chapterAdapter.onCreateViewHolder(new FrameLayout(RuntimeEnvironment.application), 0);
        chapterAdapter.onBindViewHolder(viewHolder,0);
        assertEquals("Chapter Mobile",viewHolder.chapterTextView.getText().toString());
    }

    protected static  Chapter createChapterObject(String chapterName, String cocoName) {
        CommonComponent commonComponent = new CommonComponent();
        commonComponent.setCocoName(cocoName);
        ArrayList<CommonComponent> arrayListCommonComponent = new ArrayList<>();
        arrayListCommonComponent.add(commonComponent);
        Chapter chapter = new Chapter();
        chapter.setCommonComponentsList(arrayListCommonComponent);
        chapter.setChapterName(chapterName);
        return chapter;
    }

    @Override
    public void onChaptersLoaded(ArrayList<Chapter> chaptersList) {
        chapterArrayList = chaptersList;
    }

    @Override
    public void onCOCOLoaded(ArrayList<CommonComponent> commonComponentsList) {

    }

    @Override
    public void onCOCOLoadError() {

    }
}

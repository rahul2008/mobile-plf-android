package com.philips.platform.appframework.testmicroappfw.ui;


import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;

import com.philips.platform.TestAppFrameworkApplication;
import com.philips.platform.appframework.BuildConfig;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HamburgerActivity;
import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.support.v4.SupportFragmentTestUtil;

import java.util.ArrayList;

@RunWith(RobolectricTestRunner.class)
@Config(manifest=Config.NONE, constants = BuildConfig.class, application = TestAppFrameworkApplication.class, sdk = 24)
public class TestFragmentTest extends TestCase implements TestConfigManager.TestConfigCallback{
    private HamburgerActivity hamburgerActivity = null;
    private TestFragment testFragment;
    private ArrayList<Chapter> chapterArrayList;
    private TestConfigManager testConfigManager;

    @Before
    public void setUp() throws Exception{
        super.setUp();
        setUpChapterList();

    }

    protected void setUpChapterList() {
        testConfigManager = TestConfigManager.getInstance();
        hamburgerActivity = Robolectric.buildActivity(HamburgerActivity.class).create().start().get();
        testFragment = new TestFragment();


    }

    @Test
    public void testTestFragment(){

        assertNotNull(testFragment);
    }

    @Test
    public void testDisplayChapterList(){
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
        SupportFragmentTestUtil.startFragment(testFragment);
        testFragment.displayChapterList(chapterArrayList);
        RecyclerView recyclerView = (RecyclerView) testFragment.getView().findViewById(R.id.chapter_recyclerview);
        ChapterAdapter chapterAdapter = (ChapterAdapter) recyclerView.getAdapter();
        assertEquals(3,chapterAdapter.getItemCount());
    }

    @Test
    public void testShowCoCoList(){
        testConfigManager.loadChapterList(hamburgerActivity,new Handler(),this);
        FragmentManager fragmentManager = hamburgerActivity.getSupportFragmentManager();
        int fragmentCount = fragmentManager.getBackStackEntryCount();
        fragmentManager.beginTransaction().add(testFragment,"TestFragment").commit();
        testFragment.showCoCoList(createChapterObject());
        assertEquals(1,fragmentCount);
    }

    private Chapter createChapterObject() {
        CommonComponent commonComponent = new CommonComponent();
        commonComponent.setCocoName("Blue Lib");
        ArrayList<CommonComponent> arrayListCommonComponent = new ArrayList<>();
        arrayListCommonComponent.add(commonComponent);
        Chapter chapter = new Chapter();
        chapter.setCommonComponentsList(arrayListCommonComponent);
        chapter.setChapterName("Connectivity");
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

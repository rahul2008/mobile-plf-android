package com.philips.platform.appframework.testmicroappfw.ui;

import android.content.Context;
import android.os.Handler;

import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class TestPresenter implements TestContract.UserActionsListener {

    private TestContract.View testView;

    private Context context;

    private TestConfigManager testConfigManager;

    public TestPresenter(TestContract.View view, TestConfigManager testConfigManager,Context context) {
        testView = view;
        this.context = context;
        this.testConfigManager=testConfigManager;

    }

    @Override
    public void loadChapterList() {
        testConfigManager.loadChapterList(context,new Handler(),new TestConfigManager.TestConfigCallback() {
            @Override
            public void onChaptersLoaded(ArrayList<Chapter> chaptersList) {
                testView.displayChapterList(chaptersList);
            }

            @Override
            public void onCOCOLoaded(ArrayList<CommonComponent> commonComponentsList) {

            }

            @Override
            public void onCOCOLoadError() {

            }
        });
    }

}

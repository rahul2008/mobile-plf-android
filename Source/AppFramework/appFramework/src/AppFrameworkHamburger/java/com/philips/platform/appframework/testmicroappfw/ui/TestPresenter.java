package com.philips.platform.appframework.testmicroappfw.ui;

import android.content.Context;
import android.os.Handler;

import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;

/**
 * Created by philips on 10/02/17.
 */

public class TestPresenter implements TestContract.UserActionsListener {
    public static final String TAG=TestPresenter.class.getSimpleName();

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
        RALog.d(TAG, " Load chapter List");
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

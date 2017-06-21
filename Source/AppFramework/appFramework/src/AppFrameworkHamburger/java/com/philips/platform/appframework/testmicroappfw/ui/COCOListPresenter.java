package com.philips.platform.appframework.testmicroappfw.ui;

import android.content.Context;

import com.philips.platform.appframework.testmicroappfw.data.TestConfigManager;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.util.ArrayList;

/**
 * Created by philips on 13/02/17.
 */

public class COCOListPresenter implements COCOListContract.UserActionsListener{
    public static final String TAG=COCOListPresenter.class.getSimpleName();

    private COCOListContract.View view;

    private Context context;

    private TestConfigManager testConfigManager;

    public COCOListPresenter(COCOListContract.View view, TestConfigManager testConfigManager, Context context) {
        this.view = view;
        this.context = context;
        this.testConfigManager=testConfigManager;
    }


    @Override
    public void loadCoCoList(Chapter chapter) {
        RALog.d(TAG, " Load Coco list ");
        testConfigManager.getCoCoList(chapter, new TestConfigManager.TestConfigCallback() {
            @Override
            public void onChaptersLoaded(ArrayList<Chapter> chaptersList) {

            }

            @Override
            public void onCOCOLoaded(ArrayList<CommonComponent> commonComponentsList) {
                view.displayCoCoList(commonComponentsList);
            }

            @Override
            public void onCOCOLoadError() {

            }
        });
    }
}

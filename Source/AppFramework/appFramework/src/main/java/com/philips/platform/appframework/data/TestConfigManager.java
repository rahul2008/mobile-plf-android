/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.data;

import android.content.Context;
import android.os.Handler;

import com.google.gson.GsonBuilder;
import com.philips.platform.appframework.models.Chapter;
import com.philips.platform.appframework.models.CommonComponent;
import com.philips.platform.appframework.models.TestFwConfig;
import com.philips.platform.baseapp.screens.utility.RALog;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import static com.philips.platform.baseapp.screens.utility.Constants.FILE_IO;

/**
 * Created by philips on 13/02/17.
 */

public class TestConfigManager {

    private static TestConfigManager testConfigManager;
    public static final String TAG = TestConfigManager.class.getSimpleName();

    private TestFwConfig testFwConfig;

    private TestConfigManager(){

    }
    public interface TestConfigCallback{
        void onChaptersLoaded(ArrayList<Chapter> chaptersList);

        void onCOCOLoaded(ArrayList<CommonComponent> commonComponentsList);

        void onCOCOLoadError();
    }

    public static TestConfigManager getInstance(){
        if(testConfigManager==null){
            testConfigManager=new TestConfigManager();
        }
        return testConfigManager;

    }

    public void loadChapterList(Context context, Handler handler, TestConfigCallback testConfigCallback){
        RALog.d(TAG, " Load Chapter List ");
        if(testFwConfig!=null){
            testConfigCallback.onChaptersLoaded(testFwConfig.getChaptersList());
        }else {
            TestConfigLoader testConfigLoader = new TestConfigLoader(context, testConfigCallback, handler);
            testConfigLoader.start();
        }

    }

    public String loadJSONFromAsset(Context context) {
        RALog.d(TAG, " Load Json from Asset ");
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/TestFwConfig.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            int bRead = 0;

            /*
             * PSRA: Story 83645: The method loadJSONFromAsset() in TestConfigManager.java
             * ignores the value returned by read() on line 73, which could
             * cause the program to overlook unexpected states and conditions.
             */
            while (bRead < size) {
                int rd = is.read(buffer, bRead, size - bRead);
                if (rd == -1) {
                    throw new IOException("file is unusually small");
                }
                bRead += rd;
            }

            // could add check to see if file is too large here
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
           RALog.e(TAG+ FILE_IO,
                    ex.getMessage());
            return null;
        }
        return json;
    }

    public void getCoCoList(Chapter chapter,TestConfigCallback testConfigCallback){
        if(chapter!=null){
            if(testFwConfig!=null){
                for(Chapter c:testFwConfig.getChaptersList()){
                    if(chapter.getChapterName().equalsIgnoreCase(c.getChapterName())){
                        testConfigCallback.onCOCOLoaded(c.getCommonComponentsList());
                    }
                }
            }
        }else{
            testConfigCallback.onCOCOLoadError();
        }
    }

    class TestConfigLoader extends Thread{

        private Context context;

        private TestConfigCallback testConfigCallback;

        private Handler callBackHander;

        public TestConfigLoader(Context context,TestConfigCallback testConfigCallback,Handler callBackHandler){
            this.context=context;
            this.testConfigCallback=testConfigCallback;
            this.callBackHander=callBackHandler;
        }
        @Override
        public void run() {
            testFwConfig=new GsonBuilder().create().fromJson(loadJSONFromAsset(context), TestFwConfig.class);
            callBackHander.post(new Runnable() {
                @Override
                public void run() {
                    testConfigCallback.onChaptersLoaded(testFwConfig.getChaptersList());
                }
            });
        }
    }

}

package com.philips.platform.appframework.testmicroappfw.data;

import android.content.Context;
import android.os.Handler;

import com.google.gson.GsonBuilder;
import com.philips.platform.appframework.testmicroappfw.models.Chapter;
import com.philips.platform.appframework.testmicroappfw.models.CommonComponent;
import com.philips.platform.appframework.testmicroappfw.models.TestFwConfig;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

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
        if(testFwConfig!=null){
            testConfigCallback.onChaptersLoaded(testFwConfig.getChaptersList());
        }else {
            TestConfigLoader testConfigLoader = new TestConfigLoader(context, testConfigCallback, handler);
            testConfigLoader.start();
        }

    }

    public String loadJSONFromAsset(Context context) {
        String json = null;
        try {
            InputStream is = context.getAssets().open("json/TestFwConfig.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            AppFrameworkApplication.loggingInterface.log(LoggingInterface.LogLevel.DEBUG, FILE_IO,
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

package com.philips.platform.uappdemo;

import android.content.Context;
import android.support.annotation.AnyRes;
import android.support.annotation.NonNull;
import android.support.annotation.RawRes;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.JsonStructureException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appframework.flowmanager.models.AppFlowModel;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.flowmanager.UappFlowManager;
import com.philips.platform.flowmanager.utility.UappBaseAppUtil;
import com.philips.platform.uappdemo.screens.base.UappFileUtility;
import com.philips.platform.uappdemolibrary.BuildConfig;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UappDemoUiHelper {

    private static UappDemoUiHelper uappDemoUiHelper;
    private BaseFlowManager flowManager;
    private LoggingInterface loggingInterface;
    private AppInfraInterface appInfra;
    private Context context;
    private boolean isSdCardFileCreated;
    private File tempFile;

    /*
    * Initialize everything(resources, variables etc) required for Product Registration.
    * Hosting app, which will integrate this Product Registration, has to pass app
    * context.
    */
    private UappDemoUiHelper() {
    }

    /*
     * Singleton pattern.
     */
    public static UappDemoUiHelper getInstance() {
        if (uappDemoUiHelper == null) {
            uappDemoUiHelper = new UappDemoUiHelper();
        }
        return uappDemoUiHelper;
    }

    public BaseFlowManager getFlowManager() {
        return flowManager;
    }

    protected void init(final UappDependencies uappDependencies, final UappSettings uappSettings) {
        UappDemoDependencies dependencies = (UappDemoDependencies) uappDependencies;
        this.context = uappSettings.getContext();
        this.flowManager = dependencies.getFlowManager();
        this.appInfra = uappDependencies.getAppInfra();
//        initFile();
    }

    public LoggingInterface getLoggingInterface() {
        if (loggingInterface == null) {
            loggingInterface = appInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
        }
        return loggingInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public void setTargetFlowManager(FlowManagerListener flowManagerListener, FragmentActivity activity) {
        try {
            this.flowManager = new UappFlowManager();
            this.flowManager.initialize(context, getAppFlow(R.raw.uappflow), flowManagerListener);
        } catch (JsonFileNotFoundException e) {
            if (tempFile != null) {
                this.flowManager = new UappFlowManager();
                this.flowManager.initialize(context, R.raw.uappflow, flowManagerListener);
            }
        }
    }

    private AppFlowModel getAppFlow(@RawRes int resId) throws JsonFileNotFoundException, JsonStructureException {
        AppFlowModel appFlow = null;
        if (resId == 0) {
            throw new JsonFileNotFoundException();
        } else {
            try {
                final InputStreamReader inputStreamReader = getInputStreamReader(resId);
                appFlow = new Gson().fromJson(inputStreamReader, AppFlowModel.class);
                inputStreamReader.close();
            } catch (JsonSyntaxException | FileNotFoundException e) {
                if (e instanceof JsonSyntaxException) {
                    throw new JsonStructureException();
                } else {
                    throw new JsonFileNotFoundException();
                }
            } catch (IOException e) {
                Log.e("IO-Exception", " error while reading appflow.json");
            }
        }
        return appFlow;
    }

    @NonNull
    private InputStreamReader getInputStreamReader(@AnyRes final int resId) throws FileNotFoundException {
        InputStream is = context.getResources().openRawResource(resId);
        return new InputStreamReader(is);
    }


    private void initFile() {
        isSdCardFileCreated = new UappBaseAppUtil().createDirIfNotExists();
        final int resId = R.string.af_json_path;
        UappFileUtility uappFileUtility = new UappFileUtility(context);
        tempFile = uappFileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
    }
}

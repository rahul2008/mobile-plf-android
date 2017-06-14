package com.philips.platform.uappdemo;

import android.content.Context;
import android.support.v4.app.FragmentActivity;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.flowmanager.UappFlowManager;
import com.philips.platform.flowmanager.utility.UappBaseAppUtil;
import com.philips.platform.screens.base.UappFileUtility;
import com.philips.platform.uappdemolibrary.BuildConfig;
import com.philips.platform.uappdemolibrary.R;
import com.philips.platform.uappframework.uappinput.UappDependencies;
import com.philips.platform.uappframework.uappinput.UappSettings;

import java.io.File;

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
        initFile();
    }

    public LoggingInterface getLoggingInterface() {
        if (loggingInterface == null) {
            loggingInterface = appInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
            loggingInterface.enableConsoleLog(true);
            loggingInterface.enableFileLog(true);
        }
        return loggingInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }

    public void setTargetFlowManager(FlowManagerListener flowManagerListener, FragmentActivity activity) {
        try {
            this.flowManager = new UappFlowManager();
            this.flowManager.initialize(context, new UappBaseAppUtil().getJsonFilePath().getPath(), flowManagerListener);
        } catch (JsonFileNotFoundException e) {
            if (tempFile != null) {
                this.flowManager = new UappFlowManager();
                this.flowManager.initialize(context, tempFile.getPath(), flowManagerListener);
            }
        }
    }

    private void initFile() {
        isSdCardFileCreated = new UappBaseAppUtil().createDirIfNotExists();
        final int resId = R.string.af_json_path;
        UappFileUtility uappFileUtility = new UappFileUtility(context);
        tempFile = uappFileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
    }
}

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package philips.app.base;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.JsonFileNotFoundException;
import com.philips.platform.appframework.flowmanager.listeners.FlowManagerListener;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.logging.LoggingInterface;

import java.io.File;

import flowmanager.FlowManager;
import flowmanager.screens.utility.BaseAppUtil;
import philips.app.BuildConfig;
import philips.app.R;

/**
 * Application class is used for initialization
 */
public class AppFrameworkApplication extends Application {
    private static final String LEAK_CANARY_BUILD_TYPE = "leakCanary";
    public AppInfraInterface appInfra;
    public LoggingInterface loggingInterface;
    protected FlowManager targetFlowManager;
    private boolean isSdCardFileCreated;
    private File tempFile;

    @SuppressWarnings("deprecation")
    @Override
    public void onCreate() {
        isSdCardFileCreated = new BaseAppUtil().createDirIfNotExists();
        final int resId = R.string.com_philips_app_fmwk_app_flow_url;
        FileUtility fileUtility = new FileUtility(this);
        tempFile = fileUtility.createFileFromInputStream(resId, isSdCardFileCreated);
        MultiDex.install(this);
        super.onCreate();
        appInfra = new AppInfra.Builder().build(getApplicationContext());
        loggingInterface = appInfra.getLogging().createInstanceForComponent(BuildConfig.APPLICATION_ID, BuildConfig.VERSION_NAME);
    }

    public LoggingInterface getLoggingInterface() {
        return loggingInterface;
    }

    public AppInfraInterface getAppInfra() {
        return appInfra;
    }


    public BaseFlowManager getTargetFlowManager() {
        return targetFlowManager;
    }

    public void setTargetFlowManager(FlowManagerListener flowManagerListener, Context context) {
        try {
            this.targetFlowManager = new FlowManager();
            this.targetFlowManager.initialize(context, new BaseAppUtil().getJsonFilePath().getPath(), flowManagerListener);
        } catch (JsonFileNotFoundException e) {
            if (tempFile != null) {
                this.targetFlowManager = new FlowManager();
                this.targetFlowManager.initialize(context, tempFile.getPath(), flowManagerListener);
            }
        }
    }
}

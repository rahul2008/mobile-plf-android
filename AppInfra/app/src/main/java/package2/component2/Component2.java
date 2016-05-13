package package2.component2;

import android.content.Context;

import com.philips.appinfra.AppInfra;
import com.philips.appinfra.LoggingInterface;

/**
 * Created by 310238114 on 5/13/2016.
 */
public class Component2 {
    private AppInfra mAppInfra = null;
    private LoggingInterface AILoggingInterface;
    Context mContext;

    public Component2(AppInfra pAppInfra){

        mAppInfra=pAppInfra;
       // mAppInfra = new AppInfra.Builder().build(mContext);
        AILoggingInterface = mAppInfra.getLogging().createInstanceForComponent("package2.component2", "2.0.1"); //this.getClass().getPackage().toString()
        AILoggingInterface.enableConsoleLog(true);
        AILoggingInterface.enableFileLog(true);
        showLog();
    }

    public void showLog(){
        AILoggingInterface.log(LoggingInterface.LogLevel.ERROR,"c2 er","c2 msg");
        AILoggingInterface.log(LoggingInterface.LogLevel.WARNING,"c2 er","c2 msg");

        AILoggingInterface.log(LoggingInterface.LogLevel.INFO,"c2 er","c2 msg");
        AILoggingInterface.log(LoggingInterface.LogLevel.DEBUG,"c2 er","c2 msg");
        AILoggingInterface.log(LoggingInterface.LogLevel.VERBOSE,"c2 er","c2 msg");


    }
}

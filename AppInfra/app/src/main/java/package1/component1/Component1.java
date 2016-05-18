package package1.component1;

import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.LoggingInterface;

import package2.component2.Component2;

/**
 * Created by 310238114 on 5/13/2016.
 */
public class Component1 {
    private AppInfra mAppInfra = null;
    private LoggingInterface AILoggingInterface;
    public Component1(AppInfra pAppInfra){
        mAppInfra=pAppInfra;
        //mAppInfra = new AppInfra.Builder().build(mContext);
        AILoggingInterface = mAppInfra.getLogging().createInstanceForComponent("package1.component1", "1.0.1"); //this.getClass().getPackage().toString()
        AILoggingInterface.enableConsoleLog(true);
        AILoggingInterface.enableFileLog(true);
        showLog();
    }

     void showLog(){
        AILoggingInterface.log(LoggingInterface.LogLevel.ERROR,"c1 er","c1 msg");
        AILoggingInterface.log(LoggingInterface.LogLevel.WARNING,"c1 er","c1 msg");
         Component2 component2= new Component2(mAppInfra);
        AILoggingInterface.log(LoggingInterface.LogLevel.INFO,"c1 er","c1 msg");

        AILoggingInterface.log(LoggingInterface.LogLevel.DEBUG,"c1 er","c1 msg");

        AILoggingInterface.log(LoggingInterface.LogLevel.VERBOSE,"c1 er","c1 msg");


    }
}

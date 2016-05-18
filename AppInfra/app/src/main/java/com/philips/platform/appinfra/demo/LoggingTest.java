package com.philips.platform.appinfra.demo;

import android.content.Context;

/**
 * Created by 310238114 on 4/27/2016.
 */
public class LoggingTest {
 Context mContext;
    public LoggingTest(Context pContext){
        mContext=pContext;
    }

    public void testLog(){
       /* AppInfraLogger appInfraLogger= new AppInfraLogger(mContext);

        Logger logger = appInfraLogger.createInstanceForComponent("Registration","1.2");
       // appInfraLogger.fileLogEnabled(true);
        logger.log(Level.SEVERE, "msg reg 1", "event sev");
        logger.log(Level.WARNING,"msg reg 1","event war");
        logger.log(Level.INFO,"msg reg 1","event info");
        logger.log(Level.CONFIG,"msg reg 1","event config");
        logger.log(Level.FINE, "msg reg 1", "event fine");*/
       // appInfraLogger.disableConsoleLog();
       // logger.setLevel(Level.OFF);
       /* logger.log(Level.SEVERE,"msg reg 1","event sev disabled");
        logger.log(Level.WARNING,"msg reg 1","event war");
        logger.log(Level.INFO,"msg reg 1","event info");
        logger.log(Level.CONFIG,"msg reg 1","event config");
        logger.log(Level.FINE, "msg reg 1", "event fine");*/
    }
}

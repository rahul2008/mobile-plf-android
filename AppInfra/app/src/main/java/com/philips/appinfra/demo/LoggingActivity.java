package com.philips.appinfra.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.philips.appinfra.AppInfra;
import com.philips.appinfra.LoggingInterface;

import java.util.logging.Logger;

public class LoggingActivity extends AppCompatActivity {

    private  AppInfra ai = null;
    private  LoggingInterface AILoggingInterface;

    static Logger logger;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logging);

        ////////////////////////////////////////////////////////////
        ai = new AppInfra.Builder().build();
       //  ai = new AppInfra.Builder().setLogging(myLogger).build();
        AILoggingInterface = ai.getLogging().createInstanceForComponent(this.getClass().getPackage().toString(), "1.2.3");
        AILoggingInterface.log(LoggingInterface.LogLevel.DEBUG,"event id logging Activity","some msg 123 ");


        //////////////////////////////////////////////

/**/        //Button logTestButton = (Button)findViewById(R.id.LogTestButtonID);

/*
        LogConfig mLogConfig = new LogConfig(getApplicationContext());

        Logger logger =  mLogConfig.getConfig();

        logger.warning("Hi How r u?  warning");
        logger.info("Hi How r u? info");
        logger.config("Hi How r u? config");*/


        /*logTestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });*/

       /* AppInfraLogger appInfraLogger= new AppInfraLogger(getApplicationContext());

         logger = appInfraLogger.createInstanceForComponent("UiKIT", "3.0");
       appInfraLogger.enableFileLog(true);
      //  appInfraLogger.enableConsoleLog(true);
       // logger.setLevel(Level.FINER);
        logger.log(Level.SEVERE, "msg sev 1", "event sev");
        logger.log(Level.WARNING, "msg war 1", "event war");
       // logger.setLevel(Level.OFF);
        //appInfraLogger.disableConsoleLog();
        logger.log(Level.INFO, "msg inf 1", "event info");

        logger.log(Level.CONFIG,"msg config 1","event config");
        logger.log(Level.FINE, "msg fine 1", "event fine");

        LoggingTest tl = new LoggingTest(getApplicationContext());
        tl.testLog();*/




/*
        logger.setLevel(Level.INFO);
        logger.log(Level.SEVERE, "msg sev 2", "event sev");
        logger.log(Level.WARNING,"msg war 2","event war");
        logger.log(Level.INFO,"msg inf 2","event info");
        logger.log(Level.CONFIG,"msg config 2","event config");
        logger.log(Level.FINE, "msg fine 2", "event fine");
        logger.setLevel(Level.OFF);
        logger.log(Level.SEVERE, "msg sev 3", "event sev");
        logger.log(Level.WARNING,"msg war 3","event war");
        logger.log(Level.INFO,"msg inf 3","event info");
        logger.log(Level.CONFIG,"msg config 3","event config");
        logger.log(Level.FINE, "msg fine 3", "event fine");
        */







    }
}

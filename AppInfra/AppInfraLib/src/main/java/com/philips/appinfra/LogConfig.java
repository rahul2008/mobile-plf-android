
package com.philips.appinfra;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by 310238114 on 4/25/2016.
 */

public class LogConfig {

    static Context mContext = null;

    public LogConfig(Context pContext) {
        mContext = pContext;

    }
    //static Logger log = LogManager.getLogger(LogConfig.class.getName());


    static private FileHandler fileTxt;
    static private SimpleFormatter formatterTxt;

    static private FileHandler fileHTML;
    static private Formatter formatterHTML;

    static public void setup() throws IOException {

        // get the global logger to configure it
         /*
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);

        // suppress the logging output to the console
        Logger rootLogger = Logger.*getLogger*("");
        Handler[] handlers = rootLogger.getHandlers();
        if (handlers[0] instanceof ConsoleHandler) {
            rootLogger.removeHandler(handlers[0]);
        }

        logger.setLevel(Level.INFO);
        fileTxt = new FileHandler("Logging.txt");
        //fileHTML = new FileHandler("Logging.html");

        // create a TXT formatter
        formatterTxt = new SimpleFormatter();
        fileTxt.setFormatter(formatterTxt);
        logger.addHandler(fileTxt);

        // create an HTML formatter
        formatterHTML = new MyHtmlFormatter();
        fileHTML.setFormatter(formatterHTML);
        logger.addHandler(fileHTML);
        */

    }

    public Logger getConfig() {
        Logger logger = Logger.getLogger("MyLog");
        FileHandler fh;

        try {

            // This block configure the logger with handler and formatter
           // fh = new FileHandler("C:/temp/test/MyLogFile.log");
            FileHandler fileHandler = getFileHandler();

            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);
            logger.addHandler(fileHandler);


            // the following statement is used to log any messages
           // logger.info("My first log");

        } catch (SecurityException e) {
            e.printStackTrace();
        }
        logger.setLevel(Level.INFO);
        //logger.info("Hi How r u?");
        return logger;
    }


    FileHandler getFileHandler() {
        FileHandler fh = null;
       // String  fileName = Environment.getDataDirectory()+ "/mylogfile.log";
        String filePath="";
         if (0 == Environment.getExternalStorageState().compareTo(Environment.MEDIA_MOUNTED))
             filePath = Environment.getExternalStorageDirectory().getAbsolutePath();
        else
             filePath = Environment.getDataDirectory().getAbsolutePath();

        filePath += "/mylogfile1.log";
          /* File file = new File(mContext.getFilesDir() + "/" + "AIlogFile.log");
        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
            try {
                filePath =file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }*/


        try {
            Log.e("File Path", filePath );
            fh = new FileHandler(filePath, 256 * 1024, 5, true);
            fh.setFormatter(new SimpleFormatter());
            fh.publish(new LogRecord(Level.ALL, "myTAG" + ": " + "finallly"));

        } catch (Exception e)
        {
            Log.e("MyLog", "FileHandler exception", e);

        }finally{
            if (fh != null){

            }

                //fh.close();
        }
        return fh;
    }
}


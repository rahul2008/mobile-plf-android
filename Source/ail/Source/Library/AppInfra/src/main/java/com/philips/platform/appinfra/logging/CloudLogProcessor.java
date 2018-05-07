package com.philips.platform.appinfra.logging;

import android.os.Handler;
import android.os.HandlerThread;

/**
 * Created by abhishek on 5/4/18.
 */

public class CloudLogProcessor extends HandlerThread {

    private Handler mWorkerHandler;

    public CloudLogProcessor(String name) {
        super(name);
    }
    public void postTask(Runnable task){
        mWorkerHandler.post(task);
    }

    public void prepareHandler(){
        mWorkerHandler = new Handler(getLooper());
    }
}

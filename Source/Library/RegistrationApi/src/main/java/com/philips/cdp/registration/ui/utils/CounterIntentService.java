package com.philips.cdp.registration.ui.utils;

import android.app.IntentService;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;

import com.philips.cdp.registration.events.CounterHelper;

/**
 * Created by philips on 15/06/17.
 */

public class CounterIntentService extends IntentService {
    private static long RESEND_DISABLED_DURATION=60*1000;
    private static final long INTERVAL = 1 * 1000;
   static public MyCountDownTimer myCountDownTimer;


    public CounterIntentService() {
        super(CounterIntentService.class.getName());
        myCountDownTimer = new MyCountDownTimer(RESEND_DISABLED_DURATION, INTERVAL);

    }
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        myCountDownTimer.start();

    }
    public class MyCountDownTimer extends CountDownTimer {
        public MyCountDownTimer(long startTime, long interval) {
            super(startTime, interval);
        }

        @Override
        public void onTick(long timeLeft) {
            CounterHelper.getInstance().notifyCounterEventOccurred(RegConstants.COUNTER_TICK,timeLeft);
        }

        @Override
        public void onFinish() {
            CounterHelper.getInstance().notifyCounterEventOccurred(RegConstants.COUNTER_FINISH,0);
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}

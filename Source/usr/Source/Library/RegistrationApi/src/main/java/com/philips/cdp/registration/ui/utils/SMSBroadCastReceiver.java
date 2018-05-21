package com.philips.cdp.registration.ui.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by philips on 5/21/18.
 */

public class SMSBroadCastReceiver extends BroadcastReceiver {

    private final ReceiveSMSListener receiveSMSListener;
    public static final String OTP_REGEX = "[0-9]{1,6}";

    public SMSBroadCastReceiver(ReceiveSMSListener receiveSMSListener) {

        this.receiveSMSListener = receiveSMSListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {

            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();

            //Check the sender to filter messages which we require to read
            if (sender.equals("PHILIPS")) {

                String messageBody = smsMessage.getMessageBody();

                getOTPFromMessage(messageBody);
            }
        }

    }

    private void getOTPFromMessage(String messageBody) {

        Pattern pattern = Pattern.compile(OTP_REGEX);
        Matcher matcher = pattern.matcher(messageBody);

        String otp = "";

        while (matcher.find()) {
            otp = matcher.group();
            receiveSMSListener.onSMSReceive(otp);
        }
    }

    public interface ReceiveSMSListener {

        void onSMSReceive(String otp);
    }

}


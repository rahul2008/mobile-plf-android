package com.philips.platform.appinfra.securestoragev2;

import android.os.Build;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

/**
 * Created by abhishek on 2/6/18.
 */

public class SSUtils {

    public static boolean isDeviceVersionUpgraded(){
        if(Build.VERSION.SDK_INT>=23) {
            try {
                KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
                keyStore.load(null);
                return keyStore.containsAlias(SSKeyProvider18Impl.SS_KEY_18_IMPL_ALIAS);
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }


    /**
     * Checks if the device is rooted.
     *
     * @return <code>true</code> if the device is rooted, <code>false</code> otherwise.
     */
    public static boolean checkProcess() {
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(new String[]{"/system/xbin/which", "/system/bin/which", "su"});
            final BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
            return in.readLine() != null;
        } catch (Throwable t) {
            return false;
        } finally {
            if (process != null) process.destroy();
        }
    }


}

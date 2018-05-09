package com.philips.platform.appinfra.securestoragev2;

import android.app.KeyguardManager;
import android.content.Context;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
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

    public static String getDeviceCapability() {
        final String buildTags = android.os.Build.TAGS;
        if (buildTags != null && buildTags.contains("test-keys")) {
            return "true";
        }
        final String[] paths = {"/system/app/Superuser.apk", "/sbin/su", "/system/bin/su",
                "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su",
                "/system/bin/failsafe/su", "/data/local/su", "/su/bin/su"};
        for (String path : paths) {
            if (new File(path).exists()) return "true";
        }
        if (SSUtils.checkProcess()) {
            return "true";
        }

        return "false";

    }

    public static boolean deviceHasPasscode(Context context) {
        if (context != null) {
            KeyguardManager manager = (KeyguardManager)
                    context.getSystemService(Context.KEYGUARD_SERVICE);
            return manager.isKeyguardSecure();
        }
        return false;
    }

}

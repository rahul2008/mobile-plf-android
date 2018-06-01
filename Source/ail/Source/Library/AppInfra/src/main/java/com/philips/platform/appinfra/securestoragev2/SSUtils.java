package com.philips.platform.appinfra.securestoragev2;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Build;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.security.KeyStore;
import java.security.MessageDigest;

/**
 * Created by abhishek on 2/6/18.
 */

public class SSUtils {

    public static boolean isDeviceVersionUpgraded() {
        if (Build.VERSION.SDK_INT >= 23) {
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

    public static boolean isCodeTampered(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : packageInfo.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                md.update(signature.toByteArray());
                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);
                String SIGNATURE = "lDzCVHTBzfUEYeCh3RcMEj6PJ20=";
                if (SIGNATURE.equals(currentSignature)) {
                    return false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    public static boolean isEmulator() {
        return Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || "google_sdk".equals(Build.PRODUCT);
    }


}

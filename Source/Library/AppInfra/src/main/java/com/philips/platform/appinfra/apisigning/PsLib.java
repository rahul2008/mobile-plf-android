package com.philips.platform.appinfra.apisigning;

/**
 * Please dont move this file to any other package and also don't rename this class
 * Because this class name and package name is expecting in Native c code library w.r.t below comment code
 * JNIEXPORT jbyteArray JNICALL  Java_com_philips_platform_appinfra_apisigning_PshmacLib_pshmac
 * (JNIEnv *env, jclass thisClass, jbyteArray keyJNI, jbyteArray messageJNI)
 */

public class PsLib {

    static {
        System.loadLibrary("ps");
    }

    public static native byte[] svm100514(byte[] key, byte[] message);

    public byte[] createHmac(final byte[] key, final byte[] data) {
        return svm100514(key, data);
    }
}

package com.philips.platform.appinfra.apisigning;

/**
 * Created by 310238655 on 11/23/2016.
 * Please dont move this file to any other package and also dont rename this class
 * Because this class name and package name is expecting in Native code w.r.t below comment code
 * JNIEXPORT jbyteArray JNICALL  Java_com_philips_platform_appinfra_whiteboxapi_GenerateHmacLib_pshmac
 * (JNIEnv *env, jclass thisClass, jbyteArray keyJNI, jbyteArray messageJNI)
 */

public class PshmacLib{

    static {
        System.loadLibrary("pshmac");
    }

    public static native byte[] pshmac(byte[] key, byte[] message);

    public static byte[] createHmac(final byte[] key, final byte[] data) {
        return pshmac(key, data);
    }
}

/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */

package com.philips.platform.appinfra.keybag;


public class KeyBagLib {

    static {
        System.loadLibrary("native-lib");
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     * @param chars
     */
    public native void lfsrMain(byte[] chars);

    public native void lfsrObfuscate(char[] chars, int length, int seed);
    public native String getMsgFromJni();
    public native char[] passingDataToJni(char[] chars,int lfsr, String stringValue_,char[] array,int length);
}

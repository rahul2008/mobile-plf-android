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

    public native String getMsgFromJni();

    public native byte[] ConvertString(String testString,int length,int seed);

    public native String passingDataToJni(String stringValue_, int length, int seed);
}

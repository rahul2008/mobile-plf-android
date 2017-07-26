/*
 * Copyright (c) Koninklijke Philips N.V. 2016
 * All rights are reserved. Reproduction or dissemination in whole or in part
 * is prohibited without the prior written consent of the copyright holder.
 */
package com.philips.platform.appinfra.keybag;


import android.text.TextUtils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

class KeyBagHelper {

    private String getMd5Value(String data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(data.getBytes());

            byte byteData[] = md.digest();

            StringBuilder hexString = new StringBuilder();
            for (int i=0;i<byteData.length;i++) {
                String hex=Integer.toHexString(0xff & byteData[i]);
                if(hex.length()==1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        return null;
    }

    String getSeed(String groupId, String key, int index) {
        if (!TextUtils.isEmpty(groupId) && !TextUtils.isEmpty(key)) {
            String concatData = groupId.trim().concat(String.valueOf(index));
//            String concatData = groupId.trim().concat(key.trim()).concat(String.valueOf(index));
            String md5Value = getMd5Value(concatData);
            if (md5Value != null && md5Value.length() > 4)
                return md5Value.substring(0, 4).toUpperCase();
        }

        return null;
    }

    String getHexStringToString(String hex) {
        int l = hex.length();
        char[] data = new char[l / 2];
        for (int i = 0; i < l; i += 2) {
            data[i / 2] = (char) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return new String(data);
    }

    private String convertToHexaDecimal(String input, String charsetName) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes(charsetName));
    }

    private final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }


}

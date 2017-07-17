package com.example;

import java.io.UnsupportedEncodingException;

/**
 * Created by Yogesh on 6/16/17.
 */

public class LFSRObfuscate {

    char[] chars;
    static String raviKiran = "ad 9e 95 9e df ad 9e 92 df b2 90 97 9e 91 df ad 90 86";


    private static char[] lfsr16Obfuscate(char[] data, int length, short lfsr) {
        int i, lsb;

        for (i = 0; i < length * 8; i++) {
            lsb = lfsr & 1;                // Finding least significant bit
            lfsr >>= 1;                    // Right shifting
            if (lsb == 1) {
                data[i / 8] ^= 1 << (i % 8);    // exor "1" with bit position i%8 for byte i/8 of the message
                lfsr ^= 0xB400;
            }
        }


        return data;
    }


    public static void main(String[] args) {
        int messageLength;

        String message = "Raja Ram Mohan Roy";

        System.out.println("original message: %s\n" + message);
        messageLength = message.length();

    /* Obfuscate the message */
        char[] chars = lfsr16Obfuscate(message.toCharArray(), messageLength, (short) 0xACE1);



        String string = getString(chars);
        String hexadecimal="";
        try {
            hexadecimal = hexadecimal(string, "UTF-8");
            System.out.println(raviKiran.equals(hexadecimal));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String data ="\\xad\\x9e\\x95\\x9e߭\\x9e\\x92߲\\x90\\x97\\x9e\\x91߭\\x90\\x86";
        System.out.println("obfuscated message: %s\n" + hexadecimal);


       /* byte[] bytes = hexStringToByteArray(string);

        for (byte b :
                bytes) {
            System.out.println("obfuscated message in bytes "+b);
        }*/

    /* Repeat the obfuscation process to retrieve original message */
        char[] afterDeObfuscate = lfsr16Obfuscate(data.toCharArray(), data.length(), (short) 0xACE1);

        System.out.println("Recovered message: %s\n" + getString(afterDeObfuscate));
//        System.out.println("Recovered message for data : %s\n" + getString(afterDeObfuscate));

    }


    public static String hexadecimal(String input, String charsetName) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes(charsetName));
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    public static String asHex(byte[] buf)
    {
        char[] chars = new char[2 * buf.length];
        for (int i = 0; i < buf.length; ++i)
        {
            chars[2 * i] = HEX_CHARS[(buf[i] & 0xF0) >>> 4];
            chars[2 * i + 1] = HEX_CHARS[buf[i] & 0x0F];
        }
        return new String(chars);
    }


    private static String getString(char[] message) {
        String original="";
        for (char c: message) {
            original = original + (String.format("%c", c));
        }
        return original;
    }

    private static byte[] hexStringToByteArray(String s) {
        final int len = s.length();
        final byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

}

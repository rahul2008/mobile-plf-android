package com.example;

import java.io.UnsupportedEncodingException;

public class LFSRObfuscate {

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



        String obfuscatedData = getString(chars);
        String hexadecimal="";
        try {
            hexadecimal = convertToHexaDecimal(obfuscatedData, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        System.out.println("obfuscated message: %s\n" + hexadecimal);

    /* Repeat the obfuscation process to retrieve original message */
        char[] afterDeObfuscate = lfsr16Obfuscate(obfuscatedData.toCharArray(), obfuscatedData.length(), (short) 0xACE1);

        System.out.println("Recovered message: %s\n" + getString(afterDeObfuscate));

    }


    private static String convertToHexaDecimal(String input, String charsetName) throws UnsupportedEncodingException {
        if (input == null) throw new NullPointerException();
        return asHex(input.getBytes(charsetName));
    }

    private static final char[] HEX_CHARS = "0123456789abcdef".toCharArray();

    private static String asHex(byte[] buf)
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

}

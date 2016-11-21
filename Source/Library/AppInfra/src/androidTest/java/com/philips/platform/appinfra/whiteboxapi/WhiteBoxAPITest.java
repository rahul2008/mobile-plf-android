package com.philips.platform.appinfra.whiteboxapi;

import com.philips.platform.appinfra.MockitoTestCase;

import static com.philips.platform.appinfra.whiteboxapi.GenerateHmacLib.createHmac;

/**
 * Created by 310190722 on 11/21/2016.
 */

public class WhiteBoxAPITest extends MockitoTestCase{

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testWhiteBoxAPI(){
         byte[] k = hexStringToByteArray("d9efcaeb7077f16729c1568bde56eed25635030f688990d3fc9281cb809d4666db0057e8b902382f9de16fed325889a46e7c22e31a143ee60b33c1ac22bc8b28");
         byte[] d = {'P','O','S','T'};

        byte[] resultBytes = createHmac(k,d);
        assertEquals("e4dfa5e056c7df4e9e13d2efc9b3c792b0bd3bfc4e501eb27990281de3e6f5ea",bytesToHex(resultBytes));
    }


    private static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                    + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }

    public static String bytesToHex(byte[] input) {
        final StringBuilder builder = new StringBuilder();
        for(byte b : input) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
}

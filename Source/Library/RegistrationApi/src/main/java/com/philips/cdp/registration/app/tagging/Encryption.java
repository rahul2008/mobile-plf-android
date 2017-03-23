package com.philips.cdp.registration.app.tagging;


import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;

public class Encryption {

    private static final String privateKey = "MIIBOgIBAAJBAIsEvFpH2SleHYc0EyytGHEtIcF2vKLavrfS1zEKItwPdN17sLnO\n" +
            "oO6FROHLyaKobEi8WQhhiB6GOGkW/+brjP8CAwEAAQJAHBSqzzmwHfEK2eKk9ONK\n" +
            "CqJpLSEE3Yh9+be3DArWG8ket+rGdQQ1N1tA9hyCM4Qc+e1kOvrQotyqyF5gvO/O\n" +
            "EQIhANTLL16xweissyaetHzCmGYympE/IJ09hUeudoPUfYkLAiEApz7JMyIHB6E6\n" +
            "CTIVZitSMJdX2N++PKW53Bgpue3UzF0CIDfAwLmL02V2Ej5VKK7jgXCNY6gYpR/t\n" +
            "pYUBdfXWb8m/AiB5Vrgq/PkUtAijO08DPVL4JhV6J3qiDar24CEF4GOPkQIhAJ8V\n" +
            "Nvv53QbCsUNlq9WJaQtpYomOS0CdJ/pxrKjNsvRB";

    public String encrypt(String toBeEncrypted) {
        if(toBeEncrypted == null) return null;
        //compute SHA256withRSA as a single step
        Signature rsaSha256Signature = null;
        try {
            rsaSha256Signature = Signature.getInstance("SHA256withRSA");


            byte[] pkcs8EncodedBytes = Base64.decode(privateKey, Base64.DEFAULT);

            // extract the private key

            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pkcs8EncodedBytes);
            KeyFactory kf = KeyFactory.getInstance("SHA256withRSA");
            PrivateKey privKey = kf.generatePrivate(keySpec);


            rsaSha256Signature.initSign(privKey);
            rsaSha256Signature.update(toBeEncrypted.getBytes());
            byte[] signed2 = rsaSha256Signature.sign();
            System.out.println(bytesToHex(signed2));
            String encrypted = bytesToHex(signed2);
            return encrypted;
        } catch (NoSuchAlgorithmException | SignatureException | InvalidKeyException | InvalidKeySpecException e) {
            e.printStackTrace();
        }
        return null;


    }

    private String bytesToHex(byte[] bytes) {
        final char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

}

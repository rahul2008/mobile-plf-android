package com.philips.cdp.registration.app.tagging;


import android.util.Base64;

import com.philips.cdp.registration.ui.utils.RLog;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encryption {

    private static final String ALGORITHM = "RSA";

    private static final String PROVIDER = "BC";

    private static final String TRANSFORMATION = "RSA/ECB/OAEPWITHSHA1ANDMGF1PADDING";

    private static final String PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCYR2mnp+gBnwEUar8LE3N0oyJXtOsoeA9NHMTsdljf2nHWRIl BvHVIB5wt30qSAEfY/lUzXsrcafNPCxfF8E3IsZfkrYw57EJwMQ2qKoMlulekWIXtz13n1tnRSNtT9C0tTZyKB4Q 1EBwbTRH2RCoEBm7JYQVHEm9HLFLw1OaXvQIDAQAB";

    private PublicKey getPublicKey(String publicKey) {
        PublicKey key = null;
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Base64.decode(publicKey.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance(ALGORITHM);
            key = keyFactory.generatePublic(spec);
        } catch (InvalidKeySpecException |
                NoSuchAlgorithmException expection) {
            RLog.e("Exception", expection.toString());
        }
        return key;
    }


    public String encrypt(String toBeEncrypted) {
        if (null == toBeEncrypted || toBeEncrypted.isEmpty()) {
            return String.valueOf("");
        }
        try {
            byte[] toBeEncryptedBytes = toBeEncrypted.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
            cipher.init(Cipher.ENCRYPT_MODE, getPublicKey(PUBLIC_KEY));
            byte[] cipherTextBytes = cipher.doFinal(toBeEncryptedBytes);
            byte[] base64EncodedBytes = Base64.encode(cipherTextBytes, Base64.DEFAULT);
            return new String(base64EncodedBytes);
        } catch (NoSuchAlgorithmException |
                NoSuchPaddingException | BadPaddingException |
                IllegalBlockSizeException | NoSuchProviderException | InvalidKeyException
                expection) {
            RLog.e("Exception", expection.toString());
        }
        return null;
    }
}

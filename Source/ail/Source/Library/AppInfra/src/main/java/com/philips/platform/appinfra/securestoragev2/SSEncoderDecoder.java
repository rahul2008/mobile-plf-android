package com.philips.platform.appinfra.securestoragev2;


import android.util.Base64;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;

public class SSEncoderDecoder {

    private static final String AES_ENCRYPTION_ALGORITHM = "AES/GCM/NoPadding";

    protected String encodeDecodeData(int mode, Key secretKey, String value) throws SSEncodeDecodeException {
        if (mode == Cipher.ENCRYPT_MODE) {
            byte[] cipherMessage = encodeDecodeData(mode, secretKey, value.getBytes());
            String encodedEncryptedString = Base64.encodeToString(cipherMessage, Base64.DEFAULT);
            return encodedEncryptedString;
        } else if (mode == Cipher.DECRYPT_MODE) {
            byte[] cipherMessage = Base64.decode(value, Base64.DEFAULT);
            byte[] plainText = encodeDecodeData(mode, secretKey, cipherMessage);
            return new String(plainText);
        } else {
            return null;
        }
    }

    protected byte[] encodeDecodeData(int mode, Key secretKey, byte[] value) throws SSEncodeDecodeException {

        try {
            final Cipher cipher = Cipher.getInstance(AES_ENCRYPTION_ALGORITHM);
            if (mode == Cipher.ENCRYPT_MODE) {
                byte[] iv = new byte[12];
                new SecureRandom().nextBytes(iv);
                cipher.init(Cipher.ENCRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
                byte[] encryptedBytes = cipher.doFinal(value);
                byte[] version = SecureStorageV2.VERSION.getBytes();
                ByteBuffer byteBuffer = ByteBuffer.allocate(4 + version.length + 4 + iv.length + encryptedBytes.length);//IV length integer+ Iv length+Integer to indicate Version of SS+Encrypted bytes length
                byteBuffer.putInt(version.length);//Secure Storage version
                byteBuffer.put(version);//Secure Storage version
                byteBuffer.putInt(iv.length);
                byteBuffer.put(iv);
                byteBuffer.put(encryptedBytes);
                byte[] cipherMessage = byteBuffer.array();
                return cipherMessage;
            } else if (mode == Cipher.DECRYPT_MODE) {
                ByteBuffer byteBuffer = ByteBuffer.wrap(value);
                int versionLength = byteBuffer.getInt();//Remove version from bytebuffer
                byte[] version = new byte[versionLength];
                byteBuffer.get(version);
                int ivLength = byteBuffer.getInt();
                byte[] iv = new byte[ivLength];
                byteBuffer.get(iv);
                byte[] cipherText = new byte[byteBuffer.remaining()];
                byteBuffer.get(cipherText);
                cipher.init(Cipher.DECRYPT_MODE, secretKey, new GCMParameterSpec(128, iv));
                byte[] plainText = cipher.doFinal(cipherText);
                return plainText;
            } else {
                return null;
            }
        } catch (GeneralSecurityException exception) {
            throw new SSEncodeDecodeException("Error while encoding/decoding data"+ exception.getMessage());
        } catch (Exception exception) {
            throw new SSEncodeDecodeException("Error while encoding/decoding data"+exception.getMessage());
        }
    }


}

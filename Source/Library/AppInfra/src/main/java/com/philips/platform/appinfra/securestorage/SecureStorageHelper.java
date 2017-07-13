package com.philips.platform.appinfra.securestorage;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;


class SecureStorageHelper {

    SecretKey generateAESKey() throws NoSuchAlgorithmException {
        final int outputKeyLength = 256; // Generate a 256-bit key
        final SecureRandom secureRandom = new SecureRandom();    // Do *not* seed secureRandom! Automatically seeded from system entropy.
        final KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        keyGenerator.init(outputKeyLength, secureRandom);
        return keyGenerator.generateKey();
    }

}

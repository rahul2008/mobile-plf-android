/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.security;

import android.support.annotation.NonNull;
import android.util.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.util.Arrays;


/**
 * A pin is the base64-encoded SHA-256 hash of the certificate's Subject Public Key Info.
 * This is copied from TrustKit (https://github.com/datatheorem/TrustKit-Android/blob/master/trustkit/src/main/java/com/datatheorem/android/trustkit/config/PublicKeyPin.java @ 0861bf1)
 */
public class PublicKeyPin {
    @NonNull
    private final byte[] pinBytes;

    PublicKeyPin(@NonNull Certificate certificate) {
        // Generate the certificate's spki pin
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("Should never happen");
        }
        digest.reset();

        byte[] spki = certificate.getPublicKey().getEncoded();
        pinBytes = digest.digest(spki);
    }

    PublicKeyPin(@NonNull String spkiPin) {
        // Validate the format of the pin
        pinBytes = Base64.decode(spkiPin, Base64.DEFAULT);
        if (pinBytes.length != 32) {
            throw new IllegalArgumentException("Invalid pin: length is not 32 bytes");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PublicKeyPin that = (PublicKeyPin) o;

        return Arrays.equals(pinBytes, that.pinBytes);

    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(pinBytes);
    }

    @Override
    public String toString() {
        return Base64.encodeToString(pinBytes, Base64.DEFAULT).trim();
    }
}

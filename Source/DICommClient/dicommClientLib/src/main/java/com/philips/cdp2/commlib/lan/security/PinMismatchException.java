/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.lan.security;

import android.support.annotation.NonNull;

import java.security.cert.CertificateException;

/**
 * This exception indicates a certificate pin mismatch.
 */
public class PinMismatchException extends CertificateException {
    public PinMismatchException(final @NonNull String message) {
        super(message);
    }
}

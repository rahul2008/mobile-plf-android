package com.philips.platform.appinfra.rest.hpkp;


import android.text.TextUtils;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfra;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_CERTIFICATE_EXPIRED;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE_EXPIRED;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_STORAGE_ERROR;

public class HPKPManager implements HPKPInterface {

    private static final String PUBLIC_KEY_REGEX = "pin-sha256=\"(.+?)\";";
    private Pattern publicKeyPattern = Pattern.compile(PUBLIC_KEY_REGEX);
    private HPKPLoggingHelper hpkpLoggingHelper;
    private HPKPStorageHelper hpkpStorageHelper;

    public HPKPManager(AppInfra appInfra) {
        this.hpkpStorageHelper = new HPKPStorageHelper(appInfra.getSecureStorage());
        this.hpkpLoggingHelper = new HPKPLoggingHelper(appInfra.getAppInfraLogInstance());
    }

    @Override
    public void updatePinnedPublicKey(String hostName, String networkPublicKeyDetails) {
        if (TextUtils.isEmpty(hostName)) {
            return;
        }

        String storedPublicKeyDetails = hpkpStorageHelper.getStoredPublicKeyDetails(hostName);
        boolean isStoredKeyFound = !TextUtils.isEmpty(storedPublicKeyDetails);
        boolean isNetworkKeyFound = networkPublicKeyDetails.contains("pin-sha256");

        if (!isNetworkKeyFound && isStoredKeyFound) {
            hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK);
            return;
        }

        if (isNetworkKeyFound) {
            HPKPExpirationHelper hpkpExpirationHelper = new HPKPExpirationHelper(storedPublicKeyDetails, networkPublicKeyDetails);

            networkPublicKeyDetails = networkPublicKeyDetails.concat(" " + "expiry-date=\"" + hpkpExpirationHelper.getNetworkPinsExpiryDate() + "\";");

            if (!isStoredKeyFound) {
                updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
                hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE);
            } else {
                updateStoredKeyWithNetworkKey(hostName, networkPublicKeyDetails, storedPublicKeyDetails, hpkpExpirationHelper);
            }
        }
    }

    @Override
    public boolean isPinnedCertificateMatching(String hostName, List<X509Certificate> chain) {
        String storedPublicKeyDetails = hpkpStorageHelper.getStoredPublicKeyDetails(hostName);
        boolean isCertificatePinsMisMatch = true;
        for (X509Certificate certificate : chain) {
            String certificatePin = getSHA256Value(certificate);
            if (certificatePin != null && storedPublicKeyDetails.contains(certificatePin)) {
                isCertificatePinsMisMatch = false;
                break;
            }
        }
        HPKPExpirationHelper hpkpExpirationHelper = new HPKPExpirationHelper(storedPublicKeyDetails, null);
        boolean isPinnedPublicKeyExpired = hpkpExpirationHelper.isPinnedPublicKeyExpired();
        logError(hostName, isCertificatePinsMisMatch, isPinnedPublicKeyExpired);
        return !isCertificatePinsMisMatch && !isPinnedPublicKeyExpired;
    }

    private void updateStoredKeyWithNetworkKey(String hostName, String networkPublicKeyDetails, String storedPublicKeyDetails, HPKPExpirationHelper hpkpExpirationHelper) {
        List<String> publicKeys = getPinnedPublicKeysList(networkPublicKeyDetails);
        for (String publicKey : publicKeys) {
            if (storedPublicKeyDetails.contains(publicKey)) {
                if (hpkpExpirationHelper.shouldExpiryBeUpdated()) {
                    updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
                }
                return;
            }
        }
        updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
        hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER);
    }

    private void logError(String hostName, boolean isCertificatePinsMisMatch, boolean isPinnedPublicKeyExpired) {
        if (isCertificatePinsMisMatch && isPinnedPublicKeyExpired) {
            hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE_EXPIRED);
        } else if (isCertificatePinsMisMatch) {
            hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE);
        } else if (isPinnedPublicKeyExpired) {
            hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_CERTIFICATE_EXPIRED);
        }
    }

    private String getSHA256Value(X509Certificate certificate) {
        // Generate the certificate's spki pin
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
        digest.reset();

        byte[] spki = certificate.getPublicKey().getEncoded();
        byte[] spkiHash = digest.digest(spki);
        return Base64.encodeToString(spkiHash, Base64.DEFAULT).trim();
    }

    private List<String> getPinnedPublicKeysList(String publicKeyDetails) {
        Matcher matcher = publicKeyPattern.matcher(publicKeyDetails);
        List<String> pinnedKeysList = new ArrayList<>();
        while (matcher.find()) {
            pinnedKeysList.add(matcher.group(1));
        }
        return pinnedKeysList;
    }

    private void updateStoredPublicKeyDetails(String hostName, String networkPublicKeyDetails) {
        boolean isSuccess = hpkpStorageHelper.updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
        if (!isSuccess) {
            hpkpLoggingHelper.logDebug(hostName, LOG_MESSAGE_STORAGE_ERROR);
        }
    }
}

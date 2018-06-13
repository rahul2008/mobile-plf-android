package com.philips.platform.appinfra.rest.hpkp;


import android.util.Base64;

import com.philips.platform.appinfra.AppInfraInterface;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.philips.platform.appinfra.rest.hpkp.HPKPExpirationHelper.EXPIRY_DATE_REGEX;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_EXPIRED;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER;
import static com.philips.platform.appinfra.rest.hpkp.HPKPLoggingHelper.LOG_MESSAGE_STORAGE_ERROR;

public class HPKPManager implements HPKPInterface {

    private static final String PUBLIC_KEY_REGEX = "pin-sha256=\"(.+?)\";";
    private HPKPLoggingHelper hpkpLoggingHelper;
    private HPKPStorageHelper hpkpStorageHelper;

    public HPKPManager(AppInfraInterface appInfraInterface) {
        this.hpkpStorageHelper = new HPKPStorageHelper(appInfraInterface.getSecureStorage());
        this.hpkpLoggingHelper = new HPKPLoggingHelper(appInfraInterface.getLogging());
    }

    @Override
    public void updatePinnedPublicKey(String hostName, String networkPublicKeyDetails) {
        String storedPublicKeyDetails = hpkpStorageHelper.getStoredPublicKeyDetails(hostName);
        boolean isKeyFound = networkPublicKeyDetails.contains("pin-sha256");

        if (!hostName.isEmpty()) {
            if (!isKeyFound && !storedPublicKeyDetails.isEmpty()) {
                hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_NETWORK);
            } else if (isKeyFound) {
                HPKPExpirationHelper hpkpExpirationHelper = new HPKPExpirationHelper(storedPublicKeyDetails, networkPublicKeyDetails);

                if (storedPublicKeyDetails.isEmpty()) {
                    updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails.concat(" " + "expiry-date=\"" + hpkpExpirationHelper.getNetworkPinsExpiryDate() + "\";"));
                    hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_NOT_FOUND_STORAGE);
                } else {
                    List<String> publicKeys = getPinnedPublicKeysList(networkPublicKeyDetails);
                    for (String publicKey : publicKeys) {
                        if (storedPublicKeyDetails.contains(publicKey)) {
                            if (hpkpExpirationHelper.isPinnedPublicKeyExpired()) {
                                hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_EXPIRED);
                            } else if (hpkpExpirationHelper.shouldExpiryBeUpdated()) {
                                String updatedStoredKeyDetails = storedPublicKeyDetails.split(EXPIRY_DATE_REGEX)[0];
                                updateStoredPublicKeyDetails(hostName, updatedStoredKeyDetails.concat(" " + "expiry-date=\"" + hpkpExpirationHelper.getNetworkPinsExpiryDate() + "\";"));
                            }
                            return;
                        }
                    }
                    updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
                    hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_HEADER);
                }
            }
        }
    }

    private void updateStoredPublicKeyDetails(String hostName, String networkPublicKeyDetails) {
        boolean isSuccess = hpkpStorageHelper.updateStoredPublicKeyDetails(hostName, networkPublicKeyDetails);
        if (!isSuccess) {
            hpkpLoggingHelper.logDebug(hostName, LOG_MESSAGE_STORAGE_ERROR);
        }
    }

    @Override
    public boolean isPinnedCertificateMatching(String hostName, List<X509Certificate> chain) {
        String storedPublicKeyDetails = hpkpStorageHelper.getStoredPublicKeyDetails(hostName);
        for (X509Certificate certificate : chain) {
            String certificatePin = getSHA256Value(certificate);
            if (certificatePin != null && storedPublicKeyDetails.contains(certificatePin))
                return true;
        }
        hpkpLoggingHelper.logError(hostName, LOG_MESSAGE_PUBLIC_KEY_PIN_MISMATCH_CERTIFICATE);
        return false;
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
        Pattern pattern = Pattern.compile(PUBLIC_KEY_REGEX);
        Matcher matcher = pattern.matcher(publicKeyDetails);
        List<String> pinnedKeysList = new ArrayList<>();
        while (matcher.find()) {
            pinnedKeysList.add(matcher.group(1));
        }
        return pinnedKeysList;
    }
}

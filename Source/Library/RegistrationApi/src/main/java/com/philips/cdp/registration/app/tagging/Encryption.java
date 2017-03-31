package com.philips.cdp.registration.app.tagging;


import android.util.Base64;

import com.philips.cdp.registration.ui.utils.RLog;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Provider;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class Encryption {

    private static final String ALGORITHM = "RSA";

    private static final String PROVIDER = "BC";

    private static final String TRANSFORMATION = "RSA/ECB/OAEPwithSHA-256andMGF1Padding";

    private static final String PUBLIC_KEY =
            "MIIBITANBgkqhkiG9w0BAQEFAAOCAQ4AMIIBCQKCAQBsYW88Po6F2yza8jjJAbbI\n" +
                    "JuAMQRzQbTHrPUcD04iMjlw6hU5KpdYmwTJ5cExMZE43qhUaXSNfhaWiubQRvm45\n" +
                    "V+8Bs8uGlHpKMTAaTnLX6q2RG1SpFiQcqdUVjezwevIpPrgBGunDLqAotqYtpQ0W\n" +
                    "1nZ6HfypIOVeZVS09YFdU++eVRMo8I3NzcgxKIRNU1eD3ObA1kTQktBFsVeR+5Rj\n" +
                    "QWpy/h2HQB4P6ewE50ni2ft6BWvLZePCNLGBGSe9C7ERatlQnZd1AVN/fSpmcn49\n" +
                    "Y7fzMmBLoBnlLlJJ/UVCL/rgZnACs4egofC1pQbAFgMeE0PcNAVTPhvLmln7YICX\n" +
                    "AgMBAAE=";

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
            Provider[] providers = Security.getProviders();
            System.out.println("Providers " + providers);
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

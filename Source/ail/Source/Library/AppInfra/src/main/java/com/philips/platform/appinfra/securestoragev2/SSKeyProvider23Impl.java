package com.philips.platform.appinfra.securestoragev2;

import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfraInterface;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.MGF1ParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import javax.crypto.spec.SecretKeySpec;


/**
 * Created by abhishek on 2/5/18.
 */

public class SSKeyProvider23Impl extends SSKeyProvider {

    private static final String SS_KEY_23_IMPL_ALIAS = "ss_key_23_impl_alias";

    private static final String CIPHER_ALGORITHM = "RSA/ECB/OAEPWithSHA-256AndMGF1Padding";
    public static final String SHA_256 = "SHA-256";
    public static final String MGF_1 = "MGF1";
    public static final String SHA_1 = "SHA-1";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String AES = "AES";

    private SSFileCache ssFileCache;

    private AppInfraInterface appInfra;


    public SSKeyProvider23Impl(AppInfraInterface appInfra, SSFileCache ssFileCache) {
        this.ssFileCache = ssFileCache;
        this.appInfra = appInfra;

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public SecretKey getSecureKey(String keyId) throws SSKeyProviderException {
        String aesWrappedKey = ssFileCache.getRSAWrappedAESKeyFromFileCache(keyId);
        if (TextUtils.isEmpty(aesWrappedKey)) {
            wrapAndSaveAESKey(keyId);
        }
        return unwrapAESKey(ssFileCache.getRSAWrappedAESKeyFromFileCache(keyId));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    protected boolean wrapAndSaveAESKey(String keyId) throws SSKeyProviderException {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            if (!keyStore.containsAlias(SS_KEY_23_IMPL_ALIAS)) {
                KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(
                        KeyProperties.KEY_ALGORITHM_RSA, ANDROID_KEY_STORE);
                keyPairGenerator.initialize(
                        new KeyGenParameterSpec.Builder(
                                SS_KEY_23_IMPL_ALIAS,
                                KeyProperties.PURPOSE_DECRYPT)
                                .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_RSA_OAEP)
                                .setKeySize(2048)
                                .build());
                keyPairGenerator.generateKeyPair();
            }


            PublicKey publicKey = keyStore.getCertificate(SS_KEY_23_IMPL_ALIAS).getPublicKey();
            OAEPParameterSpec sp = new OAEPParameterSpec(SHA_256, MGF_1, new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.WRAP_MODE, publicKey,sp);
            final byte[] AESbytes = cipher.wrap(generateAESKey());  // Wrap AES key using RSA
            ssFileCache.putRSAWrappedAESKeyInFileCache(keyId, Base64.encodeToString(AESbytes, Base64.DEFAULT));
        } catch (GeneralSecurityException e) {
            throw new SSKeyProviderException("Exception while creating key.");
        } catch (IOException e) {
            throw new SSKeyProviderException("Error while loading keystore");
        }
        return true;
    }

    protected SecretKey unwrapAESKey(String wrappedAESKey) throws SSKeyProviderException {
        try {
            KeyStore keyStore = KeyStore.getInstance(ANDROID_KEY_STORE);
            keyStore.load(null);
            PrivateKey privateKey = (PrivateKey) keyStore.getKey(SS_KEY_23_IMPL_ALIAS, null);
            OAEPParameterSpec sp = new OAEPParameterSpec(SHA_256, MGF_1, new MGF1ParameterSpec(SHA_1), PSource.PSpecified.DEFAULT);
            Cipher cipher = Cipher.getInstance(CIPHER_ALGORITHM);
            cipher.init(Cipher.UNWRAP_MODE, privateKey,sp);
            final SecretKey aesKey = (SecretKey) cipher.unwrap(Base64.decode(wrappedAESKey, Base64.DEFAULT), AES, Cipher.SECRET_KEY);// Unwrap AES key using RSA
            return new SecretKeySpec(aesKey.getEncoded(), AES);
        } catch (GeneralSecurityException e) {
            throw new SSKeyProviderException("Exception while creating key.");
        } catch (IOException e) {
            throw new SSKeyProviderException("Error while loading keystore");
        }
    }

}

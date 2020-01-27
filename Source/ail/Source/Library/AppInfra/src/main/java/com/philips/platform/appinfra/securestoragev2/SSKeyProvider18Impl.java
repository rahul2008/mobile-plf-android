package com.philips.platform.appinfra.securestoragev2;

import android.os.Build;
import android.security.KeyPairGeneratorSpec;
import androidx.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Base64;

import com.philips.platform.appinfra.AppInfraInterface;

import java.io.IOException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Calendar;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.x500.X500Principal;

/**
 * Created by abhishek on 2/5/18.
 */

public class SSKeyProvider18Impl extends SSKeyProvider {

    public static final String SS_KEY_18_IMPL_ALIAS = "ss_key_18_impl_alias";

    private static final String CIPHER_ALGORITHM = "RSA/ECB/PKCS1Padding";
    public static final String AES = "AES";
    public static final String ANDROID_KEY_STORE = "AndroidKeyStore";
    public static final String RSA = "RSA";

    private AppInfraInterface appInfra;

    private SSFileCache ssFileCache;

    public SSKeyProvider18Impl(AppInfraInterface appInfra, SSFileCache ssFileCache) {
        this.appInfra = appInfra;
        this.ssFileCache = ssFileCache;
    }

    @Override
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    public SecretKey getSecureKey(String keyId) throws SSKeyProviderException {
            String aesWrappedKey = ssFileCache.getRSAWrappedAESKeyFromFileCache(keyId);
            if (TextUtils.isEmpty(aesWrappedKey)) {
                wrapAndSaveAESKey(keyId);
            }
            return unwrapAESKey(ssFileCache.getRSAWrappedAESKeyFromFileCache(keyId));
    }

    protected boolean wrapAndSaveAESKey(String keyId) throws SSKeyProviderException {
        KeyStore keyStore = null;
        try {
            keyStore = getAndroidKeyStore();
            keyStore.load(null);
            if (!keyStore.containsAlias(SS_KEY_18_IMPL_ALIAS)) {
                // if key is not generated
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 50);

                AlgorithmParameterSpec algorithmParameterSpec;
                algorithmParameterSpec = new KeyPairGeneratorSpec.Builder(appInfra.getAppInfraContext())
                        .setAlias(SS_KEY_18_IMPL_ALIAS)
                        .setSubject(new X500Principal("CN=Secure Storage, O=Philips AppInfra"))
                        .setSerialNumber(BigInteger.ONE)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();

                final KeyPairGenerator generator = KeyPairGenerator.getInstance(RSA, ANDROID_KEY_STORE);
                generator.initialize(algorithmParameterSpec);
                generator.generateKeyPair();
            }
            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(SS_KEY_18_IMPL_ALIAS, null);
            final RSAPublicKey publicKey = (RSAPublicKey) privateKeyEntry.getCertificate().getPublicKey();
            final Cipher input = Cipher.getInstance(CIPHER_ALGORITHM);
            input.init(Cipher.WRAP_MODE, publicKey);
            final byte[] AESbytes = input.wrap(generateAESKey());  // Wrap AES key using RSA
            ssFileCache.putRSAWrappedAESKeyInFileCache(keyId,Base64.encodeToString(AESbytes, Base64.DEFAULT));
        } catch (GeneralSecurityException e) {
            throw  new SSKeyProviderException("Exception while creating key.");

        } catch (IOException e) {
            throw new SSKeyProviderException("Error while loading keystore");
        }

        return true;
    }

    protected SecretKey unwrapAESKey(String wrappedAESKey) throws SSKeyProviderException{
        try {
            KeyStore keyStore = getAndroidKeyStore();
            keyStore.load(null);
            final KeyStore.PrivateKeyEntry privateKeyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(SS_KEY_18_IMPL_ALIAS, null);
            final Cipher output = Cipher.getInstance(CIPHER_ALGORITHM);
            output.init(Cipher.UNWRAP_MODE, privateKeyEntry.getPrivateKey());
            final SecretKey aesKey = (SecretKey) output.unwrap(Base64.decode(wrappedAESKey, Base64.DEFAULT), AES, Cipher.SECRET_KEY);// Unwrap AES key using RSA
            return new SecretKeySpec(aesKey.getEncoded(), AES);
        }catch (GeneralSecurityException e){
            throw  new SSKeyProviderException("Error while unwrapping key");
        } catch (IOException e) {
            throw new SSKeyProviderException("Error while loading keystore");
        }
    }

    protected KeyStore getAndroidKeyStore() throws KeyStoreException {
        return KeyStore.getInstance(ANDROID_KEY_STORE);
    }


}

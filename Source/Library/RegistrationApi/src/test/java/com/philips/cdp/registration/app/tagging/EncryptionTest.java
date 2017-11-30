package com.philips.cdp.registration.app.tagging;

import android.util.*;

import com.philips.cdp.registration.BuildConfig;
import com.philips.cdp.registration.CustomRobolectricRunner;
import com.philips.cdp.registration.ui.utils.*;
import com.philips.platform.appinfra.logging.*;

import junit.framework.*;

import org.junit.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.robolectric.annotation.*;

import java.nio.charset.*;
import java.security.*;
import java.security.spec.*;

import javax.crypto.*;

@RunWith(CustomRobolectricRunner.class)
@org.robolectric.annotation.Config(constants = BuildConfig.class, sdk = 21)
public class EncryptionTest extends TestCase {
    @Mock
    LoggingInterface mockLoggingInterface;

    private Encryption encryption;

    private final static String PRIVATE_KEY =
            "MIIEogIBAAKCAQBsYW88Po6F2yza8jjJAbbIJuAMQRzQbTHrPUcD04iMjlw6hU5K\n" +
                    "pdYmwTJ5cExMZE43qhUaXSNfhaWiubQRvm45V+8Bs8uGlHpKMTAaTnLX6q2RG1Sp\n" +
                    "FiQcqdUVjezwevIpPrgBGunDLqAotqYtpQ0W1nZ6HfypIOVeZVS09YFdU++eVRMo\n" +
                    "8I3NzcgxKIRNU1eD3ObA1kTQktBFsVeR+5RjQWpy/h2HQB4P6ewE50ni2ft6BWvL\n" +
                    "ZePCNLGBGSe9C7ERatlQnZd1AVN/fSpmcn49Y7fzMmBLoBnlLlJJ/UVCL/rgZnAC\n" +
                    "s4egofC1pQbAFgMeE0PcNAVTPhvLmln7YICXAgMBAAECggEAS0chy+Znd/HUJf84\n" +
                    "EeVu33ahB+Hx8ZLmqU3WlYHHxljbFoqg5phpNmHafR7S3/yLl347SiSG/UIG2g/C\n" +
                    "UtDCHnJFQBlxs/hNOFsR3jO+8Nm/1Jn3I7qNOpt7tYzuldnPeXZmt93JoBgyhhTX\n" +
                    "N7z8anBrukY5x/9hUT9wpoAfaoKKXfXu+3q0EMdLqArE72tUETGcWhtwNpL5vCa0\n" +
                    "elauedvtZP8/WAu/4yCMd/XB4SIed9QdrS6CTt5jOpSJ1mnruTVQwDKWPYs7q9Q0\n" +
                    "rtMi5CT0ChEf8pNLsfhftVaKHC+XWHi0trxOcWp5y2fAF4UKCEqaRfa3KqdjONQi\n" +
                    "TtimgQKBgQCzCSk/x+cClHGdM/qgMXrNtYhSFhLHgPVYnQI5dWVgUmg7dBEW8NC3\n" +
                    "R26hN9NHss1VkWdk4gmMMjHHi0i1cLBTh5pis+mP9bxvtHDgpuqKmll52PQrhpJC\n" +
                    "CfC8Rc5NIQ1wYR+cNCs5fYU1/Y24Jz0YEGVE4Aieg9Es9oLhjXfqFQKBgQCa+LCo\n" +
                    "LfZ4WmjgIJigkI00EoFQ2CkzmqR7+6U3TQZFm/4gJLzFTzJZSsYR4mQ3sEB67lyU\n" +
                    "LTqZ+/yDa3ey7x0s7iONE6Qn+uWL7jtYlAaMHbtfOLfOWVOt5R2h/zMFhYAxXoXU\n" +
                    "tbEJw2kmwUK0MPqQGKGnbc/QzUYplvZiXeuG+wKBgCOXGqAXOREGOV+qyZO2wlbK\n" +
                    "U4Md6DXIqP0omRya/cAcIaRP+mW5EYSdqpxZ4KJMJQ1xjkA7vvIjuEmWoqmO8T5p\n" +
                    "ooMkiLWZgBAkqpu+Erni3Q6f1kogwkpmd3i118D1ZRqF9Ca1KDKRhT2qXdXR1w9F\n" +
                    "kxwR/SSlx5tMldAIs2HRAoGAW+sWHSrl/gziF849uT8EAdbYDPOpvIoE9eUU4fA1\n" +
                    "wCP+X2Jq9T08R+oqAYGen8lS9ZdcIieRkrP72pdrsB5T18qIG62CuJQAgzcSzD9A\n" +
                    "gus/sAMcQrCJCaPUJ7oOsT+2AZJnHvuFvpzBSdNnlSlX9RAnEf/4O4kGDLUV/tJm\n" +
                    "WPECgYEAm6jnOev4sqWPacnzTRVMp04OmFmwfC+lL6DbP/dEQACM35ryFMZhJhzZ\n" +
                    "129g9LxAwJ0yWOyXmkC2zWs8TA1CMlfwlEmBBHdUV6RTHpZQyC6lZOSwFAJ3osnN\n" +
                    "owojCm6G8uxkkC3GJjrYgZBf6I/Gk5coQ90hjIsIxa0WIQ+NhJ4=";


    private static final String ALGORITHM = "RSA";
    private static final String PROVIDER = "BC";
    private static final String TRANSFORMATION = "RSA/ECB/OAEPwithSHA-1andMGF1Padding";



    private PrivateKey getPrivateKey(String privateKey) {
        PrivateKey key = null;
        byte[] keyBytes = new byte[0];
        try {
            keyBytes = Base64.decode(privateKey.getBytes(StandardCharsets.UTF_8), Base64.DEFAULT);
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory fact = KeyFactory.getInstance(ALGORITHM, PROVIDER);
            key = fact.generatePrivate(keySpec);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException |
                NoSuchProviderException expection) {
            RLog.e("Exception", expection.toString());
        }
        return key;
    }

    private String decrypt(String toBeDecrypted) {
        try {
            byte[] toBeDecryptedBytes = toBeDecrypted.getBytes(StandardCharsets.UTF_8);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION, PROVIDER);
            byte[] base64DecodedBytes = Base64.decode(toBeDecryptedBytes, Base64.DEFAULT);
            cipher.init(Cipher.DECRYPT_MODE, getPrivateKey(PRIVATE_KEY));
            byte[] plainTextBytes = cipher.doFinal(base64DecodedBytes);
            return new String(plainTextBytes);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException |
                BadPaddingException | IllegalBlockSizeException |
                InvalidKeyException | NoSuchProviderException expection) {
            RLog.e("Exception", expection.toString());
        }
        return null;
    }

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        RLog.setMockLogger(mockLoggingInterface);
        encryption = new Encryption();

    }

    @After
    public void tearDown() throws Exception {
        encryption = null;
    }

    @Test
    public void testEncrypt_NullInput() throws Exception {
        String toBeEncrypted = null;
        String encrpytedValue = encryption.encrypt(toBeEncrypted);
        assertEquals("", encrpytedValue);
    }


    @Test
    public void testEncrypt_EmptyInput() throws Exception {
        String toBeEncrypted = "";
        String encrpytedValue = encryption.encrypt(toBeEncrypted);
        assertEquals("", encrpytedValue);
    }

    @Test
    public void testEncrypt_StringInput() throws Exception {
        String toBeEncrypted = "Test";
        String encrpytedValue = encryption.encrypt(toBeEncrypted);
        String expectedValue = "E7c+hUXGWdLdMIC1ZVh5gd1isbYx34VV55mQ/MLjtP8UWIBOyVgVNm2KoaL6n5MTmgQS007wuAjpRee9sJqp/ogldFxPRYEHz13M4Er8C5lfuLOW/RAQcsqRZTSha+5Gckv+Ecc0Aew3aSxNzA9ZzZrZ+sv/sM3FGah1PfgsFaujk4ibMDSi4v43IR445DFFvn7jeWtgjAa4aBcHmwMVzrGdHChErSHCnBzcO3O3zD6wbZstvzkacsEdLAeaaF12fLSw9Fzlt6vf/fYoYVfr+PJdpAYHXKE/jnYgLzKYeqPxovsu4eOamXgDqUrXpf+AWNOSl5kOQYQe3ANZkIREVA==\n" +
                "encodedE7c+hUXGWdLdMIC1ZVh5gd1isbYx34VV55mQ/MLjtP8UWIBOyVgVNm2KoaL6n5MTmgQS007wuAjpRee9sJqp/ogldFxPRYEHz13M4Er8C5lfuLOW/RAQcsqRZTSha+5Gckv+Ecc0Aew3aSxNzA9ZzZrZ\n" +
                "+sv/sM3FGah1PfgsFaujk4ibMDSi4v43IR445DFFvn7jeWtgjAa4aBcHmwMVzrGdHChErSHCnBzcO3O3zD6wbZstvzkacsEdLAeaaF12fLSw9Fzlt6vf/fYoYVfr+PJdpAYHXKE/jnYgLzKYeqPxovsu\n" +
                "4eOamXgDqUrXpf+AWNOSl5kOQYQe3ANZkIREVA==";
        assertEquals(decrypt(expectedValue), encrpytedValue);
    }
}

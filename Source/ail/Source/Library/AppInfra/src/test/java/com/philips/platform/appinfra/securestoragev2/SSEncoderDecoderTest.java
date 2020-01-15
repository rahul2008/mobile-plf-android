package com.philips.platform.appinfra.securestoragev2;

import android.util.Base64;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.nio.ByteBuffer;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

/**
 * Created by abhishek on 2/6/18.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Cipher.class, Base64.class})
public class SSEncoderDecoderTest {

    Cipher cipher;

    @Mock
    SecretKey secretKey;


    @Before
    public void setUp()throws NoSuchPaddingException, NoSuchAlgorithmException, BadPaddingException, IllegalBlockSizeException, SSEncodeDecodeException{
        mockStatic(Cipher.class,Base64.class,ByteBuffer.class);
        cipher=PowerMockito.mock(Cipher.class);
        MockitoAnnotations.initMocks(this);
        PowerMockito.when(Cipher.getInstance(any(String.class))).thenReturn(cipher);
    }



    //TODO:Need to refactor code to add version info in decodeBytes
//    @Test
//    public void testEncodeDecodeData_String_Decryption_Mode() throws GeneralSecurityException, SSEncodeDecodeException  {
//        byte[] decodeBytes={0,0,0,12,3,60,-86,90,-3,-73,66,7,110,116,-30,71,92,-4,67,26,79,-118,37,17,-52,113,-121,123,-96,-62,86,81,117,81,81,-5};
//        PowerMockito.when(Base64.decode(any(String.class),anyInt())).thenReturn(decodeBytes);
//        byte[] decryptedBytes=new String("abcd").getBytes();
//        PowerMockito.when(cipher.doFinal(any(byte[].class))).thenReturn(decryptedBytes);
//        assertEquals("abcd",new SSEncoderDecoder().encodeDecodeData(Cipher.DECRYPT_MODE,secretKey,"AAAADAM8qlr9t0IHbnTiR1z8QxpPiiURzHGHe6DCVlF1UVH7\n"));
//    }

    @Test
    public void testEncodeDecodeData_Bytes_Encryption_Mode() throws GeneralSecurityException, SSEncodeDecodeException {
        byte[] encrptedBytes=new String("dcba").getBytes();
        PowerMockito.when(cipher.doFinal(any(byte[].class))).thenReturn(encrptedBytes);
        PowerMockito.when(Base64.encodeToString(any(byte[].class),anyInt())).thenReturn("dcba");
        assertNotNull(new SSEncoderDecoder().encodeDecodeData(Cipher.ENCRYPT_MODE,secretKey,"abcd".getBytes()));
    }

    //TODO:Need to refactor code to add version info in decodeBytes
//    @Test
//    public void testEncodeDecodeData_Bytes_Decryption_Mode() throws GeneralSecurityException, SSEncodeDecodeException  {
//        byte[] decodeBytes={0,0,0,12,3,60,-86,90,-3,-73,66,7,110,116,-30,71,92,-4,67,26,79,-118,37,17,-52,113,-121,123,-96,-62,86,81,117,81,81,-5};
//        PowerMockito.when(Base64.decode(any(String.class),anyInt())).thenReturn(decodeBytes);
//        byte[] decryptedBytes=new String("abcd").getBytes();
//        PowerMockito.when(cipher.doFinal(any(byte[].class))).thenReturn(decryptedBytes);
//        assertNotNull(new SSEncoderDecoder().encodeDecodeData(Cipher.DECRYPT_MODE,secretKey,decodeBytes));
//    }

    @Test(expected = Throwable.class)
    public void testEncodeDecodeData_Bytes_Decryption_Mode_exception_case() throws GeneralSecurityException, SSEncodeDecodeException  {
        byte[] decodeBytes={0,0,0,12,3,60,-86,90,-3,-73,66,7,110,116,-30,71,92,-4,67,26,79,-118,37,17,-52,113,-121,123,-96,-62,86,81,117,81,81,-5};
        PowerMockito.when(Base64.decode(any(String.class),anyInt())).thenReturn(decodeBytes);
        byte[] decryptedBytes=new String("abcd").getBytes();
        PowerMockito.when(cipher.doFinal(any(byte[].class))).thenThrow(new IllegalArgumentException());
        assertNotNull(new SSEncoderDecoder().encodeDecodeData(Cipher.DECRYPT_MODE,secretKey,decodeBytes));
    }

    @Test(expected = Throwable.class)
    public void testEncodeDecodeData_Bytes_Decryption_Mode_GSE_exception_case() throws GeneralSecurityException, SSEncodeDecodeException  {
        byte[] decodeBytes={0,0,0,12,3,60,-86,90,-3,-73,66,7,110,116,-30,71,92,-4,67,26,79,-118,37,17,-52,113,-121,123,-96,-62,86,81,117,81,81,-5};
        PowerMockito.when(Base64.decode(any(String.class),anyInt())).thenReturn(decodeBytes);
        byte[] decryptedBytes=new String("abcd").getBytes();
        PowerMockito.when(cipher.doFinal(any(byte[].class))).thenThrow(new IllegalBlockSizeException());
        assertNotNull(new SSEncoderDecoder().encodeDecodeData(Cipher.DECRYPT_MODE,secretKey,decodeBytes));
    }
}
package com.philips.cdp.dicommclient.security;

import java.nio.charset.Charset;

import com.philips.cdp.dicommclient.networknode.NetworkNode;
import com.philips.cdp.dicommclient.util.DLog;

public class DISecurity {

    public interface EncryptionDecryptionFailedListener{
        void onDecryptionFailed(NetworkNode networkNode);
        void onEncryptionFailed(NetworkNode networkNode);
    }

	private EncryptionDecryptionFailedListener mEncryptionDecryptionFailedListener;

    public void setEncryptionDecryptionFailedListener(EncryptionDecryptionFailedListener encryptionDecryptionFailedListener) {
        this.mEncryptionDecryptionFailedListener = encryptionDecryptionFailedListener;
    }

    public String encryptData(String data, NetworkNode networkNode) {
        if (networkNode == null) {
            DLog.i(DLog.SECURITY, "Did not encrypt data - NetworkNode is null");
            return null;
        }

        String key = networkNode.getEncryptionKey();
        if (key == null || key.isEmpty()) {
            DLog.i(DLog.SECURITY, "Did not encrypt data - Key is null or Empty");
            return null;
        }
        if (data == null || data.isEmpty()) {
            DLog.i(DLog.SECURITY, "Did not encrypt data - Data is null or Empty");
            return null;
        }

        String encryptedBase64Str = null;
        try {
            byte[] encrypDatas = EncryptionUtil.aesEncryptData(data, key);
            encryptedBase64Str = ByteUtil.encodeToBase64(encrypDatas);
            DLog.i(DLog.SECURITY, "Encrypted data: " + encryptedBase64Str);
        } catch (Exception e) {
            e.printStackTrace();
            DLog.i(DLog.SECURITY, "Failed to encrypt data with key - " + "Error: " + e.getMessage());
        }
        return encryptedBase64Str;
    }

    public String decryptData(String data, NetworkNode networkNode) {
        DLog.i(DLog.SECURITY, "decryptData data:  "+data) ;


        if (networkNode == null) {
            DLog.i(DLog.SECURITY, "Did not encrypt data - NetworkNode is null");
            return null;
        }
        String key = networkNode.getEncryptionKey();
        DLog.i(DLog.SECURITY, "Decryption - Key   " + key);
        String decryptData = null;

        if (data == null || data.isEmpty()) {
            DLog.i(DLog.SECURITY, "Did not decrypt data - data is null");
            return null;
        }

        if (key == null || key.isEmpty()) {
            DLog.i(DLog.SECURITY, "Did not decrypt data - key is null");
            DLog.i(DLog.SECURITY, "Failed to decrypt data");

            notifyDecryptionFailedListener(networkNode);
            return null;
        }

        data = data.trim() ;

        try {
            byte[] bytesEncData = ByteUtil.decodeFromBase64(data.trim());
            byte[] bytesDecData = EncryptionUtil.aesDecryptData(bytesEncData, key);
            //For remove random bytes
            byte[] bytesDecData1 = ByteUtil.removeRandomBytes(bytesDecData);

            decryptData = new String(bytesDecData1, Charset.defaultCharset());

            DLog.i(DLog.SECURITY, "Decrypted data: " + decryptData);
        } catch (Exception e) {
            e.printStackTrace();
            DLog.i(DLog.SECURITY, "Failed to decrypt data");
        }

        if(decryptData == null){
            notifyDecryptionFailedListener(networkNode);
        }

        return decryptData;
    }

    private void notifyDecryptionFailedListener(NetworkNode networkNode) {
        if(mEncryptionDecryptionFailedListener!=null){
            mEncryptionDecryptionFailedListener.onDecryptionFailed(networkNode);
        }
    }

    public void notifyEncryptionFailedListener(NetworkNode networkNode) {
    	if(mEncryptionDecryptionFailedListener!=null){
    		mEncryptionDecryptionFailedListener.onEncryptionFailed(networkNode);
    	}
    }
}

/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */
package com.philips.cdp.registration.datamigration;

import android.content.Context;

import com.janrain.android.Jump;
import com.janrain.android.utils.ThreadUtils;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.security.SecureStorage;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

import static com.janrain.android.utils.LogUtils.throwDebugException;


public class DataMigration {

    private static final String JR_CAPTURE_SIGNED_IN_USER = "jr_capture_signed_in_user";
    private static final String HSDP_RECORD = "hsdpRecord";
    private static final String DI_PROFILE = "diProfile";
    private static final String JUMP_REFRESH_SECRET = "jr_capture_refresh_secret";
    private final Context mContext;

    public DataMigration(final Context context) {
        mContext = context;
    }

    private void migrateFileData(final String fileName) {
        try {
            final String  plainText = readDataAndDeleteFile(fileName);
            writeDataToFile(fileName, plainText);
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }
    }

    private String readDataAndDeleteFile(String fileName) throws IOException, ClassNotFoundException {
        final FileInputStream fis = mContext.openFileInput(fileName);
        final ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        String plainText = null;
        HsdpUserRecord hsdpUserRecord = null;
        DIUserProfile diUserProfile = null;
        if (object instanceof HsdpUserRecord) {
            hsdpUserRecord = (HsdpUserRecord) object;
            plainText = SecureStorage.objectToString(hsdpUserRecord);
        }
        if (object instanceof DIUserProfile) {
            diUserProfile = (DIUserProfile) object;
            plainText = SecureStorage.objectToString(diUserProfile);
        }
        if (object instanceof String) {
            plainText = (String) object;
        }

        mContext.deleteFile(fileName);
        fis.close();
        ois.close();
        return plainText;
    }

    private void writeDataToFile(final String fileName, String plainTextString) throws IOException {
//        FileOutputStream fos = mContext.openFileOutput(fileName, 0);
//        ObjectOutputStream oos = new ObjectOutputStream(fos);
//        byte[] ectext = null;
//
//        if (plainTextString != null) {
//            ectext = SecureStorage.encrypt(plainTextString);
//        }
//        oos.writeObject(ectext);
//        oos.close();
//        fos.close();
        Jump.getSecureStorageInterface().storeValueForKey(fileName,plainTextString,
                new SecureStorageInterface.SecureStorageError());
        ThreadUtils.executeInBg(new Runnable() {
            public void run() {
                try {
                    mContext.deleteFile(fileName);
                }  catch (Exception e) {
                    throwDebugException(new RuntimeException(e));
                }

            }
        });
    }


    public void checkFileEncryptionStatus() {
        if (!isFileEncryptionDone(JR_CAPTURE_SIGNED_IN_USER)) {
            SecureStorage.migrateUserData(mContext,JR_CAPTURE_SIGNED_IN_USER);
        }

        if (!isFileEncryptionDone(HSDP_RECORD)) {
            migrateFileData(HSDP_RECORD);
        }

        if (!isFileEncryptionDone(DI_PROFILE)) {
            migrateFileData(DI_PROFILE);
        }
        if (!isFileEncryptionDone(JUMP_REFRESH_SECRET)) {
            migrateFileData(JUMP_REFRESH_SECRET);
        }

    }


    private boolean isFileEncryptionDone(final String pFileName) {
        FileInputStream fis = null;
        boolean isEncryptionDone = true;
        try {
            fis = mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object plainText = ois.readObject();
            if (JR_CAPTURE_SIGNED_IN_USER.equals(pFileName)) {
                byte[] sss = (byte[]) plainText;
                String s = new String(sss);
                if (s.contains("access")) {
                    isEncryptionDone = false;
                }

            } else {
                if (plainText instanceof HsdpUserRecord) {
                    isEncryptionDone = false;
                }
                if (plainText instanceof DIUserProfile) {
                    isEncryptionDone = false;
                }
                if (plainText instanceof String) {
                    isEncryptionDone = false;
                }
            }

        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
        }

        return isEncryptionDone;
    }
}

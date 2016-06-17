/*
 *  Copyright (c) Koninklijke Philips N.V., 2016
 *  All rights are reserved. Reproduction or dissemination
 *  * in whole or in part is prohibited without the prior written
 *  * consent of the copyright holder.
 * /
 */


package com.philips.cdp.registration.datamigration;

import android.content.Context;

import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.security.SecurityHelper;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.securestorage.SecureStorageInterface;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;


public class DataMigration {

    private  final String JR_CAPTURE_SIGNED_IN_USER = "jr_capture_signed_in_user";
    private  final String HSDP_RECORD = "hsdpRecord";
    private  final String DI_PROFILE = "diProfile";
    private  final String JUMP_REFRESH_SECRET = "jr_capture_refresh_secret";
    private Context mContext;

    public DataMigration(final Context context) {
        mContext = context;
    }


    private String readDataAndDeleteFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = mContext.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        String plainText = null;
        HsdpUserRecord hsdpUserRecord = null;
        DIUserProfile diUserProfile = null;
        if (object instanceof HsdpUserRecord) {
            hsdpUserRecord = (HsdpUserRecord) object;
            plainText = SecurityHelper.objectToString(hsdpUserRecord);
        }
        if (object instanceof DIUserProfile) {
            diUserProfile = (DIUserProfile) object;
            plainText = SecurityHelper.objectToString(diUserProfile);
        }
        if(object instanceof String){
            plainText = (String)object;
        }

        mContext.deleteFile(fileName);
        fis.close();
        ois.close();
        return plainText;
    }

    private String readEncryptedDataAndDeleteFile(String fileName) throws IOException, ClassNotFoundException {
        FileInputStream fis = mContext.openFileInput(fileName);
        ObjectInputStream ois = new ObjectInputStream(fis);
        Object object = ois.readObject();
        byte[] plainText = null;
        plainText = (byte[])object;
        String data = new String(SecurityHelper.decrypt(plainText));
        mContext.deleteFile(fileName);
        fis.close();
        ois.close();
        return data;
    }





    public void checkFileEncryptionStatus() {
        if (!isFileEncryptionForJumpDone(JR_CAPTURE_SIGNED_IN_USER)) {
            deleteAllFiles();
        }else{
            migrateEncryptedText(JR_CAPTURE_SIGNED_IN_USER);
        }

        if (isFileEncryptionDone(HSDP_RECORD)) {
            migrateEncryptedObject(HSDP_RECORD);
        }

        if (isFileEncryptionDone(DI_PROFILE)) {
            migrateEncryptedObject(DI_PROFILE);
        }
        if(isFileEncryptionDone(JUMP_REFRESH_SECRET)){
            migrateEncryptedObject(JUMP_REFRESH_SECRET);
        }

    }

    private void deleteAllFiles() {
        mContext.deleteFile(JR_CAPTURE_SIGNED_IN_USER);
        mContext.deleteFile(HSDP_RECORD);
        mContext.deleteFile(DI_PROFILE);
        mContext.deleteFile(JUMP_REFRESH_SECRET);
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
                if(plainText instanceof String){
                    isEncryptionDone = false;
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isEncryptionDone;
    }

    private boolean isFileEncryptionForJumpDone(final String pFileName) {
        FileInputStream fis = null;
        boolean isEncryptionDone = true;
        try {
            fis = mContext.openFileInput(pFileName);
            byte fileContent[] = new byte[(int) fis.getChannel().size()];
            fis.read(fileContent);

            if (JR_CAPTURE_SIGNED_IN_USER.equals(pFileName)) {
                String s = new String(fileContent);
                if (s.contains("access")) {
                    isEncryptionDone = false;
                }

            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }  catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return isEncryptionDone;
    }





    //meant to migrate unencrypted data to encrypted one
    private  void migrateEncryptedText(final String pFileName){

        try {
            //Read from file
            FileInputStream fis = mContext.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            byte[] plainBytes = null;
            if(object instanceof byte[]){
                plainBytes = (byte[])object;
            }
            mContext.deleteFile(pFileName);
            fis.close();
            ois.close();

            SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
            if(plainBytes != null){
                secureStorageInterface.storeValueForKey(pFileName,new String(SecurityHelper.decrypt(plainBytes)));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void migratePlainObject(final String fileName) {
        try {
            String plainText = readDataAndDeleteFile(fileName);
            if (plainText != null) {
                SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
                secureStorageInterface.storeValueForKey(fileName,plainText);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void migrateEncryptedObject(final String fileName) {
        try {
            String plainText = readEncryptedDataAndDeleteFile(fileName);
            if (plainText != null) {
                SecureStorageInterface secureStorageInterface = new AppInfra.Builder().build(mContext).getSecureStorage();
                secureStorageInterface.storeValueForKey(fileName,plainText);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

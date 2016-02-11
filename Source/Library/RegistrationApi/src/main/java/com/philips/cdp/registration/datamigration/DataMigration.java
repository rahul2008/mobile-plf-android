package com.philips.cdp.registration.datamigration;

import android.content.Context;

import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.hsdp.HsdpUserRecord;
import com.philips.cdp.security.SecureStorage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

/**
 * Created by 310202337 on 2/11/2016.
 */
public class DataMigration {

    private Context context;

    public DataMigration(final Context context){
        this.context = context;
    }



    private  void migrateFileData(final String fileName) {

        try {
            //Read from file

            FileInputStream fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object object = ois.readObject();
            String plainTextString;
            HsdpUserRecord hsdpUserRecord;
            DIUserProfile diUserProfile;
            if (object instanceof HsdpUserRecord) {

                hsdpUserRecord = (HsdpUserRecord) object;
                plainTextString = SecureStorage.objectToString(hsdpUserRecord);
            }
            if (object instanceof DIUserProfile) {
                diUserProfile = (DIUserProfile) object;
                plainTextString = SecureStorage.objectToString(diUserProfile);
            }


            context.deleteFile(fileName);
            fis.close();
            ois.close();

            //Encrypt the contents of file
            FileOutputStream fos = context.openFileOutput(fileName, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            byte[] ectext = null;

            if (plainTextString != null) {
                ectext = SecureStorage.encrypt(plainTextString);
            }


            oos.writeObject(ectext);
            oos.close();
            fos.close();
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


    public void checkFileEncryptionStatus(){
        if(!isFileEncryptionDone("jr_capture_signed_in_user")){
            SecureStorage.migrateUserData("jr_capture_signed_in_user");
        }

        if(!isFileEncryptionDone("hsdpRecord")){
            migrateFileData("hsdpRecord");
            //SecureUtility.migrateUserData("hsdpRecord");

        }

        if(!isFileEncryptionDone("diProfile")){
            migrateFileData("diProfile");
            // SecureUtility.migrateUserData("diProfile");
        }
    }


    private  boolean isFileEncryptionDone(final String pFileName ){
        FileInputStream fis = null;
        boolean isEncryptionDone = true;
        try {
            fis =context.openFileInput(pFileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            Object plainTextString =  ois.readObject();
            if("jr_capture_signed_in_user".equals(pFileName)){
                byte[] sss = (byte[])plainTextString;
                String s = new String(sss);
                if(s.contains("access")){
                    isEncryptionDone = false;
                }

            }else{
                if(plainTextString instanceof HsdpUserRecord){
                    isEncryptionDone = false;
                }if(plainTextString instanceof DIUserProfile){
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
}

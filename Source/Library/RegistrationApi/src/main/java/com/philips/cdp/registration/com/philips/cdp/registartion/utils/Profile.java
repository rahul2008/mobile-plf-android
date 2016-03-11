package com.philips.cdp.registration.com.philips.cdp.registartion.utils;

import android.content.Context;

import com.janrain.android.capture.CaptureRecord;
import com.janrain.android.engage.session.JRSession;
import com.philips.cdp.registration.coppa.CoppaConfiguration;
import com.philips.cdp.registration.dao.DIUserProfile;
import com.philips.cdp.registration.hsdp.HsdpUser;
import com.philips.cdp.security.SecureStorage;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;


public class Profile {

    private final String DI_PROFILE_FILE = "diProfile";

    private Context mContext;

    public Profile(Context context) {
        mContext = context;
    }

    public void saveDIUserProfileToDisk(DIUserProfile diUserProfile) {
        try {
            diUserProfile.setPassword(null);
            FileOutputStream fos = mContext.openFileOutput(DI_PROFILE_FILE, 0);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            String objectPlainString = SecureStorage.objectToString(diUserProfile);
            byte[] ectext = SecureStorage.encrypt(objectPlainString);
            oos.writeObject(ectext);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public DIUserProfile getDIUserProfileFromDisk() {
        DIUserProfile diUserProfile = null;
        try {
            FileInputStream fis = mContext.openFileInput(DI_PROFILE_FILE);
            ObjectInputStream ois = new ObjectInputStream(fis);
            byte[] enctText = (byte[]) ois.readObject();
            byte[] decrtext = SecureStorage.decrypt(enctText);
            diUserProfile = (DIUserProfile) SecureStorage.stringToObject(new String(decrtext));
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diUserProfile;
    }


    public void clearData() {
        HsdpUser hsdpUser = new HsdpUser(mContext);
        hsdpUser.deleteFromDisk();
        mContext.deleteFile(DI_PROFILE_FILE);
        CoppaConfiguration.clearConfiguration();

        if (JRSession.getInstance() != null) {
            JRSession.getInstance().signOutAllAuthenticatedUsers();
        }
        CaptureRecord.deleteFromDisk(mContext);

    }

}

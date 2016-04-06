package com.philips.cdp.prodreg.backend;

import android.content.Context;
import android.util.Log;

import com.philips.cdp.registration.dao.DIUserProfile;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class CacheHandler {

    private Context mContext;
    private String TAG = getClass() + "";

    public CacheHandler(final Context context) {
        mContext = context;
    }

    public void cacheProductsToRegister(final Product product, final DIUserProfile userInstance) {
        cacheObject(product, getInternalCacheForProductToRegister(userInstance, product));
    }

    private File getInternalCacheForProductToRegister(final DIUserProfile diUserProfile, final Product product) {
        String basePath = getBasePath();
        String mUuid = "/" + getUUID(diUserProfile);
        String mCtn = product.getCtn() + "_";
        String mSerialNumber = getSerialNumber(product);
        if (mUuid.equals("/nouuid/")) {
            createFolder(basePath + mUuid);
            return createFileIfNotCreated(new File(basePath + mUuid, mCtn + mSerialNumber));
        } else {
            return createFileIfNotCreated(new File(basePath, mUuid + mCtn + mSerialNumber));
        }
    }

    private void createFolder(final String fileName) {
        final File file = new File(fileName);
        if (!file.exists())
            file.mkdirs();
    }

    private String getBasePath() {
        String s = "/productstoregister";
        return mContext.getExternalCacheDir() + s;
    }

    private String getSerialNumber(final Product product) {
        if (product.getSerialNumber() == null || product.getSerialNumber().length() == 0)
            return "NO_SERIAL";
        else
            return product.getSerialNumber();
    }

    private String getUUID(final DIUserProfile diUserProfile) {
        String uuid = null;
        if (diUserProfile != null) {
            if (diUserProfile.getHsdpUUID() != null && diUserProfile.getHsdpUUID().length() != 0)
                uuid = diUserProfile.getHsdpUUID() + "_";
            else if (diUserProfile.getJanrainUUID() != null && diUserProfile.getJanrainUUID().length() != 0)
                uuid = diUserProfile.getJanrainUUID() + "_";
            else
                uuid = "nouuid" + "/";
        } else
            uuid = "nouuid" + "/";
        return uuid;
    }

    private void cacheObject(final Object object, final File file) {
        createFileIfNotCreated(file);
        ObjectOutput out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(file));
            out.writeObject(object);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private File createFileIfNotCreated(final File file) {
        if (!file.exists())
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        return file;
    }

    private void readCachedFile() {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(new File(new File(mContext.getCacheDir(), "") + "cacheFile.srl")));
            Product product = (Product) in.readObject();
            Log.d(TAG, product.getCtn());
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}

package com.philips.platform.flowmanager;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class JSONHelper {

    //Object to hold current context
    final private Context mContext;

    /**
     * Constructor for 'JSONHelper' class.
     *
     * @param context The context to use.  Usually your {@link android.app.Application}
     *                or {@link android.app.Activity} object.
     */
    public JSONHelper(Context context) {
        mContext = context;
    }

    public String getJsonForAppFlow(int localFilePath) {
        String appFlowResponse;
        appFlowResponse = readJsonFromFile(localFilePath, mContext);
        return appFlowResponse;
    }

    /**
     * Method to get the String from string resource using resource Id.
     *
     * @param context          The context to use.  Usually your {@link android.app.Application}
     *                         or {@link android.app.Activity} object.
     * @param stringResourceId Resource Id of the String.
     * @return String as per Resource Id
     */
    private String getStringFromResource(Context context, int stringResourceId) {
        String str;
        str = context.getString(stringResourceId);
        return str;
    }

    /**
     * This method will read the Json string form the given local path.
     *
     * @param localFilePathResourceId Resource ID of local Json file path.
     * @param context                 The context to use.  Usually your {@link android.app.Application}
     *                                or {@link android.app.Activity} object.
     * @return Local Json file data as String.
     */
    public String readJsonFromFile(int localFilePathResourceId, Context context) {
        final String fileName = getStringFromResource(context, localFilePathResourceId);
        if (fileName == null)
            return null;
        return readJsonFromFile(fileName, context);
    }

    /**
     * This method will read the Json string form the given local path.
     *
     * @param localFilePath Path of the local Json file.
     * @param context       The context to use.  Usually your {@link android.app.Application}
     *                      or {@link android.app.Activity} object.
     * @return Local Json file data as String
     */
    private String readJsonFromFile(String localFilePath, Context context) {
        String json = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuilder stringBuilder = new StringBuilder();
        try {
            is = new InputStreamReader(context.getAssets().open(localFilePath), "ISO-8859-1");
            br = new BufferedReader(is);
            String line;
            while ((line = br.readLine()) != null) {
                stringBuilder.append(line);
            }
            json = stringBuilder.toString();
        } catch (Exception e) {
            Log.getStackTraceString(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }

            try {
                if (br != null) {
                    br.close();
                }
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
        return json;
    }
}

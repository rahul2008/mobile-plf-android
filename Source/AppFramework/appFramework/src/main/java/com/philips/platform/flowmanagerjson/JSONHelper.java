package com.philips.platform.flowmanagerjson;

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

    /**
     * This method will download and return the server response from given URL.
     * It check for cached response if available an return the cached one instead of downloading if its valid.
     * To handle the offline and error scenarios it takes the path of prePackaged response file.
     *
     * @param url           Server URL from where the data will be downloaded.
     * @param localFilePath Path of prePackaged response data.
     * @return The response as a String or 'null'
     */
    public String getJsonFromURL(int url, int localFilePath) {
        String response = null;
        //TODO : need to add CQ5 json download code
        response = readJsonFromFile(localFilePath, mContext);
        return response;
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
        String str = null;
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
    public String readJsonFromFile(String localFilePath, Context context) {
        String json = null;
        InputStreamReader is = null;
        BufferedReader br = null;
        StringBuffer buffer = new StringBuffer();
        try {
            is = new InputStreamReader(context.getAssets().open(localFilePath), "ISO-8859-1");
            br = new BufferedReader(is);
            String line = "";
            if (br != null) {
                while ((line = br.readLine()) != null) {
                    buffer.append(line);
                    line = null;
                }
            }
            json = buffer.toString();
            buffer = null;
        } catch (Exception e) {
            Log.getStackTraceString(e);
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                is = null;
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }

            try {
                if (br != null) {
                    br.close();
                }
                br = null;
            } catch (Exception e) {
                Log.getStackTraceString(e);
            }
        }
        return json;
    }
}

/*
 * Copyright (c) 2015-2017 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.cdp2.commlib.devicetest.runner;

import android.os.Bundle;
import android.os.Environment;
import android.support.test.runner.AndroidJUnitRunner;
import android.util.Log;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

import cucumber.api.android.CucumberInstrumentationCore;

public class CucumberRunner extends AndroidJUnitRunner {
    private static final String TAG = "CucumberRunner";

    public static final String PACKAGE_NAME = "com.philips.cdp2.commlib.devicetest";
    public static final String REPORT_PATH = "/data/data/";
    public static final String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Android/data/";
    public static final String JSON_PATH = REPORT_PATH + PACKAGE_NAME + "/report.json";
    public static final String HTML_PATH = REPORT_PATH + PACKAGE_NAME + "/cucumber-reports/html";

    private CucumberInstrumentationCore cucumberInstrumentationCore = new CucumberInstrumentationCore(this);

    @Override
    public void onCreate(final Bundle bundle) {
        cucumberInstrumentationCore.create(bundle);
        super.onCreate(bundle);
    }

    @Override
    public void onStart() {
        cucumberInstrumentationCore.start();
    }

    @Override
    public void finish(int resultCode, Bundle results) {
        try {
            copyFiles();
        } catch (IOException e) {
            Log.d(TAG, "Failed to copy files.", e);
            e.printStackTrace();
        }

        super.finish(resultCode, results);
    }

    private void copyFiles() throws IOException {
        File destinationDir = new File(EXTERNAL_PATH + PACKAGE_NAME);
        if (!destinationDir.exists()) {
            destinationDir.mkdir();
        }

        File jsonSource = new File(JSON_PATH);
        File jsonDestination = new File(JSON_PATH.replace(REPORT_PATH, EXTERNAL_PATH));
        FileUtils.copyFile(jsonSource, jsonDestination);

        File htmlSource = new File(HTML_PATH);
        File htmlDestination = new File(HTML_PATH.replace(REPORT_PATH, EXTERNAL_PATH));
        FileUtils.copyDirectory(htmlSource, htmlDestination);
    }
}

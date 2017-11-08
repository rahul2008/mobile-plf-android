/*
 *
 *  * Copyright (c) Koninklijke Philips N.V. 2017
 *  * All rights are reserved. Reproduction or dissemination in whole or in part
 *  * is prohibited without the prior written consent of the copyright holder.
 *
 */

package com.philips.platform.appinfra;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.platform.appinfra.languagepack.LanguagePackConstants;
import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;

import java.io.File;

import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LANGUAGE_PACK_PATH;

public class FileUtilsTest extends AppInfraInstrumentation {

    private FileUtils fileUtils;
    private Context context;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        fileUtils = new FileUtils(context);
    }

    public void testGetFilePath() {
        File some_file = fileUtils.getFilePath("some_file", LANGUAGE_PACK_PATH);
        assertTrue(some_file != null);
        fileUtils.saveFile("some_response", "some_file", LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(some_file.exists());
        assertEquals(some_file.getName(),"some_file");
    }

    public void testReadFile() {
        fileUtils.saveFile("Some response", "some_file_new", LANGUAGE_PACK_PATH);
        File some_file = fileUtils.getFilePath("some_file_new", LANGUAGE_PACK_PATH);
        String s = fileUtils.readFile(some_file);
        assertEquals(s,"Some response");
    }

    public void testDeleteFile() {
        fileUtils.saveFile("some_response", "some_file", LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(fileUtils.deleteFile("some_file", LANGUAGE_PACK_PATH));
    }

    public void testRenameOnActivate() {
        fileUtils.saveFile("some_response", LanguagePackConstants.LOCALE_FILE_DOWNLOADED, LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(fileUtils.renameOnActivate());
    }

    public void testSavingLanguagePackModel() {
        LanguagePackModel languagePackModel = new LanguagePackModel();
        languagePackModel.setLocale("some_locale");
        languagePackModel.setUrl("some_url");
        languagePackModel.setVersion("some_version");
        fileUtils.saveLocaleMetaData(languagePackModel);
        String s = fileUtils.readFile(fileUtils.getFilePath(LanguagePackConstants.LOCALE_FILE_INFO, LanguagePackConstants.LANGUAGE_PACK_PATH));
        LanguagePackModel model = new Gson().fromJson(s, LanguagePackModel.class);
        if (model != null) {
            assertEquals(model.getLocale(), "some_locale");
            assertEquals(model.getUrl(), "some_url");
        }
    }
}
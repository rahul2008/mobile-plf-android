/*
 * Copyright (c) 2015-2018 Koninklijke Philips N.V.
 * All rights reserved.
 */

package com.philips.platform.appinfra;

import android.content.Context;

import com.google.gson.Gson;
import com.philips.platform.appinfra.languagepack.LanguagePackConstants;
import com.philips.platform.appinfra.languagepack.model.LanguagePackModel;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static com.philips.platform.appinfra.languagepack.LanguagePackConstants.LANGUAGE_PACK_PATH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class FileUtilsTest {

    private FileUtils fileUtils;
    private Context context;

    @Before
    protected void setUp() throws Exception {
        context = getInstrumentation().getContext();
        fileUtils = new FileUtils(context);
    }

    @Test
    public void testGetFilePath() {
        File some_file = fileUtils.getFilePath("some_file", LANGUAGE_PACK_PATH);
        assertNotNull(some_file);
        fileUtils.saveFile("some_response", "some_file", LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(some_file.exists());
        assertEquals(some_file.getName(), "some_file");
    }

    @Test
    public void testReadFile() {
        fileUtils.saveFile("Some response", "some_file_new", LANGUAGE_PACK_PATH);
        File some_file = fileUtils.getFilePath("some_file_new", LANGUAGE_PACK_PATH);
        String s = fileUtils.readFile(some_file);
        assertEquals(s, "Some response");
    }

    @Test
    public void testDeleteFile() {
        fileUtils.saveFile("some_response", "some_file", LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(fileUtils.deleteFile("some_file", LANGUAGE_PACK_PATH));
    }

    @Test
    public void testRenameOnActivate() {
        fileUtils.saveFile("some_response", LanguagePackConstants.LOCALE_FILE_DOWNLOADED, LanguagePackConstants.LANGUAGE_PACK_PATH);
        assertTrue(fileUtils.renameOnActivate());
    }

    @Test
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
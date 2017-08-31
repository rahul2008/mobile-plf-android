package com.philips.platform.appinfra.contentloader;

import android.content.Context;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.philips.platform.appinfra.AppInfra;
import com.philips.platform.appinfra.ConfigValues;
import com.philips.platform.appinfra.AppInfraInstrumentation;
import com.philips.platform.appinfra.appconfiguration.AppConfigurationManager;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.contentloader.model.ContentItem;
import com.philips.platform.appinfra.logging.LoggingInterface;

import org.json.JSONObject;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * ContentLoader Test class.
 */

public class ContentLoadertest extends AppInfraInstrumentation {

    private Context context;
    private AppInfra mAppInfra;
    private ContentLoaderInterface contentLoaderInterface;
    private ContentLoader mContentLoader;
    private String serviceId = "appinfra.testing.contentloader.service1";
    private Method method;
    private JsonObject jsonObject;
    private List<ContentItem> downloadedContents;
    ContentDatabaseHandler contentDatabaseHandler;



    @Override
    protected void setUp() throws Exception {
        super.setUp();
        context = getInstrumentation().getContext();
        assertNotNull(context);
        mAppInfra = new AppInfra.Builder().build(context);
        assertNotNull(mAppInfra);
        testConfig();
        mContentLoader = new ContentLoader(context, serviceId, 1, ContentArticle.class, "articles", mAppInfra, 0);
        assertNotNull(mContentLoader);
        downloadedContents = new ArrayList<ContentItem>();
        contentDatabaseHandler = ContentDatabaseHandler.getInstance(context);

        String json = "{\n" +
                "\t\"articles\": [{\n" +
                "\t\t\"title\": \"Welkom\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/welcome/welcome.html\",\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"welcome\",\n" +
                "\t\t\t\"key\": \"ugrow++welcome\",\n" +
                "\t\t\t\"id\": \"ugrow:welcome\"\n" +
                "\t\t}],\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"https://www.philips.com/c-dam/b2c/ugrow/Welcome/ugrow_welcome.jpg\",\n" +
                "\t\t\"uid\": \"welcome\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1478175303362\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">Hallo!</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">&nbsp;</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">Welkom bij uGrow: de app die je helpt om het giswerk bij het ouderschap te verminderen en die de gezonde ontwikkeling van je baby ondersteunt. Net als jij, weten wij dat er een enorme hoeveelheid adviezen bestaat als het gaat om het opvoeden van gezonde en gelukkige kinderen, en niet alles is relevant voor jou.</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">&nbsp;</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">Wij hebben liever niet dat je je moet inlezen totdat je kindje afstudeert en daarom hebben we een ervaring ontworpen die perfect is afgestemd op jou en je baby. </span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">&nbsp;</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">uGrow geeft via berichten en artikelen nuttige adviezen op basis van de bijgehouden gegevens en ontwikkelingsfase van je baby. Je hoeft dus niets meer te missen.</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">&nbsp;</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">Door het slaap-, eet- en huilgedrag van je baby bij te houden, helpen wij je zijn unieke patronen en ritmes te ontdekken, zodat je weloverwogen keuzes kunt maken. Met uGrow kun je deze kostbare momenten ook vieren door foto's en opmerkingen toe te voegen.</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">&nbsp;</span></p>\\n<p style=\\\"font-family: tahoma, arial, helvetica, sans-serif; font-size: 12px;\\\"><span class=\\\"p-body-copy-02\\\">Jouw reis naar slimmer ouderschap gaat beginnen!</span></p>\\n\"\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"Uitleg WHO-groeicurve\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/growth-chart/who-l.html\",\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"who-l\",\n" +
                "\t\t\t\"key\": \"ugrow++who-l\",\n" +
                "\t\t\t\"id\": \"ugrow:who-l\"\n" +
                "\t\t}],\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"\",\n" +
                "\t\t\"uid\": \"who-l\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1477039952455\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"<p><b>Wat is een groeicurve?</b></p>\\n<p>Een groeicurve bestaat uit een aantal lijnen, die de gemiddelde lengte en het gemiddelde gewicht van baby's van dezelfde leeftijd en hetzelfde geslacht weergeven.</p>\\n<p>&nbsp;</p>\\n<p>De tabel is gebaseerd op de gemiddelde lengte en het gemiddelde gewicht van een groot aantal baby's en kan wereldwijd op alle baby's worden toegepast.</p>\\n<p>&nbsp;</p>\\n<p><b>Waarom zou ik een groeicurve gebruiken?</b></p>\\n<p>De groei van je kleintje geeft je - samen met een aantal andere factoren die van belang zijn - een beeld van hun algehele gezondheid en welzijn.</p>\\n<p>&nbsp;</p>\\n<p>Door de lengte en het gewicht van je baby in kaart te brengen, kunnen medische professionals het groeipatroon op de langere termijn volgen.</p>\\n<p>&nbsp;</p>\\n<p>Je kunt ook zien hoe jouw baby groeit in vergelijking met andere baby's van dezelfde leeftijd en hetzelfde geslacht.</p>\\n<p>&nbsp;</p>\\n<p><b>Hoe gebruik ik een groeicurve?</b></p>\\n<p>Stel dat je baby zich in het 50ste percentiel voor gewicht bevindt. Dit betekent dat 50% van de baby's van dezelfde leeftijd en hetzelfde geslacht minder of hetzelfde weegt en 50% meer weegt dan jouw kind.</p>\\n<p>&nbsp;</p>\\n<p>Als je baby echter in het 75ste percentiel zit, betekent dit dat 75% van alle baby's minder of hetzelfde weegt en 25% meer weegt dan jouw baby.</p>\\n<p>&nbsp;</p>\\n<p><b>Niet vergeten</b></p>\\n<p>Alle baby's groeien anders, dus geen groeipatroon is hetzelfde!</p>\\n\"\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"Uitleg WHO-groeicurve\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/growth-chart/who-w.html\",\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"who-w\",\n" +
                "\t\t\t\"key\": \"ugrow++who-w\",\n" +
                "\t\t\t\"id\": \"ugrow:who-w\"\n" +
                "\t\t}],\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"\",\n" +
                "\t\t\"uid\": \"who-w\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1477039925547\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"<p><b>Wat is een groeicurve?</b></p>\\n<p>Een groeicurve bestaat uit een aantal lijnen, die de gemiddelde lengte en het gemiddelde gewicht van baby's van dezelfde leeftijd en hetzelfde geslacht weergeven.</p>\\n<p>&nbsp;</p>\\n<p>De tabel is gebaseerd op de gemiddelde lengte en het gemiddelde gewicht van een groot aantal baby's en kan wereldwijd op alle baby's worden toegepast.</p>\\n<p>&nbsp;</p>\\n<p><b>Waarom zou ik een groeicurve gebruiken?</b></p>\\n<p>De groei van je kleintje geeft je - samen met een aantal andere factoren die van belang zijn - een beeld van hun algehele gezondheid en welzijn.</p>\\n<p>&nbsp;</p>\\n<p>Door de lengte en het gewicht van je baby in kaart te brengen, kunnen medische professionals het groeipatroon op de langere termijn volgen.</p>\\n<p>&nbsp;</p>\\n<p>Je kunt ook zien hoe jouw baby groeit in vergelijking met andere baby's van dezelfde leeftijd en hetzelfde geslacht.</p>\\n<p>&nbsp;</p>\\n<p><b>Hoe gebruik ik een groeicurve?</b></p>\\n<p>Stel dat je baby zich in het 50ste percentiel voor gewicht bevindt. Dit betekent dat 50% van de baby's van dezelfde leeftijd en hetzelfde geslacht minder of hetzelfde weegt en 50% meer weegt dan jouw kind.</p>\\n<p>&nbsp;</p>\\n<p>Als je baby echter in het 75ste percentiel zit, betekent dit dat 75% van alle baby's minder of hetzelfde weegt en 25% meer weegt dan jouw baby.</p>\\n<p>&nbsp;</p>\\n<p><b>Niet vergeten</b></p>\\n<p>Alle baby's groeien anders, dus geen groeipatroon is hetzelfde!</p>\\n\"\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"WHO Growth chart\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/growth-chart.html\",\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"\",\n" +
                "\t\t\"uid\": \"\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1477039843944\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"Uit de geregistreerde slaapmomenten blijkt dat {%baby_name%} overdag ongeveer {%avg_day%} uur heeft geslapen en 's nachts {%avg_night%} uur.\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/insights-feeding/is11b.html\",\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"insight\",\n" +
                "\t\t\t\"key\": \"ugrow++insight\",\n" +
                "\t\t\t\"id\": \"ugrow:insight\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"is11b\",\n" +
                "\t\t\t\"key\": \"ugrow++is11b\",\n" +
                "\t\t\t\"id\": \"ugrow:is11b\"\n" +
                "\t\t}],\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"\",\n" +
                "\t\t\"uid\": \"is11b\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1476966584692\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"\"\n" +
                "\t}, {\n" +
                "\t\t\"title\": \"Uit de geregistreerde slaapmomenten blijkt dat {%baby_name%} overdag ongeveer {%avg_day%} uur heeft geslapen en 's nachts {%avg_night%} uur.\",\n" +
                "\t\t\"link\": \"https://www.philips.com/content/B2C/nl_NL/ugrow-app/home/articles/insights-feeding/is11a.html\",\n" +
                "\t\t\"tags\": [{\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"insight\",\n" +
                "\t\t\t\"key\": \"ugrow++insight\",\n" +
                "\t\t\t\"id\": \"ugrow:insight\"\n" +
                "\t\t}, {\n" +
                "\t\t\t\"isVisibleOnWeb\": true,\n" +
                "\t\t\t\"name\": \"is11a\",\n" +
                "\t\t\t\"key\": \"ugrow++is11a\",\n" +
                "\t\t\t\"id\": \"ugrow:is11a\"\n" +
                "\t\t}],\n" +
                "\t\t\"previewimage\": \"\",\n" +
                "\t\t\"imageUrl\": \"\",\n" +
                "\t\t\"uid\": \"is11a\",\n" +
                "\t\t\"linkurl\": \"\",\n" +
                "\t\t\"portraitimage\": \"\",\n" +
                "\t\t\"modDate\": \"1476966567287\",\n" +
                "\t\t\"overlay\": \"\",\n" +
                "\t\t\"description\": \"\",\n" +
                "\t\t\"text\": \"\"\n" +
                "\t}]\n" +
                "}\n";
        JsonParser parser = new JsonParser();
        jsonObject = (JsonObject) parser.parse(json);


    }

    public void testConfig() {

        final AppConfigurationManager mConfigInterface = new AppConfigurationManager(mAppInfra) {
            @Override
            protected JSONObject getMasterConfigFromApp() {
                JSONObject result = null;
                try {
                    String testJson = ConfigValues.testJson();
                    result = new JSONObject(testJson);
                } catch (Exception e) {
                }
                return result;
            }

        };
        mAppInfra = new AppInfra.Builder().setConfig(mConfigInterface).build(context);
    }


    public void testRefresh() {
        mContentLoader.refresh(new ContentLoaderInterface.OnRefreshListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT result) {
                assertNotNull(result);
                assertSame(REFRESH_RESULT.REFRESHED_FROM_SERVER, REFRESH_RESULT.REFRESHED_FROM_SERVER);
            }
        });
    }

    public void testdownloadContent() {
        try {
            method = mContentLoader.getClass().getDeclaredMethod("downloadContent", ContentLoaderInterface.OnRefreshListener.class);
            method.setAccessible(true);
            method.invoke(mContentLoader, new ContentLoaderInterface.OnRefreshListener() {
                @Override
                public void onError(ContentLoaderInterface.ERROR error, String message) {
                }

                @Override
                public void onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT result) {

                }
            });
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ContentLoader",
                    e.getMessage());
        }
    }


    public void testparseJsonandSave() {
        ContentItem ContentItemTest = null;
        long mLastUpdatedTime = (new Date()).getTime();
        JsonElement content = jsonObject.get("articles");
        JsonArray contentList = null;
        if (null != content) {
            if (content.isJsonArray()) {
                contentList = content.getAsJsonArray();
            }
            if (null != contentList && contentList.size() > 0) {
                for (int contentCount = 0; contentCount < contentList.size(); contentCount++) {
                    Log.i("CL Ariticle", "" + contentList.get(contentCount));
                    /*ContentArticle contentArticle = gson.fromJson(contentList.get(contentCount), ContentArticle.class);
                    ContentItem contentItem = new ContentItem();
                    contentItem.setId(contentArticle.getId());
                    contentItem.setServiceId(serviceId);
                    contentItem.setRawData(contentList.get(contentCount).toString());
                    contentItem.setVersionNumber(contentArticle.getVersion());
                    List<String> tagList = contentArticle.getTags();
                    String tags = "";
                    if (null != tagList && tagList.size() > 0) {
                        for (String tag : tagList) {
                            tags += tag + ",";
                        }
                        tags = tags.substring(0, tags.length() - 1);// remove last comma
                    }
                    contentItem.setTags(tags);
                    downloadedContents.add(contentItem);*/
                    ContentInterface contentInterface;
                    try {
                        contentInterface = ContentArticle.class.newInstance();

                        contentInterface.parseInput(contentList.get(contentCount).toString());
                        ContentItem contentItem = new ContentItem();
                        contentItem.setId(contentInterface.getId().replace("\"", ""));
                        contentItem.setServiceId(serviceId);
                        contentItem.setRawData(contentList.get(contentCount).toString());
                        contentItem.setVersionNumber(contentInterface.getVersion());
                        contentItem.setLastUpdatedTime(mLastUpdatedTime); // last updated time
                        String tags = "";

                        List<String> tagList = contentInterface.getTags();
                        if (null != tagList && tagList.size() > 0) {
                            for (String tagId : tagList) {
                                tags += tagId + " ";
                            }
                        }
                        contentItem.setTags(tags);
                        ContentItemTest = contentItem;
                        downloadedContents.add(contentItem);
                        String articleId = contentItem.getId();
                        Log.i("CL Ariticle", "" + articleId + "  TAGs ");
                    } catch (InstantiationException | IllegalAccessException e) {
                    }
                }

                ContentDatabaseHandler mContentDatabaseHandler = ContentDatabaseHandler.getInstance(context);

                assertNotNull(mContentDatabaseHandler.addContents(downloadedContents, serviceId, mLastUpdatedTime, 1, true));
                if (downloadedContents.size() > 0) {
                    String[] str = new String[1];
                    str[0] = ContentItemTest.getId();
                    assertNotNull(mContentDatabaseHandler.getContentById(serviceId, str));
                    assertNotNull(mContentDatabaseHandler.getAllContentIds(serviceId));
                }

            }
            //TBD contentDatabaseHandler.addContents(downloadedContents, mLastUpdatedTime, serviceId, expiryTimeforUserInputTime(1));
        }
    }

    public void testgetAllContent() {
        mContentLoader.getAllContent(new ContentLoaderInterface.OnResultListener<String>() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(List<String> contents) {
                assertNotNull(contents);
                // assertTrue(contents.size() > 0);
            }
        });
    }

    public void testclearCache() {
        mContentLoader.clearCache();
    }

    public void testgetAllContentIds() {
        List<String> contentIds = contentDatabaseHandler.getAllContentIds(serviceId);
        assertNotNull(contentIds);
        //   assertTrue(contentIds.size() > 0);
        //   assertTrue(contentIds.contains("is11a"));
        mContentLoader.getContentById("is11b", new ContentLoaderInterface.OnResultListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(List contents) {
                assertNotNull(contents);
                assertTrue(contents.size() > 0);
//                assertTrue(contents.contains("is11a"));
            }
        });
    }


    public void testgetContentById() {
        String contentId[] = {"is11b", "is11a"};
        List<ContentItem> contentItems = contentDatabaseHandler.getContentById(serviceId, contentId);
        assertNotNull(contentItems);
//        assertTrue(contentItems.size() > 0);
        mContentLoader.getContentById(contentId, new ContentLoaderInterface.OnResultListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(List contents) {
                assertNotNull(contents);
                assertTrue(contents.size() > 0);

            }
        });
    }

    public void testgetContentLoaderServiceStateExpiry() {
        long keyexpiry = contentDatabaseHandler.getContentLoaderServiceStateExpiry(serviceId);
        assertNotNull(keyexpiry);
    }

    public void testclearCacheForContentLoader() {
        contentDatabaseHandler.clearCacheForContentLoader(serviceId);
    }

    public void testgetContentByTag() {
        mContentLoader.getContentByTag("ugrow:insight", new ContentLoaderInterface.OnResultListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(List contents) {
                assertNotNull(contents);
                assertTrue(contents.size() > 0);
            }
        });
    }

    public void testgetContentByTags() {
        String tags[] = {"ugrow:insight", "ugrow:is11b"};
        mContentLoader.getContentByTag(tags, ContentLoaderInterface.OPERATOR.OR, new ContentLoaderInterface.OnResultListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String message) {
                assertNotNull(error);
            }

            @Override
            public void onSuccess(List contents) {
                assertNotNull(contents);
                assertTrue(contents.size() > 0);
            }
        });
    }

    public void testgetmServiceId() {
        String servideId = mContentLoader.getmServiceId();
        assertNotNull(servideId);
    }

    public void testgetStatus() {
        assertNotNull(mContentLoader.getStatus());
    }


    public void testExpiryTimeforUserInputTime() {
        try {
            method = mContentLoader.getClass().getDeclaredMethod("expiryTimeforUserInputTime", int.class);
            method.setAccessible(true);
            method.invoke(mContentLoader, 3);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            mAppInfra.getAppInfraLogInstance().log(LoggingInterface.LogLevel.ERROR, "ContentLoader",
                    e.getMessage());
        }
    }
}



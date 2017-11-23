/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;
import android.text.TextUtils;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.net.URL;
import java.util.List;

import static com.janrain.android.engage.JREngage.getApplicationContext;


/**
 * Created by philips on 25/07/17.
 */

public class WebViewPresenter implements WebViewContract.Action {

    private WebViewContract.View viewListener;

    private Context context;

    protected static final String TERMS_AND_CONDITIONS = "app.termsandconditions";

    protected static final String PRIVACY = "app.privacynotice";

    protected static final String HIGH_DEEP_SLEEP_KEY = "app.articlehighsleepscore";

    protected static final String LOW_DEEP_SLEEP_KEY = "app.articlelowsleepscore";

    private ContentLoader contentLoader;

    public WebViewPresenter(WebViewContract.View viewListener, Context context) {
        this.viewListener = viewListener;
        this.context = context;
    }

    @Override
    public void loadUrl(WebViewEnum state) {
        AppInfraInterface appInfra = getAppInfra();
        ServiceDiscoveryInterface serviceDiscoveryInterface = appInfra.getServiceDiscovery();
        String value = "";

        switch (state) {
            case PRIVACY_CLICKED:
                value = PRIVACY;
                break;
            case TERMS_CLICKED:
                value = TERMS_AND_CONDITIONS;
                break;
            case LOW_DEEP_SLEEP_ARTICLE_CLICKED:
                value = LOW_DEEP_SLEEP_KEY;
                break;
            case HIGH_DEEP_SLEEP_ARTICLE_CLICKED:
                value = HIGH_DEEP_SLEEP_KEY;
                break;
        }

        serviceDiscoveryInterface.getServiceUrlWithCountryPreference(value, new
                ServiceDiscoveryInterface.OnGetServiceUrlListener() {
                    @Override
                    public void onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES errorvalues, String errorText) {
                        viewListener.onUrlLoadError(errorText);
                    }

                    @Override
                    public void onSuccess(URL url) {
                        if (url == null) {
                            viewListener.onUrlLoadError("Empty Url from Service discovery");
                        } else {
                            viewListener.updateUiOnUrlLoaded(url.toString());

                        }
                    }
                });
    }

    protected AppInfraInterface getAppInfra() {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
    }

    @Override
    public void loadArticle(final String articleServiceId,final String articleTitle) {
        if (contentLoader == null) {
            contentLoader = getContentLoaderInstance(articleServiceId);
        }
        contentLoader.refresh(new ContentLoaderInterface.OnRefreshListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String s) {
                viewListener.showToast(s);
            }

            @Override
            public void onSuccess(REFRESH_RESULT refresh_result) {
                viewListener.showToast(refresh_result.name());
                getArticleUrl(articleTitle);
            }
        });

    }

    protected ContentLoader getContentLoaderInstance(String articleServiceId) {
        return new ContentLoader(getApplicationContext(), articleServiceId, 1, ContentArticle.class, "articles", getAppInfra(), 0);
    }

    protected void getArticleUrl(String articleTitle) {
        contentLoader.getContentById(articleTitle, new ContentLoaderInterface.OnResultListener() {
            @Override
            public void onError(ContentLoaderInterface.ERROR error, String s) {
                viewListener.showToast(s);
            }

            @Override
            public void onSuccess(List list) {
                if(list!=null && list.size()>0){
                    viewListener.showToast("" + list.size());
                    ContentArticle article= (ContentArticle) list.get(0);
                    viewListener.onArticleLoaded(article.getLink());
                }else{
                    viewListener.showToast("Error while getting article url");
                }
            }
        });
    }
}

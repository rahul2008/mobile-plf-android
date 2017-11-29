/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.data.loaders;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import java.util.List;

import static com.janrain.android.engage.JREngage.getApplicationContext;

public class ArticleLoader {

    public static final String ARTICLES = "articles";

    private Context context;

    private ContentLoader contentLoder;

    private static final String articleServiceId = "app.articlesdeepsleepscore";

    public interface ArticleUrlLoadListener {
        void onSuccess(String url);

        void onError(String errorMessage);
    }


    public ArticleLoader(Context context) {
        this.context = context;
        contentLoder = new ContentLoader(getApplicationContext(), articleServiceId, 24, ContentArticle.class, ARTICLES, getAppInfra(), 0);
    }

    protected AppInfraInterface getAppInfra() {
        return ((AppFrameworkApplication) context.getApplicationContext()).getAppInfra();
    }

    public void getArticleUrl(final String id, final ArticleUrlLoadListener articleUrlLoadListener) {
        if (contentLoder.getStatus() == ContentLoaderInterface.STATE.CACHED_DATA_AVAILABLE) {
            contentLoder.getContentById(id, new ContentLoaderInterface.OnResultListener() {
                @Override
                public void onError(ContentLoaderInterface.ERROR error, String s) {
                    articleUrlLoadListener.onError(error.toString());
                }

                @Override
                public void onSuccess(List list) {
                    if (list != null && list.size() > 0) {
                        ContentArticle article = (ContentArticle) list.get(0);
                        articleUrlLoadListener.onSuccess(article.getLink());
                    } else {
                        articleUrlLoadListener.onError("Error while getting article url");
                    }
                }
            });
        } else {
            contentLoder.refresh(new ContentLoaderInterface.OnRefreshListener() {
                @Override
                public void onError(ContentLoaderInterface.ERROR error, String s) {
                    articleUrlLoadListener.onError(error.toString());
                }

                @Override
                public void onSuccess(REFRESH_RESULT refresh_result) {
                    if (refresh_result == REFRESH_RESULT.REFRESHED_FROM_SERVER) {
                        getArticleUrl(id, articleUrlLoadListener);
                    } else {
                        articleUrlLoadListener.onError(refresh_result.toString());
                    }
                }
            });
        }
    }
}

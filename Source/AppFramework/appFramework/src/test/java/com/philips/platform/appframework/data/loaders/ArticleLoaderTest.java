/*
 *  Copyright (c) Koninklijke Philips N.V., 2017
 *  All rights are reserved. Reproduction or dissemination
 *  in whole or in part is prohibited without the prior written
 *  consent of the copyright holder.
 */

package com.philips.platform.appframework.data.loaders;

import android.content.Context;
import android.support.annotation.NonNull;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.appinfra.contentloader.ContentLoaderInterface.ERROR;
import static com.philips.platform.appinfra.contentloader.ContentLoaderInterface.ERROR.NO_DATA_FOUND_IN_DB;
import static com.philips.platform.appinfra.contentloader.ContentLoaderInterface.OnRefreshListener;
import static com.philips.platform.appinfra.contentloader.ContentLoaderInterface.OnResultListener;
import static com.philips.platform.appinfra.contentloader.ContentLoaderInterface.STATE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ArticleLoaderTest {

    @Mock
    static AppInfraInterface appInfraInterface;

    private ArticleLoader articleLoader;

    @Mock
    static ContentLoader contentLoader;

    @Mock
    Context context;

    @Mock
    ArticleLoader.ArticleUrlLoadListener articleUrlLoadListener;

    @Captor
    ArgumentCaptor<ContentLoaderInterface.OnResultListener> onResultListenerArgumentCaptor;

    OnResultListener onResultListener;

    @Captor
    ArgumentCaptor<ContentLoaderInterface.OnRefreshListener> onRefreshListenerArgumentCaptor;

    OnRefreshListener onRefreshListener;

    @Before
    public void setUp(){
        articleLoader=new ArticleLoaderMock(context);
    }

    @Test
    public void getArticleUrl_Cached_data_available_On_Success(){
        when(contentLoader.getStatus()).thenReturn(STATE.CACHED_DATA_AVAILABLE);
        articleLoader.getArticleUrl("high_deepsleepscore",articleUrlLoadListener);
        verify(contentLoader).getContentById(anyString(),onResultListenerArgumentCaptor.capture());
        onResultListener=onResultListenerArgumentCaptor.getValue();
        List<ContentArticle> contentArticles=new ArrayList<>();
        contentArticles.add(new ContentArticle());
        onResultListener.onSuccess(contentArticles);
        verify(articleUrlLoadListener).onSuccess(anyString());


    }

    @Test
    public void getArticleUrl_Cached_data_available_On_Success_List_Empty(){
        when(contentLoader.getStatus()).thenReturn(STATE.CACHED_DATA_AVAILABLE);
        articleLoader.getArticleUrl("high_deepsleepscore",articleUrlLoadListener);
        verify(contentLoader).getContentById(anyString(),onResultListenerArgumentCaptor.capture());
        onResultListener=onResultListenerArgumentCaptor.getValue();
        List<ContentArticle> contentArticles=new ArrayList<>();
        onResultListener.onSuccess(contentArticles);
        verify(articleUrlLoadListener).onError(anyString());
    }

    @Test
    public void getArticleUrl_Cached_data_available_On_Error(){
        when(contentLoader.getStatus()).thenReturn(STATE.CACHED_DATA_AVAILABLE);
        articleLoader.getArticleUrl("high_deepsleepscore",articleUrlLoadListener);
        verify(contentLoader).getContentById(anyString(),onResultListenerArgumentCaptor.capture());
        onResultListener=onResultListenerArgumentCaptor.getValue();
        onResultListener.onError(NO_DATA_FOUND_IN_DB,any(String.class));
        verify(articleUrlLoadListener).onError(anyString());
    }

    @Test
    public void getArticleUrl_REFRESH_OnSuccess_On_Error(){
        when(contentLoader.getStatus()).thenReturn(STATE.CACHED_DATA_OUTDATED);
        articleLoader.getArticleUrl("high_deepsleepscore",articleUrlLoadListener);
        verify(contentLoader).refresh(onRefreshListenerArgumentCaptor.capture());
        onRefreshListener=onRefreshListenerArgumentCaptor.getValue();
        onRefreshListener.onSuccess(OnRefreshListener.REFRESH_RESULT.REFRESHED_FAILED);
        verify(articleUrlLoadListener).onError(anyString());
    }

    @Test
    public void getArticleUrl_REFRESH_On_Error(){
        when(contentLoader.getStatus()).thenReturn(STATE.CACHED_DATA_OUTDATED);
        articleLoader.getArticleUrl("high_deepsleepscore",articleUrlLoadListener);
        verify(contentLoader).refresh(onRefreshListenerArgumentCaptor.capture());
        onRefreshListener=onRefreshListenerArgumentCaptor.getValue();
        onRefreshListener.onError(ERROR.SERVER_ERROR,anyString());
        verify(articleUrlLoadListener).onError(anyString());
    }


    static class ArticleLoaderMock extends ArticleLoader{

        public ArticleLoaderMock(Context context) {
            super(context);
        }

        @Override
        protected AppInfraInterface getAppInfra() {
            return appInfraInterface;
        }

        @NonNull
        @Override
        protected ContentLoader getContentLoader() {
            return contentLoader;
        }
    }
}
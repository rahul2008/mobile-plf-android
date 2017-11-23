/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
* in whole or in part is prohibited without the prior written
* consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;

import com.philips.platform.appinfra.AppInfraInterface;
import com.philips.platform.appinfra.contentloader.ContentLoader;
import com.philips.platform.appinfra.contentloader.ContentLoaderInterface;
import com.philips.platform.appinfra.contentloader.model.ContentArticle;
import com.philips.platform.appinfra.servicediscovery.ServiceDiscoveryInterface;
import com.philips.platform.baseapp.base.AppFrameworkApplication;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.philips.platform.baseapp.screens.termsandconditions.WebViewPresenter.HIGH_DEEP_SLEEP_KEY;
import static com.philips.platform.baseapp.screens.termsandconditions.WebViewPresenter.LOW_DEEP_SLEEP_KEY;
import static com.philips.platform.baseapp.screens.termsandconditions.WebViewPresenter.PRIVACY;
import static com.philips.platform.baseapp.screens.termsandconditions.WebViewPresenter.TERMS_AND_CONDITIONS;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by philips on 27/07/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class WebViewPresenterTest {

    @Mock
    private Context context;

    @Mock
    private WebViewContract.View view;

    WebViewPresenter webViewPresenter;

    @Captor
    private ArgumentCaptor<ServiceDiscoveryInterface.OnGetServiceUrlListener> captor;

    ServiceDiscoveryInterface serviceDiscoveryInterfaceMock;

    ServiceDiscoveryInterface.OnGetServiceUrlListener value;

    @Mock
    private static ContentLoader contentLoader;

    @Captor
    private ArgumentCaptor<ContentLoaderInterface.OnRefreshListener> refreshListenerCaptor;

    @Captor
    private ArgumentCaptor<ContentLoaderInterface.OnResultListener> onResultListenerArgumentCaptor;

    @After
    public void tearDown() {
        context = null;
        view = null;
        webViewPresenter = null;
        captor = null;
        serviceDiscoveryInterfaceMock = null;
        value = null;
    }

    @Before
    public void setUp() {
        webViewPresenter = new WebViewPresenterMock(view, context);
        AppInfraInterface appInfraInterfaceMock = mock(AppInfraInterface.class);
        AppFrameworkApplication appFrameworkApplicationMock = mock(AppFrameworkApplication.class);
        serviceDiscoveryInterfaceMock = mock(ServiceDiscoveryInterface.class);
        when(appInfraInterfaceMock.getServiceDiscovery()).thenReturn(serviceDiscoveryInterfaceMock);
        when(appFrameworkApplicationMock.getAppInfra()).thenReturn(appInfraInterfaceMock);
        when(context.getApplicationContext()).thenReturn(appFrameworkApplicationMock);
        webViewPresenter.loadUrl(WebViewEnum.PRIVACY_CLICKED);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(PRIVACY), captor.capture());
        value= captor.getValue();
    }

    @Test
    public void loadTermsAndConditionsUrlSuccess() throws Exception {
        value.onSuccess(new URL("https://www.philips.com/wrx/b2c/c/us/en/apps/77000/TermsOfUse.html"));
        verify(view).updateUiOnUrlLoaded(any(String.class));
    }
    @Test
    public void loadTermsAndConditionsUrlError() throws Exception {
        value.onError(ServiceDiscoveryInterface.OnErrorListener.ERRORVALUES.SERVER_ERROR,"");
        verify(view).onUrlLoadError(any(String.class));
    }

    @Test
    public void loadTermsAndConditionsUrlEmpty() throws Exception {
        value.onSuccess(null);
        verify(view).onUrlLoadError(any(String.class));
    }

    @Test
    public void loadArticle_getArticleUrlSuccessWithNullListTest() {
        webViewPresenter.loadArticle(WebViewActivity.ARTICLE_SERVICE_ID, WebViewActivity.HIGH_DEEPSLEEPSCORE);
        verify(contentLoader).refresh(refreshListenerCaptor.capture());
        ContentLoaderInterface.OnRefreshListener onRefreshListener = refreshListenerCaptor.getValue();
        onRefreshListener.onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT.LOADED_FROM_LOCAL_CACHE);
        verify(view).showToast(any(String.class));
        verify(contentLoader).getContentById(any(String.class), onResultListenerArgumentCaptor.capture());

        ContentLoaderInterface.OnResultListener onResultListener = onResultListenerArgumentCaptor.getValue();
        onResultListener.onSuccess(null);
        verify(view).showToast("Error while getting article url");
    }

    @Test
    public void loadArticle_getArticleUrlSuccessWithNonEmptyListTest() {
        webViewPresenter.loadArticle(WebViewActivity.ARTICLE_SERVICE_ID, WebViewActivity.HIGH_DEEPSLEEPSCORE);
        verify(contentLoader).refresh(refreshListenerCaptor.capture());
        ContentLoaderInterface.OnRefreshListener onRefreshListener = refreshListenerCaptor.getValue();
        onRefreshListener.onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT.LOADED_FROM_LOCAL_CACHE);
        verify(view).showToast(any(String.class));
        verify(contentLoader).getContentById(any(String.class), onResultListenerArgumentCaptor.capture());

        ContentLoaderInterface.OnResultListener onResultListener = onResultListenerArgumentCaptor.getValue();
        List<ContentArticle> contentArticleList = new ArrayList<>();
        ContentArticle contentArticle = new ContentArticle();
        contentArticle.setLink("");
        contentArticleList.add(contentArticle);
        onResultListener.onSuccess(contentArticleList);
        verify(view).showToast("" + contentArticleList.size());
        verify(view).onArticleLoaded("");
    }

    @Test
    public void loadArticle_getArticleUrlErrorTest() {
        webViewPresenter.loadArticle(WebViewActivity.ARTICLE_SERVICE_ID, WebViewActivity.HIGH_DEEPSLEEPSCORE);
        verify(contentLoader).refresh(refreshListenerCaptor.capture());
        ContentLoaderInterface.OnRefreshListener onRefreshListener = refreshListenerCaptor.getValue();
        onRefreshListener.onSuccess(ContentLoaderInterface.OnRefreshListener.REFRESH_RESULT.LOADED_FROM_LOCAL_CACHE);
        verify(view).showToast(any(String.class));
        verify(contentLoader).getContentById(any(String.class), onResultListenerArgumentCaptor.capture());

        ContentLoaderInterface.OnResultListener onResultListener = onResultListenerArgumentCaptor.getValue();
        onResultListener.onError(ContentLoaderInterface.ERROR.NO_DATA_FOUND_IN_DB, "error");
        verify(view).showToast("error");
    }

    @Test
    public void loadArticleErrorTest() {
        webViewPresenter.loadArticle(WebViewActivity.ARTICLE_SERVICE_ID, WebViewActivity.HIGH_DEEPSLEEPSCORE);
        verify(contentLoader).refresh(refreshListenerCaptor.capture());
        ContentLoaderInterface.OnRefreshListener onRefreshListener = refreshListenerCaptor.getValue();
        onRefreshListener.onError(any(ContentLoaderInterface.ERROR.class), "error");
        verify(view).showToast("error");
    }

    @Test
    public void loadUrlTermClickedTest() {
        webViewPresenter.loadUrl(WebViewEnum.TERMS_CLICKED);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(TERMS_AND_CONDITIONS), any(ServiceDiscoveryInterface.OnGetServiceUrlListener.class));
    }

    @Test
    public void loadUrlLowSleepArticleClickedTest() {
        webViewPresenter.loadUrl(WebViewEnum.LOW_DEEP_SLEEP_ARTICLE_CLICKED);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(LOW_DEEP_SLEEP_KEY), any(ServiceDiscoveryInterface.OnGetServiceUrlListener.class));
    }

    @Test
    public void loadUrlHighSleepArticleClickedTest() {
        webViewPresenter.loadUrl(WebViewEnum.HIGH_DEEP_SLEEP_ARTICLE_CLICKED);
        verify(serviceDiscoveryInterfaceMock).getServiceUrlWithCountryPreference(eq(HIGH_DEEP_SLEEP_KEY), any(ServiceDiscoveryInterface.OnGetServiceUrlListener.class));
    }

    static class WebViewPresenterMock extends WebViewPresenter {

        public WebViewPresenterMock(WebViewContract.View viewListener, Context context) {
            super(viewListener, context);
        }

        @Override
        protected ContentLoader getContentLoaderInstance(String articleServiceId) {
            return contentLoader;
        }
    }
}
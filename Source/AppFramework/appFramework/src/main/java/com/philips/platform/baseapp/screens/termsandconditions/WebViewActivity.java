/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.flowmanager.base.BaseFlowManager;
import com.philips.platform.appframework.flowmanager.exceptions.ConditionIdNotSetException;
import com.philips.platform.appframework.flowmanager.exceptions.NoConditionFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoEventFoundException;
import com.philips.platform.appframework.flowmanager.exceptions.NoStateException;
import com.philips.platform.appframework.flowmanager.exceptions.StateIdNotSetException;
import com.philips.platform.baseapp.base.AbstractAppFrameworkBaseActivity;
import com.philips.platform.baseapp.base.AppFrameworkApplication;
import com.philips.platform.baseapp.screens.utility.RALog;
import com.philips.platform.uid.thememanager.UIDHelper;

import java.net.URL;

public class WebViewActivity extends AbstractAppFrameworkBaseActivity implements WebViewContract.View {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    public static String STATE="state";
    public static final String URL_TO_LOAD="url to load";
    public static final String LOW_DEEPSLEEPSCORE="low_deepsleepscore";
    public static final String HIGH_DEEPSLEEPSCORE="high_deepsleepscore";

    private WebView webView;

    private WebViewContract.Action termsAndConditionsAction;

    private WebViewEnum state;

    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        state=(WebViewEnum) getIntent().getSerializableExtra(STATE);
        url=(String)getIntent().getSerializableExtra(URL_TO_LOAD);
        webView = (WebView) findViewById(R.id.web_view);
        UIDHelper.setupToolbar(this);
        termsAndConditionsAction = new WebViewPresenter(this, this);
//        if(!TextUtils.isEmpty(url)){
//            updateUiOnUrlLoaded(url);
//        }else{
//            termsAndConditionsAction.loadUrl(state);
//        }
        if(state== WebViewEnum.PRIVACY_CLICKED){
            UIDHelper.setTitle(this,R.string.global_privacy_link);
            termsAndConditionsAction.loadUrl(state);
        }else if(state==WebViewEnum.TERMS_CLICKED){
            UIDHelper.setTitle(this,R.string.global_terms_link);
            termsAndConditionsAction.loadUrl(state);
        }else if(state==WebViewEnum.LOW_DEEP_SLEEP_ARTICLE_CLICKED){
//            setTitle(R.string.article);
            updateUiOnUrlLoaded("");
        }else if(state==WebViewEnum.HIGH_DEEP_SLEEP_ARTICLE_CLICKED){
//            setTitle(R.string.article);
            updateUiOnUrlLoaded("");
        }

    }


    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    public void updateActionBarIcon(boolean b) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_web_view_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_close) {
            onBackPressed();
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) this.getApplication();
        try {
            BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
            targetFlowManager.getBackState(targetFlowManager.getCurrentState());
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
        }
        finish();
    }

    @Override
    public void updateUiOnUrlLoaded(String url) {
        switch(state){
            case PRIVACY_CLICKED:
            case TERMS_CLICKED:
                showWebPage(url);
                break;
            case LOW_DEEP_SLEEP_ARTICLE_CLICKED:
                termsAndConditionsAction.loadArticle("app.articlesdeepsleepscore",LOW_DEEPSLEEPSCORE);
                break;
            case HIGH_DEEP_SLEEP_ARTICLE_CLICKED:
                termsAndConditionsAction.loadArticle("app.articlesdeepsleepscore",HIGH_DEEPSLEEPSCORE);
                break;
            default:
                showToast("Action event not recognized");
        }
    }

    protected void showWebPage(String url) {
        url=url.replaceAll("^\"|\"$", "");
        webView.loadUrl(url);
    }

    @Override
    public void onUrlLoadError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onArticleLoaded(String articleWebPageUrl) {
        showWebPage(articleWebPageUrl);
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}

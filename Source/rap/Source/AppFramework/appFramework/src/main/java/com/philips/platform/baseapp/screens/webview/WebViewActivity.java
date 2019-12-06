/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.webview;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.StringRes;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
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
import com.philips.platform.uid.drawable.FontIconDrawable;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.view.widget.ActionBarTextView;

public class WebViewActivity extends AbstractAppFrameworkBaseActivity implements WebViewContract.View {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    public static final String URL_TO_LOAD = "url to load";
    public static final String SERVICE_ID_KEY = "serviceId";
    public static final String ACTION_BAR_TITLE = "title";
    private WebView webView;
    private String url;
    private String title;
    private String serviceId;
    private WebViewContract.Action webViewActions;
    private ActionBarTextView actionBarTitle = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(!(savedInstanceState != null && !AppFrameworkApplication.isAppDataInitialized())) {
            setContentView(R.layout.activity_web_view);
            UIDHelper.setupToolbar(this);
            url = (String) getIntent().getSerializableExtra(URL_TO_LOAD);
            serviceId = (String) getIntent().getSerializableExtra(SERVICE_ID_KEY);
            title = (String) getIntent().getSerializableExtra(ACTION_BAR_TITLE);
            webView = (WebView) findViewById(R.id.web_view);
            webView.getSettings().setJavaScriptEnabled(true);
            webView.setWebViewClient(new WebViewClient());
            webViewActions = getWebViewPresenter();
            if(title!=null) {
                setActionbarTitle(title);
            }
            if (TextUtils.isEmpty(url)) {
                webViewActions.loadUrl(serviceId);
            } else {
                showWebPage(url);
            }
        }
    }

    private void setActionbarTitle(String title) {
        RALog.d(TAG," setActionbarTitle called");

        if (actionBarTitle == null) {
            actionBarTitle = findViewById(R.id.uid_toolbar_title);
        }
        if (actionBarTitle != null)
            actionBarTitle.setText(title);
    }

    protected WebViewPresenter getWebViewPresenter() {
        return new WebViewPresenter(this, this);
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
        FontIconDrawable drawable = new FontIconDrawable(this, getResources().getString(R.string.dls_cross), Typeface.createFromAsset(getAssets(), "fonts/iconfont.ttf")).sizeDp((int) getResources().getDimension(R.dimen.cross_icon_size));
        inflater.inflate(R.menu.menu_web_view_activity, menu);
        menu.findItem(R.id.menu_close).setIcon(drawable);
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


    protected void showWebPage(String url) {
        url = url.replaceAll("^\"|\"$", "");
        webView.loadUrl(url);
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    @Override
    public void onUrlLoadSuccess(String url) {
        showWebPage(url);
    }

    @Override
    public void onUrlLoadError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }
}

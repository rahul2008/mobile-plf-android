/*
 * Copyright (c) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 */

package com.philips.platform.baseapp.screens.termsandconditions;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.ActionBar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.webkit.WebView;

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

public class WebViewActivity extends AbstractAppFrameworkBaseActivity {

    private static final String TAG = "WebViewActivity";

    private static final String TITLE_BUNDLE_KEY = "titleInt";
    private static final String URL_BUNDLE_KEY = "url";

    @StringRes
    private int titleInt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        titleInt = extractTitleFromIntent(getIntent());
        initBarTitle(getResources().getString(titleInt));

        initViews((WebView) findViewById(R.id.web_view));
    }

    @Override
    public int getContainerId() {
        return 0;
    }

    @Override
    public void updateActionBarIcon(boolean b) {

    }

    @Override
    protected void onResume() {
        super.onResume();

        switch (titleInt) {
            case R.string.global_privacy_link:
                break;
            case R.string.global_terms_link:
                break;
        }
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

    private void initViews(WebView webView) {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(URL_BUNDLE_KEY)) {
            String url = getString(intent.getIntExtra(URL_BUNDLE_KEY, -1));
            initWithUrl(webView, Uri.parse(url));
        } else {
            RALog.e(TAG, "WebView activity started without content or url!");
        }
    }

    private void initWithUrl(@NonNull final WebView webView, @NonNull final Uri url) {
        webView.loadUrl(url.toString());
    }

    private void initBarTitle(@NonNull final String title) {
        setTitle(title);
        showBackButton(false);
    }

    @StringRes
    private int extractTitleFromIntent(@NonNull final Intent intent) {
        if (!intent.hasExtra(TITLE_BUNDLE_KEY)) {
            throw new RuntimeException("Should provide titleInt for the webview in the intent");
        }
        return intent.getIntExtra(TITLE_BUNDLE_KEY, -1);
    }

    public static void show(@NonNull final Context context, @StringRes int title, @StringRes int url) {
        context.startActivity(createIntent(context, title, url));
    }

    private static Intent createIntent(Context context, @StringRes int title, @StringRes int url) {
        return new Intent(context, WebViewActivity.class).putExtra(TITLE_BUNDLE_KEY, title)
                .putExtra(URL_BUNDLE_KEY, url);
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }

    public void showBackButton(boolean isVisible) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(isVisible);
        }
    }

    @Override
    public void onBackPressed() {
        AppFrameworkApplication appFrameworkApplication = (AppFrameworkApplication) this.getApplication();
        try {
            BaseFlowManager targetFlowManager = appFrameworkApplication.getTargetFlowManager();
            targetFlowManager.getBackState(targetFlowManager.getCurrentState());
            finish();
        } catch (NoEventFoundException | NoStateException | NoConditionFoundException | StateIdNotSetException | ConditionIdNotSetException
                e) {
            RALog.d(TAG, e.getMessage());
        }
    }
}

/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.baseapp.screens.termsandconditions;

import android.os.Bundle;
import android.support.annotation.StringRes;
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

public class WebViewActivity extends AbstractAppFrameworkBaseActivity implements TermsAndConditionsContract.View {

    private static final String TAG = WebViewActivity.class.getSimpleName();
    public static String STATE="state";

    private WebView webView;

    private TermsAndConditionsContract.Action termsAndConditionsAction;

    private TermsAndPrivacyStateData.TermsAndPrivacyEnum state;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        state=(TermsAndPrivacyStateData.TermsAndPrivacyEnum) getIntent().getSerializableExtra(STATE);
        webView = (WebView) findViewById(R.id.web_view);
        termsAndConditionsAction = new TermsAndConditionsPresenter(this, this);
        if(state== TermsAndPrivacyStateData.TermsAndPrivacyEnum.PRIVACY_CLICKED){
            setTitle(R.string.global_privacy_link);
        }else{
            setTitle(R.string.global_terms_link);
        }
        termsAndConditionsAction.loadTermsAndConditionsUrl(state);

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
        webView.loadUrl(url);
    }

    @Override
    public void onUrlLoadError(String errorMessage) {
        Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateActionBar(@StringRes int i, boolean b) {

    }

    @Override
    public void updateActionBar(String s, boolean b) {

    }
}

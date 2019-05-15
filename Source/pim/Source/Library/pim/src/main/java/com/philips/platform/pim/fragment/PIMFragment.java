package com.philips.platform.pim.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pif.DataInterface.USR.enums.UserLoggedInState;
import com.philips.platform.pim.R;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMLoginListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.manager.PIMUserManager;

import static android.app.Activity.RESULT_CANCELED;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;


public class PIMFragment extends Fragment implements PIMLoginListener {
    private PIMLoginManager pimLoginManager;
    private PIMOIDCConfigration pimoidcConfigration;
    private Context mContext;
    private LoggingInterface mLoggingInterface;
    private String TAG = PIMFragment.class.getSimpleName();
    private ProgressBar pimLoginProgreassBar;
    private Bundle mBundle;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoggingInterface = PIMSettingManager.getInstance().getLoggingInterface();
        pimoidcConfigration = PIMSettingManager.getInstance().getPimOidcConfigration();
        mBundle = getArguments();
        pimLoginManager = new PIMLoginManager(pimoidcConfigration);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pim, container, false);
        pimLoginProgreassBar = view.findViewById(R.id.pbPimRequest);
        // TODO: Deepthi, check if user is logged in before launching web page. (Done)
        PIMUserManager pimUserManager = PIMSettingManager.getInstance().getPimUserManager();
        if (pimoidcConfigration != null && pimUserManager.getUserLoggedInState() == UserLoggedInState.USER_NOT_LOGGED_IN) {
            pimLoginProgreassBar.setVisibility(View.VISIBLE);
            pimLoginManager.oidcLogin(mContext, mBundle, this, this);
        } else {
            mLoggingInterface.log(DEBUG, TAG, "OIDC Login skipped, as user is already logged in");
        }
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onLoginSuccess() {
        pimLoginProgreassBar.setVisibility(View.GONE);
    }

    @Override
    public void onLoginFailed(int errorCode) {
        pimLoginProgreassBar.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLoggingInterface.log(DEBUG, TAG, "onActivityResult : " + requestCode);
        if (resultCode == RESULT_CANCELED) {
            pimLoginProgreassBar.setVisibility(View.GONE);
        } else {
            pimLoginProgreassBar.setVisibility(View.VISIBLE);
            pimLoginManager.exchangeAuthorizationCode(mContext, data);
        }
    }
}

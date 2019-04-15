package com.philips.platform.pim.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.TokenResponse;

import static android.app.Activity.RESULT_CANCELED;

public class PIMFragment extends Fragment implements PIMListener, AuthorizationService.TokenResponseCallback {
    private PIMLoginManager pimLoginManager;
    private PIMOIDCConfigration pimoidcConfigration;
    private AuthorizationService mAuthService;
    private Context mContext;
    private AuthState mAuthState = null;
    private LoggingInterface mLoggingInterface;
    private String TAG = PIMFragment.class.getSimpleName();
    private ProgressBar pbPimRequestProgress;
    private TextView tvPimReqStatus;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PIMOIDCConfigration pimoidcConfigration = PIMSettingManager.getInstance().getPimOidcConfigration();
        pimLoginManager = new PIMLoginManager(pimoidcConfigration);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pim, container, false);
        pbPimRequestProgress = (ProgressBar) view.findViewById(R.id.pbPimRequest);
        tvPimReqStatus = (TextView) view.findViewById(R.id.tvPimRequestStatus);
        startAuthorization();
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onSuccess(AuthorizationServiceDiscovery discoveryDoc) {

    }

    @Override
    public void onError() {

    }


    private void startAuthorization() {
        pbPimRequestProgress.setVisibility(View.VISIBLE);
        tvPimReqStatus.setVisibility(View.VISIBLE);
        tvPimReqStatus.setText("Login in progress...");

        pimLoginManager.makeAuthRequest(this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult : " + requestCode);
        if (resultCode == RESULT_CANCELED) {
            pbPimRequestProgress.setVisibility(View.GONE);
            tvPimReqStatus.setVisibility(View.GONE);
        } else {
            pbPimRequestProgress.setVisibility(View.VISIBLE);
            tvPimReqStatus.setVisibility(View.VISIBLE);
            tvPimReqStatus.setText("Login in progress...");

            AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
            AuthorizationException exception = AuthorizationException.fromIntent(data);

            if (response != null)
                pimLoginManager.exchangeAuthorizationCode(mContext, response, this);

            if(exception != null) {
                //log exception
            }
        }
    }

    @Override
    public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {
        pbPimRequestProgress.setVisibility(View.GONE);
        tvPimReqStatus.setVisibility(View.VISIBLE);
        if (response != null) {
            tvPimReqStatus.setText("Access Token : " + response.accessToken);
        } else {
            tvPimReqStatus.setText(ex.errorDescription);
        }
    }
}

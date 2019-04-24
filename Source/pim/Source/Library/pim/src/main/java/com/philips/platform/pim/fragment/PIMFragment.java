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
import android.widget.TextView;

import com.philips.platform.appinfra.logging.LoggingInterface;
import com.philips.platform.pim.R;
import com.philips.platform.pim.configration.PIMOIDCConfigration;
import com.philips.platform.pim.listeners.PIMListener;
import com.philips.platform.pim.manager.PIMLoginManager;
import com.philips.platform.pim.manager.PIMSettingManager;
import com.philips.platform.pim.manager.PIMUserManager;

import net.openid.appauth.AuthState;
import net.openid.appauth.AuthorizationException;
import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationResponse;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;
import net.openid.appauth.TokenResponse;

import static android.app.Activity.RESULT_CANCELED;
import static com.philips.platform.appinfra.logging.LoggingInterface.LogLevel.DEBUG;

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
        pbPimRequestProgress = (ProgressBar) view.findViewById(R.id.pbPimRequest);
        tvPimReqStatus = (TextView) view.findViewById(R.id.tvPimRequestStatus);
        // TODO: Deepthi, check if user is logged in before launching web page
        startAuthorization(mBundle);
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


    private void startAuthorization(Bundle mBundle) {
        pbPimRequestProgress.setVisibility(View.VISIBLE);
        tvPimReqStatus.setVisibility(View.VISIBLE);
        tvPimReqStatus.setText("Login in progress...");
        // TODO:Addressed Deepthi, 15 Apr.. invoke login method after getting auth request obj as return type
        // To form auth request, inject whatever is needed, contetx etc, Then start activity from here
        if(pimoidcConfigration != null) {
            AuthorizationRequest authRequest = pimLoginManager.oidcLogin(mContext,mBundle);
            AuthorizationService authService = new AuthorizationService(mContext);
            Intent authReqIntent = authService.getAuthorizationRequestIntent(authRequest);
            startActivityForResult(authReqIntent, 100);
        }else {
            mLoggingInterface.log(DEBUG,TAG,"OIDC Configuration not available");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mLoggingInterface.log(DEBUG,TAG,"onActivityResult : " + requestCode);
        if (resultCode == RESULT_CANCELED) {
            pbPimRequestProgress.setVisibility(View.GONE);
            tvPimReqStatus.setVisibility(View.GONE);
        } else {
            pbPimRequestProgress.setVisibility(View.VISIBLE);
            tvPimReqStatus.setVisibility(View.VISIBLE);
            tvPimReqStatus.setText("Login in progress...");


            // TODO: Deepthi, 15 Apr below code is not needed, after checking alternative APIs
            AuthorizationResponse response = AuthorizationResponse.fromIntent(data);
            AuthorizationException exception = AuthorizationException.fromIntent(data);

            if(exception != null || response != null) {
                mAuthState = new AuthState(response, exception);
            }

            if (response != null)
                pimLoginManager.exchangeAuthorizationCode(mContext, response, this);

            if (exception != null) {
                mLoggingInterface.log(DEBUG,TAG,"onActivityResult : exception : " + exception.getMessage());
            }
        }
    }

    @Override
    public void onTokenRequestCompleted(@Nullable TokenResponse response, @Nullable AuthorizationException ex) {
        pbPimRequestProgress.setVisibility(View.GONE);
        tvPimReqStatus.setVisibility(View.VISIBLE);
        mAuthState.update(response,ex);
        if (response != null) {
            String msg = "Access Token: \n" + mAuthState.getAccessToken()
                    + "\n\nID Token: \n" + mAuthState.getIdToken()
                    + "\n\nRefresh Token: \n" + mAuthState.getRefreshToken();
            mLoggingInterface.log(DEBUG,TAG, msg);
            tvPimReqStatus.setText("Access Token : " + response.accessToken);
            PIMUserManager pimUserManager = PIMSettingManager.getInstance().getPimUserManager();
            pimUserManager.requestUserProfile(mAuthState);

        } else {
            tvPimReqStatus.setText(ex.errorDescription);
        }
    }
}

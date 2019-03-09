package com.philips.platform.pim.fragment;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.pim.PimActivity;
import com.philips.platform.pim.R;

import net.openid.appauth.AuthorizationRequest;
import net.openid.appauth.AuthorizationService;
import net.openid.appauth.AuthorizationServiceDiscovery;

public class PimFragment extends Fragment {
    private static final String EXTRA_AUTH_SERVICE_DISCOVERY = "authServiceDiscovery";
    private static final String EXTRA_AUTH_STATE = "authState";
    AuthorizationService mAuthService;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    public void getFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.udi_fragment, container, false);
        //login(pimAuthManager);
        return view;
    }
//
//    private void login(PimAuthManager pimAuthManager, PimUserManager pimUserManager) {
//        init();
//    }


//    private void init(Bundle savedInstanceState) {
//        mAuthService = new AuthorizationService(this.getContext());
//
//        if (savedInstanceState != null) {
//            if (savedInstanceState.containsKey(KEY_USER_INFO)) {
//                try {
//                    PimLog.d("PimFragment", "Grabbing userInfo from savedInstance");
//                    mUserInfoJson = new JSONObject(savedInstanceState.getString(KEY_USER_INFO));
//                } catch (JSONException e) {
//                    PimLog.e("PimFragment", "Failed to parse saved user info JSON" + e);
//                }
//            }
//        }
//
//        Intent intent = getActivity().getIntent();
//        if (intent != null) {
//            if (mAuthState == null) {
//                AuthorizationResponse response = AuthorizationResponse.fromIntent(intent);
//                AuthorizationException exception = AuthorizationException.fromIntent(intent);
//
//                // Check for creation, if not - create
//                if (response != null || exception != null) {
//                    mAuthState = new AuthState(response, exception);
//                }
//                if (response != null) {
//                    Log.d(TAG, "Received AuthorizationResponse");
//                    exchangeAuthorizationCode(response);
//                } else {
//                    Log.d(TAG, "Authorization failed: " + exception);
//                }
//            }
//        }
//    }

    public PendingIntent createPostAuthorizationIntent(
            @NonNull AuthorizationRequest request,
            @Nullable AuthorizationServiceDiscovery discoveryDoc
    ) {
        Intent intent = new Intent(this.getContext().getApplicationContext(), PimActivity.class);
        intent.putExtra(EXTRA_AUTH_STATE, discoveryDoc.docJson.toString());
        if (discoveryDoc != null) {
            intent.putExtra(EXTRA_AUTH_SERVICE_DISCOVERY, discoveryDoc.docJson.toString());
        }

        return PendingIntent.getActivity(this.getContext().getApplicationContext(), request.hashCode(), intent, 0);
    }

}

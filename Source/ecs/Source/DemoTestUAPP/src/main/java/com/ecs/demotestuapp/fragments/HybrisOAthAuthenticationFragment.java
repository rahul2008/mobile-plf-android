package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.jsonmodel.SubgroupItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;

public class HybrisOAthAuthenticationFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    String refreshToken = "refreshToken";

    EditText etSecret,etClient,etOAuthID;

    String secret,client,AuthID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);
        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);

        if(ECSDataHolder.INSTANCE.getJanrainID()!=null){
            refreshToken = ECSDataHolder.INSTANCE.getJanrainID();
        }

        etSecret = linearLayout.findViewWithTag("et_one");
        etSecret.setText("secret");

        etClient = linearLayout.findViewWithTag("et_two");
        etClient.setText("mobile_android");

        etOAuthID = linearLayout.findViewWithTag("et_three");
        etOAuthID.setText(refreshToken);


        btn_execute = rootView.findViewById(R.id.btn_execute);
        progressBar = rootView.findViewById(R.id.progressBar);

        btn_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                executeRequest();
            }
        });

        Button btnClear = rootView.findViewById(R.id.btn_clear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ECSDataHolder.INSTANCE.setEcsoAuthData(null);
            }
        });

        return rootView;
    }

    private void executeRequest() {


            secret =getTextFromEditText(etSecret);
            client  = getTextFromEditText(etClient);
            AuthID= getTextFromEditText(etOAuthID);



        ECSOAuthProvider ecsoAuthProvider = new ECSOAuthProvider() {
            @Override
            public String getOAuthID() {
                return AuthID;
            }

            @Override
            public String getClientID() {
                return client;
            }

            @Override
            public String getClientSecret() {
                return secret;
            }
        };

        ECSDataHolder.INSTANCE.getEcsServices().hybrisOAthAuthentication(ecsoAuthProvider, new ECSCallback<ECSOAuthData, Exception>() {
            @Override
            public void onResponse(ECSOAuthData ecsoAuthData) {

                ECSDataHolder.INSTANCE.setEcsoAuthData(ecsoAuthData);
                gotoResultActivity(getJsonStringFromObject(ecsoAuthData));
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e, ecsError);
                gotoResultActivity(errorString);
                progressBar.setVisibility(View.GONE);
            }
        });

    }

}

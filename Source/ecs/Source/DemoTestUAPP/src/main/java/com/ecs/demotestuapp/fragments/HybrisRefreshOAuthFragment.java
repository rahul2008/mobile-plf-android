package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.jsonmodel.SubgroupItem;
import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.integration.ECSOAuthProvider;
import com.philips.cdp.di.ecs.model.oauth.ECSOAuthData;
import com.philips.cdp.di.ecs.model.products.ECSProduct;

public class HybrisRefreshOAuthFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    String refreshToken = "refreshToken";

    EditText etSecret,etClient,etOAuthID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);
        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);


        etSecret = linearLayout.findViewWithTag("secret");
        etClient = linearLayout.findViewWithTag("mobile_android");
        etOAuthID = linearLayout.findViewWithTag(refreshToken);

        btn_execute = rootView.findViewById(R.id.btn_execute);
        progressBar = rootView.findViewById(R.id.progressBar);

        btn_execute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                executeRequest();
            }
        });

        return rootView;
    }

    private void executeRequest() {

        String secret = etSecret.getText().toString().trim();
        String client = etClient.getText().toString().trim();
        String AuthID = etOAuthID.getText().toString().trim();

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

        ECSDataHolder.INSTANCE.getEcsServices().hybrisRefreshOAuth(ecsoAuthProvider, new ECSCallback<ECSOAuthData, Exception>() {
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

    private void inflateLayout(LinearLayout linearLayout, SubgroupItem subgroupItem) {

        int noOfEditText = subgroupItem.getEditText();
        int noOFSpinner = subgroupItem.getSpinner();
        int noButton = subgroupItem.getButton();

        for (int i = 0; i < noOfEditText; i++) {

            EditText myEditText = new EditText(getActivity());
            myEditText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setTag(getTag(i));
            myEditText.setText(getTag(i));

            linearLayout.addView(myEditText);
        }

        for (int i = 0; i < noOFSpinner; i++) {

            Spinner spinner = new Spinner(getActivity());
            spinner.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(spinner);
        }


        for (int i = 0; i < noButton; i++) {

            Button button = new Button(getActivity());
            button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(button);
        }

    }

    private String getTag(int i) {

        if(ECSDataHolder.INSTANCE.getEcsoAuthData()!=null){
            refreshToken = ECSDataHolder.INSTANCE.getEcsoAuthData().getRefreshToken();
        }

        switch (i) {

            case 0:
                return "mobile_android";
            case 1:
                return "secret";
            case 2:
                return refreshToken;

        }
        return refreshToken;
    }

}


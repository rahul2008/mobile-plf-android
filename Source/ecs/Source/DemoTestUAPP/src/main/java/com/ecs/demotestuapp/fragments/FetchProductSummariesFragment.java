package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
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
import com.philips.cdp.di.ecs.model.products.ECSProduct;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FetchProductSummariesFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    EditText etCTN;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);
        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);


        etCTN = linearLayout.findViewWithTag("ctn");

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

        String ctn = etCTN.getText().toString().trim();

        String[] split = ctn.split(",");

        List<String> al = new ArrayList<String>();
        al = Arrays.asList(split);

        ECSDataHolder.INSTANCE.getEcsServices().fetchProductSummaries(al, new ECSCallback<List<ECSProduct>, Exception>() {
            @Override
            public void onResponse(List<ECSProduct> ecsProducts) {
                gotoResultActivity(getJsonStringFromObject(ecsProducts));
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
            myEditText.setTag("ctn");
            myEditText.setHint(getTag(i));

            if(ECSDataHolder.INSTANCE.getEcsProducts()!=null){
                if(ECSDataHolder.INSTANCE.getEcsProducts().getProducts().size()!=0){
                    myEditText.setText(ECSDataHolder.INSTANCE.getEcsProducts().getProducts().get(0).getCode());
                }
            }

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
        switch (i) {

            case 0:
                return "Enter comma separated CTNS";

        }
        return null;
    }

}


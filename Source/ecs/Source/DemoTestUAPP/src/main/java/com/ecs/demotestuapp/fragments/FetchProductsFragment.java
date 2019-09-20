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
import com.philips.cdp.di.ecs.model.products.ECSProducts;

public class FetchProductsFragment extends BaseFragment {

    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    EditText etPageNumber,etPageSize;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);
        

        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout,subgroupItem);

        etPageNumber = linearLayout.findViewWithTag("currentPage");
        etPageSize = linearLayout.findViewWithTag("pageSize");
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

        int  pageSize = Integer.valueOf(etPageNumber.getText().toString().trim());
        int  pageNumber = Integer.valueOf(etPageNumber.getText().toString().trim());

        ECSDataHolder.INSTANCE.getEcsServices().fetchProducts(pageNumber, pageSize, new ECSCallback<ECSProducts, Exception>() {
            @Override
            public void onResponse(ECSProducts ecsProducts) {
                gotoResultActivity(getJsonStringFromObject(ecsProducts));
                ECSDataHolder.INSTANCE.setEcsProducts(ecsProducts);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Exception e, ECSError ecsError) {

                String errorString = getFailureString(e,ecsError);
                gotoResultActivity(errorString);
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    private void inflateLayout(LinearLayout linearLayout, SubgroupItem subgroupItem) {

        int noOfEditText =  subgroupItem.getEditText();
        int noOFSpinner = subgroupItem.getSpinner();
        int noButton = subgroupItem.getButton();

        for (int i=0;i<noOfEditText;i++){

            EditText myEditText = new EditText(getActivity());
            myEditText.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
            myEditText.setTag(getTag(i));
            myEditText.setHint(getTag(i));

            linearLayout.addView(myEditText);
        }

        for (int i=0;i<noOFSpinner;i++){

            Spinner spinner = new Spinner(getActivity());
            spinner.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(spinner);
        }


        for (int i=0;i<noButton;i++){

            Button button = new Button(getActivity());
            button.setLayoutParams(new ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            linearLayout.addView(button);
        }

    }

    private String getTag(int i) {
        switch (i){

            case 0:
                return "currentPage";
            case 1:
                return "pageSize";

        }
        return null;
    }

}

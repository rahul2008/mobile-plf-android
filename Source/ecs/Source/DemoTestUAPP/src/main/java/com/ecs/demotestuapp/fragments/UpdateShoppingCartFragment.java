package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.philips.cdp.di.ecs.model.cart.ECSEntries;
import com.philips.cdp.di.ecs.model.cart.ECSShoppingCart;
import com.philips.cdp.di.ecs.model.products.ECSProduct;

import java.util.ArrayList;
import java.util.List;

public class UpdateShoppingCartFragment extends BaseFragment {


    private LinearLayout linearLayout;
    private SubgroupItem subgroupItem;

    private Button btn_execute;
    private ProgressBar progressBar;

    EditText etQuantity;

    Spinner spinner;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.input_fragment, container, false);
        linearLayout = rootView.findViewById(R.id.ll_container);

        Bundle bundle = getActivity().getIntent().getExtras();
        subgroupItem = (SubgroupItem) bundle.getSerializable("sub_group");
        inflateLayout(linearLayout, subgroupItem);


        etQuantity = linearLayout.findViewWithTag("quantity");
        spinner = linearLayout.findViewWithTag("spinner");

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

        int quantity = 0;
        try {
            quantity = Integer.valueOf(etQuantity.getText().toString().trim());
        } catch (Exception e) {

        }

        String entryNumber = "123";

        if(spinner.getSelectedItem()!=null){
            entryNumber = spinner.getSelectedItem().toString();
        }

        ECSEntries ecsEntriesFromID = getECSEntriesFromID(entryNumber);

        ECSDataHolder.INSTANCE.getEcsServices().updateShoppingCart(quantity, ecsEntriesFromID, new ECSCallback<ECSShoppingCart, Exception>() {
            @Override
            public void onResponse(ECSShoppingCart ecsShoppingCart) {

                ECSDataHolder.INSTANCE.setEcsShoppingCart(ecsShoppingCart);
                gotoResultActivity(getJsonStringFromObject(ecsShoppingCart));
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
            myEditText.setHint(getTag(i));
            myEditText.setInputType(InputType.TYPE_CLASS_NUMBER);


            linearLayout.addView(myEditText);
        }
        for (int i = 0; i < noOFSpinner; i++) {

            Spinner spinner = new Spinner(getActivity());

            ArrayList<String> ctns = new ArrayList<>();

            if (ECSDataHolder.INSTANCE.getEcsProducts() != null) {

                List<ECSEntries> entries = ECSDataHolder.INSTANCE.getEcsShoppingCart().getEntries();
                if (entries.size() != 0) {

                    for (ECSEntries ecsEntries : entries) {
                        ctns.add(ecsEntries.getEntryNumber() + "");
                    }

                    fillSpinner(spinner, ctns);
                }
            }

            spinner.setTag("spinner");
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
                return "quantity";

        }
        return null;
    }

    public void fillSpinner(Spinner spinner, List<String> ctns) {
        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter aa = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, ctns);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spinner.setAdapter(aa);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });
    }

    private ECSEntries getECSEntriesFromID(String ctn) {

        if(ECSDataHolder.INSTANCE.getEcsShoppingCart()==null) return  null;

        List<ECSEntries> entries = ECSDataHolder.INSTANCE.getEcsShoppingCart().getEntries();
        if (entries.size() != 0) {

            for (ECSEntries ecsEntries : entries) {
                if (ctn.equalsIgnoreCase(ecsEntries.getEntryNumber() + "")) {
                    return ecsEntries;
                }
            }

        }
        return null;
    }
}

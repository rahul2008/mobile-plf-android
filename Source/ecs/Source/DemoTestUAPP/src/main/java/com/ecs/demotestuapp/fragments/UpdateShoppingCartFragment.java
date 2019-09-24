package com.ecs.demotestuapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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


        etQuantity = linearLayout.findViewWithTag("et_one");
        spinner = linearLayout.findViewWithTag("spinner_one");

        fillSpinnerData(spinner);
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


    private void fillSpinnerData(Spinner spinner) {
        ArrayList<String> ctns = new ArrayList<>();

        if (ECSDataHolder.INSTANCE.getEcsProducts() != null) {

            List<ECSEntries> entries = ECSDataHolder.INSTANCE.getEcsShoppingCart().getEntries();

            if (entries!=null && entries.size() != 0) {

                for (ECSEntries ecsEntries : entries) {
                    ctns.add(ecsEntries.getEntryNumber() + "");
                }

                fillSpinner(spinner, ctns);
            }
        }
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

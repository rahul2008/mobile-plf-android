package com.ecs.demotestuapp.fragments;

import android.view.View;
import android.widget.Spinner;

import com.ecs.demotestuapp.util.ECSDataHolder;
import com.philips.cdp.di.ecs.error.ECSError;
import com.philips.cdp.di.ecs.integration.ECSCallback;
import com.philips.cdp.di.ecs.model.products.ECSProduct;

import java.util.ArrayList;
import java.util.List;

public class FetchProductDetailsFragment extends BaseAPIFragment {


    Spinner spinner;
    String ctn = "xyz";

    @Override
    public void onResume() {
        super.onResume();

        spinner = getLinearLayout().findViewWithTag("spinner_one");
        fillSpinnerData(spinner);
    }

    public void executeRequest() {

        if(spinner.getSelectedItem()!=null) {
             ctn = spinner.getSelectedItem().toString();
        }

            ECSProduct ecsProduct = getECSProductFromID(ctn);

            ECSDataHolder.INSTANCE.getEcsServices().fetchProductDetails(ecsProduct, new ECSCallback<ECSProduct, Exception>() {
                @Override
                public void onResponse(ECSProduct ecsProduct) {
                    gotoResultActivity(getJsonStringFromObject(ecsProduct));
                    getProgressBar().setVisibility(View.GONE);
                }

                @Override
                public void onFailure(Exception e, ECSError ecsError) {
                    String errorString = getFailureString(e, ecsError);
                    gotoResultActivity(errorString);
                    getProgressBar().setVisibility(View.GONE);
                }
            });
    }



    private void fillSpinnerData(Spinner spinner) {
        ArrayList<String> ctns = new ArrayList<>();

        if(ECSDataHolder.INSTANCE.getEcsProducts()!=null){

            List<ECSProduct> products = ECSDataHolder.INSTANCE.getEcsProducts().getProducts();
            if(products.size()!=0) {

                for(ECSProduct ecsProduct:products){
                    ctns.add(ecsProduct.getCode());
                }

                fillSpinner(spinner,ctns);
            }
        }
    }


    private ECSProduct getECSProductFromID(String ctn) {

        List<ECSProduct> ecsProducts = ECSDataHolder.INSTANCE.getEcsProducts().getProducts();

        for(ECSProduct ecsProduct:ecsProducts){
            if(ecsProduct.getCode().equalsIgnoreCase(ctn)){
                return ecsProduct;
            }
        }
        return ecsProducts.get(0);
    }


    @Override
    public void clearData() {

    }
}

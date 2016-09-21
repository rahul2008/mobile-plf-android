/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.widget.Toast;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.PRLaunchInput;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.modularui.statecontroller.UIStateData;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.launcher.UiLauncher;

import java.util.ArrayList;
import java.util.List;

public class ProductRegistrationState extends UIState implements ProdRegUiListener {

    private ArrayList<String> ctnList = null;
    Context activityContext;
    private FragmentLauncher fragmentLauncher;

    public ProductRegistrationState(@UIStateDef int stateID){
        super(stateID);
    }

    @Override
    public void init(UiLauncher uiLauncher) {
        fragmentLauncher = (FragmentLauncher) uiLauncher;
    }

    @Override
    protected void navigate(Context context) {
        activityContext = context;
        runProductRegistration();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    private Product loadProduct() {
        if (ctnList == null) {
            ctnList = ((ProductRegistrationData)getUiStateData()).getCtnList();
        }
        String[] ctnList = new String[this.ctnList.size()];
        for (int i = 0; i < this.ctnList.size(); i++) {
            ctnList[i] = this.ctnList.get(i);
        }
        Product product = new Product(ctnList[0], Sector.B2C, Catalog.CONSUMER);
        product.setSerialNumber("");
        product.setPurchaseDate("");
        product.setFriendlyName("");
        product.sendEmail(false);
        return product;
    }

    @Override
    public void onProdRegContinue(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    @Override
    public void onProdRegBack(List<RegisteredProduct> list, UserWithProducts userWithProducts) {

    }

    @Override
    public void onProdRegFailed(ProdRegError prodRegError) {
        Toast.makeText(activityContext,""+ProdRegError.USER_NOT_SIGNED_IN,Toast.LENGTH_SHORT).show();

    }

    public void runProductRegistration(){
        ArrayList<Product> products = new ArrayList<>();
        products.add(loadProduct());
        PRLaunchInput prodRegLaunchInput;
        prodRegLaunchInput = new PRLaunchInput(products, false);
        prodRegLaunchInput.setProdRegUiListener(this);
        new PRInterface().launch(fragmentLauncher,prodRegLaunchInput);
    }

    /**
     * Data Model for CoCo is defined here to have minimal import files.
     */
    public class ProductRegistrationData extends UIStateData {
        private ArrayList<String> mCtnList = null;

        public ArrayList<String> getCtnList() {
            return mCtnList;
        }

        public void setCtnList(ArrayList<String> mCtnList) {
            this.mCtnList = mCtnList;
        }
    }
}

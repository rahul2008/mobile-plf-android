/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/
package com.philips.platform.modularui.stateimpl;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.philips.cdp.localematch.enums.Catalog;
import com.philips.cdp.localematch.enums.Sector;
import com.philips.cdp.prodreg.constants.ProdRegError;
import com.philips.cdp.prodreg.launcher.PRInterface;
import com.philips.cdp.prodreg.launcher.ProdRegLaunchInput;
import com.philips.cdp.prodreg.listener.ProdRegUiListener;
import com.philips.cdp.prodreg.register.Product;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.prodreg.register.UserWithProducts;
import com.philips.platform.appframework.AppFrameworkBaseActivity;
import com.philips.platform.appframework.R;
import com.philips.platform.appframework.homescreen.HomeActivity;
import com.philips.platform.modularui.statecontroller.UIState;
import com.philips.platform.uappframework.launcher.FragmentLauncher;
import com.philips.platform.uappframework.listener.ActionBarListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductRegistrationState extends UIState implements ProdRegUiListener {

    private ArrayList<String> mCtnList = null;
    private FragmentActivity mFragmentActivity = null;
    Context mContext;
    int containerID;
    private FragmentActivity fa;
    private ActionBarListener actionBarListener;

    public ProductRegistrationState(@UIStateDef int stateID){
        super(stateID);
    }
    @Override
    protected void navigate(Context context) {
        mContext = context;
        mFragmentActivity = (HomeActivity) context;
        actionBarListener  = (HomeActivity) context;
        runProductRegistration();
    }

    @Override
    public void back(final Context context) {
        ((AppFrameworkBaseActivity)context).popBackTillHomeFragment();
    }

    private Product loadProduct() {
        if (mCtnList == null) {
            mCtnList = new ArrayList<>(Arrays.asList(mFragmentActivity.getResources().getStringArray(R.array.productselection_ctnlist)));
        }
        String[] ctnList = new String[mCtnList.size()];
        for (int i = 0; i < mCtnList.size(); i++) {
            ctnList[i] = mCtnList.get(i);
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
        Toast.makeText(mContext,""+ProdRegError.USER_NOT_SIGNED_IN,Toast.LENGTH_SHORT).show();

    }

    public void runProductRegistration(){
        ArrayList<Product> products = new ArrayList<>();
        products.add(loadProduct());
        ProdRegLaunchInput prodRegLaunchInput;
        if(mContext instanceof HomeActivity){
            containerID = R.id.frame_container;
            fa = (HomeActivity)mContext;
        }
        FragmentLauncher fragLauncher = new FragmentLauncher(fa, containerID,actionBarListener);
        fragLauncher.setCustomAnimation(0, 0);
        prodRegLaunchInput = new ProdRegLaunchInput(products, false);
        prodRegLaunchInput.setProdRegUiListener(this);
        new PRInterface().launch(fragLauncher,prodRegLaunchInput);
    }
}

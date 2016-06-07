package com.philips.cdp.prodreg.adaptor;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.prodreg.fragments.ProdRegProductsFragment;
import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.cdp.product_registration_lib.R;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

public class ProdRegListRowHolder extends RecyclerView.ViewHolder {
    protected TextView mCtn;
    protected TextView mSerailNumber;
    protected TextView mStatus;
    protected TextView mErrorStatus;

    public ProdRegListRowHolder(final View itemView) {
        super(itemView);
        this.mCtn = (TextView) itemView.findViewById(R.id.txt_ctn);
        this.mSerailNumber = (TextView) itemView.findViewById(R.id.txt_serialno);
        this.mStatus = (TextView) itemView.findViewById(R.id.txt_status);
        this.mErrorStatus = (TextView) itemView.findViewById(R.id.txt_errorstatus);
    }

    public void bind(final RegisteredProduct registeredProduct, final ProdRegProductsFragment.OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(registeredProduct);
            }
        });
    }
}

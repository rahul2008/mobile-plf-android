package com.philips.platform.prdemoapp.adaptor;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

import com.philips.cdp.prodreg.register.RegisteredProduct;
import com.philips.platform.prdemoapp.fragment.ProductListFragment;
import com.philips.platform.prdemoapplibrary.R;
import com.philips.platform.uid.view.widget.Label;

/* Copyright (c) Koninklijke Philips N.V., 2016
* All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

public class ListRowHolder extends RecyclerView.ViewHolder {
    protected Label mCtn;
    protected Label mSerailNumber;
    protected Label mStatus;
    protected Label mErrorStatus;


    public ListRowHolder(final View itemView) {
        super(itemView);
        this.mCtn = (Label) itemView.findViewById(R.id.txt_ctn);
        this.mSerailNumber = (Label) itemView.findViewById(R.id.txt_serialno);
        this.mStatus = (Label) itemView.findViewById(R.id.txt_status);
        this.mErrorStatus = (Label) itemView.findViewById(R.id.txt_errorstatus);
    }

    public void bind(final RegisteredProduct registeredProduct, final ProductListFragment.OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(registeredProduct);
            }
        });
    }
}

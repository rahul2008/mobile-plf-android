package com.philips.cdp.prodreg;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.philips.cdp.prodreg.register.RegisteredProduct;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class ListRowHolder extends RecyclerView.ViewHolder {
    protected TextView mCtn;
    protected TextView mSerailNumber;
    protected TextView mStatus;
    protected TextView mErrorStatus;


    public ListRowHolder(final View itemView) {
        super(itemView);
        this.mCtn = (TextView) itemView.findViewById(R.id.txt_ctn);
        this.mSerailNumber = (TextView) itemView.findViewById(R.id.txt_serialno);
        this.mStatus = (TextView) itemView.findViewById(R.id.txt_status);
        this.mErrorStatus = (TextView) itemView.findViewById(R.id.txt_errorstatus);
    }

    public void bind(final RegisteredProduct registeredProduct, final RegisteredProductsList.OnItemClickListener listener) {

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(registeredProduct);
            }
        });
    }
}

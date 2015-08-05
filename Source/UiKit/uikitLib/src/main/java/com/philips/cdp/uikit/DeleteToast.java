package com.philips.cdp.uikit;

import android.content.Context;
import android.widget.Toast;

/**
 * (C) Koninklijke Philips N.V., 2015.
 * All rights reserved.
 */
public class DeleteToast {

    Context context;

    public DeleteToast(Context context) {
        this.context = context;
    }

   public void showToast(String message){
       Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
   }
}

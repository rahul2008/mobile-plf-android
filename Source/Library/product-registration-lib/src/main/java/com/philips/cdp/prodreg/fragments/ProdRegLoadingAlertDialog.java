package com.philips.cdp.prodreg.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;

import com.philips.cdp.product_registration_lib.R;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Label;

/**
 * Created by 310230979 on 2/8/2017.
 */

public class ProdRegLoadingAlertDialog {
    private static AlertDialog alertDialogBuilder;
    public static void showProdRegLoadingDialog(String title, Activity activity) {
        alertDialogBuilder = new AlertDialog.Builder(activity).create();
        alertDialogBuilder.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialogBuilder.setCancelable(false);
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.prodreg_progress_dialog, null);
        Label titleView = (Label) view.findViewById(R.id.dialogDescription);
        titleView.setText(title);
        alertDialogBuilder.setView(view);
        alertDialogBuilder.show();
    }
    public static void dismissProdRegLoadingDialog(){
        if(alertDialogBuilder!=null){
                    alertDialogBuilder.dismiss();
        }
    }

    public static void dlsdialog(String title, final Activity activity) {
        AlertDialogFragment.Builder fragment = new AlertDialogFragment.Builder(activity.getApplicationContext())
                .setTitle(title)
                .setNegativeButton(R.string.reg_Continue_Btntxt, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.finish();
                    }
                });
    }

}

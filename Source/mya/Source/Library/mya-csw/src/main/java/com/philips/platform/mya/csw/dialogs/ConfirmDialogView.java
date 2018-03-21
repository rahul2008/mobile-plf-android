package com.philips.platform.mya.csw.dialogs;


import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.philips.platform.mya.csw.R;
import com.philips.platform.uid.thememanager.UIDHelper;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;
import com.philips.platform.uid.view.widget.Button;
import com.philips.platform.uid.view.widget.Label;

public class ConfirmDialogView {

    public interface ConfirmDialogResultHandler {
        void onOkClicked();
        void onCancelClicked();
    }

    protected View view;
    protected AlertDialogFragment alertDialogFragment;
    private int titleTextRes;
    private int descriptionTextRes;
    private int okButtonTextRes;
    private int cancelButtonTextRes;
    private ConfirmDialogResultHandler resultHandler;

    private View.OnClickListener buttonClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(resultHandler == null) return;

            if(view.getId() == R.id.mya_csw_confirm_dialog_button_ok) {
                resultHandler.onOkClicked();
            }
            else if(view.getId() == R.id.mya_csw_confirm_dialog_button_cancel) {
                resultHandler.onCancelClicked();
            }
            alertDialogFragment.dismiss();
        }
    };

    public void setupDialog(int titleRes, int descriptionRes, int okButtonRes, int cancelButtonRes) {
        this.titleTextRes = titleRes;
        this.descriptionTextRes = descriptionRes;
        this.okButtonTextRes = okButtonRes;
        this.cancelButtonTextRes = cancelButtonRes;
    }

    public void setResultHandler(ConfirmDialogResultHandler resultHandler) {
        this.resultHandler = resultHandler;
    }

    public void showDialog(FragmentActivity activity) {
        validateTextResources();

        Context popupThemedContext = getStyledContext(activity);
        setupView(activity, popupThemedContext);
        populateViews();

        view.findViewById(R.id.mya_csw_confirm_dialog_button_ok).setOnClickListener(buttonClickListener);
        view.findViewById(R.id.mya_csw_confirm_dialog_button_cancel).setOnClickListener(buttonClickListener);


        AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(activity)
                .setDialogView(view)
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDimLayer(DialogConstants.DIM_STRONG)
                .setCancelable(false);

        alertDialogFragment = createDialogFragment(builder);
        alertDialogFragment.show(activity.getSupportFragmentManager(),"Show dialog");
    }

    protected AlertDialogFragment createDialogFragment(AlertDialogFragment.Builder builder) {
        return builder.create(new AlertDialogFragment());
    }

    protected void setupView(FragmentActivity activity, Context popupThemedContext) {
        view = LayoutInflater
                .from(activity)
                .cloneInContext(popupThemedContext)
                .inflate(R.layout.csw_dialog_confirm, null, false);
    }

    private void validateTextResources() {
        if(titleTextRes <= 0 || descriptionTextRes <= 0 || okButtonTextRes <= 0 || cancelButtonTextRes <= 0) {
            throw new IllegalStateException("Must call setupDialog() first!");
        }
    }

    protected void populateViews() {
        Label titleLabel = view.findViewById(R.id.mya_csw_confirm_dialog_title);
        titleLabel.setText(this.titleTextRes);

        Label descrLabel = view.findViewById(R.id.mya_csw_confirm_dialog_description);
        descrLabel.setText(this.descriptionTextRes);

        Button okButton = view.findViewById(R.id.mya_csw_confirm_dialog_button_ok);
        okButton.setText(this.okButtonTextRes);

        Button cancelButton = view.findViewById(R.id.mya_csw_confirm_dialog_button_cancel);
        cancelButton.setText(this.cancelButtonTextRes);
    }

    protected Context getStyledContext(FragmentActivity activity) {
        return UIDHelper.getPopupThemedContext(activity);
    }

}

package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentDialogBinding;
import com.philips.platform.uid.utils.DialogConstants;
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DialogComponentFragment extends BaseFragment implements View.OnClickListener {

    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";

    public ObservableBoolean isSubtle = new ObservableBoolean(TRUE);
    public ObservableBoolean isAlertWithTitle = new ObservableBoolean(FALSE);
    public ObservableBoolean isDialogWithList = new ObservableBoolean(FALSE);

    private AlertDialogFragment alertDialogFragment;
    private FragmentDialogBinding fragmentDialogBinding;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        fragmentDialogBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog, container, false);
        fragmentDialogBinding.setFragment(this);
        return fragmentDialogBinding.getRoot();
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //This needs to be taken care by proposition to avoid losing listener on rotation
        final AlertDialogFragment alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);
            alertDialogFragment.setNegativeButtonListener(this);
            alertDialogFragment.setAlternateButtonListener(this);
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("isSubtle", isSubtle.get());
        outState.putBoolean("isAlertWithTitle", isAlertWithTitle.get());
        outState.putBoolean("isDialogWithList", isDialogWithList.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            setIsSubtle(savedInstanceState.getBoolean("isSubtle"));
            setIsAlertWithTitle(savedInstanceState.getBoolean("isAlertWithTitle"));
            setIsDialogWithList(savedInstanceState.getBoolean("isDialogWithList"));
        }
        super.onViewStateRestored(savedInstanceState);
    }

    public void showDialog() {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_DIALOG)
                .setDialogLayout(isDialogWithList.get()? R.layout.dialog_check_box_list : R.layout.dialog_label)
                .setPositiveButton((isDialogWithList.get() ? R.string.dialog_with_list_positive_button_text : R.string.dialog_positive_button_text), this)
                .setNegativeButton((isDialogWithList.get() ? R.string.dialog_with_list_negative_button_text : R.string.dialog_negative_button_text), this)
                .setDimLayer(isSubtle.get() ? DialogConstants.DIM_SUBTLE : DialogConstants.DIM_STRONG)
                .setDividers(isDialogWithList.get())
                .setTitle(isDialogWithList.get() ? R.string.dialog_with_list_title_text : R.string.dialog_title_text)
                .setCancelable(false);
        alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    public void showAlert() {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setDialogType(DialogConstants.TYPE_ALERT)
                .setMessage(isAlertWithTitle.get() ? R.string.alert_long_content_text : R.string.alert_short_content_text)
                .setPositiveButton(R.string.alert_positive_button_text, this)
                .setNegativeButton(R.string.alert_negative_button_text, this)
                .setDimLayer(isSubtle.get() ? DialogConstants.DIM_SUBTLE : DialogConstants.DIM_STRONG)
                .setCancelable(false);
        if (isAlertWithTitle.get()) {
            builder.setTitle(R.string.alert_title_text)
                    .setIcon(R.drawable.ic_delete_trash);
        }
        alertDialogFragment = builder.create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_alertDialog;
    }

    @Override
    public void onClick(final View v) {
        if (alertDialogFragment != null) {
            alertDialogFragment.dismiss();
        } else {
            final AlertDialogFragment wtf = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
            wtf.dismiss();
        }
    }

    public void setIsSubtle(boolean isSubtle) {
        this.isSubtle.set(isSubtle);
    }

    public void setIsAlertWithTitle(boolean isAlertWithTitle) {
        this.isAlertWithTitle.set(isAlertWithTitle);
    }

    public void setIsDialogWithList(boolean isDialogWithList) {
        this.isDialogWithList.set(isDialogWithList);
    }
}

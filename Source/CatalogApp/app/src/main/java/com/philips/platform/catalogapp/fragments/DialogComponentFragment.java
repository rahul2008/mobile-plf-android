package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.AlertDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DialogComponentFragment extends BaseFragment {

    @Bind(R.id.radio_content_size)
    RadioGroup contentSize;

    @Bind(R.id.switch_with_title)
    SwitchCompat switchWithTitle;

    @Bind(R.id.checkbox_title_with_icon)
    CheckBox checkBoxWithIcon;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dialog, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(final View view, @Nullable final Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final AlertDialogFragment wtf = (AlertDialogFragment) getFragmentManager().findFragmentByTag("WTF");
        if (wtf != null) {
            wtf.setCancelListener(getOnClickListener(wtf));
        }
    }

    @OnClick(R.id.dialog_with_text)
    public void onDialogWithTextClicked() {
        isLargeContent();
        isWithTitle();
        isWithTitleWithIcon();
    }

    @OnClick(R.id.dialog_with_list)
    public void onDialogWithListClicked() {
        final AlertDialogFragment alertDialog = new AlertDialogFragment();
        alertDialog.setTitle(getString(R.string.dialog_screen_title_text));
        alertDialog.setMessage(getString(R.string.dialog_screen_short_content_text));
        alertDialog.setPositiveButton(R.string.dialog_screen_positive_button_text, getOnClickListener(alertDialog));
        alertDialog.setNegativeButton(R.string.dialog_screen_negative_button_text, getOnClickListener(alertDialog));
        alertDialog.setIconResourceId(android.R.drawable.ic_menu_mylocation, true);
        alertDialog.show(getFragmentManager(), "WTF");
    }

    @NonNull
    private View.OnClickListener getOnClickListener(final AlertDialogFragment alertDialog) {
        return new MyOnClickListener(alertDialog);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_alertDialog;
    }

    public boolean isWithTitle() {
        return switchWithTitle.isChecked();
    }

    public boolean isWithTitleWithIcon() {
        return switchWithTitle.isChecked() && checkBoxWithIcon.isChecked();
    }

    public boolean isLargeContent() {
        return (contentSize.getCheckedRadioButtonId() == R.id.long_content) ? true : false;
    }

    private static class MyOnClickListener implements View.OnClickListener {
        private final AlertDialogFragment alertDialog;

        public MyOnClickListener(final AlertDialogFragment alertDialog) {
            this.alertDialog = alertDialog;
        }

        @Override
        public void onClick(final View v) {
            alertDialog.dismiss();
        }
    }
}

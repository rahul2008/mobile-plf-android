package com.philips.platform.catalogapp.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.uit.view.widget.AlertDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

public class DialogComponentFragment extends BaseFragment implements View.OnClickListener {

    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";
    @Bind(R.id.radio_content_size)
    RadioGroup contentSize;

    @Bind(R.id.toggle_switch_with_title)
    SwitchCompat switchWithTitle;

    @Bind(R.id.checkbox_title_with_icon)
    CheckBox checkBoxWithIcon;

    private AlertDialogFragment alertDialogFragment;

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

        //This needs to be taken care by proposition to avoid losing listener on rotation
        final AlertDialogFragment alertDialogFragment = (AlertDialogFragment) getFragmentManager().findFragmentByTag(ALERT_DIALOG_TAG);
        if (alertDialogFragment != null) {
            alertDialogFragment.setPositiveButtonListener(this);
            alertDialogFragment.setNegativeButtonListener(this);
        }
    }

    @OnClick(R.id.dialog_with_text)
    public void onDialogWithTextClicked() {
        showDialog(isLargeContent(), isWithTitle(), isWithIcon());
    }

    @OnClick(R.id.dialog_with_list)
    public void onDialogWithListClicked() {
    }

    private void showDialog(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setMessage(showLargeContent ? R.string.dialog_screen_long_content_text : R.string.dialog_screen_short_content_text).
                        setPositiveButton(R.string.dialog_screen_positive_button_text, this).
                        setNegativeButton(R.string.dialog_screen_negative_button_text, this);
        if (isWithTitle) {
            builder.setTitle(R.string.dialog_screen_title_text);
            if (showIcon) {
                builder.setIcon(R.drawable.ic_location);
            }
        }
        alertDialogFragment = builder.setCancelable(true).create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_tittle_alertDialog;
    }

    public boolean isWithTitle() {
        return switchWithTitle.isChecked();
    }

    public boolean isWithIcon() {
        return checkBoxWithIcon.isChecked();
    }

    @OnCheckedChanged(R.id.toggle_switch_with_title)
    public void onCheckedChange(CompoundButton button, boolean isChecked) {
        checkBoxWithIcon.setEnabled(isChecked ? true : false);
    }

    public boolean isLargeContent() {
        return contentSize.getCheckedRadioButtonId() == R.id.long_content ? true : false;
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
}

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
import com.philips.platform.uid.view.widget.AlertDialogFragment;

import static java.lang.Boolean.TRUE;

public class DialogComponentFragment extends BaseFragment implements View.OnClickListener {

    public static final String ALERT_DIALOG_TAG = "ALERT_DIALOG_TAG";

    public ObservableBoolean withLongContent = new ObservableBoolean(TRUE);
    public ObservableBoolean withTitle = new ObservableBoolean(TRUE);
    public ObservableBoolean withIcon = new ObservableBoolean(TRUE);

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
        }
    }

    public void onDialogWithTextClicked() {
        showDialog(isLargeContent(), isWithTitle(), isWithIcon());
    }

    private void showDialog(final boolean showLargeContent, final boolean isWithTitle, final boolean showIcon) {
        final AlertDialogFragment.Builder builder = new AlertDialogFragment.Builder(getContext())
                .setMessage(showLargeContent ? R.string.dialog_screen_long_content_text : R.string.dialog_screen_short_content_text).
                        setPositiveButton(R.string.dialog_screen_positive_button_text, this).
                        setNegativeButton(R.string.dialog_screen_negative_button_text, this);
        if (isWithTitle) {
            builder.setTitle(R.string.dialog_screen_title_text);
            if (showIcon) {
                builder.setIcon(R.drawable.alert_dialog_icon);
            }
        }
        alertDialogFragment = builder.setCancelable(false).create();
        alertDialogFragment.show(getFragmentManager(), ALERT_DIALOG_TAG);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_alertDialog;
    }

    public boolean isWithTitle() {
        return withTitle.get();
    }

    public boolean isWithIcon() {
        return withIcon.get();
    }

    public boolean isLargeContent() {
        return withLongContent.get() ? true : false;
    }

    public void setWithTitle(boolean withtitle) {
        this.withTitle.set(withtitle);
        fragmentDialogBinding.checkboxTitleWithIcon.setEnabled(withTitle.get());
    }

    public void setWithIcon(boolean withIcon) {
        this.withIcon.set(withIcon);
    }

    public void setWithLongContent(boolean withIcon) {
        this.withLongContent.set(withIcon);
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

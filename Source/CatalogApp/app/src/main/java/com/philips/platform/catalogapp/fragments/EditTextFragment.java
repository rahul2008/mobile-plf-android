/*
 * (C) Koninklijke Philips N.V., 2016.
 * All rights reserved.
 *
 */
package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableBoolean;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentEdittextBinding;

public class EditTextFragment extends BaseFragment {

    FragmentEdittextBinding texteditboxBinding;
    public ObservableBoolean disableEditBoxes = new ObservableBoolean(Boolean.FALSE);
    public ObservableBoolean isWithLabel = new ObservableBoolean(Boolean.TRUE);
    public ObservableBoolean isInlineValidation = new ObservableBoolean(Boolean.FALSE);

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        texteditboxBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_edittext, container, false);
        texteditboxBinding.setTexteditBoxfragment(this);
        return texteditboxBinding.getRoot();
    }

    private void restoreStates(Bundle savedInstance) {
        if (savedInstance != null) {
            disabledEditboxes(savedInstance.getBoolean("disableEditBoxes"));
            showWithLabel(savedInstance.getBoolean("isWithLabel"));
        }
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putBoolean("disableEditBoxes", disableEditBoxes.get());
        outState.putBoolean("isWithLabel", isWithLabel.get());
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        restoreStates(savedInstanceState);
        super.onViewStateRestored(savedInstanceState);
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_textbox;
    }

    public void disabledEditboxes(boolean toggle) {
        disableEditBoxes.set(toggle);
        //View is null in case of rotation
        final View view = getView();
        if(toggle){
            texteditboxBinding.textboxInputField.getErrorLayout().setVisibility(View.GONE);
        }else {
            texteditboxBinding.textboxInputField.getErrorLayout().setVisibility(View.VISIBLE);
        }

        if (disableEditBoxes.get() && view != null) {
            view.clearFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }

    public void showWithLabel(boolean isChecked) {
        isWithLabel.set(isChecked);
    }


}

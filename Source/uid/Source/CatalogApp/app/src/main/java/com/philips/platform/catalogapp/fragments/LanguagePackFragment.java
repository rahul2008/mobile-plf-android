package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLanguagePackBinding;

public class LanguagePackFragment extends BaseFragment {

    private FragmentLanguagePackBinding fragmentLanguagePackBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_language_pack;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentLanguagePackBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_language_pack, container, false);
        fragmentLanguagePackBinding.setFrag(this);
        return fragmentLanguagePackBinding.getRoot();
    }

    @Override
    public void onStop() {
        super.onStop();
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
    }
}

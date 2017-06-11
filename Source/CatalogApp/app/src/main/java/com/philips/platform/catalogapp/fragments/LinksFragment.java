package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentLinksBinding;

public class LinksFragment extends BaseFragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        FragmentLinksBinding binding = DataBindingUtil.inflate(inflater, R.layout.fragment_links, container, false);
        binding.setFrag(this);
        binding.linkDescription.setMovementMethod(LinkMovementMethod.getInstance());
        return binding.getRoot();
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_links;
    }
}

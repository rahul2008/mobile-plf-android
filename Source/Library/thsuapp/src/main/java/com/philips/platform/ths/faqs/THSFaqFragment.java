package com.philips.platform.ths.faqs;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.ths.R;
import com.philips.platform.ths.base.THSBaseFragment;

import java.util.Map;

public class THSFaqFragment extends THSBaseFragment{
    public static final String TAG = THSFaqFragment.class.getSimpleName();

    THSFaqPresenter mThsFaqPresenter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        ViewGroup view = (ViewGroup) inflater.inflate(R.layout.ths_faqs, container, false);
        mThsFaqPresenter = new THSFaqPresenter(this);
        mThsFaqPresenter.getFaq();
        return view;
    }

    protected void updateFaqs(Map map){

    }
}

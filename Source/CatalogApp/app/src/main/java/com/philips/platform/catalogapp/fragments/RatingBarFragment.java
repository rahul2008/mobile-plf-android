package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentRatingbarBinding;


public class RatingBarFragment extends BaseFragment {

    private FragmentRatingbarBinding fragmentRatingbarBinding;

    @Override
    public int getPageTitle() {
        return R.string.page_title_ratingbar;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentRatingbarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_ratingbar, container, false);
        fragmentRatingbarBinding.setFrag(this);
        return fragmentRatingbarBinding.getRoot();
    }

    public void changeText(){
        fragmentRatingbarBinding.ratingDisplayDefault.setRating(fragmentRatingbarBinding.ratingInput.getRating());
        fragmentRatingbarBinding.ratingDisplayDefault.setText(String.valueOf(fragmentRatingbarBinding.ratingInput.getRating()));
        fragmentRatingbarBinding.ratingDisplayStarOnly.setRating(fragmentRatingbarBinding.ratingInput.getRating());
        fragmentRatingbarBinding.ratingDisplayMini.setText(String.valueOf(fragmentRatingbarBinding.ratingInput.getRating()));
        fragmentRatingbarBinding.ratingDisplayMini.setRating(fragmentRatingbarBinding.ratingInput.getRating());
        fragmentRatingbarBinding.ratingInput.setRating(0);

    }

}

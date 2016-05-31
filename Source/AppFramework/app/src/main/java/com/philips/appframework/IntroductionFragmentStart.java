package com.philips.appframework;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 310240027 on 5/31/2016.
 */
public class IntroductionFragmentStart extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.introduction_screen_start,null);
    }

    public static IntroductionFragmentStart newInstance(int page, String title) {
        IntroductionFragmentStart fragmentFirst = new IntroductionFragmentStart();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }
}

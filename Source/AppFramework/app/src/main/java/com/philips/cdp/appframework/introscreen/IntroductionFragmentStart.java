package com.philips.cdp.appframework.introscreen;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.philips.cdp.appframework.R;

/**
 * Created by 310240027 on 5/31/2016.
 */
public class IntroductionFragmentStart extends Fragment {
    // Store instance variables
    private int page;
    private TextView largeText, smallText;

    public static IntroductionFragmentStart newInstance(int page, String title) {
        IntroductionFragmentStart fragmentFirst = new IntroductionFragmentStart();
        Bundle args = new Bundle();
        args.putInt("pageIndex", page);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.introduction_screen_start, null);
        largeText = (TextView) view.findViewById(R.id.large_text);
        smallText = (TextView) view.findViewById(R.id.small_text);
        switch (page) {
            case 0:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(getResources().getDrawable(R.drawable.introduction_start_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_one_bottom_text));
                break;
            case 1:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(getResources().getDrawable(R.drawable.introduction_center_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_two_bottom_text));
                break;
            case 2:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(getResources().getDrawable(R.drawable.introduction_end_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_three_bottom_text));
                break;
            default:
                view.findViewById(R.id.parent_introduction_fragment_layout).setBackground(getResources().getDrawable(R.drawable.introduction_start_page_bg));
                smallText.setText(getResources().getString(R.string.introduction_screen_one_bottom_text));
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("pageIndex", 0);
    }
}

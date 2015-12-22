package com.philips.cdp.ui.catalog.cardviewpager;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;


public class CardContainerFragment extends Fragment {

    public CardContainerFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_card_container, container, false);

        getChildFragmentManager()
                .beginTransaction()
                .add(R.id.container, new CardFrontFragment())
                .commit();
        return rootView;
    }


    public static class CardFrontFragment extends Fragment {

        private View rootView;

        public CardFrontFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            rootView = inflater.inflate(R.layout.uikit_card_view, container, false);
            setUpView();
            return rootView;
        }


        private void setUpView() {
            final Context mContext = getActivity();

            TypedArray typedArray = mContext.getTheme().obtainStyledAttributes(new int[]{com.philips.cdp.uikit.R.attr.baseColor});
            final int color = typedArray.getColor(0, -1);
            typedArray.recycle();
            ImageView cardImage, crossIcon;
            final TextView linkText;
            cardImage = (ImageView) rootView.findViewById(com.philips.cdp.uikit.R.id.cardImage);
            crossIcon = (ImageView) rootView.findViewById(com.philips.cdp.uikit.R.id.cross);
            linkText = (TextView) rootView.findViewById(com.philips.cdp.uikit.R.id.uikit_cards_link_text);
            cardImage.setImageDrawable(VectorDrawable.create(mContext, com.philips.cdp.uikit.R.drawable.uikit_heart_icon));
            crossIcon.setImageDrawable(VectorDrawable.create(mContext, com.philips.cdp.uikit.R.drawable.uikit_cross_icon));
            crossIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, " Clicked Close Button", Toast.LENGTH_SHORT).show();
                }
            });
            linkText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(mContext, " Clicked Link", Toast.LENGTH_SHORT).show();
                }
            });
            cardImage.getDrawable().mutate().setColorFilter(color, PorterDuff.Mode.SRC_ATOP);
        }
    }
}
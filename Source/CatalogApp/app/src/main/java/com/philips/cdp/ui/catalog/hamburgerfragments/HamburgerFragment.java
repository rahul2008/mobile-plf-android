package com.philips.cdp.ui.catalog.hamburgerfragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.ui.catalog.R;
import com.philips.cdp.uikit.drawable.VectorDrawable;

public class HamburgerFragment extends Fragment {

    private TextView textView;
    private ImageView imageView;

    public HamburgerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        textView = (TextView)rootView.findViewById(R.id.txtLabel);
        imageView = (ImageView) rootView.findViewById(R.id.frag_icon);

        setDateToView();

        return rootView;
    }

    private void setDateToView() {
        Bundle bundle= getArguments();
        textView.setText(bundle.getString("data"));
        imageView.setImageDrawable(VectorDrawable.create(getActivity().getApplicationContext(), bundle.getInt("resId")));
    }
}

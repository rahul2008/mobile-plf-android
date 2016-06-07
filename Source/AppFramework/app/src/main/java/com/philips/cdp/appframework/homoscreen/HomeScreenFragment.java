package com.philips.cdp.appframework.homoscreen;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.philips.cdp.appframework.R;

/**
 * Created by 310213373 on 5/31/2016.
 */
public class HomeScreenFragment extends Fragment {
    private TextView textView;
    private ImageView imageView;

    public HomeScreenFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.home_screen, container, false);
     //   textView = (TextView)rootView.findViewById(R.id.txtLabel);
      //  imageView = (ImageView) rootView.findViewById(R.id.frag_icon);

        setDateToView();

        return rootView;
    }

    private void setDateToView() {
        Bundle bundle= getArguments();
//        textView.setText(bundle.getString("data"));
//        imageView.setImageDrawable(VectorDrawable.create(getActivity().getApplicationContext(), bundle.getInt("resId")));
    }
}

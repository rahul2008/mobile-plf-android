package com.philips.platform.uid.components.buttons;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.uid.view.widget.ProgressIndicatorButton;

public class ButtonsTestFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        final View view = inflater.inflate(com.philips.platform.uid.test.R.layout.layout_progress_on_button, container, false);
        final ProgressIndicatorButton progressIndicatorButton = (ProgressIndicatorButton) view.findViewById(com.philips.platform.uid.test.R.id.progressButtonsNormalDeterminate);
        progressIndicatorButton.showProgressIndicator();

//        final ProgressIndicatorButton progressIndicatorIndeterminateButton = (ProgressIndicatorButton) view.findViewById(com.philips.platform.uid.test.R.id.progressButtonsNormalIndeterminate);
//        progressIndicatorIndeterminateButton.showProgressIndicator();
        return view;
    }
}

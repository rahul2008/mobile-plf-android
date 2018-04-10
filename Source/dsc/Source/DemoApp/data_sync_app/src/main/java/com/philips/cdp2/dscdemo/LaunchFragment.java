package com.philips.cdp2.dscdemo;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * A simple {@link Fragment} subclass.
 */
public class LaunchFragment extends Fragment {

    private View.OnClickListener launchClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            LauncherActivity activity = (LauncherActivity) getActivity();
            if(view.getId() == R.id.dscdemo_launcher_button_activity) {
                // Launch as Activity
                activity.launchAsActivity();
            }
            else if(view.getId() == R.id.dscdemo_launcher_button_fragment) {
                // Launch as fragment
                activity.launchAsFragment();
            }
        }
    };

    public LaunchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_launch, container, false);

        // Bind click listeners to Buttons
        fragmentView.findViewById(R.id.dscdemo_launcher_button_activity).setOnClickListener(launchClickListener);
        fragmentView.findViewById(R.id.dscdemo_launcher_button_fragment).setOnClickListener(launchClickListener);

        return fragmentView;
    }
}

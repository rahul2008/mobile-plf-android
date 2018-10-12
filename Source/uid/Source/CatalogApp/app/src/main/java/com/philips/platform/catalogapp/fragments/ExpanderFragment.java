package com.philips.platform.catalogapp.fragments;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.philips.platform.catalogapp.R;

import com.philips.platform.catalogapp.databinding.FragmentExpanderBinding;
import com.philips.platform.uid.view.widget.Expander;


public class ExpanderFragment extends BaseFragment {
    FragmentExpanderBinding expanderBinding;
    LinearLayout LinearLayoutTitle;
    LinearLayout LinearLayoutContent;
    Expander expander2;


    @Override
    public int getPageTitle() {
        return R.string.page_title_expander;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        expanderBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_expander, container, false);

        expanderBinding.setExpanderFragmentTag(this);
        return expanderBinding.getRoot();

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {

       // LinearLayoutTitle =  (LinearLayout) expanderBinding.catalogExpander1.findViewById(R.id.uid_expander_view_title);
      //  LinearLayoutContent =  (LinearLayout) expanderBinding.catalogExpander1.findViewById(R.id.uid_expander_view_content);
      //  expander2 =  expanderBinding.catalogExpander1.findViewById(R.id.catalog_expander2);
       // catalogExpander1.setDefaultExpander(getContext());

       /* if(expanderBinding.catalogExpanderOne!=null)
        {
            expanderBinding.catalogExpanderOne.setDefaultExpander(getContext());
            expanderBinding.catalogExpanderOne.setTitle("Expander 1");
        }
*/

    }


}

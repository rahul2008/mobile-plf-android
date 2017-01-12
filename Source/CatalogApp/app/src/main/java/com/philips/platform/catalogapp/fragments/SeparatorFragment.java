/*
 * (C) Koninklijke Philips N.V., 2017.
 * All rights reserved.
 *
 */

package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataModelView;
import com.philips.platform.catalogapp.Datamodel;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentSeparatorBinding;
import com.philips.platform.uid.view.widget.RecyclerViewDividerItemDecoration;

public class SeparatorFragment extends BaseFragment {
    @Override
    public int getPageTitle() {
        return R.string.page_title_separator;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        DataModelView dataModelView = new DataModelView();

        final Context context = getContext();
        dataModelView.addUser(R.drawable.ic_add_folder, R.string.title1, context);
        dataModelView.addUser(R.drawable.ic_alarm, R.string.title2, context);
        dataModelView.addUser(R.drawable.ic_bottle, R.string.title3, context);
        dataModelView.addUser(R.drawable.ic_home, R.string.title4, context);
        dataModelView.addUser(R.drawable.ic_lock, R.string.title5, context);
        dataModelView.addUser(R.drawable.ic_location_icon, R.string.title6, context);

        final FragmentSeparatorBinding fragmentSeparatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_separator, container, false);
        fragmentSeparatorBinding.setFragment(this);
        fragmentSeparatorBinding.setDatamodelview(dataModelView);
        fragmentSeparatorBinding.activityUsersRecycler.addItemDecoration(new RecyclerViewDividerItemDecoration(getContext()));
        fragmentSeparatorBinding.activityUsersRecycler.setAdapter(new MyAdapter(dataModelView.datamodels));
        fragmentSeparatorBinding.activityUsersRecycler.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentSeparatorBinding.getRoot();
    }

    public static class MyAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<Datamodel> datamodels;

        public MyAdapter(final ObservableArrayList<Datamodel> datamodels) {
            this.datamodels = datamodels;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
            BindingHolder holder = new BindingHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
            final Datamodel datamodel = datamodels.get(position);
            ((BindingHolder) holder).getBinding().setVariable(1, datamodel);
            ((BindingHolder) holder).getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return datamodels.size();
        }

        private static class BindingHolder extends RecyclerView.ViewHolder {
            private ViewDataBinding binding;

            public BindingHolder(View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }
}

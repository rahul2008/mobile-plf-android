package com.philips.platform.catalogapp.fragments;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.databinding.ViewDataBinding;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.philips.platform.catalogapp.DataHolder;
import com.philips.platform.catalogapp.DataHolderView;
import com.philips.platform.catalogapp.R;
import com.philips.platform.catalogapp.databinding.FragmentRecyclerviewBinding;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

public class RecyclerViewFragment extends BaseFragment {

    private FragmentRecyclerviewBinding fragmentRecyclerviewBinding;

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        DataHolderView dataHolderView = new DataHolderView();

        final Context context = getContext();
        dataHolderView.addUser(R.drawable.ic_add_folder, R.string.title1, context);
        dataHolderView.addUser(R.drawable.ic_home, R.string.title2, context);
        dataHolderView.addUser(R.drawable.ic_lock, R.string.title3, context);
        dataHolderView.addUser(R.drawable.ic_alarm, R.string.title4, context);
        dataHolderView.addUser(R.drawable.ic_bottle, R.string.title5, context);
        dataHolderView.addUser(R.drawable.ic_location, R.string.title6, context);

        fragmentRecyclerviewBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_recyclerview, container, false);
        fragmentRecyclerviewBinding.setFrag(this);

        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().setAdapter(new RecyclerViewAdapter(dataHolderView.dataHolders));
        fragmentRecyclerviewBinding.recyclerviewRecyclerview.getRecyclerView().setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentRecyclerviewBinding.getRoot();
    }

    static class RecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        public RecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_two_line_text_item, parent, false);
            RecyclerViewAdapter.BindingHolder holder = new RecyclerViewAdapter.BindingHolder(v);

            return holder;
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((RecyclerViewAdapter.BindingHolder) holder).getBinding().executePendingBindings();
        }

        @Override
        public int getItemCount() {
            return dataHolders.size();
        }

        static class BindingHolder extends RecyclerView.ViewHolder {
            private ViewDataBinding binding;

            public BindingHolder(@NonNull View rowView) {
                super(rowView);
                binding = DataBindingUtil.bind(rowView);
            }

            public ViewDataBinding getBinding() {
                return binding;
            }
        }
    }

    @Override
    public int getPageTitle() {
        return R.string.page_title_recyclerview;
    }
}

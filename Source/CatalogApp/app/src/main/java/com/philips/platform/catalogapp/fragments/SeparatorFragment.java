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
import com.philips.platform.catalogapp.databinding.FragmentSeparatorBinding;
import com.philips.platform.uid.view.widget.RecyclerViewSeparatorItemDecoration;

public class SeparatorFragment extends BaseFragment {
    @Override
    public int getPageTitle() {
        return R.string.page_title_separator;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {

        DataHolderView dataHolderView = new DataHolderView();

        final Context context = getContext();
        dataHolderView.addIconItem(R.drawable.ic_add_folder, R.string.title1, context);
        dataHolderView.addIconItem(R.drawable.ic_home, R.string.title2, context);
        dataHolderView.addIconItem(R.drawable.ic_lock, R.string.title3, context);
        dataHolderView.addIconItem(R.drawable.ic_alarm, R.string.title4, context);
        dataHolderView.addIconItem(R.drawable.ic_bottle, R.string.title5, context);
        dataHolderView.addIconItem(R.drawable.ic_location, R.string.title6, context);

        final FragmentSeparatorBinding fragmentSeparatorBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_separator, container, false);
        fragmentSeparatorBinding.recyclerviewSeparatorItems.addItemDecoration(new RecyclerViewSeparatorItemDecoration(getContext()));
        fragmentSeparatorBinding.recyclerviewSeparatorItems.setAdapter(new SeparatorRecyclerViewAdapter(dataHolderView.dataHolders));
        fragmentSeparatorBinding.recyclerviewSeparatorItems.setLayoutManager(new LinearLayoutManager(getContext()));

        return fragmentSeparatorBinding.getRoot();
    }

    static class SeparatorRecyclerViewAdapter extends RecyclerView.Adapter {
        private ObservableArrayList<DataHolder> dataHolders;

        public SeparatorRecyclerViewAdapter(@NonNull final ObservableArrayList<DataHolder> dataHolders) {
            this.dataHolders = dataHolders;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_data, parent, false);
            return new BindingHolder(v);
        }

        @Override
        public void onBindViewHolder(@NonNull final RecyclerView.ViewHolder holder, final int position) {
            final DataHolder dataHolder = dataHolders.get(position);
            ((BindingHolder) holder).getBinding().setVariable(1, dataHolder);
            ((BindingHolder) holder).getBinding().executePendingBindings();

            ((BindingHolder) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View v) {
                    boolean isSelected = holder.itemView.isSelected();
                    holder.itemView.setSelected(!isSelected);
                }
            });
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
}

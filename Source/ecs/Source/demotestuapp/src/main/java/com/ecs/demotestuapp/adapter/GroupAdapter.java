package com.ecs.demotestuapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ecs.demotestuapp.R;
import com.ecs.demotestuapp.activity.SubGroupActivity;
import com.ecs.demotestuapp.jsonmodel.GroupItem;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {

    private List<GroupItem> groupItemList;
    private final Context context;

    // RecyclerView recyclerView;
    public GroupAdapter(List<GroupItem> groupItemList, Context context) {
        this.groupItemList = groupItemList;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_recycler, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final GroupItem groupItem = groupItemList.get(position);
        holder.textView.setText(groupItem.getName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoSubGroupActivity(groupItem);
            }
        });

    }

    private void gotoSubGroupActivity(GroupItem groupItem) {
        Intent intent = new Intent(context, SubGroupActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("group",groupItem);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    @Override
    public int getItemCount() {
        return groupItemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public  View itemView ;

        public ViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.textView = (TextView) itemView.findViewById(R.id.tv_item);
        }
    }
}
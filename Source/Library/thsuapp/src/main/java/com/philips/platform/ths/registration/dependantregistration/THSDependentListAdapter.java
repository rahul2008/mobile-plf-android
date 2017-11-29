/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.registration.dependantregistration;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.philips.platform.ths.R;
import com.philips.platform.ths.utility.THSManager;
import com.philips.platform.uid.view.widget.Label;

import java.util.List;

public class THSDependentListAdapter extends RecyclerView.Adapter<THSDependentListAdapter.CustomViewHolder>{

    List<THSConsumer> mDependents;
    Context context;
    OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    public THSDependentListAdapter(Context activity) {
        mDependents = THSManager.getInstance().getThsParentConsumer(activity).getDependents();
        context = activity;
    }

    @Override
    public THSDependentListAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ths_practice_row, null);
        THSDependentListAdapter.CustomViewHolder viewHolder = new THSDependentListAdapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(THSDependentListAdapter.CustomViewHolder holder, final int position) {
        final THSConsumer thsConsumer = mDependents.get(position);
        holder.label.setText(thsConsumer.getFirstName());
        showProfilePic(holder, thsConsumer);

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnItemClickListener.onItemClick(thsConsumer);
            }
        };
        holder.relativeLayout.setOnClickListener(listener);
    }

    private void showProfilePic(CustomViewHolder holder, THSConsumer thsConsumer) {
        if (thsConsumer.getProfilePic() != null) {
            try{
                Bitmap b = BitmapFactory.decodeStream(thsConsumer.getProfilePic());
                if (b != null) {
                    b.setDensity(Bitmap.DENSITY_NONE);
                    Drawable d = new BitmapDrawable(context.getResources(), b);
                    holder.logo.setImageDrawable(d);
                }
                holder.logo.setImageResource(R.mipmap.child_icon);
            }catch (Exception ex){
                holder.logo.setImageResource(R.mipmap.child_icon);
            }
        } else {
            showProfilePicAsInitials(holder, thsConsumer);
        }
    }

    private void showProfilePicAsInitials(CustomViewHolder holder, THSConsumer thsConsumer) {
        String firstName = "",lastName = "";
        if(null != thsConsumer.getFirstName()){
            firstName = String.valueOf(thsConsumer.getFirstName().charAt(0));
        }

        if(null != thsConsumer.getLastName()){
            lastName = String.valueOf(thsConsumer.getLastName().charAt(0));
        }
        String nameInitials = firstName.toUpperCase() + lastName.toUpperCase();
        holder.logo.setVisibility(View.GONE);
        holder.initials.setVisibility(View.VISIBLE);
        holder.initials.setText(nameInitials);

    }
    @Override
    public int getItemCount() {
        return mDependents.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        protected ImageView logo;
        protected TextView label;
        protected RelativeLayout relativeLayout;
        protected Label initials;

        public CustomViewHolder(View view) {
            super(view);
            this.initials = view.findViewById(R.id.ths_practice_initials);
            this.logo = view.findViewById(R.id.pth_practice_logo);
            this.relativeLayout =  view.findViewById(R.id.pth_practice_row_layout);
            this.label =  view.findViewById(R.id.pth_practice_name);

        }
    }
}

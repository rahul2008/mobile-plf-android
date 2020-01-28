/* Copyright (c) Koninklijke Philips N.V., 2017
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
*/

package com.philips.platform.appframework.homescreen;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.appframework.R;
import com.philips.platform.appframework.models.HamburgerMenuItem;
import com.philips.platform.uid.view.widget.Label;

import java.util.ArrayList;

public class HamburgerMenuAdapter extends RecyclerView.Adapter<HamburgerMenuAdapter.HamburgerMenuViewHolder> {

    private ArrayList<HamburgerMenuItem> hamburgerMenuItems;
    private LayoutInflater inflater;
    private int selectedPosition = 0;
    private HamburgerMenuItemClickListener hamburgerMenuItemClickListener;

    public void setMenuItemClickListener(HamburgerMenuItemClickListener hamburgerMenuItemClickListener) {
        this.hamburgerMenuItemClickListener = hamburgerMenuItemClickListener;
    }

    public void removeMenuItemClickListener() {
        this.hamburgerMenuItemClickListener = null;
    }

    public HamburgerMenuAdapter(@NonNull final ArrayList<HamburgerMenuItem> hamburgerMenuItems) {
        this.hamburgerMenuItems = hamburgerMenuItems;
    }

    @Override
    public HamburgerMenuViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int viewType) {
        inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.rap_hamburger_menu_item, parent, false);

        return new HamburgerMenuViewHolder(v);
    }

    public HamburgerMenuItem getMenuItem(int position) {
        return hamburgerMenuItems.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull final HamburgerMenuViewHolder holder, final int position) {
        final HamburgerMenuItem hamburgerMenuItem = getMenuItem(position);

        holder.menuTitle.setText(hamburgerMenuItem.getTitle());
        holder.menuTitle.setTag(position);
        holder.menuIcon.setImageDrawable(hamburgerMenuItem.getIcon());


        holder.itemView.post(new Runnable() {
            @Override
            public void run() {
                holder.itemView.setSelected(selectedPosition == position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return hamburgerMenuItems.size();
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        notifyDataSetChanged();
    }


    protected class HamburgerMenuViewHolder extends RecyclerView.ViewHolder {

        public Label menuTitle;
        public ImageView menuIcon;

        HamburgerMenuViewHolder(@NonNull View rowView) {
            super(rowView);
            menuTitle = (Label) rowView.findViewById(R.id.rap_menu_title);
            menuIcon = (ImageView) rowView.findViewById(R.id.rap_menu_icon);

            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setSelectedPosition((int) menuTitle.getTag());
                    //notifyDataSetChanged();

                    if (hamburgerMenuItemClickListener != null) {
                        hamburgerMenuItemClickListener.onMenuItemClicked((int) menuTitle.getTag());
                    }
                }
            });
        }

    }
}

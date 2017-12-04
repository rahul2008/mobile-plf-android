/* Copyright (c) Koninklijke Philips N.V., 2016
 * All rights are reserved. Reproduction or dissemination
 * in whole or in part is prohibited without the prior written
 * consent of the copyright holder.
 */

package com.philips.platform.ths.intake.selectimage;

import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ths.R;
import com.philips.platform.uid.view.widget.ProgressBar;

import java.util.List;

public class THSImageRecyclerViewAdapter extends RecyclerView.Adapter<THSImageRecyclerViewAdapter.THSImageRecyclerViewHolder> {

    private List<THSSelectedImagePojo> imagesList;
    final int THUMBSIZE = 96;
    private THSSelectedImageCallback selectedImageCallback;
    public boolean isClickable;

    public THSImageRecyclerViewAdapter(List<THSSelectedImagePojo> imagesList, THSSelectedImageCallback selectedImageCallback) {
        this.imagesList = imagesList;
        this.selectedImageCallback = selectedImageCallback;
    }

    @Override
    public THSImageRecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ths_image_recycler_list, null);
        THSImageRecyclerViewHolder thsImageRecyclerViewHolder = new THSImageRecyclerViewHolder(view);
        return thsImageRecyclerViewHolder;
    }

    @Override
    public void onBindViewHolder(THSImageRecyclerViewHolder holder, final int position) {
        THSSelectedImagePojo selectImage = imagesList.get(holder.getAdapterPosition());
        if(!selectImage.isUploaded()){
            holder.progressBar.setVisibility(View.VISIBLE);
        }
        else {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(!isClickable){
                        return;
                    }
                    selectedImageCallback.onImageClicked(position);
                }
            });

            holder.progressBar.setVisibility(View.GONE);
        }
        holder.imageView.setImageBitmap(ThumbnailUtils
                .extractThumbnail(BitmapFactory.decodeFile(selectImage.getPath()),
                        THUMBSIZE, THUMBSIZE));

    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class THSImageRecyclerViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;
        protected ProgressBar progressBar;

        public THSImageRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.symptom_image);
            progressBar = (ProgressBar) itemView.findViewById(R.id.imageUploadProgressBar);
        }
    }
}

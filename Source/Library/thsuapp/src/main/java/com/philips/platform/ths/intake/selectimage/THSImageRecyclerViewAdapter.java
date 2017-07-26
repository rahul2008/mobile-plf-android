package com.philips.platform.ths.intake.selectimage;

import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.philips.platform.ths.R;

import java.util.List;

/**
 * Created by philips on 7/21/17.
 */

public class THSImageRecyclerViewAdapter extends RecyclerView.Adapter<THSImageRecyclerViewAdapter.THSImageRecyclerViewHolder> {

    private List<THSSelectedImagePojo> imagesList;
    final int THUMBSIZE = 96;
    private THSSelectedImageCallback selectedImageCallback;

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
        THSSelectedImagePojo selectImage = imagesList.get(position);
        holder.imageView.setImageBitmap(ThumbnailUtils
                .extractThumbnail(BitmapFactory.decodeFile(selectImage.getPath()),
                        THUMBSIZE, THUMBSIZE));
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedImageCallback.onImageClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imagesList.size();
    }

    public class THSImageRecyclerViewHolder extends RecyclerView.ViewHolder {

        protected ImageView imageView;

        public THSImageRecyclerViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.symptom_image);
        }
    }
}

package com.philips.cdp.uikit.customviews;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.philips.cdp.uikit.R;
import com.philips.cdp.uikit.SpringBoardItems;

import java.util.ArrayList;

public class SpringBoardCustomAdapter extends BaseAdapter{

    String [] result;
    Context context;
    Drawable [] imageId;
    ArrayList<SpringBoardItems> mArrayList =new ArrayList<SpringBoardItems>();
    private static LayoutInflater inflater=null;
    public SpringBoardCustomAdapter(Context con, ArrayList<SpringBoardItems> al) {
        // TODO Auto-generated constructor stub

        context=con;
        this.mArrayList=al;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return 8;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.uikit_springboard_fullblocks, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(mArrayList.get(position).getmString());
        holder.img.setImageDrawable(mArrayList.get(position).getmImage());

        rowView.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
            }
        });

        return rowView;
    }

}
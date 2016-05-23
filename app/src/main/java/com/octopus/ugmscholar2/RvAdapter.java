package com.octopus.ugmscholar2;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.koushikdutta.ion.Ion;

import java.util.List;

/**
 * Created by octopus on 20/04/16.
 */
public class RvAdapter extends RecyclerView.Adapter<RvAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(ItemData item);
    }
    private final OnItemClickListener listener;

    // Usually involves inflating a layout from XML and returning the holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView title, author;
        public ImageView img;;


        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            img = (ImageView) itemView.findViewById(R.id.img);

        }
        public void bind(final ItemData data, final OnItemClickListener listener) {
            title.setText(data.getTitle());
            author.setText("Diposting oleh : "+ data.getAuthor() + " pada  "+ data.getTgl());
            Ion.with(img)
                    .error(R.drawable.beasiswa_default)
                    .load(data.getImgUrl());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(data);
                }
            });

        }

    }



    private List<ItemData> datas;

    // Pass in the contact array into the constructor
    public RvAdapter(List<ItemData> datas, OnItemClickListener listener) {
        this.datas = datas;
        this.listener = listener;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View rvView = inflater.inflate(R.layout.rv_layout, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(rvView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RvAdapter.ViewHolder viewHolder, int position) {
        ItemData data = datas.get(position);
        viewHolder.bind(data,listener);
    }

    @Override
    public int getItemCount() {
        return datas.size();

    }
}
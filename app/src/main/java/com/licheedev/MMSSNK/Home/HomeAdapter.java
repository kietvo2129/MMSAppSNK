package com.licheedev.MMSSNK.Home;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;


public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.ExampleViewHolder> {
    private ArrayList<HomeItem> mHomeList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView Hometextview;
        public TextView Hometitel;
        public TextView Homecontenner;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            Hometextview = itemView.findViewById(R.id.HomeTextview);
            Hometitel = itemView.findViewById(R.id.Hometitle);
            Homecontenner = itemView.findViewById(R.id.Homecontener);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });


        }
    }

    public HomeAdapter(ArrayList<HomeItem> exampleList) {
        mHomeList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_item,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        HomeItem currentItem = mHomeList.get(position);

        holder.Hometextview.setText(currentItem.getImageResource());
        holder.Hometitel.setText(currentItem.getText1());
        holder.Homecontenner.setText(currentItem.getText2());
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

    public void filterList(ArrayList<HomeItem> filteredList) {
        mHomeList = filteredList;
        notifyDataSetChanged();
    }
}

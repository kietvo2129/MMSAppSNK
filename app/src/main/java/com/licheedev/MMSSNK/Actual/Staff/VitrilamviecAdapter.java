package com.licheedev.MMSSNK.Actual.Staff;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;


public class VitrilamviecAdapter extends RecyclerView.Adapter<VitrilamviecAdapter.ExampleViewHolder> {
    private ArrayList<VitriLamviecMaster> mHomeList;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder {
        TextView name_vt_lv;

        public ExampleViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);
            name_vt_lv = itemView.findViewById(R.id.name_vt_lv);

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

    public VitrilamviecAdapter(ArrayList<VitriLamviecMaster> exampleList) {
        mHomeList = exampleList;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vitri_lamviec,
                parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        VitriLamviecMaster currentItem = mHomeList.get(position);

        holder.name_vt_lv.setText(currentItem.getName());
    }

    @Override
    public int getItemCount() {
        return mHomeList.size();
    }

}

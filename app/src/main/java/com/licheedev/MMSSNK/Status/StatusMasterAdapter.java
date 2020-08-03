package com.licheedev.MMSSNK.Status;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;

public class StatusMasterAdapter extends RecyclerView.Adapter<StatusMasterAdapter.CheckerLessViewHolder> {

    private ArrayList<StatusMaster> items;

    public StatusMasterAdapter.OnItemClickListener listener;
    public interface OnItemClickListener {
        void onItemClick(int position);
        void onItemCheck(int position);
    }

    public void setOnItemClickListener(StatusMasterAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }

    public StatusMasterAdapter(ArrayList<StatusMaster> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public StatusMasterAdapter.CheckerLessViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_status_fragment, parent, false);
        StatusMasterAdapter.CheckerLessViewHolder evh = new StatusMasterAdapter.CheckerLessViewHolder(v, listener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusMasterAdapter.CheckerLessViewHolder holder, int position) {
        StatusMaster currentItem = items.get(position);

        //holder.textView_sub_1.setText(currentItem.getStt()+".");
        holder.tv_title1.setText(currentItem.getProunit_cd()+currentItem.getLine_no());

        holder.tv_title2.setText(currentItem.getProduct_code());
        holder.tv_target.setText(currentItem.getProd_qty());
        holder.tv_actual.setText(currentItem.getDone_qty());
        holder.tv_title4.setText(currentItem.getCompost_cd());
        holder.backg.setCardBackgroundColor(Color.parseColor(currentItem.getColor()));


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class CheckerLessViewHolder extends RecyclerView.ViewHolder {
        TextView tv_title1, tv_title2,tv_target,tv_actual,tv_title4;
        CardView backg;

        public CheckerLessViewHolder(View itemView, final StatusMasterAdapter.OnItemClickListener listener) {
            super(itemView);

            tv_title1 = itemView.findViewById(R.id.tv_title1);
            tv_title2 = itemView.findViewById(R.id.tv_title2);

            tv_target = itemView.findViewById(R.id.tv_target);
            tv_actual = itemView.findViewById(R.id.tv_actual);
            tv_title4 = itemView.findViewById(R.id.tv_title4);
            backg = itemView.findViewById(R.id.backg);

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

            tv_title1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemCheck(position);
                        }
                    }
                }
            });


        }
    }
    public void filterList(ArrayList<StatusMaster> filteredList) {
        items = filteredList;
        notifyDataSetChanged();
    }

}

package com.licheedev.MMSSNK.PQC;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;


public class QcListAdapter extends RecyclerView.Adapter<QcListAdapter.QcListViewHolder> {
    private ArrayList<QcListItem> qcListItems;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
        void onItemFunc(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    public static class QcListViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_stt;
        public TextView tv_subject;
        public TextView tv_typer;
        public Button bt_qc_check;

        public QcListViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            tv_stt = itemView.findViewById(R.id.tv_stt);
            tv_subject = itemView.findViewById(R.id.tv_subject);
            tv_typer = itemView.findViewById(R.id.tv_typer);
            bt_qc_check = itemView.findViewById(R.id.bt_qc_check);

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
            bt_qc_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemFunc(position);
                        }
                    }
                }
            });
        }
    }

    public QcListAdapter(ArrayList<QcListItem> qcListItem) {
        qcListItems = qcListItem;
    }

    @Override
    public QcListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pqc_listview,
                parent, false);
        QcListViewHolder evh = new QcListViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(QcListViewHolder holder, int position) {
        QcListItem currentItem = qcListItems.get(position);

        holder.tv_stt.setText(currentItem.getIcno());
        holder.tv_subject.setText(currentItem.getCheck_subject());
        holder.tv_typer.setText(currentItem.getCheck_type());
    }

    @Override
    public int getItemCount() {
        return qcListItems.size();
    }

}

package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.Thinkness;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;

public class QcCheckThinknessAdapter extends RecyclerView.Adapter<QcCheckThinknessAdapter.QcCheckViewHolder>{
    private ArrayList<QcCheckThinknessItem> qcCheckThinknessItems;

    private QcCheckThinknessAdapter.OnItemClickListener mListener;

    public QcCheckThinknessAdapter(ArrayList<QcCheckThinknessItem> qcitem) {
        qcCheckThinknessItems = qcitem;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
//        void onItemFunc(int position);
//        void onItemCheck(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mListener = listener;
    }

    @NonNull
    @Override
    public QcCheckViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_recycler_thinkness,
                viewGroup, false);
        QcCheckViewHolder evh = new QcCheckViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(QcCheckViewHolder holder, int i) {
        QcCheckThinknessItem currentItem = qcCheckThinknessItems.get(i);

        //holder.pino.setText(currentItem.getPino());
        holder.check_qty.setText(currentItem.getCheck_qty());
        holder.ok_qty.setText(currentItem.getOk_qty());
        holder.defect_qty.setText(currentItem.getDefect_qty());
        holder.defect_rate.setText(currentItem.getDefect_rate());
    }

    @Override
    public int getItemCount() {
        return qcCheckThinknessItems.size();
    }


    public static class QcCheckViewHolder extends RecyclerView.ViewHolder{
        private TextView pino;
        private TextView check_qty;
        private TextView ok_qty;
        private TextView defect_qty;
        private TextView defect_rate;

        public QcCheckViewHolder(View itemView, final OnItemClickListener listener) {
            super(itemView);

            check_qty = itemView.findViewById(R.id.tv_check_qty);
            ok_qty = itemView.findViewById(R.id.tv_pass_qty);
            defect_qty = itemView.findViewById(R.id.tv_defect_qty);
            defect_rate = itemView.findViewById(R.id.tv_defect_rate);

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
}

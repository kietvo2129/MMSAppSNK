package com.licheedev.MMSSNK.QC_check;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class QcCheckAdapter extends BaseAdapter {

    private Context context;
    private int layout;
    private List<QcCheckItem> qcCheckItems;
    public QcCheckAdapter(Context context, int layout, List<QcCheckItem> qcCheckItem) {
        this.context = context;
        this.layout = layout;
        this.qcCheckItems = qcCheckItem;
    }

    @Override
    public int getCount() {
        return qcCheckItems.size();
    }

    @Override
    public Object getItem(int position) {
        return qcCheckItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return qcCheckItems.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView tv_mq_no,tv_work_date,tv_check_qty,tv_ok_qty,tv_defect_qty;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        QcCheckAdapter.ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new QcCheckAdapter.ViewHolder();
            // anh xa view holder
            holder.tv_mq_no = convertView.findViewById(R.id.tv_mqno);
            holder.tv_work_date = convertView.findViewById(R.id.tv_word_date);
            holder.tv_check_qty = convertView.findViewById(R.id.tv_check_qty);
            holder.tv_ok_qty = convertView.findViewById(R.id.tv_ok_qty);
            holder.tv_defect_qty = convertView.findViewById(R.id.tv_defect_qty);
            convertView.setTag(holder);
        } else {
            holder = (QcCheckAdapter.ViewHolder) convertView.getTag();
        }

        QcCheckItem qcCheckItem = qcCheckItems.get(position);
        holder.tv_mq_no.setText(qcCheckItem.getMq_no());
        holder.tv_work_date.setText(qcCheckItem.getWorkdate());
        holder.tv_check_qty.setText(qcCheckItem.getCheckqty());
        holder.tv_ok_qty.setText(qcCheckItem.getOkqty());
        holder.tv_defect_qty.setText(qcCheckItem.getDefectqty());
        return convertView;
    }

}

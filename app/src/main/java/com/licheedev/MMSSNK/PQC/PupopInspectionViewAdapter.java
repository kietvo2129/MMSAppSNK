package com.licheedev.MMSSNK.PQC;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class PupopInspectionViewAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<PupopInspectionViewItem> items;

    public PupopInspectionViewAdapter(Context context, int layout, List<PupopInspectionViewItem> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView tmqno, tworkdate, tqty;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();

            // anh xa view holder
            holder.tmqno = convertView.findViewById(R.id.tv_popup_mq_no);
            holder.tworkdate = convertView.findViewById(R.id.tv_popup_work_date);
            holder.tqty = convertView.findViewById(R.id.tv_popup_qty);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PupopInspectionViewItem current = items.get(position);

        holder.tmqno.setText(current.getMq_no());
        holder.tworkdate.setText(current.getWorkdate());
        holder.tqty.setText(current.getQty());

        return convertView;
    }

}
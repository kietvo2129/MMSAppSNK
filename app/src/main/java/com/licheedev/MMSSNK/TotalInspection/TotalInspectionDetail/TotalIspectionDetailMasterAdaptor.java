package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class TotalIspectionDetailMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<TotalInspectionDetailMaster> totalInspectionDetailMasters;
     public static ViewHolder holder;

    public TotalIspectionDetailMasterAdaptor(Context context, int layout, List<TotalInspectionDetailMaster> totalInspectionDetailMasters) {
        this.context = context;
        this.layout = layout;
        this.totalInspectionDetailMasters = totalInspectionDetailMasters;
    }

    @Override
    public int getCount() {
        return totalInspectionDetailMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return totalInspectionDetailMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return totalInspectionDetailMasters.indexOf(getItem(position));
    }

    public class ViewHolder{
        TextView pno,prd_lcd,prd_dt,sts,prd_qty,gr_qty,think_def_qty,visual_def_qty,total_def_qty,total_def_rate;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.sts = convertView.findViewById(R.id.tv_status);
            holder.prd_lcd = convertView.findViewById(R.id.tv_PLNO);
            holder.prd_qty = convertView.findViewById(R.id.tv_product_qty);
            holder.gr_qty = convertView.findViewById(R.id.tv_group_qty);
            holder.prd_dt = convertView.findViewById(R.id.tv_date);
            holder.total_def_qty = convertView.findViewById(R.id.tv_total_def_qty);
            holder.total_def_rate = convertView.findViewById(R.id.tv_total_def_rate);
            holder.think_def_qty = convertView.findViewById(R.id.tv_thinkness_def_qty);
            holder.visual_def_qty = convertView.findViewById(R.id.tv_visual_def_qty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TotalInspectionDetailMaster totalInspectionDetailMaster = totalInspectionDetailMasters.get(position);
        holder.sts.setText(totalInspectionDetailMaster.getSts());
        if( totalInspectionDetailMaster.getSts().equals("Not Finish")){
            holder.sts.setBackgroundColor(Color.YELLOW);
            holder.sts.setTextColor(Color.BLACK);
        } else {
            holder.sts.setTextColor(Color.WHITE);
            holder.sts.setBackgroundColor(Color.BLUE);
        }

        holder.prd_lcd.setText(totalInspectionDetailMaster.getPrd_lcd());
        holder.prd_qty.setText(totalInspectionDetailMaster.getPrd_qty());
        holder.gr_qty.setText(totalInspectionDetailMaster.getGr_qty());
        holder.prd_dt.setText(totalInspectionDetailMaster.getPrd_dt());
        holder.total_def_qty.setText(totalInspectionDetailMaster.getTotal_def_qty());
        holder.total_def_rate.setText(totalInspectionDetailMaster.getTotal_def_rate());
        holder.think_def_qty.setText(totalInspectionDetailMaster.getThink_def_qty());
        holder.visual_def_qty.setText(totalInspectionDetailMaster.getVisual_def_qty());
        return convertView;
    }
}

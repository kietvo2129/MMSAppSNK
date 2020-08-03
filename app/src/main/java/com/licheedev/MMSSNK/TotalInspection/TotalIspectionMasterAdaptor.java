package com.licheedev.MMSSNK.TotalInspection;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class TotalIspectionMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<TotalInspectionMaster> totalInspectionMasters;

    public TotalIspectionMasterAdaptor(Context context, int layout, List<TotalInspectionMaster> totalInspectionMasters) {
        this.context = context;
        this.layout = layout;
        this.totalInspectionMasters = totalInspectionMasters;
    }

    @Override
    public int getCount() {
        return totalInspectionMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return totalInspectionMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return totalInspectionMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView pno,fo_no,style_no,start_dt,end_dt,gr_qty,prd_qty,total_check_qty,
                 total_def_qty,total_def_rate,think_def_qty,visual_def_qty,product_cnt,check_cnt,stt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.stt = convertView.findViewById(R.id.tv_status);
            holder.fo_no = convertView.findViewById(R.id.tv_wo);
            holder.style_no = convertView.findViewById(R.id.tv_product);
            holder.prd_qty = convertView.findViewById(R.id.tv_product_qty);
            holder.gr_qty = convertView.findViewById(R.id.tv_group_qty);
            holder.product_cnt = convertView.findViewById(R.id.tv_product_cnt);
            holder.check_cnt = convertView.findViewById(R.id.tv_check_cnt);
            holder.total_def_qty = convertView.findViewById(R.id.tv_total_def_qty);
            holder.total_def_rate = convertView.findViewById(R.id.tv_total_def_rate);
            holder.think_def_qty = convertView.findViewById(R.id.tv_thinkness_def_qty);
            holder.visual_def_qty = convertView.findViewById(R.id.tv_visual_def_qty);
            holder.start_dt = convertView.findViewById(R.id.tv_start_dt);
            holder.end_dt = convertView.findViewById(R.id.tv_end_dt);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        TotalInspectionMaster totalInspectionMaster = totalInspectionMasters.get(position);
        holder.stt.setText(totalInspectionMaster.getStt());
        if( totalInspectionMaster.getStt().equals("Not Finish")){
            holder.stt.setBackgroundColor(Color.YELLOW);
            holder.stt.setTextColor(Color.BLACK);
        } else {
            holder.stt.setTextColor(Color.WHITE);
            holder.stt.setBackgroundColor(Color.BLUE);
        }
        holder.fo_no.setText(convert_view(totalInspectionMaster.getFo_no()));
        holder.style_no.setText(totalInspectionMaster.getStyle_no());
        holder.prd_qty.setText(totalInspectionMaster.getPrd_qty());
        holder.gr_qty.setText(totalInspectionMaster.getGr_qty());
        holder.product_cnt.setText(totalInspectionMaster.getProduct_cnt());
        holder.check_cnt.setText(totalInspectionMaster.getCheck_cnt());
        holder.total_def_qty.setText(totalInspectionMaster.getTotal_def_qty());
        holder.total_def_rate.setText(totalInspectionMaster.getTotal_def_rate());
        holder.think_def_qty.setText(totalInspectionMaster.getThink_def_qty());
        holder.visual_def_qty.setText(totalInspectionMaster.getVisual_def_qty());
        holder.start_dt.setText(totalInspectionMaster.getStart_dt());
        holder.end_dt.setText(totalInspectionMaster.getEnd_dt());
        return convertView;
    }
    private String convert_view(String txt){
        String string = "";
        if (txt.equals("null")){

        }else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }
}

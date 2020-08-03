package com.licheedev.MMSSNK.WO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class PlanMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<PlanMaster> planMasters;

    public PlanMasterAdaptor(Context context, int layout, List<PlanMaster> planMasters) {
        this.context = context;
        this.layout = layout;
        this.planMasters = planMasters;
    }

    @Override
    public int getCount() {
        return planMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return planMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return planMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView p_date,p_process,p_prod_qty;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.p_date = convertView.findViewById(R.id.p_date);
            holder.p_process = convertView.findViewById(R.id.p_process);
            holder.p_prod_qty = convertView.findViewById(R.id.p_prod_qty);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        PlanMaster planMaster = planMasters.get(position);
        holder.p_date.setText(planMaster.getP_date());
        holder.p_process.setText(planMaster.getP_process());
        holder.p_prod_qty.setText(planMaster.getP_prod_qty());
        return convertView;
    }
}

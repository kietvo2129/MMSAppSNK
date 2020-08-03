package com.licheedev.MMSSNK.WO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class ToolsMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<ToolsMaster> toolsMasters;

    public ToolsMasterAdaptor(Context context, int layout, List<ToolsMaster> toolsMasters) {
        this.context = context;
        this.layout = layout;
        this.toolsMasters = toolsMasters;
    }

    @Override
    public int getCount() {
        return toolsMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return toolsMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return toolsMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView prounit_cd,mc_type,mc_no,t_start_dt,t_end_dt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.prounit_cd = convertView.findViewById(R.id.t_process);
            holder.mc_type = convertView.findViewById(R.id.t_machinetype);
            holder.mc_no = convertView.findViewById(R.id.t_machinecode);
            holder.t_start_dt = convertView.findViewById(R.id.t_startdate);
            holder.t_end_dt = convertView.findViewById(R.id.t_enddate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ToolsMaster toolsMaster = toolsMasters.get(position);
        holder.prounit_cd.setText(toolsMaster.getProunit_cd());
        holder.mc_type.setText(toolsMaster.getMc_type());
        holder.mc_no.setText(toolsMaster.getMc_no());
        holder.t_start_dt.setText(toolsMaster.getT_start_dt());
        holder.t_end_dt.setText(toolsMaster.getT_end_dt());
        return convertView;
    }
}

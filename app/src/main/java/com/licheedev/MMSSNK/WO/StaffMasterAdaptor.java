package com.licheedev.MMSSNK.WO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class StaffMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<StaffMaster> staffMasters;

    public StaffMasterAdaptor(Context context, int layout, List<StaffMaster> staffMasters) {
        this.context = context;
        this.layout = layout;
        this.staffMasters = staffMasters;
    }

    @Override
    public int getCount() {
        return staffMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return staffMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return staffMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView prounit_cd,staff_id,uname,dt_nm,s_start_dt, s_end_dt;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.prounit_cd = convertView.findViewById(R.id.s_process);
            holder.staff_id = convertView.findViewById(R.id.s_staffID);
            holder.uname = convertView.findViewById(R.id.s_name);
            holder.dt_nm = convertView.findViewById(R.id.s_type);
            holder.s_start_dt = convertView.findViewById(R.id.s_startdate);
            holder.s_end_dt = convertView.findViewById(R.id.s_enddate);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StaffMaster staffMaster = staffMasters.get(position);
        holder.prounit_cd.setText(staffMaster.getProunit_cd());
        holder.staff_id.setText(staffMaster.getStaff_id());
        holder.uname.setText(staffMaster.getUname());
        holder.dt_nm.setText(staffMaster.getDt_nm());
        holder.s_start_dt.setText(staffMaster.getS_start_dt());
        holder.s_end_dt.setText(staffMaster.getS_end_dt());
        return convertView;
    }
}

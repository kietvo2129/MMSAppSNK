package com.licheedev.MMSSNK.Actual.Staff;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;
import java.util.List;

public class StaffMasterAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<StaffMaster> materialMasters;

    public StaffMasterAdapter(Context context, int layout, List<StaffMaster> materialMasters) {
        this.context = context;
        this.layout = layout;
        this.materialMasters = materialMasters;
    }

    @Override
    public int getCount() {
        return materialMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return materialMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return materialMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView tv_staff_no,tv_staff_id,tv_staff_name,tv_start_date,tv_end_date,tv_type_name;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.tv_staff_no = convertView.findViewById(R.id.tv_staff_no);
            holder.tv_start_date = convertView.findViewById(R.id.tv_start_date);
            holder.tv_type_name = convertView.findViewById(R.id.tv_type_name);
            holder.tv_end_date = convertView.findViewById(R.id.tv_end_date);
            holder.tv_staff_id = convertView.findViewById(R.id.tv_staff_id);
            holder.tv_staff_name = convertView.findViewById(R.id.tv_staff_name);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        StaffMaster materialMaster = materialMasters.get(position);
        holder.tv_staff_no.setText(materialMaster.getStt());
        holder.tv_staff_id.setText(materialMaster.getStaffId());
        holder.tv_staff_name.setText(materialMaster.getStaffName());
        holder.tv_type_name.setText(materialMaster.getTypeName());
        holder.tv_start_date.setText(materialMaster.getStartDate());
        holder.tv_end_date.setText(materialMaster.getEndDate());
        return convertView;
    }

    public void filterList(ArrayList<StaffMaster> filteredList) {
        materialMasters = filteredList;
        notifyDataSetChanged();
    }

}

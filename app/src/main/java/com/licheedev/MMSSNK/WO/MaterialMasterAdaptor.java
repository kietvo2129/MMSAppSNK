package com.licheedev.MMSSNK.WO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class MaterialMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<MaterialMaster> materialMasters;

    public MaterialMasterAdaptor(Context context, int layout, List<MaterialMaster> materialMasters) {
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
        TextView process_type,mt_nm,width,need_qty,feed_size,feed_unit,div_cd;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.process_type = convertView.findViewById(R.id.m_process);
            holder.mt_nm = convertView.findViewById(R.id.m_material);
            holder.width = convertView.findViewById(R.id.m_width);
            holder.need_qty = convertView.findViewById(R.id.m_needQty);
            holder.feed_size = convertView.findViewById(R.id.m_feeding);
            holder.feed_unit = convertView.findViewById(R.id.m_unit);
            holder.div_cd = convertView.findViewById(R.id.m_division);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        MaterialMaster materialMaster = materialMasters.get(position);
        holder.process_type.setText(materialMaster.getProcess_type());
        holder.mt_nm.setText(materialMaster.getMt_nm());
        holder.width.setText(materialMaster.getWidth());
        holder.need_qty.setText(materialMaster.getNeed_qty());
        holder.feed_size.setText(materialMaster.getFeed_size());
        holder.feed_unit.setText(materialMaster.getFeed_unit());
        holder.div_cd.setText(materialMaster.getDiv_cd());
        return convertView;
    }
}

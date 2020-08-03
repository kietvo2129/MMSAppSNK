package com.licheedev.MMSSNK.WO;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class WorkOrderMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<WorkOrderMaster> workOrderMasters;

    public WorkOrderMasterAdaptor(Context context, int layout, List<WorkOrderMaster> workOrderMasters) {
        this.context = context;
        this.layout = layout;
        this.workOrderMasters = workOrderMasters;
    }

    @Override
    public int getCount() {
        return workOrderMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return workOrderMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return workOrderMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView tvFoNo,tvProductCode,tvProductName,tvStartDt,tvEndDt,tvTargetQty,tvSkedQty,tvPoNo,tvLineNo,tvFactory;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.tvFoNo = convertView.findViewById(R.id.tv_fo_no);
            holder.tvProductCode = convertView.findViewById(R.id.tv_product_code);
            holder.tvProductName = convertView.findViewById(R.id.tv_product_name);
            holder.tvStartDt = convertView.findViewById(R.id.tv_start_dt);
            holder.tvEndDt = convertView.findViewById(R.id.tv_end_dt);
            holder.tvTargetQty = convertView.findViewById(R.id.tv_target_qty);
            holder.tvSkedQty = convertView.findViewById(R.id.tv_sked_qty);
            holder.tvPoNo = convertView.findViewById(R.id.tv_po_no);
            holder.tvLineNo = convertView.findViewById(R.id.tv_line_no);
            holder.tvFactory = convertView.findViewById(R.id.tv_factory);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        WorkOrderMaster workOrderMaster = workOrderMasters.get(position);
        String fo_no_view =workOrderMaster.getFo_no();
        holder.tvFoNo.setText(convert_view(fo_no_view));
        holder.tvProductCode.setText(workOrderMaster.getProduct_code());
        holder.tvProductName.setText(workOrderMaster.getProduct_name());
        holder.tvStartDt.setText(workOrderMaster.getStart_dt());
        holder.tvEndDt.setText(workOrderMaster.getEnd_dt());
        holder.tvTargetQty.setText(workOrderMaster.getTarget_qty());
        holder.tvSkedQty.setText(workOrderMaster.getSked_qty());


        holder.tvPoNo.setText(convert_view(workOrderMaster.getPo_no()));
        holder.tvLineNo.setText(workOrderMaster.getLine_no());
        holder.tvFactory.setText(workOrderMaster.getFactory());
        return convertView;
    }

    private String convert_view(String txt){
        String string = "";
        if (txt.equals("null")||txt.equals("")){

            string = "";

        }else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }
}

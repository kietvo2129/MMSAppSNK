package com.licheedev.MMSSNK.Product;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

//import com.example.wms.R;
//import com.example.wms.WO.WorkOrderMaster;

public class LotCompositeMasterAdaptor extends BaseAdapter {
     private Context context;
     private int layout;
     private List<LotCompositeMaster> lotCompositeMasters;

    int bbCodenum;

    public LotCompositeMasterAdaptor(Context context, int layout, List<LotCompositeMaster> lotCompositeMasters) {
        this.context = context;
        this.layout = layout;
        this.lotCompositeMasters = lotCompositeMasters;
    }

    @Override
    public int getCount() {
        return lotCompositeMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return lotCompositeMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return lotCompositeMasters.indexOf(getItem(position));
    }

    private class ViewHolder{
        TextView MLNO,MTNO,USE;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final LotCompositeMaster lotCompositeMaster = lotCompositeMasters.get(position);
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.MLNO = convertView.findViewById(R.id.MLNO);
            holder.MTNO = convertView.findViewById(R.id.MTNO);
            holder.USE = convertView.findViewById(R.id.USE);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.MLNO.setText(lotCompositeMaster.getMt_cd());

        if (lotCompositeMaster.getMt_type().equals("CMT")){
            String AAAA = lotCompositeMaster.getMt_no();
           int kytucuoi = AAAA.lastIndexOf("-");
           String Cat = AAAA.substring(kytucuoi+1);
            holder.MTNO.setText(Cat);
        }else {
            holder.MTNO.setText(lotCompositeMaster.getMt_no());
        }

        holder.USE.setText(lotCompositeMaster.getUse_yn());
        return convertView;
    }
}

package com.licheedev.MMSSNK.Actual;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;

public class countrydanhsachAdapter extends ArrayAdapter<countrydanhsach> {

    public countrydanhsachAdapter(Context context, ArrayList<countrydanhsach> danhsachList){
        super(context,0, danhsachList);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position,convertView,parent);
    }
    private View initView(int position, View convertView, ViewGroup parent){
        if( convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.dong_spinnerdanhsach, parent, false);
        }

        TextView textViewdate = convertView.findViewById(R.id.date);
        TextView textViewtaget = convertView.findViewById(R.id.taget);
        TextView textViewhour = convertView.findViewById(R.id.hour);
        TextView textViewprcunit = convertView.findViewById(R.id.prcunit);

        countrydanhsach currentItem = getItem(position);
        if (currentItem!=null) {

            textViewdate.setText(currentItem.getmdateview());
            textViewtaget.setText(currentItem.gettaget());
            textViewhour.setText(currentItem.gethour());
            textViewprcunit.setText(currentItem.getprcunit());
        }

        return convertView;

    }
}

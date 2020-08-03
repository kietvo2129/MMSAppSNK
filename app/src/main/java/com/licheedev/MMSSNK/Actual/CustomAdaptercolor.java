package com.licheedev.MMSSNK.Actual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;

public class CustomAdaptercolor extends BaseAdapter {
    Context context;
    ArrayList<Usercolor> userArrayListColor;

    public CustomAdaptercolor(Context context, ArrayList<Usercolor> userArrayListColor) {
        this.context = context;
        this.userArrayListColor = userArrayListColor;
    }

    @Override
    public int getCount() {
        return userArrayListColor.size();
    }

    @Override
    public Object getItem(int position) {
        return userArrayListColor.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView txtten;
        RadioButton checkBoxten;

    }
    public void UpdateListview(ArrayList<Usercolor> users){
        this.userArrayListColor = users;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if( convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.dong_color,null);
            viewHolder = new ViewHolder();
            viewHolder.txtten = convertView.findViewById(R.id.textviewten);
            viewHolder.checkBoxten = convertView.findViewById(R.id.checkboxten);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        Usercolor user = (Usercolor) getItem(position);
        viewHolder.txtten.setText(user.getTen());

        if(user.isChecked()){
            viewHolder.checkBoxten.setChecked(true);
        }else {
            viewHolder.checkBoxten.setChecked(false);
        }
        return convertView;
    }
}

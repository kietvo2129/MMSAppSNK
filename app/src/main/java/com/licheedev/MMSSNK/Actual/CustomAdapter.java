package com.licheedev.MMSSNK.Actual;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {
    Context context;
    ArrayList<User> userArrayList;

    public CustomAdapter(Context context, ArrayList<User> userArrayList) {
        this.context = context;
        this.userArrayList = userArrayList;
    }

    @Override
    public int getCount() {
        return userArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return userArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView txtten;
        CheckBox checkBoxten;

    }
    public void UpdateListview(ArrayList<User> users){
        this.userArrayList = users;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if( convertView == null){
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.dong_listview,null);
            viewHolder = new ViewHolder();
            viewHolder.txtten = convertView.findViewById(R.id.textviewten);
            viewHolder.checkBoxten = convertView.findViewById(R.id.checkboxten);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        User user = (User) getItem(position);
        viewHolder.txtten.setText(user.getTen());

        if(user.isChecked()){
            viewHolder.checkBoxten.setChecked(true);
        }else {
            viewHolder.checkBoxten.setChecked(false);
        }
        return convertView;
    }
}

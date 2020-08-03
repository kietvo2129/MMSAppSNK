package com.licheedev.MMSSNK.PQC;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.licheedev.MMSSNK.R;

import java.util.List;

public class PupopInspectionDetailAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<PupopInspectionDetailItem> items;

    public PupopInspectionDetailAdapter(Context context, int layout, List<PupopInspectionDetailItem> items) {
        this.context = context;
        this.layout = layout;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.indexOf(getItem(position));
    }

    public class ViewHolder {
       // CheckBox ckBox;
        TextView sub, val;
        EditText edtQty;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();

            // anh xa view holder
            //holder.ckBox = convertView.findViewById(R.id.ck_detail);
            holder.sub = convertView.findViewById(R.id.tv_popup_detail_sub);
            holder.val = convertView.findViewById(R.id.tv_popup_detail_valume);
            holder.edtQty = convertView.findViewById(R.id.edt_popup_detail_qty);

//            holder.ckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            int getPosition = (Integer) buttonView.getTag();
//                            items.get(getPosition).setCheckbox(buttonView.isChecked());
//                            notifyDataSetChanged();
//                        }
//                    });

            convertView.setTag(holder);
            //convertView.setTag(R.id.ck_detail, holder.ckBox);              //
            convertView.setTag(R.id.edt_popup_detail_qty, holder.edtQty);  //
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.ckBox.setTag(position);
        holder.edtQty.setTag(position);

        holder.edtQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                int pos = (Integer) holder.edtQty.getTag();
                items.get(pos).setQty(s.toString());
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        final PupopInspectionDetailItem current = items.get(position);
        holder.sub.setText(current.getSubject());
        holder.val.setText(current.getValue());
        holder.edtQty.setText(current.getQty());        

        //
        // holder.ckBox.setChecked(current.isCheckbox());


        //convertView.setTag(holder);
        return convertView;
    }

//    private CompoundButton.OnCheckedChangeListener onCheckedChangeListener(final PupopInspectionDetailItem f) {
//        return new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    f.setCheckbox(true);
//                } else {
//                    f.setCheckbox(false);
//                }
//            }
//        };
//    }

//    private void setVal1TextChangeListener(final ViewHolder holder)
//    {
//        holder.edtQty.addTextChangedListener(new TextWatcher()
//        {
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count)
//            {
//
//            }
//
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after)
//            {
//                if(s.toString().length()>0)
//                    holder.edtQty.setText(s.toString());
//            }
//
//            @Override
//            public void afterTextChanged(Editable s)
//            {
//
//            }
//        });
//    }
}
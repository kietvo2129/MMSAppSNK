package com.licheedev.MMSSNK.PQC;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.licheedev.MMSSNK.R;

import java.util.List;

public class PupopInspectionListQCDetailAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<PupopInspectionListQCDetailitem> items;

    public PupopInspectionListQCDetailAdapter(Context context, int layout, List<PupopInspectionListQCDetailitem> items) {
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
       // CheckBox checkBox;
        TextView contents;
        EditText edtCheckQty;
        Button sub, add;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if( convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout , null);
            holder = new ViewHolder();

            // anh xa view holder
            //holder.checkBox = convertView.findViewById(R.id.ckbox_stt_check);
            holder.contents = convertView.findViewById(R.id.tv_contents_check_qc);
            holder.edtCheckQty = convertView.findViewById(R.id.edt_qty_check);
            holder.add = convertView.findViewById(R.id.popup_inspection_cong);
            holder.sub = convertView.findViewById(R.id.popup_inspection_tru);

//            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                            int getPosition = (Integer) buttonView.getTag();
//                            items.get(getPosition).setCheckbox(buttonView.isChecked());
//                            notifyDataSetChanged();
//                        }
//                    });

            holder.add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    if(items.get(pos).getQty().equals(null)){items.get(pos).setQty("0");}
                    int qty = Integer.valueOf(items.get(pos).getQty()) + 1;
                    items.get(pos).setQty(qty + "");
                    notifyDataSetChanged();
                }
            });

            holder.sub.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = position;
                    if(items.get(pos).getQty().equals(null)){items.get(pos).setQty("0");}
                    int qty = Integer.valueOf(items.get(pos).getQty()) - 1;

                    if (qty < 0) {
                        items.get(pos).setQty("0");
                    } else {
                        items.get(pos).setQty(qty + "");
                    }
                    notifyDataSetChanged();
                }
            });


            convertView.setTag(holder);
            //convertView.setTag(R.id.ck_detail, holder.checkBox);              //
            convertView.setTag(R.id.edt_popup_detail_qty, holder.edtCheckQty);  //
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        //holder.checkBox.setTag(position);
        holder.edtCheckQty.setTag(position);

        holder.edtCheckQty.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                int pos = (Integer) holder.edtCheckQty.getTag();
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

        final PupopInspectionListQCDetailitem current = items.get(position);

        holder.contents.setText(current.getContent());
        holder.edtCheckQty.setText(current.getQty());
        //holder.checkBox.setChecked(current.isCheckbox());
        //convertView.setTag(holder);
        return convertView;
    }

}
package com.licheedev.MMSSNK.Product;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

//import com.example.wms.R;
//import com.example.wms.WO.WorkOrderMaster;

import com.licheedev.MMSSNK.Actual.ActualFragment;
import com.licheedev.MMSSNK.Actual.LotCompositeActivity;
import com.licheedev.MMSSNK.Actual.LotProductActivity;
import com.licheedev.MMSSNK.QC_check.OQC;
import com.licheedev.MMSSNK.QC_check.PQC;
import com.licheedev.MMSSNK.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;

public class ProductMasterAdaptor extends BaseAdapter {
    private Context context;
    private int layout;
    private List<ProductMaster> productMasters;
    String level = ActualFragment.level;
    String WOchon = ActualFragment.WOchon;
    String Linechon = ActualFragment.Linechon;
    String Ma_QC_Code;
    int bbCodenum;

    public ProductMasterAdaptor(Context context, int layout, List<ProductMaster> productMasters) {
        this.context = context;
        this.layout = layout;
        this.productMasters = productMasters;
    }

    @Override
    public int getCount() {
        return productMasters.size();
    }

    @Override
    public Object getItem(int position) {
        return productMasters.get(position);
    }

    @Override
    public long getItemId(int position) {
        return productMasters.indexOf(getItem(position));
    }

    private class ViewHolder {
        TextView gr_qty, bb_no, mt_qrcode;
        Button button, buttonQC,bt_delete;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        final ProductMaster productMaster = productMasters.get(position);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(layout, null);
            holder = new ViewHolder();
            // anh xa view holder
            holder.gr_qty = convertView.findViewById(R.id.gr_qty);
            holder.bb_no = convertView.findViewById(R.id.bb_no);
            holder.mt_qrcode = convertView.findViewById(R.id.mt_qrcode);
            holder.button = convertView.findViewById(R.id.bt_qc_check);
            holder.buttonQC = convertView.findViewById(R.id.buttonQC);
            holder.gr_qty.setFocusable(false);
            holder.gr_qty.setClickable(false);
            holder.button.setFocusable(false);
            holder.button.setClickable(false);
            holder.buttonQC.setFocusable(false);
            holder.buttonQC.setClickable(false);
         //   holder.bt_delete = convertView.findViewById(R.id.bt_delete);
          //  holder.bt_delete.setFocusable(false);

           // holder.bt_delete.setTag(position);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
//            holder.bt_delete.setTag(position);
//            convertView.setTag(holder);
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (level.equals("last")) {
                    Intent intent = new Intent(context, LotProductMappingActivity.class);
                    intent.putExtra("mt_qrcode", productMaster.getMt_qrcode());
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, LotCompositeMappingActivity.class);
                    intent.putExtra("mt_qrcode", productMaster.getMt_qrcode());
                    context.startActivity(intent);
                }
            }
        });

        holder.buttonQC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (level.equals("last")) {
                    Ma_QC_Code = LotProductActivity.Ma_QC_Code;
                    if (Ma_QC_Code== null ||Ma_QC_Code.equals("null")||Ma_QC_Code.equals("")){

                        AlertNotExist("Create QC Code");
                    }else {
                        Intent intent = new Intent(context, OQC.class);
                        intent.putExtra("mt_qrcode", productMaster.getMt_qrcode());
                        context.startActivity(intent);
                    }
                } else {
                    Ma_QC_Code = LotCompositeActivity.Ma_QC_Code;
                    if ( Ma_QC_Code== null || Ma_QC_Code.equals("null")||Ma_QC_Code.equals("")){
                        AlertNotExist("Create QC Code");
                    }else {
                        Intent intent = new Intent(context, PQC.class);
                        intent.putExtra("mt_qrcode", productMaster.getMt_qrcode());
                        context.startActivity(intent);
                    }
                }
            }
        });


        holder.gr_qty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Input Group Qty");
                View viewInflated = LayoutInflater.from(context).inflate(R.layout.number_input_layout, null);
                final EditText input = (EditText) viewInflated.findViewById(R.id.input);
                builder.setView(viewInflated);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String bbCode = input.getText().toString();

                        if (bbCode.length() == 0) {
                            Toast.makeText(context, "Please input data!", Toast.LENGTH_SHORT).show();
                        } else {
                            bbCodenum = Integer.parseInt(bbCode);
                            //productMaster.setGr_qty(bbCode);
                            productMasters.get(position).setGr_qty(bbCodenum + "");
                            notifyDataSetChanged();
//                            Toast.makeText(context, "" + productMaster.getWmtid(), Toast.LENGTH_SHORT).show();
//                            http://snk.autonsi.com/Lot/update_w_material_gr_api?gr_qty=50&id=1221
                            if (level.equals("last")) {
                                new docJSONchinhsua().execute("http://snk.autonsi.com/Lot/update_w_material_gr_api?gr_qty=" +
                                        bbCodenum + "&id=" + productMaster.getWmtid() + "&level=" + level);
                                dialog.cancel();
                                Log.d("chinh sua", "http://snk.autonsi.com/Lot/update_w_material_gr_api?gr_qty=" +
                                        bbCodenum + "&id=" + productMaster.getWmtid() + "&level=" + level);
                            } else {
                                new docJSONchinhsua().execute("http://snk.autonsi.com/Lot/update_w_material_gr_api?gr_qty=" +
                                        bbCodenum + "&id=" + productMaster.getWmtid() + "&level=" + level + "&line_no=" + Linechon + "&fo_no=" + WOchon);
                                dialog.cancel();
                                Log.d("chinh sua", "http://snk.autonsi.com/Lot/update_w_material_gr_api?gr_qty=" +
                                        bbCodenum + "&id=" + productMaster.getWmtid() + "&level=" + level + "&line_no=" + Linechon + "&fo_no=" + WOchon);
                            }
                        }
                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });
        holder.mt_qrcode.setText(productMaster.getMt_qrcode());
        holder.bb_no.setText(productMaster.getBb_no());
        holder.gr_qty.setText(productMaster.getGr_qty());
        return convertView;
    }

    class docJSONchinhsua extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String TL = jsonObject.getString("result");
                if (TL.equals("false")) {
                    Toast.makeText(context, "Data server incorrect!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Done", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                Toast.makeText(context, "Data server incorrect!!!", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        }
    }

    private String NoiDung_Tu_URL(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }
    private void AlertNotExist(String text) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(context);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Warning!!!");
        alertDialog.setMessage(text); //"The data you entered does not exist on the server !!!");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });
        alertDialog.show();
    }
}

package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.Thinkness.ThinknessActivity;
import com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.Visual.VisualActivity;
import com.licheedev.MMSSNK.TotalInspection.TotalInspectionFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class TotalInspectionDetailActivity extends AppCompatActivity {

    String WOchon = TotalInspectionFragment.WOchon;
    String Productchon = TotalInspectionFragment.Productchon;

    TextView btn_Thickness, btn_Visual;
    TextView tv_WO, tv_Productchon;
    ArrayList<TotalInspectionDetailMaster> totalInspectionDetailMasterArrayList;
    TotalIspectionDetailMasterAdaptor totalIspectionDetailMasterAdaptor;
    ListView lvds;
    int vitri = -1;

    public static String PLno, dateno, idPLno, Group_qty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_inspection_detail);
        setTitle("Detail Inspection");
        tv_WO = findViewById(R.id.tv_WO);
        tv_Productchon = findViewById(R.id.tv_product);
        tv_WO.setText(convert_view(WOchon));
        tv_Productchon.setText(Productchon);
        lvds = findViewById(R.id.lvDanhSanh);
        btn_Visual = findViewById(R.id.btn_Visual);
        btn_Thickness = findViewById(R.id.btn_Thickness);

   //     new ReadJSON().execute("http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
    //    Log.d("Total inspection detail", "http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
        lvds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                vitri = i;


            }
        });

        btn_Thickness.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitri == -1) {
                    AlertNotExist("Please choose object!!!");
                } else {

                    Group_qty = totalInspectionDetailMasterArrayList.get(vitri).getGr_qty();
                    idPLno = totalInspectionDetailMasterArrayList.get(vitri).getPno();
                    PLno = totalInspectionDetailMasterArrayList.get(vitri).getPrd_lcd();
                    dateno = totalInspectionDetailMasterArrayList.get(vitri).getPrd_dt();
                    Intent intent = new Intent(TotalInspectionDetailActivity.this, ThinknessActivity.class);
                    startActivity(intent);

                }
            }
        });
        btn_Visual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitri == -1) {
                    AlertNotExist("Please choose object!!!");
                } else {

                    Group_qty = totalInspectionDetailMasterArrayList.get(vitri).getGr_qty();
                    idPLno = totalInspectionDetailMasterArrayList.get(vitri).getPno();
                    PLno = totalInspectionDetailMasterArrayList.get(vitri).getPrd_lcd();
                    dateno = totalInspectionDetailMasterArrayList.get(vitri).getPrd_dt();
                    Intent intent = new Intent(TotalInspectionDetailActivity.this, VisualActivity.class);
                    startActivity(intent);

                }
            }
        });

    }

    public void finish_click(View view) {
        int vitri = lvds.getPositionForView(view);
        if (!totalInspectionDetailMasterArrayList.get(vitri).getSts().equals("Finish")) {
            new ReadJSONfinish().execute("http://snk.autonsi.com/TotalInspection/update_btn_finish?pno=" +
                    totalInspectionDetailMasterArrayList.get(vitri).getPno() +
                    "&insp_yn=" +
                    "Y");
            Log.d("Totalinspectionfinnish", "http://snk.autonsi.com/TotalInspection/update_btn_finish?pno=" +
                    totalInspectionDetailMasterArrayList.get(vitri).getPno() +
                    "&insp_yn=" +
                    "Y");
        }

    }

    private String convert_view(String txt) {
        String string = "";
        if (txt.equals("null")) {

        } else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }

    public void click_groupQty(final View view) {
        final int vitri = lvds.getPositionForView(view);
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Input Group Qty");
//        View viewInflated = LayoutInflater.from(this).inflate(R.layout.number_input_layout_ok_cancel, null);













        Rect displayRectangle = new Rect();
        Window window = TotalInspectionDetailActivity.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(TotalInspectionDetailActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Input Group Qty");
        View dialogView = LayoutInflater.from(TotalInspectionDetailActivity.this).inflate(R.layout.number_input_layout_ok_cancel, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa



        final EditText input = (EditText) dialogView.findViewById(R.id.input);
        Button  in_btn_cancel = dialogView.findViewById(R.id.in_btn_cancel);
        Button  in_btn_ok = dialogView.findViewById(R.id.in_btn_ok);

        String thickness, visual, product;
        thickness = totalInspectionDetailMasterArrayList.get(vitri).getThink_def_qty();
        visual = totalInspectionDetailMasterArrayList.get(vitri).getVisual_def_qty();
        product = totalInspectionDetailMasterArrayList.get(vitri).getPrd_qty();
        int numthichness = 0, numvisual = 0, numproduct = 0, maximum = 0;

        numthichness = Integer.parseInt(thickness);
        numvisual = Integer.parseInt(visual);
        numproduct = Integer.parseInt(product);

        maximum = numproduct - numthichness - numvisual;
        if (maximum < 0) {
            maximum = 0;
        }

        int finalMaximum = maximum;
        in_btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String bbCode = input.getText().toString().trim();

                if (bbCode.length() == 0) {
                    input.setError ("Please input data!");
                    input.setFocusable(true);
                    return;
                   // Toast.makeText(TotalInspectionDetailActivity.this, "Please input data!", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(bbCode) > finalMaximum) {
                    input.setError ("Maximum = " + finalMaximum);
                    input.setFocusable(true);
                    return;
                } else {
                    // http://snk.autonsi.com/TotalInspection/update_btn_save?pno=1&gr_qty=10

                    new ReadJSONfinish().execute("http://snk.autonsi.com/TotalInspection/update_btn_save?pno=" +
                            totalInspectionDetailMasterArrayList.get(vitri).getPno() +
                            "&gr_qty=" +
                            bbCode);
                    Log.d("Totalinspectionsave", "http://snk.autonsi.com/TotalInspection/update_btn_save?pno=" +
                            totalInspectionDetailMasterArrayList.get(vitri).getPno() +
                            "&gr_qty=" +
                            bbCode);
                    alertDialog.cancel();
                }


            }
        });

        in_btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.cancel();
            }
        });

//        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                String bbCode = input.getText().toString();
//
//                if (bbCode.length() == 0) {
//                    Toast.makeText(TotalInspectionDetailActivity.this, "Please input data!", Toast.LENGTH_SHORT).show();
//                } else if (Integer.parseInt(bbCode) > finalMaximum) {
//                    AlertNotExist ("Maximum = " + finalMaximum);
//                } else {
//                    // http://snk.autonsi.com/TotalInspection/update_btn_save?pno=1&gr_qty=10
//
//                    new ReadJSONfinish().execute("http://snk.autonsi.com/TotalInspection/update_btn_save?pno=" +
//                            totalInspectionDetailMasterArrayList.get(vitri).getPno() +
//                            "&gr_qty=" +
//                            bbCode);
//                    Log.d("Totalinspectionsave", "http://snk.autonsi.com/TotalInspection/update_btn_save?pno=" +
//                            totalInspectionDetailMasterArrayList.get(vitri).getPno() +
//                            "&gr_qty=" +
//                            bbCode);
//
//                }
//            }
//        });

//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.cancel();
//            }
//        });
        alertDialog.show();


    }

    private class ReadJSONfinish extends AsyncTask<String, Void, String> {
        StringBuilder context = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    context.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return context.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                String Trave = object.getString("result");
                if (Trave.equals("true")) {
                    //Toast.makeText(TotalInspectionDetailActivity.this, "OKE", Toast.LENGTH_SHORT).show();
                    new ReadJSON().execute("http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
                    Log.d("Total inspection detail", "http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
                } else {
                    Toast.makeText(TotalInspectionDetailActivity.this, "Sever disconnected", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private class ReadJSON extends AsyncTask<String, Void, String> {
        StringBuilder context = new StringBuilder();

        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    context.append(line);
                }
                bufferedReader.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return context.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            totalInspectionDetailMasterArrayList = new ArrayList<TotalInspectionDetailMaster>();

            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");
                if (jsonArray.length() == 0) {
                    Toast.makeText(TotalInspectionDetailActivity.this, "Data incorrect", Toast.LENGTH_SHORT).show();
                } else {
                    for (int i = 0; i < jsonArray.length(); i++) {

                        JSONObject objectRow = jsonArray.getJSONObject(i);
                        String pno = objectRow.getString("pno");
                        String prd_lcd = objectRow.getString("prd_lcd");
                        String prd_dt = objectRow.getString("prd_dt");
                        String sts = objectRow.getString("sts");
                        String prd_qty = objectRow.getString("prd_qty");
                        String gr_qty = objectRow.getString("gr_qty");
                        String think_def_qty = objectRow.getString("think_def_qty");
                        String visual_def_qty = objectRow.getString("visual_def_qty");
                        String total_def_qty = objectRow.getString("total_def_qty");
                        String total_def_rate = objectRow.getString("total_def_rate");
                        if (total_def_rate.equals("null")) {
                            total_def_rate = "0%";
                        } else {
                            double total_def_rate_num = Double.parseDouble(total_def_rate);
                            total_def_rate_num = (double) Math.round(total_def_rate_num * 100) / 100;
                            total_def_rate = total_def_rate_num + "%";
                        }
                        totalInspectionDetailMasterArrayList.add(new TotalInspectionDetailMaster(pno, prd_lcd, prd_dt, sts, prd_qty, gr_qty, think_def_qty,
                                visual_def_qty, total_def_qty, total_def_rate));
                    }
                }

                totalIspectionDetailMasterAdaptor = new TotalIspectionDetailMasterAdaptor(
                        TotalInspectionDetailActivity.this, R.layout.total_inspection_detail_data, totalInspectionDetailMasterArrayList);
                lvds.setAdapter(totalIspectionDetailMasterAdaptor);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void AlertNotExist(String text) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(this);
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

    @Override
    public void onResume() {
        super.onResume();
        new ReadJSON().execute("http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
        Log.d("Total inspection detail", "http://snk.autonsi.com/TotalInspection/get_PP_detail?fo_no=" + WOchon);
    }

}

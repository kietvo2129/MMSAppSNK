package com.licheedev.MMSSNK.TotalInspection;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.TotalInspectionDetailActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class TotalInspectionFragment extends Fragment {


    ArrayList<TotalInspectionMaster> totalInspectionMasterArrayList;
    TotalIspectionMasterAdaptor totalIspectionMasterAdaptor;
    ListView lvds;
    public static String WOchon, Productchon;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_total_inspection, container, false);

        lvds = view.findViewById(R.id.lvDanhSanh);

      //  new ReadJSON().execute("http://snk.autonsi.com/TotalInspection/getIns_list_ProInfo");
     //   Log.d("Total inspection","http://snk.autonsi.com/TotalInspection/getIns_list_ProInfo");
        lvds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                WOchon = totalInspectionMasterArrayList.get(position).getFo_no();
                Productchon = totalInspectionMasterArrayList.get(position).getStyle_no();
                Intent intent = new Intent(getContext(), TotalInspectionDetailActivity.class);
                startActivity(intent);
            }
        });

        return view;
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
            totalInspectionMasterArrayList = new ArrayList<TotalInspectionMaster>();

            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");
                if (jsonArray.length()== 0){
                    Toast.makeText(getContext(), "Data incorrect", Toast.LENGTH_SHORT).show();
                }else {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        DecimalFormat formatter = new DecimalFormat("#,###,###");
                        JSONObject objectRow = jsonArray.getJSONObject(i);
                        String pno = objectRow.getString("pno");
                        String fo_no = objectRow.getString("fo_no");
                        String style_no = objectRow.getString("style_no");
                        String start_dt = objectRow.getString("start_dt");
                        String end_dt = objectRow.getString("end_dt");
                        String gr_qty = objectRow.getString("gr_qty");
                        if (gr_qty.equals("null")) {
                            gr_qty = "0";
                        }
                        int numgr_qty = Integer.parseInt(gr_qty);
                        gr_qty = formatter.format(numgr_qty);


                        String prd_qty = objectRow.getString("prd_qty");
                        if (prd_qty.equals("null")) {
                            prd_qty = "0";
                        }
                        int numprd_qty = Integer.parseInt(prd_qty);
                        prd_qty = formatter.format(numprd_qty);

                        String total_check_qty = objectRow.getString("total_check_qty");
                        String total_def_qty = objectRow.getString("total_def_qty");
                        String total_def_rate = objectRow.getString("total_def_rate");
                        if (total_def_rate.equals("null")){
                            total_def_rate = "0%";
                        }else {
                            double total_def_rate_num = Double.parseDouble(total_def_rate);
                            total_def_rate_num = (double) Math.round(total_def_rate_num * 100) / 100;
                            total_def_rate = total_def_rate_num + "%";
                        }
                        String think_def_qty = objectRow.getString("think_def_qty");
                        if (think_def_qty.equals("null")) {
                            think_def_qty = "0";
                        }
                        int numthink_def_qty = Integer.parseInt(think_def_qty);
                        think_def_qty = formatter.format(numthink_def_qty);

                        String visual_def_qty = objectRow.getString("visual_def_qty");
                        if (visual_def_qty.equals("null")) {
                            visual_def_qty = "0";
                        }
                        int numvisual_def_qty = Integer.parseInt(visual_def_qty);
                        visual_def_qty = formatter.format(numvisual_def_qty);

                        String product_cnt = objectRow.getString("product_cnt");
                        if (product_cnt.equals("null")) {
                            product_cnt = "0";
                        }
                        int numproduct_cnt = Integer.parseInt(product_cnt);
                        product_cnt = formatter.format(numproduct_cnt);

                        String check_cnt = objectRow.getString("check_cnt");
                        if (check_cnt.equals("null")) {
                            check_cnt = "0";
                        }
                        int numcheck_cnt = Integer.parseInt(check_cnt);
                        check_cnt = formatter.format(numcheck_cnt);


                        String stt = objectRow.getString("stt");
                        totalInspectionMasterArrayList.add(new TotalInspectionMaster(pno, fo_no, style_no, start_dt, end_dt, gr_qty, prd_qty,
                                total_check_qty, total_def_qty, total_def_rate, think_def_qty, visual_def_qty, product_cnt, check_cnt, stt));
                    }
                }

                totalIspectionMasterAdaptor = new TotalIspectionMasterAdaptor(
                        getActivity(), R.layout.total_inspection_data, totalInspectionMasterArrayList);
                lvds.setAdapter(totalIspectionMasterAdaptor);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        new ReadJSON().execute("http://snk.autonsi.com/TotalInspection/getIns_list_ProInfo");
        Log.d("Total inspection","http://snk.autonsi.com/TotalInspection/getIns_list_ProInfo");
    }



}

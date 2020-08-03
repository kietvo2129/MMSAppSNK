package com.licheedev.MMSSNK.OQC;


import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.PQC.QcDetailAdapterView;
import com.licheedev.MMSSNK.QC_check.QcCheckAdapter;
import com.licheedev.MMSSNK.QC_check.QcCheckItem;
import com.licheedev.MMSSNK.QC_check.QcDetailItem;
import com.licheedev.MMSSNK.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class OQCFragment extends Fragment {
    ArrayList<String> itemWO;
    ArrayList<String> itemspinnerDate;
    ArrayList<String> itemWO_view;// = new ArrayList<>();
    ArrayList<String> itemspinnerProcess;     // = new ArrayList<>();
    ArrayList<String> itemReceiDatehienthi;// = new ArrayList<>();
    ArrayAdapter adapterSpinnerWO, adapterspinnerProcess, adapterSpinnerReceiDate;
    Spinner spinnerWO;
    Spinner spinnerProcess;
    String mpono = "", Process = "", date = "";
    TextView tvTargetQty, tvActual, tvDefective, tv_QCCode, tvCheckQty, tvOkeQty;
    ArrayList<QcDetailItem> qcDetailItem;
    android.app.AlertDialog alertDialog;
    QcDetailAdapterView qcDetailAdapter;
    RecyclerView recyclerViewDetail;
    Spinner spinnerDate;
    public static String olddno, Ma_QC_Code;
    private ArrayList<QcCheckItem> qcitem;
    private QcCheckAdapter adaptorCheck;
    private ListView listViewCheck;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_pqc, container, false);

//        // Anh xa
        spinnerWO = root.findViewById(R.id.spinner_mpo);
        spinnerProcess = root.findViewById(R.id.spinner_mtno);
        tvTargetQty = root.findViewById(R.id.tvTargetQty);
        tvActual = root.findViewById(R.id.tvActual);
        tvDefective = root.findViewById(R.id.tvDefective);
        tv_QCCode = root.findViewById(R.id.tv_QCCode);
        tvCheckQty = root.findViewById(R.id.tvCheckQty);
        tvOkeQty = root.findViewById(R.id.tvOkeQty);
        spinnerDate = root.findViewById(R.id.spinner_date);
        listViewCheck = root.findViewById(R.id.recycler_view_qc_check);

        String UrlgetInput_mpo_no = "http://snk.autonsi.com/product/getDatainput_OQC?";
        Log.d("Get OQC", UrlgetInput_mpo_no);

        new docJSON_inputWO().execute(UrlgetInput_mpo_no);


        return root;
    } //End View;

    class docJSON_inputWO extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            itemWO = new ArrayList<>();
            itemWO_view = new ArrayList<>();
            itemspinnerProcess = new ArrayList<>();
            itemReceiDatehienthi = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("[]")) {
                    //   AlertNotExist("Error!");
                    adapterspinnerProcess = new ArrayAdapter(getActivity(), R.layout.spinner_item, itemspinnerProcess);
                    adapterspinnerProcess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerProcess.setAdapter(adapterspinnerProcess);
                } else {
                    JSONArray jsonArray = object.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String mpo_no = jsonArray.getString(i);
                        itemWO.add(mpo_no);
                        itemWO_view.add(convert_view(mpo_no));
                    }
                }

            } catch (JSONException e) {
                AlertNotExist("Error!");
                e.printStackTrace();
            }
            adapterSpinnerWO = new ArrayAdapter(getActivity(), R.layout.spinner_item, itemWO_view);
            adapterSpinnerWO.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerWO.setAdapter(adapterSpinnerWO);
            ItemProcess();
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

    private void ItemProcess() {
        tvActual.setText("");
        tvTargetQty.setText("");
        tvDefective.setText("");
        tv_QCCode.setText("");
        tvCheckQty.setText("");
        tvOkeQty.setText("");
        spinnerWO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mpono = itemWO.get(position);
                new docJSON_inputProcess().execute("http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono);
                Log.d("docJSON_inputProcess", "http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class docJSON_inputProcess extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            itemspinnerProcess = new ArrayList<>();
            itemReceiDatehienthi = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("[]")) {
                    // AlertNotExist("Error!");
                    // Toast.makeText(getContext(), "Data incorrect....", Toast.LENGTH_SHORT).show();
                    adapterSpinnerReceiDate = new ArrayAdapter(getActivity(), R.layout.spinner_item, itemReceiDatehienthi);
                    adapterSpinnerReceiDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    // spinnerDate.setAdapter(adapterSpinnerReceiDate);
                    tvActual.setText("");
                    tvTargetQty.setText("");
                    tvDefective.setText("");
                    tv_QCCode.setText("");
                    tvCheckQty.setText("");
                    tvOkeQty.setText("");
                    qcitem = new ArrayList<>();
                    buildListView();
                    new docJSON_inputdate().execute("http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono + "&process=" + "");
                    Log.d("docJSON_inputdate", "http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono + "&process=" + "");
                } else {
                    JSONArray jsonArray = object.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String mpo_no = jsonArray.getString(i);
                        itemspinnerProcess.add(mpo_no);
                    }
                }

            } catch (JSONException e) {
                //  Toast.makeText(getContext(), "Wifi disconnected......", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            adapterspinnerProcess = new ArrayAdapter(getActivity(), R.layout.spinner_item, itemspinnerProcess);
            adapterspinnerProcess.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerProcess.setAdapter(adapterspinnerProcess);
            ItemDate();


        }
    }

    private void ItemDate() {
        tvActual.setText("");
        tvTargetQty.setText("");
        tvDefective.setText("");
        tv_QCCode.setText("");
        tvCheckQty.setText("");
        tvOkeQty.setText("");
        spinnerProcess.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Process = itemspinnerProcess.get(position);
                new docJSON_inputdate().execute("http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono + "&process=" + Process);
                Log.d("docJSON_inputdate", "http://snk.autonsi.com/product/getDatainput_OQC?fo_no=" + mpono + "&process=" + Process);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class docJSON_inputdate extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            itemspinnerDate = new ArrayList<>();
            itemReceiDatehienthi = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("[]")) {
                    tvActual.setText("");
                    tvTargetQty.setText("");
                    tvDefective.setText("");
                    tv_QCCode.setText("");
                    tvCheckQty.setText("");
                    tvOkeQty.setText("");
                    new docJSON_inputdata().execute("http://snk.autonsi.com/product/GetListDay_OQC?fo_no=" + mpono + "&process=" + Process + "&work_ymd=" + "");
                    Log.d("docJSON_inputdata", "http://snk.autonsi.com/product/GetListDay_OQC?fo_no=" + mpono + "&process=" + Process + "&work_ymd=" + "");

                } else {
                    JSONArray jsonArray = object.getJSONArray("result");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String mpo_no = jsonArray.getString(i);
                        itemspinnerDate.add(mpo_no);
                        itemReceiDatehienthi.add(mpo_no.substring(0, 4) + "-" + mpo_no.substring(4, 6) + "-" + mpo_no.substring(6, 8));
                    }
                }

            } catch (JSONException e) {
                //  Toast.makeText(getContext(), "Wifi disconnected......", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
            adapterSpinnerReceiDate = new ArrayAdapter(getActivity(), R.layout.spinner_item, itemReceiDatehienthi);
            adapterSpinnerReceiDate.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDate.setAdapter(adapterSpinnerReceiDate);
            ItemData();
        }
    }

    private void ItemData() {
        tvActual.setText("");
        tvTargetQty.setText("");
        tvDefective.setText("");
        tv_QCCode.setText("");
        tvCheckQty.setText("");
        tvOkeQty.setText("");
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                date = itemspinnerDate.get(position);

                new docJSON_inputdata().execute("http://snk.autonsi.com/product/GetListDay_OQC?fo_no=" + mpono + "&process=" + Process + "&work_ymd=" + date);
                Log.d("docJSON_inputdata", "http://snk.autonsi.com/product/GetListDay_OQC?fo_no=" + mpono + "&process=" + Process + "&work_ymd=" + date);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    class docJSON_inputdata extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {


                qcDetailItem = new ArrayList<>();
                JSONObject object = new JSONObject(s);
                ///data table
                if (object.getString("result").equals("[]") || object.getString("result").equals("false")) {
                    Toast.makeText(getContext(), "Data incorrect....", Toast.LENGTH_SHORT).show();
                    String url2 = "http://snk.autonsi.com/product/GetDataW_Product_Qc?item_vcd=" + "" + "&olddno=" + olddno;
                    Log.d("OQC_getData", url2);
                    new LoadQcList().execute(url2);
                    return;
                } else {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    JSONObject jsonobject = object.getJSONObject("result");


                    String sked_qty = jsonobject.getString("sked_qty");
                    if (sked_qty.equals("null")) {
                        sked_qty = "0";
                    }
                    int numsked_qty = Integer.parseInt(sked_qty);
                    tvTargetQty.setText(formatter.format(numsked_qty));

                    String check_qty = jsonobject.getString("check_qty");
                    if (check_qty.equals("null")) {
                        check_qty = "0";
                    }

                    int numcheck_qty = Integer.parseInt(check_qty);
                    tvCheckQty.setText(formatter.format(numcheck_qty));

                    String tvActual1 = jsonobject.getString("actual_qty");
                    if (tvActual1.equals("null")) {
                        tvActual1 = "0";
                    }
                    int numActual1 = Integer.parseInt(tvActual1);
                    tvActual.setText(formatter.format(numActual1));

                    String ok_qty = jsonobject.getString("ok_qty");
                    if (ok_qty.equals("null")) {
                        ok_qty = "0";
                    }
                    int numok_qty = Integer.parseInt(ok_qty);
                    tvOkeQty.setText(formatter.format(numok_qty));
                    String defective_qty = jsonobject.getString("def_qty");
                    if (defective_qty.equals("null")) {
                        defective_qty = "0";
                    }
                    int numdefective_qty = Integer.parseInt(defective_qty);
                    tvDefective.setText(formatter.format(numdefective_qty));


                    String item_vcd = jsonobject.getString("item_vcd");
                    tv_QCCode.setText(item_vcd);

                    Ma_QC_Code = item_vcd;
                    olddno = jsonobject.getString("olddno");
                }

                LoadQcListAgain();

            } catch (JSONException e) {

                e.printStackTrace();
            }

        }
    }

    private void LoadQcListAgain() {

        if (!Ma_QC_Code.equals("null")) {
            String url2 = "http://snk.autonsi.com/product/GetDataW_Product_Qc?item_vcd=" + Ma_QC_Code + "&olddno=" + olddno;
            Log.d("OQC_getData", url2);
            new LoadQcList().execute(url2);
        }else {
            AlertNotExist("Create QC Code");
        }


    }

    class LoadQcList extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            qcitem = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    AlertNotExist("QC Detail not exist !!!");
                    qcitem.removeAll(qcitem);
                    buildListView();
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("result");

//                // Total
//                JSONObject obj = jsonArray.getJSONObject(0);
//                tv_qc_ok_qty.setText(/*qcitem.get(0).getOkqty()*/ obj.getString("ok_qty")
//                        .replace("null","0"));
//                tv_qc_check_qty.setText(/*qcitem.get(0).getCheckqty()*/ obj.getString("check_qty")
//                        .replace("null","0"));
//                tv_qc_defect_qty.setText(/*qcitem.get(0).getDefectqty()*/ obj.getString("defect_qty")
//                        .replace("null","0"));
//
//                // item
                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    String idMqno = object1.getString("pqno");
                    String mq_no = object1.getString("pq_no");
                    String work_dt = object1.getString("work_dt");
                    String check_qty = object1.getString("check_qty").replace("null", "0");
                    String ok_qty = object1.getString("ok_qty").replace("null", "0");
                    String defect_qty = object1.getString("defect_qty").replace("null", "0");

                    qcitem.add(new QcCheckItem(idMqno, mq_no, work_dt, check_qty, ok_qty, defect_qty));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            buildListView();


        }
    }

    private void buildListView() {
        adaptorCheck = new QcCheckAdapter(getActivity(),R.layout.item_recycler_view_qc_check,qcitem);
        listViewCheck.setAdapter(adaptorCheck);

        listViewCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PopUpQcCheckDetail(position);
            }
        });
    }


    private void PopUpQcCheckDetail(int position) {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_pqc_view, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        recyclerViewDetail = dialogView.findViewById(R.id.recycler_view_pqc);
        ImageView button_close = dialogView.findViewById(R.id.img_close_p_qc_check);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // make list

        qcDetailItem = new ArrayList<>();

        String url = "http://snk.autonsi.com/product/GetDataW_Product_Qc_detail?pq_no=" + qcitem.get(position).getMq_no();


        new LoadQcDetail().execute(url);
        Log.d("get_OQC_Detail", url);

        alertDialog.show();
    }

    class LoadQcDetail extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            qcDetailItem = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    alertDialog.dismiss();
                    AlertNotExist("QC Detail not exist !!!");

                    qcDetailItem.removeAll(qcDetailItem);
                    buildQCDetail();
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object1 = jsonArray.getJSONObject(i);

                    String mqhno = object1.getString("pqhno");
                    String subject = object1.getString("check_subject");
                    String checkvalue = object1.getString("check_value");
                    String qty = object1.getString("check_qty");

                    qcDetailItem.add(new QcDetailItem(false, mqhno, subject, checkvalue, qty));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            buildQCDetail();


        }
    }
//
//    private void popoupInspectionView() {
//        Rect displayRectangle = new Rect();
//        Window window = getActivity().getWindow();
//        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
//        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//
//        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_pqc_view, null);
//        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
//        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));
//        recyclerViewDetail = dialogView.findViewById(R.id.recycler_view_pqc);
//
//        ImageView button_close = dialogView.findViewById(R.id.img_close_p_qc_check);
//        button_close.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//        builder.setView(dialogView);
//        alertDialog = builder.create();
//        alertDialog.setCanceledOnTouchOutside(false);
//        buildQCDetail();
//        alertDialog.show();
//
//    }


    private void buildQCDetail() {
        qcDetailAdapter = new QcDetailAdapterView((ArrayList<QcDetailItem>) qcDetailItem);

        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewDetail.setHasFixedSize(true);
        recyclerViewDetail.setAdapter(qcDetailAdapter);

    }

//    //Menu
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        //
//        inflater.inflate(R.menu.menu_pqc, menu);
//        // menu.findItem(R.id.inspection_finish).setVisible(false);
//        super.onCreateOptionsMenu(menu, inflater);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.view_pqc) {
//            //popoupInspectionFinish();
//            if (itemWO.size() == 0) {
//
//            } else {
//                //  popoupInspectionView();
//            }
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    ////
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
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getActivity());
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

}// End Main;
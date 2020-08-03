package com.licheedev.MMSSNK.QC_check;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.Actual.ActualFragment;
import com.licheedev.MMSSNK.Actual.FragmentKeyin;
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
import java.util.List;

public class OQC extends AppCompatActivity {

    String numActual = FragmentKeyin.numActualsend;

    private RecyclerView recyclerViewCHECK;
    TextView tv_qc_pl_no, tv_qc_date, tv_qc_ok_qty, tv_qc_check_qty, tv_qc_defect_qty;
    String PLNo;
    String Datechon = ActualFragment.datechon;
    String Ma_QC_Code = ActualFragment.Ma_QC_Code;
    String olddnochon = ActualFragment.olddnochon;
    private ArrayList<QcCheckItem> qcitem;
    QcDetailAdapter qcDetailAdapter;
    private QcCheckAdapter adaptorCheck;
    // private RecyclerView listViewCheck;
    private int DefectQtyMax = 0;
    ArrayList<QcDetailItem> qcDetailItem;
    RecyclerView recyclerViewDetail;
    private int valDefect = 0;
    private List<QcCheckerLessItem> rViewItemJSON;
    private QcCheckerLessAdaptor adapterLess;
    AlertDialog alertDialog;
    private ListView listViewCheck;

    EditText tv_qcheck_checkqty, tv_qcheck_okcheck;
    TextView tv_qcheck_defectqty;

    AlertDialog alertDialogCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oqc);

        setTitle("OQC check list");
        tv_qc_pl_no = findViewById(R.id.tv_qc_pl_no);
        tv_qc_date = findViewById(R.id.tv_qc_date);
        tv_qc_ok_qty = findViewById(R.id.tv_qc_ok_qty);
        tv_qc_check_qty = findViewById(R.id.tv_qc_check_qty);
        tv_qc_defect_qty = findViewById(R.id.tv_qc_defect_qty);
        listViewCheck = findViewById(R.id.recycler_view_qc_check);


        Datechon.replace("-", "");
        if (Datechon.length() == 8) {
            Datechon = Datechon.substring(0, 4) + "-" + Datechon.substring(4, 6) + "-" + Datechon.substring(6, 8);
            tv_qc_date.setText(Datechon);
        }
        Intent intent = getIntent();
        PLNo = ActualFragment.Productcode;
        tv_qc_pl_no.setText(PLNo);

        LoadQcListAgain();
    }

    private void LoadQcListAgain() {
        String url2 = "http://snk.autonsi.com/product/GetDataW_Product_Qc?item_vcd=" + Ma_QC_Code + "&olddno=" + olddnochon;
        Log.d("PQC_getData", url2);
        new LoadQcList().execute(url2);


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
                DecimalFormat formatter = new DecimalFormat("#,###,###");
                // Total
                JSONObject obj = jsonArray.getJSONObject(0);


                String ok = obj.getString("ok_qty").replace("null", "0");
                ok = formatter.format(Integer.parseInt(ok));
                String check = obj.getString("check_qty").replace("null", "0");
                check = formatter.format(Integer.parseInt(check));
                String defect = obj.getString("defect_qty").replace("null", "0");
                defect = formatter.format(Integer.parseInt(defect));
                tv_qc_ok_qty.setText(ok);
                tv_qc_check_qty.setText(/*qcitem.get(0).getCheckqty()*/ check);
                tv_qc_defect_qty.setText(/*qcitem.get(0).getDefectqty()*/ defect);

                // item
                for (int i = 1; i < jsonArray.length(); i++) {
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    String idMqno = object1.getString("pqno");
                    String mq_no = object1.getString("pq_no");
                    String work_dt = object1.getString("work_dt");
                    String check_qty = object1.getString("check_qty").replace("null", "0");
                    check_qty = formatter.format(Integer.parseInt(check_qty));
                    String ok_qty = object1.getString("ok_qty").replace("null", "0");
                    ok_qty = formatter.format(Integer.parseInt(ok_qty));
                    String defect_qty = object1.getString("defect_qty").replace("null", "0");
                    defect_qty = formatter.format(Integer.parseInt(defect_qty));
                    qcitem.add(new QcCheckItem(idMqno, mq_no, work_dt, check_qty, ok_qty, defect_qty));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            buildListView();


        }
    }

    private void buildListView() {
        adaptorCheck = new QcCheckAdapter(OQC.this, R.layout.item_recycler_view_qc_check, qcitem);
        listViewCheck.setAdapter(adaptorCheck);

        listViewCheck.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!(qcitem.get(position).getDefectqty().equals("0") ||
                        qcitem.get(position).getCheckqty().equals("0"))) {
                    PopUpQcCheckDetail(position);
                } else {
                    AlertNotExist("QC Detail not exist !!!");
                }
            }
        });

        //adaptorCheck = new QcCheckListAdapter(getActivity(), R.layout.item_recycler_view_qc_check, qcitem);
        //listViewCheck.setAdapter(adaptorCheck);
    }

    private void PopUpQcCheckDetail(int position) {
        Rect displayRectangle = new Rect();
        Window window = getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OQC.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(OQC.this).inflate(R.layout.popup_qc_check_qc_detail, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        final String ITEM_MQNO = qcitem.get(position).getIdMqno().replace(",","");

        if (!qcitem.get(position).getDefectqty().trim().equals("null")) {
            DefectQtyMax = Integer.parseInt(qcitem.get(position).getDefectqty().trim().replace(",",""));
        } else {
            DefectQtyMax = 0;
        }
        //anh xa
        Button btn_Save_Detail = dialogView.findViewById(R.id.btn_Save_Detail);
        btn_Save_Detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String ITEM_MQ_HNO_S = "";
                String ITEM_CHECK_QTY_S = "";

                for (int i = 0; i < qcDetailItem.size(); i++) {
                    if (qcDetailItem.get(i).isStCheck()) {
                        ITEM_MQ_HNO_S = ITEM_MQ_HNO_S + "," + qcDetailItem.get(i).getMqhno();
                        ITEM_CHECK_QTY_S = ITEM_CHECK_QTY_S + "," + qcDetailItem.get(i).getQty();
                    }
                }

                if (ITEM_CHECK_QTY_S.length() > 1 && ITEM_MQ_HNO_S.length() > 1) {
                    ITEM_MQ_HNO_S = ITEM_MQ_HNO_S.substring(1, ITEM_MQ_HNO_S.length());
                    ITEM_CHECK_QTY_S = ITEM_CHECK_QTY_S.substring(1, ITEM_CHECK_QTY_S.length());

                    String url = "http://snk.autonsi.com/product/Update_w_product_qc_value?pqno=" + ITEM_MQNO +
                            "&pqhno=" + ITEM_MQ_HNO_S + "&check_qty=" + ITEM_CHECK_QTY_S;

                    //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                    Log.d("upditel", url);
                    new Update_p_mpo_qc_value().execute(url);


                    alertDialog.dismiss();
                } else {
                    AlertNotExist("Please choice item for save !!!");
                    // Toast.makeText(PQC.this, "Please choice item for save !!!", Toast.LENGTH_SHORT).show();

                }

            }
        });

        ImageView button_close = dialogView.findViewById(R.id.img_close_p_qc_check);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        // make list
        recyclerViewDetail = dialogView.findViewById(R.id.recycler_view_qc_check_qc_detail);
        qcDetailItem = new ArrayList<>();

        String url = "http://snk.autonsi.com/product/GetDataW_Product_Qc_detail?pq_no=" + qcitem.get(position).getMq_no();


        new LoadQcDetail().execute(url);
        Log.d("get_QC_Detail", url);

        alertDialog.show();
    }


    class Update_p_mpo_qc_value extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);

                if (object.getBoolean("result")) {
                    Toast.makeText(OQC.this, "Save success", Toast.LENGTH_SHORT).show();
                    LoadQcListAgain();
                } else {
                    Toast.makeText(OQC.this, "Error!!! Save not success", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
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

    private void buildQCDetail() {
        qcDetailAdapter = new QcDetailAdapter((ArrayList<QcDetailItem>) qcDetailItem);

        recyclerViewDetail.setLayoutManager(new LinearLayoutManager(OQC.this));
        recyclerViewDetail.setHasFixedSize(true);
        recyclerViewDetail.setAdapter(qcDetailAdapter);

        qcDetailAdapter.setOnItemClickListener(new QcDetailAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
//                Toast.makeText(getActivity(), "kk " + position, Toast.LENGTH_SHORT).show();
//
            }

            @Override
            public void onItemChecker(int position) {
                if (qcDetailItem.get(position).isStCheck()) {
                    qcDetailItem.get(position).setStCheck(false);
                } else {
                    qcDetailItem.get(position).setStCheck(true);
                }
                qcDetailAdapter.notifyDataSetChanged();
            }

            @Override
            public void onItemEditText(int position) {
                inputTextDialog(position);
            }
        });


    }

    private void inputTextDialog(final int posi) {
        Rect displayRectangle = new Rect();
        Window window = OQC.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OQC.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Qty Value");
        View dialogView = LayoutInflater.from(OQC.this).inflate(R.layout.number_input_layout_ok_cancel, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa

        final EditText input = dialogView.findViewById(R.id.input);
        Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
        Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);

        input.setText(qcDetailItem.get(posi).getQty());

        int tongdetail_tam = 0;
        for (int i = 0; i < qcDetailItem.size(); i++) {

            if (i != posi) {
                tongdetail_tam += Integer.parseInt(qcDetailItem.get(i).getQty());
            }

        }

        int maxdetail = 0;
        maxdetail = DefectQtyMax - tongdetail_tam;


        int finalMaxdetail = maxdetail;

        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = input.getText().toString().trim();


                if (txt.length() > 0) {
                    if (finalMaxdetail < Integer.parseInt(txt)) {

                        input.setError("less than or equal \"" + finalMaxdetail + "\"");
                        input.requestFocus();
                        return;

                    } else {
                        qcDetailItem.get(posi).setStCheck(true);
                        qcDetailItem.get(posi).setQty(Integer.parseInt(txt) + "");
                        qcDetailAdapter.notifyDataSetChanged();
                        alertDialog.cancel();
                    }
                } else {
                    input.setError("Please enter value ");
                    input.requestFocus();
                    return;
                }
            }
        });

        bt_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }


    private void CheckButtonPopup() {
        // TODO
        int checkagain = Integer.parseInt(numActual) - Integer.parseInt(tv_qc_check_qty.getText().toString().replace(",", ""));
        // Toast.makeText(this, "" + checkagain, Toast.LENGTH_SHORT).show();
        valDefect = 0;
        Rect displayRectangle = new Rect();
        // Window window = getActivity().getWindow();
        //   window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OQC.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(OQC.this).inflate(R.layout.popup_check_qc_check_check, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        alertDialogCheck = builder.create();
        alertDialogCheck.setCanceledOnTouchOutside(false);

        //anh xa
        final TextView tv_qcheck_mlno = dialogView.findViewById(R.id.tv_qcheck_mlno);
        TextView tv_qcheck_date = dialogView.findViewById(R.id.tv_qcheck_date);

        tv_qcheck_okcheck = dialogView.findViewById(R.id.tv_qcheck_okcheck);
        tv_qcheck_checkqty = dialogView.findViewById(R.id.tv_qcheck_checkqty);

        tv_qcheck_defectqty = dialogView.findViewById(R.id.tv_qcheck_defectqty);

        recyclerViewCHECK = dialogView.findViewById(R.id.recyclerViewContent);

        tv_qcheck_mlno.setText(PLNo);
        tv_qcheck_date.setText(Datechon);
        final int[] ck = {0};
        final int[] ok = {0};


        tv_qcheck_okcheck.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (int i = 0; i < rViewItemJSON.size(); i++) {
                    rViewItemJSON.get(i).setQty(0 + "");
                    rViewItemJSON.get(i).setCheck(false);
                }
                adapterLess.notifyDataSetChanged();

                if (tv_qcheck_checkqty.getText().length() > 0) {
                    ck[0] = Integer.parseInt(tv_qcheck_checkqty.getText().toString().trim());


                } else {
                    ck[0] = 0;
                }

                if (tv_qcheck_okcheck.getText().length() > 0) {
                    ok[0] = Integer.parseInt(tv_qcheck_okcheck.getText().toString().trim());
                    if (ok[0] > ck[0]) {
                        tv_qcheck_okcheck.setError("less than or equal \"" + ck[0]  + "\"");
                        tv_qcheck_okcheck.setText("" + ck[0] );
                    }
                } else {
                    ok[0] = 0;
                }

                if (ck[0] - ok[0] >= 0) {
                    valDefect = ck[0] - ok[0];
                    String itx = valDefect + "";
                    tv_qcheck_defectqty.setText(itx);
                } else {
                    valDefect = 0;
                    tv_qcheck_defectqty.setText("");
                    tv_qcheck_okcheck.setError("less than or equal \"" + ck[0] + "\"");
                    //tv_qcheck_okcheck.requestFocus();
                    return;
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        tv_qcheck_checkqty.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                for (int i = 0; i < rViewItemJSON.size(); i++) {
                    rViewItemJSON.get(i).setQty(0 + "");
                    rViewItemJSON.get(i).setCheck(false);
                }
                adapterLess.notifyDataSetChanged();


                if (tv_qcheck_checkqty.getText().length() > 0) {
                    ck[0] = Integer.parseInt(tv_qcheck_checkqty.getText().toString().trim());
                    if (Integer.parseInt(tv_qcheck_checkqty.getText().toString()) > checkagain) {
                        tv_qcheck_checkqty.setError("less than or equal \"" + checkagain + "\"");
                        tv_qcheck_checkqty.setText("" + checkagain);
                    }
                } else {
                    ck[0] = 0;
                }

                if (tv_qcheck_okcheck.getText().length() > 0) {
                    ok[0] = Integer.parseInt(tv_qcheck_okcheck.getText().toString().trim());
                } else {
                    ok[0] = 0;
                }

                if (ck[0] - ok[0] >= 0) {
                    valDefect = ck[0] - ok[0];
                    String itx = valDefect + "";
                    tv_qcheck_defectqty.setText(itx);
                } else {
                    valDefect = 0;
                    tv_qcheck_defectqty.setText("");
                    tv_qcheck_okcheck.setError("less than or equal \"" + ck[0] + "\"");
                    //tv_qcheck_okcheck.requestFocus();
                    return;
                }
            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        ImageView button_close = dialogView.findViewById(R.id.img_close_check_check);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogCheck.dismiss();
            }
        });

        //button save
        dialogView.findViewById(R.id.btn_check_save_ck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "Save save", Toast.LENGTH_SHORT).show();

                String ITEM_ICDNO_S = "";
                String ITEM_CHECK_ERR_S = "";

                for (int i = 0; i < rViewItemJSON.size(); i++) {
                    if (rViewItemJSON.get(i).isCheck()) {
                        ITEM_ICDNO_S = ITEM_ICDNO_S + "," + rViewItemJSON.get(i).getIcdno(); //qc_itemcheck_dt__icdno
                        ITEM_CHECK_ERR_S = ITEM_CHECK_ERR_S + "," + rViewItemJSON.get(i).getQty();// qty error input
                    }
                }
                if (tv_qcheck_checkqty.getText().toString().equals("0")) {
                    alertDialog.dismiss();
                } else {

                    if (tv_qcheck_defectqty.getText().toString().trim().equals("0")) {
                        String url = "http://snk.autonsi.com/product" /*HostSV*/ +
                                "/Insert_W_Product_Qc?olddno=" + olddnochon +
                                "&item_vcd=" + Ma_QC_Code +
                                "&check_qty=" + tv_qcheck_checkqty.getText().toString().trim() +
                                "&ok_qty=" + tv_qcheck_okcheck.getText().toString().trim() +
                                "&icdno=" + "" +
                                "&check_qty_error=" + "";
                        Log.d("Save_QC_check", url);
                        //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                        //Log.d("upditel",url);
                        //Insert_p_mpo_qc();
                        new Update_p_mpo_qc_value().execute(url);

                        alertDialogCheck.dismiss();

                        //LoadQcListAgain();

                    } else if (tv_qcheck_defectqty.getText().toString().trim().length() > 0 &&
                            tv_qcheck_okcheck.getText().toString().trim().length() > 0 &&
                            tv_qcheck_checkqty.getText().toString().trim().length() > 0) {

                        for (int i = 0; i < rViewItemJSON.size(); i++) {
                            if (rViewItemJSON.get(i).isCheck()) {
                                if (Integer.parseInt(rViewItemJSON.get(i).getQty()) == 0) {
                                    AlertNotExist("Please input the error number!!!");
                                    return;
                                }
                            }

                        }
                        if (ITEM_ICDNO_S.length() > 1 && ITEM_CHECK_ERR_S.length() > 1) {

                            ITEM_ICDNO_S = ITEM_ICDNO_S.substring(1, ITEM_ICDNO_S.length());
                            ITEM_CHECK_ERR_S = ITEM_CHECK_ERR_S.substring(1, ITEM_CHECK_ERR_S.length());


                            String url = "http://snk.autonsi.com/product" /*HostSV*/ +
                                    "/Insert_W_Product_Qc?olddno=" + olddnochon +
                                    "&item_vcd=" + Ma_QC_Code +
                                    "&check_qty=" + tv_qcheck_checkqty.getText().toString().trim() +
                                    "&ok_qty=" + tv_qcheck_okcheck.getText().toString().trim() +
                                    "&icdno=" + ITEM_ICDNO_S +
                                    "&check_qty_error=" + ITEM_CHECK_ERR_S;
                            Log.d("Save_QC_check", url);
                            //Toast.makeText(getActivity(), url, Toast.LENGTH_SHORT).show();
                            //Log.d("upditel",url);
                            //Insert_p_mpo_qc();
                            new Update_p_mpo_qc_value().execute(url);

                            alertDialogCheck.dismiss();

                            LoadQcListAgain();

                        } else {
                            AlertNotExist("Please choice qty item check !!!");
                            //Toast.makeText(PQC.this, "Please choice qty item check !!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {

                        AlertNotExist("Please enter: \"Check Qty\"  and \"Ok Qty\"");
                        // Toast.makeText(PQC.this, "Please enter: \"Check Qty\"  and \"Ok Qty\"", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        String url = "http://snk.autonsi.com/product/Popup_Qc_Check?item_vcd=" + Ma_QC_Code;

        Log.d("QC_menu_Check", url);
        //LoadCheckQc(url);
        new LoadCheckQc().execute(url);


//        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerViewContent);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        recyclerView.setAdapter(new Adapter(createDummyList(), getContext()));

        alertDialogCheck.show();
        if (checkagain < 0) {
            AlertNotExist("Actual < QC Check");
            alertDialogCheck.dismiss();
        }
    }


    class LoadCheckQc extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            rViewItemJSON = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    Toast.makeText(OQC.this, "No data", Toast.LENGTH_SHORT).show();
                    rViewItemJSON.removeAll(rViewItemJSON);
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("qc_itemcheck_mt");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject object1 = jsonArray.getJSONObject(i);
                    String qc_itemcheck_mt__icno = object1.getString("qc_itemcheck_mt__icno");
                    String qc_itemcheck_mt__check_subject = object1.getString("qc_itemcheck_mt__check_subject");
                    String qc_itemcheck_mt__check_id = object1.getString("qc_itemcheck_mt__check_id");

                    //recyclerViewItemsJSON.add(new Header(i + 1 + "", qc_itemcheck_mt__check_subject, qc_itemcheck_mt__icno, qc_itemcheck_mt__check_id));

                    JSONArray jsonArray_1 = object1.getJSONArray("view_qc_Model");
                    for (int j = 0; j < jsonArray_1.length(); j++) {
                        JSONObject object_2 = jsonArray_1.getJSONObject(j);
                        String qc_itemcheck_dt__icdno = object_2.getString("qc_itemcheck_dt__icdno");
                        String qc_itemcheck_dt__check_name = object_2.getString("qc_itemcheck_dt__check_name");

                        rViewItemJSON.add(new QcCheckerLessItem(false,
                                qc_itemcheck_dt__check_name,
                                "0",
                                qc_itemcheck_dt__icdno,
                                i + 1 + "",
                                qc_itemcheck_mt__check_subject,
                                qc_itemcheck_mt__icno,
                                qc_itemcheck_mt__check_id));
                    }
                }

                buildQCMake();

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void buildQCMake() {
        adapterLess = new QcCheckerLessAdaptor((ArrayList<QcCheckerLessItem>) rViewItemJSON);
        recyclerViewCHECK.setLayoutManager(new LinearLayoutManager(OQC.this));
        recyclerViewCHECK.setAdapter(adapterLess);

        adapterLess.setOnItemClickListener(new QcCheckerLessAdaptor.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemCheck(int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();

                if (rViewItemJSON.get(position).isCheck()) {
                    rViewItemJSON.get(position).setCheck(false);
                } else {
                    rViewItemJSON.get(position).setCheck(true);
                }
                adapterLess.notifyDataSetChanged();
            }

            @Override
            public void onItemEditText(int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
                inputNumberDialog(position);
            }

            @Override
            public void onItemButtonUp(int position) {


                // EditText tv_qcheck_checkqty,tv_qcheck_okcheck;
                // TextView tv_qcheck_defectqty;


//                if (tv_qcheck_checkqty.getText().toString().trim().length() > 0){
//                    AlertNotExist("Input Check Qty");
//
//                } else if (tv_qcheck_okcheck.getText().toString().trim().length() > 0){
//                    AlertNotExist("Input Oke Qty");
//                }else

                int Tong = 0;

                for (int i = 0; i < rViewItemJSON.size(); i++) {
                    Tong = Tong + Integer.parseInt(rViewItemJSON.get(i).getQty().toString().trim());
                }

                if (valDefect <= Tong) {
                    AlertNotExist("Defect Qty max value: \"" + valDefect + "\"");
                    // Toast.makeText(PQC.this, "Defect qty max value: \"" + valDefect + "\"", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    rViewItemJSON.get(position).setCheck(true);
                    rViewItemJSON.get(position).setQty(Integer.parseInt(rViewItemJSON.get(position).getQty().trim()) + 1 + "");
                    adapterLess.notifyDataSetChanged();
                }
            }

            @Override
            public void onItemButtonDown(int position) {
                //Toast.makeText(getActivity(), "" + position, Toast.LENGTH_SHORT).show();
//                if (tv_qcheck_checkqty.getText().toString().trim().length() > 0){
//                    AlertNotExist("Input Check Qty");
//
//                } else if (tv_qcheck_okcheck.getText().toString().trim().length() > 0){
//                    AlertNotExist("Input Oke Qty");
//                }else
                if (0 == Integer.parseInt(rViewItemJSON.get(position).getQty().toString().trim())) {
                    rViewItemJSON.get(position).setCheck(false);
                    adapterLess.notifyDataSetChanged();
                    AlertNotExist("This min value !!!");
                    // Toast.makeText(PQC.this, "this min value !!!", Toast.LENGTH_SHORT).show();
                    return;

                } else {
                    rViewItemJSON.get(position).setCheck(true);
                    rViewItemJSON.get(position).setQty(Integer.parseInt(rViewItemJSON.get(position).getQty().trim()) - 1 + "");
                    if (0 == Integer.parseInt(rViewItemJSON.get(position).getQty().toString().trim())) {
                        rViewItemJSON.get(position).setCheck(false);
                    }
                    adapterLess.notifyDataSetChanged();
                }
            }


        });
    }

    private void inputNumberDialog(final int posi) {
        Rect displayRectangle = new Rect();
        Window window = OQC.this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(OQC.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Qty Value");
        View dialogView = LayoutInflater.from(OQC.this).inflate(R.layout.number_input_layout_ok_cancel, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa

        final EditText input = dialogView.findViewById(R.id.input);
        Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
        Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);

        input.setText(rViewItemJSON.get(posi).getQty());

        int Tong = 0;

        for (int i = 0; i < rViewItemJSON.size(); i++) {
            if (i != posi) {
                Tong = Tong + Integer.parseInt(rViewItemJSON.get(i).getQty().toString().trim());
            }

        }

        int maxdetail = 0;
        maxdetail = valDefect - Tong;

        int finalMaxdetail = maxdetail;

        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = input.getText().toString().trim();
                if (txt.length() > 0) {
                    if (Integer.parseInt(txt) == 0) {
                        rViewItemJSON.get(posi).setCheck(false);
                        rViewItemJSON.get(posi).setQty("0");
                        adapterLess.notifyDataSetChanged();
                        alertDialog.cancel();
                        return;
                    }
                    if (finalMaxdetail < Integer.parseInt(txt)) {

                        input.setError("Defect qty = \"" + finalMaxdetail + "\"");
                        input.requestFocus();
                        return;

                    } else {
                        rViewItemJSON.get(posi).setCheck(true);
                        rViewItemJSON.get(posi).setQty(Integer.parseInt(txt) + "");
                        adapterLess.notifyDataSetChanged();
                        alertDialog.cancel();

                    }
                } else {
                    input.setError("Please enter value ");
                    input.requestFocus();
                    return;
                }
            }
        });

        bt_Cal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });

        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_qc, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.qc_check:
                int checkagain = Integer.parseInt(numActual) - Integer.parseInt(tv_qc_check_qty.getText().toString().replace(",", ""));
                if(checkagain <= 0){
                    AlertNotExist("Check Qty maximum.");
                }else {
                    CheckButtonPopup();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

}

package com.licheedev.MMSSNK.WO;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;


public class WOFragment extends Fragment {

    ListView lvds, lvmaterial, lvTools, lvStaff, lvplan;
    TextView btnSearch;
    TextView edtStartdate, edtEnddate, toolhome, toolactual, nameLogin;
    ImageView logout;
    Spinner spWono, spPono;
    ArrayList<String> Wolist, Polist, arrayDate, arrayOldNo, Polist_view, Wolist_view;
    ArrayAdapter adapterWo, adapterPo;

    String meterial_chon = "", tools_chon = "", staff_chon = "";

    String wono, lineno, productCode, productName, planslickDate, planslickProcess, Dateslick, OldNoslick;

    int vitri;
    TextView Material_click, Tools_click, Staff_click;

    String wo_no = null, po_no = null, sDate, eDate, userlogin = null, StrDate;

    ArrayList<WorkOrderMaster> workOrderMasterArrayList;
    WorkOrderMasterAdaptor workOrderMasterAdaptor;
    ArrayList<MaterialMaster> materialMasterArrayList;
    MaterialMasterAdaptor materialMasterAdaptor;
    ArrayList<ToolsMaster> toolsMastersList;
    ToolsMasterAdaptor toolsMasterAdaptor;
    ArrayList<StaffMaster> staffMasterArrayList;
    StaffMasterAdaptor staffMasterAdaptor;

    ArrayList<PlanMaster> planMasterArrayList;
    PlanMasterAdaptor planMasterAdaptor;
    SharedPreferences sharedPreferences;

    String API_GET_Work_Order_Master = "http://snk.autonsi.com/product/post_m_order_facline_info";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_wo, container, false);

        lvds = view.findViewById(R.id.lvDanhSanh);
        btnSearch = view.findViewById(R.id.buttonSearch);
        edtStartdate = view.findViewById(R.id.edittextStartdate);
        edtEnddate = view.findViewById(R.id.edittextEnddate);
        spWono = view.findViewById(R.id.edittextWono);
        spPono = view.findViewById(R.id.edittextPono);


        new ReadJSON().execute(API_GET_Work_Order_Master);


        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        userlogin = sharedPreferences.getString("TK", "");


        edtStartdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonStartDate();
            }
        });
        edtEnddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChonEndtDate();
            }
        });
        spPono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                po_no = Polist.get(position);
//                Toast.makeText(MainActivity.this, "" + po_no, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        lvds.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitri = i;
                goipoup_infomation_wo(i);
            }

        });


        spWono.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                wo_no = Wolist.get(position);
//                Toast.makeText(MainActivity.this, "" + wo_no, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sDate = edtStartdate.getText().toString();
                eDate = edtEnddate.getText().toString();
                if (po_no.equals("ALL")) {
                    po_no = "";
                }
                if (wo_no.equals("ALL")) {
                    wo_no = "";
                }
                String ipSearch = API_GET_Work_Order_Master +
                        "?po_no=" + po_no +
                        "&fo_no=" + wo_no +
                        "&start_dt=" + sDate +
                        "&end_dt=" + eDate;
                Log.d("AAAAA", ipSearch);
                new ReadJSONnew().execute(ipSearch);

            }
        });

        return view;
    }

    private void Anhxa() {


    }

    private void goipoup_infomation_wo(int vt) {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poup_infomation_wo);
        //dialog.setCanceledOnTouchOutside(false);

        TextView tvCheckstand = dialog.findViewById(R.id.checkstand);

        TextView tvwono = dialog.findViewById(R.id.tvwono);
        TextView tvpono = dialog.findViewById(R.id.tvpono);
        TextView tvlineno = dialog.findViewById(R.id.tvlineno);
        TextView tvproductcode = dialog.findViewById(R.id.tvproductcode);
        TextView tvproductname = dialog.findViewById(R.id.tvproductname);
        TextView tvtarget = dialog.findViewById(R.id.tvtarget);
        TextView tvschedule = dialog.findViewById(R.id.tvschedule);
        TextView tvfactory = dialog.findViewById(R.id.factory);
        TextView tvstartday = dialog.findViewById(R.id.tvstartday);
        TextView tvenddate = dialog.findViewById(R.id.tvenddate);
        TextView back_info = dialog.findViewById(R.id.back_info);

        String fo_no = "", product_code = "", product_name = "", start_dt = "", end_dt = "", target_qty = "", sked_qty = "", po_no = "", line_no = "", factory = "";
        JSONObject objectGet = null;
        try {
            objectGet = new JSONObject(workOrderMasterArrayList.get(vt).toString());
            fo_no = objectGet.getString("fo_no");
            product_code = objectGet.getString("product_code");
            product_name = objectGet.getString("product_name");
            start_dt = objectGet.getString("start_dt");
            end_dt = objectGet.getString("end_dt");
            target_qty = objectGet.getString("target_qty");
            sked_qty = objectGet.getString("sked_qty");
            po_no = objectGet.getString("po_no");
            line_no = objectGet.getString("line_no");
            factory = objectGet.getString("factory");
            wono = fo_no; //// gui qua man hinh check stand by
            lineno = line_no;
            productCode = product_code;
            productName = product_name;

        } catch (JSONException e) {
            e.printStackTrace();
        }

        tvwono.setText(convert_view(fo_no));
        tvenddate.setText(end_dt);
        tvpono.setText(convert_view(po_no));
        tvlineno.setText(line_no);
        tvproductcode.setText(product_code);
        tvproductname.setText(product_name);
        tvtarget.setText(target_qty);
        tvschedule.setText(sked_qty);
        tvfactory.setText(factory);
        tvstartday.setText(start_dt);

        dialog.show();
        back_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        tvCheckstand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //poup_check_stand_by();

                poup_plan();
            }
        });
    }

    private void poup_plan() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poup_plan);

        TextView productNo = dialog.findViewById(R.id.productNo);
        TextView product_Code = dialog.findViewById(R.id.productCode);
        TextView product_Name = dialog.findViewById(R.id.productName);
        TextView closeplan = dialog.findViewById(R.id.closeplan);


        closeplan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        productNo.setText(convert_view(wono));
        product_Code.setText(productCode);
        product_Name.setText(productName);


        lvplan = dialog.findViewById(R.id.lvplan);

        new ReadJSONplan().execute("http://snk.autonsi.com/product/info?fo_no=" +
                wono +
                "&line_no=" +
                lineno);

        lvplan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                OldNoslick = arrayOldNo.get(i);
                Dateslick = arrayDate.get(i);
                planslickDate = planMasterArrayList.get(i).getP_date();
                planslickProcess = planMasterArrayList.get(i).getP_process();
                poup_check_stand_by();
//                vitri = i;
//                goipoup_infomation_wo(i);
            }

        });

        dialog.show();
    }


    private void poup_check_stand_by() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poup_check_stand_by);
        lvmaterial = dialog.findViewById(R.id.lvMaterial);
        lvTools = dialog.findViewById(R.id.lvTool);
        lvStaff = dialog.findViewById(R.id.lvStaff);
        TextView p_productNo, p_Date, p_process;
        p_productNo = dialog.findViewById(R.id.p_productNo);
        p_Date = dialog.findViewById(R.id.p_Date);
        p_process = dialog.findViewById(R.id.p_process);

        TextView check_stand_by_Save = dialog.findViewById(R.id.check_stand_by_Save);

        Material_click = dialog.findViewById(R.id.Material_slick);
        Tools_click = dialog.findViewById(R.id.Tools_slick);
        Staff_click = dialog.findViewById(R.id.Staff_slick);

        check_stand_by_Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                check_stand_by_Save_funtion();
                dialog.dismiss();
            }
        });

        Material_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupMeterial(v);
            }
        });

        Tools_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupTools(v);
            }
        });

        Staff_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPopupStaff(v);
            }
        });

        p_productNo.setText(convert_view(wono));
        p_Date.setText(planslickDate);
        p_process.setText(planslickProcess);

        TextView tvclosecheckstandby = dialog.findViewById(R.id.closecheckstandby);
        tvclosecheckstandby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        // Toast.makeText(this, ""+ wono + lineno, Toast.LENGTH_SHORT).show();
        new ReadJSONcheckstanby().execute("http://snk.autonsi.com/product/checkstandby?fo_no=" +
                wono +
                "&line_no=" +
                lineno);

        // Log.d("AAA","http://snk.autonsi.com/product/GetCheckYN?fo_no=" + wono + "&line_no=" + lineno+ "&process_no=" + planslickProcess + "&olddno=" + OldNoslick + "&work_dt=" + Dateslick);
        //Toast.makeText(this, ""+ "http://snk.autonsi.com/product/checkstandby?fo_no=" + wono + "&line_no=" + lineno+ "&process_no=" + planslickProcess + "&old_no=" + OldNoslick + "&work_dt=" + Dateslick, Toast.LENGTH_SHORT).show();

        new ReadJSONcheckYESNOTYES().execute("http://snk.autonsi.com/product/GetCheckYN?fo_no=" + wono + "&line_no=" + lineno +
                "&process_no=" + planslickProcess + "&olddno=" + OldNoslick + "&work_dt=" + Dateslick);

        Log.d("asddada","http://snk.autonsi.com/product/GetCheckYN?fo_no=" + wono + "&line_no=" + lineno +
                "&process_no=" + planslickProcess + "&olddno=" + OldNoslick + "&work_dt=" + Dateslick);
        dialog.show();
    }


    public void check_stand_by_Save_funtion() {

        if (meterial_chon.equals("Not Yet")) {
            meterial_chon = "N";
        } else {
            meterial_chon = "Y";
        }

        if (tools_chon.equals("Not Yet")) {
            tools_chon = "N";
        } else {
            tools_chon = "Y";
        }

        if (staff_chon.equals("Not Yet")) {
            staff_chon = "N";
        } else {
            staff_chon = "Y";
        }

        new ReadJSONcheck_stand_by_Save().execute("http://snk.autonsi.com/product/updateCheckYN?material_check_yn=" + meterial_chon + "&machine_check_yn=" + tools_chon + "&staff_check_yn=" + staff_chon + "&olddno=" + OldNoslick);

    }

    private class ReadJSONcheck_stand_by_Save extends AsyncTask<String, Void, String> {
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
                String Chuoitrave = object.getString("result");

                if (Chuoitrave.equals("true")) {
                    Toast.makeText(getActivity(), "Save Success!!!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Fail!!!", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadJSONnew extends AsyncTask<String, Void, String> {
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
            workOrderMasterArrayList = new ArrayList<WorkOrderMaster>();
            //Log.d("GET_API: ",s);
            try {
                String target_qtynew;
                String sked_qtynew;
                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONArray jsonArray = object1.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject objectRow = jsonArray.getJSONObject(i);

                    String fo_no = objectRow.getString("fo_no");
                    if(fo_no.equals("null")){
                        fo_no = "";
                    }
                    String product_code = objectRow.getString("product_code");
                    if(product_code.equals("null")){
                        product_code = "";
                    }
                    String product_name = objectRow.getString("product_name");
                    if(product_name.equals("null")){
                        product_name = "";
                    }
                    String start_dt = objectRow.getString("start_dt");
                    if(start_dt.equals("null")){
                        start_dt = "";
                    }
                    String end_dt = objectRow.getString("end_dt");
                    if(end_dt.equals("null")){
                        end_dt = "";
                    }
                    String target_qty = objectRow.getString("target_qty");
                    String sked_qty = objectRow.getString("sked_qty");

                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    if (target_qty != "null") {
                        target_qtynew = formatter.format(Integer.parseInt(target_qty));
                    } else {
                        target_qtynew = "0";
                    }

                    if (sked_qty != "null") {
                        sked_qtynew = formatter.format(Integer.parseInt(sked_qty));
                    } else {
                        sked_qtynew = "0";
                    }


                    String po_no = objectRow.getString("po_no");
                    if(po_no.equals("null")){
                        po_no = "";
                    }
                    String line_no = objectRow.getString("line_no");
                    if(line_no.equals("null")){
                        line_no = "";
                    }
                    String factory = objectRow.getString("factory");
                    if(factory.equals("null")){
                        factory = "";
                    }

                    workOrderMasterArrayList.add(new WorkOrderMaster(fo_no, product_code,
                            product_name, start_dt, end_dt, target_qtynew, sked_qtynew, po_no, line_no, factory));


                }

                workOrderMasterAdaptor = new WorkOrderMasterAdaptor(
                        getActivity(), R.layout.work_order_master_line, workOrderMasterArrayList);
                lvds.setAdapter(workOrderMasterAdaptor);

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
            workOrderMasterArrayList = new ArrayList<WorkOrderMaster>();
            //Log.d("GET_API: ",s);

            Polist = new ArrayList<String>();
            Polist_view = new ArrayList<String>();
            Polist.add("ALL");
            Polist_view.add("ALL");
            Wolist = new ArrayList<String>();
            Wolist.add("ALL");
            Wolist_view = new ArrayList<String>();
            Wolist_view.add("ALL");
            try {

                JSONObject object = new JSONObject(s);
                JSONObject object1 = object.getJSONObject("result");
                JSONArray jsonArray = object1.getJSONArray("Data");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    String target_qtynew;
                    String sked_qtynew;


                    String fo_no = objectRow.getString("fo_no");
                    if(fo_no.equals("null")){
                        fo_no = "";
                    }
                    String product_code = objectRow.getString("product_code");
                    if(product_code.equals("null")){
                        product_code = "";
                    }
                    String product_name = objectRow.getString("product_name");
                    if(product_name.equals("null")){
                        product_name = "";
                    }
                    String start_dt = objectRow.getString("start_dt");
                    if(start_dt.equals("null")){
                        start_dt = "";
                    }
                    String end_dt = objectRow.getString("end_dt");
                    if(end_dt.equals("null")){
                        end_dt = "";
                    }
                    String target_qty = objectRow.getString("target_qty");
                    String sked_qty = objectRow.getString("sked_qty");

                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    if (target_qty != "null") {
                        target_qtynew = formatter.format(Integer.parseInt(target_qty));
                    } else {
                        target_qtynew = "0";
                    }

                    if (sked_qty != "null") {
                        sked_qtynew = formatter.format(Integer.parseInt(sked_qty));
                    } else {
                        sked_qtynew = "0";
                    }


                    String po_no = objectRow.getString("po_no");
                    if(po_no.equals("null")){
                        po_no = "";
                    }
                    String line_no = objectRow.getString("line_no");
                    if(line_no.equals("null")){
                        line_no = "";
                    }
                    String factory = objectRow.getString("factory");
                    if(factory.equals("null")){
                        factory = "";
                    }

                    workOrderMasterArrayList.add(new WorkOrderMaster(fo_no, product_code,
                            product_name, start_dt, end_dt, target_qtynew, sked_qtynew, po_no, line_no, factory));
                    if (!(po_no.equals("null")||po_no.equals(""))) {
                        Polist.add(po_no);
                        Polist_view.add(convert_view(po_no));
                    }
                    if (!(fo_no.equals("null")||fo_no.equals(""))) {
                        Wolist.add(fo_no);
                        Wolist_view.add(convert_view(fo_no));
                    }
                }

                workOrderMasterAdaptor = new WorkOrderMasterAdaptor(
                        getActivity(), R.layout.work_order_master_line, workOrderMasterArrayList);
                lvds.setAdapter(workOrderMasterAdaptor);
                //po no spinner
//
//                String tam = "";
//                for (int i = 0; i < Polist_view.size(); i++) {
//                    if (Polist_view.get(i).equals(tam)) {
//                        Polist_view.remove(i);
//                        i = i - 1;
//                    } else {
//                        tam = Polist_view.get(i);
//                    }
//                }
//
//                for (int i = 0; i < Polist.size(); i++) {
//                    if (Polist.get(i).equals(tam)) {
//                        Polist.remove(i);
//                        i = i - 1;
//                    } else {
//                        tam = Polist.get(i);
//                    }
//                }

                for (int i = 0; i < Polist_view.size(); i++){
                    String tam = Polist_view.get(i);
                    for (int j = i+1; j < Polist_view.size(); j++) {
                        if (Polist_view.get(j).equals(tam)) {
                            Polist_view.remove(j);
                            Polist.remove(j);
                            j = j - 1;
                            //i = i - 1;
                        }
                    }


                }

                adapterPo = new ArrayAdapter(getActivity(), R.layout.spinner_item, Polist_view);
                adapterPo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spPono.setAdapter(adapterPo);
                //wo no spinner
                adapterWo = new ArrayAdapter(getActivity(), R.layout.spinner_item, Wolist_view);
                adapterWo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spWono.setAdapter(adapterWo);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private class ReadJSONplan extends AsyncTask<String, Void, String> {
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

            planMasterArrayList = new ArrayList<PlanMaster>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");

                arrayDate = new ArrayList<String>();

                arrayOldNo = new ArrayList<String>();

                // Toast.makeText(WOMActivity.this, "" + jsonArray.length(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);

                    String olddno = objectRow.getString("olddno");
                    String prod_qty = objectRow.getString("prod_qty");
                    String work_dt = objectRow.getString("work_dt");
                    String start_time = objectRow.getString("start_time");
                    String end_time = objectRow.getString("end_time");
                    String process = objectRow.getString("process");


                    String prod_qtynew;
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    if (prod_qty != "null") {
                        prod_qtynew = formatter.format(Integer.parseInt(prod_qty));
                    } else {
                        prod_qtynew = prod_qty;
                    }

                    String p_date, p_process, p_prod_qty;
                    p_date = work_dt + "\n" + "[" + start_time + "~" + end_time + "]";
                    p_process = process;
                    p_prod_qty = prod_qtynew;

                    planMasterArrayList.add(new PlanMaster(p_date, p_process, p_prod_qty));

                    arrayDate.add(work_dt);
                    arrayOldNo.add(olddno);

                }

                //Toast.makeText(WOMActivity.this, "" + planMasterArrayList, Toast.LENGTH_SHORT).show();

                planMasterAdaptor = new PlanMasterAdaptor(getActivity(), R.layout.plan_line, planMasterArrayList);
                lvplan.setAdapter(planMasterAdaptor);
                // setListViewHeightBasedOnChildren(lvplan);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    public void showPopupMeterial(View v) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.plan_notyes:
                        Material_click.setText("Not Yet");
                        meterial_chon = "Not Yet";
                        return true;
                    case R.id.plan_oke:
                        Material_click.setText("OK");
                        meterial_chon = "OK";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    public void showPopupTools(View v) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.plan_notyes:
                        Tools_click.setText("Not Yet");
                        tools_chon = "Not Yet";
                        return true;
                    case R.id.plan_oke:
                        Tools_click.setText("OK");
                        tools_chon = "OK";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }

    public void showPopupStaff(View v) {

        PopupMenu popupMenu = new PopupMenu(getActivity(), v);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.plan_notyes:
                        Staff_click.setText("Not Yet");
                        staff_chon = "Not Yet";
                        return true;
                    case R.id.plan_oke:
                        Staff_click.setText("OK");
                        staff_chon = "OK";
                        return true;
                    default:
                        return false;
                }
            }
        });
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();

    }


    private class ReadJSONcheckYESNOTYES extends AsyncTask<String, Void, String> {
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
                JSONArray jsonArray = object.getJSONArray("result");
                JSONObject objectRow = jsonArray.getJSONObject(0);
                String material_check_yn = objectRow.getString("material_check_yn");
                String machine_check_yn = objectRow.getString("machine_check_yn");
                String staff_check_yn = objectRow.getString("staff_check_yn");

                meterial_chon = material_check_yn;
                tools_chon = machine_check_yn;
                staff_chon = staff_check_yn;

                Material_click.setText(meterial_chon);
                Tools_click.setText(tools_chon);
                Staff_click.setText(staff_chon);


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }


    private class ReadJSONcheckstanby extends AsyncTask<String, Void, String> {
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

            materialMasterArrayList = new ArrayList<MaterialMaster>();
            toolsMastersList = new ArrayList<ToolsMaster>();
            staffMasterArrayList = new ArrayList<StaffMaster>();

            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    String process_type = objectRow.getString("process_type");
                    String mt_nm = objectRow.getString("mt_nm");
                    String width = objectRow.getString("width");
                    String need_qty = objectRow.getString("need_qty");
                    String feed_size = objectRow.getString("feed_size");
                    String feed_unit = objectRow.getString("feed_unit");
                    String div_cd = objectRow.getString("div_cd");

                    materialMasterArrayList.add(new MaterialMaster(process_type, mt_nm, width, need_qty, feed_size, feed_unit, div_cd));

                }

                //Toast.makeText(WOMActivity.this, "" + materialMasterArrayList, Toast.LENGTH_SHORT).show();

                materialMasterAdaptor = new MaterialMasterAdaptor(getActivity(), R.layout.material_line, materialMasterArrayList);
                lvmaterial.setAdapter(materialMasterAdaptor);
                setListViewHeightBasedOnChildren(lvmaterial);


            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject object = new JSONObject(s);
                JSONObject jsonObject1 = object.getJSONObject("tools");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                JSONArray jsonArrayTools = jsonObject2.getJSONArray("tools");

                for (int i = 0; i < jsonArrayTools.length(); i++) {
                    JSONObject objectRow = jsonArrayTools.getJSONObject(i);
                    String prounit_cd = objectRow.getString("prounit_cd");
                    String mc_type = objectRow.getString("mc_type");
                    String mc_no = objectRow.getString("mc_no");
                    String t_start_dt = objectRow.getString("t_start_dt");
                    String t_end_dt = objectRow.getString("t_end_dt");

                    toolsMastersList.add(new ToolsMaster(prounit_cd, mc_type, mc_no, t_start_dt, t_end_dt));
                }

                //Toast.makeText(WOMActivity.this, "" + jsonArrayTools.length(), Toast.LENGTH_SHORT).show();

                toolsMasterAdaptor = new ToolsMasterAdaptor(getActivity(), R.layout.tools_line, toolsMastersList);
                lvTools.setAdapter(toolsMasterAdaptor);
                setListViewHeightBasedOnChildren(lvTools);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                JSONObject object = new JSONObject(s);
                JSONObject jsonObject1 = object.getJSONObject("staff");
                JSONObject jsonObject2 = jsonObject1.getJSONObject("Data");
                JSONArray jsonArrayTools = jsonObject2.getJSONArray("staff");

                for (int i = 0; i < jsonArrayTools.length(); i++) {
                    JSONObject objectRow = jsonArrayTools.getJSONObject(i);
                    String prounit_cd = objectRow.getString("prounit_cd");
                    String staff_id = objectRow.getString("staff_id");
                    String uname = objectRow.getString("uname");
                    String dt_nm = objectRow.getString("dt_nm");
                    String s_start_dt = objectRow.getString("s_start_dt");
                    String s_end_dt = objectRow.getString("s_end_dt");

                    staffMasterArrayList.add(new StaffMaster(prounit_cd, staff_id, uname, dt_nm, s_start_dt, s_end_dt));
                }

                //Toast.makeText(WOMActivity.this, "" + jsonArrayTools.length(), Toast.LENGTH_SHORT).show();

                staffMasterAdaptor = new StaffMasterAdaptor(getActivity(), R.layout.staff_line, staffMasterArrayList);
                lvStaff.setAdapter(staffMasterAdaptor);
                setListViewHeightBasedOnChildren(lvStaff);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void ChonStartDate() {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);

        DatePickerDialog picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
//                    StrDate = ""+i+i1+i2;
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                edtStartdate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        }, nam, thang, ngay);

        picker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                edtStartdate.setText("");
            }
        });
        picker.show();
    }

    private void ChonEndtDate() {
        final Calendar calendar = Calendar.getInstance();
        int ngay = calendar.get(Calendar.DATE);
        int thang = calendar.get(Calendar.MONTH);
        int nam = calendar.get(Calendar.YEAR);
        DatePickerDialog picker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                calendar.set(i, i1, i2);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
                edtEnddate.setText(simpleDateFormat.format(calendar.getTime()));

            }
        }, nam, thang, ngay);

        picker.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                edtEnddate.setText("");
            }
        });
        picker.show();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;

        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, ViewGroup.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private String convert_view(String txt) {
        String string = "";
        if (txt.equals("null")||txt.equals("")) {
            string = "";
        } else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }


//    private void closekeyboard(){
//        View view = this.getCurrentFocus();
//        if(view != null){
//            InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
//        }
//    }

}

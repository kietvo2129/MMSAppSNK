package com.licheedev.MMSSNK.Actual;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.licheedev.MMSSNK.QC_check.OQC;
import com.licheedev.MMSSNK.QC_check.PQC;
import com.licheedev.MMSSNK.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class FragmentScan extends Fragment {
    TextView tvActualNum, tvDefectiveNum, tvActualcong, tvActualtru, tvDefectcong, tvDefecttru;
    Spinner spinner;
    String dataActual, dataDefect;
    String WOchon, Linechon, processchon, datechon, prcunitchon;
    TextView cauhoi1, cauhoi2, cauhoi3, cauhoi4;
    int numActual, numDefect;
    //String chuoitrave;
    final ArrayList<String> arrayLinepopup = new ArrayList<String>();
    int KTcauhoi = 0;
    String Timesever = "00000000000000";
    public static String numActualsend = "0";
    //////////////////////////
    ListView listView;
    ListView listViewcolor;
    CustomAdapter customAdapter;
    CustomAdaptercolor customAdaptercolor;
    ArrayList<User> userArrayList;
    ArrayList<Usercolor> userArrayListColor;
    int preSelected = -1;
    TextView wopopup, linepopup, processpopup, cancel, save, thematerial;
    String insertmaterial, lookrightpopup, thecolorpopup, thematerialpopup;
    TextView bienkiemtra;
    ImageView logoscan;
    String chuoitrave;

    ImageView imageViewActual;
    EditText scandefective, scanactual;
    //Dialog dialogwaiting;
    //ProgressBar progressBar;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scan, container, false);

        tvActualNum = view.findViewById(R.id.tvActual);
        tvDefectiveNum = view.findViewById(R.id.tvDefective);
        tvActualcong = view.findViewById(R.id.tvActualCong);
        tvActualtru = view.findViewById(R.id.tvActualtru);
        tvDefectcong = view.findViewById(R.id.tvDefectiveCong);
        tvDefecttru = view.findViewById(R.id.tvDefectiveTru);
        scanactual = view.findViewById(R.id.scanactive);
        scandefective = view.findViewById(R.id.scandefective);
        bienkiemtra = view.findViewById(R.id.bienkiemtra);
        logoscan = view.findViewById(R.id.logoscan);
        imageViewActual = view.findViewById(R.id.imageViewActual);
        TextView KTrafragment = getActivity().findViewById(R.id.KTrafragment);
        KTrafragment.setText("ActKeyScan");
        //GetTimeSever()

        userArrayList = new ArrayList<>();
        userArrayListColor = new ArrayList<>();


        scanactual.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        WOchon = bundle.getString("WOchon");
                        Linechon = bundle.getString("Linechon");
                        processchon = bundle.getString("processchon");
                        datechon = bundle.getString("datechon");
                        prcunitchon = bundle.getString("prcunitchon");
                    } else {
                        Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                    }
                    if (checkTime()) {
                        return false;
                    } else {
                        bienkiemtra.setText("A");
                        updateText(scanactual.getText().toString());
                        return true;
                    }

                }
                return false;
            }
        });
        scandefective.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button

                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    // Perform action on key press
                    Bundle bundle = getArguments();
                    if (bundle != null) {
                        WOchon = bundle.getString("WOchon");
                        Linechon = bundle.getString("Linechon");
                        processchon = bundle.getString("processchon");
                        datechon = bundle.getString("datechon");
                        prcunitchon = bundle.getString("prcunitchon");
                    } else {
                        Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                    }
                    if (checkTime()) {
                        return false;
                    } else {
                        bienkiemtra.setText("B");
                        updateText(scandefective.getText().toString());
                        return true;
                    }
                }
                return false;
            }

        });


        imageViewActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "tao kasdad", Toast.LENGTH_SHORT).show();
                bienkiemtra.setText("A");
                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");
                    if (checkTime()) {
                        return;
                    } else {
                        startQRScanner();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }

                ///////////////////////////////dsdaddadadasadsdas

//                new docJSONinsertActual().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
//                        + processchon + "&done_qty=" + numActual + "&refer_qty=" + numDefect);

                // http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=W0000000001&line_no=F1LN00001&process_no=P001&prounit_cd=CUT001&prod_sts_cd=A&done_qty=400&refer_qty=600


            }
        });
        logoscan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getActivity(), "tao kasdad", Toast.LENGTH_SHORT).show();
                bienkiemtra.setText("B");
                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");


//                new docJSONinsertDefective().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no="
//                        + Linechon + "&process_no=" + processchon + "&done_qty=" + numActual + "&refer_qty=" + numDefect);

                    if (checkTime()) {
                        return;
                    } else {
                        startQRScanner();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        tvActualcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");

                    if (checkTime()) {
                        return;
                    } else {
                        String numActualString = tvActualNum.getText().toString();
                        numActual = Integer.parseInt(numActualString);
                        numDefect = Integer.parseInt(tvDefectiveNum.getText().toString());
                        numActual = numActual + 1;
                        new docJSONinsertActual().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                                + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "A" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvActualtru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");

                    if (checkTime()) {
                        return;
                    } else {
                        String numActualString = tvActualNum.getText().toString();
                        numDefect = Integer.parseInt(tvDefectiveNum.getText().toString());
                        numActual = Integer.parseInt(numActualString);
                        numActual = numActual - 1;
                        if (numActual < 0) {
                            numActual = 0;
                        }
                        new docJSONinsertActual().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                                + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "A" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
//                dialogwaiting = new Dialog(getActivity());
//                dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialogwaiting.setContentView(R.layout.dialog_waiting);
//                dialogwaiting.setCancelable(false);
//                dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//                progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//                Circle circle = new Circle();
//                progressBar.setIndeterminateDrawable(circle);
//                dialogwaiting.show();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvDefectcong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String level = ActualFragment.level;
                String Ma_QC_Code = ActualFragment.Ma_QC_Code;

                if (Ma_QC_Code.equals("") || Ma_QC_Code == null || Ma_QC_Code.equals("null")){
                    AlertNotExist("Please insert QC code.");
                    return;
                }

                if (level.equals("")){

                }else if (level.equals("last")) {
                    Intent intent = new Intent(getActivity(), OQC.class);
                    numActualsend = tvActualNum.getText().toString().trim();
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PQC.class);
                    numActualsend = tvActualNum.getText().toString().trim();
                    startActivity(intent);
                }

            }
        });

        tvDefecttru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");
                    if (checkTime()) {
                        return;
                    } else {
                        String numDefectString = tvDefectiveNum.getText().toString();
                        numActual = Integer.parseInt(tvActualNum.getText().toString());
                        numDefect = Integer.parseInt(numDefectString);
                        numDefect = numDefect - 1;
                        if (numDefect < 0) {
                            numDefect = 0;
                        }

                        new docJSONinsertDefective().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                                + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "D" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
//                dialogwaiting = new Dialog(getActivity());
//                dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                dialogwaiting.setContentView(R.layout.dialog_waiting);
//                dialogwaiting.setCancelable(false);
//                dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//                progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//                Circle circle = new Circle();
//                progressBar.setIndeterminateDrawable(circle);
//                dialogwaiting.show();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //
        tvActualNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");

                    if (checkTime()) {
                        return;
                    } else {
                        DialogActual();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });
        tvDefectiveNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = getArguments();
                if (bundle != null) {
                    WOchon = bundle.getString("WOchon");
                    Linechon = bundle.getString("Linechon");
                    processchon = bundle.getString("processchon");
                    datechon = bundle.getString("datechon");
                    prcunitchon = bundle.getString("prcunitchon");
                    if (checkTime()) {
                        return;
                    } else {
                        DialogDefect();
                    }
                } else {
                    AlertNotExist("Data incorrect");
                    //  Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return view;
    }


    private void DialogActual() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        Button BTYes = dialog.findViewById(R.id.yes);
        Button BTNo = dialog.findViewById(R.id.no);
        final TextView txtActual = dialog.findViewById(R.id.TxtActual);
        BTYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataActual = txtActual.getText().toString();
                if (dataActual.length() > 0) {
                    tvActualNum.setText(dataActual);
                    String numActualString = tvActualNum.getText().toString();
                    numActual = Integer.parseInt(numActualString);
                    numDefect = Integer.parseInt(tvDefectiveNum.getText().toString());
                    new docJSONinsertActual().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                            + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "A" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
                    dialog.dismiss();
//                    dialogwaiting = new Dialog(getActivity());
//                    dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialogwaiting.setContentView(R.layout.dialog_waiting);
//                    dialogwaiting.setCancelable(false);
//                    dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//                    progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//                    Circle circle = new Circle();
//                    progressBar.setIndeterminateDrawable(circle);
//                    dialogwaiting.show();
                }
            }
        });
        BTNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void DialogDefect() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout);
        Button BTYes = dialog.findViewById(R.id.yes);
        Button BTNo = dialog.findViewById(R.id.no);
        final TextView txtActual = dialog.findViewById(R.id.TxtActual);
        BTYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataDefect = txtActual.getText().toString();
                if (dataDefect.length() > 0) {
                    tvDefectiveNum.setText(dataDefect);
                    String numActualString = tvActualNum.getText().toString();
                    numActual = Integer.parseInt(numActualString);
                    numDefect = Integer.parseInt(tvDefectiveNum.getText().toString());
                    new docJSONinsertDefective().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                            + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "D" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
                    dialog.dismiss();
//                    dialogwaiting = new Dialog(getActivity());
//                    dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                    dialogwaiting.setContentView(R.layout.dialog_waiting);
//                    dialogwaiting.setCancelable(false);
//                    dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//                    progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//                    Circle circle = new Circle();
//                    progressBar.setIndeterminateDrawable(circle);
//                    dialogwaiting.show();
                }
            }
        });
        BTNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    class docJSONinsertActual extends AsyncTask<String, Integer, String> {
        String chuoitrave;

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject trave = new JSONObject(s);
                chuoitrave = trave.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (chuoitrave == "true") {
                tvActualNum.setText(numActual + "");
                // dialogwaiting.dismiss();
            } else {
                //dialogwaiting.dismiss();
                //Toast.makeText(getActivity(), "Conected Fale", Toast.LENGTH_SHORT).show();
                AlertNotExist("Internet disconnected. Please check again!");
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

    class docJSONpopup extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //try {

            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    //Toast.makeText(getActivity(), "code inexistent!!!!!!!!", Toast.LENGTH_SHORT).show();
                    KTcauhoi = 0;
                }
                ArrayList<String> arraychecktype = new ArrayList<String>();
                ArrayList<String> arraychecksubject = new ArrayList<String>();
                ArrayList<JSONArray> arraycheckname = new ArrayList<JSONArray>();
                JSONObject loi;
                JSONArray checkname;
                arrayLinepopup.removeAll(arrayLinepopup);

                for (int i = 0; i < array.length(); i++) {
                    KTcauhoi = 1;
                    loi = array.getJSONObject(i);
                    String checktype = loi.getString("check_type");
                    String checksubject = loi.getString("check_subject");
                    checkname = loi.getJSONArray("check_name");
                    //String lineNO = array.getString(i);
                    arraychecktype.add(checktype);
                    arraychecksubject.add(checksubject);
                    arraycheckname.add(checkname);
                }
                //Toast.makeText(Actual.this, "" + arraychecksubject.get(0), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < arraychecktype.size(); i++) {

                    switch (arraychecktype.get(i)) {
                        case "text":
                            cauhoi1.setText("1." + arraycheckname.get(i).get(0));
                            break;
                        case "select":
                            cauhoi2.setText("2." + arraychecksubject.get(i));
                            arrayLinepopup.removeAll(arrayLinepopup);
                            for (int k = 0; k < arraycheckname.get(i).length(); k++) {
                                arrayLinepopup.add(arraycheckname.get(i).get(k) + "");
                            }
                            lookright();
                            break;
                        case "radio":
                            cauhoi3.setText("3." + arraychecksubject.get(i));
                            for (int k = 0; k < arraycheckname.get(i).length(); k++) {
                                userArrayListColor.add(new Usercolor(arraycheckname.get(i).get(k) + "", false));
                            }
                            customAdaptercolor = new CustomAdaptercolor(getActivity(), userArrayListColor);
                            thecolor();
                            break;
                        case "check":
                            cauhoi4.setText("4." + arraychecksubject.get(i));
                            for (int k = 0; k < arraycheckname.get(i).length(); k++) {
                                userArrayList.add(new User(arraycheckname.get(i).get(k) + "", false));
                            }
                            customAdapter = new CustomAdapter(getActivity(), userArrayList);
                            defectmaterial();
                            break;
                        default:
                            // Toast.makeText(getActivity(), "Enrol", Toast.LENGTH_SHORT).show();
                            break;
                    }


                }


            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void lookright() {
        ArrayAdapter arrayAdapterpopup = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item, arrayLinepopup);
        arrayAdapterpopup.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapterpopup);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                final String line = arrayLinepopup.get(position);

                lookrightpopup = line;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void thecolor() {
        listViewcolor.setAdapter(customAdaptercolor);
        listViewcolor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int tong = 0;
                for (int i = 0; i < userArrayListColor.size(); i++) {
                    if (userArrayListColor.get(i).isChecked() == true) {
                        tong += 1;
                    }
                }
                if (tong == 0) {
                    preSelected = -1;
                }
                Usercolor user = userArrayListColor.get(position);
                user.setChecked(true);
                userArrayListColor.set(position, user);
                if (preSelected > -1) {
                    Usercolor usermodel = userArrayListColor.get(preSelected);
                    usermodel.setChecked(false);
                    userArrayListColor.set(preSelected, usermodel);
                }
                preSelected = position;
                customAdaptercolor.UpdateListview(userArrayListColor);
            }
        });

    }

    private void defectmaterial() {
        listView.setAdapter(customAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                User user = userArrayList.get(position);
                if (user.isChecked()) {
                    user.setChecked(false);
                } else {
                    user.setChecked(true);
                }
                userArrayList.set(position, user);
                customAdapter.UpdateListview(userArrayList);
                //Toast.makeText(Actual.this, ""+ userArrayList.get(position).getTen() + userArrayList.get(position).isChecked() , Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void savepopup() {
        thematerialpopup = "";
        thecolorpopup = "";

        insertmaterial = thematerial.getText().toString().trim();
        //lookrightpopup Lấy từ lookright();

        for (int i = 0; i < userArrayList.size(); i++) {
            if (userArrayList.get(i).isChecked() == true) {
                thematerialpopup += userArrayList.get(i).getTen() + " ";
            }
        }
        for (int i = 0; i < userArrayListColor.size(); i++) {
            if (userArrayListColor.get(i).isChecked() == true) {
                thecolorpopup += userArrayListColor.get(i).getTen() + " ";
            }
        }

        if (KTcauhoi == 0) {
            new docJSONinsertDefective().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                    + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "D" + "&done_qty=" + numActual + "&refer_qty=" + numDefect);
//            dialogwaiting = new Dialog(getActivity());
//            dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialogwaiting.setContentView(R.layout.dialog_waiting);
//            dialogwaiting.setCancelable(false);
//            dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//            progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//            Circle circle = new Circle();
//            progressBar.setIndeterminateDrawable(circle);
//            dialogwaiting.show();
        } else {

            String API = "http://snk.autonsi.com/product/post_popup_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon +
                    "&check_qty=65&check_value=" + insertmaterial + "|" + lookrightpopup + "|" + thecolorpopup.trim() + "|" + thematerialpopup.trim() + "&actual=" + numActual
                    + "&defective=" + numDefect + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon;

            API = API.replace(" ", "%20");

            new docJSONinsertDefective().execute(API);
//            dialogwaiting = new Dialog(getActivity());
//            dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialogwaiting.setContentView(R.layout.dialog_waiting);
//            dialogwaiting.setCancelable(false);
//            dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//            progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//            Circle circle = new Circle();
//            progressBar.setIndeterminateDrawable(circle);
//            dialogwaiting.show();
        }

    }

    class docJSONinsertDefective extends AsyncTask<String, Integer, String> {
        String chuoitrave;

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject trave = new JSONObject(s);
                chuoitrave = trave.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (chuoitrave == "true") {
                tvDefectiveNum.setText(numDefect + "");
                //dialogwaiting.dismiss();
            } else {
                //dialogwaiting.dismiss();
                //Toast.makeText(getActivity(), "Conected Fale", Toast.LENGTH_SHORT).show();
                AlertNotExist("Internet disconnected. Please check again!");
            }
        }
    }


    public void startQRScanner() {
        IntentIntegrator.forSupportFragment(this).initiateScan();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                updateText(result.getContents());
            }

        }
    }

    private void updateText(String scanCode) {
        if (bienkiemtra.getText().toString() == "A") {
            String numActualString = tvActualNum.getText().toString();
            int numActual = Integer.parseInt(numActualString);
            numActual = numActual + 1;
            tvActualNum.setText(numActual + "");
            scanactual.setText(scanCode);
            new docJSONbarcode().execute("http://snk.autonsi.com/product/post_barcode_m_order_facline_dayhist?fo_no=" +
                    WOchon +
                    "&line_no=" +
                    Linechon +
                    "&process_no=" +
                    processchon +
                    "&prod_sts_cd=" +
                    "A" +
                    "&barcode=" +
                    scanCode +
                    "&done_qty=" +
                    tvActualNum.getText().toString() +
                    "&refer_qty=" +
                    tvDefectiveNum.getText().toString()
                    + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon);
//            dialogwaiting = new Dialog(getActivity());
//            dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialogwaiting.setContentView(R.layout.dialog_waiting);
//            dialogwaiting.setCancelable(false);
//            dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//            progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//            Circle circle = new Circle();
//            progressBar.setIndeterminateDrawable(circle);
//            dialogwaiting.show();
        } else {
            String numDefectString = tvDefectiveNum.getText().toString();
            int numDefect = Integer.parseInt(numDefectString);
            numDefect = numDefect + 1;
            tvDefectiveNum.setText(numDefect + "");
            scandefective.setText(scanCode);
            new docJSONbarcodeero().execute("http://snk.autonsi.com/product/post_barcode_m_order_facline_dayhist?fo_no=" +
                    WOchon +
                    "&line_no=" +
                    Linechon +
                    "&process_no=" +
                    processchon +
                    "&prod_sts_cd=" +
                    "D" +
                    "&barcode=" +
                    scanCode +
                    "&done_qty=" +
                    tvActualNum.getText().toString() +
                    "&refer_qty=" +
                    tvDefectiveNum.getText().toString()
                    + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon);
//            dialogwaiting = new Dialog(getActivity());
//            dialogwaiting.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialogwaiting.setContentView(R.layout.dialog_waiting);
//            dialogwaiting.setCancelable(false);
//            dialogwaiting.setCanceledOnTouchOutside(false);
//
//
//            progressBar = dialogwaiting.findViewById(R.id.Spinkit);
//            Circle circle = new Circle();
//            progressBar.setIndeterminateDrawable(circle);
//            dialogwaiting.show();
        }
    }

    class docJSONbarcode extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDungbarcode_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                //JSONArray mangJSON = new JSONArray(s);
                JSONObject trave = new JSONObject(s);
                chuoitrave = trave.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (chuoitrave == "true") {
                Toast.makeText(getActivity(), "ADDED", Toast.LENGTH_SHORT).show();
                scanactual.setText("");
                //dialogwaiting.dismiss();
            } else {
                //dialogwaiting.dismiss();
                Toast.makeText(getActivity(), "Not Add", Toast.LENGTH_SHORT).show();
                String numActualString = tvActualNum.getText().toString();
                int numActual = Integer.parseInt(numActualString);
                numActual = numActual - 1;
                tvActualNum.setText(numActual + "");
            }
        }
    }

    class docJSONbarcodeero extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDungbarcode_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                //JSONArray mangJSON = new JSONArray(s);
                JSONObject trave = new JSONObject(s);
                chuoitrave = trave.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
            }

            if (chuoitrave == "true") {
                Toast.makeText(getActivity(), "ADDED", Toast.LENGTH_SHORT).show();
                scandefective.setText("");
                //dialogwaiting.dismiss();
            } else {
                // dialogwaiting.dismiss();
                Toast.makeText(getActivity(), "Not Add", Toast.LENGTH_SHORT).show();
                String numDefectString = tvDefectiveNum.getText().toString();
                int numDefect = Integer.parseInt(numDefectString);
                numDefect = numDefect - 1;
                tvDefectiveNum.setText(numDefect + "");
            }
        }
    }

    private String docNoiDungbarcode_Tu_URL(String theUrl) {
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
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
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

    private boolean checkTime() {

        //GetTimeSever()

        int timeChon = Integer.parseInt(datechon);
        int timeNgay = Integer.parseInt(Timesever.substring(0, 8));
        int timeGio = Integer.parseInt(Timesever.substring(8));
        if (timeChon == timeNgay - 1 && timeGio > 75959) {
            AlertNotExist("Please select a new date.");
            return true;
        } else if (timeChon == timeNgay && timeGio < 80000) {
            AlertNotExist("Please select an old date.");
            return true;
        } else {
            return false;
        }

    }

    private void GetTimeSever() {
        new docTimesever().execute("http://snk.autonsi.com/product/get_time_sever");
        Log.d("docTimesever", "http://snk.autonsi.com/product/get_time_sever");
    }

    class docTimesever extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject trave = new JSONObject(s);
                Timesever = trave.getString("result");

            } catch (JSONException e) {
                e.printStackTrace();
                AlertNotExist("Not connected to server.");
            }

        }
    }


}

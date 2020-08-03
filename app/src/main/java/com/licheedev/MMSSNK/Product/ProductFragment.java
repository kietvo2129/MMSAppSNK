package com.licheedev.MMSSNK.Product;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.utils.AidlUtil;
import com.licheedev.MMSSNK.utils.ESCUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class ProductFragment extends Fragment {
    TextView tv_bobbinCode, tv_LotCode;
    ImageView btn_scan_BobbinCode;
    TextView buttonMapping, buttonPrint;
    final ArrayList<String> arrayLineWO = new ArrayList<String>();
    final ArrayList<String> arrayLineRouting = new ArrayList<String>();
    final ArrayList<String> arrayLineProcessCD = new ArrayList<String>();
    final ArrayList<String> arrayLineProcessNM = new ArrayList<String>();
    final ArrayList<String> arrayLineDateCD = new ArrayList<String>();
    final ArrayList<String> arrayLineDateNM = new ArrayList<String>();

    ArrayList<ProductMaster> productMasterArrayList;
    ProductMasterAdaptor productMasterAdaptor;

    Spinner spinnerWO, spinnerRounting, spinnerPro, spinnerDate;
    String WOchon, ProcessChon, Datechon, GroupQtynum, QRQtynum;
    EditText GroupQty, QRQty;
    TextView buttonCreating;
    ListView lvDanhSanhproduct;
    private int vitri = -1;

    String API_MAPPING = "http://snk.autonsi.com/Lot/maping_code_bb?bb_no="; //BB000000001&wmtid=1123";
    String API_MAPPING1 = "&wmtid=";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product, container, false);

        btn_scan_BobbinCode = view.findViewById(R.id.btn_scan_BobbinCode);
        //btn_scan_LotCode = view.findViewById(R.id.btn_scan_LotCode);
        tv_bobbinCode = view.findViewById(R.id.tv_bobbinCode);
        tv_LotCode = view.findViewById(R.id.tv_lotCode);
        spinnerWO = view.findViewById(R.id.spinnerWO);
        spinnerRounting = view.findViewById(R.id.spinnerRounting);
        spinnerPro = view.findViewById(R.id.spinnerPro);
        spinnerDate = view.findViewById(R.id.spinnerDate);
        GroupQty = view.findViewById(R.id.GroupQty);
        QRQty = view.findViewById(R.id.QRQty);
        buttonCreating = view.findViewById(R.id.buttonCreating);
        lvDanhSanhproduct = view.findViewById(R.id.lvDanhSanhproduct);
        buttonMapping = view.findViewById(R.id.buttonMapping);
        buttonPrint = view.findViewById(R.id.buttonPrint);

        buttonMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_bobbinCode.getText().toString().length() > 0 && vitri >= 0) {
                    callMapping();
                } else {
                    Toast.makeText(getActivity(), "Please enter Bobbin Code and select Lot Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_bobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext("tv_bobbinCode");
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callPrint();
            }
        });

        btn_scan_BobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRScanner();
            }
        });

        new docJSONwo().execute("http://snk.autonsi.com/Lot/selec_wo");

        buttonCreating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_LotCode.setText("");
                GroupQtynum = GroupQty.getText().toString().trim();
                QRQtynum = QRQty.getText().toString().trim();
                productMasterArrayList = new ArrayList<ProductMaster>();
                productMasterAdaptor = new ProductMasterAdaptor(
                        getActivity(), R.layout.item_product, productMasterArrayList);
                lvDanhSanhproduct.setAdapter(productMasterAdaptor);

                if(arrayLineProcessNM.size() == 0 || arrayLineDateNM.size() == 0||arrayLineWO.size() == 0 || arrayLineRouting.size() == 0) {
                    Toast.makeText(getActivity(), "Data incorrect!!!", Toast.LENGTH_SHORT).show();
                }else {
                    if (GroupQty.length() != 0) {
                        if (QRQtynum.length() != 0) {
                            new docJSONData().execute("http://snk.autonsi.com/Lot/Creat_row?wo_no=" +
                                    WOchon +
                                    "&qr_qty=" +
                                    QRQtynum +
                                    "&gr_qty=" +
                                    GroupQtynum +
                                    "&prounit_cd=" +
                                    ProcessChon +
                                    "&date=" +
                                    Datechon +
                                    "&line_no=" +
                                    arrayLineRouting.get(0));

                        } else {
                            Toast.makeText(getActivity(), "Insert QR Qty", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getActivity(), "Insert Group Qty", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        lvDanhSanhproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitri = i;
                tv_LotCode.setText(productMasterArrayList.get(i).getMt_qrcode());
                productMasterAdaptor.notifyDataSetChanged();
            }
        });

        return view;
    }

    private void intext(final String key_location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Title");
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.text_input_layout, (ViewGroup) getView(), false);
// Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
// Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (key_location == "tv_bobbinCode") {
                    String bbCode = input.getText().toString();
                    tv_bobbinCode.setText(bbCode);
                } else {
                }
                dialog.dismiss();
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

    private void callPrint() {
        if (vitri < 0) {
            Toast.makeText(getActivity(), "Please select Lot code print", Toast.LENGTH_SHORT).show();
        } else {
            String val = productMasterArrayList.get(vitri).getMt_qrcode();

            if (val == "") {
                Toast.makeText(getActivity(), "Please select Lot code print", Toast.LENGTH_SHORT).show();
            } else {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_inventory_mtcode_qr);

                ImageView imgQr = dialog.findViewById(R.id.img_qr);
                TextView tvMtCode = dialog.findViewById(R.id.tv_mtcode);
                BitMatrix bitMatrix = null;
                if (val.length() == 0 || val == "null") {
                    Toast.makeText(getActivity(), "No QR code!", Toast.LENGTH_SHORT).show();
                } else {
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    try {
                        bitMatrix = qrCodeWriter.encode(val, BarcodeFormat.QR_CODE, 400, 400);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    int height = bitMatrix.getHeight();
                    int width = bitMatrix.getWidth();
                    Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    imgQr.setImageBitmap(bmp);
                    tvMtCode.setText(val);

                    byte[] send;
                    send = ESCUtil.alignCenter();
                    AidlUtil.getInstance().sendRawData(send);
                    AidlUtil.getInstance().printQr(val, 8, 1);
                    AidlUtil.getInstance().printText(val, 20, true, false);
                    AidlUtil.getInstance().linewrap(4);
                }
                dialog.show();
            }
        }
    }

    private void callMapping() {
        String bbNo = tv_bobbinCode.getText().toString();
        String idWm ="";
        if (productMasterArrayList.size() == 0){

        }else {
            idWm = productMasterArrayList.get(vitri).getWmtid();
            String api = API_MAPPING + bbNo + API_MAPPING1 + idWm;
            if (bbNo.length() > 0 && idWm.length() > 0) {
                new jsonMapping().execute(api);
            } else {
                Toast.makeText(getActivity(), "Please insert \"Bobbin code\" and select \"Lot code\"", Toast.LENGTH_LONG).show();
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
                tv_bobbinCode.setText(result.getContents());
            }
        }
    }

    class docJSONData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            productMasterArrayList = new ArrayList<ProductMaster>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String wmtid = jsonObject1.getString("wmtid");
                    String gr_qty = jsonObject1.getString("gr_qty");
                    String mt_qrcode = jsonObject1.getString("mt_qrcode");
                    productMasterArrayList.add(new ProductMaster(wmtid, gr_qty, null, mt_qrcode));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            productMasterAdaptor = new ProductMasterAdaptor(
                    getActivity(), R.layout.item_product, productMasterArrayList);
            lvDanhSanhproduct.setAdapter(productMasterAdaptor);


        }
    }

    class jsonMapping extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //productMasterArrayList = new ArrayList<ProductMaster>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                Boolean kq = jsonObject.getBoolean("result");
                if (kq == true) {
                    productMasterArrayList.get(vitri).setBb_no(tv_bobbinCode.getText().toString());
                    Toast.makeText(getActivity(), "Completed", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "Bobbin code not exist, please check again", Toast.LENGTH_SHORT).show();
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            productMasterAdaptor.notifyDataSetChanged();
        }
    }


    class docJSONwo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayLineWO.removeAll(arrayLineWO);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    arrayLineWO.add(array.getString(i));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            LickspinnerWO();

        }
    }

    private void LickspinnerWO() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineWO);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWO.setAdapter(arrayAdapter);
        spinnerWO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                WOchon = arrayLineWO.get(position);

                new docJSONline().execute("http://snk.autonsi.com/Lot/return_line_pro?wo=" + WOchon);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class docJSONline extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayLineRouting.removeAll(arrayLineRouting);
            arrayLineProcessCD.removeAll(arrayLineProcessCD);
            arrayLineProcessNM.removeAll(arrayLineProcessNM);
            try {
                JSONArray array = new JSONArray(s);
                JSONObject object = array.getJSONObject(0);
                arrayLineRouting.add(object.getString("line_no"));
                JSONArray arrayCD = object.getJSONArray("list_prounit_cd");
                JSONArray arrayNM = object.getJSONArray("list_process_nm");
                for (int i = 0; i < arrayCD.length(); i++) {
                    arrayLineProcessCD.add(arrayCD.getString(i));
                }
                for (int j = 0; j < arrayNM.length(); j++) {
                    arrayLineProcessNM.add(arrayNM.getString(j));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            LickspinnerRouting();

        }
    }

    private void LickspinnerRouting() {
        ArrayAdapter arrayAdapter1 = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineRouting);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRounting.setAdapter(arrayAdapter1);
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineProcessNM);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPro.setAdapter(arrayAdapter);

        /// kiem tra loi cua du lieu tra vee
        if(arrayLineProcessNM.size() ==0){
            arrayLineDateCD.removeAll(arrayLineDateCD);
            arrayLineDateNM.removeAll(arrayLineDateNM);
            ArrayAdapter arrayAdapter2 = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineDateNM);
            arrayAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerDate.setAdapter(arrayAdapter);
        }
        /// kiem tra loi cua du lieu tra vee
        spinnerPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                ProcessChon = arrayLineProcessCD.get(position);
                new docJSONDate().execute("http://snk.autonsi.com/Lot/return_date?wo=" +
                        WOchon +
                        "&prounit_cd=" +
                        ProcessChon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class docJSONDate extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayLineDateCD.removeAll(arrayLineDateCD);
            arrayLineDateNM.removeAll(arrayLineDateNM);
            try {
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonObject = array.getJSONObject(i);
                    arrayLineDateCD.add(jsonObject.getString("work_ymd"));
                    arrayLineDateNM.add(jsonObject.getString("work_dt"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            LickDate();
        }
    }

    private void LickDate() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineDateNM);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDate.setAdapter(arrayAdapter);
        spinnerDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {

                Datechon = arrayLineDateCD.get(position);
                //Toast.makeText(getActivity(), "" + Datechon, Toast.LENGTH_SHORT).show();
                // http://snk.autonsi.com/Lot/Creat_row?wo_no=W0000000016&qr_qty=1&gr_qty=30&prounit_cd=CUT002&date=20191005&line_no=LN00020

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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

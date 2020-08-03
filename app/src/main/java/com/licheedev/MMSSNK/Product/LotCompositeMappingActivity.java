package com.licheedev.MMSSNK.Product;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.licheedev.MMSSNK.Actual.ActualFragment;
import com.licheedev.MMSSNK.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class LotCompositeMappingActivity extends AppCompatActivity {
    TextView tv_LotCode, tv_bobbinCode,tv_MTbobbin ;
    String LotCode;
    String line_nochon = ActualFragment.line_nochon;
    ImageView btn_scan_BobbinCode,btn_scan_MTbobbin;
    String bom_no, ProcessChon,level;
    ArrayList<LotCompositeMaster> lotCompositeMasterArrayList;
    LotCompositeMasterAdaptor lotCompositeMasterAdaptor;
    ListView lvDanhSanhproduct;
    TextView buttonSave,buttonFinish,buttonDelete;
    String MaterialCode;
    int idchon = -1, kiemtrascan = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lot_composite_mapping);
        setTitle("Lot Composite Mapping");
        tv_LotCode = findViewById(R.id.tv_lotCode);
        tv_MTbobbin =  findViewById(R.id.tv_MTbobbin);
        btn_scan_MTbobbin = findViewById(R.id.btn_scan_MTbobbin);
        btn_scan_BobbinCode = findViewById(R.id.btn_scan_BobbinCode);
        tv_bobbinCode = findViewById(R.id.tv_bobbinCode);
        lvDanhSanhproduct = findViewById(R.id.lvDanhSanhproduct);
        buttonSave = findViewById(R.id.buttonSave);
        buttonFinish = findViewById(R.id.buttonFinish);
        buttonDelete = findViewById(R.id.buttonDelete);


        Intent intent = getIntent();
        LotCode = intent.getStringExtra("mt_qrcode");
        tv_LotCode.setText(LotCode);
        btn_scan_BobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtrascan = 0;
                startQRScanner();
            }
        });
        btn_scan_MTbobbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtrascan = 1;
                startQRScanner();
            }
        });
        tv_bobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext("tv_bobbinCode");
            }
        });

        tv_MTbobbin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext("tv_MTbobbin");
            }
        });

        bom_no = ActualFragment.bom_no;
        ProcessChon = ActualFragment.processchon;
        level = ActualFragment.level;
        Loaddata();
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MaterialCode = tv_bobbinCode.getText().toString().trim();
               String MTbb_no = tv_MTbobbin.getText().toString().trim();
                MaterialCode = MaterialCode.replace(" ", "%20");
                MTbb_no = MTbb_no.replace(" ", "%20");
                if (MaterialCode.length() == 0 && MTbb_no.length() == 0) {
                    //Toast.makeText(LotCompositeMappingActivity.this, "Insert Material Code", Toast.LENGTH_SHORT).show();
                    AlertNotExist("Insert Material Code or MT Bobbin");
                } else {
                    new docJSONSaveMaterial().execute("http://snk.autonsi.com/Lot/insertw_material_mping_api?mt_cd=" +
                            LotCode + "&qr_code=" +
                            MaterialCode + "&bb_no="+ MTbb_no + "&bom_no=" + bom_no + "&prounit_cd=" +
                            ProcessChon+ "&level=" + level + "&line_no=" + line_nochon);
                    Log.d("SaveMaterial", "http://snk.autonsi.com/Lot/insertw_material_mping_api?mt_cd=" +
                            LotCode + "&qr_code=" + MaterialCode +  "&bb_no="+ MTbb_no + "&bom_no=" + bom_no +
                            "&prounit_cd=" + ProcessChon+ "&level=" + level + "&line_no=" + line_nochon);
                }

            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String MTbb_no = tv_MTbobbin.getText().toString().trim();
                MTbb_no = MTbb_no.replace(" ", "%20");
                if (idchon == -1){
                    //Toast.makeText(LotCompositeMappingActivity.this, "No lines have been selected yet", Toast.LENGTH_SHORT).show();
                    AlertNotExist("No lines have been selected yet");
                }else {
                    if (lotCompositeMasterArrayList.get(idchon).getUse_yn().equals("Y")){
                        new docJSONfinish().execute("http://snk.autonsi.com/Lot/chane_mapping_api?id=" + lotCompositeMasterArrayList.get(idchon).getWmmid() + "&bb_no="+ MTbb_no + "&level=" + level);
                        Log.d("Finsish","http://snk.autonsi.com/Lot/chane_mapping_api?id=" + lotCompositeMasterArrayList.get(idchon).getWmmid() + "&bb_no="+ MTbb_no + "&level=" + level);
                    }
                }
            }
        });
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (idchon == -1){
                    //Toast.makeText(LotCompositeMappingActivity.this, "No lines have been selected yet", Toast.LENGTH_SHORT).show();
                    AlertNotExist("No lines have been selected yet");
                }else {
                    if (lotCompositeMasterArrayList.get(idchon).getUse_yn().equals("Y")||lotCompositeMasterArrayList.get(idchon).getUse_yn().equals("N")){
                        new docJSONfinish().execute("http://snk.autonsi.com/Lot/Delete_mapping_api?id=" + lotCompositeMasterArrayList.get(idchon).getWmmid()  + "&level=" + level);
                        Log.d("delect","http://snk.autonsi.com/Lot/Delete_mapping_api?id=" + lotCompositeMasterArrayList.get(idchon).getWmmid()  + "&level=" + level);
                    }
                }
            }
        });
        lvDanhSanhproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                idchon = position;
//                Toast.makeText(LotCompositeMappingActivity.this, lotCompositeMasterArrayList.get(position).getUse_yn(), Toast.LENGTH_SHORT).show();
//                lotCompositeMasterArrayList.get(position).getUse_yn()
            }
        });
    }

    private void Loaddata(){
        new docJSONmapping().execute("http://snk.autonsi.com/lot/ds_mapping_w_api?mt_lot=" +
                LotCode +
                "&bom_no=" +
                bom_no +
                "&prounit_cd=" +
                ProcessChon+"&level=" + level + "&line_no=" + line_nochon);
        Log.d("Mapping", "http://snk.autonsi.com/lot/ds_mapping_w_api?mt_lot=" +
                LotCode +
                "&bom_no=" +
                bom_no +
                "&prounit_cd=" +
                ProcessChon + "&level=" + level+ "&line_no=" + line_nochon);
    }


    public void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    private void intext(final String key_location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LotCompositeMappingActivity.this);
        builder.setTitle("Material Code");

        View viewInflated = LayoutInflater.from(LotCompositeMappingActivity.this).inflate(R.layout.text_input_layout, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (key_location == "tv_bobbinCode") {
                    String bbCode = input.getText().toString();
                    tv_bobbinCode.setText(bbCode);
                } else if (key_location == "tv_MTbobbin"){
                    String bbCode = input.getText().toString();
                    tv_MTbobbin.setText(bbCode);
                } else {}
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(LotCompositeMappingActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                if (kiemtrascan == 0) {
                    tv_bobbinCode.setText(result.getContents());
                }else if (kiemtrascan == 1){
                    tv_MTbobbin.setText(result.getContents());
                }
            }
        }
    }

    class docJSONmapping extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            lotCompositeMasterArrayList = new ArrayList<LotCompositeMaster>();
            try {
                JSONArray jsonArray = new JSONArray(s);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String mt_type = jsonObject1.getString("mt_type");
                    String wmmid = jsonObject1.getString("wmmid");
                    String mt_no = jsonObject1.getString("mt_no");
                    String mt_lot = jsonObject1.getString("mt_lot");
                    String use_yn = jsonObject1.getString("use_yn");
                    if (mt_type.equals("null")||mt_type.equals("")) {
                        mt_type = " ";
                    }
                    if (use_yn.equals("null")) {
                        use_yn = "";
                    }
                    String mt_cd = jsonObject1.getString("mt_cd");
                    if (mt_cd.equals("null")) {
                        mt_cd = "";
                    }
                    lotCompositeMasterArrayList.add(new LotCompositeMaster(wmmid, mt_no, mt_lot, use_yn, mt_cd, mt_type));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            lotCompositeMasterAdaptor = new LotCompositeMasterAdaptor(
                    LotCompositeMappingActivity.this, R.layout.item_product_mapping, lotCompositeMasterArrayList);
            lvDanhSanhproduct.setAdapter(lotCompositeMasterAdaptor);


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

    class docJSONSaveMaterial extends AsyncTask<String, Integer, String> {

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
                    String TL2 = jsonObject.getString("message");

                    if (TL2.equals("Data no exist !")) {
                        AlertNotExist("This \"Material Code\" not exist. So can insert another \"Material Code\".");
                    } else if (TL2.equals("Data is mapping")) {
                        AlertNotExist("This \"Material Code\" has Used. So can insert another \"Material Code\".");
                    } else {
                        AlertNotExist(jsonObject.getString("message"));
                    }


                    //Toast.makeText(LotProductMappingActivity.this, TL2, Toast.LENGTH_SHORT).show();
                }else if(TL.equals("true")){
                    Toast.makeText(LotCompositeMappingActivity.this, "Done!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loaddata();
        }
    }
    private void AlertNotExist(String text) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
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
    class docJSONfinish extends AsyncTask<String, Integer, String> {

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
                    //Toast.makeText(LotCompositeMappingActivity.this, "Data server incorrect!!!", Toast.LENGTH_SHORT).show();
                    AlertNotExist("Data server incorrect!!!");
                } else if(TL.equals("true")){
                    Toast.makeText(LotCompositeMappingActivity.this, "Done!!!", Toast.LENGTH_SHORT).show();
                }else if(TL.equals("Dont")){
                   // Toast.makeText(LotCompositeMappingActivity.this, "Data don't exists!!!", Toast.LENGTH_SHORT).show();
                    AlertNotExist("Data don't exists!!!");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            Loaddata();
        }
    }
}

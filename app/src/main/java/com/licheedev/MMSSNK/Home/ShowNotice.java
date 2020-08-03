package com.licheedev.MMSSNK.Home;


import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.MainLayout;
import com.licheedev.MMSSNK.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

public class ShowNotice extends AppCompatActivity {

    TextView tvTitel,tvcontext,btnEdit;

    String kiemtra, userlogin;

    String title,contextx,Stt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_notice);
        // khoa tat man hinh+ them ham pha duoi

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setTitle("Notice");
        tvTitel = findViewById(R.id.tv_title);
        tvcontext = findViewById(R.id.tv_content);
        btnEdit = (TextView) findViewById(R.id.btnEdit);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        contextx = intent.getStringExtra("context");
        Stt =   intent.getStringExtra("Stt");
        SharedPreferences sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        userlogin = sharedPreferences.getString("TK","");
        tvTitel.setText(title);
        String tx = contextx.replace("\\n", "\n");
        tvcontext.setText(tx);
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditRow();
            }
        });

    }

    //khoa tat man hinh
    @Override
    protected void onDestroy() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
    @Override
    public void finish() {
        Intent intent = new Intent(ShowNotice.this, MainLayout.class);
        startActivity(intent);
    }

    private void EditRow() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poup_edit);
        dialog.setCanceledOnTouchOutside(false);

        //anh xa
        final EditText edtTitle     = (EditText) dialog.findViewById(R.id.edittextTitle);
        final EditText edtContext    = (EditText) dialog.findViewById(R.id.edittextContext);
        final RadioButton checkAll    = dialog.findViewById(R.id.checkall);
        final RadioButton checkMMS    = dialog.findViewById(R.id.checkmms);
        edtTitle.setText(title);
        String tx = contextx.replace("\\n", "\n");
        edtContext.setText(tx);


        String PIvisionChon = "ALL";
        switch (PIvisionChon){
            case "ALL":
                //   checkMMS.setChecked(false);
                checkMMS.setChecked(false);
                checkAll.setChecked(true);
                kiemtra = "A";
                break;
            case "MMS":
                checkMMS.setChecked(false);
                checkAll.setChecked(false);
                // checkMMS.setChecked(true);
                kiemtra = "M";
                break;
            default:
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
                break;
        }

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll.isChecked()){
                    // checkMMS.setChecked(false);
                    checkMMS.setChecked(false);
                    kiemtra = "A";
                } else {
                    //  checkMMS.setChecked(true);
                    checkMMS.setChecked(false);
                }
            }
        });

        checkMMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMMS.isChecked()){
                    checkAll.setChecked(false);
                    // checkMMS.setChecked(false);
                    kiemtra = "M";
                } else {
                    checkAll.setChecked(true);
                    //checkMMS.setChecked(false);
                }
            }
        });




        Button btnEdit   = (Button) dialog.findViewById(R.id.buttonEdit);
        Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);

        final String finalMno = Stt;

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Title = edtTitle.getText().toString();
                String Context = edtContext.getText().toString();
                String str = Context.replace("\n","\\n");
                //noticeArrayList.set( position, new Notice(finalMno, Title, Context, finalReg_id, finalReg_dt));
                //noticeAdapter.notifyDataSetChanged();

                switch (kiemtra) {
                    case "A":
                        String apiAdd = "http://snk.autonsi.com/product/get_notice_board_edit?mno=" + finalMno + "&title=" + Title + "&content=" + str + "&reg_id=" + userlogin + "&div_cd=A";
                        new PostNoticeJson().execute(apiAdd);
                        tvTitel.setText(Title);
                        String tx = str.replace("\\n", "\n");
                        tvcontext.setText(tx);
                        dialog.dismiss();
                        break;
                    case "M":
                        new PostNoticeJson().execute("http://snk.autonsi.com/product/get_notice_board_edit?mno=" + finalMno + "&title=" + Title + "&content=" + str + "&reg_id=" + userlogin + "&div_cd=M");
                        tvTitel.setText(Title);
                        String txx = str.replace("\\n", "\n");
                        tvcontext.setText(txx);
                        dialog.dismiss();
                        break;

                    default:
                        Toast.makeText(ShowNotice.this, "Error", Toast.LENGTH_SHORT).show();
                        break;
                }


                dialog.dismiss();

            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                dialog.cancel();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private class PostNoticeJson extends AsyncTask<String, Void, String> {
        StringBuilder context = new StringBuilder();
        @Override
        protected String doInBackground(String... strings) {
            try {
                URL url = new URL(strings[0]);
                InputStreamReader inputStreamReader = new InputStreamReader(url.openConnection().getInputStream());
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line="";
                while((line=bufferedReader.readLine())!=null){
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
                boolean result = object.getBoolean("result");
                if(result == true){
                    Toast.makeText(ShowNotice.this,"Completed", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(ShowNotice.this,"Error !!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}


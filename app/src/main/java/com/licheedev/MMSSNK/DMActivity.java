package com.licheedev.MMSSNK;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class DMActivity extends AppCompatActivity{

    TextView btnsignup,aaaaa;
    EditText edtuser;
    EditText edtpass;
    CheckBox check;
    SharedPreferences sharedPreferences; // lư thông tin check box
    String user;
    String pass;
    String chuoitrave;
    private long back;
    private Toast backToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dm);

        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        Anhxa();
        edtuser.setText(sharedPreferences.getString("TK",""));
        edtpass.setText(sharedPreferences.getString("MK",""));
        check.setChecked(sharedPreferences.getBoolean("checked", false));

        findViewById(R.id.logo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(DMActivity.this, "snk.autonsi.com", Toast.LENGTH_SHORT).show();
            }
        });


        //xulydangnhap();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                btnsignup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        user = edtuser.getText().toString();
                        pass = edtpass.getText().toString();
                        Log.d("Login","http://snk.autonsi.com/Product/" + "Login?" + "user=" + user + "&password=" + pass);
                        if(user.length()>0&&pass.length()>0){
                            //Toast.makeText(DMActivity.this, "Logging in to the system", Toast.LENGTH_SHORT).show();
                            new docJSON().execute("http://snk.autonsi.com/Product/" + "Login?" + "user=" + user + "&password=" + pass);/// gui du lieu len server
                        }else{
                            Toast.makeText(DMActivity.this, "Complete user and password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
    class docJSON extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return docNoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // Toast.makeText(getApplicationContext(),s,Toast.LENGTH_LONG).show();
            try {
                //JSONArray mangJSON = new JSONArray(s);
                JSONObject trave = new JSONObject(s);
                chuoitrave = trave.getString("result");
                if(chuoitrave == "true"){
                    if(check.isChecked()){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("TK",user);
                        editor.putString("MK",pass);
                        editor.putBoolean("checked", true);
                        editor.commit();
                    } else{
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.remove("TK");
                        editor.remove("MK");
                        editor.remove("checked");
                        editor.commit();
                    }

                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TK",user);
                    editor.commit();
                    Intent intent = new Intent(DMActivity.this, MainLayout.class);
                    startActivity(intent);
                } else {
                    Canhbaoloi("The User name or Password you entered were invalid.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
                Canhbaoloi(getString(R.string.sever_error));

            }
        }
    }

    @Override
    public void onBackPressed() {
        AppExit();
        //super.onBackPressed();
    }

    public void AppExit()
    {
        //this.finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }



    private String docNoiDung_Tu_URL(String theUrl){
        StringBuilder content = new StringBuilder();
        try    {
            // create a url object
            URL url = new URL(theUrl);

            // create a urlconnection object
            URLConnection urlConnection = url.openConnection();

            // wrap the urlconnection in a bufferedreader
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));

            String line;

            // read from the urlconnection via the bufferedreader
            while ((line = bufferedReader.readLine()) != null){
                content.append(line + "\n");
            }
            bufferedReader.close();
        }
        catch(Exception e)    {
            e.printStackTrace();
        }
        return content.toString();
    }
    private void Anhxa() {
        btnsignup   =  findViewById(R.id.btnsignup);
        edtuser     =  findViewById(R.id.edtuser);
        edtpass     =  findViewById(R.id.edtpass);
        check       =  findViewById(R.id.check);
    }
    private void Canhbaoloi(String text) {
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


}

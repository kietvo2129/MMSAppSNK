package com.licheedev.MMSSNK.TotalInspection.TotalInspectionDetail.Thinkness;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import java.util.ArrayList;

public class ThinknessActivity extends AppCompatActivity {

    String Group_qty = TotalInspectionDetailActivity.Group_qty;
    String idPLno = TotalInspectionDetailActivity.idPLno;
    String PLNo = TotalInspectionDetailActivity.PLno;
    String DateNo = TotalInspectionDetailActivity.dateno;
    TextView tvPlNO, tvDate, tv_qc_group_qty;
    int numOk = 0, numDefecty = 0;

    ArrayList<QcCheckThinknessItem> qcCheckThinknessItems;
    QcCheckThinknessAdapter qcCheckThinknessAdapter;
    private RecyclerView listViewCheck;
    int maximum = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thinkness);
        setTitle("Thickness");
        tvPlNO = findViewById(R.id.tv_qc_pl_no);
        tvDate = findViewById(R.id.tv_qc_date);
        tvPlNO.setText(PLNo);
        tvDate.setText(DateNo);
        listViewCheck = findViewById(R.id.recycler_view_qc_check);
        tv_qc_group_qty = findViewById(R.id.tv_qc_group_qty);
        tv_qc_group_qty.setText(Group_qty);

        loaddata();


        //

    }

    private void loaddata() {
        new getData().execute("http://snk.autonsi.com/TotalInspection/list_data_thinkness?pno=" + idPLno);
        Log.d("getData", "http://snk.autonsi.com/TotalInspection/list_data_thinkness?pno=" + idPLno);
    }

    private class getData extends AsyncTask<String, Void, String> {
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
            qcCheckThinknessItems = new ArrayList<QcCheckThinknessItem>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String pino = jsonObject.getString("pino");
                    String check_qty = jsonObject.getString("check_qty");
                    String ok_qty = jsonObject.getString("ok_qty");
                    String defect_qty = jsonObject.getString("defect_qty");
                    String defect_rate = jsonObject.getString("defect_rate");

                    qcCheckThinknessItems.add(new QcCheckThinknessItem(pino, check_qty, ok_qty, defect_qty, defect_rate + " %"));

                }


            } catch (JSONException e) {
                e.printStackTrace();
                AlertNotExist("Data incorrect");
            }
            buildListView();
        }
    }

    private void buildListView() {
        qcCheckThinknessAdapter = new QcCheckThinknessAdapter(qcCheckThinknessItems);
        listViewCheck.setLayoutManager(new LinearLayoutManager(ThinknessActivity.this));
        listViewCheck.setHasFixedSize(true);
        listViewCheck.setAdapter(qcCheckThinknessAdapter);

    }

    private void CheckButtonPopup() {
        Rect displayRectangle = new Rect();
        // Window window = getActivity().getWindow();
        //   window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final AlertDialog.Builder builder = new AlertDialog.Builder(ThinknessActivity.this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);

        View dialogView = LayoutInflater.from(ThinknessActivity.this).inflate(R.layout.popup_check_qc_thickness_check, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa
        final TextView tv_qcheck_mlno = dialogView.findViewById(R.id.tv_qcheck_mlno);
        TextView tv_qcheck_date = dialogView.findViewById(R.id.tv_qcheck_date);
        TextView tv_qc_group_qty = dialogView.findViewById(R.id.tv_qc_group_qty);
        tv_qcheck_date.setText(DateNo);
        tv_qcheck_mlno.setText(PLNo);
        tv_qc_group_qty.setText(Group_qty);

        numOk = 0;
        numDefecty = 0;

        ImageView imgButton_up1 = dialogView.findViewById(R.id.imgButton_up1);
        ImageView imgButton_down1 = dialogView.findViewById(R.id.imgButton_down1);
        ImageView imgButton_up2 = dialogView.findViewById(R.id.imgButton_up2);
        ImageView imgButton_down2 = dialogView.findViewById(R.id.imgButton_down2);
        TextView textView_food_1 = dialogView.findViewById(R.id.textView_food_1);
        TextView textView_food_2 = dialogView.findViewById(R.id.textView_food_2);
        TextView tv_qcheck_defectqty = dialogView.findViewById(R.id.tv_qcheck_defectqty);
        TextView btn_check_save_ck = dialogView.findViewById(R.id.btn_check_save_ck);


        textView_food_1.setText(numOk + "");
        textView_food_2.setText(numDefecty + "");
        tv_qcheck_defectqty.setText(numOk + numDefecty + "");

        ImageView button_close = dialogView.findViewById(R.id.img_close_check_check);
        button_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        imgButton_up1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((numOk + numDefecty) < Integer.parseInt(Group_qty)) {
                    textView_food_1.setError(null);
                    numOk += 1;
                    tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                    textView_food_1.setText(numOk + "");
                    textView_food_2.setText(numDefecty + "");
                } else {
                    tv_qcheck_defectqty.setError("");
                }
            }
        });
        imgButton_down1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_qcheck_defectqty.setError(null);
                numOk -= 1;

                if (numOk < 0) {
                    numOk = 0;
                    textView_food_1.setError("");
                } else {
                    textView_food_1.setError(null);
                }
                tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                textView_food_1.setText(numOk + "");
                textView_food_2.setText(numDefecty + "");
            }
        });
        imgButton_up2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((numOk + numDefecty) < Integer.parseInt(Group_qty)) {
                    textView_food_2.setError(null);
                    numDefecty += 1;
                    tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                    textView_food_1.setText(numOk + "");
                    textView_food_2.setText(numDefecty + "");
                } else {
                    tv_qcheck_defectqty.setError("");
                }
            }
        });
        imgButton_down2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_qcheck_defectqty.setError(null);
                numDefecty -= 1;

                if (numDefecty < 0) {
                    numDefecty = 0;
                    textView_food_2.setError("");
                } else {
                    textView_food_2.setError(null);
                }
                tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                textView_food_1.setText(numOk + "");
                textView_food_2.setText(numDefecty + "");
            }
        });

        textView_food_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_food_1.setError(null);
                View dialogView = LayoutInflater.from(ThinknessActivity.this).inflate(R.layout.number_input_layout_ok_cancel, null);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
                //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                final EditText input = dialogView.findViewById(R.id.input);
                Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
                Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);
                bt_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = input.getText().toString().trim();
                        maximum = Integer.parseInt(Group_qty) - numDefecty;
                        if (txt.length() > 0) {
                            if (maximum < Integer.parseInt(txt)) {
                                input.setError("Maximum = \"" + maximum + "\"");
                                input.requestFocus();
                                return;

                            } else {

                                numOk = Integer.parseInt(txt);
                                tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                                textView_food_1.setText(numOk + "");
                                textView_food_2.setText(numDefecty + "");
                                alertDialog.cancel();
                            }
                        } else {
                            input.setError("Please enter value");
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
        });

        textView_food_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView_food_1.setError(null);
                View dialogView = LayoutInflater.from(ThinknessActivity.this).inflate(R.layout.number_input_layout_ok_cancel, null);
                dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
                //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

                builder.setView(dialogView);
                final AlertDialog alertDialog = builder.create();
                alertDialog.setCanceledOnTouchOutside(false);
                final EditText input = dialogView.findViewById(R.id.input);
                Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
                Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);
                bt_OK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String txt = input.getText().toString().trim();
                        maximum = Integer.parseInt(Group_qty) - numOk;
                        if (txt.length() > 0) {
                            if (maximum < Integer.parseInt(txt)) {
                                input.setError("Maximum = \"" + maximum + "\"");
                                input.requestFocus();
                                return;

                            } else {

                                numDefecty = Integer.parseInt(txt);
                                tv_qcheck_defectqty.setText(numOk + numDefecty + "");
                                textView_food_1.setText(numOk + "");
                                textView_food_2.setText(numDefecty + "");
                                alertDialog.cancel();
                            }
                        } else {
                            input.setError("Please enter value");
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
        });


        btn_check_save_ck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (numOk == 0 && numDefecty ==0){

                }else {
                    new SaveData_Check().execute("http://snk.autonsi.com/TotalInspection/save_thickness_visual?pino=" + idPLno + "&ti_cd=" + "TI0001" + "&ok_qty=" + numOk + "&def_qty=" + numDefecty);
                    Log.d("SaveData_Check", "http://snk.autonsi.com/TotalInspection/save_thickness_visual?pino=" + idPLno + "&ti_cd=" + "TI0001" + "&ok_qty=" + numOk + "&def_qty=" + numDefecty);
                    alertDialog.dismiss();
                }

            }
        });


        alertDialog.show();
    }


    private class SaveData_Check extends AsyncTask<String, Void, String> {
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
                String trave = object.getString("result");

                if (trave.equals("true")) {
                    Toast.makeText(ThinknessActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    int tam = Integer.parseInt(Group_qty) - numDefecty;
                    Group_qty = tam + "";
                    tv_qc_group_qty.setText(Group_qty);
                    loaddata();
                } else {
                    AlertNotExist("Data incorrect");
                }


            } catch (JSONException e) {
                e.printStackTrace();
                AlertNotExist("Data incorrect");
            }
            buildListView();
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
                CheckButtonPopup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

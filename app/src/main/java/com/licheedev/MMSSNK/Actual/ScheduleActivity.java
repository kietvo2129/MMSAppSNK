package com.licheedev.MMSSNK.Actual;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.licheedev.MMSSNK.Home.HomeItem;
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
import java.util.ArrayList;
import java.util.Collections;

public class ScheduleActivity extends AppCompatActivity {

    private ArrayList<HomeItem> mHomeList;
    private ScheduleAdapter mAdapter;
    String WOchon, Linechon, processnamechon, process_nochon;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);
        setTitle("Schedule");
        mRecyclerView = findViewById(R.id.recyclerView);
        WOchon = ActualFragment.WOchon;
        Linechon = ActualFragment.Linechon;
        processnamechon = ActualFragment.processnamechon;
        process_nochon = ActualFragment.process_nochon;

        dialog = new ProgressDialog(ScheduleActivity.this);

        getData();
        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getData();
                pullToRefresh.setRefreshing(false);
            }
        });


    }

    private void getData() {
        new ReadJSON().execute("http://snk.autonsi.com/product/get_list_day_schedule?fo_no=" +
                WOchon +
                "&line_no=" +
                Linechon +
                "&process_no=" +
                process_nochon +
                "&process_nm=" +
                processnamechon);
        Log.d("ReadJSON", "http://snk.autonsi.com/product/get_list_day_schedule?fo_no=" +
                WOchon +
                "&line_no=" +
                Linechon +
                "&process_no=" +
                process_nochon +
                "&process_nm=" +
                processnamechon);
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
            mHomeList = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray jsonArray = object.getJSONArray("result");

                for (int i = 0; i < jsonArray.length(); i++) {

                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    String olddno = objectRow.getString("olddno");
                    String work_ymd = objectRow.getString("work_ymd");
                    work_ymd = work_ymd.substring(0, 4) + "-" + work_ymd.substring(4, 6) + "-" + work_ymd.substring(6, 8);
                    String prod_qty = objectRow.getString("prod_qty");
                    if (prod_qty != "null") {
                        prod_qty = formatter.format(Integer.parseInt(prod_qty));
                    } else {
                        prod_qty = "0";
                    }

                    String prounit_nm = objectRow.getString("prounit_nm");
                    mHomeList.add(new HomeItem(work_ymd, prod_qty, prounit_nm, olddno));
                }

                buildRecyclerView();

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

    }

    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ScheduleAdapter(mHomeList);

        Collections.sort(mHomeList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new ScheduleAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                intext(position);
            }
        });

    }

    private void intext(int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
        builder.setTitle("Schedule new: ");
        View viewInflated = LayoutInflater.from(ScheduleActivity.this).inflate(R.layout.number_input_layout, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                new JsonUpdate().execute("http://snk.autonsi.com/product/Update_Schedule_qty?olddno="
                        + mHomeList.get(position).getHomemno() +
                        "&schedule=" +
                        input.getText().toString().trim());
                Log.d("JsonUpdate", "http://snk.autonsi.com/product/Update_Schedule_qty?olddno="
                        + mHomeList.get(position).getHomemno() +
                        "&schedule=" +
                        input.getText().toString().trim());

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

    private class JsonUpdate extends AsyncTask<String, Void, String> {
        StringBuilder context = new StringBuilder();
        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

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
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            return context.toString();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                Boolean trave = object.getBoolean("result");
                if (trave){
                    Toast.makeText(ScheduleActivity.this, "Done", Toast.LENGTH_SHORT).show();
                    getData();
                }else {
                    AlertNotExist(object.getString("message"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

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
}

package com.licheedev.MMSSNK.Composite;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.licheedev.MMSSNK.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;


public class CompositeFragment extends Fragment {
    Spinner spinnerWO, spinnerRounting, spinnerPro, spinnerDate;
    final ArrayList<String> arrayLineWO = new ArrayList<String>();
    final ArrayList<String> arrayLineRouting = new ArrayList<String>();
    final ArrayList<String> arrayLineFactory = new ArrayList<String>();
    final ArrayList<String> arrayLineDateNM = new ArrayList<String>();
    String WOchon, ProcessChon, Datechon, GroupQtynum, QRQtynum;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_composite, container, false);
        spinnerWO = view.findViewById(R.id.spinnerWO);
        spinnerRounting = view.findViewById(R.id.spinnerRounting);
        spinnerPro = view.findViewById(R.id.spinnerPro);
        spinnerDate = view.findViewById(R.id.spinnerDate);

        new docJSONwo().execute("http://snk.autonsi.com/Lot/selec_wo");

        return view;
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

                new docJSONline().execute("http://snk.autonsi.com/product/return_line_pro?wo=" + WOchon);

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
            arrayLineFactory.removeAll(arrayLineFactory);
            arrayLineDateNM.removeAll(arrayLineDateNM);
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONArray array = jsonObject.getJSONArray("result");
                JSONObject jsonObject1 = array.getJSONObject(0);
                String line_no = jsonObject1.getString("line_no");
                String lct_nm = jsonObject1.getString("lct_nm");
                JSONArray work_dt = jsonObject1.getJSONArray("work_dt");
                arrayLineRouting.add(line_no);
                arrayLineFactory.add(lct_nm);
                for (int i =0; i<work_dt.length();i++){
                    arrayLineDateNM.add(work_dt.getString(i));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            Toast.makeText(getActivity(), "" + arrayLineRouting.get(0)+
            arrayLineFactory.get(0) + arrayLineDateNM.get(1), Toast.LENGTH_SHORT).show();

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

}

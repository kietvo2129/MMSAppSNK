package com.licheedev.MMSSNK.Status;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SearchView;
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
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatusFragment extends Fragment implements SearchView.OnQueryTextListener{


    RecyclerView RecyclerViewList;
    ArrayList<StatusMaster> statusMasters;
    ArrayList<StatusMaster> statusMastersnew;
    private SearchView searchView;

    StatusMasterAdapter statusMasterAdapter;
    Spinner spStatus;
    TextView tv_status;
    ArrayAdapter adapterStatus;
    ArrayList<String> StatusList, StatusListid;
    String CodeSp;
    int vitrispchon = 0;
    private ProgressDialog dialog;
    TextView NoPlan,Stop,Repair,ChangeOver,Working,all;
    EditText search_type,search_factory;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_status, container, false);
        NoPlan = view.findViewById(R.id.NoPlan);
        Stop = view.findViewById(R.id.Stop);
        Repair = view.findViewById(R.id.Repair);
        ChangeOver = view.findViewById(R.id.ChangeOver);
        Working = view.findViewById(R.id.Working);
        all = view.findViewById(R.id.all);
        search_type = view.findViewById(R.id.search_type);
        search_factory = view.findViewById(R.id.search_factory);

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                LoadData();
                pullToRefresh.setRefreshing(false);
            }
        });


        search_type.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());

            }
        });

        search_factory.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter_factory(s.toString());

            }
        });

        NoPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_tv("#a6a6a6");
            }
        });
        Stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_tv("#d55051");
            }
        });
        Repair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_tv("#E5D85C");
            }
        });
        ChangeOver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_tv("#4374D9");
            }
        });
        Working.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter_tv("#67952b");
            }
        });

        all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filter("");
            }
        });



        RecyclerViewList = view.findViewById(R.id.RecyclerViewList);
        dialog = new ProgressDialog(getContext());

        LoadData();
        return view;
    }

    private void LoadData() {
        new ReadJSON().execute("http://snk.autonsi.com/DashBoardQC/Get_DataFactoryStatus_API");
        Log.d("Read data", "http://snk.autonsi.com/DashBoardQC/Get_DataFactoryStatus_API");

    }

    private class ReadJSON extends AsyncTask<String, Void, String> {


        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

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
            statusMasters = new ArrayList<>();
            statusMastersnew = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("result")) {
                    AlertNotExist("No data!");
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("qc_itemcheck_mt");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    //  DecimalFormat formatter = new DecimalFormat("#,###,###");
//                    if (target_qty != "null") {
//                        target_qtynew = formatter.format(Integer.parseInt(target_qty));
//                    } else {
//                        target_qtynew = "0";
//                    }
                    String line_no = objectRow.getString("line_no").replace("null","");
                    String prod_qty = objectRow.getString("prod_qty").replace("null","");
                    String done_qty = objectRow.getString("done_qty").replace("null","");
                    String process_no = objectRow.getString("process_no").replace("null","");
                    String prounit_cd = objectRow.getString("prounit_cd").replace("null","");
                    String product_code = objectRow.getString("product_code").replace("null","");
                    String compost_cd = objectRow.getString("compost_cd").replace("null","");
                    String color = objectRow.getString("color").replace("null","");
                    String process_sts_cd = objectRow.getString("process_sts_cd").replace("null","");
                    statusMasters.add(new StatusMaster(line_no, prod_qty, done_qty, process_no, prounit_cd, product_code, compost_cd, color, process_sts_cd));
                    statusMastersnew = statusMasters;

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            setAdapter();
        }
    }

    private void setAdapter() {
        statusMasterAdapter = new StatusMasterAdapter(statusMasters);

        RecyclerViewList.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        RecyclerViewList.setAdapter(statusMasterAdapter);

        statusMasterAdapter.setOnItemClickListener(new StatusMasterAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onItemCheck(int position) {

                // Toast.makeText(getContext(), "" + statusMasters.get(position).getProunit_cd() + statusMasters.get(position).getProcess_sts_cd(), Toast.LENGTH_SHORT).show();

                getpopup(position);
            }
        });

    }

    private void getpopup(int position) {

        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_status_check);
        spStatus = dialog.findViewById(R.id.sp_Status);
        tv_status = dialog.findViewById(R.id.tv_status);
        TextView tv_ProcessUnit = dialog.findViewById(R.id.tv_ProcessUnit);
        tv_ProcessUnit.setText(statusMasters.get(position).getProunit_cd());
        CodeSp = statusMasters.get(position).getProcess_sts_cd();
        final boolean[] KtspSlick = {false};
        final boolean[] KtspSave = {false};

        spStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!KtspSlick[0]) {
                    KtspSlick[0] = true;
                } else {
                    tv_status.setText(StatusList.get(i));
                    vitrispchon = i;
                    KtspSave[0] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        dialog.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_check_save_ck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!KtspSave[0]) {
                    dialog.dismiss();
                } else {


                    new readJsonSave().execute("http://snk.autonsi.com/DashBoardQC/UpdateStatus_API?prounit_cd=" + statusMasters.get(position).getProunit_cd() +
                            "&process_sts_cd=" +
                            StatusListid.get(vitrispchon));

                    Log.d("readJsonSave", "http://snk.autonsi.com/DashBoardQC/UpdateStatus_API?prounit_cd=" + statusMasters.get(position).getProunit_cd() +
                            "&process_sts_cd=" +
                            StatusListid.get(vitrispchon));
                    dialog.dismiss();
                }


            }
        });

        new getDataSpiner().execute("http://snk.autonsi.com/DashBoardQC/Getstatus_API");
        Log.d("dataSpinner", "http://snk.autonsi.com/DashBoardQC/Getstatus_API");
        dialog.show();
    }

    private class readJsonSave extends AsyncTask<String, Void, String> {
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
            try {
                JSONObject object = new JSONObject(s);
                String Trave = object.getString("result");
                if (Trave.equals("false")) {
                    AlertNotExist("Data incorrect.");
                } else {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    LoadData();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                AlertNotExist("Sever error!");
            }

        }
    }


    private class getDataSpiner extends AsyncTask<String, Void, String> {
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
            StatusList = new ArrayList<>();
            StatusListid = new ArrayList<>();
            StatusList.add("*Status*");
            StatusListid.add("");
            try {
                JSONObject object = new JSONObject(s);
                if (!object.getBoolean("result")) {
                    AlertNotExist("No data!");
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("ds");

                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    String dt_cd = objectRow.getString("dt_cd");
                    String dt_nm = objectRow.getString("dt_nm");
                    StatusList.add(dt_nm);
                    StatusListid.add(dt_cd);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            adapterStatus = new ArrayAdapter(getActivity(), R.layout.spinner_item_nocolor, StatusList);
            adapterStatus.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spStatus.setAdapter(adapterStatus);
            XulySp();
        }
    }

    private void XulySp() {
        if (CodeSp.equals("") || CodeSp.equals("null")) {
            tv_status.setText(StatusList.get(0));
        } else {
            for (int i = 1; i < StatusList.size(); i++) {
                if (CodeSp.equals(StatusListid.get(i))) {
                    tv_status.setText(StatusList.get(i));
                    Log.e("asssaas", StatusList.get(i));
                    spStatus.setSelection(i);
                    return;
                }
            }

        }


    }


    private void AlertNotExist(String text) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
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


    // menu
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }




    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // TODO Add your menu entries here


//        inflater.inflate(R.menu.menu_search, menu);
//        searchView = (SearchView) menu.findItem(R.id.action_search)
//                .getActionView(); // set the reference to the searchView
//        searchView.setOnQueryTextListener(this);
//        MenuItem searchMenuItem = (MenuItem) menu.findItem(R.id.action_search);
//        searchMenuItem.expandActionView(); // expand the search action item automatically
//        searchView.setQueryHint("Search Code"); // fill in the search term by default
//        searchView.clearFocus(); // close the keyboard on load
//        super.onCreateOptionsMenu(menu, inflater);
    }


    public boolean onQueryTextSubmit(String query) {

        // This method can be used when a query is submitted eg. creating search history using SQLite DB

        filter(query);
        searchView.clearFocus();
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {

        filter(newText);

        return true;
    }

    private void filter(String text) {
        ArrayList<StatusMaster> filteredList = new ArrayList<>();


        for (StatusMaster item : statusMastersnew) {
            if (item.getProunit_cd().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        statusMasters = filteredList;
        statusMasterAdapter.filterList(filteredList);
    }

    private void filter_factory(String text) {
        ArrayList<StatusMaster> filteredList = new ArrayList<>();


        for (StatusMaster item : statusMastersnew) {
            if (item.getCompost_cd().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        statusMasters = filteredList;
        statusMasterAdapter.filterList(filteredList);
    }

    private void filter_tv(String text) {
        ArrayList<StatusMaster> filteredList = new ArrayList<>();


        for (StatusMaster item : statusMastersnew) {
            if (item.getColor().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        statusMasters = filteredList;
        statusMasterAdapter.filterList(filteredList);
    }

}

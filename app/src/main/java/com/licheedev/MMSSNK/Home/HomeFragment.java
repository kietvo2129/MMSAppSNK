package com.licheedev.MMSSNK.Home;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.github.ybq.android.spinkit.style.Circle;
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


public class HomeFragment extends Fragment {
    private ArrayList<HomeItem> mHomeList;
    private ArrayList<HomeItem> mHomeListnew;

    private Handler mHandler = new Handler();

    private RecyclerView mRecyclerView;
    private HomeAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    String ApiGet= "http://snk.autonsi.com/product/get_notice_board";
    FloatingActionButton fab;
    String kiemtra;
    EditText editText;
    String userlogin = null;
    ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        fab = view.findViewById(R.id.fab);
        progressBar = view.findViewById(R.id.Spinkit);
        mRecyclerView = view.findViewById(R.id.recyclerView);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        userlogin = sharedPreferences.getString("TK","");

        final SwipeRefreshLayout pullToRefresh = view.findViewById(R.id.pullToRefresh);
        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                xulyPIvision();
                pullToRefresh.setRefreshing(false);
            }
        });
        xulyPIvision();

        editText = view.findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddRow();
            }
        });
        return view;
    }

    private void AddRow() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.poup_add);
        dialog.setCanceledOnTouchOutside(false);

        //anh xa
        final EditText edtTitle     = (EditText) dialog.findViewById(R.id.edittextTitle);
        final EditText edtContext    = (EditText) dialog.findViewById(R.id.edittextContext);
        final RadioButton checkAll    = dialog.findViewById(R.id.checkall_create);
        final RadioButton checkMMS    = dialog.findViewById(R.id.checkmms_create);

        checkMMS.setChecked(false);
        checkAll.setChecked(true);
        kiemtra = "A";

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll.isChecked()){
                    //checkMMS.setChecked(false);
                    checkMMS.setChecked(false);
                    kiemtra = "A";
                } else {
                    //checkMMS.setChecked(true);
                    checkMMS.setChecked(false);
                }
            }
        });
        checkMMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkMMS.isChecked()){
                    checkAll.setChecked(false);
                    //checkMMS.setChecked(false);
                    kiemtra = "M";
                } else {
                    checkAll.setChecked(true);
                    //checkMMS.setChecked(false);
                }
            }
        });




        Button btnEdit   = (Button) dialog.findViewById(R.id.buttonEdit);
        Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String Title = edtTitle.getText().toString();
                String Context = edtContext.getText().toString();
                String str = Context.replace("\n","\\n");

                //Log.d("trieu ", Context) ;
                //Log.d("trieu ", str) ;
                if(Title.length()>0 && Context.length()>0) {
                    switch (kiemtra) {
                        case "A":
                            String apiAdd = "http://snk.autonsi.com/product/get_notice_board_create?title=" + Title + "&content=" + str + "&reg_id=" + userlogin + "&div_cd=A";
                            new PostNoticeJson().execute(apiAdd);
                            xulyPIvision();
                            dialog.dismiss();
                            break;
                        case "M":
                            new PostNoticeJson().execute("http://snk.autonsi.com/product/get_notice_board_create?title=" + Title + "&content=" + str + "&reg_id=" + userlogin + "&div_cd=M");
                            xulyPIvision();
                            dialog.dismiss();
                            break;

                        default:
                            Toast.makeText(getActivity(), "Error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                } else {
                    Toast.makeText(getActivity(), "Please enter content", Toast.LENGTH_SHORT).show();
                }
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
                    Toast.makeText(getActivity(),"Completed", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getActivity(),"Error!!!", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
    }

    private void filter(String text) {
        ArrayList<HomeItem> filteredList = new ArrayList<>();


        for (HomeItem item : mHomeListnew) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }

        mHomeList = filteredList;
        mAdapter.filterList(filteredList);
    }


    private void buildRecyclerView() {
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new HomeAdapter(mHomeList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                DeleteRow(viewHolder.getAdapterPosition());

            }
        }).attachToRecyclerView(mRecyclerView);



        mAdapter.setOnItemClickListener(new HomeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {

                Intent intent = new Intent(getActivity(),ShowNotice.class);
                intent.putExtra("title", mHomeList.get(position).getText1());
                intent.putExtra("context", mHomeList.get(position).getText2());
                intent.putExtra("Stt", mHomeList.get(position).getHomemno());
                startActivity(intent);
                //overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);


            }
        });

    }


    private final Runnable mDelay = new Runnable() {
        @Override
        public void run() {

            progressBar.setVisibility(View.GONE);
        }
    };


    private void xulyPIvision() {
        mHomeList = new ArrayList<HomeItem>();
        mHomeListnew = new ArrayList<HomeItem>();
        new ReadJSON().execute(ApiGet+"?div_cd=" + "A,M");
        Log.d("getNotice",ApiGet+"?div_cd=" + "A,M");
        //new ReadJSON().execute(ApiGet+"?div_cd=" + "M");
        progressBar.setVisibility(View.VISIBLE);
        Circle circle = new Circle();
        progressBar.setIndeterminateDrawable(circle);

    }

    private class ReadJSON extends AsyncTask<String, Void, String> {
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
                JSONArray jsonArray = object.getJSONArray("result");
                //int len =  jsonArray.length() - 1;
                for (int i = 0; i < jsonArray.length(); i++){

                    JSONObject objectRow = jsonArray.getJSONObject(i);
                    int imno = objectRow.getInt("mno");
                    String mno = String.format("%d", imno);
                    String title = objectRow.getString("title");
                    String content = objectRow.getString("content");
                    content = content.replace("\\n", "\n");
                    String reg_id = objectRow.getString("reg_id");
                    String reg_dt = objectRow.getString("reg_dt");

                    reg_dt = reg_dt.substring(0,10).trim();

                    String textnew = reg_id + " \n" + reg_dt;
                    mHomeList.add(new HomeItem(textnew,title,content,mno));
                    mHomeListnew = mHomeList;
                }

                buildRecyclerView();

            } catch (JSONException e) {
                e.printStackTrace();

            }
            mHandler.postDelayed(mDelay,1500);
        }

    }

    private void DeleteRow(final int position) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Confirm delete");
        String title = mHomeList.get(position).getText1();

        alertDialog.setMessage("Are you sure you want delete Notice: \n \"" + title + "\"");

        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                xulyPIvision();
            }
        });

        final String finalMno = mHomeList.get(position).getHomemno();
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String apiDelete = "http://snk.autonsi.com/product/get_notice_board_delete?mno=" + finalMno;
                new PostNoticeJson().execute(apiDelete);
                xulyPIvision();
                editText.setText("");
            }
        });
        alertDialog.show();
    }

}

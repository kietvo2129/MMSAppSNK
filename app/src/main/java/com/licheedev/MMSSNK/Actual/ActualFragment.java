package com.licheedev.MMSSNK.Actual;

import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SwitchCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants;
import com.licheedev.MMSSNK.Actual.Staff.StaffMaster;
import com.licheedev.MMSSNK.Actual.Staff.StaffMasterAdapter;
import com.licheedev.MMSSNK.Actual.Staff.VitriLamviecMaster;
import com.licheedev.MMSSNK.Actual.Staff.VitrilamviecAdapter;
import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.comn.SerialPortManager;
import com.licheedev.MMSSNK.comn.SerialReadThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import java.util.UUID;


public class ActualFragment extends Fragment {

    public static String numActual = "0",Productcode = "";

    TextView keyin, scan,spinnerProUnit;
    Spinner spinnerWO, spinnerLine, spinnerPro;
    String line;
    public static String WOchon, Linechon, processnamechon, process_nochon, line_nochon, olddnochon,datechon,Ma_QC_Code;
    CheckBox checkboxActual, checkboxDefective;
    ImageView logout;
    TextView option, tollHome, toolwo;
    final ArrayList<String> arrayLineWO = new ArrayList<String>();
    final ArrayList<String> arrayLineWO_view = new ArrayList<String>();
    final ArrayList<String> arrayLineLine = new ArrayList<String>();
    final ArrayList<String> arrayLinePro = new ArrayList<String>();
    final ArrayList<String> arrayLineNO = new ArrayList<String>();
    final ArrayList<String> arrayLineLever = new ArrayList<String>();

    final ArrayList<String> arrayLineolddno = new ArrayList<String>();


    final ArrayList<String> arrayLineprocess_no = new ArrayList<String>();
    final ArrayList<String> arrayLineline_no = new ArrayList<String>();

    public static String numQCcheck = "0";

    //final ArrayList<String> arrayLinepopup = new ArrayList<String>();
    String chuoitrave;
    SharedPreferences sharedPreferences;

    private ArrayList<countrydanhsach> mcountrydanhsach;
    private countrydanhsachAdapter mAdapter;
    ArrayList<Integer> arrayActual = new ArrayList<>();
    ArrayList<Integer> arrayDefective = new ArrayList<>();
    TextView nameLogin;

    ArrayList<String> arrayProCode;

    FragmentKeyin fragmentKeyin;
    FragmentScan fragmentScan;
    int kiemtra = 0;
    String  prcunitchon;
    public static String level = "";


    private TextView mBluetoothStatus;
    private TextView mReadBuffer;
    private Button mScanBtn;
    private Button mOffBtn;
    private Button mListPairedDevicesBtn;
    private Button mDiscoverBtn;
    private BluetoothAdapter mBTAdapter;
    private Set<BluetoothDevice> mPairedDevices;
    private ArrayAdapter<String> mBTArrayAdapter;
    private ListView mDevicesListView;

    TextView huy,Schedule;

    private Handler mHandler; // Our main handler that will receive callback notifications
    private ConnectedThread mConnectedThread; // bluetooth background worker thread to send and receive data
    private BluetoothSocket mBTSocket = null; // bi-directional client-to-client data path

    private static final UUID BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB"); // "random" unique identifier


    // #defines for identifying shared types between calling functions
    private final static int REQUEST_ENABLE_BT = 1; // used to identify adding bluetooth names
    private final static int MESSAGE_READ = 2; // used in bluetooth handler to identify message update
    private final static int CONNECTING_STATUS = 3; // used in bluetooth handler to identify message status

    String userlogin;
    Spinner spinnerdanhsach;
    public static String bom_no, processchon, index, style_no;

    ArrayList<StaffMaster> staffMasters;
    StaffMasterAdapter staffMasterAdapter;
    ListView lvStaff;
    ListView lvStaffAll;
    //staff all
    ArrayList<StaffMaster> staffAllMasters;
    StaffMasterAdapter staffAllMasterAdapter;
    ArrayList<StaffMaster> staffAllMastersnew;

    ArrayList<VitriLamviecMaster> vitriLamviecMasters;
    VitrilamviecAdapter vitrilamviecAdapter;

    int vitrilamviec = -1;
    Dialog popup_add_vt_lv;
    Dialog popup_add_staff;
    ListView lvMCall;


    ListView lvMachine;
    ListView lvMold;
    ArrayList<StaffMaster> machineMasters;
    StaffMasterAdapter machineMasterAdapter;
    Dialog popup_add_MC;
    int vitriMC = -1;
    ArrayList<StaffMaster> MCAllMasters;
    StaffMasterAdapter MCAllMasterAdapter;
    ArrayList<StaffMaster> MCAllMastersnew;

    ArrayList<StaffMaster> moldMasters;
    StaffMasterAdapter moldMasterAdapter;
    Dialog popup_add_Mold;
    ListView lvMoldall;
    int vitriMold = -1;
    ArrayList<StaffMaster> MoldAllMasters;
    StaffMasterAdapter MoldAllMasterAdapter;
    ArrayList<StaffMaster> MoldAllMastersnew;

    TextView weight_current,num_current;
    TextView qty_standa,weight_standa,weight1_standa;
    int numQty_standa=1;
    double numWeight=3.33, numWeight1=numWeight/numQty_standa;
    int KTvonglap = -1;
    int numActualweight;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_actual, container, false);


        mcountrydanhsach = new ArrayList<>();
        spinnerdanhsach = view.findViewById(R.id.spinnerdanhsach);
        TextView KTrafragment = getActivity().findViewById(R.id.KTrafragment);
        KTrafragment.setText("ActKeyIn");

        spinnerWO = view.findViewById(R.id.spinnerWO);
        spinnerLine = view.findViewById(R.id.spinnerLine);
        spinnerPro = view.findViewById(R.id.spinnerPro);
        option = view.findViewById(R.id.option);
        keyin = view.findViewById(R.id.keyin);
        scan = view.findViewById(R.id.scan);
        Schedule = view.findViewById(R.id.Schedule);
        spinnerProUnit = view.findViewById(R.id.spinnerProUnit);

        DuLieuWO();
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("datalogin", Context.MODE_PRIVATE);
        userlogin = sharedPreferences.getString("TK", "");


        FragmentManager fragmentManager = getFragmentManager(); // da sua
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentKeyin = new FragmentKeyin();
        fragmentTransaction.replace(R.id.framContent, fragmentKeyin);
        fragmentTransaction.commit();


        Schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!level.equals("")) {
                    Intent intent = new Intent(getActivity(), ScheduleActivity.class);
                    startActivity(intent);
                }
            }
        });


        keyin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtra = 0;
                // keyin.setTextColor(Color.parseColor("#776D6D"));
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentKeyin = new FragmentKeyin();

                fragmentTransaction.replace(R.id.framContent, fragmentKeyin);
                fragmentTransaction.commit();
                //DuLieuWO();
                new docJSONdata2().execute("http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);
            }
        });

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                kiemtra = 1;
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentScan = new FragmentScan();
                fragmentTransaction.replace(R.id.framContent, fragmentScan);
                fragmentTransaction.commit();
                //DuLieuWO();
                new docJSONdata2().execute("http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);

            }
        });

        option.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog = new Dialog(getActivity());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.dialog_caidatscan);
                dialog.show();


                ////////////////////

                huy = (TextView) dialog.findViewById(R.id.huy);
                mBluetoothStatus = (TextView) dialog.findViewById(R.id.bluetoothStatus);
                mReadBuffer = (TextView) dialog.findViewById(R.id.readBuffer);
                mScanBtn = (Button) dialog.findViewById(R.id.scan);
                mOffBtn = (Button) dialog.findViewById(R.id.off);
                mDiscoverBtn = (Button) dialog.findViewById(R.id.discover);
                mListPairedDevicesBtn = (Button) dialog.findViewById(R.id.PairedBtn);
                //mLED1 = (CheckBox)dialog.findViewById(R.id.checkboxLED1);
                mBTArrayAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1);
                mBTAdapter = BluetoothAdapter.getDefaultAdapter(); // get a handle on the bluetooth radio
                mDevicesListView = (ListView) dialog.findViewById(R.id.devicesListView);
                mDevicesListView.setAdapter(mBTArrayAdapter); // assign model to view
                mDevicesListView.setOnItemClickListener(mDeviceClickListener);

                checkboxActual = dialog.findViewById(R.id.checkboxActual);
                checkboxDefective = dialog.findViewById(R.id.checkboxDefective);


                checkboxActual.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (checkboxActual.isChecked()) {
                            checkboxDefective.setChecked(false);
                        } else {
                            checkboxDefective.setChecked(true);
                        }

                    }
                });
                checkboxDefective.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (checkboxDefective.isChecked()) {
                            checkboxActual.setChecked(false);
                        } else {
                            checkboxActual.setChecked(true);
                        }

                    }
                });


                mHandler = new Handler() {
                    public void handleMessage(android.os.Message msg) {

                        String tam = "";
                        if (msg.what == MESSAGE_READ) {
                            String readMessage = null;
                            try {
                                readMessage = new String((byte[]) msg.obj, "UTF-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            //Log.d("SSSS",readMessage);
                            tam = readMessage.substring(1, 5).trim();
                            if (tam.equals("B0fB")) {
                                int in = readMessage.indexOf("\n", 7);
                                mReadBuffer.setText(readMessage.substring(8, in).trim());
                                if (kiemtra == 1) {

                                    if (checkboxActual.isChecked()) {
                                        fragmentScan.scandefective.setText("");
                                        fragmentScan.scanactual.setText(readMessage.substring(8, in).trim());
                                        String numActualString = fragmentScan.tvActualNum.getText().toString();
                                        int numActual = Integer.parseInt(numActualString);
                                        numActual = numActual + 1;
                                        fragmentScan.tvActualNum.setText(numActual + "");
                                        fragmentScan.scanactual.setText(readMessage.substring(8, in).trim());
                                        new docJSONbarcode().execute("http://snk.autonsi.com/product/post_barcode_m_order_facline_dayhist?fo_no=" +
                                                WOchon +
                                                "&line_no=" +
                                                Linechon +
                                                "&process_no=" +
                                                processchon +
                                                "&prod_sts_cd=" +
                                                "A" +
                                                "&barcode=" +
                                                readMessage.substring(8, in).trim() +
                                                "&done_qty=" +
                                                fragmentScan.tvActualNum.getText().toString() +
                                                "&refer_qty=" +
                                                fragmentScan.tvDefectiveNum.getText().toString()
                                                + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon);

                                    } else {
                                        fragmentScan.scanactual.setText("");
                                        fragmentScan.scandefective.setText(readMessage.substring(8, in).trim());
                                        String numDefectString = fragmentScan.tvDefectiveNum.getText().toString();
                                        int numDefect = Integer.parseInt(numDefectString);
                                        numDefect = numDefect + 1;
                                        fragmentScan.tvDefectiveNum.setText(numDefect + "");
                                        fragmentScan.scandefective.setText(readMessage.substring(8, in).trim());
                                        new docJSONbarcode().execute("http://snk.autonsi.com/product/post_barcode_m_order_facline_dayhist?fo_no=" +
                                                WOchon +
                                                "&line_no=" +
                                                Linechon +
                                                "&process_no=" +
                                                processchon +
                                                "&prod_sts_cd=" +
                                                "D" +
                                                "&barcode=" +
                                                readMessage.substring(8, in).trim() +
                                                "&done_qty=" +
                                                fragmentScan.tvActualNum.getText().toString() +
                                                "&refer_qty=" +
                                                fragmentScan.tvDefectiveNum.getText().toString()
                                                + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon);
                                    }
                                }


                            }
                        }


                        if (msg.what == CONNECTING_STATUS) {
                            if (msg.arg1 == 1)
                                mBluetoothStatus.setText("Connected to Device: " + (String) (msg.obj));
                            else
                                mBluetoothStatus.setText("Connection Failed");
                        }
                    }
                };


                huy.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                if (mBTArrayAdapter == null) {
                    // Device does not support Bluetooth
                    mBluetoothStatus.setText("Status: Bluetooth not found");
                    Toast.makeText(getContext(), "Bluetooth device not found!", Toast.LENGTH_SHORT).show();
                } else {


                    mScanBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bluetoothOn(v);
                        }
                    });

                    mOffBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            bluetoothOff(v);
                        }
                    });

                    mListPairedDevicesBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            listPairedDevices(v);
                        }
                    });

                    mDiscoverBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            discover(v);
                        }
                    });
                }


            }
        });

        return view;
    }


    private void initList() {

        mAdapter = new countrydanhsachAdapter(getActivity(), mcountrydanhsach);
        spinnerdanhsach.setAdapter(mAdapter);

        spinnerdanhsach.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                new docJSONdataAgain().execute("http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);
                Log.d("docJSONdataAgain", "http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);



                processnamechon = arrayLinePro.get(position);
                level = arrayLineLever.get(position);
                olddnochon = arrayLineolddno.get(position);
                process_nochon = arrayLineprocess_no.get(position);
                line_nochon = arrayLineline_no.get(position);
                //  Toast.makeText(getActivity(), ""+processnamechon, Toast.LENGTH_SHORT).show();

                countrydanhsach clickedItem = (countrydanhsach) parent.getItemAtPosition(position);
                String clickedcountrydanhsach = clickedItem.getdate();
                //Toast.makeText(Actual.this, ""+ clickedcountrydanhsach, Toast.LENGTH_SHORT).show();

                if (kiemtra == 1) {
                    fragmentScan.tvActualNum.setText(arrayActual.get(position) + "");
                    fragmentScan.tvDefectiveNum.setText(arrayDefective.get(position) + "");

                } else {
                    fragmentKeyin.tvActualNum.setText(arrayActual.get(position) + "");
                    fragmentKeyin.tvDefectiveNum.setText(arrayDefective.get(position) + "");
                }

                datechon = clickedItem.getdate();
                prcunitchon = clickedItem.getprcunit();

                Bundle bundle = new Bundle();
                bundle.putString("WOchon", WOchon);
                bundle.putString("Linechon", Linechon);
                bundle.putString("processchon", processchon);
                bundle.putString("datechon", datechon);
                bundle.putString("prcunitchon", prcunitchon);
                bundle.putString("level", level);
                bundle.putString("processnamechon", processnamechon);
                if (kiemtra == 1) {
                    fragmentScan.setArguments(bundle);
                } else {
                    fragmentKeyin.setArguments(bundle);
                }

                new getQC_code().execute("http://snk.autonsi.com/Lot/get_QC?level=" +
                        level +
                        "&process_no=" +
                        processchon +
                        "&line_no=" +
                        line_nochon +
                        "&style_no=" +
                        style_no);

                Log.d("getQC_code","http://snk.autonsi.com/Lot/get_QC?level=" +
                        level +
                        "&process_no=" +
                        processchon +
                        "&line_no=" +
                        line_nochon +
                        "&style_no=" +
                        style_no);



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void getDataQCcheck() {
        if (level.equals("last")){
            String url2 = "http://snk.autonsi.com/product/GetDataW_Product_Qc?item_vcd=" +
                    Ma_QC_Code +
                    "&olddno=" +
                    olddnochon;
            Log.d("OQC_getData", url2);
            new LoadQcList().execute(url2);
        }else {

            String url2 = "http://snk.autonsi.com/product/GetDataFaclineQc?item_vcd=" +
                    Ma_QC_Code +
                    "&olddno=" +
                    olddnochon;
            Log.d("PQC_getData", url2);
            new LoadQcList().execute(url2);
        }


    }
    class LoadQcList extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    AlertNotExist("QC Detail not exist !!!");
                    return;
                }
                JSONArray jsonArray = object.getJSONArray("result");
                JSONObject obj = jsonArray.getJSONObject(0);
                numQCcheck = obj.getString("check_qty").replace("null", "0");

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    class getQC_code extends AsyncTask<String, Integer, String> {

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
                JSONArray jsonArray = object.getJSONArray("result");
                if (jsonArray.length() == 0){
                    Ma_QC_Code = "";
                }
                Ma_QC_Code = jsonArray.getString(0);
                getDataQCcheck();
            } catch (JSONException e) {
                Ma_QC_Code = "";
                e.printStackTrace();
            }


        }
    }


    private void bluetoothOn(View view) {
        if (!mBTAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            mBluetoothStatus.setText("Bluetooth enabled");
            Toast.makeText(getContext(), "Bluetooth turned on", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Bluetooth is already on", Toast.LENGTH_SHORT).show();
        }
    }

    // Enter here after user selects "yes" or "no" to enabling radio
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent Data){
//        // Check which request we're responding to
//        if (requestCode == REQUEST_ENABLE_BT) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//                // The user picked a contact.
//                // The Intent's data Uri identifies which contact was selected.
//                mBluetoothStatus.setText("Enabled");
//            }
//            else
//                mBluetoothStatus.setText("Disabled");
//        }
//    }

    private void bluetoothOff(View view) {
        mBTAdapter.disable(); // turn off
        mBluetoothStatus.setText("Bluetooth disabled");
        Toast.makeText(getContext(), "Bluetooth turned Off", Toast.LENGTH_SHORT).show();
    }

    private void discover(View view) {
        // Check if the device is already discovering
        if (mBTAdapter.isDiscovering()) {
            mBTAdapter.cancelDiscovery();
            Toast.makeText(getContext(), "Discovery stopped", Toast.LENGTH_SHORT).show();
        } else {
            if (mBTAdapter.isEnabled()) {
                mBTArrayAdapter.clear(); // clear items
                mBTAdapter.startDiscovery();
                Toast.makeText(getContext(), "Discovery started", Toast.LENGTH_SHORT).show();
                //registerReceiver(blReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
            } else {
                Toast.makeText(getContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
            }
        }
    }

    final BroadcastReceiver blReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                // add the name to the list
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                mBTArrayAdapter.notifyDataSetChanged();
            }
        }
    };

    private void listPairedDevices(View view) {
        mPairedDevices = mBTAdapter.getBondedDevices();
        if (mBTAdapter.isEnabled()) {
            // put it's one to the adapter
            for (BluetoothDevice device : mPairedDevices)
                mBTArrayAdapter.add(device.getName() + "\n" + device.getAddress());

            Toast.makeText(getContext(), "Show Paired Devices", Toast.LENGTH_SHORT).show();
        } else
            Toast.makeText(getContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
    }

    private AdapterView.OnItemClickListener mDeviceClickListener = new AdapterView.OnItemClickListener() {
        public void onItemClick(AdapterView<?> av, View v, int arg2, long arg3) {

            if (!mBTAdapter.isEnabled()) {
                Toast.makeText(getContext(), "Bluetooth not on", Toast.LENGTH_SHORT).show();
                return;
            }

            mBluetoothStatus.setText("Connecting...");
            // Get the device MAC address, which is the last 17 chars in the View
            String info = ((TextView) v).getText().toString();
            final String address = info.substring(info.length() - 17);
            final String name = info.substring(0, info.length() - 17);

            // Spawn a new thread to avoid blocking the GUI one
            new Thread() {
                public void run() {
                    boolean fail = false;

                    BluetoothDevice device = mBTAdapter.getRemoteDevice(address);

                    try {
                        mBTSocket = createBluetoothSocket(device);
                    } catch (IOException e) {
                        fail = true;
                        Toast.makeText(getContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                    }
                    // Establish the Bluetooth socket connection.
                    try {
                        mBTSocket.connect();
                    } catch (IOException e) {
                        try {
                            fail = true;
                            mBTSocket.close();
                            mHandler.obtainMessage(CONNECTING_STATUS, -1, -1)
                                    .sendToTarget();
                        } catch (IOException e2) {
                            //insert code to deal with this
                            Toast.makeText(getContext(), "Socket creation failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                    if (fail == false) {
                        mConnectedThread = new ConnectedThread(mBTSocket);
                        mConnectedThread.start();

                        mHandler.obtainMessage(CONNECTING_STATUS, 1, -1, name)
                                .sendToTarget();
                    }
                }
            }.start();
        }
    };

    private BluetoothSocket createBluetoothSocket(BluetoothDevice device) throws IOException {
        return device.createRfcommSocketToServiceRecord(BTMODULEUUID);
        //creates secure outgoing connection with BT device using UUID
    }

    private class ConnectedThread extends Thread {
        private final BluetoothSocket mmSocket;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;

        public ConnectedThread(BluetoothSocket socket) {
            mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;

            // Get the input and output streams, using temp objects because
            // member streams are final
            try {
                tmpIn = socket.getInputStream();
                tmpOut = socket.getOutputStream();
            } catch (IOException e) {
            }

            mmInStream = tmpIn;
            mmOutStream = tmpOut;
        }

        public void run() {
            byte[] buffer = new byte[1024];  // buffer store for the stream
            int bytes; // bytes returned from read()
            // Keep listening to the InputStream until an exception occurs
            while (true) {
                try {
                    // Read from the InputStream
                    bytes = mmInStream.available();
                    if (bytes != 0) {
                        SystemClock.sleep(100); //pause and wait for rest of data. Adjust this depending on your sending speed.
                        bytes = mmInStream.available(); // how many bytes are ready to be read?
                        bytes = mmInStream.read(buffer, 0, bytes); // record how many bytes we actually read
                        mHandler.obtainMessage(MESSAGE_READ, bytes, -1, buffer)
                                .sendToTarget(); // Send the obtained bytes to the UI activity
                    }
                } catch (IOException e) {
                    e.printStackTrace();

                    break;
                }
            }
        }

        /* Call this from the main activity to send data to the remote device */
        public void write(String input) {
            byte[] bytes = input.getBytes();           //converts entered String into bytes
            try {
                mmOutStream.write(bytes);
            } catch (IOException e) {
            }
        }

        /* Call this from the main activity to shutdown the connection */
        public void cancel() {
            try {
                mmSocket.close();
            } catch (IOException e) {
            }
        }
    }

    ////////////////////////


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

    private void DuLieuWO() {

        new docJSONwo().execute("http://snk.autonsi.com/product/get_fo_no_m_order_facline_day");
        Log.d("docJSONwo", "http://snk.autonsi.com/product/get_fo_no_m_order_facline_day");


    }

    //
//
    class docJSONwo extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            arrayProCode = new ArrayList<>();
            arrayLineWO.removeAll(arrayLineWO);
            arrayLineWO_view.removeAll(arrayLineWO_view);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").replace("[", "").replace("]", "").equals("")||object.getString("result").equals("false")) {
                    AlertNotExist("\"WO\" does not exist today. Please, insert \"Plan Master\" for today.");
                    return;
                }
                JSONArray array = object.getJSONArray("data");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    String lineNO = object2.getString("fo_no");
                    //String lineNO = array.getString(i);
                    String ProCode = object2.getString("product_code");
                    arrayProCode.add(ProCode);
                    arrayLineWO.add(lineNO);
                    arrayLineWO_view.add(convert_view(lineNO));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LickspinnerWO();

        }
    }

    private String convert_view(String txt) {
        String string = "";
        if (txt.equals("null")) {

        } else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }

    class docJSONline extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                arrayLineLine.removeAll(arrayLineLine);
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    String lineNO = object2.getString("line_no");
                    //String lineNO = array.getString(i);
                    arrayLineLine.add(lineNO);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LickspinnerLine();

        }
    }

    class docJSONpro extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                //arrayLinePro.removeAll(arrayLinePro);
                arrayLineNO.removeAll(arrayLineNO);
                //JSONObject object = new JSONObject(s);
                //JSONArray array = object.getJSONArray("result");
                JSONArray array = new JSONArray(s);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    //String lineNO = object2.getString("process_nm");
                    //String lineNO = array.getString(i);
                    //arrayLinePro.add(lineNO);
                    arrayLineNO.add(object2.getString("process_no"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            LickspinnerPro();

        }
    }

    class docJSONdata extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //try {
            arrayLinePro.removeAll(arrayLinePro);
            arrayLineLever.removeAll(arrayLineLever);
            arrayLineprocess_no.removeAll(arrayLineprocess_no);
            arrayLineline_no.removeAll(arrayLineline_no);
            arrayLineolddno.removeAll(arrayLineolddno);

            try {
                mcountrydanhsach.removeAll(mcountrydanhsach);
                arrayActual.removeAll(arrayActual);
                arrayDefective.removeAll(arrayDefective);
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                }

                for (int i = 0; i < array.length(); i++) {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    JSONObject object2 = array.getJSONObject(i);
                    String date = object2.getString("work_ymd");
                    String dateview = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
                    String target = object2.getString("prod_qty");
                    if (target.equals("null")) {
                        target = "0";
                    }
                    target = formatter.format(Integer.parseInt(target));
                    String hour = object2.getString("work_time");
                    String prcunit = object2.getString("prounit_nm");
                    String process_nm = object2.getString("process_nm");
                    String process_no = object2.getString("process_no");
                    String line_no = object2.getString("line_no");
                    String level1 = object2.getString("level");
                    String olddno = object2.getString("olddno");


                    arrayLineolddno.add(olddno);
                    arrayLineLever.add(level1);
                    arrayLinePro.add(process_nm);
                    arrayLineprocess_no.add(process_no);
                    arrayLineline_no.add(line_no);
                    mcountrydanhsach.add(new countrydanhsach(dateview, date, target, hour, prcunit));
                    arrayActual.add(Integer.parseInt(object2.getString("done_qty")));
                    arrayDefective.add(Integer.parseInt(object2.getString("refer_qty")));
                }
                initList();


//                JSONObject loi = array.getJSONObject(0);
//                tvdate.setText(loi.getString("work_ymd"));
//                tvtarget.setText(loi.getString("prod_qty"));
//                tvhour.setText(loi.getString("work_time"));
//                tvprcUnit.setText(loi.getString("prounit_nm"));

                /////////////
//                if(kiemtra == 1){
//                    fragmentScan.tvActualNum.setText(loi.getString("done_qty"));
//                    fragmentScan.tvDefectiveNum.setText(loi.getString("refer_qty"));
//
//                }else {
//                    fragmentKeyin.tvActualNum.setText(loi.getString("done_qty"));
//                    fragmentKeyin.tvDefectiveNum.setText(loi.getString("refer_qty"));
//                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class docJSONdataAgain extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //try {
            arrayLinePro.removeAll(arrayLinePro);
            arrayLineLever.removeAll(arrayLineLever);
            arrayLineprocess_no.removeAll(arrayLineprocess_no);
            arrayLineline_no.removeAll(arrayLineline_no);
            arrayLineolddno.removeAll(arrayLineolddno);

            try {
                mcountrydanhsach.removeAll(mcountrydanhsach);
                arrayActual.removeAll(arrayActual);
                arrayDefective.removeAll(arrayDefective);
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                   // Toast.makeText(getActivity(), "data incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

                for (int i = 0; i < array.length(); i++) {
                    DecimalFormat formatter = new DecimalFormat("#,###,###");
                    JSONObject object2 = array.getJSONObject(i);
                    String date = object2.getString("work_ymd");
                    String dateview = date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
                    String target = object2.getString("prod_qty");
                    if (target.equals("null")) {
                        target = "0";
                    }
                    target = formatter.format(Integer.parseInt(target));
                    String hour = object2.getString("work_time");
                    String prcunit = object2.getString("prounit_nm");
                    String process_nm = object2.getString("process_nm");
                    String process_no = object2.getString("process_no");
                    String line_no = object2.getString("line_no");
                    String level1 = object2.getString("level");
                    String olddno = object2.getString("olddno");


                    arrayLineolddno.add(olddno);
                    arrayLineLever.add(level1);
                    arrayLinePro.add(process_nm);
                    arrayLineprocess_no.add(process_no);
                    arrayLineline_no.add(line_no);
                    mcountrydanhsach.add(new countrydanhsach(dateview, date, target, hour, prcunit));
                    arrayActual.add(Integer.parseInt(object2.getString("done_qty")));
                    arrayDefective.add(Integer.parseInt(object2.getString("refer_qty")));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    class docJSONdata2 extends AsyncTask<String, Integer, String> {

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
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object2 = array.getJSONObject(i);
                    String date = object2.getString("work_ymd");
                    String prcunit = object2.getString("prounit_nm");
                    if (date.equals(datechon)) {
                        if (prcunit.equals(prcunitchon)) {
                            if (kiemtra == 1) {
                                fragmentScan.tvActualNum.setText(object2.getString("done_qty") + "");
                                fragmentScan.tvDefectiveNum.setText(object2.getString("refer_qty") + "");
                                Bundle bundle = new Bundle();
                                bundle.putString("WOchon", WOchon);
                                bundle.putString("Linechon", Linechon);
                                bundle.putString("processchon", processchon);
                                bundle.putString("datechon", datechon);
                                bundle.putString("prcunitchon", prcunitchon);
                                bundle.putString("level", level);
                                bundle.putString("processnamechon", processnamechon);
                                fragmentScan.setArguments(bundle);

                            } else {
                                fragmentKeyin.tvActualNum.setText(object2.getString("done_qty") + "");
                                fragmentKeyin.tvDefectiveNum.setText(object2.getString("refer_qty") + "");
                                Bundle bundle = new Bundle();
                                bundle.putString("WOchon", WOchon);
                                bundle.putString("Linechon", Linechon);
                                bundle.putString("processchon", processchon);
                                bundle.putString("datechon", datechon);
                                bundle.putString("prcunitchon", prcunitchon);
                                bundle.putString("level", level);
                                bundle.putString("processnamechon", processnamechon);
                                fragmentKeyin.setArguments(bundle);
                            }
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    private void LickspinnerWO() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineWO_view);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerWO.setAdapter(arrayAdapter);
        spinnerWO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                line = arrayLineWO.get(position);

                Productcode = arrayProCode.get(position).toString();
                spinnerProUnit.setText(arrayProCode.get(position).toString());


                WOchon = line;
                new docJSONline().execute("http://snk.autonsi.com/product/get_line_no_m_order_facline_day?fo_no=" + WOchon);
                new docJSONdata_them().execute("http://snk.autonsi.com/Lot/return_mp_mt_line?wo=" + WOchon);
                Log.d("docJSONline", "http://snk.autonsi.com/product/get_line_no_m_order_facline_day?fo_no=" + WOchon);
                Log.d("docJSONdata_them", "http://snk.autonsi.com/Lot/return_mp_mt_line?wo=" + WOchon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    class docJSONdata_them extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                bom_no = jsonObject.getString("bom_no");
                index = jsonObject.getString("index");
                style_no = jsonObject.getString("style_no");

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void LickspinnerLine() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineLine);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerLine.setAdapter(arrayAdapter);
        spinnerLine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                line = arrayLineLine.get(position);
                Linechon = line;

                new docJSONpro().execute("http://snk.autonsi.com/product/get_process_nm_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon);

                Log.d("docJSONpro","http://snk.autonsi.com/product/get_process_nm_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void LickspinnerPro() {
        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, arrayLineNO);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerPro.setAdapter(arrayAdapter);
        spinnerPro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int position, long id) {
                line = arrayLineNO.get(position);
                processchon = arrayLineNO.get(position);
                //processnamechon = arrayLinePro.get(position);

                new docJSONdata().execute("http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);

                Log.d("Actual_All", "http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    //them{


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

//            if(chuoitrave == "true"){
//               // Toast.makeText(Actual.this, "Oke", Toast.LENGTH_SHORT).show();
//            } else {
//               // Toast.makeText(Actual.this, "NoOke", Toast.LENGTH_SHORT).show();
//            }
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
                content.append(line + "\n");
            }
            bufferedReader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content.toString();
    }

//    // menu
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setHasOptionsMenu(true);
//    }
//
//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        // TODO Add your menu entries here
//        //inflater.inflate(R.menu.menu_composite_lot, menu); 0 xoa
//        super.onCreateOptionsMenu(menu, inflater);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
////        if (id == R.id.CompositeLot) {
////            if (level.equals("")){
////
////            }else if (level.equals("last")) {
////                Intent intent = new Intent(getActivity(), LotProductActivity.class);
////                intent.putExtra("WOchon", WOchon);
////                intent.putExtra("Linechon", Linechon);
////                intent.putExtra("processchon", processchon);
////                intent.putExtra("processnamechon", processnamechon);
////                intent.putExtra("datechon", datechon);
////                intent.putExtra("level", level);
////                intent.putExtra("process_nochon", process_nochon);
////                intent.putExtra("line_nochon", line_nochon);
////                intent.putExtra("olddnochon", olddnochon);
////
////
////                startActivity(intent);
////            } else {
////                Intent intent = new Intent(getActivity(), LotCompositeActivity.class);
////                intent.putExtra("WOchon", WOchon);
////                intent.putExtra("Linechon", Linechon);
////                intent.putExtra("processchon", processchon);
////                intent.putExtra("processnamechon", processnamechon);
////                intent.putExtra("datechon", datechon);
////                intent.putExtra("level", level);
////                intent.putExtra("process_nochon", process_nochon);
////                intent.putExtra("line_nochon", line_nochon);
////                intent.putExtra("olddnochon", olddnochon);
////                startActivity(intent);
////            }
////            return true;
////        }
//
//        if (id == R.id.Staff_id) {
//            if (!level.equals("")) {
//                popup_Staff();
//            }
//
//        }
//
//        if (id == R.id.machine_id) {
//            if (!level.equals("")) {
//                popup_Machine();
//            }
//        }
//
//        if (id == R.id.mold_id) {
//            if (!level.equals("")) {
//                popup_Mold();
//            }
//        }
//
//
//        if (id == R.id.CompositeLottext) {
//            if (level.equals("")) {
//
//            } else if (level.equals("last")) {
//
//                if (kiemtra == 1){
//                    numActual = fragmentScan.tvActualNum.getText().toString();
//                }else {
//                    numActual = fragmentKeyin.tvActualNum.getText().toString();
//                }
//
//                Intent intent = new Intent(getActivity(), LotProductActivity.class);
//                intent.putExtra("WOchon", WOchon);
//                intent.putExtra("Linechon", Linechon);
//                intent.putExtra("processchon", processchon);
//                intent.putExtra("processnamechon", processnamechon);
//                intent.putExtra("datechon", datechon);
//                intent.putExtra("level", level);
//                intent.putExtra("process_nochon", process_nochon);
//                intent.putExtra("line_nochon", line_nochon);
//                intent.putExtra("olddnochon", olddnochon);
//                startActivity(intent);
//            } else {
//                if (kiemtra == 1){
//                    numActual = fragmentScan.tvActualNum.getText().toString();
//                }else {
//                    numActual = fragmentKeyin.tvActualNum.getText().toString();
//                }
//                numActual = fragmentKeyin.tvActualNum.getText().toString();
//                Intent intent = new Intent(getActivity(), LotCompositeActivity.class);
//                intent.putExtra("WOchon", WOchon);
//                intent.putExtra("Linechon", Linechon);
//                intent.putExtra("processchon", processchon);
//                intent.putExtra("processnamechon", processnamechon);
//                intent.putExtra("datechon", datechon);
//                intent.putExtra("level", level);
//                intent.putExtra("process_nochon", process_nochon);
//                intent.putExtra("line_nochon", line_nochon);
//                intent.putExtra("olddnochon", olddnochon);
//                startActivity(intent);
//            }
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    ///////Mold///////////////////////////
    private void popup_Mold() {
        final int[] vitrixoa = {-1};
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_mold_working, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        dialog.setCancelable(false);
        dialog.setContentView(dialogView);
        lvMold = dialogView.findViewById(R.id.lvMold);
        LoadData_mold_working();
        dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(false);


        dialogView.findViewById(R.id.btn_addStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_mold();
            }
        });


        lvMold.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitrixoa[0] = i;
                //       Toast.makeText(getContext(), ""+i , Toast.LENGTH_SHORT).show();
                dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(true);
            }
        });

        dialogView.findViewById(R.id.btn_deleteStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitrixoa[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {
                    xacnhandeleteMold(vitrixoa[0]);
                }

            }
        });


        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void LoadData_mold_working() {
        new Data_Mold_working().execute("http://snk.autonsi.com/product/Getprocess_moldunit_API?process_nm=" + processnamechon+"&fo_no="+WOchon);
        Log.d("Data_Mold_working", "http://snk.autonsi.com/product/Getprocess_moldunit_API?process_nm=" + processnamechon+"&fo_no="+WOchon);
    }

    class Data_Mold_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            moldMasters = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    moldMasters.add(new StaffMaster("", "", "", "There are no Mold.", "", "", ""));
                } else {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String staff_id = jsonObject.getString("mc_type");
                        String uname = jsonObject.getString("mc_no");
                        String prounit_cd = jsonObject.getString("prounit_cd");
                        String id = jsonObject.getString("pmid");
                        String machine_type_nm = jsonObject.getString("machine_type_nm");
                        String start_dt = jsonObject.getString("start_dt");
                        String end_dt = jsonObject.getString("end_dt");
                        moldMasters.add(new StaffMaster(staff_id, id, prounit_cd, uname, machine_type_nm, start_dt, end_dt));
                    }
                }
                moldMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, moldMasters);
                lvMold.setAdapter(moldMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class Delete_Mole_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    LoadData_mold_working();
                } else {
                    AlertNotExist(object.getString("message") + ".");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void popup_add_mold() {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        vitriMold = -1;


        popup_add_Mold = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_mold_all, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        lvMoldall = dialogView.findViewById(R.id.lvMoldall);
        popup_add_Mold.setCancelable(false);

        EditText search_name = dialogView.findViewById(R.id.search_name);

        Data_mold_working_All();

        Button bt_add = dialogView.findViewById(R.id.btn_addStaff);
        final int[] vitriLV = {-1};
        bt_add.setEnabled(false);

        lvMoldall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitriLV[0] = i;

                bt_add.setEnabled(true);

            }
        });


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitriLV[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {

                    vitriMold = vitriLV[0];
                    new Add_Mold_working().execute("http://snk.autonsi.com/product/Createmold_unit_api?process_nm=" +
                            processnamechon  + "&md_no=" + MoldAllMasters.get(vitriMold).getStaffId() + "&remark=" + ""+"&fo_no="+WOchon);
                    Log.d("Add_Mold_working", "http://snk.autonsi.com/product/Createmold_unit_api?process_nm=" +
                            processnamechon + "&md_no=" + MoldAllMasters.get(vitriMold).getStaffId() + "&remark=" + ""+"&fo_no="+WOchon);

                }
            }
        });

///////////////////
        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterMold(s.toString());
            }
        });

        search_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filterMold(v.getText().toString());
                    search_name.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_name.getWindowToken(), 0);
                    //set vi tri con tro nhap nhay
                    search_name.setFocusableInTouchMode(true);
                    search_name.requestFocus();
                    return true;
                }
                return false;
            }
        });


        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_Mold.dismiss();
            }
        });

        popup_add_Mold.setContentView(dialogView);
        popup_add_Mold.show();

    }

    private void Data_mold_working_All() {

        new Data_mold_working_All().execute("http://snk.autonsi.com/product/MoldMgtData_api");
        Log.d("Data_mold_working_All", "http://snk.autonsi.com/product/MoldMgtData_api");
    }

    class Data_mold_working_All extends AsyncTask<String, Integer, String> {

        @Override

        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MoldAllMasters = new ArrayList<>();
            MoldAllMastersnew = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    MoldAllMasters.add(new StaffMaster("", "", "", "There are no Machine.", "", "", ""));
                } else {

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        // String staff_id = jsonObject.getString("mc_type");
                        String uno = jsonObject.getString("md_no");
                        String mc_nm = jsonObject.getString("md_nm");
                        //String id = jsonObject.getString("pmid");
                        MoldAllMasters.add(new StaffMaster(i + 1 + "", "", uno, mc_nm, "", "", ""));
                        MoldAllMastersnew = MoldAllMasters;
                    }

                }
                MoldAllMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, MoldAllMasters);
                lvMoldall.setAdapter(MoldAllMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void filterMold(String code) {
        ArrayList<StaffMaster> filteredList = new ArrayList<>();


        for (StaffMaster item : MoldAllMastersnew) {
            if (item.getStaffId().toLowerCase().contains(code.toLowerCase())) {
                filteredList.add(item);
            }

        }

        MoldAllMasters = filteredList;
        MoldAllMasterAdapter.filterList(filteredList);
    }

    class Add_Mold_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    popup_add_Mold.dismiss();
                    LoadData_mold_working();

                } else {
                    AlertNotExist(object.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ///////End Mold///////////////////////////


    ///////Machine///////////////////////////
    private void popup_Machine() {
        final int[] vitrixoa = {-1};
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_machine_working, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        dialog.setCancelable(false);

        dialog.setContentView(dialogView);
        lvMachine = dialogView.findViewById(R.id.lvMachine);
        LoadData_machine_working();
        dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(false);


        dialogView.findViewById(R.id.btn_addStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_machine();
            }
        });


        lvMachine.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitrixoa[0] = i;
                //       Toast.makeText(getContext(), ""+i , Toast.LENGTH_SHORT).show();
                dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(true);
            }
        });

        dialogView.findViewById(R.id.btn_deleteStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitrixoa[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {
                    xacnhandeleteMC(vitrixoa[0]);
                }

            }
        });


        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void LoadData_machine_working() {

        new Data_Machine_working().execute("http://snk.autonsi.com/product/Getprocess_machineunit_api?prounit_cd=" + processnamechon+"&fo_no="+WOchon);
        Log.d("Data_Machine_working", "http://snk.autonsi.com/product/Getprocess_machineunit_api?prounit_cd=" + processnamechon+"&fo_no="+WOchon);
    }

    class Data_Machine_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            machineMasters = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    machineMasters.add(new StaffMaster("", "", "", "There are no machine.", "", "", ""));
                } else {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String staff_id = jsonObject.getString("mc_type");
                        String uname = jsonObject.getString("mc_no");
                        String prounit_cd = jsonObject.getString("prounit_cd");
                        String id = jsonObject.getString("pmid");
                        String machine_type_nm = jsonObject.getString("machine_type_nm");
                        String start_dt = jsonObject.getString("start_dt");
                        String end_dt = jsonObject.getString("end_dt");
                        machineMasters.add(new StaffMaster(staff_id, id, prounit_cd, uname, machine_type_nm, start_dt, end_dt));
                    }
                }
                machineMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, machineMasters);
                lvMachine.setAdapter(machineMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class Delete_Machine_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    LoadData_machine_working();
                } else {
                    AlertNotExist(object.getString("message") + ".");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void popup_add_machine() {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        vitriMC = -1;


        popup_add_MC = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_machine_all, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        lvMCall = dialogView.findViewById(R.id.lvMCall);
        popup_add_MC.setCancelable(false);

        EditText search_name = dialogView.findViewById(R.id.search_name);

        Data_MC_working_All();

        Button bt_add = dialogView.findViewById(R.id.btn_addStaff);
        final int[] vitriLV = {-1};
        bt_add.setEnabled(false);

        lvMCall.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitriLV[0] = i;

                bt_add.setEnabled(true);

            }
        });


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitriLV[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {

                    vitriMC = vitriLV[0];
                    new Add_MC_working().execute("http://snk.autonsi.com/product/Createprocessmachine_unit_API?prounit_cd=" +
                            processnamechon + "&mc_type=" + MCAllMasters.get(vitriMC).getStt() + "&mc_no=" + MCAllMasters.get(vitriMC).getStaffId() + "&remark=" + ""+"&fo_no="+WOchon);
                    Log.d("Add_MC_working", "http://snk.autonsi.com/product/Createprocessmachine_unit_API?prounit_cd=" +
                            processnamechon + "&mc_type=" + MCAllMasters.get(vitriMC).getStt() + "&mc_no=" + MCAllMasters.get(vitriMC).getStaffId() + "&remark=" + ""+"&fo_no="+WOchon);

                }
            }
        });

///////////////////
        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filterMC(s.toString());
            }
        });

        search_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filterMC(v.getText().toString());
                    search_name.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_name.getWindowToken(), 0);
                    //set vi tri con tro nhap nhay
                    search_name.setFocusableInTouchMode(true);
                    search_name.requestFocus();
                    return true;
                }
                return false;
            }
        });


        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_MC.dismiss();
            }
        });

        popup_add_MC.setContentView(dialogView);
        popup_add_MC.show();

    }

    private void Data_MC_working_All() {
        new Data_MC_working_All().execute("http://snk.autonsi.com/product/Getmachine_api");
        Log.d("Data_MC_working_All", "http://snk.autonsi.com/product/Getmachine_api");
    }

    class Data_MC_working_All extends AsyncTask<String, Integer, String> {

        @Override

        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            MCAllMasters = new ArrayList<>();
            MCAllMastersnew = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    MCAllMasters.add(new StaffMaster("", "", "", "There are no Machine.", "", "", ""));
                } else {

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String staff_id = jsonObject.getString("mc_type");
                        String uno = jsonObject.getString("mc_no");
                        String mc_nm = jsonObject.getString("mc_nm");
                        //String id = jsonObject.getString("pmid");
                        MCAllMasters.add(new StaffMaster(staff_id, "", uno, mc_nm, "", "", ""));
                        MCAllMastersnew = MCAllMasters;
                    }

                }
                MCAllMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, MCAllMasters);
                lvMCall.setAdapter(MCAllMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void filterMC(String code) {
        ArrayList<StaffMaster> filteredList = new ArrayList<>();


        for (StaffMaster item : MCAllMastersnew) {
            if (item.getStaffId().toLowerCase().contains(code.toLowerCase())) {
                filteredList.add(item);
            }

        }

        MCAllMasters = filteredList;
        MCAllMasterAdapter.filterList(filteredList);
    }


    class Add_MC_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    popup_add_MC.dismiss();
                    LoadData_machine_working();

                } else {
                    AlertNotExist(object.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    /////////////////////////////////////////ket thu popup machine


    /// popup Staff////////////////////
    private void popup_Staff() {
        final int[] vitrixoa = {-1};
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);


        final Dialog dialog = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_staff_working, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));
        dialog.setCancelable(false);

        dialog.setContentView(dialogView);
        lvStaff = dialogView.findViewById(R.id.lvStaff);
        LoadData_staff_working();
        dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(false);


        dialogView.findViewById(R.id.btn_addStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_staff();
            }
        });


        lvStaff.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitrixoa[0] = i;
                dialogView.findViewById(R.id.btn_deleteStaff).setEnabled(true);
            }
        });

        dialogView.findViewById(R.id.btn_deleteStaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitrixoa[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {
                    xacnhandelete(vitrixoa[0]);
                }

            }
        });

        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void LoadData_staff_working() {
        new Data_Staff_working().execute("http://snk.autonsi.com/product/get_staff?process_nm=" + processnamechon+"&fo_no="+WOchon);
        Log.d("Data_Staff_working", "http://snk.autonsi.com/product/get_staff?process_nm=" + processnamechon+"&fo_no="+WOchon);
    }

    class Data_Staff_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            staffMasters = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    staffMasters.add(new StaffMaster("", "", "", "There are no staff", "", "", ""));
                } else {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String staff_id = jsonObject.getString("staff_id");
                        String uname = jsonObject.getString("uname");
                        String id = jsonObject.getString("psid");
                        String staff_type_nm = jsonObject.getString("staff_type_nm");
                        String start_dt = jsonObject.getString("start_dt");
                        String end_dt = jsonObject.getString("end_dt");
                        staffMasters.add(new StaffMaster(i + 1 + "", id, staff_id, uname, staff_type_nm, start_dt, end_dt));
                    }
                }
                staffMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, staffMasters);
                lvStaff.setAdapter(staffMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    class Delete_Staff_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    LoadData_staff_working();
                } else {
                    AlertNotExist("Delete failed. Please, check data sever.");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void popup_add_staff() {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        vitrilamviec = -1;


        popup_add_staff = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        dialog.setContentView(R.layout.popup_staff_working);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.popup_staff_all, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        dialogView.setMinimumHeight((int) (displayRectangle.height() * 1f));

        lvStaffAll = dialogView.findViewById(R.id.lvStaffAll);
        popup_add_staff.setCancelable(false);
        EditText search_id = dialogView.findViewById(R.id.search_id);

        EditText search_name = dialogView.findViewById(R.id.search_name);

        Data_Staff_working_All();

        Button bt_add = dialogView.findViewById(R.id.btn_addStaff);
        final int[] vitriLV = {-1};
        bt_add.setEnabled(false);

        lvStaffAll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitriLV[0] = i;
                bt_add.setEnabled(true);

            }
        });


        bt_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (vitriLV[0] == -1) {
                    AlertNotExist("No lines have been selected yet");
                } else {

                    vitrilamviec = vitriLV[0];
                    new Data_vt_lamviec().execute("http://snk.autonsi.com/product/get_staff_type");
                    Log.d("Data_vt_lamviec", "http://snk.autonsi.com/product/get_staff_type");


                    //  http://snk.autonsi.com/product/Createprocess_unitstaff_api?process_nm=FNS001&staff_id=180033&staff_tp=LEA
                }
            }
        });


        search_id.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString(), search_name.getText().toString());
            }
        });

        search_id.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filter(v.getText().toString(), search_name.getText().toString());
                    search_id.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_id.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

///////////////////
        search_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(search_id.getText().toString(), s.toString());
            }
        });

        search_name.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    filter(search_id.getText().toString(), v.getText().toString());
                    search_name.clearFocus();
                    InputMethodManager in = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    in.hideSoftInputFromWindow(search_name.getWindowToken(), 0);
                    //set vi tri con tro nhap nhay
                    search_name.setFocusableInTouchMode(true);
                    search_name.requestFocus();
                    return true;
                }
                return false;
            }
        });


        dialogView.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_staff.dismiss();
            }
        });

        popup_add_staff.setContentView(dialogView);
        popup_add_staff.show();

    }

    private void popup_add_vt_lv() {


        popup_add_vt_lv = new Dialog(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        View dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.custum_vitri_lamviec, null);
        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));

        RecyclerView recyclerView = dialogView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        vitrilamviecAdapter = new VitrilamviecAdapter(vitriLamviecMasters);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(vitrilamviecAdapter);

        vitrilamviecAdapter.setOnItemClickListener(new VitrilamviecAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // /product/Createprocess_unitstaff_api?process_nm=ROT027&staff_id=180038&staff_tp=LEA
                new Add_Staff_working().execute("http://snk.autonsi.com/product/Createprocess_unitstaff_api?process_nm=" +
                        processnamechon +
                        "&staff_id=" +
                        staffAllMasters.get(vitrilamviec).getStaffId() +
                        "&staff_tp=" +
                        vitriLamviecMasters.get(position).getId()+"&fo_no="+WOchon);
                Log.d("Add_Staff_working", "http://snk.autonsi.com/product/Createprocess_unitstaff_api?process_nm=" +
                        processnamechon +
                        "&staff_id=" +
                        staffAllMasters.get(vitrilamviec).getStaffId() +
                        "&staff_tp=" +
                        vitriLamviecMasters.get(position).getId()+"&fo_no="+WOchon);

            }
        });

        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup_add_vt_lv.cancel();
            }
        });

        popup_add_vt_lv.setContentView(dialogView);
        popup_add_vt_lv.show();


    }

    class Add_Staff_working extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject object = new JSONObject(s);
                if (object.getBoolean("result")) {
                    Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
                    popup_add_vt_lv.dismiss();
                    popup_add_staff.dismiss();
                    LoadData_staff_working();

                } else {
                    AlertNotExist(object.getString("message"));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    class Data_vt_lamviec extends AsyncTask<String, Integer, String> {


        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            vitriLamviecMasters = new ArrayList<>();

            try {
                JSONObject object = new JSONObject(s);
                if (object.getString("result").equals("") || object.getString("result").equals("null")
                        || object.getString("result").equals("false") || object.getString("result").equals("[]")) {
                    Log.e("sssssssss", "error");
                    return;
                }

                JSONArray jsonArray = object.getJSONArray("result");
                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    String staff_tp = jsonObject.getString("staff_tp");
                    String staff_tp_nm = jsonObject.getString("staff_tp_nm");
                    vitriLamviecMasters.add(new VitriLamviecMaster(staff_tp, staff_tp_nm));
                }

                popup_add_vt_lv();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }


    private void Data_Staff_working_All() {
        new Data_Staff_working_All().execute("http://snk.autonsi.com/product/get_staff_list_all");
        Log.d("Data_Staff_working_All", "http://snk.autonsi.com/product/get_staff_list_all");
    }

    class Data_Staff_working_All extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            staffAllMasters = new ArrayList<>();
            staffAllMastersnew = new ArrayList<>();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray array = object.getJSONArray("result");
                if (array.length() == 0) {
                    staffAllMasters.add(new StaffMaster("", "", "", "There are no staff", "", "", ""));
                } else {
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonObject = array.getJSONObject(i);
                        String staff_id = jsonObject.getString("userid");
                        String uname = jsonObject.getString("uname");
                        staffAllMasters.add(new StaffMaster(i + 1 + "", "", staff_id, uname, "", "", ""));
                        staffAllMastersnew = staffAllMasters;
                    }
                }
                staffAllMasterAdapter = new StaffMasterAdapter(getContext(), R.layout.item_staff_working, staffAllMasters);
                lvStaffAll.setAdapter(staffAllMasterAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }

    private void filter(String id, String name) {
        ArrayList<StaffMaster> filteredList = new ArrayList<>();


        for (StaffMaster item : staffAllMastersnew) {
            if (item.getStaffId().toLowerCase().contains(id.toLowerCase())) {
                if (item.getStaffName().toLowerCase().contains(name.toLowerCase())) {
                    filteredList.add(item);
                }
            }

        }

        staffAllMasters = filteredList;
        staffAllMasterAdapter.filterList(filteredList);
    }


    @Override
    public void onResume() {
        super.onResume();

        if (WOchon != null && Linechon != null && processchon != null) {
            new docJSONdata().execute("http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);
            Log.d("docJSONData", "http://snk.autonsi.com/product/get_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no=" + processchon);
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

    private void xacnhandelete(int position) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Are you sure you want to delete this name?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Delete_Staff_working().execute("http://snk.autonsi.com/product/Delete_api?psid=" + staffMasters.get(position).getStaffNo());
                Log.d("Delete_Staff_working", "http://snk.autonsi.com/product/Delete_api?psid=" + staffMasters.get(position).getStaffNo());
            }
        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
        ;
        alertDialog.show();
    }

    private void xacnhandeleteMC(int position) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Are you sure you want to delete this Machine?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Delete_Machine_working().execute("http://snk.autonsi.com/product/Delete_mc_api?pmid=" + machineMasters.get(position).getStaffNo());
                Log.d("Delete_Machine_working", "http://snk.autonsi.com/product/Delete_mc_api?pmid=" + machineMasters.get(position).getStaffNo());
            }
        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
        ;
        alertDialog.show();
    }

    private void xacnhandeleteMold(int position) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(getContext());
        alertDialog.setCancelable(false);
        alertDialog.setTitle("Are you sure you want to delete this Mold?");
        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                new Delete_Mole_working().execute("http://snk.autonsi.com/product/Delete_mold_api?pmid=" + moldMasters.get(position).getStaffNo());
                Log.d("Delete_Mole_working", "http://snk.autonsi.com/product/Delete_mold_api?pmid=" + moldMasters.get(position).getStaffNo());
            }
        })
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
        ;
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
        if (SerialPortManager.instance().open("/dev/ttyS0","9600") != null) {
            inflater.inflate(R.menu.menu_weight, menu);
        } else {
            inflater.inflate(R.menu.menu_composite_lot, menu);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }


//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
////        if (id == R.id.CompositeLot) {
////            if (level.equals("")){
////
////            }else if (level.equals("last")) {
////                Intent intent = new Intent(getActivity(), LotProductActivity.class);
////                intent.putExtra("WOchon", WOchon);
////                intent.putExtra("Linechon", Linechon);
////                intent.putExtra("processchon", processchon);
////                intent.putExtra("processnamechon", processnamechon);
////                intent.putExtra("datechon", datechon);
////                intent.putExtra("level", level);
////                intent.putExtra("process_nochon", process_nochon);
////                intent.putExtra("line_nochon", line_nochon);
////                intent.putExtra("olddnochon", olddnochon);
////
////
////                startActivity(intent);
////            } else {
////                Intent intent = new Intent(getActivity(), LotCompositeActivity.class);
////                intent.putExtra("WOchon", WOchon);
////                intent.putExtra("Linechon", Linechon);
////                intent.putExtra("processchon", processchon);
////                intent.putExtra("processnamechon", processnamechon);
////                intent.putExtra("datechon", datechon);
////                intent.putExtra("level", level);
////                intent.putExtra("process_nochon", process_nochon);
////                intent.putExtra("line_nochon", line_nochon);
////                intent.putExtra("olddnochon", olddnochon);
////                startActivity(intent);
////            }
////            return true;
////        }
//
//        if (id == R.id.Staff_id) {
//            if (!level.equals("")) {
//                popup_Staff();
//            }
//
//        }
//
//        if (id == R.id.machine_id) {
//            if (!level.equals("")) {
//                popup_Machine();
//            }
//        }
//
//        if (id == R.id.mold_id) {
//            if (!level.equals("")) {
//                popup_Mold();
//            }
//        }
//
//
//        if (id == R.id.CompositeLottext) {
//            if (level.equals("")) {
//
//            } else if (level.equals("last")) {
//
//                if (kiemtra == 1){
//                    numActual = fragmentScan.tvActualNum.getText().toString();
//                }else {
//                    numActual = fragmentKeyin.tvActualNum.getText().toString();
//                }
//
//                Intent intent = new Intent(getActivity(), LotProductActivity.class);
//                intent.putExtra("WOchon", WOchon);
//                intent.putExtra("Linechon", Linechon);
//                intent.putExtra("processchon", processchon);
//                intent.putExtra("processnamechon", processnamechon);
//                intent.putExtra("datechon", datechon);
//                intent.putExtra("level", level);
//                intent.putExtra("process_nochon", process_nochon);
//                intent.putExtra("line_nochon", line_nochon);
//                intent.putExtra("olddnochon", olddnochon);
//                startActivity(intent);
//            } else {
//                if (kiemtra == 1){
//                    numActual = fragmentScan.tvActualNum.getText().toString();
//                }else {
//                    numActual = fragmentKeyin.tvActualNum.getText().toString();
//                }
//                numActual = fragmentKeyin.tvActualNum.getText().toString();
//                Intent intent = new Intent(getActivity(), LotCompositeActivity.class);
//                intent.putExtra("WOchon", WOchon);
//                intent.putExtra("Linechon", Linechon);
//                intent.putExtra("processchon", processchon);
//                intent.putExtra("processnamechon", processnamechon);
//                intent.putExtra("datechon", datechon);
//                intent.putExtra("level", level);
//                intent.putExtra("process_nochon", process_nochon);
//                intent.putExtra("line_nochon", line_nochon);
//                intent.putExtra("olddnochon", olddnochon);
//                startActivity(intent);
//            }
//
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.Staff_id) {
            if (!level.equals("")) {
                popup_Staff();
            }

        }

        if (id == R.id.machine_id) {
            if (!level.equals("")) {
                popup_Machine();
            }
        }

        if (id == R.id.mold_id) {
            if (!level.equals("")) {
                popup_Mold();
            }
        }


        if (id == R.id.CompositeLottext) {
            if (level.equals("")) {

            } else if (level.equals("last")) {

                if (kiemtra == 1){
                    numActual = fragmentScan.tvActualNum.getText().toString();
                }else {
                    numActual = fragmentKeyin.tvActualNum.getText().toString();
                }

                Intent intent = new Intent(getActivity(), LotProductActivity.class);
                intent.putExtra("WOchon", WOchon);
                intent.putExtra("Linechon", Linechon);
                intent.putExtra("processchon", processchon);
                intent.putExtra("processnamechon", processnamechon);
                intent.putExtra("datechon", datechon);
                intent.putExtra("level", level);
                intent.putExtra("process_nochon", process_nochon);
                intent.putExtra("line_nochon", line_nochon);
                intent.putExtra("olddnochon", olddnochon);
                startActivity(intent);
            } else {
                if (kiemtra == 1){
                    numActual = fragmentScan.tvActualNum.getText().toString();
                }else {
                    numActual = fragmentKeyin.tvActualNum.getText().toString();
                }
                numActual = fragmentKeyin.tvActualNum.getText().toString();
                Intent intent = new Intent(getActivity(), LotCompositeActivity.class);
                intent.putExtra("WOchon", WOchon);
                intent.putExtra("Linechon", Linechon);
                intent.putExtra("processchon", processchon);
                intent.putExtra("processnamechon", processnamechon);
                intent.putExtra("datechon", datechon);
                intent.putExtra("level", level);
                intent.putExtra("process_nochon", process_nochon);
                intent.putExtra("line_nochon", line_nochon);
                intent.putExtra("olddnochon", olddnochon);
                startActivity(intent);
            }

            return true;
        }

        if (id == R.id.Weight) {
            if (SerialPortManager.instance().open("/dev/ttyS0","9600") != null) {
                String getdata = SerialReadThread.data;
                if (getdata == null){
                    AlertNotExist("Connection to device failed! Please check again");
                }
                else {
                    if (datechon == null|| datechon.length()==0){
                        AlertNotExist("Please check WO again!");
                    }else {
                        popup_weight();
                    }
                }

            }else {

                AlertNotExist("Device not support!");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void popup_weight() {
        final Dialog dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.popup_weight_scale);
        dialog.setCancelable(false);
        weight_current = dialog.findViewById(R.id.weight_current);
        num_current = dialog.findViewById(R.id.num_current);

        qty_standa = dialog.findViewById(R.id.qty_standa);
        weight_standa = dialog.findViewById(R.id.weight_standa);
        weight1_standa = dialog.findViewById(R.id.weight1_standa);
        qty_standa.setText(numQty_standa+"");
        weight_standa.setText(numWeight+"");
        weight1_standa.setText(numWeight1+"");
        qty_standa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputTextDialog("qty_standa");
            }
        });
        weight_standa.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                inputTextDialog("weight_standa");
                return false;
            }
        });


        weight_standa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String getdata =SerialReadThread.data;
                if (getdata == null){
                    return;
                }else {


                    if (getdata.length() == 18) {
                        getdata=getdata.replace(",+","").replace("g","").replace(",-","").trim();
                        numWeight = Double.parseDouble(getdata);
                        weight_standa.setText(numWeight + "");

                        numWeight1 = Math.round(numWeight / numQty_standa * 100.0) / 100.0;

                        weight1_standa.setText(numWeight1 + "");

                    }



                }

                //inputTextDialog("weight_standa");
            }
        });




        KTvonglap = -1;
        mmHandler.postDelayed(mDelay,100);
        dialog.findViewById(R.id.img_close_check_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KTvonglap = 1;
                dialog.dismiss();
            }
        });
        dialog.findViewById(R.id.btn_check_save_ck).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                KTvonglap = 1;

               String GroupQtyStr = num_current.getText().toString().trim();
               int GroupQtynum = Integer.parseInt(GroupQtyStr);
//                GroupQty.setText(GroupQtynum);

                String numActualString = fragmentKeyin.tvActualNum.getText().toString();
                numActualweight = Integer.parseInt(numActualString);
                numActualweight = GroupQtynum + numActualweight ;
                String numDefString = fragmentKeyin.tvDefectiveNum.getText().toString();
                int numDefect = Integer.parseInt(numDefString);


                new docJSONinsertActual().execute("http://snk.autonsi.com/product/post_m_order_facline_day?fo_no=" + WOchon + "&line_no=" + Linechon + "&process_no="
                        + processchon + "&prounit_nm=" + prcunitchon + "&work_ymd=" + datechon + "&prod_sts_cd=" + "A" + "&done_qty=" + numActualweight + "&refer_qty=" + numDefect);


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
                fragmentKeyin.tvActualNum.setText(numActualweight+"");
            } else {
                AlertNotExist("Error!");
            }
        }
    }


    private void inputTextDialog(String key) {
        Rect displayRectangle = new Rect();
        Window window = getActivity().getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity(), R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Qty Value");
        View dialogView = null;
        if (key.equals("qty_standa")){
            dialogView= LayoutInflater.from(getActivity()).inflate(R.layout.number_input_layout_ok_cancel, null);
        }else if (key.equals("weight_standa")){
            dialogView = LayoutInflater.from(getActivity()).inflate(R.layout.number_input_layout_ok_cancel_le, null);
        }



        dialogView.setMinimumWidth((int) (displayRectangle.width() * 1f));
        //dialogView.setMinimumHeight((int)(displayRectangle.height() * 1f));

        builder.setView(dialogView);
        final android.app.AlertDialog alertDialog = builder.create();
        alertDialog.setCanceledOnTouchOutside(false);

        //anh xa

        final EditText input = dialogView.findViewById(R.id.input);
        Button bt_OK = dialogView.findViewById(R.id.in_btn_ok);
        Button bt_Cal = dialogView.findViewById(R.id.in_btn_cancel);

        bt_OK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt = "";
                txt = input.getText().toString().trim();
                if (txt.length() > 0 && Double.parseDouble(txt)>0) {
                    if (key.equals("qty_standa")){
                        numQty_standa = Integer.parseInt(txt);
                        qty_standa.setText(numQty_standa+"");
                        numWeight1 = Math.round(numWeight/numQty_standa* 100.0) / 100.0;

                        weight1_standa.setText(numWeight1+"");

                        alertDialog.cancel();

                    }else if (key.equals("weight_standa")){
                        numWeight = Double.parseDouble(txt);
                        weight_standa.setText(numWeight+"");

                        numWeight1 = Math.round(numWeight/numQty_standa* 100.0) / 100.0;

                        weight1_standa.setText(numWeight1+"");


                        alertDialog.cancel();
                    }

                } else {
                    input.setError("Please enter value ");
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
    private Handler mmHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

        }

    };

    private final Runnable mDelay = new Runnable() {
        @Override
        public void run() {
            if (KTvonglap==1){

            }else {

                String getdata =SerialReadThread.data;
                if (getdata.length() == 18) {
                    getdata=getdata.replace(",+","").replace("g","").replace(",-","").trim();
                    weight_current.setText(getdata);
                    Double dataActualWeightnum = Double.parseDouble(getdata);
                    int SLsp = Math.round(Math.round(dataActualWeightnum / numWeight1));
                    num_current.setText(SLsp + "");
                }

                //weight_current.setText(SerialReadThread.data);
                mmHandler.postDelayed(mDelay, 50);
            }
        }
    };

}

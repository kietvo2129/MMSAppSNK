package com.licheedev.MMSSNK.WeightScale;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Dialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.le.BluetoothLeScanner;
import android.bluetooth.le.ScanCallback;
import android.bluetooth.le.ScanFilter;
import android.bluetooth.le.ScanSettings;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bixolon.labelprinter.BixolonLabelPrinter;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.activity.base.BaseActivity;
import com.licheedev.MMSSNK.comn.SerialPortManager;
import com.licheedev.MMSSNK.comn.SerialReadThread;
import com.licheedev.MMSSNK.print_bluetooth.BitmapUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;

public class WeightScaleActivity extends BaseActivity {
    TextView tvWO,tvStandarData,save,ActualWeight,ActualAmount,Squareallmaterial,DefectiveAmount,ProductYieldRate;
    private boolean mOpened = false;
    EditText StandardAmount,StandardWeight,PalletWeight,SquarePerPcs;
    TextView SaveDialogStandarData,Confirm;
    double dataStandardWeightNum = 0.00,dataPalletWeightNum = 0.00,dataSquarePerPcsNum = 0.00;
    int dataStandardAmountNum = 0,dataActualAmountnum = 0, dataDefectiveAmountnum = 0;
    SharedPreferences sharedPreferences;
    double KLmotSP,dataActualWeightnum, PhanTram = 0.00;
    int Actual = 0,Defective = 0;
    TextView tvActual,tvDefective;
    CheckBox checkpallet;
    Spinner spinner_WO;




    private static final String[] FUNCTIONS = { "drawText", "drawVectorFontText", "draw1dBarcode", "drawMaxicode", "drawPdf417", "drawQrCode", "drawDataMatrix", "drawBlock", "drawCircle", "setCharacterSet", "setPrintingType", "setMargin", "setLength", "setWidth", "setBufferMode", "clearBuffer", "setSpeed", "setDensity", "setOrientation", "setOffset", "setCutterPosition", "drawBitmap", "initializePrinter", "printInformation", "setAutoCutter", "getStatus", "getPrinterInformation", "executeDirectIo", "printSample", "print PDF"/*, "Sample Receipt" */};

    private static InputStream[] demoFiles = {};
    // Name of the connected device
    private String mConnectedDeviceName = null;

    private ListView mListView;
    private android.app.AlertDialog mWifiDialog;
    private android.app.AlertDialog mPrinterInformationDialog;
    private android.app.AlertDialog mSetPrintingTypeDialog;
    private android.app.AlertDialog mSetMarginDialog;
    private android.app.AlertDialog mSetWidthDialog;
    private android.app.AlertDialog mSetLengthDialog;
    private android.app.AlertDialog mSetBufferModeDialog;
    private android.app.AlertDialog mSetSpeedDialog;
    private android.app.AlertDialog mSetDensityDialog;
    private android.app.AlertDialog mSetOrientationDialog;
    private android.app.AlertDialog mSetOffsetDialog;
    private android.app.AlertDialog mCutterPositionSettingDialog;
    private android.app.AlertDialog mAutoCutterDialog;
    private android.app.AlertDialog mGetCharacterSetDialog;
    private android.app.AlertDialog mWifiDirectDialog;

    private boolean mIsConnected;

    static BixolonLabelPrinter mBixolonLabelPrinter;

    private boolean checkedManufacture = false;

    public Handler m_hHandler = null;
    public BluetoothAdapter m_BluetoothAdapter = null;
    public BluetoothLeScanner mLEScanner = null;
    public ScanSettings settings = null;
    public List<ScanFilter> filters;
    public ArrayAdapter<String> adapter = null;
    public ArrayList<BluetoothDevice> m_LeDevices;

    private ScanCallback mScanCallback;

    private final String ACTION_USB_PERMISSION = "com.android.example.USB_PERMISSION";
    private PendingIntent mPermissionIntent;
    private UsbManager usbManager;
    private UsbDevice device;
    private boolean tryedAutoConnect = false;


    PhotoEditor photoEditor;
    PhotoEditorView photoEditorView;
    public static final String pictureName = "hinh.jpg";
    Bitmap aaaaa;
    CoordinatorLayout coordinatorLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_weight_scale);
        mOpened = SerialPortManager.instance().open("/dev/ttyS0","9600") != null;//////// hiển thị khối lượng một cách liên tục
        sharedPreferences = getSharedPreferences("dataInputWeightScale", MODE_PRIVATE);
        save = findViewById(R.id.Save);
        Confirm = findViewById(R.id.Confirm);
        tvWO = findViewById(R.id.tvWO);
        tvStandarData = findViewById(R.id.t42);
        ActualWeight = findViewById(R.id.ActualWeight);
        ActualAmount = findViewById(R.id.ActualAmount);
        Squareallmaterial = findViewById(R.id.Squareallmaterial);
        DefectiveAmount = findViewById(R.id.DefectiveAmount);
        ProductYieldRate = findViewById(R.id.ProductYieldRate);
        tvActual = findViewById(R.id.tvActual);
        tvDefective = findViewById(R.id.tvDefective);
        spinner_WO = findViewById(R.id.spinner_WO);
        checkpallet = findViewById(R.id.checkpallet);

        dataStandardAmountNum = Integer.parseInt(sharedPreferences.getString("StandardAmount","0"));
        dataStandardWeightNum = Double.parseDouble(sharedPreferences.getString("StandardWeight","0.00"));
        dataPalletWeightNum = Double.parseDouble(sharedPreferences.getString("PalletWeight","0.00"));
        dataSquarePerPcsNum = Double.parseDouble(sharedPreferences.getString("SquarePerPcs","0.00"));

        final List<String> list = new ArrayList<>();
        list.add("W0000000001");
        list.add("W0000000002");
        list.add("W0000000003");
        list.add("W0000000004");
        list.add("W0000000005");


        ArrayAdapter<String> adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item,list);
        adapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice);
        spinner_WO.setAdapter(adapter);
        spinner_WO.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.parseColor("#D2D3D5"));
                tvWO.setText(list.get(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        tvWO.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogWO();
                Toast.makeText(WeightScaleActivity.this, "" + SerialReadThread.data, Toast.LENGTH_SHORT).show();
            }
        });
        tvStandarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogStandarData();
            }
        });
        Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double tam = 0.00;
                if(SerialReadThread.data != null) {

                    dataActualWeightnum = Double.parseDouble(SerialReadThread.data);
                    ActualWeight.setText(dataActualWeightnum + " kg");
                    if (checkpallet.isChecked()) {
                        tam = dataPalletWeightNum;
                        if (dataActualWeightnum >= tam) {
                            KLmotSP = (dataStandardWeightNum) / dataStandardAmountNum;//(dataStandardWeightNum - dataPalletWeightNum) / dataStandardAmountNum;
                            // KL can
                            // sl hang dat
                            dataActualAmountnum = Math.round(Math.round((dataActualWeightnum - tam) / KLmotSP));
                            ActualAmount.setText(dataActualAmountnum + " pcs");
                            // dien tich
                            double aaa = Math.round(dataSquarePerPcsNum * dataActualAmountnum * 100.0) / 100.0;
                            Squareallmaterial.setText(aaa + " cm2");
                        } else {
                            Toast.makeText(WeightScaleActivity.this, "Data uncorrected. Please check again!!!", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        KLmotSP = (dataStandardWeightNum) / dataStandardAmountNum;//(dataStandardWeightNum - dataPalletWeightNum) / dataStandardAmountNum;
                        // sl hang dat
                        dataActualAmountnum = Math.round(Math.round(dataActualWeightnum / KLmotSP));
                        ActualAmount.setText(dataActualAmountnum + " pcs");
                        // dien tich
                        double aaa = Math.round(dataSquarePerPcsNum * dataActualAmountnum * 100.0) / 100.0;
                        Squareallmaterial.setText(aaa + " cm2");
                    }
                }else {
                    Toast.makeText(WeightScaleActivity.this, "Please connect weight scale", Toast.LENGTH_SHORT).show();
                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intext();
            }
        });
        tvActual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intextActual();
            }
        });
        tvDefective.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intextDefective();
            }
        });

//        ArrayList<String> list = new ArrayList<String>();
//        for(int i = 0; i < FUNCTIONS.length; i++)
//        {
//            list.add(FUNCTIONS[i]);
//        }

        AssetManager assetMgr = getAssets();
        try {
            demoFiles = new InputStream[4];
            demoFiles[0] = assetMgr.open("demo0_203dpi.txt");
            demoFiles[1] = assetMgr.open("demo2_203dpi.txt");
            demoFiles[2] = assetMgr.open("demo3_203dpi.txt");
            demoFiles[3] = assetMgr.open("demo4_203dpi.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

       // ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        // mListView = (ListView) findViewById(android.R.id.list);
        // mListView.setAdapter(adapter);
        // mListView.setEnabled(false);

        mBixolonLabelPrinter = new BixolonLabelPrinter(this, mHandler, Looper.getMainLooper());

        final int ANDROID_NOUGAT = 24;
        if(Build.VERSION.SDK_INT >= ANDROID_NOUGAT)
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_weight_scale;
    }
    private void DialogWO(){
        final Dialog dialog = new Dialog(WeightScaleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_thongtin_wo_weight);
        dialog.show();
    }
    private void DialogStandarData(){
        final Dialog dialog = new Dialog(WeightScaleActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_nhap_wo_weight);
        dialog.setCanceledOnTouchOutside(false);
        StandardAmount = dialog.findViewById(R.id.StandardAmount);
        StandardWeight = dialog.findViewById(R.id.StandardWeight);
        PalletWeight = dialog.findViewById(R.id.PalletWeight);
        SquarePerPcs = dialog.findViewById(R.id.Squareperpcs);
        SaveDialogStandarData = dialog.findViewById(R.id.SaveDialogStandarData);

        // set du lieu tu bo nho
        StandardAmount.setText(dataStandardAmountNum + "");
        StandardWeight.setText(dataStandardWeightNum + "");
        PalletWeight.setText(dataPalletWeightNum + "");
        SquarePerPcs.setText(dataSquarePerPcsNum + "");
        // set du lieu tu bo nho

        SaveDialogStandarData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(StandardAmount.getText().toString().length() == 0||StandardWeight.getText().toString().length() == 0 ||
                        PalletWeight.getText().toString().length() == 0||SquarePerPcs.getText().toString().length() == 0){
                    Toast.makeText(WeightScaleActivity.this, "Please fill out the form!", Toast.LENGTH_SHORT).show();

                }else {
                    if (Double.parseDouble(PalletWeight.getText().toString()) > Double.parseDouble(StandardWeight.getText().toString())){
                        Toast.makeText(WeightScaleActivity.this, "Data incorrect, please check again!", Toast.LENGTH_SHORT).show();
                    } else {
                        dataStandardAmountNum = Integer.parseInt(StandardAmount.getText().toString());
                        dataStandardWeightNum = Double.parseDouble(StandardWeight.getText().toString());
                        dataPalletWeightNum = Double.parseDouble(PalletWeight.getText().toString());
                        dataSquarePerPcsNum = Double.parseDouble(SquarePerPcs.getText().toString());
                        // luu du lieu vao bo nho
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString("StandardAmount", dataStandardAmountNum + "");
                        editor.putString("StandardWeight", dataStandardWeightNum + "");
                        editor.putString("PalletWeight", dataPalletWeightNum + "");
                        editor.putString("SquarePerPcs", dataSquarePerPcsNum + "");
                        editor.commit();
                        // luu du lieu vao bo nho
                        dialog.dismiss();
                    }
                }
            }
        });
        dialog.show();
    }


    private void callPrint() {


                final Dialog dialog = new Dialog(WeightScaleActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_inventory_mtcode_weight);



                photoEditorView = dialog.findViewById(R.id.image_preview);
                photoEditor = new PhotoEditor.Builder(this,photoEditorView).setPinchTextScalable(true).
                        setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(),"emojione-android.ttf")).build();
                Bitmap originalBitmap;
                originalBitmap = BitmapUtils.getBitmapFromAssets(this,pictureName,400,290);
                photoEditorView.getSource().setImageBitmap(originalBitmap);


                String val = "LOT-12045-ATL123599855541895524725178954700";

                final ImageView imgQr = dialog.findViewById(R.id.img_qr);
                TextView tvMtCode = dialog.findViewById(R.id.tv_mtcode);
                TextView tvWO = dialog.findViewById(R.id.tv_WO);
                BitMatrix bitMatrix = null;
                if (val.length() == 0 || val == "null") {
                    Toast.makeText(WeightScaleActivity.this, "No QR code!", Toast.LENGTH_SHORT).show();
                } else {
                    QRCodeWriter qrCodeWriter = new QRCodeWriter();
                    try {
                        bitMatrix = qrCodeWriter.encode(val, BarcodeFormat.QR_CODE, 200, 200);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }

                    int height = bitMatrix.getHeight();
                    int width = bitMatrix.getWidth();
                    final Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_4444);
                    for (int x = 0; x < width; x++) {
                        for (int y = 0; y < height; y++) {
                            bmp.setPixel(x, y, bitMatrix.get(x, y) ? Color.BLACK : Color.WHITE);
                        }
                    }
                    imgQr.setImageBitmap(bmp);
                    if (val.length() <40){
                        tvMtCode.setText(val.substring(0,20)+ "\n"+ val.substring(20,val.length()));
                    }else {
                        tvMtCode.setText(val.substring(0,20)+ "\n"+ val.substring(20,40)+"\n"+ val.substring(40,val.length()));
                    }

                    tvWO.setText("WO: 123456789");
                    dialog.show();

                    final Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                @Override
                                public void onBitmapReady(Bitmap saveBitmap) {
                                    ////////////////
                                    aaaaa = Bitmap.createScaledBitmap(saveBitmap, 400, 290, false);
                                    WeightScaleActivity.mBixolonLabelPrinter.setPrintingType(BixolonLabelPrinter.PRINTING_TYPE_THERMAL_TRANSFER);
                                    WeightScaleActivity.mBixolonLabelPrinter.setOrientation(BixolonLabelPrinter.ORIENTATION_BOTTOM_TO_TOP);
                                    WeightScaleActivity.mBixolonLabelPrinter.setWidth(500);
                                    WeightScaleActivity.mBixolonLabelPrinter.setMargin(0, 0);
                                    WeightScaleActivity.mBixolonLabelPrinter.drawCompressionImage(aaaaa, 0, 0, 500, 70
                                            , true);
                                    WeightScaleActivity.mBixolonLabelPrinter.print(1, 1);
                                    //////////////////+

                                }

                                @Override
                                public void onFailure(Exception e) {

                                }
                            });
                        }
                    }, 2000);
        }
    }

    private void intext() {
        //
        //
         callPrint();
        AlertDialog.Builder builder = new AlertDialog.Builder(WeightScaleActivity.this);
        builder.setTitle("Input Defective Amount");
        View viewInflated = LayoutInflater.from(WeightScaleActivity.this).inflate(R.layout.number_input_layout, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bbCode = input.getText().toString();
                if (bbCode.length() ==0){
                    Toast.makeText(WeightScaleActivity.this, "Please input data!", Toast.LENGTH_SHORT).show();
                }else {
                    if (Integer.parseInt(bbCode)>dataActualAmountnum){
                        Toast.makeText(WeightScaleActivity.this, "Defective Amount < Actual Amount" + " \n Please check input data!", Toast.LENGTH_SHORT).show();
                    }else {
                        DefectiveAmount.setText(bbCode);
                        dataDefectiveAmountnum = Integer.parseInt(bbCode);
                        // phan tram
                        PhanTram = Math.round(((dataActualAmountnum - dataDefectiveAmountnum)/(dataActualAmountnum * 1.00))*100*100.0)/100.0;
                        //PhanTram = Math.round((((dataActualAmountnum - dataDefectiveAmountnum)* 100.0 /100.0)/dataActualAmountnum)* 100);
                        ProductYieldRate.setText(PhanTram + " %");
                        Actual = Actual + dataActualAmountnum - dataDefectiveAmountnum;
                        tvActual.setText(Actual+"");
                        Defective = Defective + dataDefectiveAmountnum;
                        tvDefective.setText(Defective+"");
                        dialog.cancel();
                    }
                }
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
    private void intextActual() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeightScaleActivity.this);
        builder.setTitle("Input Actual");
        View viewInflated = LayoutInflater.from(WeightScaleActivity.this).inflate(R.layout.number_input_layout, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bbCode = input.getText().toString();
                if (bbCode.length() ==0){
                    Toast.makeText(WeightScaleActivity.this, "Please input data!", Toast.LENGTH_SHORT).show();
                }else {
                    Actual = Integer.parseInt(bbCode);
                    tvActual.setText(Actual+"");
                }
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
    private void intextDefective() {
        AlertDialog.Builder builder = new AlertDialog.Builder(WeightScaleActivity.this);
        builder.setTitle("Input Actual");
        View viewInflated = LayoutInflater.from(WeightScaleActivity.this).inflate(R.layout.number_input_layout, null);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String bbCode = input.getText().toString();
                if (bbCode.length() ==0){
                    Toast.makeText(WeightScaleActivity.this, "Please input data!", Toast.LENGTH_SHORT).show();
                }else {
                    Defective = Integer.parseInt(bbCode);
                    tvDefective.setText(Defective+"");
                }
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
    public void onResume() {
        super.onResume();

        if(!tryedAutoConnect) {
            isConnectedPrinter();
        }
    }

    @Override
    public void onDestroy()
    {
        try
        {
            unregisterReceiver(mUsbReceiver);
        }
        catch(IllegalArgumentException e)
        {
            e.printStackTrace();
        }
        mBixolonLabelPrinter.disconnect();
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    @SuppressWarnings("deprecation")
    public void scanLeDevice(final boolean bEnable)
    {
        if(bEnable)
        {
            if(Build.VERSION.SDK_INT >= 21){
//                try {
//                    //setScanCallback();
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                }
            }
            // Stops scanning after a pre-defined scan period.
            m_hHandler.postDelayed(new Runnable()
            {
                @Override
                public void run()
                {
                    if(Build.VERSION.SDK_INT < 21)
                    {
                        m_BluetoothAdapter.stopLeScan(m_LeScanCallback);
                    }
                    else
                    {
                        mLEScanner.stopScan(mScanCallback);
                    }
                }
            }, 10000);

            if(Build.VERSION.SDK_INT < 21)
            {
                m_BluetoothAdapter.startLeScan(m_LeScanCallback);
            }
            else
            {
                mLEScanner.startScan(filters, settings, mScanCallback);
            }
        }
        else
        {
            if(Build.VERSION.SDK_INT < 21)
            {
                m_BluetoothAdapter.stopLeScan(m_LeScanCallback);
            }
            else
            {
                mLEScanner.stopScan(mScanCallback);
            }
        }
    }



    public BluetoothAdapter.LeScanCallback m_LeScanCallback = new BluetoothAdapter.LeScanCallback()
    {
        @Override
        public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord)
        {
            runOnUiThread(new Runnable()
            {
                @Override
                public void run()
                {
                    if(!m_LeDevices.contains(device))
                    {
                        m_LeDevices.add(device);
                        adapter.add(device.getName() + "\n" + device.getAddress());
                    }
                }
            });
        }
    };

    Intent intent;


    private final void setStatus(int resId)
    {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(resId);
    }

    private void printDemo(){
        AssetManager assetMgr = getAssets();
        try {
            demoFiles = new InputStream[4];
            demoFiles[0] = assetMgr.open("demo0_203dpi.txt");
            demoFiles[1] = assetMgr.open("demo2_203dpi.txt");
            demoFiles[2] = assetMgr.open("demo3_203dpi.txt");
            demoFiles[3] = assetMgr.open("demo4_203dpi.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        for(int a=0;a<demoFiles.length;a++){
            int i;
            StringBuffer buffer = new StringBuffer();
            byte[] b = new byte[4096];
            try {
                while( (i = demoFiles[a].read(b)) != -1){
                    buffer.append(new String(b, 0, i));
                }

                String str = buffer.toString();

                mBixolonLabelPrinter.executeDirectIo(str, false, 0);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }


    private void printSampleReceipt()
    {
        mBixolonLabelPrinter.drawText("75-C51", 50, 1200, BixolonLabelPrinter.FONT_SIZE_24, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, true, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???", 35, 900, BixolonLabelPrinter.FONT_SIZE_KOREAN6, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???", 85, 900, BixolonLabelPrinter.FONT_SIZE_KOREAN6, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("????? 1026-1287-1927", 160, 1250, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("????   2017/12/31", 190, 1250, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.draw1dBarcode("123456789012", 160, 900, BixolonLabelPrinter.BARCODE_CODE128, 2, 10, 60, BixolonLabelPrinter.ROTATION_270_DEGREES, BixolonLabelPrinter.HRI_NOT_PRINTED, 0);

        mBixolonLabelPrinter.drawText("???                          010-1234-5678", 230, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("?? ??? ??? 3912?? ?????????", 260, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("??? 204 / 702?", 290, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("??? ???                     02-468-4317", 330, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("??? ??? ??? 361-6?? ???? 2?", 360, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("??", 410, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, true, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???? GX-100", 440, 1200, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, true, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("31-C1", 30, 600, BixolonLabelPrinter.FONT_SIZE_24, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, true, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???", 80, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN6, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???", 120, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN6, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("???", 300, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN1, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("010-1234-5678", 330, 550, BixolonLabelPrinter.FONT_SIZE_KOREAN1, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("??? 3912?? ???", 400, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN1, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("????????? 204", 430, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN1, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("702?", 460, 600, BixolonLabelPrinter.FONT_SIZE_KOREAN1, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("????? 1026-1287-1927", 50, 400, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???? 2017/12/31", 80, 400, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("??? 010-1234-5678", 130, 350, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("??? 3912?? ???", 160, 400, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("????????? 204/702?", 190, 400, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawText("??? ???", 220, 350, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("02-648-4317", 250, 300, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);
        mBixolonLabelPrinter.drawText("???? GX-100", 280, 380, BixolonLabelPrinter.FONT_SIZE_KOREAN2, 1, 1, 0, BixolonLabelPrinter.ROTATION_270_DEGREES, false, false, BixolonLabelPrinter.TEXT_ALIGNMENT_NONE);

        mBixolonLabelPrinter.drawQrCode("www.bixolon.com", 350, 400, BixolonLabelPrinter.QR_CODE_MODEL2, BixolonLabelPrinter.ECC_LEVEL_15, 4, BixolonLabelPrinter.ROTATION_270_DEGREES);

        mBixolonLabelPrinter.print(1, 1);
    }

    private final void setStatus(CharSequence subtitle)
    {
        final ActionBar actionBar = getActionBar();
        actionBar.setSubtitle(subtitle);
    }

    @SuppressLint("HandlerLeak")
    private void dispatchMessage(Message msg)
    {
        switch (msg.arg1)
        {
            case BixolonLabelPrinter.PROCESS_GET_STATUS:
                byte[] report = (byte[]) msg.obj;
                StringBuffer buffer = new StringBuffer();
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY) == BixolonLabelPrinter.STATUS_1ST_BYTE_PAPER_EMPTY)
                {
                    buffer.append("Paper Empty.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN) == BixolonLabelPrinter.STATUS_1ST_BYTE_COVER_OPEN)
                {
                    buffer.append("Cover open.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED) == BixolonLabelPrinter.STATUS_1ST_BYTE_CUTTER_JAMMED)
                {
                    buffer.append("Cutter jammed.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT) == BixolonLabelPrinter.STATUS_1ST_BYTE_TPH_OVERHEAT)
                {
                    buffer.append("TPH(thermal head) overheat.\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE) == BixolonLabelPrinter.STATUS_1ST_BYTE_AUTO_SENSING_FAILURE)
                {
                    buffer.append("Gap detection error. (Auto-sensing failure)\n");
                }
                if((report[0] & BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR) == BixolonLabelPrinter.STATUS_1ST_BYTE_RIBBON_END_ERROR)
                {
                    buffer.append("Ribbon end error.\n");
                }

                if(report.length == 2)
                {
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_BUILDING_IN_IMAGE_BUFFER)
                    {
                        buffer.append("On building label to be printed in image buffer.\n");
                    }
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER) == BixolonLabelPrinter.STATUS_2ND_BYTE_PRINTING_IN_IMAGE_BUFFER)
                    {
                        buffer.append("On printing label in image buffer.\n");
                    }
                    if((report[1] & BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT) == BixolonLabelPrinter.STATUS_2ND_BYTE_PAUSED_IN_PEELER_UNIT)
                    {
                        buffer.append("Issued label is paused in peeler unit.\n");
                    }
                }
                if(buffer.length() == 0)
                {
                    buffer.append("No error");
                }
                Toast.makeText(getApplicationContext(), buffer.toString(), Toast.LENGTH_SHORT).show();
                break;

            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_MODEL_NAME:
            case BixolonLabelPrinter.PROCESS_GET_INFORMATION_FIRMWARE_VERSION:
            case BixolonLabelPrinter.PROCESS_EXECUTE_DIRECT_IO:
                Toast.makeText(getApplicationContext(), (String) msg.obj, Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private final Handler mHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch (msg.what)
            {
                case BixolonLabelPrinter.MESSAGE_STATE_CHANGE:
                    switch (msg.arg1)
                    {
                        case BixolonLabelPrinter.STATE_CONNECTED:
                            //setStatus(getString(R.string.title_connected_to, mConnectedDeviceName));
                            // mListView.setEnabled(true);
                            mIsConnected = true;
                            invalidateOptionsMenu();
                            WeightScaleActivity.mBixolonLabelPrinter.setOrientation(BixolonLabelPrinter.ORIENTATION_TOP_TO_BOTTOM);
                            break;

                        case BixolonLabelPrinter.STATE_CONNECTING:
                            //setStatus(R.string.title_connecting);
                            break;

                        case BixolonLabelPrinter.STATE_NONE:
                            // setStatus(R.string.title_not_connected);
                            // mListView.setEnabled(false);
                            mIsConnected = false;
                            invalidateOptionsMenu();
                            break;
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_READ:
                    WeightScaleActivity.this.dispatchMessage(msg);
                    break;

                case BixolonLabelPrinter.MESSAGE_DEVICE_NAME:
                    mConnectedDeviceName = msg.getData().getString(BixolonLabelPrinter.DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), mConnectedDeviceName, Toast.LENGTH_LONG).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_TOAST:
                    // mListView.setEnabled(false);
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonLabelPrinter.TOAST), Toast.LENGTH_SHORT).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_LOG:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(BixolonLabelPrinter.LOG), Toast.LENGTH_SHORT).show();
                    break;

                case BixolonLabelPrinter.MESSAGE_BLUETOOTH_DEVICE_SET:
                    if(msg.obj == null)
                    {
                        Toast.makeText(getApplicationContext(), "No paired device", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // DialogManager.showBluetoothDialog(MainActivity.this, (Set<BluetoothDevice>) msg.obj);
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_USB_DEVICE_SET:
                    if(msg.obj == null)
                    {
                        Toast.makeText(getApplicationContext(), "No connected device", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        // DialogManager.showUsbDialog(MainActivity.this, (Set<UsbDevice>) msg.obj, mUsbReceiver);
                    }
                    break;

                case BixolonLabelPrinter.MESSAGE_NETWORK_DEVICE_SET:
                    if(msg.obj == null)
                    {
                        Toast.makeText(getApplicationContext(), "No connectable device", Toast.LENGTH_SHORT).show();
                    }
                    //DialogManager.showNetworkDialog(MainActivity.this, (Set<String>) msg.obj);
                    break;

            }
        }
    };

    private BroadcastReceiver mUsbReceiver = new BroadcastReceiver()
    {

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();

            if(UsbManager.ACTION_USB_DEVICE_ATTACHED.equals(action))
            {
                mBixolonLabelPrinter.connect();
                Toast.makeText(getApplicationContext(), "Found USB device", Toast.LENGTH_SHORT).show();
            }
            else if(UsbManager.ACTION_USB_DEVICE_DETACHED.equals(action))
            {
                mBixolonLabelPrinter.disconnect();
                Toast.makeText(getApplicationContext(), "USB device removed", Toast.LENGTH_SHORT).show();
            } else if(ACTION_USB_PERMISSION.equals(action)){
                mBixolonLabelPrinter.connect(device);
                device = null;
            }

        }
    };

    private void isConnectedPrinter(){
        try {
            usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
            Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
            mPermissionIntent = PendingIntent.getBroadcast(this, 0,
                    new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_UPDATE_CURRENT);
            while (deviceIterator.hasNext()) {
                UsbDevice d = deviceIterator.next();
                if (d != null) {
                    for(int i=0;i<d.getInterfaceCount();i++){
                        UsbInterface usbInterface = d.getInterface(i);
                        if(usbInterface.getInterfaceClass() == 7 && usbInterface.getInterfaceSubclass() == 1 && usbInterface.getInterfaceProtocol() == 2){
                            device = d;
                            break;
                        }
                    }
                }
            }

            IntentFilter filter = new IntentFilter(
                    ACTION_USB_PERMISSION);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
            filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
            filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            filter.addAction(BluetoothAdapter.ACTION_CONNECTION_STATE_CHANGED);
            registerReceiver(mUsbReceiver, filter);
            if (device != null) {
                usbManager.requestPermission(device, mPermissionIntent);
                tryedAutoConnect = true;
            } else {
                Log.e("Exception", "Printer not found");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

    }


}

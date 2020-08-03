package com.licheedev.MMSSNK.Actual;

import android.app.Activity;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.print.sdk.PrinterConstants;
import com.android.print.sdk.PrinterInstance;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;
import com.licheedev.MMSSNK.activity.base.BaseActivity;
import com.licheedev.MMSSNK.comn.SerialPortManager;
import com.licheedev.MMSSNK.comn.SerialReadThread;
import com.licheedev.MMSSNK.print_bluetooth.BitmapUtils;
import com.licheedev.MMSSNK.print_bluetooth.BluetoothOperation;
import com.licheedev.MMSSNK.print_bluetooth.IPrinterOpertion;
import com.licheedev.MMSSNK.MainLayout;
import com.licheedev.MMSSNK.Product.ProductMaster;
import com.licheedev.MMSSNK.Product.ProductMasterAdaptor;
import com.licheedev.MMSSNK.R;
import com.licheedev.MMSSNK.utils.AidlUtil;
import com.licheedev.MMSSNK.utils.BluetoothUtil;
import com.licheedev.MMSSNK.utils.ESCUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import ja.burhanrashid52.photoeditor.OnSaveBitmap;
import ja.burhanrashid52.photoeditor.PhotoEditor;
import ja.burhanrashid52.photoeditor.PhotoEditorView;
import woyou.aidlservice.jiuiv5.IWoyouService;

public class LotCompositeActivity extends BaseActivity {

    public static final int CONNECT_DEVICE = 1;
    public static final int ENABLE_BT = 2;
    TextView tv_bobbinCode, tv_LotCode;
    ImageView btn_scan_BobbinCode;
    TextView buttonMapping, buttonPrint;
    ArrayList<ProductMaster> productMasterArrayList;
    ProductMasterAdaptor productMasterAdaptor;

    public static String date_chon,Ma_QC_Code,olddnochon;

    TextView tvWO, tvRouting, tvProcess, tvDate;
    String WOchon, Datechon, GroupQtynum, Linechon, ProcessnameChon,line_nochon,process_nochon;
    EditText GroupQty;
    TextView buttonCreating;
    ListView lvDanhSanhproduct;
    private int vitri = -1;
    int kiemtra = 0;
    TextView chonchedo;

    String API_MAPPING = "http://snk.autonsi.com/Lot/maping_code_bb?bb_no="; //BB000000001&wmtid=1123";
    String API_MAPPING1 = "&wmtid=";

    private boolean mOpened = false;
    TextView weight_current,num_current,tvproductCode;

    private static boolean isConnected;
    private IPrinterOpertion myOpertion;
    private PrinterInstance mPrinter;
    private Context mContext;
    private ProgressDialog dialog;
    private boolean showUSB;
    private IWoyouService woyouService;
    SharedPreferences sharedPreferences;

    String DCMac;
    private static final String SERVICE＿PACKAGE = "woyou.aidlservice.jiuiv5";
    private static final String SERVICE＿ACTION = "woyou.aidlservice.jiuiv5.IWoyouService";
    private Context context;
    String checkUnit = "";
    int chon_kieu_in = 1;

    PhotoEditor photoEditor;
    PhotoEditorView photoEditorView;
    public static final String pictureName = "hinh.jpg";
    Bitmap aaaaa;
    CoordinatorLayout coordinatorLayout;
    String bom_no, ProcessChon;
    String index, style_no, level;
    int KTvonglap = -1;
    TextView qty_standa,weight_standa,weight1_standa;
    
    int numQty_standa=1;
    double numWeight=3.33, numWeight1=numWeight/numQty_standa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_lot_composite);
        showUSB = Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1;
        mContext = this;
        mOpened = SerialPortManager.instance().open("/dev/ttyS0","9600") != null;//////// hiển thị khối lượng một cách liên tục

        // khoa tat man hinh+ them ham pha duoi

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

///ham dialog ket noi vs bluetooth
        InitView();
        //ham kiem tra device ( may in hoac cai khac)
        String namedevice = android.os.Build.MODEL;
        if (namedevice.equals("D1s")){
            checkUnit = "Bluetooth";
        }else {
            checkUnit = AidlUtil.getInstance().initPrinter();
        }



        Intent resultIntent = new Intent(this, MainLayout.class);
// Create the TaskStackBuilder and add the intent, which inflates the back stack
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addNextIntentWithParentStack(resultIntent);
// Get the PendingIntent containing the entire back stack
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        tvproductCode = findViewById(R.id.tvproductCode);
        tvproductCode.setText(ActualFragment.Productcode);
        btn_scan_BobbinCode = findViewById(R.id.btn_scan_BobbinCode);
        //btn_scan_LotCode = view.findViewById(R.id.btn_scan_LotCode);
        tv_bobbinCode = findViewById(R.id.tv_bobbinCode);
        tv_LotCode = findViewById(R.id.tv_lotCode);
        tvWO = findViewById(R.id.tvWO);
        tvRouting = findViewById(R.id.tvRouting);
        tvProcess = findViewById(R.id.tvProcess);
        tvDate = findViewById(R.id.tvDate);
        GroupQty = findViewById(R.id.GroupQty);
        // QRQty = findViewById(R.id.QRQty);
        buttonCreating = findViewById(R.id.buttonCreating);
        lvDanhSanhproduct = findViewById(R.id.lvDanhSanhproduct);
        buttonMapping = findViewById(R.id.buttonMapping);
        buttonPrint = findViewById(R.id.buttonPrint);

        Intent intent = getIntent();
        WOchon = ActualFragment.WOchon;
        ProcessChon = ActualFragment.processchon;
        ProcessnameChon = ActualFragment.processnamechon;
        Linechon = ActualFragment.Linechon;
        Datechon = ActualFragment.datechon;
        process_nochon = ActualFragment.process_nochon;
        line_nochon = ActualFragment.line_nochon;
        olddnochon = ActualFragment.olddnochon;



        date_chon = Datechon;



        level = intent.getStringExtra("level");
        setTitle("Lot Composite");
        if (WOchon == null) {
            WOchon = "";
        }
        if (ProcessnameChon == null) {
            ProcessnameChon = "";
        }
        if (ProcessChon == null) {
            ProcessChon = "";
        }
        if (Linechon == null) {
            Linechon = "";
        }
        if (Datechon == null) {
            Datechon = "";
        }

        tvWO.setText(convert_view(WOchon));
        tvRouting.setText(Linechon);
        tvProcess.setText(ProcessChon);
        tvDate.setText(Datechon.substring(0, 4) + "-" + Datechon.substring(4, 6) + "-" + Datechon.substring(6, 8));

        bom_no = ActualFragment.bom_no;
        index = ActualFragment.index;
        style_no = ActualFragment.style_no;
        getdata();

        /////////////////
//        new docJSONdata_them().execute("http://snk.autonsi.com/Lot/return_mp_mt_line?wo="+ WOchon);
//        Log.d("AAAA","http://snk.autonsi.com/Lot/return_mp_mt_line?wo="+ WOchon);


        ////////////////

        buttonMapping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_bobbinCode.getText().toString().length() > 0 && vitri >= 0) {
                    callMapping();
                } else {
                    Toast.makeText(LotCompositeActivity.this, "Please enter Bobbin Code and select Lot Code", Toast.LENGTH_SHORT).show();
                }
            }
        });

        tv_bobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intext("tv_bobbinCode");
            }
        });

        buttonPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(mContext, "AAAAAAA" + checkUnit, Toast.LENGTH_SHORT).show();
                callPrint();
                //PrintUtils.printBarCode(mPrinter,"QQQQQQQ");

            }
        });

        btn_scan_BobbinCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQRScanner();
            }
        });


        buttonCreating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_LotCode.setText("");
               // GroupQtynum = GroupQty.getText().toString().trim();
                //QRQtynum = QRQty.getText().toString().trim();
                productMasterArrayList = new ArrayList<ProductMaster>();
                productMasterAdaptor = new ProductMasterAdaptor(
                        LotCompositeActivity.this, R.layout.item_product, productMasterArrayList);
                lvDanhSanhproduct.setAdapter(productMasterAdaptor);

                if (ProcessChon.length() == 0 || Datechon.length() == 0 || WOchon.length() == 0 || Linechon.length() == 0) {
                    Toast.makeText(LotCompositeActivity.this, "Data incorrect!!!", Toast.LENGTH_SHORT).show();
                } else {
              //      if (GroupQty.length() != 0) {
                        new docJSONtaomoi().execute("http://snk.autonsi.com/Lot/Creat_row?wo_no=" +
                                WOchon +
                                "&gr_qty=" +
                                0 + //GroupQtynum
                                "&prounit_cd=" +
                                ProcessChon +
                                "&date=" +
                                Datechon +
                                "&line_no=" +
                                Linechon +
                                "&pr_nm=" +
                                ProcessnameChon +
                                "&index=" +
                                index +
                                "&style_no=" +
                                style_no + "&bom_no=" + bom_no + "&level=" + level);
                        Log.d("taomoi", "http://snk.autonsi.com/Lot/Creat_row?wo_no=" +
                                WOchon +
                                "&gr_qty=" +
                                0 + //GroupQtynum
                                "&prounit_cd=" +
                                ProcessChon +
                                "&date=" +
                                Datechon +
                                "&line_no=" +
                                Linechon +
                                "&pr_nm=" +
                                ProcessnameChon +
                                "&index=" +
                                index +
                                "&style_no=" +
                                style_no + "&bom_no=" + bom_no + "&level=" + level);
//                    } else {
//                        Toast.makeText(LotCompositeActivity.this, "Insert Group Qty", Toast.LENGTH_SHORT).show();
//                        //getdata();
//                    }
                }
            }
        });

        lvDanhSanhproduct.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                vitri = i;
                tv_LotCode.setText(productMasterArrayList.get(i).getMt_qrcode());
                productMasterAdaptor.notifyDataSetChanged();
            }
        });

    }
    @Override
    protected int getLayoutId() {
        return R.layout.activity_lot_composite;
    }
    //khoa tat man hinh
    @Override
    protected void onDestroy() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }
    private String convert_view(String txt){
        String string = "";
        if (txt.equals("null")){

        }else {
            string = txt.substring(0, txt.indexOf("0")) + Integer.parseInt(txt.substring(txt.indexOf("0"), txt.length()));
        }
        return string;
    }

//    public void Delete_row(View view) {
//
//
//        int position = (int) view.getTag();
//        String url = "http://snk.autonsi.com/ActualWO/Xoa_mt_pp_composite?id="+ productMasterArrayList.get(position).getWmtid();
//        new jsonDelete().execute(url);
//        Log.d("jsonDelete",url);
//    }
//
//    class jsonDelete extends AsyncTask<String, Integer, String> {
//        @Override
//        protected String doInBackground(String... strings) {
//            return NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            String TL = s.trim();
//            if (TL.equals("true")){
//                Toast.makeText(LotCompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
//                getdata();
//            }else {
//                Toast.makeText(LotCompositeActivity.this, "Error", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }

    private void intext(final String key_location) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LotCompositeActivity.this);
        builder.setTitle("Tray Code");
// I'm using fragment here so I'm using getView() to provide ViewGroup
// but you can provide here any other instance of ViewGroup from your Fragment / Activity
        View viewInflated = LayoutInflater.from(LotCompositeActivity.this).inflate(R.layout.text_input_layout, null);
// Set up the input
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
// Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        builder.setView(viewInflated);
// Set up the buttons
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (key_location == "tv_bobbinCode") {
                    String bbCode = input.getText().toString();
                    tv_bobbinCode.setText(bbCode);
                } else {
                }
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


//    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            String action = intent.getAction();
//            BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
//
//            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
//                Toast.makeText(LotCompositeActivity.this, "AAAAA", Toast.LENGTH_SHORT).show();
//            }
//            else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
//                Toast.makeText(LotCompositeActivity.this, "CCCCC", Toast.LENGTH_SHORT).show();
//            }
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
//                Toast.makeText(LotCompositeActivity.this, "DDDDD", Toast.LENGTH_SHORT).show();
//            }
//            else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
//                Toast.makeText(LotCompositeActivity.this, "EEEE", Toast.LENGTH_SHORT).show();
//                ///ddiisss
//            } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
//                Toast.makeText(LotCompositeActivity.this, "BBBBB", Toast.LENGTH_SHORT).show();
//                sharedPreferences = getSharedPreferences("DCMac", MODE_PRIVATE);
//                SharedPreferences.Editor editor = sharedPreferences.edit();
//                editor.putString("DC",DCMac);
//                editor.commit();
//                //conec
//            }
//        }
//    };

    private void callPrint() {


//        IntentFilter filter = new IntentFilter();
//        filter.addAction(BluetoothDevice.ACTION_ACL_CONNECTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
//        filter.addAction(BluetoothDevice.ACTION_ACL_DISCONNECTED);
//        this.registerReceiver(mReceiver, filter);


        if (vitri < 0) {
            Toast.makeText(LotCompositeActivity.this, "Please select Lot code print", Toast.LENGTH_SHORT).show();
        } else {
            String val = productMasterArrayList.get(vitri).getMt_qrcode();

            if (val == "") {
                Toast.makeText(LotCompositeActivity.this, "Please select Lot code print", Toast.LENGTH_SHORT).show();
            } else {

                final Dialog dialog = new Dialog(LotCompositeActivity.this);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.popup_inventory_mtcode_qr);


                photoEditorView = dialog.findViewById(R.id.image_preview);
                photoEditor = new PhotoEditor.Builder(this, photoEditorView).setPinchTextScalable(true).
                        setDefaultEmojiTypeface(Typeface.createFromAsset(getAssets(), "emojione-android.ttf")).build();
                Bitmap originalBitmap;
                originalBitmap = BitmapUtils.getBitmapFromAssets(this, pictureName, 320, 280);//380 400
                photoEditorView.getSource().setImageBitmap(originalBitmap);


                final ImageView imgQr = dialog.findViewById(R.id.img_qr);
                TextView tvMtCode = dialog.findViewById(R.id.tv_mtcode);
                TextView tvWO = dialog.findViewById(R.id.tv_WO);
                BitMatrix bitMatrix = null;
                if (val.length() == 0 || val == "null") {
                    Toast.makeText(LotCompositeActivity.this, "No QR code!", Toast.LENGTH_SHORT).show();
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
                    tvMtCode.setText(val);
                    tvWO.setText("WO: " + WOchon);
                    dialog.show();

//                    PrintUtils.printTextLeft("WO: "+WOchon ,mPrinter);
//                    PrintUtils.printImage(mPrinter,bmp);
//                    PrintUtils.printText(val,mPrinter);
//                    PrintUtils.printkhoangtrang(2,mPrinter);


                    byte[] send;
                    send = ESCUtil.alignLeft();

                    if (checkUnit.equals("Location") && chon_kieu_in == 1) {
                        AidlUtil.getInstance().sendRawData(send);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                    @Override
                                    public void onBitmapReady(Bitmap saveBitmap) {
                                        ////////////////
                                        aaaaa = Bitmap.createScaledBitmap(saveBitmap, 380, 333, false);
                                        AidlUtil.getInstance().printBitmap(aaaaa);
                                        AidlUtil.getInstance().linewrap(1);
                                        //////////////////+

                                    }

                                    @Override
                                    public void onFailure(Exception e) {

                                    }
                                });
                            }
                        }, 2000);


//                        AidlUtil.getInstance().printText("W0: "+ WOchon, 24, false, false);
//                        AidlUtil.getInstance().linewrap(2);
//                        AidlUtil.getInstance().printQr(val, 8, 1);
//                        AidlUtil.getInstance().linewrap(2);
//                        AidlUtil.getInstance().printText(val, 24, false, false);
//                        AidlUtil.getInstance().linewrap(4);
                    } else {
                        sharedPreferences = getSharedPreferences("DCMac", MODE_PRIVATE);
                        DCMac = sharedPreferences.getString("DC", "");
                        if (DCMac.length() == 0) {
                            Toast.makeText(LotCompositeActivity.this, "Can't find device", Toast.LENGTH_SHORT).show();
                        } else {

                            BluetoothUtil.connectBlueTooth(LotCompositeActivity.this, DCMac);
//                            photoEditorView.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {

                            final Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    photoEditor.saveAsBitmap(new OnSaveBitmap() {
                                        @Override
                                        public void onBitmapReady(Bitmap saveBitmap) {
                                            ////////////////
                                            aaaaa = Bitmap.createScaledBitmap(saveBitmap, 380, 333, false);
                                            BluetoothUtil.sendData(ESCUtil.printBitmap(aaaaa));
                                            BluetoothUtil.sendData(ESCUtil.nextLine(2));
                                            //////////////////
                                        }

                                        @Override
                                        public void onFailure(Exception e) {

                                        }

                                    });
                                }
                            }, 2000);


//                                }
//                            });

//                            send = ESCUtil.alignLeft();
//                            BluetoothUtil.sendData(send);
//
//                            String content = "WO: " + WOchon + "\n";
//                            try {
//                                BluetoothUtil.sendData(content.getBytes("utf-8"));
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            send = ESCUtil.alignCenter();
//
//                            BluetoothUtil.sendData(send);
//
//                            BluetoothUtil.sendData(ESCUtil.getPrintQRCode2(val, 320));
//                            //BluetoothUtil.sendData(ESCUtil.printBitmap(bmp, 0));
//                            //AidlUtil.getInstance().linewrap(4);
//                            try {
//                                BluetoothUtil.sendData(val.getBytes("utf-8"));
//                            } catch (UnsupportedEncodingException e) {
//                                e.printStackTrace();
//                            }
//                            BluetoothUtil.sendData(ESCUtil.nextLine(4));
                        }
                    }

//                    tvMtCode.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            dialog.dismiss();


//                            View v1 = dialog.getWindow().getDecorView().getRootView();
//                            loadView(v1);
//                            final Dialog dialog = new Dialog(LotCompositeActivity.this);
//                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//                            dialog.setContentView(R.layout.popup_text);
//                            ImageView aaaa=   dialog.findViewById(R.id.img_qr);
//                            Bitmap icon = BitmapFactory.decodeResource(getResources(),R.drawable.test);
//                           Bitmap BBBB = Bitmap.createScaledBitmap(loadView(v1), 350, 350, false);
//                           aaaa.setImageBitmap(BBBB);
//                           dialog.show();


//                        }
//                    });

                }
            }
        }
    }


    private void callMapping() {
        String bbNo = tv_bobbinCode.getText().toString();
        String idWm = "";
        if (productMasterArrayList.size() == 0) {

        } else {
            idWm = productMasterArrayList.get(vitri).getWmtid();
            String api = API_MAPPING + bbNo + API_MAPPING1 + idWm;
            if (bbNo.length() > 0 && idWm.length() > 0) {
                new jsonMapping().execute(api + "&level=" + level);
                Log.d("Bopping",api + "&level=" + level);
            } else {
                Toast.makeText(LotCompositeActivity.this, "Please insert \"Bobbin code\" and select \"Lot code\"", Toast.LENGTH_LONG).show();
            }
        }

    }

    public void startQRScanner() {
        new IntentIntegrator(this).initiateScan();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
///////Bluetooth
        switch (requestCode) {
            case CONNECT_DEVICE:
                if (resultCode == Activity.RESULT_OK) {
                    dialog.show();
                    new Thread(new Runnable() {
                        public void run() {
                            myOpertion.open(data);
                        }
                    }).start();
                }
                break;
            case ENABLE_BT:
                if (resultCode == Activity.RESULT_OK) {
                    myOpertion.chooseDevice();
                } else {
                    Toast.makeText(this, R.string.bt_not_enabled, Toast.LENGTH_SHORT).show();
                }
        }
/////////////
        if (result != null) {
            if (result.getContents() == null) {
                Toast.makeText(LotCompositeActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                tv_bobbinCode.setText(result.getContents());
            }
        }
    }

    class docJSONData extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            productMasterArrayList = new ArrayList<ProductMaster>();
            try {
                //JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("result");
                JSONArray jsonArray1 = jsonObject.getJSONArray("qc_code");


                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                    String wmtid = jsonObject1.getString("wmtid");
                    String gr_qty = jsonObject1.getString("gr_qty");
                    String mt_qrcode = jsonObject1.getString("mt_qrcode");
                    String bb_no = jsonObject1.getString("bb_no");
                    if (bb_no.equals("null")) {
                        bb_no = "";
                    }
                    productMasterArrayList.add(new ProductMaster(wmtid, gr_qty, bb_no, mt_qrcode));
                }

                Ma_QC_Code = jsonArray1.getString(0);


            } catch (JSONException e) {
                e.printStackTrace();
            }
            productMasterAdaptor = new ProductMasterAdaptor(
                    LotCompositeActivity.this, R.layout.item_product, productMasterArrayList);
            lvDanhSanhproduct.setAdapter(productMasterAdaptor);

           // Toast.makeText(LotCompositeActivity.this, "" + Ma_QC_Code, Toast.LENGTH_SHORT).show();

        }
    }

    class jsonMapping extends AsyncTask<String, Integer, String> {
        @Override
        protected String doInBackground(String... strings) {
            return NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //productMasterArrayList = new ArrayList<ProductMaster>();
            try {
                JSONObject jsonObject = new JSONObject(s);
                Boolean kq = jsonObject.getBoolean("result");
                if (kq == true) {
                    productMasterArrayList.get(vitri).setBb_no(tv_bobbinCode.getText().toString());
                    Toast.makeText(LotCompositeActivity.this, "Done", Toast.LENGTH_SHORT).show();
                } else {
                    AlertNotExist("Bobbin code not exist, please check again");
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
            productMasterAdaptor.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.Setting) {
            openConn();
//            final Dialog dialog = new Dialog(LotCompositeActivity.this);
//            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//            dialog.setContentView(R.layout.popup_setting_print);
//            chonchedo =dialog.findViewById(R.id.setting_textview1);
//            popup_setting_print();
//            dialog.show();
            return true;
        }
        if (id == R.id.Weight) {
            if (SerialPortManager.instance().open("/dev/ttyS0","9600") != null) {
                String getdata = SerialReadThread.data;
                if (getdata == null){
                    AlertNotExist("Connection to device failed! Please check again");
                }
                else {
                    popup_weight();
                }

            }else {

                AlertNotExist("Device not support!");
            }

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void popup_weight() {
        final Dialog dialog = new Dialog(this);
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
        mHandler.postDelayed(mDelay,100);
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

                GroupQtynum = num_current.getText().toString().trim();
                GroupQty.setText(GroupQtynum);

                dialog.dismiss();

            }
        });

        dialog.show();
    }

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
                mHandler.postDelayed(mDelay, 50);
            }
        }
    };


    private void inputTextDialog(String key) {
        Rect displayRectangle = new Rect();
        Window window = this.getWindow();
        window.getDecorView().getWindowVisibleDisplayFrame(displayRectangle);
        final android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this, R.style.Theme_AppCompat_DayNight_Dialog_Alert);
        builder.setTitle("Qty Value");
        View dialogView = null;
        if (key.equals("qty_standa")){
            dialogView= LayoutInflater.from(this).inflate(R.layout.number_input_layout_ok_cancel, null);
        }else if (key.equals("weight_standa")){
            dialogView = LayoutInflater.from(this).inflate(R.layout.number_input_layout_ok_cancel_le, null);
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
        // Inflate the menu; this adds items to the action bar if it is present.

        if (checkUnit.equals("Bluetooth")) {
            getMenuInflater().inflate(R.menu.menu_setting_print, menu);
            return true;
        } else {

            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_setting_print_bluetooth, menu);
            MenuItem switchOnOffItem = menu.findItem(R.id.switchOnOffItem);
            switchOnOffItem.setActionView(R.layout.switch_layout);

            final SwitchCompat switchOnOff = switchOnOffItem.getActionView().findViewById(R.id.switchOnOff);
            switchOnOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(final CompoundButton buttonView, boolean isChecked) {
                    if (switchOnOff.isChecked()) {
                        chon_kieu_in = 1;
                    } else {
                        chon_kieu_in = 0;
                        openConn();
                    }
                }
            });

            return true;
        }
    }

    ///////
    private void openConn() {
        if (!isConnected) {
            myOpertion = new BluetoothOperation(LotCompositeActivity.this, mHandler);
            myOpertion.chooseDevice();
        } else {
            myOpertion.close();
            myOpertion = null;
            mPrinter = null;
        }
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PrinterConstants.Connect.SUCCESS:
                    isConnected = false;
                    //mPrinter = myOpertion.getPrinter();
                    Toast.makeText(mContext, "connected...", Toast.LENGTH_SHORT).show();
                    myOpertion.close();
                    myOpertion = null;
                    mPrinter = null;
                    break;
                case PrinterConstants.Connect.FAILED:
                    isConnected = false;
                    Toast.makeText(mContext, "connect failed...", Toast.LENGTH_SHORT).show();
                    sharedPreferences = getSharedPreferences("DCMac", MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("DC", "");
                    editor.commit();
                    break;
                case PrinterConstants.Connect.CLOSED:
                    isConnected = false;
                    // Toast.makeText(mContext, "connect close...", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            if (dialog != null && dialog.isShowing()) {
                dialog.dismiss();
            }
        }

    };

    private void InitView() {
        dialog = new ProgressDialog(mContext);
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        dialog.setTitle("Connecting...");
        dialog.setMessage("Please Wait...");
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
    }


    public static Bitmap loadView(View v) {
        Bitmap b = Bitmap.createBitmap(650, 650, Bitmap.Config.ARGB_4444);
        Canvas c = new Canvas(b);
        v.layout(0, 0, v.getWidth(), v.getHeight());
        v.draw(c);
        return b;
    }

    void saveFile(Bitmap bitmap) {
        String extr = Environment.getExternalStorageDirectory().toString() + File.separator + "Folder";
        String fileName = new SimpleDateFormat("yyyyMMddhhmm'_bitmap.jpg'", Locale.US).format(new Date());
        File myPath = new File(extr, fileName);
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Screen", "screen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void takeScreenshot(Dialog dialog) {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // image naming and path  to include sd card  appending name you choose for file
            String mPath = "/data/data/com.rohit.test/test.jpg"; // use your desired path

            // create bitmap screen capture
            View v1 = dialog.getWindow().getDecorView().getRootView();

            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(mPath);

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

        } catch (Throwable e) {
            // Several error may come out with file handling or OOM
            e.printStackTrace();
        }
    }

    //    class docJSONdata_them extends AsyncTask<String, Integer, String> {
//
//        @Override
//        protected String doInBackground(String... strings) {
//            return NoiDung_Tu_URL(strings[0]);
//        }
//
//        @Override
//        protected void onPostExecute(String s) {
//            super.onPostExecute(s);
//            productMasterArrayList = new ArrayList<ProductMaster>();
//            try {
//                JSONArray jsonArray = new JSONArray(s);
//                JSONObject jsonObject = jsonArray.getJSONObject(0);
//                bom_no = jsonObject.getString("bom_no");
//                index = jsonObject.getString("index");
//                style_no = jsonObject.getString("style_no");
//
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            getdata();
//        }
//    }
    class docJSONtaomoi extends AsyncTask<String, Integer, String> {

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
                    Toast.makeText(mContext, "Data server incorrect!!!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Toast.makeText(mContext, "Done", Toast.LENGTH_SHORT).show();
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            getdata();
        }
    }

    private void getdata() {
        new docJSONData().execute("http://snk.autonsi.com/Lot/getmt_date_web?wo_no=" +
                WOchon + "&prounit_cd=" + ProcessChon + "&date=" + Datechon + "&pr_nm=" +
                ProcessnameChon + "&index=" + index + "&style_no=" + style_no + "&bom_no=" + bom_no + "&level=" + level + "&process_no="+ process_nochon+ "&line_no=" +line_nochon);

        Log.d("lay data", "http://snk.autonsi.com/Lot/getmt_date_web?wo_no=" +
                WOchon + "&prounit_cd=" + ProcessChon + "&date=" + Datechon + "&pr_nm=" +
                ProcessnameChon + "&index=" + index + "&style_no=" + style_no + "&bom_no=" + bom_no + "&level=" + level + "&process_no="+ process_nochon+ "&line_no=" +line_nochon);

    }
}

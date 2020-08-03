package com.licheedev.MMSSNK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.Toast;

import com.licheedev.MMSSNK.Actual.ActualFragment;
import com.licheedev.MMSSNK.Actual.TerminalFragment;
import com.licheedev.MMSSNK.Home.HomeFragment;
import com.licheedev.MMSSNK.OQC.OQCFragment;
import com.licheedev.MMSSNK.PQC.PQCFragment;
import com.licheedev.MMSSNK.Status.StatusFragment;
import com.licheedev.MMSSNK.TotalInspection.TotalInspectionFragment;
import com.licheedev.MMSSNK.WO.WOFragment;
import com.licheedev.MMSSNK.WeightScale.WeightScaleActivity;
import com.licheedev.MMSSNK.comn.SerialPortManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainLayout extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, FragmentManager.OnBackStackChangedListener {

    String userlogin = null;

    private DrawerLayout drawer;
    private SerialSocket socket;
    private SerialService service;

    ExpandableListAdapter expandableListAdapter;
    ExpandableListView expandableListView;
    List<MenuModel> headerList = new ArrayList<>();
    HashMap<MenuModel, List<MenuModel>> childList = new HashMap<>();

    TerminalFragment terminalFragment;
    TextView KTrafragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_layout);
        // khoa tat man hinh+ them ham pha duoi

        KTrafragment = findViewById(R.id.KTrafragment);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        SharedPreferences sharedPreferences;
        sharedPreferences = getSharedPreferences("datalogin", MODE_PRIVATE);
        userlogin = sharedPreferences.getString("TK","");


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        prepareMenuData();
        populateExpandableList();

        drawer = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView datalogin = (TextView) headerView.findViewById(R.id.datalogin);
        datalogin.setText(userlogin);
        TextView dataloginemal = (TextView) headerView.findViewById(R.id.dataloginemail);
        dataloginemal.setText(userlogin + "@gmail.com");

        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            setTitle("Notice");
            navigationView.setCheckedItem(R.id.nav_notice);

        }



    }

//khoa tat man hinh
    @Override
    protected void onDestroy() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.nav_notice:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new HomeFragment()).commit();
//                setTitle("Notice");
//                break;
//            case R.id.nav_wo:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new WOFragment()).commit();
//                setTitle("WO List");
//
//                break;
//            case R.id.nav_actual:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new ActualFragment()).commit();
//                setTitle("Actual");
//                break;
//            case R.id.nav_pqc:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new PQCFragment()).commit();
//                setTitle("PQC");
//                break;
//            case R.id.nav_oqc:
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                        new OQCFragment()).commit();
//                setTitle("OQC");
//                break;
////            case R.id.nav_Composite:
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new ProductFragment()).commit();
////                setTitle("Lot Composite");
////                break;
////            case R.id.nav_product:
////                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
////                        new CompositeFragment()).commit();
////                setTitle("Lot Product");
////                break;
//            case R.id.logout:
//                Intent intent = new Intent(MainLayout.this, DMActivity.class);
//                startActivity(intent);
//                finish();
//                break;
////            case R.id.xulydulieu:
////                if(SerialPortManager.instance().open("/dev/ttyS0","9600") == null){
////                    Toast.makeText(this, "Device not support", Toast.LENGTH_SHORT).show();
////                }else {
////                    Intent intent1 = new Intent(MainLayout.this, WeightScaleActivity.class);
////                    startActivity(intent1);
////                }
////                break;
//
//        }
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void prepareMenuData() {

        MenuModel menuModel = new MenuModel(R.drawable.notice,"Notice", true, false, "Notice"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new MenuModel(R.drawable.actual,"Status", true, false, "Status"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel(R.drawable.wo,"WO List", true, false, "WO List"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }
        menuModel = new MenuModel(R.drawable.actual,"Actual", true, false, "Actual"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

//        menuModel = new MenuModel(R.drawable.actual,"Total Inspection", true, false, "TotalInspection"); //Menu of Android Tutorial. No sub menus
//        headerList.add(menuModel);
//
//        if (!menuModel.hasChildren) {
//            childList.put(menuModel, null);
//        }


        menuModel = new MenuModel(R.drawable.wo,"QC", true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);

        List<MenuModel> childModelsList = new ArrayList<>();
        MenuModel childModel = new MenuModel(R.drawable.ic_data,"PQC", false, false, "PQC");
        childModelsList.add(childModel);

        childModel = new MenuModel(R.drawable.ic_data,"OQC", false, false, "OQC");
        childModelsList.add(childModel);

        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        if (SerialPortManager.instance().open("/dev/ttyS0","9600") != null) {
            menuModel = new MenuModel(R.drawable.actual, "Weight Scale", true, false, "Weight Scale"); //Menu of Android Tutorial. No sub menus
            headerList.add(menuModel);

            if (!menuModel.hasChildren) {
                childList.put(menuModel, null);
            }
        }

        menuModel = new MenuModel(R.drawable.ic_white_24dp,"Logout", true, false, "Logout"); //Menu of Android Tutorial. No sub menus
        headerList.add(menuModel);

        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }


    }

    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
//                        WebView webView = findViewById(R.id.webView);
//                        webView.loadUrl(headerList.get(groupPosition).url);
                        String key = headerList.get(groupPosition).url;
                        //Toast.makeText(MainActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                        RunFragment(key);
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.url.length() > 0) {
//                        WebView webView = findViewById(R.id.webView);
//                        webView.loadUrl(model.url);
                        String key = model.url;
                        //Toast.makeText(MainActivity.this, ""+key, Toast.LENGTH_SHORT).show();
                        RunFragment(key);
                        onBackPressed();
                    }
                }
                return false;
            }
        });
    }



    private void RunFragment(String keyFragment) {
        switch (keyFragment) {
            case "Notice":
                KTrafragment.setText("Notice");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new HomeFragment())
                        .commit();
                setTitle("Notice");
                break;

            case "Status":
                KTrafragment.setText("Status");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new StatusFragment())
                        .commit();
                setTitle("Factory Status");
                break;

            case "WO List":
                KTrafragment.setText("Notice");
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container, new WOFragment())
                        .commit();

                setTitle("WO List");
                break;

            case "Actual":
                KTrafragment.setText("ActKeyIn");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ActualFragment()).commit();
              //  if (savedInstanceState == null)

                setTitle("Actual");
                break;

            case "PQC":
                KTrafragment.setText("PQC");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new PQCFragment()).commit();
                setTitle("PQC");
                break;

            case "OQC":
                KTrafragment.setText("OQC");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new OQCFragment()).commit();
                setTitle("OQC");
                break;

            case "TotalInspection":
                KTrafragment.setText("TotalInspection");
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new TotalInspectionFragment()).commit();
                setTitle("Total Inspection");
                break;

            case "Weight Scale":
                KTrafragment.setText("Weight");
                Intent intent1 = new Intent(MainLayout.this, WeightScaleActivity.class);
                startActivity(intent1);
                break;

            case "Logout":
                KTrafragment.setText("Logout");
                Intent intent = new Intent(MainLayout.this, DMActivity.class);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }

    
    
    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //super.onBackPressed();

            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to Logout", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }
    }
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_home_receiving, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.Receiving) {
////            Intent intent = new Intent(MainLayout.this, ReceivingScan.class);
////            startActivity(intent);
////            return true;
////        }
////        if (id == R.id.InventoryReceiving) {
////            Intent intent = new Intent(MainLayout.this, InventoryActivity.class);
////            startActivity(intent);
////            return true;
////        }
////        if (id == R.id.ShippingReceiving) {
////            Intent intent = new Intent(MainLayout.this, ShippingScanActivity.class);
////            startActivity(intent);
////            return true;
////        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if(intent.getAction().equals("android.hardware.usb.action.USB_DEVICE_ATTACHED")) {
            TerminalFragment terminal = (TerminalFragment)getSupportFragmentManager().findFragmentByTag("terminal");
            if (terminal != null)
                terminal.status("USB device detected");
        }
        super.onNewIntent(intent);
    }

}

package com.licheedev.MMSSNK;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.hoho.android.usbserial.driver.UsbSerialDriver;
import com.hoho.android.usbserial.driver.UsbSerialProber;
import com.licheedev.MMSSNK.Actual.TerminalFragment;

import java.util.ArrayList;
import java.util.Locale;

public class DevicesFragment extends ListFragment {

    class ListItem {
        UsbDevice device;
        int port;
        UsbSerialDriver driver;

        ListItem(UsbDevice device, int port, UsbSerialDriver driver) {
            this.device = device;
            this.port = port;
            this.driver = driver;
        }
    }

    private ArrayList<ListItem> listItems = new ArrayList<>();
    private ArrayAdapter<ListItem> listAdapter;
    private int baudRate = 19200;
    int TAM = 1;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


        listAdapter = new ArrayAdapter<ListItem>(getActivity(), 0, listItems) {
            @Override
            public View getView(int position, View view, ViewGroup parent) {
                ListItem item = listItems.get(position);


                if (view == null)
                    view = getActivity().getLayoutInflater().inflate(R.layout.device_list_item, parent, false);
                TextView text1 = view.findViewById(R.id.text1);
                TextView text2 = view.findViewById(R.id.text2);
                if (item.driver == null) {

                }

                //text1.setText("<no driver>");
                else if (item.driver.getPorts().size() == 1) {

                }
                //  text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver",""));
                else
                    text1.setText(item.driver.getClass().getSimpleName().replace("SerialDriver", "") + ", Port " + item.port);
                text2.setText(String.format(Locale.US, "Vendor %04X, Product %04X", item.device.getVendorId(), item.device.getProductId()));

              //  ListItem item3 = listItems.get(0);
                //Toast.makeText(getActivity(), item3.driver +"", Toast.LENGTH_SHORT).show();

                for (int i=0;i<listItems.size();i++) {
                    if (listItems.get(i).driver == null) {
                        //Toast.makeText(getActivity(), "no driver", Toast.LENGTH_SHORT).show();
                    } else {
                        Bundle args = new Bundle();
                        args.putInt("device", listItems.get(i).device.getDeviceId());
                        args.putInt("port", listItems.get(i).port);
                        args.putInt("baud", baudRate);
                        Fragment fragment = new TerminalFragment();
                        fragment.setArguments(args);
                        getFragmentManager().beginTransaction().replace(R.id.serial, fragment, "terminal").addToBackStack(null).commit();
                    }
                }


                return view;
            }
        };


    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setListAdapter(null);
        View header = getActivity().getLayoutInflater().inflate(R.layout.device_list_header, null, false);
        getListView().addHeaderView(header, null, false);
        setEmptyText("");
        ((TextView) getListView().getEmptyView()).setTextSize(18);
        setListAdapter(listAdapter);


    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//        inflater.inflate(R.menu.menu_devices, menu);
//    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.refresh) {
            refresh();
            return true;
        } else if (id == R.id.baud_rate) {
            final String[] baudRates = getResources().getStringArray(R.array.baud_rates);
            int pos = java.util.Arrays.asList(baudRates).indexOf(String.valueOf(baudRate));
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Baud rate");
            builder.setSingleChoiceItems(baudRates, pos, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    baudRate = Integer.valueOf(baudRates[item]);
                    dialog.dismiss();
                }
            });
            builder.create().show();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    void refresh() {
        UsbManager usbManager = (UsbManager) getActivity().getSystemService(Context.USB_SERVICE);
        UsbSerialProber usbDefaultProber = UsbSerialProber.getDefaultProber();
        UsbSerialProber usbCustomProber = CustomProber.getCustomProber();
        listItems.clear();
        for (UsbDevice device : usbManager.getDeviceList().values()) {
            UsbSerialDriver driver = usbDefaultProber.probeDevice(device);
            if (driver == null) {
                driver = usbCustomProber.probeDevice(device);
            }
            if (driver != null) {
                for (int port = 0; port < driver.getPorts().size(); port++)
                    listItems.add(new ListItem(device, port, driver));
            } else {
                listItems.add(new ListItem(device, 0, null));
            }
        }
        listAdapter.notifyDataSetChanged();
    }

//    @Override
//    public void onListItemClick(ListView l, View v, int position, long id) {
//        ListItem item = listItems.get(position-1);
//        if(item.driver == null) {
//            Toast.makeText(getActivity(), "no driver", Toast.LENGTH_SHORT).show();
//        } else {
//            Bundle args = new Bundle();
//            args.putInt("device", item.device.getDeviceId());
//            args.putInt("port", item.port);
//            args.putInt("baud", baudRate);
//            Fragment fragment = new TerminalFragment();
//            fragment.setArguments(args);
//            getFragmentManager().beginTransaction().replace(R.id.serial, fragment, "terminal").addToBackStack(null).commit();
//        }
//    }

}
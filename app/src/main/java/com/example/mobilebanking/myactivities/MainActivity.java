package com.example.mobilebanking.myactivities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.example.mobilebanking.R;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import Pockdata.PocketPos;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }

    private Button mEnableBtn, mPrintReceiptBtn;
    private static Button mConnectBtn;
    private Spinner mDeviceSp;


    StringBuffer buffer;
//    HttpResponse response;
//    HttpClient httpclient;
//    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    TextView tv;
    static SQLiteDatabase db;

    private ProgressDialog mProgressDlg, mConnectingDlg;

    private BluetoothAdapter mBluetoothAdapter;

    private P25Connector mConnector;
    String qty1 = "";// =getIntent().getStringExtra("qty").toString();
    String sno1 = "";
    String pin1 = "";

    MainActivity mainActivity;
    public static String company, userrrrr, branchhh;

    private ArrayList<BluetoothDevice> mDeviceList = new ArrayList<BluetoothDevice>();
    String comp;

    private static BluetoothSocket mSocket;
    BluetoothDevice selectDevice = null;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main2);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        myToolbar.setTitle("Print Report");
        setSupportActionBar(myToolbar);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mConnectBtn = (Button) findViewById(R.id.btn_connect);
        mEnableBtn = (Button) findViewById(R.id.btn_enable);
        mPrintReceiptBtn = (Button) findViewById(R.id.btn_print_receipt);
        mDeviceSp = (Spinner) findViewById(R.id.sp_device);


        tv = (TextView) findViewById(R.id.tv);
        qty1 = "2342";//getIntent().getStringExtra("qty").toString();
        sno1 = "234234";//getIntent().getStringExtra("sno").toString();
        pin1 = "A345455345G";// getIntent().getStringExtra("pin").toString();
        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            showUnsupported();
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                showDisabled();
            } else {
                showEnabled();

                Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

                if (pairedDevices != null) {
                    mDeviceList.addAll(pairedDevices);
                    updateDeviceList();
                }
            }

            mProgressDlg = new ProgressDialog(this);
            mProgressDlg.setMessage("Scanning...");
            mProgressDlg.setCancelable(false);
            mProgressDlg.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mBluetoothAdapter.cancelDiscovery();
                }
            });

            mConnectingDlg = new ProgressDialog(this);
            mConnectingDlg.setMessage("Connecting...");
            mConnectingDlg.setCancelable(false);
            mConnector = new com.example.mobilebanking.myactivities.P25Connector(new com.example.mobilebanking.myactivities.P25Connector.P25ConnectionListener() {
                @Override
                public void onStartConnecting() {
                    mConnectingDlg.show();
                }

                @Override
                public void onConnectionSuccess() {
                    mConnectingDlg.dismiss();
                    showConnected();
                }

                @Override
                public void onConnectionFailed(String error) {
                    mConnectingDlg.dismiss();
                }

                @Override
                public void onConnectionCancelled() {
                    mConnectingDlg.dismiss();
                }

                @Override
                public void onDisconnected() {
                    showDisonnected();
                }
            });

            //enable bluetooth
            mEnableBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1000);
                }
            });

            //connect/disconnect
            mConnectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    connect();


                }
            });

            mPrintReceiptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View arg0) {


                    printStruk();
                    dialog = ProgressDialog.show(MainActivity.this, "",
                            "submitting collection Online, please wait...", true);
                    new Thread(new Runnable() {
                        public void run() {
                            //sendToDB();
                            //SocketApplication app = (SocketApplication) getApplicationContext();
                            //app.setDevice(null);
                            MainActivity.this.finish();
                            //socket.close();
                            dialog.dismiss();
                        }
                    }).start();


                }
            });
        }


        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(mReceiver, filter);

    }

    public void setSupportActionBar(Toolbar myToolbar) {
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onPause() {
        if (mBluetoothAdapter != null) {
            if (mBluetoothAdapter.isDiscovering()) {
                mBluetoothAdapter.cancelDiscovery();
            }
        }

        if (mConnector != null) {
            try {
                mConnector.disconnect();
            } catch (P25ConnectionException e) {
                e.printStackTrace();
            }
        }

        super.onPause();
    }

    private String[] getArray(ArrayList<BluetoothDevice> data) {
        String[] list = new String[0];
        if (data == null) return list;
        int size = data.size();
        list = new String[size];
        for (int i = 0; i < size; i++) {
            list[i] = data.get(i).getName();
        }
        return list;
    }


    private void showToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    private void updateDeviceList() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.simple_spinner_item2, getArray(mDeviceList));
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item1);
        mDeviceSp.setAdapter(adapter);
        mDeviceSp.setSelection(0);
    }

    private void showDisabled() {
        showToast("Bluetooth disabled");
        mEnableBtn.setVisibility(View.VISIBLE);
        mConnectBtn.setVisibility(View.GONE);
        mDeviceSp.setVisibility(View.GONE);
    }

    private void showEnabled() {
        showToast("Bluetooth enabled");
        mEnableBtn.setVisibility(View.GONE);
        mConnectBtn.setVisibility(View.VISIBLE);
        mDeviceSp.setVisibility(View.VISIBLE);
    }

    private void showUnsupported() {
        showToast("Bluetooth is unsupported by this device");
        mConnectBtn.setEnabled(false);
        mPrintReceiptBtn.setEnabled(false);
        mDeviceSp.setEnabled(false);
    }

    private void showConnected() {
        showToast("Connected");
        mConnectBtn.setText("Disconnect");
        mPrintReceiptBtn.setEnabled(true);
        mDeviceSp.setEnabled(false);
    }

    private void showDisonnected() {
        showToast("Disconnected");
        mConnectBtn.setText("Connect");
        mPrintReceiptBtn.setEnabled(false);
        mDeviceSp.setEnabled(true);
    }

    private void connect() {
        if (mDeviceList == null || mDeviceList.size() == 0) {

            return;
        }

        BluetoothDevice device = mDeviceList.get(mDeviceSp.getSelectedItemPosition());
        if (device.getBondState() == BluetoothDevice.BOND_NONE) {
            try {
                createBond(device);
            } catch (Exception e) {
                showToast("Failed to pair device");
                return;
            }
        }

        if (!mConnector.isConnected()) {
            try {
                mConnector.connect(device);
            } catch (P25ConnectionException e) {
                e.printStackTrace();
            }
        } else {
            try {
                mConnector.disconnect();
            } catch (P25ConnectionException e) {
                e.printStackTrace();
            }

            showDisonnected();
        }


        //my code
    }

    private void createBond(BluetoothDevice device) throws Exception {

        try {
            Class<?> cl = Class.forName("android.bluetooth.BluetoothDevice");
            Class<?>[] par = {};
            Method method = cl.getMethod("createBond", par);
            method.invoke(device);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    private void sendData(byte[] bytes) {
        try {
            mConnector.sendData(bytes);
        } catch (P25ConnectionException e) {
            e.printStackTrace();
        }
        db.execSQL("UPDATE CollectionDB set status='1' where status='0';");
    }


    public void autosynch() {

        db = openOrCreateDatabase("CollectionDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS CollectionDB(supplier VARCHAR," +
                "quantity VARCHAR,branch VARCHAR,datep DATETIME,date DATETIME, auditId VARCHAR,shift VARCHAR, status VARCHAR,transdate VARCHAR);");

        Cursor c = db.rawQuery("SELECT * FROM CollectionDB WHERE status='0'", null);

        if (c.getCount() == 0) {
            showMessage("Collection Message", "No new collection found");
            return;
        } else {

            dialog = ProgressDialog.show(MainActivity.this, "",
                    " Detecting new collection, please wait...", true);
            dialog.setCancelable(true);

            new Thread(new Runnable() {
                public void run() {


                    //MainPrintActivity.this.finish();

                    //showAlert();
                    dialog.dismiss();

                }
            }).start();
        }
    }



    public void showAlert() {
        MainActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Success.");
                builder.setMessage("Milk Collection submited Successfully.")
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    private void printStruk() {

//        Cursor c = db.rawQuery("SELECT * FROM CollectionDB WHERE status='0'", null);
//        if (c.getCount() == 0) {
//            showMessage("Record Message", "No records found");
//            return;
//        }

        StringBuffer buffer = new StringBuffer();
        MainActivity ma = new MainActivity();
//        company = ma.company;
////        userrrrr = ma.logedInUser;
////        branchhh = ma.branch;
//
//        while (c.moveToNext()) {
//            buffer.append("Supplier No    :" + c.getString(0) + "\n");
//            buffer.append("Quantity       :" + c.getString(1) + " KGs\n");
//            buffer.append("Station Name    :" + branchhh + "\n");
//            buffer.append("Received By    :" + userrrrr + "\n");
//        }

        showMessage("Collection Details", buffer.toString());
        long milis1 = System.currentTimeMillis();
        String date1 = util.DateUtil.timeMilisToString(milis1, "dd-MM-yyyy");
        String time1 = util.DateUtil.timeMilisToString(milis1, "  HH:mm a");

        StringBuilder content2Sb = new StringBuilder();
        content2Sb.append("\n" + "Amtech Technologies Limited"+ "\n" + "MILK RECEIPT" + "\n");
        content2Sb.append("-----------------------------" + "\n");
        content2Sb.append("" + buffer.toString() + "" + "\n");
        content2Sb.append("--------------------------" + "\n");
        content2Sb.append("Date:" + date1 + "" + "," + "Time:" + time1 + "" + "\n");
        content2Sb.append("--------------------------" + "\n");
        content2Sb.append("MILK FOR HEALTH AND WEALTH" + "\n");
        content2Sb.append("--------------------------" + "\n");
        content2Sb.append("DESIGNED & DEVELOPED BY" + "\n");
        content2Sb.append("AMTECH TECHNOLOGIES LTD" + "\n");
        content2Sb.append("www.amtechafrica.com" + "\n");
        content2Sb.append("--------------------------" + "\n");

        byte[] content2Byte = util.Printer.printfont(content2Sb.toString(), util.FontDefine.FONT_32PX, util.FontDefine.Align_LEFT, (byte) 0x1A,
                PocketPos.LANGUAGE_ENGLISH);
        byte[] totalByte = new byte[content2Byte.length];
        int offset = 0;
        System.arraycopy(content2Byte, 0, totalByte, offset, content2Byte.length);
        offset += content2Byte.length;
        byte[] senddata = PocketPos.FramePack(PocketPos.FRAME_TOF_PRINT, totalByte, 0, totalByte.length);
        sendData(senddata);

    }



    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    private final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, BluetoothAdapter.ERROR);

                if (state == BluetoothAdapter.STATE_ON) {
                    showEnabled();
                } else if (state == BluetoothAdapter.STATE_OFF) {
                    showDisabled();
                }
            } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                mDeviceList = new ArrayList<BluetoothDevice>();

                mProgressDlg.show();
            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                mProgressDlg.dismiss();

                updateDeviceList();
            } else if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = (BluetoothDevice) intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                mDeviceList.add(device);

                showToast("Found device " + device.getName());
            } else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                final int state = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, BluetoothDevice.ERROR);

                if (state == BluetoothDevice.BOND_BONDED) {
                    showToast("Paired");

                    connect();
                }
            }
        }
    };

    @Override
    public void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();
    }
}
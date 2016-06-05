package com.example.desktop.btdata;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.Set;

public class BTDataMainActivity extends AppCompatActivity {

    public BluetoothAdapter mBluetoothAdapter;
    public TextView textBTData;
    public BroadcastReceiver mReceiver;
    public BluetoothDevice device;

    private static final int REQUEST_ENABLE_BT = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_btdata_main);

        textBTData = (TextView) findViewById(R.id.textBTData);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            textBTData.append("\n Bluetooth is not supported \n");
            System.exit(0);
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            Set<BluetoothDevice> pairedDevices = mBluetoothAdapter.getBondedDevices();

            if (pairedDevices.size() > 0) {
                for (BluetoothDevice device : pairedDevices) {
                    textBTData.append("\n-----------\nPaired device name: " + device.getName() + "\nAddress: " + device.getAddress() + "\n");
                }
            }

            mReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    String action = intent.getAction();
                    device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                    if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                        textBTData.append("\n-----------\nACTION_FOUND\n " + device.getName() + "," + device.getAddress() + "\n");
                        Log.v("Receiver", "ACTION_FOUND " + device.getName() + "," + device.getAddress() + "\n");
                    } else if (BluetoothDevice.ACTION_ACL_CONNECTED.equals(action)) {
                        textBTData.append("\n-----------\nACTION_ACL_CONNECTED\n" + device.getName() + "," + device.getAddress() + "\n");
                        Log.v("Receiver", "ACTION_ACL_CONNECTED " + device.getName() + "," + device.getAddress() + "\n");
                    } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                        textBTData.append("\n-----------\nACTION_DISCOVERY_FINISHED\n" + device.getName() + "," + device.getAddress() + "\n");
                        Log.v("Receiver", "ACTION_DISCOVERY_FINISHED " + device.getName() + "," + device.getAddress() + "\n");
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED.equals(action)) {
                        textBTData.append("\n-----------\nACTION_ACL_DISCONNECT_REQUESTED\n" + device.getName() + "," + device.getAddress() + "\n");
                        Log.v("Receiver", "ACTION_ACL_DISCONNECT_REQUESTED " + device.getName() + "," + device.getAddress() + "\n");
                    } else if (BluetoothDevice.ACTION_ACL_DISCONNECTED.equals(action)) {
                        textBTData.append("\n-----------\nACTION_ACL_DISCONNECTED\n" + device.getName() + "," + device.getAddress() + "\n");
                        Log.v("Receiver", "ACTION_ACL_DISCONNECTED " + device.getName() + "," + device.getAddress() + "\n");
                    }
                }
            };

            IntentFilter filter1 = new IntentFilter(BluetoothDevice.ACTION_ACL_CONNECTED);
            IntentFilter filter2 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECT_REQUESTED);
            IntentFilter filter3 = new IntentFilter(BluetoothDevice.ACTION_ACL_DISCONNECTED);
            this.registerReceiver(mReceiver, filter1);
            this.registerReceiver(mReceiver, filter2);
            this.registerReceiver(mReceiver, filter3);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.unregisterReceiver(mReceiver);
        Log.v("onPause", "Unregistering");
    }
}

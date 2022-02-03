package com.example.bof_group_28;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Bluetooth extends AppCompatActivity {
  //Citation: [Online] Medium. Available at: https://davidadeyyinka.medium.com/creating-a-simple-bluetooth-chat-app-in-7-days-f33758140e5c.
  //Citation: [Online] Android Developer Bluetooth. Available at: https://developer.android.com/guide/topics/connectivity/bluetooth/find-bluetooth-devices#query-paired

  public static final String NO_BLUETOOTH_MSG = "This app requires that your phone has bluetooth capabilities!";
  public static final int REQUEST_ENABLE_BT = 1;
  public static final int DISCOVERABLE_DURATION_NUM_SECONDS = 1800;
  public static final int NUM_DEVICE_INFO_FIELDS = 2;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
    registerReceiver(receiver, filter);
  }

  public Bluetooth() {
    BluetoothAdapter adapter = setupBluetooth();
    discoverDevices(adapter);
  }

  BluetoothAdapter setupBluetooth() {
    BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

    if (!phoneHasBluetooth(adapter)) {
      Utilities.showAlert(this, NO_BLUETOOTH_MSG);
      return null;
    }

    adapter = enableBluetooth(adapter);
    return adapter;
  }

  boolean phoneHasBluetooth(BluetoothAdapter adapter) {
    if (adapter == null) return false;
    return true;
  }

  BluetoothAdapter enableBluetooth(BluetoothAdapter adapter) {
    if (!adapter.isEnabled()) {
      Intent enableBluetoothIntent = new Intent(adapter.ACTION_REQUEST_ENABLE);
      startActivityForResult(enableBluetoothIntent, REQUEST_ENABLE_BT);
    }

    return adapter;
  }

  void enableDiscoverability() {
    int requestCode = 1;
    Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
    discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, DISCOVERABLE_DURATION_NUM_SECONDS);
    startActivityForResult(discoverableIntent, requestCode);
  }

  void discoverDevices(BluetoothAdapter adapter) {
    enableDiscoverability();
    if (adapter.isDiscovering()) {
      adapter.cancelDiscovery();
    }
    adapter.startDiscovery();
  }

  private final BroadcastReceiver receiver = new BroadcastReceiver() {
    public void onReceive(Context context, Intent intent) {
      String action = intent.getAction();
      if (BluetoothDevice.ACTION_FOUND.equals(action)) {
        BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
        String deviceName = device.getName();
        String deviceHardwareAddress = device.getAddress();
      }
    }
  };

  List<String[]> getPairedDevices(BluetoothAdapter adapter) {
    Set<BluetoothDevice> pairedDevices = adapter.getBondedDevices();
    List<String[]> pairedDevicesInfo = new ArrayList<String[]>();
    if (pairedDevices.size() > 0) {
      for (BluetoothDevice device : pairedDevices) {
        String[] deviceInfo = new String[NUM_DEVICE_INFO_FIELDS];
        deviceInfo[0] = device.getName();
        deviceInfo[1] = device.getAddress(); // MAC address
        pairedDevicesInfo.add(deviceInfo);
      }
    }

    return pairedDevicesInfo;
  }

  protected void onDestroy() {
   super.onDestroy();

   BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
   adapter.cancelDiscovery();
   unregisterReceiver(receiver);
  }
  
}

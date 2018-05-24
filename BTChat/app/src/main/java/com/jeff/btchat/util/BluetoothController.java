package com.jeff.btchat.util;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;

import java.util.Iterator;
import java.util.Set;

import me.drakeet.multitype.Items;

/**
 * Created by lxn on 2016/3/4.
 * 蓝牙管理类
 */
public class BluetoothController {
    private static BluetoothController instance;

    public static BluetoothController getInstance() {
        if (instance == null) {
            synchronized (BluetoothController.class) {
                if (instance == null) {
                    instance = new BluetoothController();
                }
            }
        }
        return instance;
    }

    private BluetoothAdapter mAdapter;

    public BluetoothController() {
        mAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * 判断当前设备是否支持蓝牙
     *
     * @return
     */
    public boolean isSupportBluetooth() {
        if (mAdapter != null) {
            return true;
        }
        return false;
    }

    /**
     * 获取蓝牙的状态
     *
     * @return
     */
    public boolean getBluetoothStatus() {
        return mAdapter.isEnabled();
    }

    /**
     * 打开蓝牙
     *
     * @param activity
     * @param requestCode
     */
    public void turnOnBluetooth(Activity activity, int requestCode) {
        if (mAdapter != null && !mAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            activity.startActivityForResult(intent, requestCode);
        }
    }

    /**
     * 关闭蓝牙
     */
    public void turnOffBluetooth() {
        if (mAdapter != null && mAdapter.isEnabled()) {
            mAdapter.disable();
        }
    }


    public boolean stopSearthBltDevice() {
        //暂停搜索设备
        if (mAdapter != null) {
            return mAdapter.cancelDiscovery();
        }
        return false;
    }

    public void startSearthBltDevice() {
        //开始搜索设备
        if (mAdapter != null) {
            mAdapter.startDiscovery();//开始搜索蓝牙
        }
    }

    /**
     * 尝试配对
     *
     * @param btDev
     */
    public void createBond(BluetoothDevice btDev) {
        if (btDev.getBondState() == BluetoothDevice.BOND_NONE) {
            //如果这个设备取消了配对，则尝试配对
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                btDev.createBond();
            }
        }
    }

    /**
     * 尝试连接
     *
     * @param btDev
     */
    public void connectBond(BluetoothDevice btDev) {
        if (btDev.getBondState() == BluetoothDevice.BOND_BONDED) {
            //如果这个设备已经配对完成，则尝试连接
            //connect(btDev, handler);
        }
    }

    /**
     * 获得系统保存的配对成功过的设备，并尝试连接
     */
    public void getBltList(Items items) {
        if (mAdapter == null)
            return;
        else {
            //获得已配对的远程蓝牙设备的集合
            Set<BluetoothDevice> devices = mAdapter.getBondedDevices();
            if (devices.size() > 0) {
                for (Iterator<BluetoothDevice> it = devices.iterator(); it.hasNext(); ) {
                    BluetoothDevice device = it.next();
                    //自动连接已有蓝牙设备
                    items.add(device);
                }
            }
        }

    }
}
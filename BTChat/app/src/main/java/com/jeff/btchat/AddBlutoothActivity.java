package com.jeff.btchat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.jeff.btchat.adapter.BlutoothDeviceBinder;
import com.jeff.btchat.adapter.BtChatViewBinder;
import com.jeff.btchat.model.UserModel;
import com.jeff.btchat.util.BluetoothController;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import pub.devrel.easypermissions.EasyPermissions;

public class AddBlutoothActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    private MultiTypeAdapter adapter;
    /* Items 等同于 ArrayList<Object> */
    private Items items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_blutooth);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true); //设置返回键可用
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        getStartLeScan();
        // 用BroadcastReceiver来取得搜索结果
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        this.registerReceiver(searchDevices, intent);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MultiTypeAdapter();

        /* 注册类型和 View 的对应关系 */
        adapter.register(BluetoothDevice.class, new BlutoothDeviceBinder());
        items = new Items();
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    /**
     * 蓝牙接收广播
     */
    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //    onRegisterBltReceiver.onBluetoothDevice(device);
                Log.e("bluetooth", device.toString());
                items.add(device);
                adapter.notifyDataSetChanged();
            }
            //状态改变时
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.d("BlueToothTestActivity", "正在配对......");
                        //     onRegisterBltReceiver.onBltIng(device);
                        Toast.makeText(AddBlutoothActivity.this,"正在配对",Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        Log.d("BlueToothTestActivity", "完成配对");
                        //     onRegisterBltReceiver.onBltEnd(device);
                        Toast.makeText(AddBlutoothActivity.this,"完成配对",Toast.LENGTH_SHORT).show();
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        Log.d("BlueToothTestActivity", "取消配对");
                        //       onRegisterBltReceiver.onBltNone(device);
                        Toast.makeText(AddBlutoothActivity.this,"取消配对",Toast.LENGTH_SHORT).show();
                    default:
                        break;
                }
            }
        }
    };

/*    在 BluetoothAdapter 中，我们可以看到有两个扫描蓝牙的方法。第一个方法可以指定只扫描含有特定 UUID Service 的蓝牙设备，第二个方法则是扫描全部蓝牙设备。
    boolean startLeScan(UUID[] serviceUuids, BluetoothAdapter.LeScanCallback callback)
    boolean startLeScan(BluetoothAdapter.LeScanCallback callback)*/


    public void getStartLeScan() {//扫描蓝牙
        String[] perms = {Manifest.permission.ACCESS_COARSE_LOCATION};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...
            BluetoothController.getInstance().startSearthBltDevice();
        } else {
            // Do not have permissions, request them now
            EasyPermissions.requestPermissions(this, "搜索蓝牙需要位置权限",
                    Content.EASYPERMISSION_CODE, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        BluetoothController.getInstance().startSearthBltDevice();
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        //不给权限表示啥也做不了

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(searchDevices);//取消接收广播
        BluetoothController.getInstance().stopSearthBltDevice();
    }

}

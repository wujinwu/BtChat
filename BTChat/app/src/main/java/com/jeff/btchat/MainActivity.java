package com.jeff.btchat;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.jeff.btchat.adapter.BtChatViewBinder;
import com.jeff.btchat.model.UserModel;
import com.jeff.btchat.util.BluetoothController;

import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.drakeet.multitype.Items;
import me.drakeet.multitype.MultiTypeAdapter;
import pub.devrel.easypermissions.EasyPermissions;

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MultiTypeAdapter adapter;
    /* Items 等同于 ArrayList<Object> */
    private Items items;


    public static MainActivity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        activity = this;
        setSupportActionBar(toolbar);

        getBluetoothManager();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //设置布局管理器
        recyclerView.setLayoutManager(layoutManager);

        adapter = new MultiTypeAdapter();

        /* 注册类型和 View 的对应关系 */
        adapter.register(BluetoothDevice.class, new BtChatViewBinder());


        /* 模拟加载数据，也可以稍后再加载，然后使用
         * adapter.notifyDataSetChanged() 刷新列表 */
        items = new Items();
        BluetoothController.getInstance().getBltList(items);
        adapter.setItems(items);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add:
                if (BluetoothController.getInstance().getBluetoothStatus()) {
                    startActivity(new Intent(this, AddBlutoothActivity.class));
                } else {
                    getBluetoothManager();
                }

                break;

        }
        return true;
    }


    public void getBluetoothManager() {
        if (!BluetoothController.getInstance().isSupportBluetooth()) {
            Toast.makeText(this, "该设备不支持蓝牙", Toast.LENGTH_SHORT).show();
        } else {
            if (BluetoothController.getInstance().getBluetoothStatus()) {

            } else {
                BluetoothController.getInstance().turnOnBluetooth(this, 1000);
            }
        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1000:
                if (resultCode == RESULT_OK) {
                    Toast.makeText(this, "蓝牙启动成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "蓝牙启动失败", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}

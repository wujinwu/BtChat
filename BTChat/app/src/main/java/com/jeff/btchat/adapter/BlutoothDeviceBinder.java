package com.jeff.btchat.adapter;

import android.bluetooth.BluetoothDevice;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jeff.btchat.R;
import com.jeff.btchat.model.UserModel;
import com.jeff.btchat.util.BluetoothController;

import me.drakeet.multitype.ItemViewBinder;

public class BlutoothDeviceBinder
    extends ItemViewBinder<BluetoothDevice, BlutoothDeviceBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_addblutooth, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull final BluetoothDevice model) {
        holder.tvName.setText("名字:"+model.getName());
        holder.tvUuid.setText("地址:"+model.getAddress());
        holder.tvPeidui.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothController.getInstance().createBond(model);
            }
        });
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull private final TextView tvName,tvUuid,tvPeidui;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
            this.tvUuid = (TextView) itemView.findViewById(R.id.tv_uuid);
            this.tvPeidui = (TextView) itemView.findViewById(R.id.tv_peidui);
        }
    }
}
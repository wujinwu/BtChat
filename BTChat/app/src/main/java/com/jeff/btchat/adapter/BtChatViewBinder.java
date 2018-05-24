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

import java.util.Set;

import me.drakeet.multitype.ItemViewBinder;

public class BtChatViewBinder
        extends ItemViewBinder<BluetoothDevice, BtChatViewBinder.ViewHolder> {

    @NonNull
    @Override
    protected ViewHolder onCreateViewHolder(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        View root = inflater.inflate(R.layout.item_main_btchat, parent, false);
        return new ViewHolder(root);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, @NonNull BluetoothDevice model) {
        holder.tvName.setText("名字:" + model.getName());
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        @NonNull
        private final TextView tvName;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tvName = (TextView) itemView.findViewById(R.id.tv_name);
        }
    }
}
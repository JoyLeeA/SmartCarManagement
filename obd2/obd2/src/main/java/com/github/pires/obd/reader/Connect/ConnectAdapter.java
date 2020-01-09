package com.github.pires.obd.reader.Connect;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import com.github.pires.obd.reader.R;

public class ConnectAdapter extends RecyclerView.Adapter<ConnectViewHolder> implements ConnectAdapterContract.Model,ConnectAdapterContract.View{

    private ConnectAdapterContract.OnItemClickListener onItemClickListener;
    private ArrayList<BluetoothDevice> Items = new ArrayList<>();
    private Context context;

    /** Adapter */
    public ConnectAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ConnectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_device, viewGroup, false);
        return new ConnectViewHolder(v,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull ConnectViewHolder attentionViewHolder, int position) {
        attentionViewHolder.onBind(getItem(position), position);
    }

    /** Model */
    @Override
    public void addItems(ArrayList<BluetoothDevice> Items) {
        this.Items.addAll(Items);
    }

    @Override
    public void addItem(BluetoothDevice Item) {
        Items.add(Item);
    }

    @Override
    public void clearItem() {
        if (Items != null) {
            Items.clear();
        }
    }

    @Override
    public BluetoothDevice getItem(int position) {
        return Items.get(position);
    }

    @Override
    public ArrayList<BluetoothDevice> getItems() {
        return Items;
    }

    @Override
    public int getItemCount() {
        return Items != null ? Items.size() : 0;
    }


    /** View */
    @Override
    public void setOnClickListener(ConnectAdapterContract.OnItemClickListener clickListener) {
        this.onItemClickListener = clickListener;
    }

    @Override
    public void notifyAdapter() {
        notifyDataSetChanged();
    }

}

class ConnectViewHolder extends RecyclerView.ViewHolder {

    private Context context;
    private ConnectAdapterContract.OnItemClickListener onItemClickListener;

    private TextView name;

    ConnectViewHolder(View view, ConnectAdapterContract.OnItemClickListener onItemClickListener) {
        super(view);

        this.context = view.getContext();
        this.onItemClickListener = onItemClickListener;

        name = view.findViewById(R.id.item_device_name);

    }



    public void onBind(final BluetoothDevice item, final int position) {

        name.setText(item.getName());

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (onItemClickListener != null) {
                    onItemClickListener.onConnectItemClick(context,item,position);
                }

            }
        });

    }

}
package com.intentfilter.androidwificonnectsample;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.intentfilter.wificonnect.WifiConnectionManager;

import java.util.List;

public class WifiNetworksListAdapter extends RecyclerView.Adapter<WifiNetworksListAdapter.WifiNetworkItemViewHolder> {

    private final WifiConnectionManager wifiConnectionManager;
    private Context context;
    private List<ScanResult> scanResults;

    WifiNetworksListAdapter(Context context, List<ScanResult> scanResults) {
        this.context = context;
        this.scanResults = scanResults;
        this.wifiConnectionManager = new WifiConnectionManager(context.getApplicationContext());
    }

    @NonNull
    @Override
    public WifiNetworkItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.wifi_network_item_view, parent, false);
        return new WifiNetworkItemViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final WifiNetworkItemViewHolder holder, int position) {
        holder.addWifiNetworkToList(scanResults.get(position));
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final ScanResult scanResult = scanResults.get(holder.getAdapterPosition());
                Snackbar.make(((Activity) context).findViewById(R.id.status), "Connecting to: " + scanResult.SSID, Snackbar.LENGTH_LONG).show();

                wifiConnectionManager.connectToAvailableSSID(scanResult.SSID, new WifiConnectionManager.ConnectionStateChangedListener() {
                    @Override
                    public void onConnectionEstablished() {
                        Toast.makeText(context, "Now connected to " + scanResult.SSID, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onConnectionError(String reason) {
                        Toast.makeText(context, "Could't connect due to: " + reason, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return scanResults.size();
    }

    static class WifiNetworkItemViewHolder extends RecyclerView.ViewHolder {
        private final TextView wifiNetworkNameView;
        private final TextView capabilitiesView;
        private TextView wifiNetworkRssiLevelView;

        WifiNetworkItemViewHolder(View itemView) {
            super(itemView);
            wifiNetworkNameView = itemView.findViewById(R.id.wifi_network_name);
            wifiNetworkRssiLevelView = itemView.findViewById(R.id.wifi_network_level);
            capabilitiesView = itemView.findViewById(R.id.capabilities);
        }

        void addWifiNetworkToList(ScanResult scanResult) {
            wifiNetworkNameView.setText(scanResult.SSID);
            wifiNetworkRssiLevelView.setText(String.valueOf(scanResult.level));
            capabilitiesView.setText(scanResult.capabilities);
        }
    }
}

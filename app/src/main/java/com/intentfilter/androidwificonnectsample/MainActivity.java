package com.intentfilter.androidwificonnectsample;

import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.intentfilter.androidpermissions.PermissionManager;
import com.intentfilter.wificonnect.ScanResultsListener;
import com.intentfilter.wificonnect.WifiConnectionManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PermissionManager permissionManager = PermissionManager.getInstance(getApplicationContext());
        permissionManager.checkPermissions(Collections.singletonList(ACCESS_COARSE_LOCATION),
                new PermissionManager.PermissionRequestListener() {
                    @Override
                    public void onPermissionGranted() {
                        scanForAvailableNetworks();
                    }

                    @Override
                    public void onPermissionDenied() {
                        Toast.makeText(getApplicationContext(), "Please provide permission to scan networks", Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void scanForAvailableNetworks() {
        WifiConnectionManager wifiConnectionManager = new WifiConnectionManager(getApplicationContext());
        wifiConnectionManager.scanForNetworks(new ScanResultsListener() {
            @Override
            public void onScanResultsAvailable(List<ScanResult> scanResults) {
                showAvailableNetworks(scanResults);
            }
        });
    }

    private void showAvailableNetworks(List<ScanResult> scanResults) {
        RecyclerView wifiNetworksListView = findViewById(R.id.list_wifi_networks);
        wifiNetworksListView.addItemDecoration(new DividerItemDecoration(MainActivity.this, DividerItemDecoration.VERTICAL));
        wifiNetworksListView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        wifiNetworksListView.setAdapter(new WifiNetworksListAdapter(MainActivity.this, scanResults));
    }
}

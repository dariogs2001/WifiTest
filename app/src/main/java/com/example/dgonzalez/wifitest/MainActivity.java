package com.example.dgonzalez.wifitest;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    //http://stackoverflow.com/questions/18831442/how-to-get-signal-strength-of-connected-wifi-android

    Button mCheckSignalButton;
    TextView mShowSignalTextView;

    WifiManager mWifiManager;
    WifiReceiver mWifiReceiver;
    List<ScanResult> wifiList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        mShowSignalTextView = (TextView)findViewById(R.id.show_signal_text_view);
        mShowSignalTextView.setText("Time to test");

        mCheckSignalButton = (Button) findViewById(R.id.check_signal_button);
        mCheckSignalButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
//                onReceive(wifi);
                mWifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
                if (!mWifiManager.isWifiEnabled()) {
                    // If wifi disabled then enable it
                    Toast.makeText(getBaseContext(), "wifi is disabled..making it enabled",
                            Toast.LENGTH_LONG).show();
                    mWifiManager.setWifiEnabled(true);
                }
                mWifiReceiver = new WifiReceiver();
                IntentFilter mIntentFilter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
                mIntentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
                getBaseContext().registerReceiver(mWifiReceiver, mIntentFilter);
                mWifiManager.startScan();
            }
        });
    }

    public void onReceive(WifiManager wifiManager) {
        int numberOfLevels=5;
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int level=WifiManager.calculateSignalLevel(wifiInfo.getRssi(), numberOfLevels);
        System.out.println("Bars =" + level);
        mShowSignalTextView.setText("Bars = " + level);
    }

    class WifiReceiver extends BroadcastReceiver
    {
        // This method call when number of wifi connections changed
        public void onReceive(Context c, Intent intent) {
            int state = mWifiManager.getWifiState();
            int maxLevel = 5;
            if (state == WifiManager.WIFI_STATE_ENABLED) {
                // Get Scanned results in an array List
                wifiList = mWifiManager.getScanResults();
                // Iterate on the list
                for (ScanResult result : wifiList) {
                    //The level of each wifiNetwork from 0-5
                    int level = WifiManager.calculateSignalLevel(
                            result.level, maxLevel);
                    String SSID = result.SSID;
                    String capabilities = result.capabilities;
                    // TODO add your own code.
                }
            }

        }
    }
}

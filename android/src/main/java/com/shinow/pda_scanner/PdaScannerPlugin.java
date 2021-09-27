package com.shinow.pda_scanner;

import android.content.IntentFilter;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.PluginRegistry;

public class PdaScannerPlugin implements EventChannel.StreamHandler {
    private static final String CHANNEL = "com.shinow.pda_scanner/plugin";
    private static final String SUNMI_SCAN_ACTION = "com.sunmi.scanner.ACTION_DATA_CODE_RECEIVED";

    private static EventChannel.EventSink eventSink;

    private static final BroadcastReceiver scanReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String actionName = intent.getAction();
           if (SUNMI_SCAN_ACTION.equals(actionName)) {
                eventSink.success(intent.getStringExtra("data"));
            } else {
                Log.i("PdaScannerPlugin", "NoSuchAction");
            }
        }
    };

    private PdaScannerPlugin(Activity activity) {
        IntentFilter sunmiIntentFilter = new IntentFilter();
        sunmiIntentFilter.addAction(SUNMI_SCAN_ACTION);
        sunmiIntentFilter.setPriority(Integer.MAX_VALUE);
        activity.registerReceiver(scanReceiver, sunmiIntentFilter);
    }

    public static void registerWith(PluginRegistry.Registrar registrar) {
        EventChannel channel = new EventChannel(registrar.messenger(), CHANNEL);
        PdaScannerPlugin plugin = new PdaScannerPlugin(registrar.activity());
        channel.setStreamHandler(plugin);
    }

    @Override
    public void onListen(Object o, final EventChannel.EventSink eventSink) {
        PdaScannerPlugin.eventSink = eventSink;
    }

    @Override
    public void onCancel(Object o) {
        Log.i("PdaScannerPlugin", "PdaScannerPlugin:onCancel");
    }
}

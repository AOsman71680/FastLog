package com.fastlog.app;

import android.content.Context;
import android.content.SharedPreferences;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

/**
 * Receives the current fasting state from the web app and refreshes the home-screen widget.
 * JS side: Capacitor.Plugins.WidgetBridge.sync({ start, goal, streak })
 */
@CapacitorPlugin(name = "WidgetBridge")
public class WidgetBridgePlugin extends Plugin {

    @PluginMethod
    public void sync(PluginCall call) {
        JSObject data = call.getData();
        long start = data.optLong("start", 0L);
        int goal = data.optInt("goal", 16);
        int streak = data.optInt("streak", 0);

        SharedPreferences.Editor editor = getContext()
                .getSharedPreferences(FastWidgetProvider.PREFS, Context.MODE_PRIVATE)
                .edit();
        editor.putLong("start", start);
        editor.putInt("goal", goal);
        editor.putInt("streak", streak);
        editor.apply();

        FastWidgetProvider.updateAll(getContext());
        call.resolve();
    }
}

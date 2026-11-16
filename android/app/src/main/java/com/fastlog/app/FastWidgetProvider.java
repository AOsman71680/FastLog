package com.fastlog.app;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.RemoteViews;

import java.util.Date;

/** Home-screen widget showing how long the current fast has been running. */
public class FastWidgetProvider extends AppWidgetProvider {

    static final String PREFS = "fastlog_widget";

    @Override
    public void onUpdate(Context context, AppWidgetManager manager, int[] appWidgetIds) {
        for (int id : appWidgetIds) {
            update(context, manager, id);
        }
    }

    static void updateAll(Context context) {
        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] ids = manager.getAppWidgetIds(new ComponentName(context, FastWidgetProvider.class));
        for (int id : ids) {
            update(context, manager, id);
        }
    }

    static void update(Context context, AppWidgetManager manager, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        long start = prefs.getLong("start", 0L);
        int goal = prefs.getInt("goal", 16);
        int streak = prefs.getInt("streak", 0);

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_fast);

        Intent open = new Intent(context, MainActivity.class);
        open.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pending = PendingIntent.getActivity(
                context, 0, open, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        views.setOnClickPendingIntent(R.id.widget_root, pending);

        if (start > 0) {
            long now = System.currentTimeMillis();
            long elapsed = Math.max(0L, now - start);
            long goalMs = goal * 3600000L;

            views.setTextViewText(R.id.w_status, "FASTING");
            views.setViewVisibility(R.id.w_timer, View.VISIBLE);
            views.setViewVisibility(R.id.w_idle, View.GONE);
            // Chronometer counts up on its own, so the widget needs no further updates.
            views.setChronometer(R.id.w_timer, SystemClock.elapsedRealtime() - elapsed, null, true);

            String sub;
            if (elapsed >= goalMs) {
                sub = "Goal " + goal + "h reached";
            } else {
                String end = DateFormat.getTimeFormat(context).format(new Date(start + goalMs));
                sub = "Goal " + goal + "h · ends " + end;
            }
            views.setTextViewText(R.id.w_sub, sub);
        } else {
            views.setTextViewText(R.id.w_status, "FASTLOG");
            views.setViewVisibility(R.id.w_timer, View.GONE);
            views.setViewVisibility(R.id.w_idle, View.VISIBLE);
            views.setChronometer(R.id.w_timer, SystemClock.elapsedRealtime(), null, false);
            views.setTextViewText(R.id.w_sub, "Tap to start a " + goal + "h fast");
        }
        views.setTextViewText(R.id.w_streak, "🔥 " + streak);

        manager.updateAppWidget(appWidgetId, views);
    }
}

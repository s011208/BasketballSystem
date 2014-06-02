
package com.bj4.yhh.coachboard;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class SimpleDataWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        performUpdate(context, awm);
    }

    private static void performUpdate(Context context, AppWidgetManager awm) {
        int widgetId[] = awm.getAppWidgetIds(new ComponentName(context, SimpleDataWidget.class));
        for (int appWidgetId : widgetId) {
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }

    private static PendingIntent getPendingSelfIntent(Context context, String action) {
        Intent intent = new Intent(context, SimpleDataWidget.class);
        intent.setAction(action);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }
}

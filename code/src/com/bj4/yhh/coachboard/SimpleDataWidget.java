
package com.bj4.yhh.coachboard;

import com.bj4.yhh.coachboard.basketball.R;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

public class SimpleDataWidget extends AppWidgetProvider {
    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        performUpdate(context);
    }

    public static void performUpdate(Context context) {
        AppWidgetManager awm = AppWidgetManager.getInstance(context);
        performUpdate(context, awm);
    }

    public static void performUpdate(Context context, AppWidgetManager awm) {
        int widgetId[] = awm.getAppWidgetIds(new ComponentName(context, SimpleDataWidget.class));
        for (int appWidgetId : widgetId) {
            RemoteViews updateViews = new RemoteViews(context.getPackageName(),
                    R.layout.simple_data_widget);
            Intent remoteAdapterIntent = new Intent(context, SimpleDataWidgetService.class);
            remoteAdapterIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            remoteAdapterIntent.setData(Uri.parse(remoteAdapterIntent
                    .toUri(Intent.URI_INTENT_SCHEME)));
            updateViews.setRemoteAdapter(R.id.simple_data_widget_listview, remoteAdapterIntent);
            Intent clickIntent = new Intent(context, MainActivity.class);
            PendingIntent clickPI = PendingIntent.getActivity(context, 0, clickIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            updateViews.setPendingIntentTemplate(R.id.simple_data_widget_listview, clickPI);
            awm.updateAppWidget(appWidgetId, updateViews);
        }
    }
}

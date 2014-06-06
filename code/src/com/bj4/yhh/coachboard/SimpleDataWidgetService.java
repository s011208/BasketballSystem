
package com.bj4.yhh.coachboard;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.json.JSONException;
import org.json.JSONObject;

import com.bj4.yhh.coachboard.basketball.R;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

public class SimpleDataWidgetService extends RemoteViewsService {
    public static final String SIMPLE_DATA_WIDGET_SERVICE_CLICKED_ITEM = "com.bj4.yhh.coachboard.SimpleDataWidgetService.clickedItem";

    static class SimpleDataWidgetServiceRemoteView implements RemoteViewsService.RemoteViewsFactory {
        static class DataStru {
            public static final int TYPE_TITLE = 0;

            public static final int TYPE_DATA = 1;

            String mTacticData;

            String mTacticName;

            String mFileName;

            int mType;

            public DataStru(String fileName, String name, String data, int type) {
                mTacticData = data;
                mTacticName = name;
                mFileName = fileName;
                mType = type;
            }
        }

        static class DataStruComparator implements Comparator<DataStru> {

            @Override
            public int compare(DataStru arg0, DataStru arg1) {
                // TODO Auto-generated method stub
                return arg0.mFileName.compareTo(arg1.mFileName);
            }
        }

        private final ArrayList<DataStru> mData = new ArrayList<DataStru>();

        private Context mContext = null;

        private int mAppWidgetId;

        private boolean mHasBasketBall, mHasBaseBall, mHasSoccer, mHasTennis, mHasFootBall,
                mHasVolleyBall, mHasHandBall, mHasHockey, mHasTchouk;

        public SimpleDataWidgetServiceRemoteView(Context ctxt, Intent intent) {
            mContext = ctxt;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
            refreshFileContent();
        }

        private void refreshFileContent() {
            mData.clear();
            mHasBasketBall = mHasBaseBall = mHasSoccer = mHasTennis = mHasFootBall = mHasVolleyBall = mHasHandBall = mHasHockey = mHasTchouk = false;
            final File[] fileList = mContext.getFilesDir().listFiles();
            for (File f : fileList) {
                String fileName = f.getName();
                if (!mHasBasketBall
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_BASKETBALL)) {
                    mHasBasketBall = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_BASKETBALL, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasBaseBall
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_BASEBALL)) {
                    mHasBaseBall = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_BASEBALL, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasSoccer
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_SOCCER)) {
                    mHasSoccer = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_SOCCER, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasTennis
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_TENNIS)) {
                    mHasTennis = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_TENNIS, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasFootBall
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_FOOTBALL)) {
                    mHasFootBall = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_FOOTBALL, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasVolleyBall
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_VOLLEYBALL)) {
                    mHasVolleyBall = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_VOLLEYBALL, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasHandBall
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_HANDBALL)) {
                    mHasHandBall = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_HANDBALL, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasHockey
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_HOCKEY)) {
                    mHasHockey = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_HOCKEY, null, null,
                            DataStru.TYPE_TITLE));
                } else if (!mHasTchouk
                        && fileName.startsWith(SettingManager.FILE_NAME_PREFIX_TCHOUK)) {
                    mHasTchouk = true;
                    mData.add(new DataStru(SettingManager.FILE_NAME_PREFIX_TCHOUK, null, null,
                            DataStru.TYPE_TITLE));
                }
                String data = PlayGround.readFromFile(f.getAbsolutePath());
                try {
                    JSONObject j = new JSONObject(data);
                    mData.add(new DataStru(fileName, j.getString(PlayGround.JSON_KEY_TITLE), data,
                            DataStru.TYPE_DATA));
                } catch (JSONException e) {
                }
                Collections.sort(mData, new DataStruComparator());
            }
        }

        @Override
        public void onCreate() {
            // no-op
        }

        @Override
        public void onDestroy() {
            // no-op
        }

        @Override
        public int getCount() {
            return mData.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if (position >= getCount())
                return null;
            DataStru data = mData.get(position);
            RemoteViews row = new RemoteViews(mContext.getPackageName(),
                    R.layout.simple_data_widget_list_item);
            if (data.mType == DataStru.TYPE_DATA) {
                row.setTextViewText(R.id.simple_data_widget_listview_item_text, data.mTacticName);
                Intent i = new Intent();
                Bundle extras = new Bundle();
                extras.putString(SIMPLE_DATA_WIDGET_SERVICE_CLICKED_ITEM, data.mTacticData);
                i.putExtras(extras);
                row.setOnClickFillInIntent(R.id.simple_data_widget_listview_item_text, i);
                row.setViewVisibility(R.id.simple_data_widget_listview_item_title, View.GONE);
            } else {
                row.setViewVisibility(R.id.simple_data_widget_listview_item_text, View.GONE);
                String titleName = "";
                if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_BASEBALL)) {
                    titleName = mContext.getString(R.string.settings_baseball);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_BASKETBALL)) {
                    titleName = mContext.getString(R.string.settings_basketball);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_FOOTBALL)) {
                    titleName = mContext.getString(R.string.settings_football);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_HANDBALL)) {
                    titleName = mContext.getString(R.string.settings_handball);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_HOCKEY)) {
                    titleName = mContext.getString(R.string.settings_hockey);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_SOCCER)) {
                    titleName = mContext.getString(R.string.settings_soccer);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_TCHOUK)) {
                    titleName = mContext.getString(R.string.settings_tchoukball);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_TENNIS)) {
                    titleName = mContext.getString(R.string.settings_tennies);
                } else if (data.mFileName.equals(SettingManager.FILE_NAME_PREFIX_VOLLEYBALL)) {
                    titleName = mContext.getString(R.string.settings_volleyball);
                }
                row.setTextViewText(R.id.simple_data_widget_listview_item_title, titleName);
            }
            return (row);
        }

        @Override
        public RemoteViews getLoadingView() {
            return (null);
        }

        @Override
        public int getViewTypeCount() {
            return (1);
        }

        @Override
        public long getItemId(int position) {
            return (position);
        }

        @Override
        public boolean hasStableIds() {
            return (true);
        }

        @Override
        public void onDataSetChanged() {
            refreshFileContent();
            // no-op
        }
    }

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return (new SimpleDataWidgetServiceRemoteView(getApplicationContext(), intent));
    }
}

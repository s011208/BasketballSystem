
package com.bj4.yhh.coachboard;

import java.io.File;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import com.bj4.yhh.coachboard.basketball.R;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class SaveDataListFragment extends Fragment {
    private RelativeLayout mContentView;

    private ListView mListView;

    private Context mContext;

    private SaveDataListAdapter mListAdapter;

    private LayoutInflater mInflater;

    public SaveDataListFragment() {
    }

    public SaveDataListFragment(Context context) {
        mContext = context;
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mContentView = (RelativeLayout)mInflater.inflate(R.layout.saved_data_list_fragment, null);
        initComponents();
    }

    private void initComponents() {
        mListAdapter = new SaveDataListAdapter();
        mListView = (ListView)mContentView.findViewById(R.id.saved_data_list_view);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                new AlertDialog.Builder(new ContextThemeWrapper(mContext,
                        android.R.style.Theme_DeviceDefault_Dialog))
                        .setTitle(
                                mContext.getString(R.string.saved_data_list_item_confirm_dialog)
                                        + " \"" + mListAdapter.getItem(position) + "\" ?")
                        .setIcon(android.R.drawable.ic_delete)
                        .setPositiveButton(R.string.ok, new OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                File itemToRemove = new File(mListAdapter.getItemPath(position));
                                itemToRemove.delete();
                                mListAdapter.notifyDataSetChanged();
                            }
                        }).setNegativeButton(R.string.cancel, null).setCancelable(true).show();
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mListAdapter.notifyDataSetChanged();
        return mContentView;
    }

    private class SaveDataListAdapter extends BaseAdapter {
        private ArrayList<String> mFilePath = new ArrayList<String>();

        private ArrayList<String> mTitle = new ArrayList<String>();

        public void notifyDataSetChanged() {
            refreshContent();
            super.notifyDataSetChanged();
        }

        private void refreshContent() {
            mTitle.clear();
            mFilePath.clear();
            final File[] fileList = mContext.getFilesDir().listFiles();
            for (File f : fileList) {
                String data = PlayGround.readFromFile(f.getAbsolutePath());
                try {
                    JSONObject j = new JSONObject(data);
                    mTitle.add(j.getString(PlayGround.JSON_KEY_TITLE));
                    mFilePath.add(f.getAbsolutePath());
                } catch (JSONException e) {
                }
            }
        }

        public SaveDataListAdapter() {
            refreshContent();
        }

        public String getItemPath(int position) {
            return mFilePath.get(position);
        }

        @Override
        public int getCount() {
            return mTitle.size();
        }

        @Override
        public String getItem(int position) {
            return mTitle.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            final ViewHolder holder;
            if (row == null) {
                row = mInflater.inflate(R.layout.saved_data_list_item, null);
                holder = new ViewHolder();
                holder.mTitle = (TextView)row.findViewById(R.id.saved_data_list_item_title);
                row.setTag(holder);
            } else {
                holder = (ViewHolder)row.getTag();
            }
            holder.mTitle.setText(getItem(position));
            return row;
        }

        private class ViewHolder {
            TextView mTitle;
        }
    }
}

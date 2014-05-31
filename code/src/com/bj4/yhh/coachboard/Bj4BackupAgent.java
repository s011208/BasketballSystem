
package com.bj4.yhh.coachboard;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.os.ParcelFileDescriptor;
import android.util.Log;

public class Bj4BackupAgent extends BackupAgentHelper {
    private static final String TAG = "Bj4BackupAgent";

    private static final boolean DEBUG = false;

    private static final String KEY_BACKUP_FILES = "backup_files";

    private static final String ENTITY_ALL_FILES = "entity_all_files";

    public void onCreate() {
        this.addHelper(KEY_BACKUP_FILES, new FileBackupHelper());
    }

    class FileBackupHelper implements BackupHelper {

        @Override
        public void performBackup(ParcelFileDescriptor oldState, BackupDataOutput data,
                ParcelFileDescriptor newState) {
            File[] fileList = getFilesDir().listFiles();
            JSONArray entity = new JSONArray();
            for (File file : fileList) {
                String fileData = PlayGround.readFromFile(file.getAbsolutePath());
                try {
                    JSONObject j = new JSONObject(fileData);
                    j.put(PlayGround.JSON_KEY_FILE_NAME, file.getName());
                    entity.put(j);
                } catch (JSONException e) {
                    if (DEBUG)
                        Log.w(TAG, "failed", e);
                }
            }
            byte[] bytes;
            try {
                bytes = entity.toString().getBytes("UTF-8");
                data.writeEntityHeader(ENTITY_ALL_FILES, bytes.length);
                data.writeEntityData(bytes, bytes.length);
            } catch (Exception e) {
                if (DEBUG)
                    Log.w(TAG, "failed", e);
            }
        }

        @Override
        public void restoreEntity(BackupDataInputStream data) {
            if (ENTITY_ALL_FILES.equals(data.getKey())) {
                InputStreamReader inputStreamReader = new InputStreamReader(data);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();
                try {
                    while ((receiveString = bufferedReader.readLine()) != null) {
                        stringBuilder.append(receiveString);
                    }
                    data.close();
                    JSONArray entity = new JSONArray(stringBuilder.toString());
                    String root = getFilesDir().getAbsolutePath();
                    for (int i = 0; i < entity.length(); i++) {
                        JSONObject j = entity.getJSONObject(i);
                        String fileName = j.getString(PlayGround.JSON_KEY_FILE_NAME);
                        PlayGround.writeToFile(root + File.separator + fileName, j.toString());
                    }
                } catch (Exception e) {
                    if (DEBUG)
                        Log.w(TAG, "failed", e);
                }
            }
        }

        @Override
        public void writeNewStateDescription(ParcelFileDescriptor newState) {
        }
    }
}

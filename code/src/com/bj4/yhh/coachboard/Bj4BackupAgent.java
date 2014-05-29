package com.bj4.yhh.coachboard;

import java.io.File;
import java.nio.charset.Charset;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.backup.BackupAgentHelper;
import android.app.backup.BackupDataInputStream;
import android.app.backup.BackupDataOutput;
import android.app.backup.BackupHelper;
import android.os.ParcelFileDescriptor;

public class Bj4BackupAgent extends BackupAgentHelper {
	private static final String KEY_BACKUP_FILES = "backup_files";
	private static final String ENTITY_ALL_FILES = "entity_all_files";

	public void onCreate() {
		this.addHelper(KEY_BACKUP_FILES, new FileBackupHelper());
	}

	class FileBackupHelper implements BackupHelper {

		@Override
		public void performBackup(ParcelFileDescriptor oldState,
				BackupDataOutput data, ParcelFileDescriptor newState) {
			File[] fileList = getFilesDir().listFiles();
			JSONArray entity = new JSONArray();
			for (File file : fileList) {
				String fileData = PlayGround.readFromFile(file
						.getAbsolutePath());
				try {
					JSONObject j = new JSONObject(fileData);
					j.put(PlayGround.JSON_KEY_FILE_NAME, file.getAbsolutePath());
					entity.put(j);
				} catch (JSONException e) {
				}
			}
		}

		@Override
		public void restoreEntity(BackupDataInputStream data) {
		}

		@Override
		public void writeNewStateDescription(ParcelFileDescriptor newState) {
		}
	}
}

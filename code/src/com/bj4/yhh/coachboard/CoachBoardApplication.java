
package com.bj4.yhh.coachboard;

import android.app.Application;
import android.content.Context;

public class CoachBoardApplication extends Application {
    private static SettingManager sSettingManager;

    @Override
    public void onCreate() {
        super.onCreate();
        sSettingManager = new SettingManager(this);
    }

    public static SettingManager getSettingManager(Context context) {
        if (sSettingManager == null) {
            sSettingManager = new SettingManager(context);
        }
        return sSettingManager;
    }

}

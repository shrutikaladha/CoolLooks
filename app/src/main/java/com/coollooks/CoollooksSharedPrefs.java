package com.coollooks;

import android.app.backup.SharedPreferencesBackupHelper;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Shrutika on 1/1/2016.
 */
public class CoollooksSharedPrefs extends SharedPreferencesBackupHelper {
    /**
     * Construct a helper for backing up and restoring the
     * {@link SharedPreferences} under the given names.
     *
     * @param context    The application {@link Context}
     * @param prefGroups The names of each {@link SharedPreferences} file to
     */
    public CoollooksSharedPrefs(Context context, String... prefGroups) {
        super(context, prefGroups);
    }
}

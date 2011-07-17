package org.whiskeysierra.impairedvision;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

public final class SettingsActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OptionsMenu.createMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }
}

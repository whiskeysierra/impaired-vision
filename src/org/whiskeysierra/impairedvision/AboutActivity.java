package org.whiskeysierra.impairedvision;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.view.Menu;

public final class AboutActivity extends PreferenceActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.info);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OptionsMenu.createMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

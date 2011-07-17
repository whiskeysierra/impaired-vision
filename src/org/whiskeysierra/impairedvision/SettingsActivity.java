package org.whiskeysierra.impairedvision;

import android.hardware.Camera;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import android.view.Menu;
import com.google.common.base.Function;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import java.util.List;

public final class SettingsActivity extends PreferenceActivity {

    private final Function<Camera.Size,String> toEntry = new Function<Camera.Size, String>() {

        @Override
        public String apply(Camera.Size size) {
            return size.width + " x " + size.height;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        final Camera camera = Camera.open();

        try {
            final Camera.Parameters parameters = camera.getParameters();
            final List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
            final List<String> entries = Lists.transform(sizes, toEntry);

            final PreferenceScreen screen = getPreferenceScreen();
            final ListPreference preference = (ListPreference) screen.findPreference("sizeIndex");

            preference.setEntries(Iterables.toArray(entries, CharSequence.class));
            preference.setEntryValues(indices(entries));
        } finally {
            camera.release();
        }
    }

    private String[] indices(List<?> list) {
        final String[] indices = new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            indices[i] = Integer.toString(i);
        }
        return indices;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        OptionsMenu.createMenu(this, menu);
        return super.onCreateOptionsMenu(menu);
    }

}

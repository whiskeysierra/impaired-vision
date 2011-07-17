package org.whiskeysierra.impairedvision;

import android.R;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

final class OptionsMenu {

    public static void createMenu(final Activity activity, Menu menu) {
        final MenuItem about = menu.add("About");
        about.setIcon(android.R.drawable.ic_menu_info_details);
        about.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.startActivity(new Intent(activity, AboutActivity.class));
                return false;
            }

        });

        final MenuItem settings = menu.add("Settings");
        settings.setIcon(R.drawable.ic_menu_preferences);
        settings.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                activity.startActivity(new Intent(activity, SettingsActivity.class));
                return false;
            }

        });

    }

}

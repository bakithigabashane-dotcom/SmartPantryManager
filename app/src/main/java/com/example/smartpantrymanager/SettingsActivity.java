
package com.example.smartpantrymanager;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(32, 32, 32, 32);
        layout.setBackgroundColor(Color.WHITE);

        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(28);
        title.setTextColor(Color.rgb(40, 90, 70));
        title.setGravity(Gravity.CENTER);
        title.setPadding(0, 16, 0, 24);
        layout.addView(title);

        TextView appName = new TextView(this);
        appName.setText("Smart Pantry Manager");
        appName.setTextSize(20);
        appName.setTextColor(Color.BLACK);
        appName.setPadding(0, 12, 0, 8);
        layout.addView(appName);

        TextView description = new TextView(this);
        description.setText(
                "Keep track of your ingredients and discover recipes " +
                        "you can make with what you have."
        );
        description.setTextSize(16);
        description.setTextColor(Color.DKGRAY);
        description.setPadding(0, 0, 0, 24);
        layout.addView(description);

        TextView storage = new TextView(this);
        storage.setText("Data Storage\nYour pantry is saved locally on this device using SQLite.");
        storage.setTextSize(16);
        storage.setTextColor(Color.BLACK);
        storage.setPadding(0, 12, 0, 24);
        layout.addView(storage);

        TextView matching = new TextView(this);
        matching.setText("Recipe Matching\nRecipes are shown only when you have every required ingredient in sufficient quantity.");
        matching.setTextSize(16);
        matching.setTextColor(Color.BLACK);
        matching.setPadding(0, 12, 0, 24);
        layout.addView(matching);

        TextView version = new TextView(this);
        version.setText("Version 1.0\nStudent project");
        version.setTextSize(14);
        version.setTextColor(Color.GRAY);
        version.setPadding(0, 24, 0, 0);
        layout.addView(version);

        setContentView(layout);
    }
}

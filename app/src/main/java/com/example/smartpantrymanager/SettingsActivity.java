
package com.example.smartpantrymanager;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ScrollView;

public class SettingsActivity extends Activity {

    private static final int CREAM = Color.rgb(247, 245, 239);
    private static final int GREEN = Color.rgb(49, 91, 69);
    private static final int MUTED = Color.rgb(101, 117, 104);
    private static final int WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(CREAM);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 28, 24, 28);
        layout.setBackgroundColor(CREAM);

        TextView title = new TextView(this);
        title.setText("Settings");
        title.setTextSize(29);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(GREEN);
        title.setPadding(0, 0, 0, 8);
        layout.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("About pantry app");
        subtitle.setTextSize(16);
        subtitle.setTextColor(MUTED);
        subtitle.setPadding(0, 0, 0, 22);
        layout.addView(subtitle);

        layout.addView(createCard(
                "Smart Pantry Manager",
                "Keep track of your ingredients, manage expiry dates, " +
                        "and discover recipes you can make with the food you have."
        ));

        layout.addView(createCard(
                "Data Storage",
                "Your pantry ingredients are stored locally on this device " +
                        "using an SQLite database."
        ));

        layout.addView(createCard(
                "Recipe Matching",
                "Recipes are suggested only when every required ingredient " +
                        "is available in sufficient quantity. Automatic unit conversion " +
                        "is not supported."
        ));

        layout.addView(createCard(
                "Expiry Dates",
                "Save an expiry date for each ingredient. Expired ingredients " +
                        "are highlighted in red, and ingredients expiring within three " +
                        "days are highlighted in amber."
        ));

        TextView version = new TextView(this);
        version.setText("Version 1.0\nMobile App Development 700");
        version.setTextSize(13);
        version.setTextColor(MUTED);
        version.setGravity(Gravity.CENTER);
        version.setPadding(0, 16, 0, 0);
        layout.addView(version);

        scrollView.addView(layout);
        setContentView(scrollView);
    }

    private LinearLayout createCard(String headingText, String bodyText) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(18, 16, 18, 18);

        GradientDrawable background = new GradientDrawable();
        background.setColor(WHITE);
        background.setCornerRadius(20);
        background.setStroke(1, Color.rgb(225, 232, 223));
        card.setBackground(background);

        TextView heading = new TextView(this);
        heading.setText(headingText);
        heading.setTextSize(18);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setTextColor(GREEN);
        heading.setPadding(0, 0, 0, 8);
        card.addView(heading);

        TextView body = new TextView(this);
        body.setText(bodyText);
        body.setTextSize(15);
        body.setTextColor(Color.rgb(55, 65, 57));
        body.setLineSpacing(4, 1.0f);
        card.addView(body);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.bottomMargin = 14;
        card.setLayoutParams(params);

        return card;
    }
}

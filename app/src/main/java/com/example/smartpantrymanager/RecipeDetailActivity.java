
package com.example.smartpantrymanager;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String EXTRA_RECIPE_NAME = "recipe_name";
    public static final String EXTRA_RECIPE_DESCRIPTION = "recipe_description";
    public static final String EXTRA_RECIPE_INGREDIENTS = "recipe_ingredients";
    public static final String EXTRA_RECIPE_INSTRUCTIONS = "recipe_instructions";

    private static final int CREAM = Color.rgb(247, 245, 239);
    private static final int GREEN = Color.rgb(49, 91, 69);
    private static final int MUTED = Color.rgb(101, 117, 104);
    private static final int WHITE = Color.WHITE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String name = getIntent().getStringExtra(EXTRA_RECIPE_NAME);
        String description = getIntent().getStringExtra(EXTRA_RECIPE_DESCRIPTION);
        String ingredients = getIntent().getStringExtra(EXTRA_RECIPE_INGREDIENTS);
        String instructions = getIntent().getStringExtra(EXTRA_RECIPE_INSTRUCTIONS);

        if (name == null) name = "Recipe Details";
        if (description == null) description = "";
        if (ingredients == null) ingredients = "No ingredients listed.";
        if (instructions == null) instructions = "No instructions listed.";

        ScrollView scrollView = new ScrollView(this);
        scrollView.setFillViewport(true);
        scrollView.setBackgroundColor(CREAM);

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(24, 28, 24, 28);

        TextView eyebrow = new TextView(this);
        eyebrow.setText("SMART PANTRY • RECIPE");
        eyebrow.setTextSize(12);
        eyebrow.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        eyebrow.setTextColor(MUTED);
        eyebrow.setPadding(0, 0, 0, 10);
        layout.addView(eyebrow);

        TextView title = new TextView(this);
        title.setText(name);
        title.setTextSize(29);
        title.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        title.setTextColor(GREEN);
        title.setPadding(0, 0, 0, 10);
        layout.addView(title);

        TextView descriptionView = new TextView(this);
        descriptionView.setText(description);
        descriptionView.setTextSize(16);
        descriptionView.setTextColor(MUTED);
        descriptionView.setPadding(0, 0, 0, 22);
        layout.addView(descriptionView);

        layout.addView(createSection(
                "Required Ingredients", ingredients));

        layout.addView(createSection(
                "Cooking Instructions", instructions));

        scrollView.addView(layout);
        setContentView(scrollView);
    }

    private LinearLayout createSection(String headingText, String bodyText) {
        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(20, 18, 20, 20);

        GradientDrawable background = new GradientDrawable();
        background.setColor(WHITE);
        background.setCornerRadius(22);
        background.setStroke(1, Color.rgb(225, 232, 223));
        card.setBackground(background);

        TextView heading = new TextView(this);
        heading.setText(headingText);
        heading.setTextSize(20);
        heading.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        heading.setTextColor(GREEN);
        heading.setPadding(0, 0, 0, 12);
        card.addView(heading);

        TextView body = new TextView(this);
        body.setText(bodyText);
        body.setTextSize(16);
        body.setTextColor(Color.rgb(45, 55, 47));
        body.setLineSpacing(6, 1.0f);
        card.addView(body);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        params.bottomMargin = 18;
        card.setLayoutParams(params);

        return card;
    }
}

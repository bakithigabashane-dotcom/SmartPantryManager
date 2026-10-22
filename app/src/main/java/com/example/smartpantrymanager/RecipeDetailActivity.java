
package com.example.smartpantrymanager;

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

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(40, 32, 40, 32);

        TextView title = new TextView(this);
        title.setText(name);
        title.setTextSize(28);
        title.setGravity(Gravity.CENTER_VERTICAL);
        title.setPadding(0, 0, 0, 12);
        layout.addView(title);

        TextView descriptionView = new TextView(this);
        descriptionView.setText(description);
        descriptionView.setTextSize(17);
        descriptionView.setPadding(0, 0, 0, 24);
        layout.addView(descriptionView);

        TextView ingredientsHeading = new TextView(this);
        ingredientsHeading.setText("Required Ingredients");
        ingredientsHeading.setTextSize(21);
        ingredientsHeading.setPadding(0, 0, 0, 8);
        layout.addView(ingredientsHeading);

        TextView ingredientsView = new TextView(this);
        ingredientsView.setText(ingredients);
        ingredientsView.setTextSize(16);
        ingredientsView.setPadding(0, 0, 0, 24);
        layout.addView(ingredientsView);

        TextView instructionsHeading = new TextView(this);
        instructionsHeading.setText("Cooking Instructions");
        instructionsHeading.setTextSize(21);
        instructionsHeading.setPadding(0, 0, 0, 8);
        layout.addView(instructionsHeading);

        TextView instructionsView = new TextView(this);
        instructionsView.setText(instructions);
        instructionsView.setTextSize(16);
        layout.addView(instructionsView);

        scrollView.addView(layout);
        setContentView(scrollView);
    }
}

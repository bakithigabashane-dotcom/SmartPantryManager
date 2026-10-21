
package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SimpleCursorAdapter adapter;
    private EditText nameInput;
    private EditText quantityInput;
    private EditText unitInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.ingredientName);
        quantityInput = findViewById(R.id.ingredientQuantity);
        unitInput = findViewById(R.id.ingredientUnit);

        Button addButton = findViewById(R.id.addIngredientButton);
        ListView pantryList = findViewById(R.id.pantryList);

        Cursor cursor = databaseHelper.getAllIngredients();

        adapter = new SimpleCursorAdapter(
                this,
                android.R.layout.simple_list_item_2,
                cursor,
                new String[]{"name", "unit"},
                new int[]{android.R.id.text1, android.R.id.text2},
                0
        );

        pantryList.setAdapter(adapter);

        addButton.setOnClickListener(v -> addIngredient());
    }

    private void addIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText =
                quantityInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty()
                || unit.isEmpty()) {
            Toast.makeText(this,
                    "Please complete all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;

        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this,
                    "Enter a valid quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {
            Toast.makeText(this,
                    "Quantity must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        long result = databaseHelper.insertIngredient(
                name, quantity, unit);

        if (result != -1) {
            Toast.makeText(this,
                    "Ingredient saved",
                    Toast.LENGTH_SHORT).show();

            nameInput.setText("");
            quantityInput.setText("");
            unitInput.setText("");

            refreshPantryList();
        } else {
            Toast.makeText(this,
                    "Could not save ingredient",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshPantryList() {
        Cursor newCursor = databaseHelper.getAllIngredients();

        Cursor oldCursor = adapter.swapCursor(newCursor);

        if (oldCursor != null) {
            oldCursor.close();
        }
    }

    @Override
    protected void onDestroy() {
        if (adapter != null && adapter.getCursor() != null) {
            adapter.getCursor().close();
        }

        if (databaseHelper != null) {
            databaseHelper.close();
        }

        super.onDestroy();
    }
}

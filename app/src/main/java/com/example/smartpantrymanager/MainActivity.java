
package com.example.smartpantrymanager;

import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SimpleCursorAdapter adapter;
    private EditText nameInput, quantityInput, unitInput;
    private ListView pantryList;
    private int editingId = -1;
    private Button addButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        databaseHelper = new DatabaseHelper(this);

        nameInput = findViewById(R.id.ingredientName);
        quantityInput = findViewById(R.id.ingredientQuantity);
        unitInput = findViewById(R.id.ingredientUnit);
        addButton = findViewById(R.id.addIngredientButton);
        pantryList = findViewById(R.id.pantryList);

        refreshPantryList();

        addButton.setOnClickListener(v -> saveIngredient());
        Button findRecipesButton = findViewById(R.id.findRecipesButton);

        findRecipesButton.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    MainActivity.this, RecipeSuggestionsActivity.class);
            startActivity(intent);
        });

        Button settingsButton = findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(
                    MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        });

        pantryList.setOnItemClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) adapter.getItem(position);
            if (cursor != null) {
                int ingredientId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
                double quantity = cursor.getDouble(
                        cursor.getColumnIndexOrThrow("quantity"));
                String unit = cursor.getString(
                        cursor.getColumnIndexOrThrow("unit"));

                editingId = ingredientId;
                nameInput.setText(name);
                quantityInput.setText(String.valueOf(quantity));
                unitInput.setText(unit);
                addButton.setText("Update Ingredient");
            }
        });

        pantryList.setOnItemLongClickListener((parent, view, position, id) -> {
            Cursor cursor = (Cursor) adapter.getItem(position);
            if (cursor != null) {
                int ingredientId = cursor.getInt(
                        cursor.getColumnIndexOrThrow("_id"));
                String name = cursor.getString(
                        cursor.getColumnIndexOrThrow("name"));
                confirmDelete(ingredientId, name);
            }
            return true;
        });
    }

    private void saveIngredient() {
        String name = nameInput.getText().toString().trim();
        String quantityText = quantityInput.getText().toString().trim();
        String unit = unitInput.getText().toString().trim();

        if (name.isEmpty() || quantityText.isEmpty() || unit.isEmpty()) {
            Toast.makeText(this, "Please complete all fields",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        double quantity;
        try {
            quantity = Double.parseDouble(quantityText);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Enter a valid quantity",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (!Double.isFinite(quantity) || quantity <= 0) {
            Toast.makeText(this, "Quantity must be greater than zero",
                    Toast.LENGTH_SHORT).show();
            return;
        }

        if (editingId == -1) {
            long result = databaseHelper.insertIngredient(
                    name, quantity, unit);
            if (result != -1) {
                Toast.makeText(this, "Ingredient added",
                        Toast.LENGTH_SHORT).show();
                clearForm();
                refreshPantryList();
            } else {
                Toast.makeText(this, "Could not add ingredient",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            int result = databaseHelper.updateIngredient(
                    editingId, name, quantity, unit);
            if (result > 0) {
                Toast.makeText(this, "Ingredient updated",
                        Toast.LENGTH_SHORT).show();
                clearForm();
                refreshPantryList();
            } else {
                Toast.makeText(this, "Could not update ingredient",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void confirmDelete(int ingredientId, String name) {
        new AlertDialog.Builder(this)
                .setTitle("Delete ingredient")
                .setMessage("Delete " + name + " from your pantry?")
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setPositiveButton("Delete", (dialog, which) -> {
                    int result = databaseHelper.deleteIngredient(ingredientId);
                    if (result > 0) {
                        if (editingId == ingredientId) {
                            clearForm();
                        }
                        refreshPantryList();
                        Toast.makeText(this, "Ingredient deleted",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(this, "Could not delete ingredient",
                                Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void clearForm() {
        nameInput.setText("");
        quantityInput.setText("");
        unitInput.setText("");
        editingId = -1;
        addButton.setText("Add Ingredient");
    }



    private void refreshPantryList() {
        Cursor newCursor = databaseHelper.getAllIngredients();

        if (adapter == null) {
            adapter = new SimpleCursorAdapter(
                    this,
                    android.R.layout.simple_list_item_2,
                    newCursor,
                    new String[]{"name", "quantity"},
                    new int[]{android.R.id.text1, android.R.id.text2},
                    0
            );

            adapter.setViewBinder((view, cursor, columnIndex) -> {
                if (view.getId() == android.R.id.text2) {
                    String quantity = cursor.getString(
                            cursor.getColumnIndexOrThrow("quantity"));
                    String unit = cursor.getString(
                            cursor.getColumnIndexOrThrow("unit"));

                    ((android.widget.TextView) view).setText(
                            quantity + " " + unit);
                    return true;
                }
                return false;
            });

            pantryList.setAdapter(adapter);
        } else {
            Cursor oldCursor = adapter.swapCursor(newCursor);

            if (oldCursor != null) {
                oldCursor.close();
            }
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

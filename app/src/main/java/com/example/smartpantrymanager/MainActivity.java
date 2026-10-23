
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
import android.app.DatePickerDialog;
import java.util.Calendar;
import java.util.Locale;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class MainActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private SimpleCursorAdapter adapter;
    private EditText nameInput, quantityInput, unitInput;
    private ListView pantryList;
    private int editingId = -1;
    private Button addButton;
    private TextView expiryDateInput;
    private String selectedExpiryDate = "";

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

        expiryDateInput = findViewById(R.id.expiryDateInput);

        expiryDateInput.setOnClickListener(v -> showExpiryDatePicker());

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

                int expiryIndex = cursor.getColumnIndex("expiry_date");

                selectedExpiryDate = expiryIndex >= 0
                        ? cursor.getString(expiryIndex)
                        : null;

                if (selectedExpiryDate == null || selectedExpiryDate.isEmpty()) {
                    selectedExpiryDate = "";
                    expiryDateInput.setText("Expiry date: Not set");
                } else {
                    expiryDateInput.setText("Expiry date: " + selectedExpiryDate);
                }
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

        String expiryDate = selectedExpiryDate.isEmpty()
                ? null : selectedExpiryDate;

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
                    name, quantity, unit, expiryDate);
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
                    editingId, name, quantity, unit, expiryDate);
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
    private void showExpiryDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog dialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    selectedExpiryDate = String.format(
                            Locale.ROOT, "%04d-%02d-%02d",
                            year, month + 1, dayOfMonth);

                    expiryDateInput.setText(
                            "Expiry date: " + selectedExpiryDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        dialog.setButton(
                DatePickerDialog.BUTTON_NEUTRAL,
                "Clear date",
                (dateDialog, which) -> {
                    selectedExpiryDate = "";
                    expiryDateInput.setText("Expiry date: Not set");
                }
        );

        dialog.show();
    }

    private String getExpiryStatus(String expiryDate) {
        if (expiryDate == null || expiryDate.trim().isEmpty()) {
            return "Expiry date: Not set";
        }

        try {
            LocalDate expiry = LocalDate.parse(expiryDate);
            LocalDate today = LocalDate.now();
            long daysRemaining = ChronoUnit.DAYS.between(today, expiry);

            if (daysRemaining < 0) {
                return "EXPIRED";
            } else if (daysRemaining <= 3) {
                return "EXPIRING SOON";
            } else {
                return "Fresh";
            }
        } catch (java.time.format.DateTimeParseException e) {
            return "Expiry date: " + expiryDate;
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
        selectedExpiryDate = "";
        expiryDateInput.setText("Expiry date: Not set");
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
                    String expiryDate = cursor.getString(
                            cursor.getColumnIndexOrThrow("expiry_date"));


                    String status = getExpiryStatus(expiryDate);

                    String details = quantity + " " + unit;

                    if (expiryDate != null && !expiryDate.isEmpty()) {
                        details += "\nExpires: " + expiryDate;
                        details += "\nStatus: " + status;
                    } else {
                        details += "\n" + status;
                    }

                    android.widget.TextView textView =
                            (android.widget.TextView) view;

                    textView.setText(details);

                    if ("EXPIRED".equals(status)) {
                        textView.setTextColor(android.graphics.Color.rgb(179, 38, 30));
                    } else if ("EXPIRING SOON".equals(status)) {
                        textView.setTextColor(android.graphics.Color.rgb(138, 75, 8));
                    } else {
                        textView.setTextColor(android.graphics.Color.DKGRAY);
                    }

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

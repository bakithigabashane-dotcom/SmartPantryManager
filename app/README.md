# Smart Pantry Manager

## Overview

This project is an Android application smart pantry manager, which I developed in Java.
It help users manage pantry ingredients, track expiry dates and identify recipes they can prepare with those ingredients.

## Features

- Add pantry ingredients with a name, quantity and unit.
- Edit existing pantry ingredients.
- Delete ingredients with a confirmation prompt.
- Store pantry data locally using SQLite.
- Display ingredient names, quantities and units.
- Identify expired or soon-to-expire ingredients.
- Suggest recipes based on available pantry ingredients.
- Display recipe details, required ingredients, and cooking instructions.
- Access an information screen through Settings.

## Technologies Used

- Java
- Android Studio
- Android SDK
- SqLite
- Android Intents
- Android ListView and SimpleCursorAdapter

## Database

The application uses SQLite to store pantry ingredients locally on the device.

The pantry table contains the following fields:

| Field | Description                                |
| --- |--------------------------------------------|
| _id | Unique ingredient identifier               |
| quantity | Available quantity                         |
| unit | Unit of measurement                        |
| expiry_date | Database field reserved for an expiry date |

## Recipe Matching

This application has 20 sample recipes with which the recipe matcher compares with the ingredients stored in the pantry.

A recipe is suggested only when all required ingredients are available in sufficient quantities.
Simple singular and plural differences in ingredient names are normalized.
Ingredient units must match after normalization, automatic unit conversion is not supported.

## Main Source Files

- MainActivity.java: main pantry screen and ingredient management.
- DatabaseHelper.java: SQLite database operations.
- Recipe.java: recipe and ingredient data models.
- RecipeData.java: sample recipe collection.
- RecipeMatcher.java: recipe matching logic.
- RecipeSuggestionsActivity.java: displays matching recipes.
- RecipeDetailActivity.java: displays recipe details and cooking instructions.
- SettingsActivity.java: application information screen.



## How to Run

1. Install Android Studio.
2. Clone or download this repository. 
3. Open the project in Android Studio.
4. Allow Gradle sync to finish.
5. Run the app on a compatible Android emulator or Android device.

## Testing

Important test cases include:

- Adding, editing and deleting pantry items.
- Checking that saved items persist after restarting the app.
- Saving and updating expiry dates.
- Identifying expired and soon-to-expire ingredients.
- Verifying that recipes appear only when all required ingredients and quantities are available.
- Opening recipe details and the Settings screen.

## Limitations

- Pantry data is stored locally on the device.
- Recipes use predefined sample data.
- Automatic unit conversion is not supported.
- Expiry-date behaviour must be verified after implementation.
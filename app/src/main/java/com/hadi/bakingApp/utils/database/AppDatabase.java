package com.hadi.bakingApp.utils.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hadi.bakingApp.models.Ingredient;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.models.Step;

import java.util.ArrayList;
import java.util.List;


public class AppDatabase extends SQLiteOpenHelper {

    public AppDatabase(Context context) {
        super(context, Contract.DATABASE_NAME, null, Contract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        sqLiteDatabase.execSQL(Contract.RecipesTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.StepsTable.CREATE_TABLE);
        sqLiteDatabase.execSQL(Contract.IngredientTable.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        sqLiteDatabase.execSQL(Contract.RecipesTable.DROP_TABLE);
        sqLiteDatabase.execSQL(Contract.StepsTable.DROP_TABLE);
        sqLiteDatabase.execSQL(Contract.IngredientTable.DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    public void insert(List<Recipe> recipes) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues values = new ContentValues();

        for (int i = 0; i < recipes.size(); i++) {
            Recipe recipe = recipes.get(i);

            values.put(Contract.RecipesTable.COLUMN_RECIPE_ID, recipe.getId());
            values.put(Contract.RecipesTable.COLUMN_RECIPE_NAME, recipe.getName());
            db.insert(Contract.RecipesTable.TABLE_RECIPES, null, values);
            values.clear();

            List<Ingredient> ingredients = recipe.getIngredients();
            for (int j = 0; j < ingredients.size(); j++) {
                Ingredient ingredient = ingredients.get(j);

                values.put(Contract.IngredientTable.COLUMN_QUANTITY, ingredient.getQuantity());
                values.put(Contract.IngredientTable.COLUMN_MEASURE, ingredient.getMeasure());
                values.put(Contract.IngredientTable.COLUMN_INGREDIENT_DESCRIPTION, ingredient.getIngredient());
                values.put(Contract.IngredientTable.COLUMN_RECIPE_ID, recipe.getId());
                db.insert(Contract.IngredientTable.TABLE_INGREDIENT, null, values);
                values.clear();
            }

            List<Step> steps = recipe.getSteps();
            for (int k = 0; k < steps.size(); k++) {
                Step step = steps.get(k);

                values.put(Contract.StepsTable.COLUMN_ID, step.getId());
                values.put(Contract.StepsTable.COLUMN_SHORT_DESCRIPTION, step.getShortDescription());
                values.put(Contract.StepsTable.COLUMN_DESCRIPTION, step.getDescription());
                values.put(Contract.StepsTable.COLUMN_VIDEO_URL, step.getVideoURL());
                values.put(Contract.StepsTable.COLUMN_THUMBNAIL_URL, step.getThumbnailURL());
                values.put(Contract.StepsTable.COLUMN_RECIPE_ID, recipe.getId());
                db.insert(Contract.StepsTable.TABLE_STEPS, null, values);
                values.clear();
            }

            values.clear();
        }
    }

    public List<Recipe> getRecipes() {
        List<Recipe> recipes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.RecipesTable.TABLE_RECIPES, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                Recipe recipe = new Recipe();

                recipe.setId(cursor.getInt(cursor.getColumnIndex(Contract
                        .RecipesTable.COLUMN_RECIPE_ID)));
                recipe.setName(cursor.getString(cursor.getColumnIndex(Contract
                        .RecipesTable.COLUMN_RECIPE_NAME)));

                recipe.setIngredients(getIngredients(recipe.getId()));
                recipe.setSteps(getSteps(recipe.getId()));

                recipes.add(recipe);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return recipes;
    }

    public List<Ingredient> getIngredients(int recipeId) {
        List<Ingredient> ingredients = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.IngredientTable.TABLE_INGREDIENT, null,
                Contract.IngredientTable.COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Ingredient ingredient = new Ingredient();

                ingredient.setQuantity(cursor.getDouble(cursor.getColumnIndex(Contract
                        .IngredientTable.COLUMN_QUANTITY)));
                ingredient.setMeasure(cursor.getString(cursor.getColumnIndex(Contract
                        .IngredientTable.COLUMN_MEASURE)));
                ingredient.setIngredient(cursor.getString(cursor.getColumnIndex(Contract
                        .IngredientTable.COLUMN_INGREDIENT_DESCRIPTION)));

                ingredients.add(ingredient);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return ingredients;
    }

    public List<Step> getSteps(int recipeId) {
        List<Step> steps = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(Contract.StepsTable.TABLE_STEPS, null,
                Contract.StepsTable.COLUMN_RECIPE_ID + " = ?", new String[]{String.valueOf(recipeId)},
                null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Step step = new Step();

                step.setId(cursor.getInt(cursor.getColumnIndex(Contract
                        .StepsTable.COLUMN_ID)));
                step.setShortDescription(cursor.getString(cursor.getColumnIndex(Contract
                        .StepsTable.COLUMN_SHORT_DESCRIPTION)));
                step.setDescription(cursor.getString(cursor.getColumnIndex(Contract
                        .StepsTable.COLUMN_DESCRIPTION)));
                step.setVideoURL(cursor.getString(cursor.getColumnIndex(Contract
                        .StepsTable.COLUMN_VIDEO_URL)));
                step.setThumbnailURL(cursor.getString(cursor.getColumnIndex(Contract
                        .StepsTable.COLUMN_THUMBNAIL_URL)));

                steps.add(step);
            } while (cursor.moveToNext());
        }

        try {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
                db.close();
            }
        } catch (Exception e) {
            throw new UnsupportedOperationException("Error : Close Cursor");
        }

        return steps;
    }

    public void deleteAll() {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(Contract.IngredientTable.TABLE_INGREDIENT, null, null);
        db.delete(Contract.StepsTable.TABLE_STEPS, null, null);
        db.delete(Contract.RecipesTable.TABLE_RECIPES, null, null);
    }


}

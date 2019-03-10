package com.hadi.bakingApp.utils.database;

import android.provider.BaseColumns;

public final class Contract {

    public static final String DATABASE_NAME = "recipes.db";
    public static final int DATABASE_VERSION = 1;

    private Contract() {

    }

    public static abstract class RecipesTable implements BaseColumns {

        public static final String TABLE_RECIPES = "recipes";

        public static final String COLUMN_RECIPE_ID = "recipe_id";
        public static final String COLUMN_RECIPE_NAME = "recipe_name";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_RECIPES + "(" +
                COLUMN_RECIPE_ID + " INTEGER NOT NULL, " +
                COLUMN_RECIPE_NAME + " TEXT)";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_RECIPES;
    }

    public static abstract class StepsTable implements BaseColumns {

        public static final String TABLE_STEPS = "steps";

        public static final String COLUMN_ID = "step_id";
        public static final String COLUMN_SHORT_DESCRIPTION = "short_description";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_VIDEO_URL = "video_url";
        public static final String COLUMN_THUMBNAIL_URL = "thumbnail_url";
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_STEPS + "(" +
                COLUMN_ID + " INTEGER NOT NULL, " +
                COLUMN_SHORT_DESCRIPTION + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_VIDEO_URL + " TEXT, " +
                COLUMN_THUMBNAIL_URL + " TEXT, " +
                COLUMN_RECIPE_ID + " INTEGER NOT NULL)";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_STEPS;
    }

    public static abstract class IngredientTable implements BaseColumns {

        public static final String TABLE_INGREDIENT = "ingredient";

        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_MEASURE = "measure";
        public static final String COLUMN_INGREDIENT_DESCRIPTION = "ingredient_description";
        public static final String COLUMN_RECIPE_ID = "recipe_id";

        public static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_INGREDIENT + "(" +
                COLUMN_QUANTITY + " REAL, " +
                COLUMN_MEASURE + " TEXT, " +
                COLUMN_INGREDIENT_DESCRIPTION + " TEXT, " +
                COLUMN_RECIPE_ID + " INTEGER NOT NULL)";

        public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_INGREDIENT;
    }
}

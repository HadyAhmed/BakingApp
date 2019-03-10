package com.hadi.bakingApp.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hadi.bakingApp.R;
import com.hadi.bakingApp.adapters.IngredientsAdapter;
import com.hadi.bakingApp.databinding.ActivityIngredientsBinding;
import com.hadi.bakingApp.models.Ingredient;
import com.hadi.bakingApp.models.Recipe;

import java.util.List;

import static com.hadi.bakingApp.ui.activities.StepsActivity.RECIPE;


public class Ingredients extends AppCompatActivity {

    private Recipe recipe;

    private ActivityIngredientsBinding ingredientsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ingredientsBinding = DataBindingUtil.setContentView(this, R.layout.activity_ingredients);
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE)) {

            recipe = (Recipe) intent.getSerializableExtra(RECIPE);
        } else if (savedInstanceState != null) {

            recipe = (Recipe) savedInstanceState.getSerializable(RECIPE);
        }

        initializeViews();
    }

    private void initializeViews() {

        List<Ingredient> ingredientList = recipe.getIngredients();
        IngredientsAdapter ingredientsAdapter = new IngredientsAdapter(ingredientList);

        ingredientsBinding.listIngredient.setAdapter(ingredientsAdapter);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(RECIPE, recipe);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = (Recipe) savedInstanceState.getSerializable(RECIPE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

package com.hadi.bakingApp.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hadi.bakingApp.ui.fragments.DetailsFragment;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.R;

public class DetailsActivity extends AppCompatActivity {

    private Recipe recipe = null;
    private int position = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(StepsActivity.RECIPE) && intent.hasExtra(StepsActivity.POSITION)) {

            recipe = (Recipe) intent.getSerializableExtra(StepsActivity.RECIPE);
            position = intent.getIntExtra(StepsActivity.POSITION, -1);
        } else if (savedInstanceState != null) {

            recipe = (Recipe) savedInstanceState.getSerializable(StepsActivity.RECIPE);
            position = savedInstanceState.getInt(StepsActivity.POSITION, -1);
        }

        if (recipe != null && position != -1) {

            DetailsFragment fragment = DetailsFragment.newInstance(position, recipe);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment).commit();
        } else {
            finish();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(StepsActivity.RECIPE, recipe);
        outState.putInt(StepsActivity.POSITION, position);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        recipe = (Recipe) savedInstanceState.getSerializable(StepsActivity.RECIPE);
        position = savedInstanceState.getInt(StepsActivity.POSITION, -1);
    }
}

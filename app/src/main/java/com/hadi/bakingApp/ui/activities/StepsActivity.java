package com.hadi.bakingApp.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.hadi.bakingApp.ui.fragments.DetailsFragment;
import com.hadi.bakingApp.ui.fragments.StepsFragment;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.R;
import com.hadi.bakingApp.databinding.ActivityStepsBinding;

import static com.hadi.bakingApp.ui.activities.MainActivity.RECIPE_SELECTED;


public class StepsActivity extends AppCompatActivity implements StepsFragment.OnFragmentInteractionListener {

    public static final String RECIPE = "recipe";
    public static final String POSITION = "position";
    public static boolean masterDetailFlowMode = false;
    private Recipe recipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityStepsBinding stepsBinding = DataBindingUtil.setContentView(this, R.layout.activity_steps);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra(RECIPE_SELECTED)) {

            recipe = (Recipe) intent.getSerializableExtra(RECIPE_SELECTED);
        } else if (savedInstanceState != null) {

            recipe = (Recipe) savedInstanceState.getSerializable(RECIPE);
        }


        if (recipe == null) {
            finish();
        }

        StepsFragment stepsFragment = StepsFragment.newInstance(recipe);
        getSupportFragmentManager().beginTransaction().replace(R.id.steps_fragment, stepsFragment).commit();

        if (stepsBinding.detailsFragment != null) {
            masterDetailFlowMode = true;
        }

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
    public void fragmentOnItemClick(int position) {
        if (masterDetailFlowMode) {
            DetailsFragment fragment = DetailsFragment.newInstance(position, recipe);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_fragment, fragment).commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(RECIPE, recipe);
            intent.putExtra(POSITION, position);
            startActivity(intent);
        }
    }
}

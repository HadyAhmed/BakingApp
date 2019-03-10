package com.hadi.bakingApp.ui.activities;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.test.espresso.idling.CountingIdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.hadi.bakingApp.adapters.RecipesAdapter;
import com.hadi.bakingApp.utils.database.AppDatabase;
import com.hadi.bakingApp.models.Recipe;
import com.hadi.bakingApp.utils.network.ApiClient;
import com.hadi.bakingApp.utils.network.WebServices;
import com.hadi.bakingApp.R;
import com.hadi.bakingApp.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements RecipesAdapter.OnRecipeClickListener {

    public static final String RECIPE_SELECTED = "recipe_selected";
    private RecipesAdapter recipesAdapter;

    private List<Recipe> recipeList;
    private List<String> recipeNames;

    private AppDatabase mDatabase;

    private CountingIdlingResource countingIdlingResource = new CountingIdlingResource("IdlingResource");

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mDatabase = new AppDatabase(this);

        initUi();

        countingIdlingResource.increment();

        fetchData();
    }

    private void initUi() {

        mainBinding.loadingRecipesProgress.setVisibility(View.VISIBLE);

        recipesAdapter = new RecipesAdapter(this, this);

        mainBinding.recipesRv.setAdapter(recipesAdapter);

    }

    private void fetchData() {
        WebServices webServices = ApiClient.getClient().create(WebServices.class);
        if (isConnected()) {
            mainBinding.loadingRecipesProgress.setVisibility(View.VISIBLE);
            Call<List<Recipe>> listCall = webServices.getRecipes();
            listCall.enqueue(new Callback<List<Recipe>>() {
                @Override
                public void onResponse(@NonNull Call<List<Recipe>> call, @NonNull Response<List<Recipe>> response) {
                    if (response.isSuccessful()) {
                        recipeList = response.body();
                        mDatabase.deleteAll();
                        mDatabase.insert(recipeList);

                        recipeNames = new ArrayList<>();
                        for (int i = 0; i < recipeList.size(); i++) {

                            recipeNames.add(recipeList.get(i).getName());
                        }
                        recipesAdapter.setNames(recipeNames);
                        mainBinding.loadingRecipesProgress.setVisibility(View.GONE);
                    } else {

                        recipeList = mDatabase.getRecipes();
                        if (recipeList.isEmpty()) {

                            Toast.makeText(MainActivity.this, "No data was fetched", Toast.LENGTH_LONG).show();
                            mainBinding.loadingRecipesProgress.setVisibility(View.GONE);
                        } else {

                            recipeNames = new ArrayList<>();
                            for (int i = 0; i < recipeList.size(); i++) {

                                recipeNames.add(recipeList.get(i).getName());
                            }
                            recipesAdapter.setNames(recipeNames);
                            mainBinding.loadingRecipesProgress.setVisibility(View.GONE);
                        }

                    }

                    countingIdlingResource.decrement();
                }

                @Override
                public void onFailure(@NonNull Call<List<Recipe>> call, @NonNull Throwable t) {
                    countingIdlingResource.decrement();
                }
            });

        } else {

            mainBinding.loadingRecipesProgress.setVisibility(View.VISIBLE);
            recipeList = mDatabase.getRecipes();

            if (recipeList.isEmpty()) {

                Toast.makeText(MainActivity.this, "No data was fetched", Toast.LENGTH_LONG).show();
                mainBinding.loadingRecipesProgress.setVisibility(View.GONE);
            } else {

                recipeNames = new ArrayList<>();
                for (int i = 0; i < recipeList.size(); i++) {

                    recipeNames.add(recipeList.get(i).getName());
                }
                recipesAdapter.setNames(recipeNames);
                mainBinding.loadingRecipesProgress.setVisibility(View.GONE);
            }

            countingIdlingResource.decrement();
        }

    }

    @Override
    public void onRecipeClick(int position) {

        Recipe selectedRecipe = recipeList.get(position);
        Intent intent = new Intent(MainActivity.this, StepsActivity.class);
        intent.putExtra(RECIPE_SELECTED, selectedRecipe);
        startActivity(intent);
    }

    public boolean isConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnected();
    }

    public CountingIdlingResource getIdlingResource() {
        return countingIdlingResource;
    }


}

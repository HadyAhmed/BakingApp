package com.hadi.bakingApp.ui.widgets;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.RadioGroup;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.hadi.bakingApp.R;
import com.hadi.bakingApp.utils.database.AppDatabase;


public class BakingAppWidgetConfig extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener {

    private int appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    public static String KEY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baking_app_widget_config);

        if (getIntent() != null && getIntent().getExtras() != null){
            appWidgetId = getIntent().getExtras().getInt(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        Intent resultIntent = new Intent();
        resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        setResult(RESULT_CANCELED, resultIntent);

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID){
            finish();
        }
        RadioGroup radioGroup = findViewById(R.id.recipes_group);
        radioGroup.setOnCheckedChangeListener(this);

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {

        int id = 0;
        switch (checkedId){
            case (R.id.nutella_pie):
                id = 1;
                break;
            case (R.id.brownies):
                id = 2;
                break;
            case (R.id.yellow_cake):
                id = 3;
                break;
            case (R.id.cheesecake):
                id = 4;
                break;
        }
        if (id == 0) {
            Toast.makeText(this, "You must select a recipe", Toast.LENGTH_SHORT).show();
        } else {
            KEY = "selected_recipe" + appWidgetId;
            SharedPreferences sharedPreferences = getSharedPreferences("pref", MODE_PRIVATE);
            sharedPreferences.edit().putInt(KEY, id).apply();

            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

            AppDatabase dataBaseBaking = new AppDatabase(this);
            String nameRecipe = dataBaseBaking.getRecipes().get(id - 1).getName();

            RemoteViews views = new RemoteViews(getPackageName(), R.layout.baking_app_widget);
            views.setCharSequence(R.id.recipe_widget, "setText", nameRecipe);

            Intent intent = new Intent(this, BakingAppWidgetService.class);
            views.setRemoteAdapter(R.id.list_widget, intent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

            Intent resultIntent = new Intent();
            resultIntent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}

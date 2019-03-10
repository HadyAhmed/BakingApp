package com.hadi.bakingApp.ui.widgets;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.hadi.bakingApp.R;
import com.hadi.bakingApp.models.Ingredient;
import com.hadi.bakingApp.utils.database.AppDatabase;

import java.util.List;

import static com.hadi.bakingApp.ui.widgets.BakingAppWidgetConfig.KEY;


public class BakingAppWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new BakingAppWidgetFactory(getApplicationContext());
    }

    public class BakingAppWidgetFactory implements RemoteViewsFactory {

        private Context context;
        private List<Ingredient> listOfIngredients;
        private AppDatabase appDatabase;
        private int id;

        BakingAppWidgetFactory(Context context) {
            this.context = context;
            this.appDatabase = new AppDatabase(context);
            SharedPreferences sharedPreferences = context.getSharedPreferences("pref", MODE_PRIVATE);
            id = sharedPreferences.getInt(KEY, 1);
        }

        @Override
        public void onCreate() {
            listOfIngredients = appDatabase.getIngredients(id);
        }

        @Override
        public void onDataSetChanged() {
            listOfIngredients.clear();
            listOfIngredients = appDatabase.getIngredients(id);
        }

        @Override
        public void onDestroy() {
        }

        @Override
        public int getCount() {
            return listOfIngredients.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.baking_list_item_widget);
            remoteViews.setTextViewText(R.id.widget_item_description, listOfIngredients.get(i).getIngredient());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}

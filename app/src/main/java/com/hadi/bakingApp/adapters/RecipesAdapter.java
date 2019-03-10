package com.hadi.bakingApp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hadi.bakingApp.R;

import java.util.List;


public class RecipesAdapter extends RecyclerView.Adapter<RecipesAdapter.RecipeViewHolder> {

    private List<String> names = null;
    private Context context;
    private OnRecipeClickListener onRecipeClickListener;

    public RecipesAdapter(Context context, OnRecipeClickListener onRecipeClickListener) {
        this.context = context;
        this.onRecipeClickListener = onRecipeClickListener;
    }

    public interface OnRecipeClickListener {
        void onRecipeClick(int position);
    }

    public void setNames(List<String> names) {

        if (this.names != null) {
            this.names.clear();
        }
        this.names = names;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RecipeViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.recipes_item, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, int position) {
        recipeViewHolder.recipeName.setText(names.get(position));
    }

    @Override
    public int getItemCount() {
        if (this.names == null)
            return 0;
        else
            return this.names.size();
    }

    public class RecipeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView recipeName;

        RecipeViewHolder(@NonNull View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipe_name);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onRecipeClickListener.onRecipeClick(getAdapterPosition());
        }
    }
}

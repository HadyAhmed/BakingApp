package com.hadi.bakingApp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.hadi.bakingApp.databinding.IngredientItemBinding;
import com.hadi.bakingApp.models.Ingredient;

import java.util.List;


public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> ingredientList;
    private LayoutInflater inflater;

    public IngredientsAdapter(List<Ingredient> ingredientList) {
        this.ingredientList = ingredientList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        if (inflater == null) {
            inflater = LayoutInflater.from(viewGroup.getContext());
        }
        return new IngredientViewHolder(IngredientItemBinding.inflate(inflater, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int position) {
        Ingredient ingredient = ingredientList.get(position);
        ingredientViewHolder.setIngredient(ingredient);
        ingredientViewHolder.ingredient.ingredientNumber.setText(String.valueOf(position + 1));
    }

    @Override
    public int getItemCount() {

        if (ingredientList == null) {
            return 0;
        } else {
            return ingredientList.size();
        }
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        private IngredientItemBinding ingredient;

        IngredientViewHolder(@NonNull IngredientItemBinding ingredientItem) {
            super(ingredientItem.getRoot());
            this.ingredient = ingredientItem;
        }

        void setIngredient(Ingredient ingredient) {
            this.ingredient.setIngredient(ingredient);
        }
    }
}

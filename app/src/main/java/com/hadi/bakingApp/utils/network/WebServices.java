package com.hadi.bakingApp.utils.network;

import java.util.List;

import com.hadi.bakingApp.models.Recipe;

import retrofit2.Call;
import retrofit2.http.GET;

public interface WebServices {

    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}

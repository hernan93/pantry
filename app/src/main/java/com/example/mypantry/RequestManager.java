package com.example.mypantry;

import android.content.Context;
import android.util.Log;

import com.example.mypantry.Listeners.ComplexRecipeResponseListener;
import com.example.mypantry.Listeners.InstructionsListener;
import com.example.mypantry.Listeners.RandomRecipeResponseListener;
import com.example.mypantry.Listeners.RecipeDetailsListener;
import com.example.mypantry.Models.ComplexRecipeApiResponse;
import com.example.mypantry.Models.InstructionsResponse;
import com.example.mypantry.Models.RandomRecipeApiResponse;
import com.example.mypantry.Models.RecipeDetailsResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class RequestManager {
    Context context;
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("https://api.spoonacular.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    public RequestManager(Context context) { this.context = context; }

    public void getRandomRecipes(RandomRecipeResponseListener listener, List<String> tags){
        CallRandomRecipes callRandomRecipes = retrofit.create(CallRandomRecipes.class);
        Call<RandomRecipeApiResponse> call =
                callRandomRecipes.callRandomRecipe(context.getString(R.string.api_key),
                "50",
                tags);

        call.enqueue(new Callback<RandomRecipeApiResponse>() {
            @Override
            public void onResponse(
                    Call<RandomRecipeApiResponse> call, Response<RandomRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<RandomRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }
    public void getRecipeDetails(RecipeDetailsListener listener, int id){
        CallRecipeDetails callRecipeDetails = retrofit.create(CallRecipeDetails.class);
        Call<RecipeDetailsResponse> call = callRecipeDetails.callRecipeDetails(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<RecipeDetailsResponse>() {
            @Override
            public void onResponse(Call<RecipeDetailsResponse> call, Response<RecipeDetailsResponse> response) {
                if(!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(),response.message());
            }

            @Override
            public void onFailure(Call<RecipeDetailsResponse> call, Throwable t) {
                listener.didError((t.getMessage()));
            }
        });
    }

    private interface CallRandomRecipes{
        @GET("recipes/random")
        Call<RandomRecipeApiResponse> callRandomRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("tags") List<String> tags
        );
    }

    public void getComplexRecipes(ComplexRecipeResponseListener listener, List<String> tags){
        CallComplexRecipes callComplexRecipes = retrofit.create(CallComplexRecipes.class);
        Call<ComplexRecipeApiResponse> call =
                callComplexRecipes.callComplexRecipe(context.getString(R.string.api_key),
                "50", "True", "True", tags);

        call.enqueue(new Callback<ComplexRecipeApiResponse>() {
            @Override
            public void onResponse(
                    Call<ComplexRecipeApiResponse> call, Response<ComplexRecipeApiResponse> response) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<ComplexRecipeApiResponse> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    public void getInstructions(InstructionsListener listener, int id){
        CallInstructions callInstructions = retrofit.create(CallInstructions.class);
        Call<List<InstructionsResponse>> call = callInstructions.callInstructions(id, context.getString(R.string.api_key));
        call.enqueue(new Callback<List<InstructionsResponse>>() {
            @Override
            public void onResponse(Call<List<InstructionsResponse>> call, Response<List<InstructionsResponse>> response) {
                if (!response.isSuccessful()){
                    listener.didError(response.message());
                    return;
                }
                listener.didFetch(response.body(), response.message());
            }

            @Override
            public void onFailure(Call<List<InstructionsResponse>> call, Throwable t) {
                listener.didError(t.getMessage());
            }
        });
    }

    private interface CallComplexRecipes{
        @GET("recipes/complexSearch")
        Call<ComplexRecipeApiResponse> callComplexRecipe(
                @Query("apiKey") String apiKey,
                @Query("number") String number,
                @Query("addRecipeInformation") String addRecipeInformation,
                @Query("addRecipeNutrition") String addRecipeNutrition,
                @Query("query") List<String> tags
        );
    }

    private interface CallRecipeDetails{
        @GET("recipes/{id}/information")
        Call<RecipeDetailsResponse> callRecipeDetails(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );


    }

    private interface CallInstructions{
        @GET("recipes/{id}/analyzedInstructions")
        Call<List<InstructionsResponse>> callInstructions(
                @Path("id") int id,
                @Query("apiKey") String apiKey
        );
    }
}

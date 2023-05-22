package com.example.mypantry

import com.example.mypantry.Models.Ingredient
import com.example.mypantry.Models.Equipment
import com.example.mypantry.Models.ExtendedIngredient
import com.example.mypantry.Models.AnalyzedInstruction
import com.example.mypantry.Models.Nutrition
import com.example.mypantry.Models.Us
import com.example.mypantry.Models.Metric
import com.example.mypantry.Models.Temperature
import com.example.mypantry.Models.Nutrient
import com.example.mypantry.Models.Flavonoid
import com.example.mypantry.Models.CaloricBreakdown
import com.example.mypantry.Models.WeightPerServing
import com.example.mypantry.Models.ProductMatch
import com.example.mypantry.Models.Measures
import com.example.mypantry.Models.WinePairing
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import android.app.ProgressDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import com.example.mypantry.R
import androidx.recyclerview.widget.LinearLayoutManager
import android.graphics.drawable.ColorDrawable
import android.widget.EditText
import android.text.TextUtils
import com.example.mypantry.Pantry.PantryItem
import com.google.android.gms.tasks.OnCompleteListener
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.firebase.ui.database.FirebaseRecyclerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import android.view.WindowManager
import android.content.Intent
import com.example.mypantry.MainActivity
import com.example.mypantry.Profile.RegistrationActivity
import com.google.firebase.auth.AuthResult
import com.example.mypantry.Profile.LoginActivity
import android.content.DialogInterface
import com.example.mypantry.Recipes.RandomRecipesOptionFragment
import com.example.mypantry.Recipes.ComplexRecipeSearchOptionFragment
import com.example.mypantry.RequestManager
import com.example.mypantry.Adapters.RandomRecipeAdapter
import com.example.mypantry.Listeners.RandomRecipeResponseListener
import com.example.mypantry.Models.RandomRecipeApiResponse
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypantry.Listeners.RecipeClickListener
import com.example.mypantry.RecipeDetailsFragment
import com.example.mypantry.Adapters.ComplexRecipeAdapter
import com.example.mypantry.Listeners.ComplexRecipeResponseListener
import com.example.mypantry.Models.ComplexRecipeApiResponse
import com.example.mypantry.Adapters.IngredientsViewHolder
import com.squareup.picasso.Picasso
import com.example.mypantry.Models.InstructionsResponse
import com.example.mypantry.Adapters.InstructionsViewHolder
import com.example.mypantry.Adapters.InstructionStepAdapter
import com.example.mypantry.Adapters.RandomRecipeViewHolder
import androidx.cardview.widget.CardView
import com.example.mypantry.Adapters.ComplexRecipeViewHolder
import com.example.mypantry.Adapters.InstructionStepViewHolder
import com.example.mypantry.Adapters.InstructionsIngredientsAdapter
import com.example.mypantry.Adapters.InstructionsEquipmentsAdapter
import com.example.mypantry.Adapters.InstructionEquipmentsViewHolder
import com.example.mypantry.Adapters.InstructionIngredientsViewHolder
import com.example.mypantry.Models.RecipeDetailsResponse
import android.os.Parcelable
import android.os.Parcel
import android.os.Parcelable.Creator
import com.example.mypantry.GroceryList.GroceryItem
import android.view.View.OnLongClickListener
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.mypantry.RequestManager.CallRandomRecipes
import com.example.mypantry.Listeners.RecipeDetailsListener
import com.example.mypantry.RequestManager.CallRecipeDetails
import retrofit2.http.GET
import com.example.mypantry.RequestManager.CallComplexRecipes
import com.example.mypantry.Listeners.InstructionsListener
import com.example.mypantry.RequestManager.CallInstructions
import android.view.animation.Animation
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Path
import retrofit2.http.Query

class RequestManager constructor(var context: Context?) {
    var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.spoonacular.com/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getRandomRecipes(listener: RandomRecipeResponseListener, tags: List<String>?) {
        val callRandomRecipes: CallRandomRecipes = retrofit.create(
            CallRandomRecipes::class.java
        )
        val call: Call<RandomRecipeApiResponse> = callRandomRecipes.callRandomRecipe(
            context!!.getString(R.string.api_key),
            "50",
            tags
        )
        call.enqueue(object : Callback<RandomRecipeApiResponse?> {
            public override fun onResponse(
                call: Call<RandomRecipeApiResponse?>, response: Response<RandomRecipeApiResponse?>
            ) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            public override fun onFailure(call: Call<RandomRecipeApiResponse?>, t: Throwable) {
                listener.didError(t.message)
            }
        })
    }

    fun getRecipeDetails(listener: RecipeDetailsListener, id: Int) {
        val callRecipeDetails: CallRecipeDetails = retrofit.create(
            CallRecipeDetails::class.java
        )
        val call: Call<RecipeDetailsResponse> =
            callRecipeDetails.callRecipeDetails(id, context!!.getString(R.string.api_key))
        call.enqueue(object : Callback<RecipeDetailsResponse?> {
            public override fun onResponse(
                call: Call<RecipeDetailsResponse?>,
                response: Response<RecipeDetailsResponse?>
            ) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            public override fun onFailure(call: Call<RecipeDetailsResponse?>, t: Throwable) {
                listener.didError((t.message))
            }
        })
    }

    open interface CallRandomRecipes {
        @GET("recipes/random")
        fun callRandomRecipe(
            @Query("apiKey") apiKey: String?,
            @Query("number") number: String?,
            @Query("tags") tags: List<String>?
        ): Call<RandomRecipeApiResponse>
    }

    fun getComplexRecipes(listener: ComplexRecipeResponseListener, tags: List<String>?) {
        val callComplexRecipes: CallComplexRecipes = retrofit.create(
            CallComplexRecipes::class.java
        )
        val call: Call<ComplexRecipeApiResponse> = callComplexRecipes.callComplexRecipe(
            context!!.getString(R.string.api_key),
            "50", "True", "True", tags
        )
        call.enqueue(object : Callback<ComplexRecipeApiResponse?> {
            public override fun onResponse(
                call: Call<ComplexRecipeApiResponse?>, response: Response<ComplexRecipeApiResponse?>
            ) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch(response.body(), response.message())
            }

            public override fun onFailure(call: Call<ComplexRecipeApiResponse?>, t: Throwable) {
                listener.didError(t.message)
            }
        })
    }

    fun getInstructions(listener: InstructionsListener, id: Int) {
        val callInstructions: CallInstructions = retrofit.create(
            CallInstructions::class.java
        )
        val call: Call<List<InstructionsResponse>> =
            callInstructions.callInstructions(id, context!!.getString(R.string.api_key))
        call.enqueue(object : Callback<List<InstructionsResponse>> {
            public override fun onResponse(
                call: Call<List<InstructionsResponse>>,
                response: Response<List<InstructionsResponse>>
            ) {
                if (!response.isSuccessful()) {
                    listener.didError(response.message())
                    return
                }
                listener.didFetch((response.body())!!, response.message())
            }

            public override fun onFailure(call: Call<List<InstructionsResponse>>, t: Throwable) {
                listener.didError(t.message)
            }
        })
    }

    open interface CallComplexRecipes {
        @GET("recipes/complexSearch")
        fun callComplexRecipe(
            @Query("apiKey") apiKey: String?,
            @Query("number") number: String?,
            @Query("addRecipeInformation") addRecipeInformation: String?,
            @Query("addRecipeNutrition") addRecipeNutrition: String?,
            @Query("query") tags: List<String>?
        ): Call<ComplexRecipeApiResponse>
    }

    open interface CallRecipeDetails {
        @GET("recipes/{id}/information")
        fun callRecipeDetails(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String?
        ): Call<RecipeDetailsResponse>
    }

    open interface CallInstructions {
        @GET("recipes/{id}/analyzedInstructions")
        fun callInstructions(
            @Path("id") id: Int,
            @Query("apiKey") apiKey: String?
        ): Call<List<InstructionsResponse>>
    }
}
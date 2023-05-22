package com.example.mypantry.Adapters

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
import android.view.View
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
import android.widget.ImageView
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import com.example.mypantry.Models.Result

class ComplexRecipeAdapter constructor(
    var context: Context?,
    var list: List<Result?>?,
    var listener: RecipeClickListener
) : RecyclerView.Adapter<ComplexRecipeViewHolder>() {
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ComplexRecipeViewHolder {
        return ComplexRecipeViewHolder(
            LayoutInflater
                .from(context).inflate(R.layout.list_random_recipe, parent, false)
        )
    }

    public override fun onBindViewHolder(holder: ComplexRecipeViewHolder, position: Int) {
        holder.textView_title.setText(list!!.get(position)!!.title)
        holder.textView_title.setSelected(true)
        holder.textView_likes.setText(list!!.get(position)!!.aggregateLikes.toString() + " Likes")
        holder.textView_servings.setText(list!!.get(position)!!.servings.toString() + " Servings")
        holder.textView_time.setText(list!!.get(position)!!.readyInMinutes.toString() + " Minutes")
        Picasso.get().load(list!!.get(position)!!.image).into(holder.imageView_food)
        holder.random_list_container.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                listener.onRecipeClicked(list!!.get(holder.getAbsoluteAdapterPosition())!!.id.toString())
            }
        })
    }

    public override fun getItemCount(): Int {
        return list!!.size
    }
}

class ComplexRecipeViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var random_list_container: CardView
    var textView_title: TextView
    var textView_servings: TextView
    var textView_likes: TextView
    var textView_time: TextView
    var imageView_food: ImageView

    init {
        random_list_container = itemView.findViewById(R.id.random_list_container)
        textView_title = itemView.findViewById(R.id.textView_title)
        textView_servings = itemView.findViewById(R.id.textView_servings)
        textView_likes = itemView.findViewById(R.id.textView_likes)
        textView_time = itemView.findViewById(R.id.textView_time)
        imageView_food = itemView.findViewById(R.id.imageView_food)
    }
}
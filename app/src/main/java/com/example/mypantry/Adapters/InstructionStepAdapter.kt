package com.example.mypantry.Adapters

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
import androidx.recyclerview.widget.GridLayoutManager
import com.example.mypantry.Listeners.RecipeClickListener
import com.example.mypantry.RecipeDetailsFragment
import com.example.mypantry.Adapters.ComplexRecipeAdapter
import com.example.mypantry.Listeners.ComplexRecipeResponseListener
import com.example.mypantry.Adapters.IngredientsViewHolder
import com.squareup.picasso.Picasso
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
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import com.example.mypantry.Models.*

class InstructionStepAdapter constructor(var context: Context?, var list: List<Step?>?) :
    RecyclerView.Adapter<InstructionStepViewHolder>() {
    public override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InstructionStepViewHolder {
        return InstructionStepViewHolder(
            LayoutInflater.from(context).inflate(R.layout.list_instructions_steps, parent, false)
        )
    }

    public override fun onBindViewHolder(holder: InstructionStepViewHolder, position: Int) {
        holder.textView_instructions_steps_number.setText(list!!.get(position)!!.number.toString())
        holder.textView_instructions_step_title.setText(list!!.get(position)!!.step)
        holder.recycler_instructions_ingredients.setHasFixedSize(true)
        holder.recycler_instructions_ingredients.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        val instructionsIngredientsAdapter: InstructionsIngredientsAdapter =
            InstructionsIngredientsAdapter(
                context, list!!.get(position)!!.ingredients
            )
        holder.recycler_instructions_ingredients.setAdapter(instructionsIngredientsAdapter)
        holder.recycler_instructions_equipments.setHasFixedSize(true)
        holder.recycler_instructions_equipments.setLayoutManager(
            LinearLayoutManager(
                context,
                LinearLayoutManager.HORIZONTAL,
                false
            )
        )
        val instructionsEquipmentsAdapter: InstructionsEquipmentsAdapter =
            InstructionsEquipmentsAdapter(
                context, list!!.get(position)!!.equipment
            )
        holder.recycler_instructions_equipments.setAdapter(instructionsEquipmentsAdapter)
    }

    public override fun getItemCount(): Int {
        return list!!.size
    }
}

class InstructionStepViewHolder constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textView_instructions_steps_number: TextView
    var textView_instructions_step_title: TextView
    var recycler_instructions_equipments: RecyclerView
    var recycler_instructions_ingredients: RecyclerView

    init {
        textView_instructions_steps_number =
            itemView.findViewById(R.id.textView_instructions_step_number)
        textView_instructions_step_title =
            itemView.findViewById(R.id.textView_instructions_step_title)
        recycler_instructions_equipments =
            itemView.findViewById(R.id.recycler_instructions_equipments)
        recycler_instructions_ingredients =
            itemView.findViewById(R.id.recycler_instructions_ingredients)
    }
}
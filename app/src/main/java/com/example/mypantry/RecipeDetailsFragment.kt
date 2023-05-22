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
import androidx.fragment.app.Fragment
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter

class RecipeDetailsFragment constructor() : Fragment() {
    @get:JvmName("getIdCustom")
    var id: Int = 0
    var textView_meal_name: TextView? = null
    var textView_meal_source: TextView? = null
    var textView_meal_summary: TextView? = null
    var imageView_meal_image: ImageView? = null
    var recycler_meal_ingredients: RecyclerView? = null
    var recycler_meal_instructions: RecyclerView? = null
    var manager: RequestManager? = null
    var dialog: ProgressDialog? = null
    var ingredientsAdapter: IngredientsAdapter? = null
    var instructionsAdapter: InstructionsAdapter? = null

    //    @Override
    //    public void onCreate(Bundle savedInstanceState) {
    //        super.onCreate(savedInstanceState);
    //    }
    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.fragment_recipe_details, container, false)
        findViews(view)
        id = requireArguments().getString("id")!!.toInt()
        //id = Integer.parseInt(savedInstanceState.getString("id"));
        manager = RequestManager(getActivity())
        manager!!.getRecipeDetails(recipeDetailsListener, id)
        manager!!.getInstructions(instructionsListener, id)
        dialog = ProgressDialog(getActivity())
        dialog!!.setTitle("Loading Details...")
        dialog!!.show()
        return view
    }

    private fun findViews(view: View) {
        textView_meal_name = view.findViewById(R.id.textView_meal_name)
        textView_meal_source = view.findViewById(R.id.textView_meal_source)
        textView_meal_summary = view.findViewById(R.id.textView_meal_summary)
        imageView_meal_image = view.findViewById(R.id.imageView_meal_image)
        recycler_meal_ingredients = view.findViewById(R.id.recycler_meal_ingredients)
        recycler_meal_instructions = view.findViewById(R.id.recycler_meal_instructions)
    }

    private val recipeDetailsListener: RecipeDetailsListener = object : RecipeDetailsListener {
        public override fun didFetch(response: RecipeDetailsResponse?, message: String?) {
            dialog!!.dismiss()
            textView_meal_name!!.setText(response!!.title)
            textView_meal_source!!.setText(response.sourceName)
            textView_meal_summary!!.setText(response.summary)
            Picasso.get().load(response.image).into(imageView_meal_image)
            recycler_meal_ingredients!!.setHasFixedSize(true)
            recycler_meal_ingredients!!.setLayoutManager(
                LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.HORIZONTAL,
                    false
                )
            )
            ingredientsAdapter = IngredientsAdapter(getActivity(), response.extendedIngredients)
            recycler_meal_ingredients!!.setAdapter(ingredientsAdapter)
        }

        public override fun didError(message: String?) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
        }
    }
    private val instructionsListener: InstructionsListener = object : InstructionsListener {
        public override fun didFetch(response: List<InstructionsResponse>, message: String?) {
            recycler_meal_instructions!!.setHasFixedSize(true)
            recycler_meal_instructions!!.setLayoutManager(
                LinearLayoutManager(
                    getActivity(),
                    LinearLayoutManager.VERTICAL,
                    false
                )
            )
            instructionsAdapter = InstructionsAdapter(getActivity(), response)
            recycler_meal_instructions!!.setAdapter(instructionsAdapter)
        }

        public override fun didError(message: String?) {}
    }
}
package com.example.mypantry.Recipes

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
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import java.util.ArrayList

class ComplexRecipeSearchOptionFragment constructor() : Fragment() {
    var dialog: ProgressDialog? = null
    var manager: RequestManager? = null
    var complexRecipeAdapter: ComplexRecipeAdapter? = null
    var recyclerView: RecyclerView? = null
    var searchView: SearchView? = null
    var tags: MutableList<String> = ArrayList()
    public override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        setHasOptionsMenu(true)
        val view: View = inflater.inflate(R.layout.fragment_recipes, container, false)
        dialog = ProgressDialog(getActivity())
        dialog!!.setTitle("Loading...")
        searchView = view.findViewById(R.id.searchView_home)
        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            public override fun onQueryTextSubmit(query: String): Boolean {
                tags.clear()
                tags.add(query)
                manager!!.getComplexRecipes(complexRecipeResponseListener, tags)
                dialog!!.show()
                return true
            }

            public override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        manager = RequestManager(getActivity())
        manager!!.getComplexRecipes(complexRecipeResponseListener, tags)
        dialog!!.show()
        return view
    }

    private val complexRecipeResponseListener: ComplexRecipeResponseListener =
        object : ComplexRecipeResponseListener {
            public override fun didFetch(response: ComplexRecipeApiResponse?, message: String?) {
                dialog!!.dismiss()
                recyclerView = requireView().findViewById(R.id.recycler_random)
                recyclerView?.setHasFixedSize(true)
                recyclerView?.setLayoutManager(GridLayoutManager(getActivity(), 1))
                complexRecipeAdapter = ComplexRecipeAdapter(
                    getActivity(),
                    response!!.results, recipeClickListener
                )
                recyclerView?.setAdapter(complexRecipeAdapter)
            }

            public override fun didError(message: String?) {
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show()
            }
        }
    private val recipeClickListener: RecipeClickListener = object : RecipeClickListener {
        public override fun onRecipeClicked(id: String?) {
            val bundle: Bundle = Bundle()
            bundle.putString("id", id)
            val RecipeDetailsFragment: Fragment = RecipeDetailsFragment()
            RecipeDetailsFragment.setArguments(bundle)
            val fragmentTrans: FragmentTransaction = requireActivity().getSupportFragmentManager()
                .beginTransaction()
            fragmentTrans.replace(R.id.frame_layout, RecipeDetailsFragment)
            fragmentTrans.commit()
        }
    }
}
package com.example.mypantry.Profile

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
import android.app.AlertDialog
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
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter

class ProfileFragment : Fragment() {
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var onlineUserID: String? = null
    private var loader: ProgressDialog? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        mAuth = FirebaseAuth.getInstance()
        loader = ProgressDialog(activity)
        mUser = mAuth!!.currentUser
        onlineUserID = mUser!!.uid
        val changeEmail = view.findViewById<TextView>(R.id.mEmail)
        val changePass = view.findViewById<EditText>(R.id.mNewPass)
        val confirmPass = view.findViewById<EditText>(R.id.mConfirmPass)
        changeEmail.text = "Email: " + mUser!!.email
        changePass.setText("")
        confirmPass.setText("")
        val btnLogout = view.findViewById<Button>(R.id.btnLogOut)
        val btnUpdate = view.findViewById<Button>(R.id.btnUpdate)
        val btnDeleteAccount = view.findViewById<Button>(R.id.btnDeleteAccount)
        btnUpdate.setOnClickListener {
            if (changePass.text.toString().trim { it <= ' ' } != confirmPass.text.toString()
                    .trim { it <= ' ' }) {
                val error = "Error: Passwords do not match"
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            } else if (changePass.text.toString().trim { it <= ' ' } == "") {
                val error = "Error: Password cannot be empty"
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            } else if (confirmPass.text.toString().trim { it <= ' ' } == "") {
                val error = "Error: Please confirm new password"
                Toast.makeText(activity, error, Toast.LENGTH_SHORT).show()
            } else {
                mUser!!.updatePassword(changePass.text.toString().trim { it <= ' ' })
                Toast.makeText(
                    activity,
                    "Password has been changed successfully",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        btnLogout.setOnClickListener {
            mAuth!!.signOut()
            val intent = Intent(activity, LoginActivity::class.java)
            startActivity(intent)
        }
        btnDeleteAccount.setOnClickListener {
            val builder = AlertDialog.Builder(activity)
            builder.setCancelable(true)
            builder.setTitle("Delete Confirmation")
            builder.setMessage("Are you sure you want to delete your account?")
            builder.setPositiveButton(
                "Delete"
            ) { dialog, which ->
                mAuth!!.currentUser!!.delete()
                mAuth!!.signOut()
                val intent = Intent(activity, LoginActivity::class.java)
                startActivity(intent)
                dialog.dismiss()
            }
            builder.setNegativeButton(android.R.string.cancel) { dialog, which -> }
            val dialog = builder.create()
            dialog.show()
        }
        return view
    }

    override fun onStart() {
        super.onStart()
    }
}
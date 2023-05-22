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
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegistrationActivity() : AppCompatActivity() {
    private var RegEmail: EditText? = null
    private var RegPwd: EditText? = null
    private var GroupId: EditText? = null
    private var RegBtn: Button? = null
    private var RegQn: TextView? = null
    private var mAuth: FirebaseAuth? = null
    private val reference: DatabaseReference? = null
    private val reference2: DatabaseReference? = null
    private var loader: ProgressDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_registration)
        mAuth = FirebaseAuth.getInstance()
        loader = ProgressDialog(this)
        RegEmail = findViewById(R.id.RegistrationEmail)
        RegPwd = findViewById(R.id.RegistrationPassword)
        RegBtn = findViewById(R.id.RegistrationButton)
        RegQn = findViewById(R.id.RegistrationPageQuestion)
        GroupId = findViewById(R.id.GroupId)
        RegQn?.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@RegistrationActivity, LoginActivity::class.java)
            startActivity(intent)
        })
        RegBtn?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(view: View) {
                val email = RegEmail?.getText().toString().trim { it <= ' ' }
                val password = RegPwd?.getText().toString().trim { it <= ' ' }
                val Groupid = GroupId?.getText().toString().trim { it <= ' ' }
                if (TextUtils.isEmpty(email)) {
                    RegEmail?.setError("A valid email is required")
                    return
                }
                if (TextUtils.isEmpty(Groupid)) {
                    GroupId?.setError("A valid Group is required")
                    return
                }
                if (TextUtils.isEmpty(password)) {
                    RegPwd?.setError("A valid password is required")
                    return
                } else {
                    loader!!.setMessage("Registration in progress")
                    loader!!.setCanceledOnTouchOutside(false)
                    loader!!.show()
                    mAuth!!.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                        OnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val currentUser = FirebaseAuth.getInstance().currentUser

                                // Obtener referencias a la base de datos
                                val databaseRef = FirebaseDatabase.getInstance().reference
                                val usersRef = databaseRef.child("users")

                                // Obtener datos relevantes
                                val email =
                                    currentUser!!.email // Correo electrónico del usuario actual
                                val userId = currentUser.uid // ID de usuario del usuario actual
                                val groupId = Groupid // ID del grupo obtenido del EditText

                                // Crear un nuevo nodo para el usuario actual
                                val userRef = usersRef.child(userId)

                                // Guardar los datos del usuario actual en el nodo correspondiente
                                userRef.child("email").setValue(email)
                                userRef.child("groupId").setValue(groupId)
                                val intent =
                                    Intent(this@RegistrationActivity, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                val error = task.exception.toString()
                                Toast.makeText(
                                    this@RegistrationActivity,
                                    "Registration failed: $error",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            loader!!.dismiss()
                        })
                }
            }
        })
    }
}
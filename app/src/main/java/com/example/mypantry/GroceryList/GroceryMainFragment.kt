package com.example.mypantry.GroceryList

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
import android.graphics.Color
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
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.example.mypantry.splash_activity
import com.example.mypantry.Adapters.IngredientsAdapter
import com.example.mypantry.Adapters.InstructionsAdapter
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GroceryMainFragment constructor() : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var reference: DatabaseReference? = null
    private var reference2: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var onlineUserID: String? = null
    private var loader: ProgressDialog? = null
    private var key: String? = ""
    private var groceryItem: String? = null
    public override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.grocery_main_activity, container, false)

        //getSupportActionBar().setTitle("");
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.recyclerView)
        val linearLayoutManager: LinearLayoutManager = LinearLayoutManager(getActivity())
        linearLayoutManager.setReverseLayout(true)
        linearLayoutManager.setStackFromEnd(true)
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.setLayoutManager(linearLayoutManager)

        //set up a loader
        loader = ProgressDialog(getActivity())
        mUser = mAuth!!.getCurrentUser()
        onlineUserID = mUser!!.getUid()
        reference = FirebaseDatabase.getInstance().getReference().child("Grocery Items").child(
            onlineUserID!!
        )
        reference2 = FirebaseDatabase.getInstance().getReference().child("Pantry Items").child(
            onlineUserID!!
        )
        floatingActionButton = view.findViewById(R.id.fab)
        floatingActionButton!!.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                addGroceryItem()
            }
        })
        return view
    }

    // function that adds a grocery item and uploads it to Firebase
    private fun addGroceryItem() {
        val myDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireActivity()
        ) //create a alert dialog
        val inflater: LayoutInflater = LayoutInflater.from(getActivity())


        // use the input_file layout as the view
        val myView: View = inflater.inflate(R.layout.grocery_add_item_activity, null)
        myDialog.setView(myView)
        val dialog: AlertDialog = myDialog.create()
        dialog.setCancelable(false)

        // make a round-corner dialog
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // initialize the texts and buttons
        val groceryItem: EditText = myView.findViewById(R.id.item)
        val save: Button = myView.findViewById(R.id.saveBtn)
        val cancel: Button = myView.findViewById(R.id.CancelBtn)

        // if the user clicks cancel button
        cancel.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                dialog.dismiss()
            }
        })

        // if the user clicks the save button
        save.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(v: View) {
                val mGroceryItem: String = groceryItem.getText().toString().trim({ it <= ' ' })
                val id: String? = reference!!.push().getKey() //get the key for each data set

//                String date = DateFormat.getDateInstance().format(new Date());
                if (TextUtils.isEmpty(mGroceryItem)) {
                    groceryItem.setError("Ingredient Required!")
                    return
                } else {
                    loader!!.setMessage("Adding your ingredient")
                    loader!!.setCanceledOnTouchOutside(false)
                    loader!!.show()

                    // use the Model class to pack up the data
                    val model: GroceryItem = GroceryItem(mGroceryItem, id)

                    // update the data to Firebase
                    reference!!.child((id)!!).setValue(model)
                        .addOnCompleteListener(object : OnCompleteListener<Void?> {
                            public override fun onComplete(groceryItem: Task<Void?>) {
                                if (groceryItem.isSuccessful()) {
                                    Toast.makeText(
                                        getActivity(),
                                        "grocery item has been added successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                } else {
                                    val error: String = groceryItem.getException().toString()
                                    Toast.makeText(
                                        getActivity(),
                                        "Failed: " + error,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                                loader!!.dismiss()
                            }
                        })
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    public override fun onStart() {
        super.onStart()
        val options: FirebaseRecyclerOptions<GroceryItem> =
            FirebaseRecyclerOptions.Builder<GroceryItem>().setQuery(
                (reference)!!, GroceryItem::class.java
            ).build()
        val adapter: FirebaseRecyclerAdapter<GroceryItem, MyViewHolder> =
            object : FirebaseRecyclerAdapter<GroceryItem, MyViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    @SuppressLint("RecyclerView") position: Int,
                    model: GroceryItem
                ) {
                    holder.setgroceryItem(model.groceryItem)

                    // Tap grocery item and pulls update dialog box
                    holder.mView.setOnClickListener(object : View.OnClickListener {
                        public override fun onClick(view: View) {
                            key = getRef(position).getKey()
                            groceryItem = model.groceryItem
                            updateGroceryItem()
                        }
                    })

                    // Hold down grocery item and checks off item
                    holder.mView.setOnLongClickListener(object : OnLongClickListener {
                        public override fun onLongClick(view: View): Boolean {
                            key = getRef(position).getKey()
                            groceryItem = model.groceryItem
                            val model: PantryItem = PantryItem(groceryItem, "", key, "")

                            // Adds item to Pantry
                            reference2!!.child((key)!!).setValue(model)
                                .addOnCompleteListener(object : OnCompleteListener<Void?> {
                                    public override fun onComplete(task: Task<Void?>) {
                                        Toast.makeText(
                                            getActivity(),
                                            "Ingredient has been transferred to Pantry",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                })

                            // Removes item from grocery
                            reference!!.child((key)!!).removeValue()
                                .addOnCompleteListener(object : OnCompleteListener<Void?> {
                                    public override fun onComplete(groceryItem: Task<Void?>) {
                                        if (groceryItem.isSuccessful()) {
                                            Toast.makeText(
                                                getActivity(),
                                                "Ingredient has been checked",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        } else {
                                            val error: String =
                                                groceryItem.getException().toString()
                                            Toast.makeText(
                                                getActivity(),
                                                "Delete failed :(" + error,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                })
                            return false
                        }
                    })
                }

                public override fun onCreateViewHolder(
                    parent: ViewGroup,
                    viewType: Int
                ): MyViewHolder {
                    val view: View = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.grocery_item_activity, parent, false)
                    return MyViewHolder(view)
                }
            }
        recyclerView!!.setAdapter(adapter)
        adapter.startListening()
    }

    class MyViewHolder constructor(var mView: View) : RecyclerView.ViewHolder(
        mView
    ) {
        fun setgroceryItem(task: String?) {
            val itemTextView: TextView = mView.findViewById(R.id.groceryItemTv)
            itemTextView.setText(task)
        }
    }

    private fun updateGroceryItem() {
        val myDialog: AlertDialog.Builder = AlertDialog.Builder(
            requireActivity()
        )
        val inflater: LayoutInflater = LayoutInflater.from(getActivity())
        val view: View = inflater.inflate(R.layout.grocery_item_update_activity, null)
        myDialog.setView(view)
        val dialog: AlertDialog = myDialog.create()
        dialog.getWindow()!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mItem: EditText = view.findViewById(R.id.mEditedItem)
        mItem.setText(groceryItem)
        mItem.setSelection(groceryItem!!.length)
        val deleteBtn: Button = view.findViewById(R.id.btnDelete)
        val updateBtn: Button = view.findViewById(R.id.btnUpdate)
        updateBtn.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                groceryItem = mItem.getText().toString().trim({ it <= ' ' })
                val model: GroceryItem = GroceryItem(groceryItem, key)
                reference!!.child((key)!!).setValue(model)
                    .addOnCompleteListener(object : OnCompleteListener<Void?> {
                        public override fun onComplete(groceryItem: Task<Void?>) {
                            if (groceryItem.isSuccessful()) {
                                Toast.makeText(
                                    getActivity(),
                                    "Ingredient has been updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val error: String = groceryItem.getException().toString()
                                Toast.makeText(
                                    getActivity(),
                                    "Update failed :(" + error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                dialog.dismiss()
            }
        })
        deleteBtn.setOnClickListener(object : View.OnClickListener {
            public override fun onClick(view: View) {
                reference!!.child((key)!!).removeValue()
                    .addOnCompleteListener(object : OnCompleteListener<Void?> {
                        public override fun onComplete(groceryItem: Task<Void?>) {
                            if (groceryItem.isSuccessful()) {
                                Toast.makeText(
                                    getActivity(),
                                    "Ingredient has been deleted successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                val error: String = groceryItem.getException().toString()
                                Toast.makeText(
                                    getActivity(),
                                    "Delete failed :(" + error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                dialog.dismiss()
            }
        })
        dialog.show()
    }
}
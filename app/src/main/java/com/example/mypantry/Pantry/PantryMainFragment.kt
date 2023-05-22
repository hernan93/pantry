package com.example.mypantry.Pantry

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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class PantryMainFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var floatingActionButton: FloatingActionButton? = null
    private var reference: DatabaseReference? = null
    private var mAuth: FirebaseAuth? = null
    private var mUser: FirebaseUser? = null
    private var onlineUserID: String? = null
    private var loader: ProgressDialog? = null
    private var key: String? = ""
    private var pantryItem: String? = null
    private var date: String? = null
    private var details: String? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.pantry_main_activity, container, false)
        mAuth = FirebaseAuth.getInstance()
        recyclerView = view.findViewById(R.id.recyclerView)
        val linearLayoutManager = LinearLayoutManager(activity)
        linearLayoutManager.reverseLayout = true
        linearLayoutManager.stackFromEnd = true
        recyclerView?.setHasFixedSize(true)
        recyclerView?.setLayoutManager(linearLayoutManager)

        //set up a loader
        loader = ProgressDialog(activity)
        mUser = mAuth!!.currentUser
        onlineUserID = mUser!!.uid
        reference = FirebaseDatabase.getInstance().reference.child("Pantry Items").child(
            onlineUserID!!
        )
        floatingActionButton = view.findViewById(R.id.fab)
        floatingActionButton.run {  }
        floatingActionButton?.setOnClickListener(View.OnClickListener { addPantryItem() })
        return view
    }

    // function that adds a pantry item and uploads it to Firebase
    private fun addPantryItem() {
        val myDialog = AlertDialog.Builder(
            requireActivity()
        ) //create a alert dialog
        val inflater = LayoutInflater.from(activity)

        // use the input_file layout as the view
        val myView = inflater.inflate(R.layout.pantry_add_item_activity, null)
        myDialog.setView(myView)
        val dialog = myDialog.create()
        dialog.setCancelable(false)

        // make a round-corner dialog
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        // initialize the texts and buttons
        val pantryItem = myView.findViewById<EditText>(R.id.item)
        val description = myView.findViewById<EditText>(R.id.description)
        val date = myView.findViewById<EditText>(R.id.date)
        val save = myView.findViewById<Button>(R.id.saveBtn)
        val cancel = myView.findViewById<Button>(R.id.CancelBtn)

        // if the user clicks cancel button
        cancel.setOnClickListener { dialog.dismiss() }

        // if the user clicks the save button
        save.setOnClickListener(View.OnClickListener {
            val mPantryItem = pantryItem.text.toString().trim { it <= ' ' }
            val mDetails = description.text.toString().trim { it <= ' ' }
            val mdate = date.text.toString().trim { it <= ' ' }
            val id = reference!!.push().key // get the key for each data set
            if (TextUtils.isEmpty(mPantryItem)) {
                pantryItem.error = "Pantry Item Required"
                return@OnClickListener
            } else {
                loader!!.setMessage("Adding your pantry item")
                loader!!.setCanceledOnTouchOutside(false)
                loader!!.show()

                // use the Model class to pack up the data
                val model = PantryItem(mPantryItem, mDetails, id, mdate)

                // update the data to Firebase
                reference!!.child(id!!).setValue(model).addOnCompleteListener { pantryItem ->
                    if (pantryItem.isSuccessful) {
                        Toast.makeText(
                            activity,
                            "Pantry item has been added successfully",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val error = pantryItem.exception.toString()
                        Toast.makeText(activity, "Failed: $error", Toast.LENGTH_SHORT).show()
                    }
                    loader!!.dismiss()
                }
            }
            dialog.dismiss()
        })
        dialog.show()
    }

    override fun onStart() {
        super.onStart()
        val options = FirebaseRecyclerOptions.Builder<PantryItem>().setQuery(
            reference!!, PantryItem::class.java
        ).build()
        val adapter: FirebaseRecyclerAdapter<PantryItem, MyViewHolder> =
            object : FirebaseRecyclerAdapter<PantryItem, MyViewHolder>(options) {
                override fun onBindViewHolder(
                    holder: MyViewHolder,
                    @SuppressLint("RecyclerView") position: Int,
                    model: PantryItem
                ) {
                    holder.setDate(model.date)
                    holder.setPantryItem(model.pantryItem)
                    holder.setDesc(model.details)
                    holder.mView.setOnClickListener {
                        key = getRef(position).key
                        pantryItem = model.pantryItem
                        details = model.details
                        date = model.date
                        updatePantryItem()
                    }
                }

                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
                    val view = LayoutInflater.from(parent.context)
                        .inflate(R.layout.pantry_item_activity, parent, false)
                    return MyViewHolder(view)
                }
            }
        recyclerView!!.adapter = adapter
        adapter.startListening()
    }

    class MyViewHolder(var mView: View) : RecyclerView.ViewHolder(
        mView
    ) {
        fun setPantryItem(task: String?) {
            val itemTextView = mView.findViewById<TextView>(R.id.pantryItemTv)
            itemTextView.text = task
        }

        fun setDesc(desc: String?) {
            val descTextView = mView.findViewById<TextView>(R.id.detailsTv)
            descTextView.text = desc
        }

        fun setDate(date: String?) {
            val dateTextView = mView.findViewById<TextView>(R.id.dateTv)
            dateTextView.text = date
        }
    }

    private fun updatePantryItem() {
        val myDialog = AlertDialog.Builder(
            requireActivity()
        )
        val inflater = LayoutInflater.from(activity)
        val view = inflater.inflate(R.layout.pantry_item_update_activity, null)
        myDialog.setView(view)
        val dialog = myDialog.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val mItem = view.findViewById<EditText>(R.id.mEditedItem)
        val mDetails = view.findViewById<EditText>(R.id.mEditedDetails)
        val mDate = view.findViewById<EditText>(R.id.mEditedDate)
        mItem.setText(pantryItem)
        mItem.setSelection(pantryItem!!.length)
        mDetails.setText(details)
        mDetails.setSelection(details!!.length)
        mDate.setText(date)
        mDate.setSelection(date!!.length)
        val deleteBtn = view.findViewById<Button>(R.id.btnDelete)
        val updateBtn = view.findViewById<Button>(R.id.btnUpdate)
        updateBtn.setOnClickListener {
            pantryItem = mItem.text.toString().trim { it <= ' ' }
            details = mDetails.text.toString().trim { it <= ' ' }
            date = mDate.text.toString().trim { it <= ' ' }
            val model = PantryItem(pantryItem, details, key, date)
            reference!!.child(key!!).setValue(model).addOnCompleteListener { pantryItem ->
                if (pantryItem.isSuccessful) {
                    Toast.makeText(
                        activity,
                        "Pantry item has been updated successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val error = pantryItem.exception.toString()
                    Toast.makeText(activity, "Update failed$error", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        deleteBtn.setOnClickListener {
            reference!!.child(key!!).removeValue().addOnCompleteListener { pantryItem ->
                if (pantryItem.isSuccessful) {
                    Toast.makeText(
                        activity,
                        "Item has been deleted successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    val error = pantryItem.exception.toString()
                    Toast.makeText(activity, "Delete failed$error", Toast.LENGTH_SHORT).show()
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    } /*
    //set up the log out function
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout:
                mAuth.signOut();
                Intent intent = new Intent(PantryActivity.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
    */
}
package com.example.mypantry

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.fragment.app.Fragment
import com.example.mypantry.Recipes.RecipesMainFragment
import com.google.android.material.navigation.NavigationBarView
import com.example.mypantry.R
import com.example.mypantry.GroceryList.GroceryMainFragment
import com.example.mypantry.Pantry.PantryMainFragment
import com.example.mypantry.Profile.ProfileFragment
import com.example.mypantry.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var binding: ActivityMainBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        replaceFragment(GroceryMainFragment())
        binding!!.bottomNavigationView.setOnItemSelectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.Grocery_List -> replaceFragment(GroceryMainFragment())
                R.id.Pantry -> replaceFragment(PantryMainFragment())
                R.id.Profile -> replaceFragment(ProfileFragment())
               // R.id.Recipes -> replaceFragment(RecipesMainFragment())
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}
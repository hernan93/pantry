package com.example.mypantry;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;

import com.example.mypantry.GroceryList.GroceryMainFragment;
import com.example.mypantry.Pantry.PantryMainFragment;
import com.example.mypantry.Profile.ProfileFragment;
import com.example.mypantry.Recipes.RecipesMainFragment;
import com.example.mypantry.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new RecipesMainFragment());


        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch  (item.getItemId()){

                case R.id.Grocery_List:
                    replaceFragment(new GroceryMainFragment());
                    break;
                case R.id.Pantry:
                    replaceFragment(new PantryMainFragment());
                    break;
                case R.id.Profile:
                    replaceFragment(new ProfileFragment());
                    break;
                case R.id.Recipes:
                    replaceFragment(new RecipesMainFragment());
                    break;
            }

            return true;
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
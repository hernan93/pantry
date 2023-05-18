package com.example.mypantry.Recipes;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.fragment.app.FragmentManager;


import com.example.mypantry.R;

public class RecipesMainFragment extends Fragment {

    Button gotoRandomRecipesSearch;
    Button gotoComplexRecipesSearch;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recipes_main, container, false);

        gotoRandomRecipesSearch = view.findViewById(R.id.btnClickRandomRecipes);
        gotoComplexRecipesSearch = view.findViewById(R.id.btnClickSearchRecipes);

        gotoRandomRecipesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment RandomRecipesSearch = new RandomRecipesOptionFragment();
                FragmentTransaction fragmentTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTrans.replace(R.id.frame_layout, RandomRecipesSearch);
                fragmentTrans.commit();
            }
        });

        gotoComplexRecipesSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment ComplexRecipesSearch = new ComplexRecipeSearchOptionFragment();
                FragmentTransaction fragmentTrans = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTrans.replace(R.id.frame_layout, ComplexRecipesSearch);
                fragmentTrans.commit();
            }
        });


        return view;
    }
}

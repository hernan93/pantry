package com.example.mypantry.Recipes;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mypantry.Adapters.ComplexRecipeAdapter;
import com.example.mypantry.Listeners.ComplexRecipeResponseListener;
import com.example.mypantry.Listeners.RecipeClickListener;
import com.example.mypantry.Models.ComplexRecipeApiResponse;
import com.example.mypantry.R;
import com.example.mypantry.RecipeDetailsFragment;
import com.example.mypantry.RequestManager;

import java.util.ArrayList;
import java.util.List;

public class ComplexRecipeSearchOptionFragment extends Fragment {
    ProgressDialog dialog;
    RequestManager manager;
    ComplexRecipeAdapter complexRecipeAdapter;
    RecyclerView recyclerView;
    SearchView searchView;
    List<String> tags = new ArrayList<>();

    public ComplexRecipeSearchOptionFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container, Bundle savedInstanceState)
    {
        super.onCreateView(inflater, container, savedInstanceState);
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_recipes, container, false);

        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading...");

        searchView = view.findViewById(R.id.searchView_home);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                tags.clear();
                tags.add(query);
                manager.getComplexRecipes(complexRecipeResponseListener, tags);
                dialog.show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) { return false;}
        });
        manager = new RequestManager(getActivity());
        manager.getComplexRecipes(complexRecipeResponseListener, tags);
        dialog.show();
        return view;
    }

    private final ComplexRecipeResponseListener complexRecipeResponseListener =
            new ComplexRecipeResponseListener() {
        @Override
        public void didFetch(ComplexRecipeApiResponse response, String message) {
            dialog.dismiss();
            recyclerView = requireView().findViewById(R.id.recycler_random);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
            complexRecipeAdapter = new ComplexRecipeAdapter(getActivity(),
                    response.results, recipeClickListener);
            recyclerView.setAdapter(complexRecipeAdapter);
        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final RecipeClickListener recipeClickListener = new RecipeClickListener() {
        @Override
        public void onRecipeClicked(String id) {
            Bundle bundle = new Bundle();
            bundle.putString("id", id);
            Fragment RecipeDetailsFragment = new RecipeDetailsFragment();
            RecipeDetailsFragment.setArguments(bundle);
            FragmentTransaction fragmentTrans = getActivity().getSupportFragmentManager()
                    .beginTransaction();
            fragmentTrans.replace(R.id.frame_layout, RecipeDetailsFragment);
            fragmentTrans.commit();

        }
    };

}
package com.example.mypantry;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mypantry.Adapters.IngredientsAdapter;
import com.example.mypantry.Adapters.InstructionsAdapter;
import com.example.mypantry.Listeners.InstructionsListener;
import com.example.mypantry.Listeners.RecipeDetailsListener;
import com.example.mypantry.Models.InstructionsResponse;
import com.example.mypantry.Models.RecipeDetailsResponse;
import com.squareup.picasso.Picasso;

import java.util.List;


public class RecipeDetailsFragment extends Fragment {
    int id;
    TextView textView_meal_name, textView_meal_source, textView_meal_summary;
    ImageView imageView_meal_image;
    RecyclerView recycler_meal_ingredients, recycler_meal_instructions;
    RequestManager manager;
    ProgressDialog dialog;
    IngredientsAdapter ingredientsAdapter;
    InstructionsAdapter instructionsAdapter;

    public RecipeDetailsFragment() {
        // Required empty public constructor
    }
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_recipe_details, container, false);

        findViews(view);

        id = Integer.parseInt(getArguments().getString("id"));
        //id = Integer.parseInt(savedInstanceState.getString("id"));

        manager = new RequestManager(getActivity());
        manager.getRecipeDetails(recipeDetailsListener, id);
        manager.getInstructions(instructionsListener, id);
        dialog = new ProgressDialog(getActivity());
        dialog.setTitle("Loading Details...");
        dialog.show();

        return view;
    }

    private void findViews(View view) {

        textView_meal_name = view.findViewById(R.id.textView_meal_name);
        textView_meal_source = view.findViewById(R.id.textView_meal_source);
        textView_meal_summary = view.findViewById(R.id.textView_meal_summary);
        imageView_meal_image = view.findViewById(R.id.imageView_meal_image);
        recycler_meal_ingredients = view.findViewById(R.id.recycler_meal_ingredients);
        recycler_meal_instructions = view.findViewById(R.id.recycler_meal_instructions);

    }

    private final RecipeDetailsListener recipeDetailsListener = new RecipeDetailsListener() {
        @Override
        public void didFetch(RecipeDetailsResponse response, String message) {
            dialog.dismiss();
            textView_meal_name.setText(response.title);
            textView_meal_source.setText(response.sourceName);
            textView_meal_summary.setText(response.summary);
            Picasso.get().load(response.image).into(imageView_meal_image);

            recycler_meal_ingredients.setHasFixedSize(true);
            recycler_meal_ingredients.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false));
            ingredientsAdapter = new IngredientsAdapter(getActivity(), response.extendedIngredients);
            recycler_meal_ingredients.setAdapter(ingredientsAdapter);

        }

        @Override
        public void didError(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    };

    private final InstructionsListener instructionsListener = new InstructionsListener() {
        @Override
        public void didFetch(List<InstructionsResponse> response, String message) {
            recycler_meal_instructions.setHasFixedSize(true);
            recycler_meal_instructions.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
            instructionsAdapter = new InstructionsAdapter(getActivity(), response);
            recycler_meal_instructions.setAdapter(instructionsAdapter);
        }

        @Override
        public void didError(String message) {

        }
    };
}
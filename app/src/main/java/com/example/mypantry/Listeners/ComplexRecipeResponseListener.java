package com.example.mypantry.Listeners;

import com.example.mypantry.Models.ComplexRecipeApiResponse;

public interface ComplexRecipeResponseListener {
    void didFetch(ComplexRecipeApiResponse response, String message);
    void didError(String message);
}

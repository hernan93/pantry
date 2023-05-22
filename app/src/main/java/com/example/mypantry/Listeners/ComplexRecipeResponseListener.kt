package com.example.mypantry.Listeners

import com.example.mypantry.Models.ComplexRecipeApiResponse

open interface ComplexRecipeResponseListener {
    fun didFetch(response: ComplexRecipeApiResponse?, message: String?)
    fun didError(message: String?)
}
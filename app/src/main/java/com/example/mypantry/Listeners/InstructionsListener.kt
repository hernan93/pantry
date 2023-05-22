package com.example.mypantry.Listeners

import com.example.mypantry.Models.InstructionsResponse

open interface InstructionsListener {
    fun didFetch(response: List<InstructionsResponse>, message: String?)
    fun didError(message: String?)
}
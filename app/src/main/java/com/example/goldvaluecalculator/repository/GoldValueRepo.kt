package com.example.goldvaluecalculator.repository

import com.example.goldvaluecalculator.model.Gold
import com.example.goldvaluecalculator.model.GoldValues
import com.example.goldvaluecalculator.service.GoldValueAPI
import com.example.goldvaluecalculator.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class GoldValueRepo @Inject constructor(
    private val api : GoldValueAPI
) {

    suspend fun getGoldValues() : Resource<Gold> {
        val response = try {
            api.getGoldValues()
        }catch (e: Exception){
            return Resource.Error("Error: Check your internet connection!")
        }
        return Resource.Success(response)
    }

}
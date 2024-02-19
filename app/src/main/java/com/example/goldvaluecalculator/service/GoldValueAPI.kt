package com.example.goldvaluecalculator.service

import com.example.goldvaluecalculator.model.Gold
import com.example.goldvaluecalculator.model.GoldValues
import retrofit2.http.GET
import retrofit2.http.Headers

interface GoldValueAPI {

    @GET("economy/goldPrice")
    @Headers(
        "authorization: apikey 3PdZvpcsIc15dmh6eT0ivP:3or76tJZJafRDH18jtFvqY",
        "content-type: application/json"
    )
    suspend fun getGoldValues() : Gold
}
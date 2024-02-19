package com.example.goldvaluecalculator.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.goldvaluecalculator.model.Gold
import com.example.goldvaluecalculator.model.GoldValues
import com.example.goldvaluecalculator.repository.GoldValueRepo
import com.example.goldvaluecalculator.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoldValueCalculatorViewModel  @Inject constructor(
    val repo : GoldValueRepo
) : ViewModel()
{
    var totalValue = mutableStateOf<Float?>(null)
    var halfItem = mutableStateOf<GoldValues?>(null)
    val halfGold = mutableStateOf<Float?>(0f)
    var quarterItem = mutableStateOf<GoldValues?>(null)
    val quarterGold = mutableStateOf<Float?>(0f)
    var gramItem = mutableStateOf<GoldValues?>(null)
    val gramGold = mutableStateOf<Float?>(0f)
    var isLoading = mutableStateOf(false)
    var errorMessage = mutableStateOf("")

    init {
        loadGoldValues()
    }

    fun loadGoldValues(){
        viewModelScope.launch {
            isLoading.value = true
            //delay(500)
            val result = repo.getGoldValues()
            when(result){
                is Resource.Success -> {
                     halfItem.value = result.data?.result?.find {
                        it.name == "Yarım Altın"
                    }
                    quarterItem.value = result.data?.result?.find {
                        it.name == "Çeyrek Altın"
                    }
                    gramItem.value = result.data?.result?.find {
                        it.name == "Gram Altın"
                    }
                    halfGold.value = halfItem.value?.selling
                    quarterGold.value = quarterItem.value?.selling
                    gramGold.value = gramItem.value?.selling

                    errorMessage.value = ""
                    isLoading.value = false

                }
                is Resource.Error -> {
                    errorMessage.value = result.message!!
                    isLoading.value = false
                }
                else -> {
                    isLoading.value = true
                }
            }
        }
    }

    fun calculateValue(halfCount: Float = 0f, quarterCount : Float = 0f, gramCount : Float = 0f){
        viewModelScope.launch {
            totalValue.value = (halfCount * (halfGold.value ?: 0f)) + (quarterCount * (quarterGold.value ?: 0f)) + (gramCount * (gramGold.value ?: 0f))
        }

    }

}
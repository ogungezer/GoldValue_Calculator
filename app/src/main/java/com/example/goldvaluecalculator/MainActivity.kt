package com.example.goldvaluecalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Indication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.interaction.InteractionSource
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.goldvaluecalculator.common.customTextField
import com.example.goldvaluecalculator.ui.theme.GoldValueCalculatorTheme
import com.example.goldvaluecalculator.ui.theme.PrettyBlack
import com.example.goldvaluecalculator.ui.theme.Yellow
import com.example.goldvaluecalculator.viewmodel.GoldValueCalculatorViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GoldValueCalculatorTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = PrettyBlack, titleContentColor = Color.White) ,
                            title = { Text(text = "Altın Toplayıcı") },
                        )
                    }
                ) {padding ->
                    GoldValueApp(padding = padding)
                }

            }
        }
    }
}


@Composable
fun GoldValueApp(padding : PaddingValues, viewModel: GoldValueCalculatorViewModel = hiltViewModel()) {
    val isError by remember { viewModel.errorMessage }
    val isLoading by remember { viewModel.isLoading }

    Box(modifier = Modifier
        .background(Yellow)
        .fillMaxSize(), contentAlignment = Alignment.Center) {
        if(isLoading) {
            Column(verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator(color = PrettyBlack)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = "Güncel altın fiyatları alınıyor..", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = PrettyBlack)
            }
        }
        else if (isError.isNotEmpty()) {
            ErrorMessage(error = isError, onRetry = {viewModel.loadGoldValues()})
        }
        else{
            GoldValueCalculator(padding = padding)
        }

    }

}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun GoldValueCalculator(viewModel: GoldValueCalculatorViewModel = hiltViewModel(), padding : PaddingValues){
    var halfGoldCount by remember { mutableStateOf("")}
    var quarterGoldCount by remember { mutableStateOf("")}
    var gramGoldCount by remember { mutableStateOf("")}
    val totalPrice = viewModel.totalValue.value
    val controller = LocalSoftwareKeyboardController.current // klavyenin kapanıp açılmasını kontrol ediyor.

    Box(modifier = Modifier
        .background(color = Color.Transparent)
        .fillMaxSize()
        .clickable(
            indication = null, // tıklamada ripple efektini kaldırmak için
            interactionSource = remember { MutableInteractionSource() }
        ) {
            controller?.hide()
        })
    {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(color = Yellow)
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = "ALTIN TOPLAYICI",
                color = PrettyBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 36.sp,
                modifier = Modifier.padding(8.dp),
            )

            Spacer(modifier = Modifier.height(24.dp))
            halfGoldCount = customTextField(label = "Yarım altın miktarını giriniz")
            Spacer(modifier = Modifier.height(8.dp))

            quarterGoldCount = customTextField(label = "Çeyrek altın miktarını giriniz")
            Spacer(modifier = Modifier.height(8.dp))

            gramGoldCount = customTextField(label = "Gram altın miktarını giriniz")
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    viewModel.calculateValue(
                        halfCount = halfGoldCount.toFloatOrNull() ?: 0f,
                        quarterCount = quarterGoldCount.toFloatOrNull() ?: 0f,
                        gramCount = gramGoldCount.toFloatOrNull() ?: 0f
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = PrettyBlack)
            ) {
                Text(
                    text = "Hesapla",
                    modifier = Modifier.padding(6.dp),
                    color = Yellow,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
            Spacer(modifier = Modifier.height(20.dp))
            if (totalPrice != null) {
                Text(
                    text = "Toplam Tutar: $totalPrice TL",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }

        }
    }
}

@Composable
fun ErrorMessage(
    error : String,
    onRetry : () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .background(Yellow)
            .fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(150.dp))
        Text(text = error, color = PrettyBlack, fontWeight = FontWeight.SemiBold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(12.dp))
        Button(
            onClick = onRetry,
            colors = ButtonDefaults.buttonColors(containerColor = PrettyBlack),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(
                text = "Retry",
                color = Yellow,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "UYARI: İNTERNET BAĞLANTINDA SIKINTI OLMAMASINA RAĞMEN BU SAYFAYI GÖRÜYORSAN API NİN KULLANIMI AŞILMIŞTIR.",
            modifier = Modifier.padding(16.dp),
            fontSize = 14.sp,
            textAlign = TextAlign.Justify
        )
    }
}

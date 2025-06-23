package com.example.my_projet

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.ui.res.painterResource
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.my_projet.ui.theme.My_ProjetTheme
import com.example.my_projet.nav.AppNavigation
import com.example.my_projet.ui.product.ProductViewModel

import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModels<ProductViewModel>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            My_ProjetTheme {
                Surface{
                    AppNavigation(viewModel)
                }
            }
        }
    }
}



@Composable
fun MyComposable(id: String) {
    LaunchedEffect(id) {
        // Coroutine démarrée quand `id` change
        delay(1000)
        println("Chargement terminé pour $id")
    }
}

@Composable
fun MyDisposable() {
    DisposableEffect(Unit) {
        println("Composé")

        onDispose {
            println("Nettoyé")
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

val imageModifier = Modifier
    .size(150.dp)
    .border(BorderStroke(1.dp, Color.Black))
    .background(Color.Yellow)

@Composable  fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.ml),
        contentDescription = stringResource(R.string.manga_lighter),
        contentScale = ContentScale.Fit,
        modifier = imageModifier
    )
}

@Composable
fun LogoCard(modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        CounterScreen()
        // Appel de votre composable Logo à l’intérieur de la Card
        Logo()
    }
}

@Composable
fun Counter(count: Int, onIncrement: () -> Unit) {
    Button(onClick = onIncrement) {
        Text("Vous avez cliqué $count fois")
    }
}

@Composable
fun CounterScreen() {
    var count by rememberSaveable { mutableStateOf(0) }
    Counter(count = count, onIncrement = { count++ })
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    My_ProjetTheme {
        Column {
            Greeting("Android")
            CounterScreen()
            LogoCard()
        }

    }
}



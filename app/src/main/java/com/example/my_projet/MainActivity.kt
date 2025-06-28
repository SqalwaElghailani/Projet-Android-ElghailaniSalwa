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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.my_projet.ui.theme.My_ProjetTheme
import com.example.my_projet.nav.AppNavigation
import com.example.my_projet.ui.product.ProductViewModel
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Build
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import java.util.Locale
import androidx.compose.runtime.saveable.rememberSaveable


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: ProductViewModel by viewModels()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val savedLang = LocaleHelper.getSavedLanguage(this)
        val localeContext = LocaleHelper.setLocale(this, savedLang)

        enableEdgeToEdge()
        setContent {
            CompositionLocalProvider(LocalContext provides localeContext) {
                My_ProjetTheme {
                    Surface {
                        AppNavigation(
                            viewModel = viewModel,
                            onLanguageSelected = { langCode ->
                                val newContext = LocaleHelper.setLocale(this, langCode)
                                val intent = Intent(newContext, MainActivity::class.java)
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK
                                startActivity(intent)
                                finish()
                            }
                        )
                    }
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



object LocaleHelper {
    private const val PREF_LANGUAGE = "selected_language"
    private const val PREF_NAME = "app_prefs"

    fun setLocale(context: Context, language: String): Context {
        saveLanguage(context, language)

        val locale = Locale(language)
        Locale.setDefault(locale)
        val config = Configuration(context.resources.configuration)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        } else {
            config.locale = locale
            context.resources.updateConfiguration(config, context.resources.displayMetrics)
            return context
        }
    }

    private fun saveLanguage(context: Context, language: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        prefs.edit().putString(PREF_LANGUAGE, language).apply()
    }

    fun getSavedLanguage(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return prefs.getString(PREF_LANGUAGE, Locale.getDefault().language) ?: "en"
    }
}



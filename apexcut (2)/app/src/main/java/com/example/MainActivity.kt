package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.AIAudioScreen
import com.example.ui.screens.AIEnhanceScreen
import com.example.ui.screens.AIToolsScreen
import com.example.ui.screens.AIVideoGeneratorScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.PhotoEditorScreen
import com.example.ui.screens.PremiumScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.TemplatesScreen
import com.example.ui.screens.VideoEditorScreen
import com.example.ui.theme.ApexDarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.ApexCutViewModel
import com.example.ui.viewmodel.CurrentScreen

class MainActivity : ComponentActivity() {

    private val viewModel: ApexCutViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme(darkTheme = true) {
                // Set Arabic RTL Layout Direction
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(ApexDarkBackground)
                    ) {
                        Crossfade(
                            targetState = currentScreen,
                            label = "ScreenTransition"
                        ) { screen ->
                            when (screen) {
                                is CurrentScreen.Splash -> SplashScreen(
                                    onSplashFinished = { viewModel.finishSplash() }
                                )
                                is CurrentScreen.Home -> HomeScreen(viewModel = viewModel)
                                is CurrentScreen.VideoEditor -> VideoEditorScreen(viewModel = viewModel)
                                is CurrentScreen.PhotoEditor -> PhotoEditorScreen(viewModel = viewModel)
                                is CurrentScreen.AIEnhance -> AIEnhanceScreen(viewModel = viewModel)
                                is CurrentScreen.AIAudio -> AIAudioScreen(viewModel = viewModel)
                                is CurrentScreen.AITools -> AIToolsScreen(viewModel = viewModel)
                                is CurrentScreen.Templates -> TemplatesScreen(viewModel = viewModel)
                                is CurrentScreen.AIVideoGen -> AIVideoGeneratorScreen(viewModel = viewModel)
                                is CurrentScreen.Premium -> PremiumScreen(
                                    viewModel = viewModel,
                                    onClose = { viewModel.navigateTo(CurrentScreen.Home) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

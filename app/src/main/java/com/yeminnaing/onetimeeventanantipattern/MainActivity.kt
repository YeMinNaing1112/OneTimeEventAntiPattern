package com.yeminnaing.onetimeeventanantipattern

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.yeminnaing.onetimeeventanantipattern.ui.theme.OneTimeEventAnAntiPatternTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OneTimeEventAnAntiPatternTheme {
                val navController = rememberNavController()
                val viewModel = viewModel<MainViewModel>()
                val state = viewModel.state


                ObserveAsEvent(flow = viewModel.navigationChannelFlow) { event ->
                    when (event) {
                        NavigationEvent.NavigateToProfile -> {
                            navController.navigate(NavigationScreens.Profile)
                        }
                    }

                }

                NavHost(
                    navController = navController,
                    startDestination = NavigationScreens.Login
                ) {
                    composable<NavigationScreens.Login> {
                        LogInScreen(state = state) {
                            viewModel.login()
                        }
                    }
                    composable<NavigationScreens.Profile> {
                        Profile()
                    }
                }
            }
        }
    }
}

@Composable
fun <T> ObserveAsEvent(flow: Flow<T>, event: (T) -> Unit) {
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(flow, lifecycleOwner.lifecycle) {
        lifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
            //region AboutWiithContext
            //this immediate run first in main thread before ui recompose
            // so it prevent event form lost while config change
            //endregion
            withContext(Dispatchers.Main.immediate){
                flow.collect(event)
            }

        }
    }
}

@Composable
fun LogInScreen(state: LoginState, login: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Button(onClick = { login() }) {
                Text(text = "Login")
            }
            if (state.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
        }
    }
}

@Composable
fun Profile() {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(modifier = Modifier.align(Alignment.Center)) {
            Button(onClick = { }) {
                Text(text = "Profile")
            }

        }
    }
}
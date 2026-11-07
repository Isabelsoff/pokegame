package com.example.pokegame.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokegame.ui.analysis.TeamAnalysisScreen
import com.example.pokegame.ui.auth.AuthViewModel
import com.example.pokegame.ui.auth.LoginScreen
import com.example.pokegame.ui.pokemondetail.PokemonDetailScreen
import com.example.pokegame.ui.pokemonlist.PokemonListScreen
import com.example.pokegame.ui.team.TeamScreen
import com.example.pokegame.ui.team.TeamViewModel

@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val teamViewModel: TeamViewModel = viewModel()
    val authViewModel: AuthViewModel = viewModel()
    val team by teamViewModel.team.collectAsState()
    val authState by authViewModel.state.collectAsState()

    val startDestination = remember {
        if (authViewModel.state.value.isLoggedIn) "list" else "login"
    }

    // Con sesión se carga el equipo guardado; sin sesión se limpia lo local
    LaunchedEffect(authState.isLoggedIn) {
        if (authState.isLoggedIn) teamViewModel.loadTeam() else teamViewModel.clearTeam()
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("login") {
            LoginScreen(
                authViewModel = authViewModel,
                onLoggedIn = {
                    navController.navigate("list") {
                        popUpTo("login") { inclusive = true }
                    }
                }
            )
        }
        composable("list") {
            PokemonListScreen(
                teamCount = team.size,
                onPokemonClick = { name -> navController.navigate("detail/$name") },
                onTeamClick = { navController.navigate("team") },
                onSignOut = {
                    authViewModel.signOut()
                    navController.navigate("login") {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }
        composable(
            route = "detail/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) {
            PokemonDetailScreen(
                onBack = { navController.popBackStack() },
                teamViewModel = teamViewModel
            )
        }
        composable("team") {
            TeamScreen(
                onBack = { navController.popBackStack() },
                onAnalyzeClick = { navController.navigate("analysis") },
                teamViewModel = teamViewModel
            )
        }
        composable("analysis") {
            TeamAnalysisScreen(
                onBack = { navController.popBackStack() },
                teamViewModel = teamViewModel
            )
        }
    }
}
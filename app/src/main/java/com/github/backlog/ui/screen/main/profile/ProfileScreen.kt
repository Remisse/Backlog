package com.github.backlog.ui.screen.main.profile

import android.os.Bundle
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelFactoryStore

class ProfileScreen(
    drawerState: DrawerState,
    vmFactories: ViewModelFactoryStore
) : MainScreen(drawerState, vmFactories) {

    override val section: Section = Section.Profile

    @Composable
    override fun Content(arguments: Bundle?) {
        val profileViewModel = profileViewModel()
        val gameViewModel = gameViewModel()
        val taskViewModel = taskViewModel()

        ProfileScreenContent(
            profileName = profileViewModel.profileName,
            profileBio = profileViewModel.profileBio,
            profileImage = profileViewModel.profileImage,
            onImageSelect = { profileViewModel.saveProfileImage(it) },
            onNameChange = { profileViewModel.saveProfileName(it) },
            onBioChange = { profileViewModel.saveProfileBio(it) },
            games = gameViewModel.backlog
                .collectAsState(initial = emptyList())
                .value,
            tasks = taskViewModel.tasksWithGameTitle
                .collectAsState(initial = emptyList())
                .value,
            completedGamesByGenre = gameViewModel.completedGamesByGenre
                .collectAsState(initial = emptyList())
                .value,
            completedGamesByDeveloper = gameViewModel.completedGamesByDeveloper
                .collectAsState(initial = emptyList())
                .value,
            droppedGamesByGenre = gameViewModel.droppedGamesByGenre
                .collectAsState(initial = emptyList())
                .value,
            droppedGamesByDeveloper = gameViewModel.droppedGamesByDeveloper
                .collectAsState(initial = emptyList())
                .value,
            completedGamesInCurrentYear = gameViewModel.completedGamesInCurrentYear
                .collectAsState(initial = 0)
                .value
        )
    }

    @Composable
    override fun Fab() {}

    @Composable
    override fun TopBarExtraButtons() {}
}

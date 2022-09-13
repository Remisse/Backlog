package com.github.backlog.ui.screen.main.profile

import android.os.Bundle
import androidx.compose.material.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import com.github.backlog.Section
import com.github.backlog.ui.screen.main.MainScreen
import com.github.backlog.utils.ViewModelContainer
import com.github.backlog.utils.ViewModelContainerAccessor

class ProfileScreen(
    drawerState: DrawerState,
    accessor: ViewModelContainerAccessor
) : MainScreen(drawerState, accessor) {

    override val section: Section = Section.Profile

    @Composable
    override fun Content(arguments: Bundle?) {
        ProfileScreenContent(
            profileName = viewModelContainer().profileViewModel.profileName,
            profileBio = viewModelContainer().profileViewModel.profileBio,
            profileImage = viewModelContainer().profileViewModel.profileImage,
            onImageSelect = {
                viewModelContainer().profileViewModel.saveProfileImage(it)
            },
            onNameChange = {
                viewModelContainer().profileViewModel.saveProfileName(it)
            },
            onBioChange = {
                viewModelContainer().profileViewModel.saveProfileBio(it)
            },
            games = viewModelContainer().gameViewModel.backlog
                .collectAsState(initial = emptyList())
                .value,
            tasks = viewModelContainer().taskViewModel.tasksWithGameTitle
                .collectAsState(initial = emptyList())
                .value,
            completedGamesByGenre = viewModelContainer().gameViewModel.completedGamesByGenre
                .collectAsState(initial = emptyList())
                .value,
            completedGamesByDeveloper = viewModelContainer().gameViewModel.completedGamesByDeveloper
                .collectAsState(initial = emptyList())
                .value,
            droppedGamesByGenre = viewModelContainer().gameViewModel.droppedGamesByGenre
                .collectAsState(initial = emptyList())
                .value,
            droppedGamesByDeveloper = viewModelContainer().gameViewModel.droppedGamesByDeveloper
                .collectAsState(initial = emptyList())
                .value,
            completedGamesInCurrentYear = viewModelContainer().gameViewModel.completedGamesInCurrentYear
                .collectAsState(initial = 0)
                .value
        )
    }

    @Composable
    override fun Fab() {}

    @Composable
    override fun TopBarExtraButtons() {}
}

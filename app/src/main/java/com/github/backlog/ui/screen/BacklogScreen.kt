package com.github.backlog.ui.screen

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavHostController
import com.github.backlog.Section

interface BacklogScreen {

    val section: Section

    @Composable
    fun Content(arguments: Bundle?)

    @Composable
    fun BottomBar(navController: NavHostController, sections: List<Section>)

    @Composable
    fun TopBar()

    @Composable
    fun Fab()

    val viewModelStoreOwner: ViewModelStoreOwner
}

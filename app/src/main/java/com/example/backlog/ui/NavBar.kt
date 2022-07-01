package com.example.backlog.ui

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.backlog.ui.theme.BacklogTheme
import com.example.backlog.R

@Composable
fun BottomNavigationBar(
    onClickGameList: () -> Unit,
    onClickTaskList: () -> Unit,
    onClickProfile: () -> Unit
) {
    BacklogTheme() {
        BottomNavigation(backgroundColor = MaterialTheme.colors.background) {
            BottomNavigationItem(
                selected = true,
                onClick = onClickGameList,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Games,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.nav_game_list))
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = onClickTaskList,
                icon = {
                    Icon(
                        imageVector = Icons.Default.Task,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.nav_tasks))
                }
            )
            BottomNavigationItem(
                selected = false,
                onClick = onClickProfile,
                icon = {
                    Icon(
                        imageVector = Icons.Default.AccountBox,
                        contentDescription = null
                    )
                },
                label = {
                    Text(stringResource(R.string.nav_profile))
                }
            )
        }
    }
}

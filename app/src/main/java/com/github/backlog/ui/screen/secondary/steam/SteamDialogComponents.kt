package com.github.backlog.ui.screen.secondary.steam

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Help
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.github.backlog.R
import com.github.backlog.ui.components.TextLabel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SteamDialogContent(
    heading: String,
    onDismissRequest: () -> Unit,
    onConfirmClick: (String) -> Unit
) {
    var steamId by rememberSaveable { mutableStateOf("") }
    var isInfoExpanded by remember { mutableStateOf(false) }

    val transition = updateTransition(targetState = isInfoExpanded, label = "ExpandClick")
    val rotation = transition.animateFloat(label = "") { if (!it) 0f else 180f }

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true,
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(modifier = Modifier
            .animateContentSize()
            .padding(horizontal = 16.dp)) {
            Column(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = heading,
                    style = MaterialTheme.typography.h6
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    text = stringResource(R.string.steam_dialog_disclaimer),
                    style = MaterialTheme.typography.body2
                )
                Spacer(modifier = Modifier.padding(vertical = 2.dp))
                TextField(
                    value = steamId,
                    onValueChange = { steamId = it },
                    label = { Text(stringResource(R.string.steam_id_field)) },
                    singleLine = true
                )
                Button(
                    onClick = { onConfirmClick(steamId) }
                ) {
                    Text(stringResource(R.string.button_confirm).uppercase())
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.align(Alignment.Start)
                ) {
                    TextLabel(
                        text = { Text(stringResource(R.string.steam_dialog_help_caption)) },
                        label = { Icon(imageVector = Icons.Default.Help, contentDescription = "") },
                        modifier = Modifier.alpha(.5f)
                    )
                    IconButton(
                        onClick = { isInfoExpanded = !isInfoExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            imageVector = Icons.Default.ExpandMore,
                            contentDescription = stringResource(R.string.alt_steam_dialog_expand_help),
                            modifier = Modifier
                                .rotate(rotation.value)
                                .alpha(.5f)
                        )
                    }
                }
                if (isInfoExpanded) {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Start)
                            .alpha(.5f),
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(stringResource(R.string.steam_dialog_help_1))
                        Text(stringResource(R.string.steam_dialog_help_2))
                        Text(stringResource(R.string.steam_dialog_help_3))
                        Text(stringResource(R.string.steam_dialog_help_4))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun SteamDialogPreview() {
    SteamDialogContent(
        heading = stringResource(R.string.steam_import_prep_heading),
        onDismissRequest = {  },
        onConfirmClick = {  }
    )
}

package com.github.backlog.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.github.backlog.R

@Composable
fun MainDrawer() {
    Box(modifier = Modifier.padding(16.dp)) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.h5
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text(
                text = stringResource(R.string.app_version),
                style = MaterialTheme.typography.caption
            )
        }
    }
}
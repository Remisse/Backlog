package com.example.backlog.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.backlog.R
import com.example.backlog.model.GameStatus
import com.example.backlog.viewmodel.GameViewModel
import com.example.backlog.viewmodel.TaskViewModel
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.PieChartData.Slice

@Composable
private fun OutlinedSurface(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    Surface(
        border = BorderStroke(0.5.dp, Color.DarkGray),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        content()
    }
}

@Composable
private fun GameStatusPie(counts: Map<GameStatus, Int>) {
    OutlinedSurface(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(12.dp)
            ) {
                GameStatus.values().forEach { status ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = ColorPainter(gameStatusToColor(status)),
                            contentDescription = null,
                            modifier = Modifier
                                .size(16.dp)
                                .clip(RoundedCornerShape(2.dp))
                        )
                        Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                        Text(text = "${stringResource(gameStatusToResource(status))} (${counts[status]})")
                    }
                }
            }
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
            PieChart(
                pieChartData = PieChartData(counts.map { entry ->
                    Slice(value = entry.value.toFloat(), color = gameStatusToColor(entry.key))
                } ),
                modifier = Modifier
                    .size(150.dp)
                    .padding(12.dp)
            )
        }
    }
}

@Composable
fun ProfileScreen(gameViewModel: GameViewModel, taskViewModel: TaskViewModel) {
    val backlog = gameViewModel.backlog
        .collectAsState(initial = emptyList())
        .value

    Column(
        modifier = LookAndFeel.FieldColumnModifier,
        horizontalAlignment = LookAndFeel.FieldColumnHorizontalAlignment,
        verticalArrangement = LookAndFeel.FieldColumnVerticalArrangement
    ) {
        if (backlog.isEmpty()) {
            Text(
                text = stringResource(R.string.profile_no_data),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        } else {
            val counts: Map<GameStatus, Int> = GameStatus.values().associateBy(
                keySelector = { status -> status },
                valueTransform = { status -> backlog.filter { it.status == status }.size }
            )
            Text(text = stringResource(R.string.profile_stats_heading), style = MaterialTheme.typography.subtitle1)
            GameStatusPie(counts = counts)
        }
    }
}

package com.example.backlog.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.backlog.model.database.GameStatus
import com.example.backlog.viewmodel.GameViewModel
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
private fun ChartBox(counts: List<Pair<Float, GameStatus>>) {
    OutlinedSurface() {
        Row(verticalAlignment = Alignment.CenterVertically) {
            PieChart(
                pieChartData = PieChartData(counts.map { pair ->
                    Slice(value = pair.first, color = gameStatusToColor(pair.second))
                } ),
                modifier = Modifier
                    .size(150.dp)
                    .padding(12.dp)
            )
            Spacer(modifier = Modifier.padding(horizontal = 8.dp))
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
                        Text(text = stringResource(gameStatusToResource(status)))
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileScreen(gameViewModel: GameViewModel) {
    val gamesFlow = gameViewModel.backlog

    val counts: List<Pair<Float, GameStatus>> = GameStatus.values().map { status ->
        gamesFlow.collectAsState(initial = listOf()).value
            .filter { g -> g.status == status }
            .size
            .toFloat() to status
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChartBox(counts = counts)
    }
}

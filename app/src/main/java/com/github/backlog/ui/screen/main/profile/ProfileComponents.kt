package com.github.backlog.ui.screen.main.profile

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.content.res.AppCompatResources
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.graphics.drawable.toBitmap
import com.github.backlog.R
import com.github.backlog.model.GameStatus
import com.github.backlog.model.TaskStatus
import com.github.backlog.model.database.backlog.entity.Game
import com.github.backlog.model.database.backlog.queryentity.IntByString
import com.github.backlog.model.database.backlog.queryentity.TaskWithGameTitle
import com.github.backlog.ui.components.LookAndFeel
import com.github.backlog.ui.components.toColor
import com.github.backlog.ui.components.toResource
import com.github.backlog.ui.theme.BacklogTheme
import com.github.backlog.utils.ComposeFileProvider
import com.github.backlog.utils.RandomColor
import com.github.backlog.utils.launchWithPermission
import com.github.backlog.utils.toBitmap
import com.github.tehras.charts.bar.BarChart
import com.github.tehras.charts.bar.BarChartData
import com.github.tehras.charts.bar.renderer.label.SimpleValueDrawer
import com.github.tehras.charts.bar.renderer.xaxis.SimpleXAxisDrawer
import com.github.tehras.charts.bar.renderer.yaxis.SimpleYAxisDrawer
import com.github.tehras.charts.piechart.PieChart
import com.github.tehras.charts.piechart.PieChartData
import com.github.tehras.charts.piechart.renderer.SimpleSliceDrawer
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import java.text.NumberFormat

@Composable
private inline fun OutlinedSurface(
    modifier: Modifier = Modifier, 
    crossinline content: @Composable () -> Unit
) {
    Surface(
        border = BorderStroke(0.5.dp, Color.DarkGray),
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
    ) {
        content()
    }
}

private inline fun <reified T : Enum<T>> countOccurrences(data: List<T>): Map<T, Int> {
    return enumValues<T>().associateBy(
        keySelector = { status -> status },
        valueTransform = { status ->
            data.filter { it == status }
                .size
        }
    )
}

@Composable
private inline fun <reified T : Enum<T>> StatusPie(
    data: Map<T, Int>,
    title: String,
    crossinline toColor: (T) -> Color,
    crossinline toResource: (T) -> Int
) {
    OutlinedSurface(modifier = Modifier.fillMaxWidth()) {
        Column() {
            SectionTitleText(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxSize()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.padding(12.dp)
                ) {
                    enumValues<T>().forEach { status ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = ColorPainter(toColor(status)),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(16.dp)
                                    .clip(RoundedCornerShape(2.dp))
                            )
                            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
                            Text(text = "${stringResource(toResource(status))}: ${data[status]}")
                        }
                    }
                }
                Spacer(modifier = Modifier.padding(horizontal = 8.dp))
                PieChart(
                    pieChartData = PieChartData(data.map { entry ->
                        PieChartData.Slice(value = entry.value.toFloat(), color = toColor(entry.key))
                    }),
                    modifier = Modifier
                        .size(150.dp)
                        .padding(12.dp),
                    sliceDrawer = SimpleSliceDrawer(sliceThickness = 50f)
                )
            }
        }
    }
}

@Composable
private fun StatsBar(
    title: String,
    placeholder: String,
    data: List<IntByString>
) {
    val colorGenerator = RandomColor()

    OutlinedSurface(modifier = Modifier.fillMaxWidth()) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            SectionTitleText(
                text = title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(4.dp)
            )
            if (data.isEmpty()) {
                PlaceholderText(
                    text = placeholder,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            } else {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    BarChart(
                        barChartData = BarChartData(data.map { elem ->
                            BarChartData.Bar(
                                value = elem.count.toFloat(),
                                color = Color(colorGenerator.getColor()),
                                label = elem.field
                            )
                        }),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(150.dp)
                            .padding(12.dp),
                        yAxisDrawer = SimpleYAxisDrawer(
                            labelRatio = 5, // Base step is 0.2, so 0.2 * 5 -> steps of 1 unit
                            labelTextColor = MaterialTheme.colors.onBackground,
                            labelValueFormatter = { value -> NumberFormat.getIntegerInstance().format(value) },
                            axisLineColor = MaterialTheme.colors.onBackground,
                            axisLineThickness = 2.dp
                        ),
                        xAxisDrawer = SimpleXAxisDrawer(
                            axisLineColor = MaterialTheme.colors.onBackground,
                            axisLineThickness = 2.dp
                        ),
                        labelDrawer = SimpleValueDrawer(
                            labelTextColor = MaterialTheme.colors.onBackground
                        )
                    )
                }
            }
        }
    }
}

@Composable
fun SectionTitleText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        style = MaterialTheme.typography.subtitle1,
        modifier = modifier,
        textAlign = TextAlign.Center
    )
}

@Composable
fun PlaceholderText(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
        textAlign = TextAlign.Center,
        modifier = modifier
    )
}

@Composable
fun ProfileScreenContent(
    profileName: Flow<String?>,
    profileBio: Flow<String?>,
    profileImage: Flow<Uri?>,
    onImageSelect: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    games: List<Game>,
    tasks: List<TaskWithGameTitle>,
    completedGamesByGenre: List<IntByString>,
    droppedGamesByGenre: List<IntByString>,
    completedGamesByDeveloper: List<IntByString>,
    droppedGamesByDeveloper: List<IntByString>,
    completedGamesInCurrentYear: Int
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState, enabled = true)
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalAlignment = LookAndFeel.FieldColumnHorizontalAlignment,
        verticalArrangement = LookAndFeel.FieldColumnVerticalArrangement
    ) {
        val tabNames = listOf("Profile", "Stats")
        val selectedTab = rememberSaveable { mutableStateOf(0) }

        TabRow(
            selectedTabIndex = selectedTab.value,
            modifier = Modifier
                .clip(RoundedCornerShape(25))
                .padding(horizontal = 8.dp, vertical = 8.dp),
            backgroundColor = MaterialTheme.colors.surface
        ) {
            tabNames.forEachIndexed { index, name ->
                Tab(
                    selected = selectedTab.value == index,
                    onClick = { selectedTab.value = index },
                ) {
                    Text(
                        text = name,
                        modifier = Modifier
                            .clip(RoundedCornerShape(25))
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }

        when (selectedTab.value) {
            0 -> ProfilePart(
                profileName = profileName,
                profileBio = profileBio,
                profileImage = profileImage,
                onImageSelect = onImageSelect,
                onNameChange = onNameChange,
                onBioChange = onBioChange
            )
            1 -> StatsPart(
                games = games,
                tasks = tasks,
                completedGamesByGenre = completedGamesByGenre,
                droppedGamesByGenre = droppedGamesByGenre,
                completedGamesByDeveloper = completedGamesByDeveloper,
                droppedGamesByDeveloper = droppedGamesByDeveloper,
                completedGamesInCurrentYear = completedGamesInCurrentYear
            )
        }
    }
}

@Composable
private fun ActionDialog(
    onDismissRequest: () -> Unit,
    onCameraButtonClick: () -> Unit,
    onGalleryButtonClick: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(dismissOnBackPress = true, dismissOnClickOutside = true)
    ) {
        Surface() {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = stringResource(R.string.profile_image_action_heading),
                    style = MaterialTheme.typography.subtitle1
                )
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    IconButton(
                        onClick = onCameraButtonClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = stringResource(R.string.profile_image_action_camera),
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                    IconButton(
                        onClick = onGalleryButtonClick,
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Image,
                                contentDescription = "",
                                modifier = Modifier.size(40.dp)
                            )
                            Text(
                                text = stringResource(R.string.profile_image_action_gallery),
                                style = MaterialTheme.typography.caption
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun ProfilePart(
    profileName: Flow<String?>,
    profileBio: Flow<String?>,
    profileImage: Flow<Uri?>,
    onImageSelect: (Uri) -> Unit,
    onNameChange: (String) -> Unit,
    onBioChange: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name: String? by remember { mutableStateOf(null) }
    var bio: String? by remember { mutableStateOf(null) }
    var imageUri: Uri? by remember { mutableStateOf(null) }
    var newUriTemp: Uri? by remember { mutableStateOf(null) }

    LaunchedEffect(profileName, profileImage) {
        scope.launch {
            profileName.collect {
                name = it
            }
            profileBio.collect {
                bio = it
            }
            profileImage.collect {
                imageUri = it
            }
        }
    }

    val contentPicker =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                imageUri = it
                onImageSelect(it)
            }
        }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
            if (success) {
                imageUri = newUriTemp
                onImageSelect(imageUri!!)
            }
        }

    val mediaPermission = rememberPermissionState(android.Manifest.permission.READ_EXTERNAL_STORAGE)
    val cameraPermission = rememberPermissionState(android.Manifest.permission.CAMERA)

    var showActionDialog by remember { mutableStateOf(false) }
    if (showActionDialog) {
        ActionDialog(
            onDismissRequest = { showActionDialog = false },
            onCameraButtonClick = {
                launchWithPermission(cameraPermission) {
                    newUriTemp = ComposeFileProvider.createNewImageUri(context)
                    cameraLauncher.launch(newUriTemp)
                    showActionDialog = false
                }
            },
            onGalleryButtonClick = {
                launchWithPermission(mediaPermission) {
                    contentPicker.launch("image/*")
                    showActionDialog = false
                }
            }
        )
    }

    Column(
        modifier = Modifier.padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val bitmap = imageUri?.toBitmap(context)
            ?: AppCompatResources.getDrawable(context, R.drawable.ic_placeholder_image)!!
                .toBitmap()
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = stringResource(R.string.alt_profile_image),
            modifier = Modifier
                .size(200.dp)
                .clip(RoundedCornerShape(50))
                .clickable {
                    scope.launch { showActionDialog = true }
                }
        )
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var readOnly by remember { mutableStateOf(true) }

            TextField(
                value = name ?: "",
                onValueChange = { name = it },
                label = { Text("Name") },
                readOnly = readOnly
            )
            if (readOnly) {
                IconButton(onClick = { readOnly = false }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit name")
                }
            } else {
                IconButton(
                    onClick = {
                        onNameChange(name ?: "")
                        readOnly = true
                    }
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Save name")
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            var readOnly by remember { mutableStateOf(true) }

            TextField(
                value = bio ?: "",
                onValueChange = { bio = it },
                modifier = Modifier.height(200.dp),
                label = { Text("Bio") },
                readOnly = readOnly
            )
            if (readOnly) {
                IconButton(onClick = { readOnly = false }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "Edit bio")
                }
            } else {
                IconButton(
                    onClick = {
                        onBioChange(bio ?: "")
                        readOnly = true
                    }
                ) {
                    Icon(imageVector = Icons.Default.Done, contentDescription = "Save bio")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ActionDialogPreview() {
    BacklogTheme() {
        ActionDialog(onDismissRequest = { /*TODO*/ }, onCameraButtonClick = { /*TODO*/ }) {

        }
    }
}

@Composable
private fun StatsPart(
    games: List<Game>,
    tasks: List<TaskWithGameTitle>,
    completedGamesByGenre: List<IntByString>,
    droppedGamesByGenre: List<IntByString>,
    completedGamesByDeveloper: List<IntByString>,
    droppedGamesByDeveloper: List<IntByString>,
    completedGamesInCurrentYear: Int
) {
    // Assume we're inside a ColumnScope
    SectionTitleText(text = stringResource(R.string.profile_games_subheading))
    if (games.isEmpty()) {
        PlaceholderText(stringResource(R.string.profile_no_data))
    } else {
        val counts: Map<GameStatus, Int> = countOccurrences(games.map { it.status })

        StatusPie(
            title = stringResource(R.string.profile_games_by_status_title),
            data = counts,
            toColor = { it.toColor() }, 
            toResource = { it.toResource() }
        )
        StatsBar(
            title = stringResource(R.string.profile_top5_genres_completed),
            data = completedGamesByGenre,
            placeholder = stringResource(R.string.profile_stats_placeholder)
        )
        StatsBar(
            title = stringResource(R.string.profile_top5_genres_dropped),
            data = droppedGamesByGenre,
            placeholder = stringResource(R.string.profile_stats_placeholder)
        )
        StatsBar(
            title = stringResource(R.string.profile_top5_devs_completed),
            data = completedGamesByDeveloper,
            placeholder = stringResource(R.string.profile_stats_placeholder)
        )
        StatsBar(
            title = stringResource(R.string.profile_top5_devs_dropped),
            data = droppedGamesByDeveloper,
            placeholder = stringResource(R.string.profile_stats_placeholder)
        )
        OutlinedSurface(modifier = Modifier.fillMaxWidth()) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 8.dp)
            ) {
                SectionTitleText(text = stringResource(R.string.profile_games_completed_year))
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(color = MaterialTheme.colors.secondary)
                        .size(64.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = completedGamesInCurrentYear.toString(),
                        color = MaterialTheme.colors.onSecondary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }

    Spacer(modifier = Modifier.padding(vertical = 4.dp))

    SectionTitleText(text = stringResource(R.string.profile_tasks_subheading))
    if (tasks.isEmpty()) {
        PlaceholderText(text = stringResource(R.string.profile_tasks_no_data))
    } else {
        val counts: Map<TaskStatus, Int> = countOccurrences(tasks.map { it.task.status })
        StatusPie(counts, stringResource(R.string.profile_tasks_by_status_title), { it.toColor() }, { it.toResource() })
    }
}

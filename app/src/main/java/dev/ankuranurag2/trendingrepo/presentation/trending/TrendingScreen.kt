package dev.ankuranurag2.trendingrepo.presentation.trending

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import dev.ankuranurag2.trendingrepo.R
import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.presentation.theme.GreenColor
import dev.ankuranurag2.trendingrepo.utils.AppUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@ExperimentalAnimationApi
@Composable
fun TrendingScreen() {
    val viewModel: TrendingRepoVM = hiltViewModel()
    val uiState by viewModel.uiState.collectAsState()

    TrendingScreenContent(
        uiState = uiState,
        scaffoldState = rememberScaffoldState(),
        coroutineScope = rememberCoroutineScope(),
        onRefresh = { viewModel.refreshPosts() },
        onSortRequest = { viewModel.sortData(it) }
    )
}

@ExperimentalAnimationApi
@Composable
fun TrendingScreenContent(
    uiState: RepoListUiState,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
    onRefresh: () -> Unit,
    onSortRequest: (sortBy: SortBy) -> Unit
) {
    var showMenu by remember { mutableStateOf(false) }
    Scaffold(
        scaffoldState = scaffoldState,
        snackbarHost = { SnackbarHost(hostState = it) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Trending",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                elevation = 4.dp,
                backgroundColor = Color.White,
                actions = {
                    IconButton(onClick = { showMenu = !showMenu }) {
                        Icon(painter = painterResource(id = R.drawable.more_black_24), contentDescription = null)
                    }
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(onClick = { onSortRequest(SortBy.STAR) }) {
                            Text(text = "Sort by stars")
                        }
                        DropdownMenuItem(onClick = { onSortRequest(SortBy.NAME) }) {
                            Text(text = "Sort by name")
                        }
                    }
                }
            )
        },
        content = {
            if (uiState.initialLoad) {
                FullScreenLoading()
            } else {
                SwipeRefresh(
                    state = rememberSwipeRefreshState(isRefreshing = uiState.loading),
                    onRefresh = onRefresh,
                    content = {
                        Box(modifier = Modifier.fillMaxSize().background(color = Color.White)) {
                            if (uiState.repoList.isEmpty() && uiState.errorMsgId != null) {
                                ErrorScreen(onRefresh)
                            } else {
                                uiState.errorMsgId?.let {
                                    val errorMsg = stringResource(id = it)
                                    coroutineScope.launch {
                                        scaffoldState.snackbarHostState.showSnackbar(errorMsg)
                                    }
                                }
                                LazyColumn {
                                    items(uiState.repoList) { repoData ->
                                        RepoListItem(repoData)
                                        Divider(
                                            color = Color.LightGray,
                                            thickness = 1.dp
                                        )
                                    }
                                }
                            }
                        }
                    }
                )
            }
        }
    )
}

@ExperimentalAnimationApi
@Composable
fun RepoListItem(repoData: RepoData) {
    var isExpanded by rememberSaveable {
        mutableStateOf(false)
    }

    Column(modifier = Modifier
        .fillMaxWidth()
        .background(color = Color.White)
        .clickable {
            isExpanded = !isExpanded
        }
        .padding(16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.Top
        ) {
            Image(
                painter = rememberImagePainter(repoData.avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.size(16.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                Text(text = repoData.author, style = MaterialTheme.typography.subtitle1)

                Spacer(modifier = Modifier.height(6.dp))

                Text(text = repoData.name, style = MaterialTheme.typography.subtitle2)

                Spacer(modifier = Modifier.height(8.dp))
                AnimatedVisibility(visible = isExpanded, modifier = Modifier.fillMaxWidth()) {
                    ExpandedItem(repoData = repoData)
                }
            }
        }
    }
}

@Composable
fun ExpandedItem(repoData: RepoData) {
    Column {
        Text(text = repoData.description, style = MaterialTheme.typography.subtitle1)

        Spacer(modifier = Modifier.height(8.dp))

        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Canvas(
                modifier = Modifier.size(8.dp),
                onDraw = {
                    8.dp.toPx().let {
                        drawCircle(
                            color = AppUtils.getComposeColorFromHex(repoData.languageColor),
                            radius = it / 2f
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(text = repoData.language, style = MaterialTheme.typography.subtitle1)

            Spacer(modifier = Modifier.size(8.dp))

            Image(
                painter = painterResource(id = R.drawable.star_yellow_16),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = repoData.stars.toString(), style = MaterialTheme.typography.subtitle1)

            Spacer(modifier = Modifier.size(8.dp))

            Image(
                painter = painterResource(id = R.drawable.fork_black_16),
                contentDescription = null,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.size(4.dp))
            Text(text = repoData.forks.toString(), style = MaterialTheme.typography.subtitle1)
        }
    }
}

@Composable
private fun FullScreenLoading() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .wrapContentSize(Alignment.Center)
    ) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorScreen(
    onRetry: () -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.nointernet_connection),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Inside
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Something went wrong",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            color = AppUtils.getComposeColorFromHex("#4a4a4a")
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "An alien is probably blocking your signal",
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            fontFamily = FontFamily.SansSerif,
            textAlign = TextAlign.Center,
            color = AppUtils.getComposeColorFromHex("#929292")
        )

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = { onRetry() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = GreenColor,
                backgroundColor = Color.White
            ),
            border = BorderStroke(1.dp, GreenColor)
        ) {
            Text(
                text = "RETRY",
                color = GreenColor,
                fontSize = 15.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}
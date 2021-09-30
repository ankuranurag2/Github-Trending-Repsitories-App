package dev.ankuranurag2.trendingrepo.presentation.trending

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberImagePainter
import dev.ankuranurag2.trendingrepo.R
import dev.ankuranurag2.trendingrepo.data.model.RepoData
import dev.ankuranurag2.trendingrepo.utils.AppUtils
import dev.ankuranurag2.trendingrepo.utils.Resource
import kotlinx.coroutines.launch

@Composable
fun TrendingScreen() {
    val viewModel: TrendingRepoVM = hiltViewModel()
    val resourceData by viewModel.resourceFlow.collectAsState(initial = Resource.Loading)
    val scaffoldState = rememberScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        when (resourceData) {
            is Resource.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is Resource.Success -> {
                LazyColumn {
                    (resourceData as? Resource.Success<List<RepoData>>)?.data?.let {
                        items(it) { repoData ->
                            RepoListItem(repoData)
                            Divider(
                                color = Color.LightGray,
                                thickness = 1.dp
                            )
                        }
                    }
                }
            }

            is Resource.Error -> {
                coroutineScope.launch {
                    scaffoldState.snackbarHostState.showSnackbar("")
                }
            }
        }
    }
}

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

                if (isExpanded) {
                    Spacer(modifier = Modifier.height(8.dp))

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
        }
    }
}
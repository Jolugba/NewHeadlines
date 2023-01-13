package com.tinude.newsheadlines.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.tinude.newsheadlines.network.response.Article
import com.tinude.newsheadlines.ui.theme.NewsHeadlinesTheme
import com.tinude.newsheadlines.ui.viewmodel.NewsHeadlineViewModel
import com.tinude.newsheadlines.util.openUrlInApp
import com.tinude.newsheadlines.util.toast

@Composable
fun ShowNewsHeadlines(modifier: Modifier = Modifier,
                      viewModel: NewsHeadlineViewModel = hiltViewModel()) {

    val uiState = viewModel.uiState
    val context = LocalContext.current

    when (val state = uiState.value) {
        is NewsHeadlineViewModel.NewsState.ERROR -> {
            context.toast(state.message)
            Column(
                        modifier = modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = CenterHorizontally
                    ) {
                        Button(
                            onClick = { viewModel.fetchNews() },
                            modifier = modifier
                        ) {
                            Text(
                                text = "Retry"
                            )
                        }
                    }

        }

        is NewsHeadlineViewModel.NewsState.Success -> {
            LazyColumn {
                items(items = state.data){    news ->
                    NewsItem(news, onClick = {
                        news.url.let { context.openUrlInApp(it) }
                    })
                }
            }
        }

        is NewsHeadlineViewModel.NewsState.LOADING -> if (state.loading) ShowLoader()

    }

}

@Composable
fun ShowLoader() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = CenterHorizontally
    ) {
        CircularProgressIndicator()
    }
}
@Composable
fun NewsItem(news: Article, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp)
            .padding(bottom = 4.dp)
            .height(IntrinsicSize.Min)
            .clickable(
                onClick = onClick
            )
    ) {

        val image: Painter = rememberAsyncImagePainter(news.urlToImage)

        Image(
            image,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
            contentScale = ContentScale.FillWidth
        )

        // Title
        Column(
            modifier = Modifier.align(Alignment.BottomStart)
        ) {
            Text(
                text = news.description?:"",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(start = 10.dp)
            )
            Text(
                text = news.author?:"",
                fontSize = 12.sp,
                color = Color.White,
                modifier = Modifier.padding(start = 10.dp, end = 10.dp, top = 5.dp, bottom = 10.dp)
            )
        }
    }
}
@Composable
fun ShowAppBar(){
    SmallTopAppBar(title = { Text(text = "NewsFeeds", fontWeight = FontWeight.Bold) },
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = Color.Black,
            titleContentColor = Color.White))
}
@Composable
fun ShowText(modifier: Modifier=Modifier){
    Column(modifier.background(Color.Black).fillMaxWidth(),
        horizontalAlignment = CenterHorizontally) {
        Text(text = "Top News", fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center, color = Color.White)
    }

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShowFullScreen(modifier:Modifier=Modifier){
   NewsHeadlinesTheme{
        Scaffold(
           topBar = { ShowAppBar() }
        ) { padding ->
            Column(modifier.padding(padding)) {
                ShowText()
                ShowNewsHeadlines()
            }
        }
    }
}
@Preview(showBackground = true)
@Composable
fun show(){
  ShowText()
}


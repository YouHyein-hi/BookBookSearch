package com.example.bookbooksearch.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.lerp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.bookbooksearch.MainViewModel
import com.example.bookbooksearch.dataClass.DetailItem
import com.example.bookbooksearch.ui.Util.bounceClick
import com.example.bookbooksearch.ui.Util.dottedLine
import com.example.bookbooksearch.ui.theme.BookbookSearchTheme
import com.example.bookbooksearch.ui.theme.pretendard
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt


private val BottomBarHeight = 56.dp
private val TitleHeight = 128.dp
private val GradientScroll = 180.dp
private val ImageOverlap = 115.dp
private val MinTitleOffset = 56.dp
private val MinImageOffset = 12.dp
private val MaxTitleOffset = ImageOverlap + MinTitleOffset + GradientScroll
private val ExpandedImageSize = 300.dp
private val CollapsedImageSize = 70.dp
private val HzPadding = Modifier.padding(horizontal = 24.dp)

@Composable
fun DetailScreen(viewModel: MainViewModel) {

    val isbn by viewModel.isbn.observeAsState()
    val result by viewModel.resultDetail.observeAsState()
    val detailData by viewModel.detailData.observeAsState()
    result?.items?.forEach { it->
        viewModel.setDetailData(it)
    }
    Log.e("DetailScreen", "DetailScreen: ${isbn}", )
    viewModel.getDataDetail(viewModel.isbn.value.toString())

    Box(Modifier.fillMaxSize()) {
        val scroll = rememberScrollState(0)
        Header()
        Body(detailData, scroll)
        Title(detailData) { scroll.value }
        Image(detailData?.image.toString()) { scroll.value }
        Heart()
    }

}

@Composable
private fun Header() {
    Spacer(
        modifier = Modifier
            .height(280.dp)
            .fillMaxWidth()
            .background(Brush.horizontalGradient(listOf(Color(0xFF177C46), Color(0xFFA4C7B4))))
    )
}

@Composable
private fun Heart() {
    var heartClick by remember { mutableStateOf(false) }
    IconButton(
        onClick = { Log.e("TAG", "Heart: 클릭!",) },
        modifier = Modifier
            .statusBarsPadding()
            .padding(horizontal = 16.dp, vertical = 10.dp)
            .size(36.dp)
            .background(
                color = Color(0xff121212).copy(alpha = 0.32f),
                shape = CircleShape
            )
            .bounceClick()

    ) {
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = "heart",
            tint = if(heartClick) Color.Red else Color.Black,
            modifier = Modifier.clickable {
                heartClick = !heartClick
            }
        )
    }
}

@Composable
private fun Title(data : DetailItem?, scrollProvider: () -> Int) {
    val maxOffset = with(LocalDensity.current) { MaxTitleOffset.toPx() }
    val minOffset = with(LocalDensity.current) { MinTitleOffset.toPx() }

    Column(
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .heightIn(min = TitleHeight)
            .statusBarsPadding()
            .offset {
                val scroll = scrollProvider()
                val offset = (maxOffset - scroll).coerceAtLeast(minOffset)
                IntOffset(x = 0, y = offset.toInt())
            }
            .background(color = Color.White)
    ) {
        Spacer(Modifier.height(16.dp))
        Text(
            text = data?.title.toString(),
            style = MaterialTheme.typography.h4,
            color = Color.Black,
            modifier = HzPadding,
            fontSize = 20.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "저자 : ${data?.author}",
            style = MaterialTheme.typography.subtitle2,
            fontSize = 16.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.Medium,
            color = Color.Black,
            modifier = HzPadding
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = "${data?.discount}원",
            style = MaterialTheme.typography.h6,
            fontSize = 18.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = HzPadding
        )

        Spacer(Modifier.height(8.dp))

        dottedLine()
        Spacer(Modifier.height(8.dp))
    }
}

@Composable
private fun Image(imageUrl: String, scrollProvider: () -> Int) {
    val collapseRange = with(LocalDensity.current) { (MaxTitleOffset - MinTitleOffset).toPx() }
    val collapseFractionProvider = {
        (scrollProvider() / collapseRange).coerceIn(0f, 1f)
    }

    ImageLayout(
        collapseFractionProvider = collapseFractionProvider,
        modifier = HzPadding.then(Modifier.statusBarsPadding())
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .size(110.dp)
        )
    }
}

@Composable
private fun ImageLayout(collapseFractionProvider: () -> Float, modifier: Modifier = Modifier, content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content
    ) { measurables, constraints ->
        check(measurables.size == 1)

        val collapseFraction = collapseFractionProvider()

        val imageMaxSize = min(ExpandedImageSize.roundToPx(), constraints.maxWidth)
        val imageMinSize = max(CollapsedImageSize.roundToPx(), constraints.minWidth)
        val imageWidth = imageMaxSize + ((imageMinSize - imageMaxSize) * collapseFraction.toDouble()).roundToInt()
        val imagePlaceable = measurables[0].measure(Constraints.fixed(imageWidth, imageWidth))

        val start = (constraints.maxWidth - imageWidth) / 2
        val stop = constraints.maxWidth - imageWidth
        val imageX = start + ((stop - start) * collapseFraction.toDouble()).roundToInt()
        val imageY = lerp(MinTitleOffset, MinImageOffset, collapseFraction).roundToPx()
        layout(
            width = constraints.maxWidth,
            height = imageY + imageWidth
        ) {
            imagePlaceable.placeRelative(imageX, imageY)
        }
    }
}

@Composable
private fun Body(data : DetailItem?, scroll: ScrollState) {
    Column {
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .height(MinTitleOffset)
        )
        Column(
            modifier = Modifier.verticalScroll(scroll)
        ) {
            Spacer(Modifier.height(GradientScroll))
            CustomSurface(Modifier.fillMaxWidth()) {
                Column {
                    Spacer(Modifier.height(ImageOverlap))
                    Spacer(Modifier.height(TitleHeight))

                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = "책 소개",
                        style = MaterialTheme.typography.overline,
                        color = Color.Black,
                        modifier = HzPadding,
                        fontSize = 16.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Spacer(Modifier.height(8.dp))
                    var seeMore by remember { mutableStateOf(true) }
                    Text(
                        text = data?.description.toString(),
                        style = MaterialTheme.typography.body1,
                        color = Color.Black,
                        modifier = HzPadding,
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Light,
                        maxLines = if (seeMore) 5 else Int.MAX_VALUE,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val textButton = if (seeMore) {
                        "더보기"
                    } else {
                        "줄이기"
                    }
                    Text(
                        text = textButton,
                        style = MaterialTheme.typography.button,
                        textAlign = TextAlign.Center,
                        color = Color.LightGray,
                        modifier = Modifier
                            .heightIn(20.dp)
                            .fillMaxWidth()
                            .padding(top = 15.dp)
                            .clickable {
                                seeMore = !seeMore
                            }
                    )

                    BodyText("출판사", data?.publisher.toString())
                    BodyText("출간일", data?.pubdate.toString())
                    BodyText("ISBN", data?.isbn.toString())

                    Spacer(Modifier.height(16.dp))

                    Spacer(
                        modifier = Modifier
                            .padding(bottom = BottomBarHeight)
                            .navigationBarsPadding()
                            .height(8.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun BodyText(text: String, data: String){
    Spacer(Modifier.height(25.dp))
    Text(
        text = text,
        style = MaterialTheme.typography.overline,
        color = Color.Black,
        modifier = HzPadding,
        fontSize = 16.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.SemiBold,
    )
    Spacer(Modifier.height(8.dp))
    Text(
        text = data,
        style = MaterialTheme.typography.body1,
        color = Color.Black,
        modifier = HzPadding,
        fontSize = 14.sp,
        fontFamily = pretendard,
        fontWeight = FontWeight.Light,
    )
}



/////////////////////////////////

@Composable
fun CustomSurface(
    modifier: Modifier = Modifier,
    shape: Shape = RectangleShape,
    color: Color = Color.White,
    contentColor: Color = Color.Magenta,
    border: BorderStroke? = null,
    elevation: Dp = 0.dp,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
            .shadow(elevation = elevation, shape = shape, clip = false)
            .zIndex(elevation.value)
            .then(if (border != null) Modifier.border(border, shape) else Modifier)
            .background(
                color = color,
                shape = shape
            )
            .clip(shape)
    ) {
        CompositionLocalProvider(LocalContentColor provides contentColor, content = content)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BookbookSearchTheme {

    }
}

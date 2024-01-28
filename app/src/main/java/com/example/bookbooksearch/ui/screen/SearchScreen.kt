package com.example.bookbooksearch.ui.screen

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.RadioButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.bookbooksearch.MainViewModel
import com.example.bookbooksearch.dataClass.DataMain
import com.example.bookbooksearch.dataClass.Item
import com.example.bookbooksearch.ui.Util.bounceClick
import com.example.bookbooksearch.ui.Util.dottedLine
import com.example.bookbooksearch.ui.theme.pretendard

@Composable
fun SearchScreen(viewModel: MainViewModel, navController: NavHostController) {
    var searchText by remember { mutableStateOf("") }
    val result by viewModel.result.observeAsState()
    val display by viewModel.display.observeAsState()
    val sort by viewModel.sort.observeAsState()

    Column(
        modifier = Modifier.padding(16.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row{
            Text(
                text = "검색",
                fontSize = 16.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = "(제목, 저자 등 모두 검색 가능)",
                fontSize = 13.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.Light,
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Row {
            OutlinedTextField(
                value = searchText, // textState는 mutableStateOf로 정의해야 함
                onValueChange = { it -> searchText = it },
                modifier = Modifier
                    .weight(3.5f)
                    .border(
                        BorderStroke(
                            width = 4.dp,
                            brush = Brush.horizontalGradient(
                                listOf(
                                    Color(0xFF177C46),
                                    Color(0xFFA4C7B4)
                                )
                            ),
                        )
                    ),
            )
            Spacer(modifier = Modifier.weight(0.2f))
            Button(
                onClick = { viewModel.getDataApi(searchText, display, sort) },
                shape = IconButtonDefaults.filledShape,
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.White, contentColor = Color.Black),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier
                    .weight(0.7f)
                    .bounceClick()) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "Search")
            }
        }
        Spacer(modifier = Modifier.height(5.dp))
        var filterOnOff by remember { mutableStateOf(true) }
        Text(
            text = if(filterOnOff) "▲ 필터" else "▼ 필터",
            style = MaterialTheme.typography.button,
            textAlign = TextAlign.Center,
            color = if(filterOnOff) Color.Gray else Color(0xFF608F75),
            modifier = Modifier
                .heightIn(20.dp)
                .fillMaxWidth()
                .padding(top = 15.dp)
                .clickable {
                    filterOnOff = !filterOnOff
                }
        )
        if (filterOnOff) {

        } else {
            Spacer(modifier = Modifier.height(3.dp))
            FilterContent(viewModel)
        }

        Spacer(modifier = Modifier.height(10.dp))
        dottedLine()

        Spacer(modifier = Modifier.height(20.dp))
        RecyclerViewContent(result, navController, viewModel)
    }
}

/**  **/
@Composable
fun FilterContent(viewModel: MainViewModel){
    var display by remember { mutableStateOf("") }

    Surface(modifier = Modifier
        .clip(RoundedCornerShape(CornerSize(16.dp)))
        .border(
            width = 1.dp,
            color = Color(0xFF608F75),
            shape = RoundedCornerShape(16.dp)
        )
    ) {
        Column( modifier = Modifier.padding(10.dp)) {
            Row{
                Text(
                    text = "검색 개수",
                    fontSize = 14.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.SemiBold,
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = "(0~100, 기본 : 10)",
                    fontSize = 12.sp,
                    fontFamily = pretendard,
                    fontWeight = FontWeight.Light,
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            BasicTextField(
                value = display,
                onValueChange = {
                    if(it == ""){
                        display =""
                        viewModel.getdDisplay(10)
                    } else{
                        display = it
                        viewModel.getdDisplay(display.toInt())
                    }},
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "정렬",
                fontSize = 14.sp,
                fontFamily = pretendard,
                fontWeight = FontWeight.SemiBold,
            )
            Spacer(modifier = Modifier.width(5.dp))

            val radioOptions = listOf("정확도순", "출간일순")
            val (selectedOption, onOptionSelected) = remember { mutableStateOf(radioOptions[0]) }
            Column(Modifier.selectableGroup()) {
                radioOptions.forEach { text ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(30.dp)
                            .selectable(
                                selected = (text == selectedOption),
                                onClick = {
                                    onOptionSelected(text)
                                    when (text) {
                                        "정확도순" -> viewModel.getdSort("sim")
                                        "출간일순" -> viewModel.getdSort("date")
                                    }
                                },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = (text == selectedOption),
                            onClick = null
                        )
                        Text(
                            text = text,
                            style = MaterialTheme.typography.body1.merge(),
                            modifier = Modifier.padding(start = 10.dp),
                            fontSize = 14.sp,
                            fontFamily = pretendard,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}


/** RecyclerView **/
@Composable
fun RecyclerViewContent(result: DataMain?, navController: NavHostController, viewModel: MainViewModel) {
    if (result != null) {
        Text(
            text = "총 검색 개수 : ${result.display}개",
            fontSize = 14.sp,
            fontFamily = pretendard,
            fontWeight = FontWeight.SemiBold
        )
        Spacer(modifier = Modifier.height(10.dp))
        LazyColumn {
            items(result.items) { item ->
                SimpleLayoutContent(item, navController, viewModel)
                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    } else {
        Text(text = "데이터가 없습니다.")
    }
}

/** RecyclerView item **/
@Composable
fun SimpleLayoutContent(item : Item, navController: NavHostController, viewModel: MainViewModel) {
// item : Item
    Surface(modifier = Modifier
        .clickable {
            navController.navigate("detail")
            Log.e("SearchScreen", "SimpleLayoutContent isbn: ${item.isbn}",)
            viewModel.getIsbn(item.isbn)
        }
        .clip(RoundedCornerShape(CornerSize(16.dp)))
        .border(
            width = 1.dp,
            color = Color(0xFFBDBDBD),
            shape = RoundedCornerShape(16.dp),
        )
        ) {
        Column(
            modifier = Modifier.padding(10.dp)
        ) {
            Row {
                AsyncImage(
                    model = item.image,
                    contentDescription = null,
                    contentScale = ContentScale.FillHeight,
                    modifier = Modifier
                        //.padding(5.dp)
                        .size(110.dp)
                        //.clip(RoundedCornerShape(CornerSize(16.dp)))
                        //.border(1.dp, Color.Black)
                )
                Column(
                ) {

                    Spacer(modifier = Modifier.width(20.dp))
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    dottedLine()

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "저자: ${item.author}",
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "출판사: ${item.publisher}",
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Light
                    )

                    Spacer(modifier = Modifier.height(5.dp))

                    Text(
                        text = "가격: ${item.discount}원",
                        fontSize = 14.sp,
                        fontFamily = pretendard,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}
package com.flaao0.notes.presentation.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.flaao0.notes.domain.entity.ContentItem

@Composable
fun Content(
    modifier: Modifier = Modifier,
    content: List<ContentItem>,
    onTextChanged: (String, Int) -> Unit,
    onDeleteImageClick: (String) -> Unit
) {
    LazyColumn(
        modifier = modifier
    ) {

        content.forEachIndexed { index, item ->
            item(key = index) {
                when (item) {
                    is ContentItem.Image -> {
                        ImageContent(
                            url = item.url,
                            onDeleteImageClick = {
                                onDeleteImageClick(item.url)
                            }
                        )
                    }

                    is ContentItem.Text -> {
                        TextContent(
                            text = item.text,
                            onTextChanged = {
                                onTextChanged(it, index)
                            }
                        )
                    }
                }
            }
        }

    }
}

@Composable
private fun ImageContent(
    modifier: Modifier = Modifier,
    url: String,
    onDeleteImageClick: () -> Unit
) {
    Box(
        modifier = modifier.padding(horizontal = 24.dp)
    ) {
        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(8.dp)),
            model = url,
            contentDescription = "Изображение ",
            contentScale = ContentScale.FillWidth
        )

        Icon(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(8.dp)
                .size(24.dp)
                .clickable {
                    onDeleteImageClick()
                },
            imageVector = Icons.Default.Close,
            contentDescription = "Удалить изображение",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
private fun TextContent(
    modifier: Modifier = Modifier,
    text: String,
    onTextChanged: (String) -> Unit
) {
    TextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp),
        value = text,
        onValueChange = onTextChanged,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.Transparent,
            unfocusedContainerColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        textStyle = TextStyle(
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface
        ),
        placeholder = {
            Text(
                text = "Введите что нибудь ниже...",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
        }
    )
}
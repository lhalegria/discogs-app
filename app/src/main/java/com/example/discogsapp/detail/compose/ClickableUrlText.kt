package com.example.discogsapp.detail.compose

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.discogsapp.common.util.buildAnnotatedStringWithLinks

@Composable
fun ClickableTextUrl(
    url: String,
    onClickLink: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val annotatedString = remember(url) { buildAnnotatedStringWithLinks(url) }
    var textLayoutResult: TextLayoutResult? = null

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier =
            modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .pointerInput(Unit) {
                    detectTapGestures { offsetPosition ->
                        textLayoutResult?.let { layout ->
                            val offset = layout.getOffsetForPosition(offsetPosition)
                            annotatedString
                                .getStringAnnotations("URL", offset, offset)
                                .firstOrNull()
                                ?.let { annotation ->
                                    onClickLink(annotation.item)
                                }
                        }
                    }
                },
        onTextLayout = { textLayoutResult = it },
    )
}

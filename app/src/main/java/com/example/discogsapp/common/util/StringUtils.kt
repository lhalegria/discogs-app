package com.example.discogsapp.common.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle

fun buildAnnotatedStringWithLinks(text: String): AnnotatedString {
    val linkStyle =
        SpanStyle(
            fontWeight = FontWeight.Bold,
            textDecoration = TextDecoration.Underline,
            color = Color.Blue,
        )

    val markdownRegex = Regex("""\*\*\[(.+?)]\((.+?)\)\*\*""")
    val isSimpleUrl = text.startsWith("http://") || text.startsWith("https://")

    val builder = AnnotatedString.Builder()

    if (isSimpleUrl) {
        builder.pushStringAnnotation(
            tag = "URL",
            annotation = text,
        )

        builder.withStyle(linkStyle) {
            append(text)
        }

        builder.pop()
    } else {
        var lastIndex = 0

        markdownRegex.findAll(text).forEach { match ->
            val start = match.range.first
            val end = match.range.last + 1

            if (start > lastIndex) {
                builder.append(text.substring(lastIndex, start))
            }

            val linkText = match.groupValues[1]
            val linkUrl = match.groupValues[2]

            builder.pushStringAnnotation(
                tag = "URL",
                annotation = linkUrl,
            )

            builder.withStyle(linkStyle) {
                append(linkText)
            }

            builder.pop()

            lastIndex = end
        }

        if (lastIndex < text.length) {
            builder.append(text.substring(lastIndex))
        }
    }

    return builder.toAnnotatedString()
}

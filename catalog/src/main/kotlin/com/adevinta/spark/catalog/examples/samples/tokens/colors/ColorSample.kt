/*
 * Copyright (c) 2023 Adevinta
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.adevinta.spark.catalog.examples.samples.tokens.colors

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.catalog.util.splitCamelInSpaces
import com.adevinta.spark.components.surface.Surface
import com.adevinta.spark.components.text.Text
import com.adevinta.spark.tokens.Order
import com.adevinta.spark.tokens.SparkColors
import kotlin.reflect.KProperty1
import kotlin.reflect.full.declaredMemberProperties
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.starProjectedType

@Composable
internal fun ColorSample() {
    val colors = SparkTheme.colors::class.declaredMemberProperties
    // Don't recompute this on every composition, only when colors are changing
    val tokensColorsGroups by remember(colors) {
        derivedStateOf {
            colors
                .filterNot { field ->
                    field.hasAnnotation<Deprecated>()
                }
                .filter {
                    it.returnType == Color::class.starProjectedType
                } // remove dims and any non color tokens
                .map {
                    @Suppress("UNCHECKED_CAST") // we use this to cast the type otherwise we get a star type instead of Color
                    it as KProperty1<SparkColors, Long>
                }
                .filterNot { it.name.startsWith("on") } // remove content co
                .sortedBy {
                    // Use the same order than the one in the specs
                    it.findAnnotation<Order>()?.value ?: Int.MAX_VALUE
                }
                .groupBy {
                    // Group by token name like "main",  "mainContainer" or "mainVariant"
                    it.name.takeWhile { char -> !char.isUpperCase() }
                }.values.toList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
    ) {
        items(tokensColorsGroups) { tokens ->
            Row {
                tokens.forEach { token ->
                    ColorItem(token)
                }
            }
        }
    }
}

@Composable
private fun RowScope.ColorItem(color: KProperty1<SparkColors, Long>) {
    Surface(
        modifier = Modifier
            .padding(8.dp)
            .heightIn(104.dp)
            .weight(1f),
        color = Color(color.get(SparkTheme.colors).toULong()),
        shape = SparkTheme.shapes.extraLarge,
        border = BorderStroke(2.dp, SparkTheme.colors.onBackground),
    ) {
        Box(
            modifier = Modifier.padding(8.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = color.name.splitCamelInSpaces(),
                style = SparkTheme.typography.body2,
                textAlign = TextAlign.Center,
            )
        }
    }
}


@Preview
@Composable
private fun ColorSamplePreview() {
    SparkTheme {
        ColorSample()
    }
}
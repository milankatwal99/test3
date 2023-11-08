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
package com.adevinta.spark.components.progressbar

import androidx.annotation.FloatRange
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import com.adevinta.spark.InternalSparkApi
import com.adevinta.spark.PreviewTheme
import com.adevinta.spark.SparkTheme
import com.adevinta.spark.tokens.dim4
import com.adevinta.spark.tools.modifiers.sparkUsageOverlay
import com.adevinta.spark.tools.preview.ThemeProvider
import com.adevinta.spark.tools.preview.ThemeVariant
import kotlin.random.Random
import androidx.compose.material3.LinearProgressIndicator as MaterialProgressbar

@InternalSparkApi
@Composable
internal fun SparkProgressbar(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    isIndeterminate: Boolean,
    isRounded: Boolean,
    intent: ProgressbarIntent,
    modifier: Modifier = Modifier,
) {
    val color = intent.colors().color
    val trackColor = SparkTheme.colors.onBackground.dim4
    if (isIndeterminate) {
        MaterialProgressbar(
            modifier = modifier.sparkUsageOverlay(),
            color = color,
            trackColor = trackColor,
            strokeCap = if (isRounded) StrokeCap.Round else StrokeCap.Butt,
        )
    } else {
        MaterialProgressbar(
            modifier = modifier.sparkUsageOverlay(),
            progress = progress,
            color = color,
            trackColor = trackColor,
            strokeCap = if (isRounded) StrokeCap.Round else StrokeCap.Butt,
        )
    }
}

/**
 * Determinate
 *
 * Progress indicators express an unspecified wait time or display the duration of a process.
 *
 * By default there is no animation between [progress] values. You can use
 * [ProgressIndicatorDefaults.ProgressAnimationSpec] as the default recommended [AnimationSpec] when
 * animating progress
 *
 * @param progress the progress of this progress indicator, where 0.0 represents no progress and 1.0
 * represents full progress. Values outside of this range are coerced into the range.
 * @param modifier the [Modifier] to be applied to this progress indicator
 */
@Composable
public fun Progressbar(
    @FloatRange(from = 0.0, to = 1.0) progress: Float,
    intent: ProgressbarIntent,
    modifier: Modifier = Modifier,
    isRounded: Boolean = false,
) {
    SparkProgressbar(
        progress = progress.coerceIn(0.0f, 1.0f),
        isIndeterminate = false,
        intent = intent,
        modifier = modifier,
        isRounded = isRounded,
    )
}

/**
 * Progress indicators express an unspecified wait time or display the duration of a process.
 *
 * @param modifier the [Modifier] to be applied to this progress indicator
 */
@Composable
public fun ProgressbarIndeterminate(
    intent: ProgressbarIntent,
    modifier: Modifier = Modifier,
    isRounded: Boolean = false,
) {
    SparkProgressbar(
        progress = 0f,
        isIndeterminate = true,
        intent = intent,
        modifier = modifier,
        isRounded = isRounded,
    )
}

@Preview(
    group = "Progressbar",
    name = "Progressbar",
)
@Composable
internal fun PreviewProgressbar(
    @PreviewParameter(ThemeProvider::class) theme: ThemeVariant,
) {
    PreviewTheme(theme) {
        ProgressbarIntent.entries.forEach { intent ->
            val progress: Double = Random.nextDouble(0.0, 1.0)
            Progressbar(
                intent = intent,
                modifier = Modifier.fillMaxWidth(),
                progress = progress.toFloat(),
                isRounded = Random.nextBoolean(),
            )
        }
    }
}

@Preview(
    group = "Progressbar",
    name = "ProgressbarIndeterminate",
)
@Composable
internal fun PreviewProgressbarIndeterminate(
    @PreviewParameter(ThemeProvider::class) theme: ThemeVariant,
) {
    PreviewTheme(theme) {
        PreviewTheme(theme) {
            ProgressbarIntent.entries.forEach { intent ->
                ProgressbarIndeterminate(
                    intent = intent,
                    modifier = Modifier.fillMaxWidth(),
                    isRounded = Random.nextBoolean(),
                )
            }
        }
    }
}

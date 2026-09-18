package com.rogue.shopcontrol.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.rogue.shopcontrol.R
import com.rogue.shopcontrol.domain.model.DateRange
import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

private val DATA_DISPLAY_FORMATTER: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd/MM/yyyy")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateRangeSelector(
    dateRange: DateRange,
    onDateRangeChanged: (DateRange) -> Unit,
    modifier: Modifier = Modifier
) {

    var showPicker by remember {
        mutableStateOf(false)
    }

    val spanDias =
        ChronoUnit.DAYS.between(dateRange.start, dateRange.end) + 1

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {

        IconButton(
            onClick = {

                onDateRangeChanged(
                    DateRange(
                        start = dateRange.start.minusDays(spanDias),
                        end = dateRange.end.minusDays(spanDias)
                    )
                )

            }
        ) {

            Icon(
                imageVector = Icons.Filled.ChevronLeft,
                contentDescription = stringResource(R.string.date_range_previous_content_description)
            )

        }

        OutlinedButton(
            onClick = { showPicker = true },
            modifier = Modifier.weight(1f)
        ) {

            Text(
                "${dateRange.start.format(DATA_DISPLAY_FORMATTER)} - ${dateRange.end.format(DATA_DISPLAY_FORMATTER)}"
            )

        }

        IconButton(
            onClick = {

                onDateRangeChanged(
                    DateRange(
                        start = dateRange.start.plusDays(spanDias),
                        end = dateRange.end.plusDays(spanDias)
                    )
                )

            }
        ) {

            Icon(
                imageVector = Icons.Filled.ChevronRight,
                contentDescription = stringResource(R.string.date_range_next_content_description)
            )

        }

    }

    if (showPicker) {

        val pickerState =
            rememberDateRangePickerState(
                initialSelectedStartDateMillis =
                    dateRange.start.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli(),
                initialSelectedEndDateMillis =
                    dateRange.end.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()
            )

        val configuration = LocalConfiguration.current

        // Compose Dialogs measure their content with a wrap-content height regardless of
        // usePlatformDefaultWidth (that flag only affects width), so fillMaxHeight(fraction)
        // resolves against an unbounded constraint and is a no-op. DateRangePicker also needs
        // real width to lay its two-field header out side by side instead of wrapping character
        // by character. Pin both dimensions to concrete dp values derived from the screen size
        // instead, which is unambiguous no matter how the dialog window sizes itself.
        val larguraDialogo = configuration.screenWidthDp.dp - 32.dp
        val alturaDialogo = (configuration.screenHeightDp * 0.85f).dp

        Dialog(
            onDismissRequest = { showPicker = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {

            Surface(
                modifier = Modifier
                    .width(larguraDialogo)
                    .height(alturaDialogo),
                shape = MaterialTheme.shapes.large
            ) {

                Column(
                    modifier = Modifier.fillMaxSize()
                ) {

                    DateRangePicker(
                        state = pickerState,
                        modifier = Modifier.weight(1f),
                        title = {

                            Text(
                                text = stringResource(R.string.date_range_picker_title),
                                modifier = Modifier.padding(start = 24.dp, end = 12.dp, top = 16.dp)
                            )

                        },
                        headline = {

                            val inicioTexto =
                                pickerState.selectedStartDateMillis?.let { millis ->

                                    Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                        .format(DATA_DISPLAY_FORMATTER)

                                } ?: "–"

                            val fimTexto =
                                pickerState.selectedEndDateMillis?.let { millis ->

                                    Instant.ofEpochMilli(millis)
                                        .atZone(ZoneOffset.UTC)
                                        .toLocalDate()
                                        .format(DATA_DISPLAY_FORMATTER)

                                } ?: "–"

                            Text(
                                text = "$inicioTexto - $fimTexto",
                                style = MaterialTheme.typography.titleMedium,
                                modifier = Modifier.padding(start = 24.dp, end = 12.dp, bottom = 12.dp)
                            )

                        }
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.End
                    ) {

                        TextButton(
                            onClick = { showPicker = false }
                        ) {

                            Text(stringResource(R.string.cancel))

                        }

                        TextButton(
                            onClick = {

                                val startMillis = pickerState.selectedStartDateMillis
                                val endMillis = pickerState.selectedEndDateMillis

                                if (startMillis != null) {

                                    val novoInicio =
                                        Instant.ofEpochMilli(startMillis)
                                            .atZone(ZoneOffset.UTC)
                                            .toLocalDate()

                                    val novoFim =
                                        if (endMillis != null) {
                                            Instant.ofEpochMilli(endMillis)
                                                .atZone(ZoneOffset.UTC)
                                                .toLocalDate()
                                        } else {
                                            novoInicio
                                        }

                                    onDateRangeChanged(
                                        DateRange(start = novoInicio, end = novoFim)
                                    )

                                }

                                showPicker = false

                            }
                        ) {

                            Text(stringResource(R.string.ok))

                        }

                    }

                }

            }

        }

    }

}

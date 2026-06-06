package com.example.quacksports.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quacksports.model.TimeSlot

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SlotPickerGrid(slots: List<TimeSlot>, selectedHour: Int?, onSelect: (Int) -> Unit) {
    FlowRow(
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        slots.forEach { slot ->
            FilterChip(
                selected = slot.hour == selectedHour,
                onClick = { if (slot.available) onSelect(slot.hour) },
                enabled = slot.available,
                label = { Text(slot.label) }
            )
        }
    }
}

package com.example.quacksports.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quacksports.model.TimeSlot

@Composable
fun SlotPickerGrid(slots: List<TimeSlot>, selectedHour: Int?, onSelect: (Int) -> Unit) {
    val rows = slots.chunked(3)
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        rows.forEach { rowSlots ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowSlots.forEach { slot ->
                    FilterChip(
                        modifier = Modifier.weight(1f),
                        selected = slot.hour == selectedHour,
                        onClick = { if (slot.available) onSelect(slot.hour) },
                        enabled = slot.available,
                        label = {
                            Text(
                                text = slot.label,
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        }
                    )
                }
                // Add empty spacers if the row is not full to maintain grid alignment
                repeat(3 - rowSlots.size) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

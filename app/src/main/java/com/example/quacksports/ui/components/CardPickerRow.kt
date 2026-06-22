package com.example.quacksports.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.quacksports.model.Card

@Composable
fun CardPickerRow(
    cards: List<Card>,
    selectedId: String?,
    onSelect: (String) -> Unit,
    onAddCard: () -> Unit
) {
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(vertical = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(cards) { card ->
            FilterChip(
                selected = card.id == selectedId,
                onClick = { onSelect(card.id) },
                label = { Text("${card.brand} ****${card.last4}") }
            )
        }
        item {
            AssistChip(
                onClick = onAddCard,
                label = { Text("Adicionar cartão") },
                leadingIcon = { Icon(Icons.Filled.Add, contentDescription = null) }
            )
        }
    }
}

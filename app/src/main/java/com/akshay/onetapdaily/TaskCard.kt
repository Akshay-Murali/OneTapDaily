package com.akshay.onetapdaily

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun TaskCard(
    task: TaskEntity,
    onToggle: () -> Unit,
    onDelete: () -> Unit
) {

    val typeColor =
        when (task.taskType) {

            TaskType.HABIT.name ->
                Color(0xFF43A047)

            TaskType.ONE_TIME.name ->
                Color(0xFF1E88E5)

            else ->
                Color(0xFFFB8C00)
        }

    val typeLabel =
        when (task.taskType) {

            TaskType.HABIT.name ->
                "🌱 HABIT"

            TaskType.ONE_TIME.name ->
                "📝 ONE-TIME"

            else ->
                "📅 SCHEDULED"
        }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },

        border = BorderStroke(
            2.dp,
            typeColor
        ),

        colors = CardDefaults.cardColors(
            containerColor =
                if (task.completed)
                    Color(0xFF2E7D32)
                else
                    MaterialTheme.colorScheme.surfaceVariant
        )
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),

            horizontalArrangement =
                Arrangement.SpaceBetween,

            verticalAlignment =
                Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text =
                        if (task.completed)
                            "✅ ${task.name}"
                        else
                            "⬜ ${task.name}",

                    style =
                        MaterialTheme.typography.titleMedium
                )

                Spacer(
                    modifier =
                        Modifier.height(8.dp)
                )

                AssistChip(
                    onClick = {},
                    enabled = false,

                    label = {
                        Text(typeLabel)
                    },

                    colors =
                        AssistChipDefaults.assistChipColors(
                            containerColor = typeColor,
                            labelColor = Color.White
                        )
                )
                if (task.lastCompletedAt > 0) {

                    Spacer(
                        modifier = Modifier.height(4.dp)
                    )

                    Text(
                        text = "Last done: " +
                                java.text.SimpleDateFormat(
                                    "MMM dd, yyyy"
                                ).format(
                                    java.util.Date(
                                        task.lastCompletedAt
                                    )
                                ),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }

            IconButton(
                onClick = onDelete
            ) {
                Text(
                    text = "🗑",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}
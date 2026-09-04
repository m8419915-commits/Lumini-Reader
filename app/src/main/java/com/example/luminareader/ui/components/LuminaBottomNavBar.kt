package com.example.luminareader.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luminareader.data.model.ScreenType
import com.example.luminareader.ui.theme.*

data class NavItem(
    val type: ScreenType,
    val label: String,
    val icon: ImageVector,
    val badgeCount: Int = 0
)

@Composable
fun LuminaBottomNavBar(
    currentScreen: ScreenType,
    onNavigate: (ScreenType) -> Unit,
    updatesCount: Int = 4,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        NavItem(ScreenType.HOME, "Home", Icons.Default.Home),
        NavItem(ScreenType.LIBRARY, "Library", Icons.AutoMirrored.Filled.MenuBook),
        NavItem(ScreenType.UPDATES, "Updates", Icons.Default.Update, updatesCount),
        NavItem(ScreenType.HISTORY, "History", Icons.Default.History),
        NavItem(ScreenType.EXPLORE, "Explore", Icons.Default.Explore),
        NavItem(ScreenType.MORE, "More", Icons.Default.MoreHoriz)
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding(),
        color = LuminaBlack,
        tonalElevation = 8.dp
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(
                                Color.Transparent,
                                LuminaBorder,
                                LuminaViolet.copy(alpha = 0.5f),
                                LuminaBorder,
                                Color.Transparent
                            )
                        )
                    )
            )

            NavigationBar(
                containerColor = LuminaBlack,
                tonalElevation = 0.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                items.forEach { item ->
                    val isSelected = currentScreen == item.type

                    NavigationBarItem(
                        selected = isSelected,
                        onClick = { onNavigate(item.type) },
                        modifier = Modifier.testTag("nav_${item.label.lowercase()}"),
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (item.badgeCount > 0) {
                                        Badge(
                                            containerColor = LuminaCyan,
                                            contentColor = Color.Black
                                        ) {
                                            Text(
                                                text = item.badgeCount.toString(),
                                                fontSize = 10.sp
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = item.icon,
                                    contentDescription = item.label,
                                    tint = if (isSelected) LuminaCyan else TextMuted,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        },
                        label = {
                            Text(
                                text = item.label,
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isSelected) LuminaCyan else TextMuted,
                                fontSize = 10.sp
                            )
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = LuminaCyan,
                            selectedTextColor = LuminaCyan,
                            unselectedIconColor = TextMuted,
                            unselectedTextColor = TextMuted,
                            indicatorColor = LuminaViolet.copy(alpha = 0.2f)
                        )
                    )
                }
            }
        }
    }
}

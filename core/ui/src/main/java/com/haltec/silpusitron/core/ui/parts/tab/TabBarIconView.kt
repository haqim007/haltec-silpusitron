package com.haltec.silpusitron.core.ui.parts.tab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme

data class TabBarItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeAmount: Int? = null,
    val navigate: () -> Unit = {}
)

@Composable
fun TabBarIconView(
    tabBar: TabBarItem,
    isSelected: Boolean
) {
    BadgedBox(badge = { TabBarBadgeView(count = tabBar.badgeAmount) }) {
        Icon(
            imageVector = if (isSelected) tabBar.selectedIcon else tabBar.unselectedIcon,
            contentDescription = tabBar.title,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun TabBarIconViewPreview() {
    SILPUSITRONTheme {
        TabBarIconView(
            tabBar = TabBarItem(
                title = "Home",
                selectedIcon = Icons.Filled.Home,
                unselectedIcon = Icons.Outlined.Home,
                badgeAmount = 3
            ),
            isSelected = false
        )
    }
}
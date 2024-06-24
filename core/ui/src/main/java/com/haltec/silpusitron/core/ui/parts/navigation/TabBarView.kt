package com.haltec.silpusitron.core.ui.parts.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.StackedBarChart
import androidx.compose.material.icons.rounded.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.haltec.silpusitron.core.ui.theme.SILPUSITRONTheme

@Composable
fun TabBarView(tabItems: List<TabBarItem>){
    var selectedIndex by rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        tabItems.forEachIndexed { index, tab ->
            NavigationBarItem(
                selected = index == selectedIndex,
                onClick = {
                    selectedIndex = index
                    tab.navigate()
                },
                icon = { TabBarIconView(tabBar = tab, isSelected = index == selectedIndex) },
                label = { Text(text = tab.title) },
                colors = NavigationBarItemDefaults.colors().copy(
                    selectedIndicatorColor = MaterialTheme.colorScheme.primary.copy(
                        alpha = 0.75F
                    ),
                    selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                )
            )
        }
    }

}

private val homeTab = TabBarItem(title = "Dashboard", selectedIcon = Icons.Filled.StackedBarChart, unselectedIcon = Icons.Outlined.StackedBarChart)
private val alertsTab = TabBarItem(
    title = "Tambah",
    selectedIcon = Icons.Filled.AddCircle,
    unselectedIcon = Icons.Rounded.AddCircle,
    badgeAmount = 7
)
private val settingsTab = TabBarItem(title = "Riwayat", selectedIcon = Icons.Filled.History, unselectedIcon = Icons.Outlined.History)
private val moreTab = TabBarItem(title = "Profile", selectedIcon = Icons.Filled.Person2, unselectedIcon = Icons.Outlined.Person)
val tabBarItems = listOf(homeTab, alertsTab, settingsTab, moreTab)

@Preview(showBackground = true)
@Composable
fun TabBarViewPreview(){
    SILPUSITRONTheme {
        TabBarView(tabBarItems)
    }
}
package com.example.ui.components

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.AppLanguage
import com.example.model.AppRole
import com.example.ui.theme.*
import com.example.viewmodel.NavTab

@Composable
fun HeavyLinkBottomNav(
    currentTab: NavTab,
    currentRole: AppRole,
    language: AppLanguage,
    onTabSelected: (NavTab) -> Unit
) {
    NavigationBar(
        containerColor = Navy800,
        contentColor = TextPrimary,
        windowInsets = WindowInsets.navigationBars,
        modifier = Modifier.testTag("heavy_link_bottom_nav")
    ) {
        // Home
        NavigationBarItem(
            selected = currentTab == NavTab.HOME,
            onClick = { onTabSelected(NavTab.HOME) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (language == AppLanguage.TA) "முகப்பு" else "Home",
                    fontSize = 10.sp,
                    fontWeight = if (currentTab == NavTab.HOME) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Navy900,
                selectedTextColor = ConstructionYellow,
                indicatorColor = ConstructionYellow,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        // Explore Fleet
        NavigationBarItem(
            selected = currentTab == NavTab.EXPLORE,
            onClick = { onTabSelected(NavTab.EXPLORE) },
            icon = {
                Icon(
                    imageVector = Icons.Default.PrecisionManufacturing,
                    contentDescription = "Fleet",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (language == AppLanguage.TA) "வாகனங்கள்" else "Fleet",
                    fontSize = 10.sp,
                    fontWeight = if (currentTab == NavTab.EXPLORE) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Navy900,
                selectedTextColor = ConstructionYellow,
                indicatorColor = ConstructionYellow,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        // AI Vehicle Finder
        NavigationBarItem(
            selected = currentTab == NavTab.AI_FINDER,
            onClick = { onTabSelected(NavTab.AI_FINDER) },
            icon = {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = "AI Finder",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = "AI Finder",
                    fontSize = 10.sp,
                    fontWeight = if (currentTab == NavTab.AI_FINDER) FontWeight.Bold else FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Navy900,
                selectedTextColor = ConstructionYellow,
                indicatorColor = ConstructionYellow,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )

        // Bookings (or Owner Console if Owner, or Admin Console if Admin)
        if (currentRole == AppRole.CUSTOMER) {
            NavigationBarItem(
                selected = currentTab == NavTab.BOOKINGS,
                onClick = { onTabSelected(NavTab.BOOKINGS) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Bookings",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = if (language == AppLanguage.TA) "முன்பதிவு" else "Bookings",
                        fontSize = 10.sp,
                        fontWeight = if (currentTab == NavTab.BOOKINGS) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy900,
                    selectedTextColor = ConstructionYellow,
                    indicatorColor = ConstructionYellow,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        } else if (currentRole == AppRole.OWNER) {
            NavigationBarItem(
                selected = currentTab == NavTab.OWNER_PANEL,
                onClick = { onTabSelected(NavTab.OWNER_PANEL) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Engineering,
                        contentDescription = "Manager",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = if (language == AppLanguage.TA) "என் எந்திரம்" else "My Equipment",
                        fontSize = 10.sp,
                        fontWeight = if (currentTab == NavTab.OWNER_PANEL) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy900,
                    selectedTextColor = ConstructionYellow,
                    indicatorColor = ConstructionYellow,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        } else {
            NavigationBarItem(
                selected = currentTab == NavTab.ADMIN_PANEL,
                onClick = { onTabSelected(NavTab.ADMIN_PANEL) },
                icon = {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Admin",
                        modifier = Modifier.size(22.dp)
                    )
                },
                label = {
                    Text(
                        text = "Admin",
                        fontSize = 10.sp,
                        fontWeight = if (currentTab == NavTab.ADMIN_PANEL) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Navy900,
                    selectedTextColor = ConstructionYellow,
                    indicatorColor = ConstructionYellow,
                    unselectedIconColor = TextSecondary,
                    unselectedTextColor = TextSecondary
                )
            )
        }

        // Drivers Hub or Help
        NavigationBarItem(
            selected = currentTab == NavTab.DRIVERS || currentTab == NavTab.SUPPORT,
            onClick = {
                if (currentTab == NavTab.DRIVERS) {
                    onTabSelected(NavTab.SUPPORT)
                } else {
                    onTabSelected(NavTab.DRIVERS)
                }
            },
            icon = {
                Icon(
                    imageVector = if (currentTab == NavTab.SUPPORT) Icons.Default.HelpCenter else Icons.Default.Badge,
                    contentDescription = "More",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = {
                Text(
                    text = if (currentTab == NavTab.SUPPORT) "Help" else "Drivers",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Normal
                )
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Navy900,
                selectedTextColor = ConstructionYellow,
                indicatorColor = ConstructionYellow,
                unselectedIconColor = TextSecondary,
                unselectedTextColor = TextSecondary
            )
        )
    }
}

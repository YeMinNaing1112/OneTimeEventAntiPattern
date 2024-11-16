package com.yeminnaing.onetimeeventanantipattern

import kotlinx.serialization.Serializable


sealed class NavigationScreens {
    @Serializable
    data object Login:NavigationScreens()
    @Serializable
    data object Profile:NavigationScreens()
}
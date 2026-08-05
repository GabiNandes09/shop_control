package com.rogue.shopcontrol.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val BlackGoldColorScheme = darkColorScheme(
    primary = Gold,
    onPrimary = PureBlack,
    primaryContainer = GoldDark,
    onPrimaryContainer = OffWhite,

    secondary = GoldDark,
    onSecondary = OffWhite,
    secondaryContainer = SurfaceVariantBlack,
    onSecondaryContainer = Gold,

    tertiary = Gold,
    onTertiary = PureBlack,

    background = PureBlack,
    onBackground = OffWhite,

    surface = SurfaceBlack,
    onSurface = OffWhite,
    surfaceVariant = SurfaceVariantBlack,
    onSurfaceVariant = GoldLight,

    outline = GoldDark,

    error = ErrorRed,
    onError = PureBlack
)

@Composable
fun ShopControlTheme(
    content: @Composable () -> Unit
) {

    MaterialTheme(
        colorScheme = BlackGoldColorScheme,
        typography = Typography,
        content = content
    )

}

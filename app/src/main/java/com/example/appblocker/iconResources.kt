package com.example.appblocker

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

@Suppress("CheckReturnValue")
val add: ImageVector
    get() {
        if (_add != null) {
            return _add!!
        }
        _add =
            ImageVector.Builder(
                name = "add",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(11f, 13f)
                        horizontalLineTo(5f)
                        verticalLineTo(11f)
                        horizontalLineToRelative(6f)
                        verticalLineTo(5f)
                        horizontalLineToRelative(2f)
                        verticalLineToRelative(6f)
                        horizontalLineToRelative(6f)
                        verticalLineToRelative(2f)
                        horizontalLineTo(13f)
                        verticalLineToRelative(6f)
                        horizontalLineTo(11f)
                        verticalLineTo(13f)
                        close()
                    }
                }
                .build()
        return _add!!
    }

private var _add: ImageVector? = null

@Suppress("CheckReturnValue")
public val delete: ImageVector
    get() {
        if (_delete != null) {
            return _delete!!
        }
        _delete =
            ImageVector.Builder(
                name = "delete",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(7f, 21f)
                        quadTo(6.18f, 21f, 5.59f, 20.41f)
                        reflectiveQuadTo(5f, 19f)
                        verticalLineTo(6f)
                        horizontalLineTo(4f)
                        verticalLineTo(4f)
                        horizontalLineTo(9f)
                        verticalLineTo(3f)
                        horizontalLineToRelative(6f)
                        verticalLineTo(4f)
                        horizontalLineToRelative(5f)
                        verticalLineTo(6f)
                        horizontalLineTo(19f)
                        verticalLineTo(19f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(17f, 21f)
                        horizontalLineTo(7f)
                        close()
                        moveTo(17f, 6f)
                        horizontalLineTo(7f)
                        verticalLineTo(19f)
                        horizontalLineTo(17f)
                        verticalLineTo(6f)
                        close()
                        moveTo(9f, 17f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(8f)
                        horizontalLineTo(9f)
                        verticalLineToRelative(9f)
                        close()
                        moveToRelative(4f, 0f)
                        horizontalLineToRelative(2f)
                        verticalLineTo(8f)
                        horizontalLineTo(13f)
                        verticalLineToRelative(9f)
                        close()
                        moveTo(7f, 6f)
                        verticalLineTo(19f)
                        verticalLineTo(6f)
                        close()
                    }
                }
                .build()
        return _delete!!
    }

private var _delete: ImageVector? = null

@Suppress("CheckReturnValue")
public val lock_clock: ImageVector
    get() {
        if (_lock_clock != null) {
            return _lock_clock!!
        }
        _lock_clock =
            ImageVector.Builder(
                name = "lock_clock",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(9f, 8f)
                        horizontalLineToRelative(6f)
                        verticalLineTo(6f)
                        quadTo(15f, 4.75f, 14.13f, 3.88f)
                        reflectiveQuadTo(12f, 3f)
                        reflectiveQuadTo(9.88f, 3.88f)
                        reflectiveQuadTo(9f, 6f)
                        verticalLineTo(8f)
                        close()
                        moveToRelative(3.25f, 14f)
                        horizontalLineTo(6f)
                        quadTo(5.18f, 22f, 4.59f, 21.41f)
                        reflectiveQuadTo(4f, 20f)
                        verticalLineTo(10f)
                        quadTo(4f, 9.17f, 4.59f, 8.59f)
                        reflectiveQuadTo(6f, 8f)
                        horizontalLineTo(7f)
                        verticalLineTo(6f)
                        quadTo(7f, 3.92f, 8.46f, 2.46f)
                        reflectiveQuadTo(12f, 1f)
                        reflectiveQuadToRelative(3.54f, 1.46f)
                        reflectiveQuadTo(17f, 6f)
                        verticalLineTo(8f)
                        horizontalLineToRelative(1f)
                        quadToRelative(0.82f, 0f, 1.41f, 0.59f)
                        reflectiveQuadTo(20f, 10f)
                        verticalLineToRelative(1.3f)
                        quadTo(19.55f, 11.15f, 19.06f, 11.08f)
                        reflectiveQuadTo(18f, 11f)
                        verticalLineTo(10f)
                        horizontalLineTo(6f)
                        verticalLineTo(20f)
                        horizontalLineToRelative(5.3f)
                        quadToRelative(0.2f, 0.6f, 0.4f, 1.04f)
                        reflectiveQuadTo(12.25f, 22f)
                        close()
                        moveToRelative(2.21f, -0.46f)
                        quadTo(13f, 20.08f, 13f, 18f)
                        reflectiveQuadToRelative(1.46f, -3.54f)
                        reflectiveQuadTo(18f, 13f)
                        reflectiveQuadToRelative(3.54f, 1.46f)
                        reflectiveQuadTo(23f, 18f)
                        reflectiveQuadToRelative(-1.46f, 3.54f)
                        reflectiveQuadTo(18f, 23f)
                        quadToRelative(-2.07f, 0f, -3.54f, -1.46f)
                        close()
                        moveToRelative(5.19f, -1.19f)
                        lineToRelative(0.7f, -0.7f)
                        lineTo(18.5f, 17.8f)
                        verticalLineTo(15f)
                        horizontalLineToRelative(-1f)
                        verticalLineToRelative(3.2f)
                        lineToRelative(2.15f, 2.15f)
                        close()
                        moveTo(6f, 10f)
                        quadToRelative(0f, 0f, 0f, 1.47f)
                        reflectiveQuadToRelative(0f, 3.29f)
                        reflectiveQuadToRelative(0f, 3.41f)
                        quadTo(6f, 19.77f, 6f, 20f)
                        verticalLineTo(10f)
                        close()
                    }
                }
                .build()
        return _lock_clock!!
    }

private var _lock_clock: ImageVector? = null

@Suppress("CheckReturnValue")
public val info_i: ImageVector
    get() {
        if (_info_i != null) {
            return _info_i!!
        }
        _info_i =
            ImageVector.Builder(
                name = "info_i",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(10.59f, 6.41f)
                        quadTo(10f, 5.82f, 10f, 5f)
                        quadTo(10f, 4.17f, 10.59f, 3.59f)
                        reflectiveQuadTo(12f, 3f)
                        reflectiveQuadToRelative(1.41f, 0.59f)
                        reflectiveQuadTo(14f, 5f)
                        quadToRelative(0f, 0.82f, -0.59f, 1.41f)
                        reflectiveQuadTo(12f, 7f)
                        reflectiveQuadTo(10.59f, 6.41f)
                        close()
                        moveTo(10.5f, 21f)
                        verticalLineTo(9f)
                        horizontalLineToRelative(3f)
                        verticalLineTo(21f)
                        horizontalLineToRelative(-3f)
                        close()
                    }
                }
                .build()
        return _info_i!!
    }

private var _info_i: ImageVector? = null

@Suppress("CheckReturnValue")
public val settings: ImageVector
    get() {
        if (_settings != null) {
            return _settings!!
        }
        _settings =
            ImageVector.Builder(
                name = "settings",
                defaultWidth = 24.dp,
                defaultHeight = 24.dp,
                viewportWidth = 24f,
                viewportHeight = 24f,
            )
                .apply {
                    path(
                        fill = SolidColor(Color.Black),
                        fillAlpha = 1f,
                        stroke = null,
                        strokeAlpha = 1f,
                        strokeLineWidth = 1f,
                        strokeLineCap = StrokeCap.Butt,
                        strokeLineJoin = StrokeJoin.Bevel,
                        strokeLineMiter = 1f,
                        pathFillType = PathFillType.Companion.NonZero,
                    ) {
                        moveTo(9.25f, 22f)
                        lineTo(8.85f, 18.8f)
                        quadTo(8.53f, 18.68f, 8.24f, 18.5f)
                        reflectiveQuadTo(7.68f, 18.13f)
                        lineTo(4.7f, 19.38f)
                        lineTo(1.95f, 14.63f)
                        lineTo(4.53f, 12.68f)
                        quadTo(4.5f, 12.5f, 4.5f, 12.34f)
                        quadToRelative(0f, -0.16f, 0f, -0.34f)
                        reflectiveQuadToRelative(0f, -0.34f)
                        reflectiveQuadTo(4.53f, 11.33f)
                        lineTo(1.95f, 9.38f)
                        lineTo(4.7f, 4.63f)
                        lineTo(7.68f, 5.88f)
                        quadTo(7.95f, 5.68f, 8.25f, 5.5f)
                        reflectiveQuadTo(8.85f, 5.2f)
                        lineTo(9.25f, 2f)
                        horizontalLineToRelative(5.5f)
                        lineToRelative(0.4f, 3.2f)
                        quadToRelative(0.33f, 0.13f, 0.61f, 0.3f)
                        reflectiveQuadToRelative(0.56f, 0.38f)
                        lineTo(19.3f, 4.63f)
                        lineToRelative(2.75f, 4.75f)
                        lineToRelative(-2.57f, 1.95f)
                        quadToRelative(0.02f, 0.18f, 0.02f, 0.34f)
                        reflectiveQuadToRelative(0f, 0.34f)
                        reflectiveQuadToRelative(0f, 0.34f)
                        reflectiveQuadToRelative(-0.05f, 0.34f)
                        lineToRelative(2.57f, 1.95f)
                        lineToRelative(-2.75f, 4.75f)
                        lineTo(16.33f, 18.13f)
                        quadToRelative(-0.27f, 0.2f, -0.57f, 0.38f)
                        reflectiveQuadToRelative(-0.6f, 0.3f)
                        lineTo(14.75f, 22f)
                        horizontalLineTo(9.25f)
                        close()
                        moveTo(11f, 20f)
                        horizontalLineToRelative(1.98f)
                        lineToRelative(0.35f, -2.65f)
                        quadToRelative(0.78f, -0.2f, 1.44f, -0.59f)
                        reflectiveQuadToRelative(1.21f, -0.94f)
                        lineToRelative(2.47f, 1.03f)
                        lineToRelative(0.98f, -1.7f)
                        lineTo(17.28f, 13.52f)
                        quadToRelative(0.13f, -0.35f, 0.17f, -0.74f)
                        reflectiveQuadTo(17.5f, 12f)
                        reflectiveQuadTo(17.45f, 11.21f)
                        quadTo(17.4f, 10.83f, 17.28f, 10.48f)
                        lineTo(19.43f, 8.85f)
                        lineTo(18.45f, 7.15f)
                        lineTo(15.98f, 8.2f)
                        quadTo(15.43f, 7.63f, 14.76f, 7.24f)
                        reflectiveQuadTo(13.33f, 6.65f)
                        lineTo(13f, 4f)
                        horizontalLineTo(11.03f)
                        lineTo(10.68f, 6.65f)
                        quadTo(9.9f, 6.85f, 9.24f, 7.24f)
                        reflectiveQuadTo(8.03f, 8.17f)
                        lineTo(5.55f, 7.15f)
                        lineTo(4.58f, 8.85f)
                        lineToRelative(2.15f, 1.6f)
                        quadTo(6.6f, 10.83f, 6.55f, 11.2f)
                        reflectiveQuadTo(6.5f, 12f)
                        quadToRelative(0f, 0.4f, 0.05f, 0.77f)
                        reflectiveQuadToRelative(0.17f, 0.75f)
                        lineTo(4.58f, 15.15f)
                        lineToRelative(0.98f, 1.7f)
                        lineTo(8.03f, 15.8f)
                        quadToRelative(0.55f, 0.58f, 1.21f, 0.96f)
                        reflectiveQuadToRelative(1.44f, 0.59f)
                        lineTo(11f, 20f)
                        close()
                        moveToRelative(1.05f, -4.5f)
                        quadToRelative(1.45f, 0f, 2.47f, -1.03f)
                        reflectiveQuadTo(15.55f, 12f)
                        reflectiveQuadTo(14.53f, 9.52f)
                        reflectiveQuadTo(12.05f, 8.5f)
                        quadToRelative(-1.47f, 0f, -2.49f, 1.02f)
                        reflectiveQuadTo(8.55f, 12f)
                        reflectiveQuadToRelative(1.01f, 2.47f)
                        reflectiveQuadToRelative(2.49f, 1.03f)
                        close()
                        moveTo(12f, 12f)
                        close()
                    }
                }
                .build()
        return _settings!!
    }

private var _settings: ImageVector? = null

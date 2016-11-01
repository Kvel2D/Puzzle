package com.puzzle

import com.badlogic.gdx.math.Vector2

object Constants {
    const val VIEWPORT_WIDTH = 640
    const val VIEWPORT_HEIGHT = 720
    const val DEGTORAD = 0.0175f
    const val TIME_STEP = 1 / 60f

    val BOARD_ORIGIN = Vector2(250f, 150f)
    const val BOARD_SIZE = 4
    const val SLOT_SIZE = 50f
    const val MOVEMENT_TIME = 0.1f
    const val SQUARE_DISAPPEAR_TIME = 0.4f
    var BUFFER_DROP_FREQUENCY = 3f
}

var overflow = false
var broken = false
var score = 0

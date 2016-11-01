package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.math.Rectangle
import com.puzzle.Constants

class TouchComponent: Component {
    var bounds: Rectangle
    var touched = false

    constructor(x: Int, y: Int, width: Float, height: Float) {
        val xScreen = Constants.BOARD_ORIGIN.x + x * Constants.SLOT_SIZE - Constants.SLOT_SIZE / 2
        val yScreen = Constants.BOARD_ORIGIN.y + y * Constants.SLOT_SIZE - Constants.SLOT_SIZE / 2
        bounds = Rectangle(xScreen, yScreen, width, height)
    }
}

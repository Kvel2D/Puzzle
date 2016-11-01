package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.puzzle.Constants

class ArrowComponent: Component {
    val movementDuration = Constants.MOVEMENT_TIME
    val left: Boolean
    val down: Boolean
    var movementTimer = -1f
    var updated = false

    constructor(direction: String) {
        if (direction == "left") {
            left = true
            down = false
        }
        else if (direction == "down") {
            left = false
            down = true
        }
        else {
            left = false
            down = false
        }
    }
}

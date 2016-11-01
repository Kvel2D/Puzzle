package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class SlotComponent : Component {
    var x: Int
    var y: Int
    var xOffset = 0f
    var yOffset = 0f

    constructor(x: Int, y: Int) {
        this.x = x
        this.y = y
    }
}
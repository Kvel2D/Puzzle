package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.ashley.core.Entity
import com.badlogic.gdx.utils.Pool

class TetherComponent : Component {
    val entity: Entity
    var xOffset: Float
    var yOffset: Float
    var angleOffset: Float
    var rotated: Boolean

    constructor(entity: Entity, xOffset: Float, yOffset: Float, angleOffset: Float, rotated: Boolean) {
        this.entity = entity
        this.xOffset = xOffset
        this.yOffset = yOffset
        this.angleOffset = angleOffset
        this.rotated = rotated
    }
}
package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class TransformComponent : Component {
    var x: Float
    var y: Float
    var angle: Float
    var scale: Float
    var z: Int

    constructor() {
        this.x = 0f
        this.y = 0f
        this.z = 0
        this.angle = 0f
        this.scale = 1f
    }

    constructor(x: Float, y: Float, angle: Float, z: Int, scale: Float) {
        this.x = x
        this.y = y
        this.z = z
        this.angle = angle
        this.scale = scale
    }
}
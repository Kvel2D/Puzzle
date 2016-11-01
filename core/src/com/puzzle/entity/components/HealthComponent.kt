package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class HealthComponent: Component {
    var health:Float
    val healthMax:Float
    var regeneration: Float
    var updated = false

    constructor(health: Float, regeneration:Float) {
        this.health = health
        this.healthMax = health
        this.regeneration = regeneration
    }
}

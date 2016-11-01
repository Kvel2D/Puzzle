package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class FadeComponent : Component {
    val duration: Float
    var currentDuration: Float

    constructor(duration: Float) {
        this.duration = duration
        this.currentDuration = duration
    }
}
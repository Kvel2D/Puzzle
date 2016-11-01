package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class RemoveComponent : Component {
    var timer: Float

    constructor() {
        timer = 0f
    }

    constructor(timer: Float) {
        this.timer = timer
    }
}
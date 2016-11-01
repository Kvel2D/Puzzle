package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class PressAnimationComponent: Component {
    val downFrame: TextureRegion
    val upFrame: TextureRegion
    var updated = true

    constructor(downFrame: TextureRegion, upFrame: TextureRegion) {
        this.downFrame = downFrame
        this.upFrame = upFrame
    }
}

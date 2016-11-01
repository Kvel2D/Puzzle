package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class CrackComponent : Component {
    val frames: Array<TextureRegion>
    var updated = true

    constructor(frames: Array<TextureRegion>) {
        this.frames = frames
    }
}

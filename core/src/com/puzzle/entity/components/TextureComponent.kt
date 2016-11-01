package com.puzzle.entity.components

import com.badlogic.ashley.core.Component
import com.badlogic.gdx.graphics.g2d.TextureRegion

class TextureComponent : Component {
    var region: TextureRegion

    constructor(region: TextureRegion) {
        this.region = region
    }
}
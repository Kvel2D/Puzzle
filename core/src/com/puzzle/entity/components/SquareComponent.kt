package com.puzzle.entity.components

import com.badlogic.ashley.core.Component

class SquareComponent: Component {
    val number: Int

    constructor(number: Int) {
        this.number = number
    }
}

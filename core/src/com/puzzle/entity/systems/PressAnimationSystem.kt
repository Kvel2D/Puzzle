package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.PressAnimationComponent
import com.puzzle.entity.components.TextureComponent
import com.puzzle.entity.components.TouchComponent

class PressAnimationSystem : IteratingSystem {

    constructor() :
    super(Family.all(PressAnimationComponent::class.java, TextureComponent::class.java, TouchComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(PressAnimationComponent::class.java, TextureComponent::class.java, TouchComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val pressAnimationC = Mappers.pressAnimationComponent.get(entity)

        if (!pressAnimationC.updated) {
            pressAnimationC.updated = true

            val textureC = Mappers.textureComponent.get(entity)
            val touchC = Mappers.touchComponent.get(entity)

            textureC.region =
                    if (touchC.touched) pressAnimationC.downFrame
                    else pressAnimationC.upFrame
        }
    }
}


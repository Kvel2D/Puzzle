package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.ColorComponent
import com.puzzle.entity.components.FadeComponent

/*
 * Decrease alpha of color components
 */
class FadeSystem : IteratingSystem {

    constructor() :
    super(Family.all(FadeComponent::class.java, ColorComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(FadeComponent::class.java, ColorComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val fadeC = Mappers.fadeComponent.get(entity)
        val colorC = Mappers.colorComponent.get(entity)

        fadeC.currentDuration -= deltaTime
        colorC.color.a = fadeC.currentDuration / fadeC.duration
    }
}
package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.puzzle.Main
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.RemoveComponent

/*
 * Remove entities(delayed)
 */
class RemovalSystem : IteratingSystem {

    constructor() :
    super(Family.all(RemoveComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(RemoveComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val removeC = Mappers.removeComponent.get(entity)

        removeC.timer -= deltaTime

        if (removeC.timer <= 0f)
            Main.engine.removeEntity(entity)
    }
}

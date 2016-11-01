package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.CrackComponent
import com.puzzle.entity.components.HealthComponent
import com.puzzle.entity.components.TextureComponent

class CrackingSystem : IteratingSystem {

    constructor() :
    super(Family.all(CrackComponent::class.java, TextureComponent::class.java, HealthComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(CrackComponent::class.java, TextureComponent::class.java, HealthComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val crackC = Mappers.crackComponent.get(entity)
        val textureC = Mappers.textureComponent.get(entity)
        val healthC = Mappers.healthComponent.get(entity)

        val healthPoints = healthC.health.toInt()

        // Cracks appear at 10hp and increase until 0
        // HP higher than 10 have no cracks(blank)
        if (0 <= healthPoints && healthPoints <= 10)
            textureC.region = crackC.frames[10 - healthPoints]
        else
            textureC.region = crackC.frames[0]
    }
}
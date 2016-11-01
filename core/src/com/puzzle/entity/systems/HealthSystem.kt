package com.puzzle.entity.systems

import com.badlogic.ashley.core.Entity
import com.badlogic.ashley.core.Family
import com.badlogic.ashley.systems.IteratingSystem
import com.badlogic.gdx.assets.AssetDescriptor
import com.badlogic.gdx.audio.Sound
import com.puzzle.AssetPaths
import com.puzzle.Main
import com.puzzle.entity.Mappers
import com.puzzle.entity.components.*
import com.puzzle.overflow

/*
 * Decrease health when entitity touched, regenerate health, kill entities, play death sounds
 * Kill entities if buffer overflowed
 */
class HealthSystem : IteratingSystem {
    internal val hurtSound: Sound = Main.assets.get(AssetPaths.HURT_SOUND)

    constructor() :
    super(Family.all(HealthComponent::class.java, TouchComponent::class.java).get())

    constructor(priority: Int) :
    super(Family.all(HealthComponent::class.java, TouchComponent::class.java).get(), priority)

    override fun processEntity(entity: Entity, deltaTime: Float) {
        val touchC = Mappers.touchComponent.get(entity)
        val healthC = Mappers.healthComponent.get(entity)

        // After overflow all entities are "broken"
        if (overflow) {
            if (!Mappers.squareComponent.has(entity))
                healthC.health -= 1f
            if (healthC.health < 0f)
                healthC.health = 0f
        }

        // regenerate health
        if (healthC.health != 0f)
            healthC.health += healthC.regeneration
        if (healthC.health > healthC.healthMax)
            healthC.health = healthC.healthMax

        // apply damage from touch event
        if (!healthC.updated && touchC.touched) {
            healthC.updated = true
            healthC.health -= 2f
            hurtSound.play()

            // entity "dies"
            if (healthC.health <= 0f) {
                if (Mappers.touchComponent.has(entity))
                    entity.remove(TouchComponent::class.java)
                if (Mappers.tetherComponent.has(entity)) {
                    val tetherC = Mappers.tetherComponent.get(entity)
                    if (Mappers.squareComponent.has(tetherC.entity) || Mappers.bufferComponent.has(tetherC.entity))
                    {
                        Main.engine.removeEntity(tetherC.entity)
                        Main.engine.removeEntity(entity)
                    }
                }

                healthC.health = 0f
                healthC.regeneration = 0f
            }

        }
    }
}
